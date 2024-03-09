package com.example.Util;

import java.sql.*;
import java.sql.DriverManager;

public class DbConnection {
    private String dburl = "jdbc:postgresql://localhost:5432/postgres";
    private String user = "postgres";
    private String password = "";

    public static Connection connect_to_db() throws Exception {
        Connection conn = null;
        Class.forName("org.postgresql.Driver");
        DbConnection u = new DbConnection();
        try {
            conn = DriverManager.getConnection(u.dburl, u.user, u.password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

}
