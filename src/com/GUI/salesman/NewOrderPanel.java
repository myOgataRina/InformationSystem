package com.GUI.salesman;

import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewOrderPanel extends JPanel {
    private JTextField userIDTextField = new JTextField(20);
    private JTextField goodNameTextField = new JTextField(20);
    private JTextField goodAmountTextField = new JTextField(20);
    private JButton button = new JButton("提交订单");
    private JTable table = new JTable();

    public NewOrderPanel(){
        JLabel userIDLabel = new JLabel("订单收货人：");
        JLabel goodNameLabel = new JLabel("订购商品名称：");
        JLabel goodAmountLabel = new JLabel("订购数量：");

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel containPanel = new JPanel(gb);
        gbc.insets = new Insets(40, 0, 0, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(userIDLabel, gbc);
        containPanel.add(userIDLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(userIDTextField, gbc);
        containPanel.add(userIDTextField);
        gbc.gridwidth = 1;
        gb.setConstraints(goodNameLabel, gbc);
        containPanel.add(goodNameLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(goodNameTextField, gbc);
        containPanel.add(goodNameTextField);
        gbc.gridwidth = 1;
        gb.setConstraints(goodAmountLabel, gbc);
        containPanel.add(goodAmountLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(goodAmountTextField, gbc);
        containPanel.add(goodAmountTextField);
        gbc.insets = new Insets(40, 0, 40, 0);
        gb.setConstraints(button, gbc);
        containPanel.add(button);

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(800, 50));

        Connection connection = SqlControler.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT * FROM good " +
                    "ORDER BY g_id DESC ");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            table = new JTable(resultSetTableModel);
            table.getColumnModel().getColumn(0).setHeaderValue("商品订单");
            table.getColumnModel().getColumn(1).setHeaderValue("商品名称");
            table.getColumnModel().getColumn(2).setHeaderValue("商品库存");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        JScrollPane goodListPanel = new JScrollPane(table);

        this.setLayout(new BorderLayout());
        this.add(containPanel, BorderLayout.NORTH);
        this.add(separator, BorderLayout.CENTER);
        this.add(goodListPanel, BorderLayout.SOUTH);
    }

    public String getUserID(){
        return this.userIDTextField.getText();
    }

    public String getGoodName(){
        return this.goodNameTextField.getText();
    }

    public String getGoodAmount(){
        return this.goodAmountTextField.getText();
    }

    public JTable getTable() {
        return table;
    }

    public void setButtonListener(ActionListener listener){
        this.button.addActionListener(listener);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new NewOrderPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
