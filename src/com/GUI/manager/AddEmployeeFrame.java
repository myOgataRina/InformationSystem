package com.GUI.manager;

import com.main.Client;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddEmployeeFrame extends JFrame {
    private JTextField phoneTextField = new JTextField(20);
    private JTextField addressTextField = new JTextField(20);
    private JTextField userNameTextField = new JTextField(20);
    private JComboBox<String> typeComboBox;

    public AddEmployeeFrame(String u_id) {
        super("添加员工");
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel panel = new JPanel(gb);
        JLabel userNameLabel = new JLabel("员工名：");
        JLabel phoneLabel = new JLabel("联系电话：");
        JLabel addressLabel = new JLabel("员工住址：");
        JButton addButton = new JButton("添加员工");
        JLabel typeLabel = new JLabel("员工类型：");
        final String[] TYPE_OF_USER = new String[]{"经理", "业务员", "仓库管理员", "配送员"};
        typeComboBox = new JComboBox<>(TYPE_OF_USER);

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
        gb.setConstraints(typeLabel, gbc);
        panel.add(typeLabel);
        gbc.insets = new Insets(20, 0, 0, 40);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(typeComboBox, gbc);
        panel.add(typeComboBox);


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
        gb.setConstraints(addButton, gbc);
        panel.add(addButton);

        userNameTextField.setText(u_id);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        this.add(panel);
        this.pack();
        this.setVisible(true);

    }

    private void addEmployee() {
        String userName = userNameTextField.getText();
        String type = (String) typeComboBox.getSelectedItem();
        String phone = phoneTextField.getText();
        String address = addressTextField.getText();
        String power = null;
        if (type.equals("经理")) {
            power = "manager";
        } else if (type.equals("业务员")) {
            power = "salesman";
        } else if (type.equals("仓库管理员")) {
            power = "storekeeper";
        } else if (type.equals("配送员")) {
            power = "distributor";
        }
        if (phone.equals("")) {
            JOptionPane.showMessageDialog(null, "请输入联系电话", "联系电话不能为空", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (address.equals("")) {
            JOptionPane.showMessageDialog(null, "请输入员工住址", "员工住址不能为空", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection connection = SqlControler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO user(u_id , password, power , phone ,address) " +
                    "VALUES (?,'JNU',?,?,?)");
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, power);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            if (preparedStatement.executeUpdate() == 1) {
                //弹出提示
                System.out.println("员工添加成功");
                this.dispose();
                JOptionPane.showMessageDialog(null, "员工添加成功", "注册", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("员工添加失败");
                JOptionPane.showMessageDialog(null, "员工添加失败", "注册", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e1) {
            //增加unique约束时，重复注册用户名会报错。
            e1.printStackTrace();
            System.out.println(e1.getMessage());
            JOptionPane.showMessageDialog(null, e1.getMessage(), "注册", JOptionPane.WARNING_MESSAGE);
        }
    }

}

