package com.util;

import java.sql.*;

public class SqlControler {
    static Connection connection;
    final public static String DBUSER = "root";
    final public static String DBPWD = "root";
    final public static String DB = "jdbc:mysql://localhost:3306/informationsystem";

    //连接数据库
//    public static Connection connectTo(String DB, String name, String pwd) {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection(DB, name, pwd);
//            return connection;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(DB, DBUSER, DBPWD);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("无法获取数据库连接");
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("无法获取数据库连接");
                return null;
            }
            return connection;
        } else{
            return connection;
        }
    }
}
