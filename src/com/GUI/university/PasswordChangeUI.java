package com.GUI.university;

import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PasswordChangeUI {
    private String u_id;
    private static JFrame frame = new JFrame("密码更改界面");
    private static JPanel panel = new JPanel();
    private static JLabel oldPasswordLabel = new JLabel("请输入旧密码:");
    private static JLabel newPasswordLabel = new JLabel("请输入新密码:");
    private static JLabel repeatNewPasswordLabel = new JLabel("请重复新密码:");
    private static JPasswordField oldPasswordTextField = new JPasswordField(20);
    private static JPasswordField newPasswordTextField = new JPasswordField(20);
    private static JPasswordField repeatNewPasswordTextField = new JPasswordField(20);
    private static JButton changePasswordButton = new JButton("更改密码");

    public PasswordChangeUI(String u_id) {
        this.u_id = u_id;
        GridBagLayout gb = new GridBagLayout();
        panel.setLayout(gb);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(50, 20, 20, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(oldPasswordLabel, gbc);
        panel.add(oldPasswordLabel);
        gbc.insets = new Insets(50, 0, 20, 20);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(oldPasswordTextField, gbc);
        panel.add(oldPasswordTextField);
        gbc.insets = new Insets(20, 20, 20, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(newPasswordLabel, gbc);
        panel.add(newPasswordLabel);
        gbc.insets = new Insets(20, 0, 20, 20);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(newPasswordTextField, gbc);
        panel.add(newPasswordTextField);
        gbc.insets = new Insets(20, 20, 20, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(repeatNewPasswordLabel, gbc);
        panel.add(repeatNewPasswordLabel);
        gbc.insets = new Insets(20, 0, 20, 20);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(repeatNewPasswordTextField, gbc);
        panel.add(repeatNewPasswordTextField);
        gbc.insets = new Insets(20, 20, 20, 20);
        gb.setConstraints(changePasswordButton, gbc);
        panel.add(changePasswordButton);
        //changePasswordButton添加功能
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldPassword = new String(oldPasswordTextField.getPassword());
                String newPassword = new String(newPasswordTextField.getPassword());
                String newPassword2 = new String(repeatNewPasswordTextField.getPassword());
                if(newPassword.equals(newPassword2)){
                    PasswordChangeUI.changePassword(u_id, oldPassword, newPassword);
                } else {
                    System.out.println("两次输入的新密码不一致");
                    JOptionPane.showMessageDialog(null, "两次输入的新密码不一致", "警告", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void changePassword(String userID, String oldPassword, String newPassword) {
        Connection connection = SqlControler.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "UPDATE user " +
                    "SET password = ? " +
                    "WHERE u_id = ? AND password = ?");
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, userID);
            preparedStatement.setString(3, oldPassword);
            int i = preparedStatement.executeUpdate();
            if(i==0){
                System.out.println("旧密码错误，重置密码失败，请重新再试");
                JOptionPane.showMessageDialog(null, "旧密码错误，重置密码失败，请重新再试", "错误", JOptionPane.ERROR_MESSAGE);
            }
            System.out.println("更新了" + i + "条记录");
            JOptionPane.showMessageDialog(null, "更新了" + i + "条记录", "更新成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
