package com.GUI.customer;

import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddMyOrderPanel extends JPanel {
    private JTextField goodNameTextField = new JTextField(20);
    private JTextField goodAmountTextField = new JTextField(20);
    private JButton button = new JButton("提交订单");
    private JTable table = new JTable();

    public AddMyOrderPanel() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel containerPanel = new JPanel(gb);
        JLabel goodNameLabel = new JLabel("订购商品名称");
        JLabel goodAmountLabel = new JLabel("订购商品数量");
        gbc.insets = new Insets(40, 0, 0, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(goodNameLabel, gbc);
        containerPanel.add(goodNameLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(goodNameTextField, gbc);
        containerPanel.add(goodNameTextField);
        gbc.gridwidth = 1;
        gb.setConstraints(goodAmountLabel, gbc);
        containerPanel.add(goodAmountLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(goodAmountTextField, gbc);
        containerPanel.add(goodAmountTextField);
        gbc.insets = new Insets(40, 0, 40, 0);
        gb.setConstraints(button, gbc);
        containerPanel.add(button);

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(800, 50));

        Connection connection = SqlControler.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT * FROM good " +
                    "ORDER BY g_id");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            table = new JTable(resultSetTableModel);
            table.getColumnModel().getColumn(0).setHeaderValue("商品编号");
            table.getColumnModel().getColumn(1).setHeaderValue("商品名称");
            table.getColumnModel().getColumn(2).setHeaderValue("商品库存");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JScrollPane goodListScrollPane = new JScrollPane(table);

        this.setLayout(new BorderLayout());
        this.add(containerPanel, BorderLayout.NORTH);
        this.add(separator, BorderLayout.CENTER);
        this.add(goodListScrollPane, BorderLayout.SOUTH);
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setButtonListener(ActionListener listener) {
        this.button.addActionListener(listener);
    }

    public JTable getTable() {
        return table;
    }

    public String getGoodName(){
        return this.goodNameTextField.getText();
    }

    public String getGoodAmount(){
        return this.goodAmountTextField.getText();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        AddMyOrderPanel addMyOrderPanel = new AddMyOrderPanel();
        frame.add(addMyOrderPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
