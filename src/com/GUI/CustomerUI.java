package com.GUI;

import com.main.Client;
import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CustomerUI extends OperationUI {
    private static PreparedStatement statement;
    private static ResultSet resultSet;

    private static JFrame frame;
    private static JTabbedPane tabbedPane;
    private static JPanel myOrderPanel;
    private static JPanel newOrderPanel;
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
    private static JTextField goodNameTextField;
    private JLabel amountLabel;
    private static JTextField amountTextField;
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
        myOrderQueryPanel = new JScrollPane();

        //新建订单初始化
//        submitNewOrderPanel = new JPanel();
//        goodNameLabel = new JLabel("商品名称：");
//        goodNameTextField = new JTextField(20);
//        amountLabel = new JLabel("订购数量：");
//        amountTextField = new JTextField(20);

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

        //按钮添加功能
        submitNewOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerUI.addOrder();
                CustomerUI.refreshGoodList();
                CustomerUI.refreshMyOrderList();
            }
        });
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
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int i = tabbedPane.getSelectedIndex();
                if(i==0){
                    CustomerUI.refreshMyOrderList();
                }
                if(i==1){
                    CustomerUI.refreshGoodList();
                }
            }
        });
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

    }

    public static void searchMyTable(String para) {
        System.out.println("searchMyTable.");
        if (para.equals("")) {
//            myOrderQueryPanel.remove(queryTable);
//            Connection connection = SqlControler.getConnection();
//            try {
//                PreparedStatement preparedStatement = connection.prepareStatement(
//                        "SELECT o_id , m_order.amount , m_order.g_id , g_name , o_time , status " +
//                                "FROM m_order , good " +
//                                "WHERE u_id=?  AND m_order.g_id=good.g_id");
//                preparedStatement.setString(1, Client.u_id);
//                resultSet = preparedStatement.executeQuery();
//                resultSetTableModel = new ResultSetTableModel(resultSet);
//                queryTable = new JTable(resultSetTableModel);
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
            CustomerUI.refreshMyOrderList();
        } else {
            CustomerUI.searchMyOrder();
        }

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

    public static void addOrder() {
        System.out.println("addOrder");
        Connection connection = SqlControler.getConnection();

        String goodName;//订购商品名称
        int orderAmount;//订购数量
        int goodID;//商品ID
        int goodAmount;//商品库存

        goodName = goodNameTextField.getText();
        try {
            orderAmount = Integer.parseInt(amountTextField.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("数量输入非数字");
            orderAmount = -1;
        }
        if (orderAmount < 1) {
            System.out.println("请输入大于0的数字");
        } else {
            try {
                System.out.println("goodName = " + goodName);
                statement = connection.prepareStatement("SELECT g_id , amount FROM good " +
                        "WHERE g_name = ?");
                statement.setString(1, goodName);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    //通过商品名字查询是否有该种商品
                    System.out.println("查询到商品");
                    goodID = resultSet.getInt(1);
                    goodAmount = resultSet.getInt(2);
                    System.out.println("goodID:" + goodID + ", goodAmount:" + goodAmount);
                    if (orderAmount > goodAmount) {
                        System.out.println(goodName + "库存不足，无法提交订单");
                    } else {
                        //更新新订单到数据库
                        statement = connection.prepareStatement("INSERT INTO m_order(amount , g_id , u_id , o_time) " +
                                "VALUES( ? , ? , ? , ?)");
                        System.out.println("orderAmount:" + orderAmount + ", goodID:" + goodID + ", u_id:" + Client.u_id);
                        statement.setInt(1, orderAmount);
                        statement.setInt(2, goodID);
                        statement.setString(3, Client.u_id);
                        statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                        int changedCount = statement.executeUpdate();
                        System.out.println("成功添加" + changedCount + "个订单");

                        //更新商品库存
                        goodAmount = goodAmount - orderAmount;
                        statement = connection.prepareStatement("UPDATE good " +
                                "SET amount = ? " +
                                "WHERE g_id = ? AND g_name = ?");
                        statement.setInt(1, goodAmount);
                        statement.setInt(2, goodID);
                        statement.setString(3, goodName);
                        changedCount = statement.executeUpdate();
                        System.out.println("更新了" + changedCount + "条库存记录");
                    }
                } else {
                    System.out.println("查询不到商品");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void refreshGoodList() {
        Connection connection = SqlControler.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM good ");
            resultSet = preparedStatement.executeQuery();
            resultSetTableModel = new ResultSetTableModel(resultSet);
            queryTable = new JTable(resultSetTableModel);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        newOrderQueryPanel = new JScrollPane(queryTable);
        newOrderPanel.add(newOrderQueryPanel, BorderLayout.SOUTH);
        tabbedPane.repaint();
        frame.repaint();
    }

    public static void refreshMyOrderList() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        myOrderQueryPanel = new JScrollPane(queryTable);
        myOrderPanel.add(myOrderQueryPanel, BorderLayout.SOUTH);
        tabbedPane.repaint();
        frame.repaint();
    }

    public static void searchMyOrder() {
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
            System.out.println("添加容器");
            myOrderQueryPanel = new JScrollPane(queryTable);
            myOrderPanel.add(myOrderQueryPanel, BorderLayout.SOUTH);
//                myOrderQueryPanel.repaint();
//                myOrderPanel.repaint();
            tabbedPane.repaint();
            frame.repaint();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void run() {
        CustomerUI UI = new CustomerUI();
        UI.init();
        searchMyTable(searchField.getText());
    }


}

