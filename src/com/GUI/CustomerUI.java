package com.GUI;

import com.main.Client;
import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CustomerUI extends OperationUI {
    private static PreparedStatement statement;
    private static ResultSet resultSet;

    private static JFrame frame;
    private JTabbedPane tabbedPane;
    private static JPanel myOrderPanel;
    private JPanel newOrderPanel;
    private static ResultSetTableModel resultSetTableModel;
    private static JTable queryTable;

    //我的订单页面
    private JPanel searchPanel;
    private JPanel combinePanel;
    private JLabel searchString;
    private static JTextField searchField;
    private static JScrollPane myOrderQueryPanel;

    private JButton queryButton;

    //新建订单页面
    private JPanel submitNewOrderPanel;
    private JPanel submitPanel;
    private JPanel namePanel;
    private JPanel amountPanel;
    private JLabel goodNameLabel;
    private JTextField goodNameTextField;
    private JLabel amountLabel;
    private JTextField amountTextField;
    private JButton submitNewOrderButton;
    private static JScrollPane newOrderQueryPanel = new JScrollPane();

    @Override
    public void init() {
        frame = new JFrame("客户界面");
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        myOrderPanel = new JPanel();
        newOrderPanel = new JPanel();

        //我的订单标签初始化
        searchPanel = new JPanel();
        combinePanel = new JPanel();
        searchString = new JLabel("订单商品编号或名称：");
        searchField = new JTextField(20);
        queryButton = new JButton("查询");
        submitNewOrderPanel = new JPanel();
        goodNameLabel = new JLabel("商品名称：");
        goodNameTextField = new JTextField(20);
        amountLabel = new JLabel("订购数量：");
        amountTextField = new JTextField(20);
        submitNewOrderButton = new JButton("提交新订单");
        myOrderQueryPanel = new JScrollPane();

        //查询按键绑定监听
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerUI.searchMyTable(searchField.getText());
            }
        });

        //给searchPanel设置GridBagLayout
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;

        combinePanel.setLayout(new BorderLayout());
        combinePanel.add(searchString, BorderLayout.WEST);
        combinePanel.add(searchField, BorderLayout.CENTER);


        searchPanel.setLayout(gb);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(50, 20, 0, 20);
        gb.setConstraints(combinePanel, gbc);
        searchPanel.add(combinePanel);
        gb.setConstraints(queryButton, gbc);
        searchPanel.add(queryButton);
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(800, 50));
        gbc.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(separator, gbc);
        searchPanel.add(separator);

        myOrderPanel.setLayout(new BorderLayout());
        myOrderPanel.add(searchPanel, BorderLayout.CENTER);
        myOrderPanel.add(myOrderQueryPanel, BorderLayout.SOUTH);

        {
            queryTable = new JTable();
            myOrderQueryPanel = new JScrollPane(queryTable);
            showMyTableInNewOrder();
        }
        //新建订单标签初始化
        submitNewOrderPanel = new JPanel();
        namePanel = new JPanel(new BorderLayout());
        amountPanel = new JPanel(new BorderLayout());
        submitPanel = new JPanel(new BorderLayout());
        goodNameLabel = new JLabel("订购商品名称：");
        goodNameTextField = new JTextField(20);
        amountLabel = new JLabel("订购数量：");
        amountTextField = new JTextField(20);
        submitNewOrderButton = new JButton("提交订单");

        //GridBagLayout装载标签和文本框
        JPanel submitPanel2 = new JPanel(gb);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        gb.setConstraints(goodNameLabel, gbc);
        submitPanel2.add(goodNameLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(goodNameTextField, gbc);
        submitPanel2.add(goodNameTextField);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gb.setConstraints(amountLabel, gbc);
        submitPanel2.add(amountLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(amountTextField, gbc);
        submitPanel2.add(amountTextField);

        submitPanel.add(submitPanel2);

        submitNewOrderPanel.setLayout(gb);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(50, 0, 0, 0);

        gb.setConstraints(submitPanel, gbc);
        submitNewOrderPanel.add(submitPanel);
        gb.setConstraints(submitNewOrderButton, gbc);
        submitNewOrderPanel.add(submitNewOrderButton);


        JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
        separator2.setPreferredSize(new Dimension(800, 50));
        gbc.insets = new Insets(10, 0, 10, 0);
        gb.setConstraints(separator2, gbc);
        submitNewOrderPanel.add(separator2);


        newOrderPanel.setLayout(new BorderLayout());
        newOrderPanel.add(submitNewOrderPanel, BorderLayout.CENTER);
        newOrderPanel.add(newOrderQueryPanel, BorderLayout.SOUTH);

        //添加到tabbedPane
        tabbedPane.add("我的订单", myOrderPanel);
        tabbedPane.add("新建订单", newOrderPanel);
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

    }

    public static void searchMyTable(String para) {
        System.out.println("searchMyTable.");
        if (para.equals("")) {
            myOrderQueryPanel.remove(queryTable);
            Connection connection = SqlControler.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT o_id , m_order.amount , m_order.g_id , g_name , o_time , status " +
                                "FROM m_order , good " +
                                "WHERE u_id=?  AND m_order.g_id=good.g_id");
                preparedStatement.setString(1, Client.u_id);
                resultSet = preparedStatement.executeQuery();
                resultSetTableModel = new ResultSetTableModel(resultSet);
                queryTable = new JTable(resultSetTableModel);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } else {
            System.out.println("删除容器");
//            myOrderPanel.remove(myOrderQueryPanel);
            myOrderQueryPanel.remove(queryTable);
            System.out.println("查询");
            Connection connection = SqlControler.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT o_id , m_order.amount , m_order.g_id , g_name , o_time , status " +
                                "FROM m_order , good " +
                                "WHERE u_id=? AND m_order.g_id=good.g_id AND (g_name=? OR  m_order.g_id=?)");
                preparedStatement.setString(1, Client.u_id);
                preparedStatement.setString(2, searchField.getText());
                try {
                    preparedStatement.setInt(3, Integer.valueOf(searchField.getText()));
                } catch (NumberFormatException e2) {
                    e2.printStackTrace();
                    preparedStatement.setInt(3, 0);
                }
                resultSet = preparedStatement.executeQuery();
                resultSetTableModel = new ResultSetTableModel(resultSet);
                queryTable = new JTable(resultSetTableModel);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        System.out.println("添加容器");
        myOrderQueryPanel = new JScrollPane(queryTable);
        myOrderPanel.add(myOrderQueryPanel, BorderLayout.SOUTH);
//                myOrderQueryPanel.repaint();
//                myOrderPanel.repaint();
        frame.repaint();
    }

    public static void showMyTableInNewOrder() {
        Connection connection = SqlControler.getConnection();
        try {
            statement = connection.prepareStatement(
                    "SELECT * " +
                            "FROM good ");
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resultSetTableModel = new ResultSetTableModel(resultSet);
        queryTable = new JTable(resultSetTableModel);
        newOrderQueryPanel = new JScrollPane(queryTable);
    }

    public static void addOrder(){
        Connection connection = SqlControler.getConnection();
        try {
            statement = connection.prepareStatement("" +
                    "insert into m_order(amount,)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void listGoods(){

    }

    public void run() {
        CustomerUI UI = new CustomerUI();
        UI.init();
        searchMyTable(searchField.getText());
    }


}

