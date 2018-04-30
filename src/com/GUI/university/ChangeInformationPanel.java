package com.GUI.university;

import com.main.Client;
import com.object.User;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangeInformationPanel extends JPanel {
    private JLabel u_idLabel = new JLabel("目前登陆的用户：" + Client.u_id);
    ;
    private JLabel phoneLabel = new JLabel("电话：");
    private JLabel addressLabel = new JLabel("地址：");
    private JTextField phoneTextField = new JTextField(20);
    private JTextField addressTextField = new JTextField(20);
    private JButton changePasswordButton = new JButton("更改密码");
    private JButton changeInformationButton = new JButton("更新信息");

    public ChangeInformationPanel() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(gb);
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(u_idLabel, gbc);
        this.add(u_idLabel);
        gbc.gridwidth = 1;
        gb.setConstraints(phoneLabel, gbc);
        this.add(phoneLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(phoneTextField, gbc);
        this.add(phoneTextField);
        gbc.gridwidth = 1;
        gb.setConstraints(addressLabel, gbc);
        this.add(addressLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(addressTextField, gbc);
        this.add(addressTextField);

        gb.setConstraints(changePasswordButton, gbc);
        this.add(changePasswordButton);
        gb.setConstraints(changeInformationButton, gbc);
        this.add(changeInformationButton);

        User user = SqlControler.getUser(Client.u_id);
        if (user != null) {
            u_idLabel = new JLabel("目前登陆的用户：" + Client.u_id);
            phoneTextField.setText(user.getPhone());
            addressTextField.setText(user.getAddress());
        }
    }

    //给按键绑定功能
    public void setChangePasswordButtonListener(ActionListener listener ) {
        changePasswordButton.addActionListener(listener);
    }
    public void setChangePasswordButtonListener( ) {
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasswordChangeUI passwordChangeUI = new PasswordChangeUI(Client.u_id);
            }
        });
    }
    public void setChangeInformationButtonListener(ActionListener listener) {
        changeInformationButton.addActionListener(listener);
    }
    public void setChangeInformationButtonListener() {
        changeInformationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = SqlControler.getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("" +
                            "UPDATE user " +
                            "SET phone = ?, address = ? " +
                            "WHERE u_id = ?");
                    preparedStatement.setString(1, new String(phoneTextField.getText()));
                    preparedStatement.setString(2, new String(addressTextField.getText()));
                    preparedStatement.setString(3, Client.u_id);

                    int i = preparedStatement.executeUpdate();
                    if (i == 0) {
                        System.out.println("资料更新失败");
                    } else {
                        System.out.println("更新" + i + "条记录");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        ChangeInformationPanel changeInformationPanel = new ChangeInformationPanel();
        JFrame jFrame = new JFrame();
        jFrame.add(changeInformationPanel);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
