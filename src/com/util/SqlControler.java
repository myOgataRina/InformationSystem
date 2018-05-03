package com.util;

import com.object.User;

import javax.swing.*;
import java.sql.*;

public class SqlControler {
    static Connection connection = null;
    final private static String DBUSER = "root";
    final private static String DBPWD = "root";
    final private static String DB = "jdbc:mysql://45.32.38.78:3306/informationsystem";


    //连接数据库
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(DB, DBUSER, DBPWD);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("无法获取数据库连接");
                JOptionPane.showMessageDialog(null, "无法获取数据库连接", "错误", JOptionPane.ERROR_MESSAGE);
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("无法获取数据库连接");
                JOptionPane.showMessageDialog(null, "无法获取数据库连接", "错误", JOptionPane.ERROR_MESSAGE);
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
            preparedStatement.setString(1, u_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
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

    public static class Storehouse {
        public static void entry(int g_id, int r_amount, float price, String r_name, String r_phone, String u_id) throws SQLException {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO exit_and_entry( g_id , r_amount , price , r_name , r_phone , ExitOrEntry , u_id , r_time) " +
                    "VALUES(? , ? , ? , ? , ? , 'Entry' , ? , ? )");
            preparedStatement.setInt(1, g_id);
            preparedStatement.setInt(2, r_amount);
            preparedStatement.setFloat(3, price);
            preparedStatement.setString(4, r_name);
            preparedStatement.setString(5, r_phone);
            preparedStatement.setString(6, u_id);
            preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
        }

        public static void exit(int g_id, int r_amount, float price, String r_name, String r_phone, String u_id) throws SQLException {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO exit_and_entry( g_id , r_amount , price , r_name , r_phone , ExitOrEntry , u_id , r_time) " +
                    "VALUES(? , ? , ? , ? , ? , 'Exit' , ? , ? )");
            preparedStatement.setInt(1, g_id);
            preparedStatement.setInt(2, r_amount);
            preparedStatement.setFloat(3, price);
            preparedStatement.setString(4, r_name);
            preparedStatement.setString(5, r_phone);
            preparedStatement.setString(6, u_id);
            preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
        }
    }
}
