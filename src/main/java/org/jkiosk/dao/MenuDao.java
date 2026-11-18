package org.jkiosk.dao;

import org.jkiosk.db.DBUtil;
import org.jkiosk.model.Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// menu 테이블을 읽는 코드를 모아둔 클래스
public class MenuDao {

    // 카테고리별 메뉴 목록 (카테고리 탭 누를 때 사용)
    public List<Menu> findByCategory(int categoryId) throws SQLException {
        String sql = "SELECT menu_id, category_id, name, price, sold_out "
                + "FROM menu WHERE category_id = ? ORDER BY menu_id";
        List<Menu> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Menu(
                            rs.getInt("menu_id"),
                            rs.getInt("category_id"),
                            rs.getString("name"),
                            rs.getInt("price"),
                            rs.getBoolean("sold_out")));
                }
            }
        }
        return list;
    }
}