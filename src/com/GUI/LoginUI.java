package com.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentListener;

public class LoginUI {
    private JFrame loginFrame = new JFrame("登陆界面");
    private ImageIcon picture = new ImageIcon("Resource/login.jpg");
    private GridBagConstraints gbc = new GridBagConstraints();
    private GridBagLayout gb = new GridBagLayout();
    private JPanel loginArea = new JPanel(gb);
    private JPanel accountArea = new JPanel();
    private JLabel accountLabel = new JLabel("账  号");
    private JTextField accountTF = new JTextField(20);
    private JPanel passwordArea = new JPanel();
    private JLabel passwordLabel = new JLabel("密  码");
    private JPasswordField passwordTF = new JPasswordField(20);
    private JPanel typeArea = new JPanel();
    private JLabel typeLabel = new JLabel("类  型");
    final private String[] TYPE_OF_USER = new String[]{"客户","部门经理","员工"};
    private JComboBox<String> type = new JComboBox<>(TYPE_OF_USER);
    private String selectedType;
    private JPanel buttonArea = new JPanel();
    private JButton loginButton = new JButton("登录");

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

        //按钮绑定监听器
        LoginListener ll = new LoginListener();
        loginButton.addActionListener(ll);

        //帐号密码输入区初始化
        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.ipadx = 20;
//        gbc.ipady = 20;
        gbc.insets = new Insets(30, 10, 0, 10);

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

        gbc.insets = new Insets(30, 10, 0, 10);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gb.setConstraints(passwordArea, gbc);
        loginArea.add(passwordArea);

        //选择类型区域
        typeArea.add(typeLabel,BorderLayout.WEST);
        //加入ComboBox
        type.setMaximumRowCount(3);
        type.setSelectedIndex(0);
        type.addActionListener(e -> {
            selectedType = (String)type.getSelectedItem();
            System.out.println(selectedType);
        });
        typeArea.add(type);

        gbc.insets = new Insets(20, 10, 0, 10);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gb.setConstraints(typeArea, gbc);
        loginArea.add(typeArea);

        //加入button
        gbc.insets = new Insets(20, 10, 30, 10);
        gbc.gridx = 1;
        gbc.gridy = 7;
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

    //内部类实现监听事件
    private class LoginListener extends ComponentAdapter implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //此处添加监听事件响应
            System.out.println("e = [" + e + "]");
        }

    }
}

