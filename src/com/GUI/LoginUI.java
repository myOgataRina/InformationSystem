package com.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.main.Client;
import com.util.*;

public class LoginUI {
//    final private String DBUSER = "root";
//    final private String DBPWD = "root";

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
    final private String[] TYPE_OF_USER = new String[]{"客户", "部门经理", "员工"};
    private JComboBox<String> type = new JComboBox<>(TYPE_OF_USER);
    private String selectedType = "客户";
    private JPanel buttonArea = new JPanel();
    private JButton loginButton = new JButton("登录");
    private JButton signUpButton = new JButton("注册");
    private Connection connection = SqlControler.getConnection();

    public void init() {
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
        SignUpListener sul = new SignUpListener();
        signUpButton.addActionListener(sul);

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
        typeArea.add(typeLabel, BorderLayout.WEST);
        //加入ComboBox
        type.setMaximumRowCount(3);
        type.setSelectedIndex(0);
        type.addActionListener(e -> {
            selectedType = (String) type.getSelectedItem();
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
        buttonArea.add(loginButton, BorderLayout.WEST);
        buttonArea.add(signUpButton, BorderLayout.EAST);
        //borderLayout自动居中，解决按键居中问题
        gb.setConstraints(buttonArea, gbc);
        loginArea.add(buttonArea);

        //界面初始化
        loginFrame.add(new JLabel(picture), BorderLayout.WEST);
        loginFrame.add(loginArea, BorderLayout.CENTER);
        loginFrame.pack();
        loginFrame.setVisible(true);


    }

    public static void main() {
        LoginUI loginUI = new LoginUI();
        loginUI.init();
    }

    //内部类实现监听事件
    private class LoginListener extends ComponentAdapter implements ActionListener {
        public void actionPerformed(ActionEvent e) throws RuntimeException {
            //此处添加监听事件响应
            System.out.println("登录中");
            Client.u_id = accountTF.getText();
            Client.password = new String(passwordTF.getPassword());
            System.out.println("u_id = " + Client.u_id + ", password = " + Client.password + ".");


            PreparedStatement preparedStatement = null;
            try {
                if (selectedType.equals("客户")) {
                    preparedStatement = connection.prepareStatement("SELECT * FROM USER WHERE u_id=? AND password=? AND power='customer'");
                    preparedStatement.setString(1, Client.u_id);
                    preparedStatement.setString(2, Client.password);
                } else if (selectedType.equals("部门经理")) {
                    preparedStatement = connection.prepareStatement("SELECT * FROM USER WHERE u_id=? AND password=? AND power='manager'");
                    preparedStatement.setString(1, Client.u_id);
                    preparedStatement.setString(2, Client.password);
                } else if (selectedType.equals("员工")) {
                    preparedStatement = connection.prepareStatement("SELECT * FROM USER WHERE u_id=? AND password=? AND power='employee'");
                    preparedStatement.setString(1, Client.u_id);
                    preparedStatement.setString(2, Client.password);
                } else {
                    System.out.println("没选择对象");
                    throw new RuntimeException();
                }

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    //登录成功
                    System.out.println("登录成功");
                    OperationUI operationUI;
                    if (selectedType.equals("客户")) {
                        operationUI = new CustomerUI();
                    } else if (selectedType.equals("部门经理")) {
                        operationUI = new ManagerUI();
                    } else if (selectedType.equals("员工")) {
                        operationUI = new EmployeeUI();
                    } else {
                        operationUI = new OperationUI();
                    }
                    operationUI.run();

                } else {
                    //登录失败
                    System.out.println("登录失败，账户名或密码错误，请重新再试。");
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (RuntimeException e2) {
                e2.printStackTrace();
                System.out.println("类型选择错误");
            }

        }
    }

    private class SignUpListener extends ComponentAdapter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("注册中");
            selectedType = (String) type.getSelectedItem();
            if (selectedType.equals("客户")) {
                try {
                    PreparedStatement preparedStatement = null;
                    preparedStatement = connection.prepareStatement("INSERT INTO user(u_id , password, power) VALUES (?,?,\"customer\")");
                    Client.u_id = accountTF.getText();
                    Client.password = new String(passwordTF.getPassword());
                    preparedStatement.setString(1, Client.u_id);
                    preparedStatement.setString(2, Client.password);
                    if (preparedStatement.executeUpdate() == 1) {
                        //弹出提示
                        System.out.println("用户添加成功");
                    } else {
                        System.out.println("用户添加失败");
                    }
                } catch (SQLException e1) {
                    //增加unique约束时，重复注册用户名会报错。
                    e1.printStackTrace();
                    System.out.println(e1.getMessage());
                }

            } else {
                //弹出窗口显示非客户不能注册
                System.out.println("注册权限不足，非客户用户请与总部联系。");
            }
        }
    }
}

