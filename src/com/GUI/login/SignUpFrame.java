package com.GUI.login;

import com.main.Client;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpFrame extends JFrame {
    private JPasswordField passwordField = new JPasswordField(20);
    private JPasswordField repeatPasswordField = new JPasswordField(20);
    private JTextField phoneTextField = new JTextField(20);
    private JTextField addressTextField = new JTextField(20);
    private JTextField userNameTextField = new JTextField(20);

    public SignUpFrame() {
        super("顾客账号注册");
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel panel = new JPanel(gb);
        JLabel userNameLabel = new JLabel("账号：");
        JLabel passwordLabel = new JLabel("密码：");
        JLabel repeatPasswordLabel = new JLabel("重复密码：");
        JLabel phoneLabel = new JLabel("联系电话：");
        JLabel addressLabel = new JLabel("收货地址：");
        JButton button = new JButton("确认注册");

        gbc.insets = new Insets(40, 40, 0, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(userNameLabel, gbc);
        panel.add(userNameLabel);
        gbc.insets = new Insets(40, 0, 0, 40);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(userNameTextField, gbc);
        panel.add(userNameTextField);

        gbc.insets = new Insets(20, 40, 0, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(passwordLabel, gbc);
        panel.add(passwordLabel);
        gbc.insets = new Insets(20, 0, 0, 40);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(passwordField, gbc);
        panel.add(passwordField);

        gbc.insets = new Insets(20, 40, 0, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(repeatPasswordLabel, gbc);
        panel.add(repeatPasswordLabel);
        gbc.insets = new Insets(20, 0, 0, 40);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(repeatPasswordField, gbc);
        panel.add(repeatPasswordField);

        gbc.insets = new Insets(20, 40, 0, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(phoneLabel, gbc);
        panel.add(phoneLabel);
        gbc.insets = new Insets(20, 0, 0, 40);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(phoneTextField, gbc);
        panel.add(phoneTextField);

        gbc.insets = new Insets(20, 40, 0, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(addressLabel, gbc);
        panel.add(addressLabel);
        gbc.insets = new Insets(20, 0, 0, 40);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(addressTextField, gbc);
        panel.add(addressTextField);

        gbc.insets = new Insets(20, 40, 40, 40);
        gb.setConstraints(button, gbc);
        panel.add(button);

        userNameTextField.setText(Client.u_id);
        passwordField.setText(Client.password);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });

        this.add(panel);
        this.pack();
        this.setVisible(true);
    }

    private void signUp() {
        String userName = userNameTextField.getText();
        String password = new String(passwordField.getPassword());
        String repeatPassword = new String(repeatPasswordField.getPassword());
        String phone = phoneTextField.getText();
        String address = addressTextField.getText();
        if (password.equals("")) {
            JOptionPane.showMessageDialog(null, "请输入密码", "密码不能为空", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (phone.equals("")) {
            JOptionPane.showMessageDialog(null, "请输入联系电话", "联系电话不能为空", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (address.equals("")) {
            JOptionPane.showMessageDialog(null, "请输入收货地址", "收货地址不能为空", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (password.equals(repeatPassword)) {
            try {
                Connection connection = SqlControler.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("" +
                        "INSERT INTO user(u_id , password, power , phone ,address) " +
                        "VALUES (?,?,\"customer\",?,?)");
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, phone);
                preparedStatement.setString(4, address);
                if (preparedStatement.executeUpdate() == 1) {
                    //弹出提示
                    System.out.println("用户添加成功");
                    this.setVisible(false);
                    JOptionPane.showMessageDialog(null, "用户添加成功", "注册", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    System.out.println("用户添加失败");
                    JOptionPane.showMessageDialog(null, "用户添加失败", "注册", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e1) {
                //增加unique约束时，重复注册用户名会报错。
                e1.printStackTrace();
                System.out.println(e1.getMessage());
                JOptionPane.showMessageDialog(null, e1.getMessage(), "注册", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "两次输入的密码不一致，请重新输入", "密码不一致", JOptionPane.ERROR_MESSAGE);
        }
    }
}
