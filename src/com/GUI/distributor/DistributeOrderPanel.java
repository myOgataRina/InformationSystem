package com.GUI.distributor;

import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DistributeOrderPanel extends JPanel {
    private JTextField orderTextField = new JTextField(20);
    private JTextField userTextField = new JTextField(20);
    private JTextField goodTextField = new JTextField(20);
    private JButton searchButton = new JButton("查询");
    private JButton shippingButton = new JButton("订单出货");

    private JTable jTable = new JTable();


    public DistributeOrderPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new BorderLayout());
        gbc.insets = new Insets(40, 0, 0, 0);
        gbc.gridwidth = 1;
        JLabel orderLabel = new JLabel("订单编号：");
        GridBagLayout gb = new GridBagLayout();
        gb.setConstraints(orderLabel, gbc);
        JPanel containerPanel = new JPanel(gb);
        containerPanel.add(orderLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(orderTextField, gbc);
        containerPanel.add(orderTextField);
        gbc.gridwidth = 1;
        JLabel userLabel = new JLabel("订单用户：");
        gb.setConstraints(userLabel, gbc);
        containerPanel.add(userLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(userTextField, gbc);
        containerPanel.add(userTextField);
        gbc.gridwidth = 1;
        JLabel goodLabel = new JLabel("商品名称：");
        gb.setConstraints(goodLabel, gbc);
        containerPanel.add(goodLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(goodTextField, gbc);
        containerPanel.add(goodTextField);
        gbc.insets = new Insets(40, 20, 0, 20);
        gbc.gridwidth = 1;
        gb.setConstraints(searchButton, gbc);
        JPanel buttonPanel = new JPanel(gb);
        buttonPanel.add(searchButton);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(shippingButton, gbc);
        buttonPanel.add(shippingButton);
        gbc.insets = new Insets(0, 0, 20, 0);
        gb.setConstraints(buttonPanel, gbc);
        containerPanel.add(buttonPanel);


        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(800, 50));

        Connection connection = SqlControler.getConnection();
        JScrollPane confirmOrderPane = new JScrollPane();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , exit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已出仓' AND m_order.g_id=good.g_id " +
                    "ORDER BY o_id");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            jTable.setModel(resultSetTableModel);
            jTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            jTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
            jTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
            jTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
            jTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
            jTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
            jTable.getColumnModel().getColumn(6).setHeaderValue("订单出仓时间");
            confirmOrderPane = new JScrollPane(jTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        this.add(containerPanel, BorderLayout.NORTH);
        this.add(separator, BorderLayout.CENTER);
        this.add(confirmOrderPane, BorderLayout.SOUTH);
    }

    public void setSearchButtonListener(ActionListener actionListener) {
        this.searchButton.addActionListener(actionListener);
    }

    public void setShippingButtonListener(ActionListener actionListener) {
        this.shippingButton.addActionListener(actionListener);
    }

    public String getO_id() {
        return this.orderTextField.getText();
    }

    public String getU_id() {
        return this.userTextField.getText();
    }

    public String getGoodName() {
        return this.goodTextField.getText();
    }

    public JTable getTable() {
        return jTable;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        DistributeOrderPanel distributeOrderPanel = new DistributeOrderPanel();
        frame.add(distributeOrderPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
