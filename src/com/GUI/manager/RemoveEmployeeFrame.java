package com.GUI.manager;

import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveEmployeeFrame extends JFrame {
    public RemoveEmployeeFrame(String u_id) throws SQLException {
        super("员工开除");
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT u_id , power_cn , phone , address " +
                "FROM user , translate " +
                "WHERE u_id = ? AND power=power_en AND power IN ('manager','salesman','storekeeper','distributor') ");
        preparedStatement.setString(1, u_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String power = resultSet.getString("power_cn");
            String phone = resultSet.getString("phone");
            String address = resultSet.getString("address");
            JLabel userNameLabel = new JLabel("员工名字：");
            JLabel userNameLabel1 = new JLabel(u_id);
            JLabel powerLabel = new JLabel("员工类型：");
            JLabel powerLabel1 = new JLabel(power);
            JLabel phoneLabel = new JLabel("联系方式：");
            JLabel phoneLabel1 = new JLabel(phone);
            JLabel addressLabel = new JLabel("员工住址：");
            JLabel addressLabel1 = new JLabel(address);
            GridBagLayout gb = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            JPanel panel = new JPanel(gb);

            JButton yesButton = new JButton("是");
            JButton noButton = new JButton("否");

            yesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Connection connection1 = SqlControler.getConnection();
                    try {
                        PreparedStatement preparedStatement1 = connection1.prepareStatement("" +
                                "DELETE FROM user " +
                                "WHERE u_id=? ");
                        preparedStatement1.setString(1, u_id);
                        int i = preparedStatement1.executeUpdate();
                        if (i <= 0) {
                            JOptionPane.showMessageDialog(null, "开除员工失败，请稍后再试", "开除失败", JOptionPane.ERROR_MESSAGE);
                        } else {
                            dispose();
                            JOptionPane.showMessageDialog(null, "员工已开除", "开除成功", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                }
            });

            noButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            JLabel askLabel = new JLabel("请确认该员工的详细信息，确认要开除该员工吗?");
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(40, 10, 0, 10);
            gb.setConstraints(askLabel, gbc);
            panel.add(askLabel);

            gbc.insets = new Insets(20, 20, 0, 20);
            gbc.gridwidth = 1;
            gb.setConstraints(userNameLabel, gbc);
            panel.add(userNameLabel);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gb.setConstraints(userNameLabel1, gbc);
            panel.add(userNameLabel1);

            gbc.gridwidth = 1;
            gbc.insets = new Insets(20, 20, 0, 20);
            gb.setConstraints(powerLabel, gbc);
            panel.add(powerLabel);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gb.setConstraints(powerLabel1, gbc);
            panel.add(powerLabel1);

            gbc.gridwidth = 1;
            gb.setConstraints(phoneLabel, gbc);
            panel.add(phoneLabel);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gb.setConstraints(phoneLabel1, gbc);
            panel.add(phoneLabel1);

            gbc.gridwidth = 1;
            gb.setConstraints(addressLabel, gbc);
            panel.add(addressLabel);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gb.setConstraints(addressLabel1, gbc);
            panel.add(addressLabel1);

            gbc.insets = new Insets(20, 20, 20, 20);
            gbc.gridwidth = 1;
            gb.setConstraints(yesButton, gbc);
            panel.add(yesButton);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gb.setConstraints(noButton, gbc);
            panel.add(noButton);
            this.add(panel);
            this.pack();
            this.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "员工查询错误，请确认员工名是否输入错误", "员工名错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
