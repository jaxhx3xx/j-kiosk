package org.jkiosk.dao;

import org.jkiosk.db.DBUtil;
import org.jkiosk.model.Cart;
import org.jkiosk.model.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// orders, order_item 테이블에 주문을 저장하는 코드
public class OrderDao {

    // 주문 저장: orders 1줄 + order_item 여러 줄
    // 둘 중 하나라도 실패하면 둘 다 취소되도록 "트랜잭션"으로 묶음
    // 저장된 주문 번호(order_id)를 돌려줌
    public int save(String orderType, String payMethod, Cart cart) throws SQLException {
        String orderSql = "INSERT INTO orders (order_type, pay_method) VALUES (?, ?)";
        String itemSql = "INSERT INTO order_item "
                + "(order_id, menu_id, temp, size, shot, syrup, quantity, price) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection()) {

            // 트랜잭션 시작: commit()을 부르기 전까지는 DB에 확정되지 않음
            conn.setAutoCommit(false);

            try {
                // ----- 1. orders에 저장하고, 자동으로 만들어진 order_id 받아오기 -----
                int orderId;
                try (PreparedStatement ps = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, orderType);  // "STORE" 또는 "TAKEOUT"
                    ps.setString(2, payMethod);  // "CARD" 또는 "CASH"
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        orderId = keys.getInt(1);
                    }
                }

                // ----- 2. 장바구니 한 줄마다 order_item 저장 -----
                try (PreparedStatement ps = conn.prepareStatement(itemSql)) {
                    for (CartItem item : cart.getItems()) {
                        ps.setInt(1, orderId);
                        ps.setInt(2, item.getMenu().getMenuId());
                        ps.setString(3, item.getTemp());    // 디저트는 null → DB에 NULL로 저장됨
                        ps.setString(4, item.getSize());
                        ps.setInt(5, item.getShot());
                        ps.setString(6, item.getSyrup());
                        ps.setInt(7, item.getQuantity());
                        ps.setInt(8, item.getUnitPrice());  // 옵션 포함 1개 가격 (주문 당시 가격)
                        ps.executeUpdate();
                    }
                }

                // ----- 3. 전부 성공하면 확정 -----
                conn.commit();
                return orderId;

            } catch (SQLException e) {
                // 하나라도 실패하면 지금까지 저장한 것도 전부 취소
                conn.rollback();
                throw e;
            }
        }
    }

    // 대기번호: 오늘 들어온 주문 중 몇 번째인지 계산 (예: 27번째 → "A-027")
    public String getWaitingNumber(int orderId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders "
                + "WHERE DATE(order_time) = CURDATE() AND order_id <= ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                int number = rs.getInt(1);
                return String.format("A-%03d", number);  // 숫자를 3자리로 (27 → 027)
            }
        }
    }
}