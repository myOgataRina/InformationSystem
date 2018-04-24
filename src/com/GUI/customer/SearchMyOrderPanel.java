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

public class SearchMyOrderPanel extends JPanel {
    private JTextField searchTextField = new JTextField(20);
    private JButton button = new JButton("查询");
    private JTable table = new JTable();

    public SearchMyOrderPanel() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();


        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(gb);
        JLabel searchLabel = new JLabel("订单商品编号或商品名称：");
        gbc.gridwidth = 1;
        gbc.insets = new Insets(40, 0, 0, 0);
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
                    "SELECT o_id , m_order.g_id , g_name , m_order.amount , status " +
                    "FROM m_order , good " +
                    "WHERE u_id=? AND m_order.g_id=good.g_id " +
                    "ORDER BY o_id");
            preparedStatement.setString(1, Client.u_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            table = new JTable(resultSetTableModel);
            table.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
            table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
            table.getColumnModel().getColumn(3).setHeaderValue("订单商品数量");
            table.getColumnModel().getColumn(4).setHeaderValue("订单状态");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JScrollPane orderScrollPane = new JScrollPane(table);

        this.setLayout(new BorderLayout());
        this.add(containerPanel, BorderLayout.NORTH);
        this.add(separator, BorderLayout.CENTER);
        this.add(orderScrollPane, BorderLayout.SOUTH);
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setButtonListener(ActionListener listener) {
        button.addActionListener(listener);
    }

    public JTable getTable() {
        return table;
    }

    public String getSearchText(){
        return this.searchTextField.getText();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new SearchMyOrderPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
