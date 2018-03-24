package com.util;

import java.sql.*;

public class SqlControler {
    Connection connection;

    //连接数据库
    public Connection connectTo(String DB, String name, String pwd) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB, name, pwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return connection;
        }
    }
}
