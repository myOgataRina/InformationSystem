package com.util;

import com.object.User;

import java.sql.*;

public class SqlControler {
    static Connection connection;
    final public static String DBUSER = "root";
    final public static String DBPWD = "root";
    //    final public static String DB = "jdbc:mysql://localhost:3306/informationsystem";
    final public static String DB = "jdbc:mysql://155.94.163.237:3306/informationsystem";


    //连接数据库
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
        } else {
            return connection;
        }
    }

    //从数据库获取用户信息，查无此人则返回NULL
    public static User getUser(String u_id) {
        User user = null;
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM user " +
                    "WHERE u_id = ?");
            preparedStatement.setString(1,u_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                user = new User();
                user.setU_id(resultSet.getString("u_id"));
                user.setPassword(resultSet.getString("password"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
