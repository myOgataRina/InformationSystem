package com.GUI.customer;

import com.main.Client;
import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReceiptPanel extends JPanel {
    private JTextField orderIDTextField = new JTextField(20);
    private JTextField goodNameTextField = new JTextField(20);
    private JButton receiptButton = new JButton("签收订单");
    private JButton queryButton = new JButton("订单查询");
    private JTable table = new JTable();
    public ReceiptPanel(){
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel containerPanel = new JPanel(gb);
        JLabel orderIDLabel = new JLabel("订单编号：");
        JLabel goodNameLabel = new JLabel("商品名称：");
        gbc.insets = new Insets(40, 0, 0, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(orderIDLabel, gbc);
        containerPanel.add(orderIDLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(orderIDTextField, gbc);
        containerPanel.add(orderIDTextField);
        gbc.gridwidth = 1;
        gb.setConstraints(goodNameLabel, gbc);
        containerPanel.add(goodNameLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(goodNameTextField, gbc);
        containerPanel.add(goodNameTextField);
        gbc.insets = new Insets(40, 0, 40, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(queryButton, gbc);
        containerPanel.add(queryButton);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(receiptButton, gbc);
        containerPanel.add(receiptButton);


        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(800, 50));

        Connection connection = SqlControler.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , m_order.g_id , g_name , m_order.amount , submit_time , confirm_time , exit_time , ship_time , status " +
                    "FROM m_order , good " +
                    "WHERE m_order.g_id=good.g_id AND status=? AND u_id=? " +
                    "ORDER BY o_id");
            preparedStatement.setString(1,"订单配送中");
            preparedStatement.setString(2, Client.u_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            table = new JTable(resultSetTableModel);
            table.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
            table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
            table.getColumnModel().getColumn(3).setHeaderValue("订单数量");
            table.getColumnModel().getColumn(4).setHeaderValue("订单提交时间");
            table.getColumnModel().getColumn(5).setHeaderValue("订单确认时间");
            table.getColumnModel().getColumn(6).setHeaderValue("订单出仓时间");
            table.getColumnModel().getColumn(7).setHeaderValue("订单派送时间");
            table.getColumnModel().getColumn(8).setHeaderValue("订单状态");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JScrollPane receiptPane = new JScrollPane(table);

        this.setLayout(new BorderLayout());
        this.add(containerPanel, BorderLayout.NORTH);
        this.add(separator, BorderLayout.CENTER);
        this.add(receiptPane, BorderLayout.SOUTH);
    }

    public String getOrderID(){
        return new String(orderIDTextField.getText());
    }

    public String getGoodName(){
        return new String(goodNameTextField.getText());
    }

    public JTable getTable() {
        return table;
    }

    public void setReceiptButtonListener(ActionListener listener){
        receiptButton.addActionListener(listener);
    }

    public void setQueryButtonListener(ActionListener listener){
        queryButton.addActionListener(listener);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        ReceiptPanel receiptPanel = new ReceiptPanel();
        frame.add(receiptPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
