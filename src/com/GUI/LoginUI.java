package com.GUI;

import javax.swing.*;
import java.awt.*;

public class LoginUI {
    private JFrame loginFrame = new JFrame("登陆界面");
    private JPanel buttonArea = new JPanel();
    private ImageIcon picture = new ImageIcon("Resource/login.jpg");
    private GridBagLayout gb = new GridBagLayout();
    private JPanel loginArea = new JPanel(gb);
    private JPanel accountArea = new JPanel();
    private JPanel passwordArea = new JPanel();
    private JButton loginButton = new JButton("登录");
    private JLabel accountLabel = new JLabel("账号");
    private JLabel passwordLabel = new JLabel("密码");
    private JTextField accountTF = new JTextField(20);
    private JPasswordField passwordTF = new JPasswordField(20);
    private GridBagConstraints gbc = new GridBagConstraints();

    private void init() {
//        pictureArea.setBounds(200,200,200,200);

        //账号输入栏
        accountArea.setLayout(new BorderLayout());
        accountArea.add(accountLabel, BorderLayout.WEST);
        accountArea.add(accountTF, BorderLayout.CENTER);

        //密码输入栏
        passwordArea.setLayout(new BorderLayout());
        passwordArea.add(passwordLabel, BorderLayout.WEST);
        passwordArea.add(passwordTF, BorderLayout.CENTER);

        //帐号密码输入区初始化
        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.ipadx = 20;
//        gbc.ipady = 20;
        gbc.insets = new Insets(30, 10, 30, 10);

//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 3;
//        gbc.gridheight = 7;
//        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gb.setConstraints(accountArea, gbc);
        loginArea.add(accountArea);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gb.setConstraints(passwordArea, gbc);
        loginArea.add(passwordArea);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        buttonArea.add(loginButton);
        //borderLayout自动居中，解决按键居中问题
        gb.setConstraints(buttonArea, gbc);
        loginArea.add(buttonArea);

        //界面初始化
        loginFrame.add(new JLabel(picture), BorderLayout.WEST);
        loginFrame.add(loginArea, BorderLayout.CENTER);
        loginFrame.pack();
        loginFrame.setVisible(true);

    }

    public static void main(String[] args) {
        LoginUI loginUI = new LoginUI();
        loginUI.init();
    }
}
