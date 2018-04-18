package com.object;

import com.util.SqlControler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Storehouse {
    public static void entry(int g_id, String u_id) throws SQLException {
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "INSERT INTO exit_and_entry(g_id, ExitOrEntry, u_id, r_time) " +
                "VALUES(? , 'Entry' , ? , ? )");
        preparedStatement.setInt(1, g_id);
        preparedStatement.setString(2, u_id);
        preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        preparedStatement.executeUpdate();
    }

    public static void exit(int g_id, String u_id) throws SQLException {
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "INSERT INTO exit_and_entry(g_id, ExitOrEntry, u_id, r_time) " +
                "VALUES(? , 'Exit' , ? , ?)");
        preparedStatement.setInt(1, g_id);
        preparedStatement.setString(2, u_id);
        preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        preparedStatement.executeUpdate();
    }
}
