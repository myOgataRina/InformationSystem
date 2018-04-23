package com.GUI.storekeeper;

import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EntryOrExitPanel extends JPanel {
    private JTextField goodNameTextField = new JTextField(50);
    private JTextField goodAmountTextField = new JTextField(20);
    private JTextField goodPriceTextField = new JTextField(20);
    private JTextField contactTextField = new JTextField(20);
    private JTextField contactPhoneTextField = new JTextField(20);
    private JButton button;
    private JTable table = new JTable();
    ResultSetTableModel resultSetTableModel;

    public EntryOrExitPanel(boolean isEntry) {
        if (isEntry) {
            button = new JButton("确认入库");
        } else {
            button = new JButton("确认出库");
        }
        JPanel containerPanel = new JPanel();
        JLabel goodNameLabel = new JLabel("商品名称：");
        JLabel goodAmountLabel = new JLabel("商品数量：");
        JLabel goodPriceLabel = new JLabel("商品价格：");
        JLabel contactLabel = new JLabel("联系人：");
        JLabel contactPhoneLabel = new JLabel("联系方式：");


        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        containerPanel.setLayout(gb);
        gbc.insets = new Insets(30, 10, 0, 10);
        gbc.gridwidth = 1;
        gb.setConstraints(goodNameLabel, gbc);
        containerPanel.add(goodNameLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(goodNameTextField, gbc);
        containerPanel.add(goodNameTextField);
        gbc.gridwidth = 1;

        gb.setConstraints(goodAmountLabel, gbc);
        containerPanel.add(goodAmountLabel);
        gb.setConstraints(goodAmountTextField, gbc);
        containerPanel.add(goodAmountTextField);
        gb.setConstraints(goodPriceLabel, gbc);
        containerPanel.add(goodPriceLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(goodPriceTextField, gbc);
        containerPanel.add(goodPriceTextField);
        gbc.gridwidth = 1;
        gb.setConstraints(contactLabel, gbc);
        containerPanel.add(contactLabel);

        gb.setConstraints(contactTextField, gbc);
        containerPanel.add(contactTextField);
        gb.setConstraints(contactPhoneLabel, gbc);
        containerPanel.add(contactPhoneLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(contactPhoneTextField, gbc);
        containerPanel.add(contactPhoneTextField);
        gbc.insets = new Insets(30, 10, 40, 10);
        gb.setConstraints(button, gbc);
        containerPanel.add(button);

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(800, 50));

        ResultSet resultSet;
        JScrollPane jScrollPane = new JScrollPane();
        Connection connection = SqlControler.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT * FROM good");
            resultSet = preparedStatement.executeQuery();
            resultSetTableModel = new ResultSetTableModel(resultSet);
            table = new JTable(resultSetTableModel);
            table.getColumnModel().getColumn(0).setHeaderValue("商品编号");
            table.getColumnModel().getColumn(1).setHeaderValue("商品名称");
            table.getColumnModel().getColumn(2).setHeaderValue("库存");
            jScrollPane = new JScrollPane(table);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        this.setLayout(new BorderLayout());
        this.add(containerPanel, BorderLayout.NORTH);
        this.add(separator, BorderLayout.CENTER);
        this.add(jScrollPane, BorderLayout.SOUTH);
    }

    public void setButtonListener(ActionListener listener) {
        button.addActionListener(listener);
    }

    public String getGoodName() {
        return goodNameTextField.getText();
    }

    public String getGoodAmount() {
        return goodAmountTextField.getText();
    }

    public String getPrice() {
        return goodPriceTextField.getText();
    }

    public String getContact() {
        return contactTextField.getText();
    }

    public String getContactPhone() {
        return contactPhoneTextField.getText();
    }

    public JTable getTable() {
        return table;
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        EntryOrExitPanel entryOrExitPanel = new EntryOrExitPanel(true);
        jFrame.add(entryOrExitPanel);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}

