package com.GUI.storekeeper;

import com.GUI.salesman.ConfirmationPanel;
import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShippingPanel extends JPanel {
    private GridBagLayout gb = new GridBagLayout();
    private JPanel containerPanel = new JPanel(new BorderLayout());
    private JPanel searchPanel = new JPanel(gb);
    private JLabel orderLabel = new JLabel("订单编号");
    private JLabel userLabel = new JLabel("订单用户");
    private JLabel goodLabel = new JLabel("商品名称");
    private JTextField orderTextField = new JTextField(20);
    private JTextField userTextField = new JTextField(20);
    private JTextField goodTextField = new JTextField(20);
    private JPanel buttonPanel = new JPanel(gb);
    private JButton searchButton = new JButton("查询");
    private JButton shippingButton = new JButton("订单出货");

    private JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
    private JTable jTable = new JTable();
    private JScrollPane confirmOrderPane = new JScrollPane();


    public ShippingPanel(){
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new BorderLayout());
        gbc.insets = new Insets(40, 0, 0, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(orderLabel, gbc);
        searchPanel.add(orderLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(orderTextField, gbc);
        searchPanel.add(orderTextField);
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.gridwidth = 1;
        gb.setConstraints(userLabel, gbc);
        searchPanel.add(userLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(userTextField, gbc);
        searchPanel.add(userTextField);
        gbc.gridwidth = 1;
        gb.setConstraints(goodLabel, gbc);
        searchPanel.add(goodLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(goodTextField, gbc);
        searchPanel.add(goodTextField);
        gbc.insets = new Insets(20, 20, 0, 20);
        gbc.gridwidth = 1;
        gb.setConstraints(searchButton, gbc);
        buttonPanel.add(searchButton);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(shippingButton, gbc);
        buttonPanel.add(shippingButton);
        gbc.insets = new Insets(0, 0, 20, 0);
        gb.setConstraints(buttonPanel, gbc);
        searchPanel.add(buttonPanel);
        containerPanel.add(searchPanel);

        separator.setPreferredSize(new Dimension(800, 50));

        Connection connection = SqlControler.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            jTable.setModel(resultSetTableModel);
            jTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            jTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
            jTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
            jTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
            jTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
            jTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
            jTable.getColumnModel().getColumn(6).setHeaderValue("订单确认时间");
            confirmOrderPane = new JScrollPane(jTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        gbc.insets = new Insets(20, 20, 0, 20);
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
//        gb.setConstraints(containerPanel, gbc);
        this.add(containerPanel, BorderLayout.NORTH);
//        gb.setConstraints(separator, gbc);
        this.add(separator, BorderLayout.CENTER);
//        gb.setConstraints(confirmOrderPane, gbc);
        this.add(confirmOrderPane, BorderLayout.SOUTH);
    }

//    public static void main(String[] args) {
//        JFrame jFrame = new JFrame();
//        ShippingPanel shippingPanel= new ShippingPanel();
//        shippingPanel.setSearchButtonListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("listener");
//            }
//        });
//        jFrame.add(shippingPanel);
//        jFrame.pack();
//        jFrame.setVisible(true);
//
//    }

    public void setSearchButtonListener(ActionListener actionListener){
        this.searchButton.addActionListener(actionListener);
    }

    public void setShippingButtonListener(ActionListener actionListener){
        this.shippingButton.addActionListener(actionListener);
    }

    public String getO_id(){
        return new String(this.orderTextField.getText());
    }

    public String getU_id(){
        return new String(this.userTextField.getText());
    }

    public String getGoodName(){
        return new String(this.goodTextField.getText());
    }

    public void changeConfirmedOrderPanel(JScrollPane newSubmittedOrderPane){
        confirmOrderPane = newSubmittedOrderPane;
        this.repaint();
    }

    public JTable getTable(){
        return jTable;
    }
}
