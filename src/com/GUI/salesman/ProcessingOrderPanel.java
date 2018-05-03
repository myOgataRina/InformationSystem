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

public class ProcessingOrderPanel extends JPanel {
    private JTextField userIDTextField = new JTextField(20);
    private JTextField searchTextField = new JTextField(20);
    private JButton button = new JButton("查询");
    private JTable table = new JTable();

    public ProcessingOrderPanel(){
        JLabel userIDLabel = new JLabel("订单收货人：");
        JLabel searchLabel = new JLabel("订单商品编号或商品名称：");

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel containerPanel = new JPanel(gb);
        gbc.insets = new Insets(40, 0, 0, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(userIDLabel, gbc);
        containerPanel.add(userIDLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(userIDTextField, gbc);
        containerPanel.add(userIDTextField);
        gbc.gridwidth = 1;
        gb.setConstraints(searchLabel, gbc);
        containerPanel.add(searchLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(searchTextField, gbc);
        containerPanel.add(searchTextField);
        gbc.insets = new Insets(40, 0, 40, 0);
        gb.setConstraints(button, gbc);
        containerPanel.add(button);

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(800, 50));

        Connection connection = SqlControler.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , m_order.g_id , g_name , m_order.amount , u_id , submit_time , confirm_time , ship_time , receipt_time , status " +
                    "FROM m_order , good " +
                    "WHERE m_order.g_id=good.g_id " +
                    "ORDER BY o_id");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            table = new JTable(resultSetTableModel);
            table.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
            table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
            table.getColumnModel().getColumn(3).setHeaderValue("订单数量");
            table.getColumnModel().getColumn(4).setHeaderValue("订单用户");
            table.getColumnModel().getColumn(5).setHeaderValue("提交时间");
            table.getColumnModel().getColumn(6).setHeaderValue("确认时间");
            table.getColumnModel().getColumn(7).setHeaderValue("出仓时间");
            table.getColumnModel().getColumn(8).setHeaderValue("收货时间");
            table.getColumnModel().getColumn(9).setHeaderValue("订单状态");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JScrollPane orderPanel = new JScrollPane(table);

        this.setLayout(new BorderLayout());
        this.add(containerPanel, BorderLayout.NORTH);
        this.add(separator, BorderLayout.CENTER);
        this.add(orderPanel, BorderLayout.SOUTH);
    }

    public String getUserID(){
        return this.userIDTextField.getText();
    }

    public String getSearchText(){
        return this.searchTextField.getText();
    }

    public void setButtonListener(ActionListener listener){
        this.button.addActionListener(listener);
    }

    public JTable getTable(){
        return table;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new ProcessingOrderPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
