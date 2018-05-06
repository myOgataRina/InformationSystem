package com.GUI.manager;

import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministrateEmployeePanel extends JPanel {
    private JTextField userNameTextField = new JTextField(25);
    private JComboBox<String> typeComboBox;
    private JTable table;
    private JButton searchButton = new JButton("查询");
    private JButton adjustButton = new JButton("岗位调动");
    private JButton addButton = new JButton("添加员工");
    private JButton removeButton = new JButton("开除员工");
    public AdministrateEmployeePanel() {
        JLabel userNameLabel = new JLabel("员工名：");
        JLabel typeLabel = new JLabel("员工类型：");
        final String[] TYPE_OF_USER = new String[]{"全体员工", "经理", "业务员", "仓库管理员", "配送员"};
        typeComboBox = new JComboBox<>(TYPE_OF_USER);

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel containerPanel = new JPanel(gb);
        gbc.insets = new Insets(40, 10, 0, 10);
        gbc.gridwidth = 1;
        gb.setConstraints(userNameLabel, gbc);
        containerPanel.add(userNameLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(userNameTextField, gbc);
        containerPanel.add(userNameTextField);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 10, 0, 10);
        gb.setConstraints(typeLabel, gbc);
        containerPanel.add(typeLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(typeComboBox, gbc);
        containerPanel.add(typeComboBox);
        gbc.insets = new Insets(20, 10, 40, 10);
        gbc.gridwidth = 1;
        gb.setConstraints(searchButton, gbc);
        containerPanel.add(searchButton);
        gb.setConstraints(addButton, gbc);
        containerPanel.add(addButton);
        gb.setConstraints(removeButton, gbc);
        containerPanel.add(removeButton);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(adjustButton, gbc);
        containerPanel.add(adjustButton);

//        JPanel containerPanel1 = new JPanel(new BorderLayout());
//        containerPanel1.add(searchButton);
//        containerPanel1.add(adjustButton, BorderLayout.EAST);
//        gb.setConstraints(containerPanel1, gbc);
//        containerPanel.add(containerPanel1);

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(800, 50));

        String sql = "" +
                "SELECT u_id , power_cn , phone " +
                "FROM user , translate " +
                "WHERE power=power_en ";
        Connection connection = SqlControler.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql+"AND power IN ('manager','salesman','storekeeper','distributor') ");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            table = new JTable(resultSetTableModel);
            table.getColumnModel().getColumn(0).setHeaderValue("员工名");
            table.getColumnModel().getColumn(1).setHeaderValue("岗位");
            table.getColumnModel().getColumn(2).setHeaderValue("联系电话");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JScrollPane scrollPane = new JScrollPane(table);

        this.setLayout(new BorderLayout());
        this.add(containerPanel, BorderLayout.NORTH);
        this.add(separator, BorderLayout.CENTER);
        this.add(scrollPane, BorderLayout.SOUTH);
    }

    public String getSelectedType(){
        return (String) typeComboBox.getSelectedItem();
    }

    public String getUserName(){
        return userNameTextField.getText();
    }

    public JTable getTable() {
        return table;
    }

    public void setSearchButtonListener(ActionListener actionListener){
        searchButton.addActionListener(actionListener);
    }

    public void setAdjustButtonListener(ActionListener adjustButtonListener){
        adjustButton.addActionListener(adjustButtonListener);
    }

    public void setAddButtonListener(ActionListener addButtonListener){
        addButton.addActionListener(addButtonListener);
    }

    public void setRemoveButtonListener(ActionListener removeButtonListener) {
        removeButton.addActionListener(removeButtonListener);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new AdministrateEmployeePanel());
        frame.pack();
        frame.setVisible(true);
    }
}
