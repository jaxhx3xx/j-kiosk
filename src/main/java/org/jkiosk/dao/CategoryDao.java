package org.jkiosk.dao;

import org.jkiosk.db.DBUtil;
import org.jkiosk.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// category 테이블을 읽는 코드
public class CategoryDao {

    // 카테고리 전체 (탭 만들 때 사용)
    public List<Category> findAll() throws SQLException {
        String sql = "SELECT category_id, name FROM category ORDER BY category_id";
        List<Category> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("category_id"),
                        rs.getString("name")));
            }
        }
        return list;
    }
}