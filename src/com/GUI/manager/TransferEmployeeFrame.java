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

public class TransferEmployeeFrame extends JFrame {
    final private String[] TYPE_OF_USER = new String[]{"经理", "业务员", "仓库管理员", "配送员"};
    private JComboBox<String> typeComboBox = new JComboBox<>(TYPE_OF_USER);

    public TransferEmployeeFrame(String u_id) throws SQLException {
        super("岗位调动");
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

            typeComboBox.setSelectedItem(power);

            JLabel userNameLabel = new JLabel("员工名字：");
            JLabel userNameLabel1 = new JLabel(u_id);
            JLabel powerLabel = new JLabel("员工类型：");
            JLabel phoneLabel = new JLabel("联系方式：");
            JLabel phoneLabel1 = new JLabel(phone);
            JLabel addressLabel = new JLabel("员工住址：");
            JLabel addressLabel1 = new JLabel(address);
            GridBagLayout gb = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            JPanel panel = new JPanel(gb);

            JButton yesButton = new JButton("调岗");
            JButton noButton = new JButton("取消");

            yesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Connection connection1 = SqlControler.getConnection();
                    try {
                        PreparedStatement preparedStatement1 = connection1.prepareStatement("" +
                                "UPDATE user " +
                                "SET power=? " +
                                "WHERE u_id=? ");
                        if (((String) typeComboBox.getSelectedItem()).equals("经理")) {
                            preparedStatement1.setString(1, "manager");
                        } else if (((String) typeComboBox.getSelectedItem()).equals("业务员")) {
                            preparedStatement1.setString(1, "salesman");
                        } else if (((String) typeComboBox.getSelectedItem()).equals("仓库管理员")) {
                            preparedStatement1.setString(1, "storekeeper");
                        } else if (((String) typeComboBox.getSelectedItem()).equals("配送员")) {
                            preparedStatement1.setString(1, "distributor");
                        }
                        preparedStatement1.setString(2, u_id);
                        int i = preparedStatement1.executeUpdate();
                        if (i <= 0) {
                            JOptionPane.showMessageDialog(null, "调岗失败，请稍后再试", "调岗失败", JOptionPane.ERROR_MESSAGE);
                        } else {
                            dispose();
                            JOptionPane.showMessageDialog(null, "员工已成功调岗", "调岗成功", JOptionPane.INFORMATION_MESSAGE);
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

            JLabel askLabel = new JLabel("请在下方选择框内，为该员工分配岗位。");
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
            gb.setConstraints(typeComboBox, gbc);
            panel.add(typeComboBox);

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
