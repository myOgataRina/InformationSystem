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

public class StoreEntryOrExitListPanel extends JPanel {
    private JTable table = null;
    private JTextField IDTextField = new JTextField(20);
    private JTextField goodNameTextField = new JTextField(20);
    private JTextField contactPersonTextField = new JTextField(20);
    private JButton button = new JButton("查询记录");

    public StoreEntryOrExitListPanel() {
        JLabel IDLabel = new JLabel("出入库记录编号：");
        JLabel goodNameLabel = new JLabel("商品名称：");
        JLabel contactPersonLabel = new JLabel("联系人：");

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JPanel containPanel = new JPanel(gb);
        gridBagConstraints.insets = new Insets(40, 0, 0, 0);
        gridBagConstraints.gridwidth = 1;
        gb.setConstraints(IDLabel, gridBagConstraints);
        containPanel.add(IDLabel);
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(IDTextField, gridBagConstraints);
        containPanel.add(IDTextField);
        gridBagConstraints.gridwidth = 1;
        gb.setConstraints(goodNameLabel, gridBagConstraints);
        containPanel.add(goodNameLabel);
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(goodNameTextField, gridBagConstraints);
        containPanel.add(goodNameTextField);
        gridBagConstraints.gridwidth = 1;
        gb.setConstraints(contactPersonLabel, gridBagConstraints);
        containPanel.add(contactPersonLabel);
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(contactPersonTextField, gridBagConstraints);
        containPanel.add(contactPersonTextField);
        gridBagConstraints.insets = new Insets(40, 0, 40, 0);
        gb.setConstraints(button, gridBagConstraints);
        containPanel.add(button);

        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(800, 50));

        Connection connection = SqlControler.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT r_id , exit_and_entry.g_id , g_name , r_amount , price , r_name , r_phone , ExitOrEntry ,u_id , r_time FROM exit_and_entry , good " +
                    "WHERE exit_and_entry.g_id = good.g_id " +
                    "ORDER BY r_id DESC ");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            table = new JTable(resultSetTableModel);
            table.getColumnModel().getColumn(0).setHeaderValue("出入库记录编号");
            table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
            table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
            table.getColumnModel().getColumn(3).setHeaderValue("出入库数量");
            table.getColumnModel().getColumn(4).setHeaderValue("出入库价格");
            table.getColumnModel().getColumn(5).setHeaderValue("联系人");
            table.getColumnModel().getColumn(6).setHeaderValue("联系人联系方式");
            table.getColumnModel().getColumn(7).setHeaderValue("出/入库");
            table.getColumnModel().getColumn(8).setHeaderValue("经手人");
            table.getColumnModel().getColumn(9).setHeaderValue("操作时间");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        JScrollPane scrollPane = new JScrollPane(table);

        this.setLayout(new BorderLayout());
        this.add(containPanel, BorderLayout.NORTH);
        this.add(separator, BorderLayout.CENTER);
        this.add(scrollPane, BorderLayout.SOUTH);
    }

    public String getID() {
        return IDTextField.getText();
    }

    public String getGoodName() {
        return goodNameTextField.getText();
    }

    public String getContactPerson() {
        return contactPersonTextField.getText();
    }

    public void setButtonListener(ActionListener listener) {
        this.button.addActionListener(listener);
    }

    public JTable getTable() {
        return this.table;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        StoreEntryOrExitListPanel storeEntryOrExitListPanel = new StoreEntryOrExitListPanel();
        frame.add(storeEntryOrExitListPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
