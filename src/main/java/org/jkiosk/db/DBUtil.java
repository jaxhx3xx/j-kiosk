package org.jkiosk.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static final Properties props = new Properties();

    // 프로그램 시작 시 db.properties를 한 번 읽어둠
    static {
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("db.properties를 찾을 수 없어요 (src/main/resources 확인)");
            }
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("db.properties 읽기 실패", e);
        }
    }

    // DB 연결을 하나 만들어서 돌려줌
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));
    }
}