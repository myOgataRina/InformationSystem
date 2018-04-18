package com.GUI.salesman;

import com.GUI.OperationUI;
import com.main.Client;
import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SalesmanUI extends OperationUI {
    private static PreparedStatement statement;
    private static ResultSet resultSet;

    private static JFrame frame;
    private static JTabbedPane tabbedPane;
    private static JPanel orderPanel;
    private static JPanel newOrderPanel;
    private static ResultSetTableModel resultSetTableModel;
    private static JTable queryTable;

    //进行中的订单页面
    private JPanel processingPanel;
    private JPanel searchPanel;
    private JPanel combinePanel;
    private JLabel searchUserNameLabel;
    private static JTextField searchUserNameTextField;
    private JLabel searchGoodLabel;
    private static JTextField searchField;
    private static JScrollPane orderQueryPanel;

    private JButton queryButton;

    //新建订单页面
    private JPanel submitNewOrderPanel;
    private JPanel submitPanel;
    private JPanel namePanel;
    private JPanel amountPanel;
    private JLabel newOrderUserNameLabel;
    private JLabel goodNameLabel;
    private static JTextField newOrderUserNameTextField;
    private static JTextField goodNameTextField;
    private JLabel amountLabel;
    private static JTextField amountTextField;
    private JButton submitNewOrderButton;
    private static JScrollPane newOrderQueryPanel = new JScrollPane();

    //确认订单
    ConfirmationPanel confirmationPanel = new ConfirmationPanel();

    @Override
    public void init() {
        frame = new JFrame("员工界面");
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        orderPanel = new JPanel();
        newOrderPanel = new JPanel();

        //进行中订单标签初始化
        processingPanel = new JPanel();
        searchPanel = new JPanel();
        combinePanel = new JPanel();
        searchUserNameLabel = new JLabel("订单收货人");
        searchUserNameTextField = new JTextField(20);
        searchGoodLabel = new JLabel("订单商品编号或名称：");
        searchField = new JTextField(20);
        queryButton = new JButton("查询");
        orderQueryPanel = new JScrollPane();

        //查询按键绑定监听
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SalesmanUI.searchTable(searchUserNameTextField.getText(), searchField.getText());
            }
        });


        //给searchPanel设置GridBagLayout
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;

        processingPanel.setLayout(gb);
        combinePanel.setLayout(new BorderLayout());
//        combinePanel.add(searchGoodLabel, BorderLayout.WEST);
//        combinePanel.add(searchField, BorderLayout.CENTER);


        searchPanel.setLayout(gb);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 20, 0, 20);
        gb.setConstraints(searchUserNameLabel, gbc);
        searchPanel.add(searchUserNameLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(searchUserNameTextField, gbc);
        searchPanel.add(searchUserNameTextField);
        gbc.gridwidth = 1;
        gb.setConstraints(searchGoodLabel, gbc);
        searchPanel.add(searchGoodLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(searchField, gbc);
        searchPanel.add(searchField);
        gb.setConstraints(queryButton, gbc);
        searchPanel.add(queryButton);
        combinePanel.add(searchPanel);

        gb.setConstraints(combinePanel, gbc);
        processingPanel.add(combinePanel);

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(800, 50));
        gbc.insets = new Insets(20, 0, 20, 0);
        gb.setConstraints(separator, gbc);
        processingPanel.add(separator);

        orderPanel.setLayout(new BorderLayout());
        orderPanel.add(processingPanel, BorderLayout.CENTER);
        orderPanel.add(orderQueryPanel, BorderLayout.SOUTH);


        queryTable = new JTable();
        orderQueryPanel = new JScrollPane(queryTable);
        Connection connection = SqlControler.getConnection();
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM good ");
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resultSetTableModel = new ResultSetTableModel(resultSet);
        queryTable = new JTable(resultSetTableModel);
        queryTable.getColumnModel().getColumn(0).setHeaderValue("商品编号");
        queryTable.getColumnModel().getColumn(1).setHeaderValue("商品名称");
        queryTable.getColumnModel().getColumn(2).setHeaderValue("库存");
        newOrderQueryPanel = new JScrollPane(queryTable);

        //新建订单标签初始化
        submitNewOrderPanel = new JPanel();
        namePanel = new JPanel(new BorderLayout());
        amountPanel = new JPanel(new BorderLayout());
        submitPanel = new JPanel(new BorderLayout());
        newOrderUserNameLabel = new JLabel("订单收货人：");
        newOrderUserNameTextField = new JTextField(20);
        goodNameLabel = new JLabel("订购商品名称：");
        goodNameTextField = new JTextField(20);
        amountLabel = new JLabel("订购数量：");
        amountTextField = new JTextField(20);
        submitNewOrderButton = new JButton("提交订单");

        //新建订单功能
        submitNewOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SalesmanUI.addOrder();
                SalesmanUI.refreshGoodList();
                SalesmanUI.refreshOrderList();
            }
        });
        //GridBagLayout装载标签和文本框
        JPanel submitPanel2 = new JPanel(gb);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        gb.setConstraints(newOrderUserNameLabel, gbc);
        submitPanel2.add(newOrderUserNameLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(newOrderUserNameTextField, gbc);
        submitPanel2.add(newOrderUserNameTextField);
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
        tabbedPane.add("进行中的订单", orderPanel);
        tabbedPane.add("新建订单", newOrderPanel);
        tabbedPane.add("确认订单", confirmationPanel);

        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

    }

    public static void searchTable(String userName, String g_idOrG_name) {
        System.out.println("searchTable.");
        if (userName.equals("") && g_idOrG_name.equals("")) {
            SalesmanUI.refreshOrderList();
        } else {
            SalesmanUI.searchOrder(userName, g_idOrG_name);
        }

    }

    public static void addOrder() {
        System.out.println("addOrder");
        Connection connection = SqlControler.getConnection();

        String userName;
        String goodName;//订购商品名称
        int orderAmount;//订购数量
        int goodID;//商品ID
        int goodAmount;//商品库存

        userName = newOrderUserNameTextField.getText();
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
                System.out.println("userName = " + userName + ", goodName = " + goodName);
                statement = connection.prepareStatement("" +
                        "SELECT * FROM user " +
                        "WHERE u_id = ?");
                statement.setString(1, userName);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    System.out.println("用户存在。");

                    statement = connection.prepareStatement("" +
                            "SELECT g_id , amount FROM good " +
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
                            statement = connection.prepareStatement("INSERT INTO m_order(amount , g_id , u_id , submit_time) " +
                                    "VALUES( ? , ? , ? , ?)");
                            System.out.println("orderAmount:" + orderAmount + ", goodID:" + goodID + ", u_id:" + Client.u_id);
                            statement.setInt(1, orderAmount);
                            statement.setInt(2, goodID);
                            statement.setString(3, userName);
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
                } else {
                    System.out.println("用户不存在");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void refreshGoodList() {
        Connection connection = SqlControler.getConnection();
        newOrderPanel.remove(newOrderQueryPanel);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM good ");
            resultSet = preparedStatement.executeQuery();
            resultSetTableModel = new ResultSetTableModel(resultSet);
            queryTable = new JTable(resultSetTableModel);
            queryTable.getColumnModel().getColumn(0).setHeaderValue("商品编号");
            queryTable.getColumnModel().getColumn(1).setHeaderValue("商品名称");
            queryTable.getColumnModel().getColumn(2).setHeaderValue("库存");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        newOrderQueryPanel = new JScrollPane(queryTable);
        newOrderPanel.add(newOrderQueryPanel, BorderLayout.SOUTH);
        newOrderPanel.validate();
        tabbedPane.repaint();
        frame.repaint();
    }

    public static void refreshOrderList() {
        orderPanel.remove(orderQueryPanel);
        orderPanel.repaint();

        Connection connection = SqlControler.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , submit_time , confirm_time , emit_time , receipt_time " +
                            "FROM m_order , good " +
                            "WHERE m_order.g_id=good.g_id");
            resultSet = preparedStatement.executeQuery();
            resultSetTableModel = new ResultSetTableModel(resultSet);
            queryTable = new JTable(resultSetTableModel);
            queryTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            queryTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
            queryTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
            queryTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
            queryTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
            queryTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
            queryTable.getColumnModel().getColumn(6).setHeaderValue("提交时间");
            queryTable.getColumnModel().getColumn(7).setHeaderValue("订单确认时间");
            queryTable.getColumnModel().getColumn(8).setHeaderValue("订单发货时间");
            queryTable.getColumnModel().getColumn(9).setHeaderValue("订单收货时间");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        orderQueryPanel = new JScrollPane(queryTable);
        orderPanel.add(orderQueryPanel, BorderLayout.SOUTH);
        orderPanel.validate();
        tabbedPane.repaint();
        frame.repaint();
    }

    public static void searchOrder(String userName, String g_idOrG_name) {
        if (!(userName.equals("") || g_idOrG_name.equals(""))) {
            System.out.println("userName,g_idOrG_name都不为空");
            orderQueryPanel.remove(queryTable);
            orderQueryPanel.repaint();
            Connection connection = SqlControler.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , submit_time , confirm_time , emit_time , receipt_time " +
                                "FROM m_order , good " +
                                "WHERE u_id= ? AND m_order.g_id=good.g_id AND (g_name=? OR  m_order.g_id=?)");
                preparedStatement.setString(1, userName);
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
                queryTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
                queryTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
                queryTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
                queryTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
                queryTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
                queryTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
                queryTable.getColumnModel().getColumn(6).setHeaderValue("提交时间");
                queryTable.getColumnModel().getColumn(7).setHeaderValue("订单确认时间");
                queryTable.getColumnModel().getColumn(8).setHeaderValue("订单发货时间");
                queryTable.getColumnModel().getColumn(9).setHeaderValue("订单收货时间");
                orderQueryPanel = new JScrollPane(queryTable);
                orderPanel.add(orderQueryPanel, BorderLayout.SOUTH);
//                orderQueryPanel.repaint();
//                orderPanel.repaint();
//                orderPanel.validate();
                tabbedPane.repaint();
                frame.repaint();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } else if (userName.equals("") && !g_idOrG_name.equals("")) {
            System.out.println("userName为空");
            orderQueryPanel.remove(queryTable);
            orderQueryPanel.repaint();
            Connection connection = SqlControler.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , submit_time , confirm_time , emit_time , receipt_time " +
                                "FROM m_order , good " +
                                "WHERE  m_order.g_id=good.g_id AND (g_name=? OR  m_order.g_id=?)");
                preparedStatement.setString(1, searchField.getText());
                try {
                    preparedStatement.setInt(2, Integer.valueOf(searchField.getText()));
                } catch (NumberFormatException e2) {
                    e2.printStackTrace();
                    preparedStatement.setInt(2, 0);
                }
                resultSet = preparedStatement.executeQuery();
                resultSetTableModel = new ResultSetTableModel(resultSet);
                queryTable = new JTable(resultSetTableModel);
                queryTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
                queryTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
                queryTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
                queryTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
                queryTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
                queryTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
                queryTable.getColumnModel().getColumn(6).setHeaderValue("提交时间");
                queryTable.getColumnModel().getColumn(7).setHeaderValue("订单确认时间");
                queryTable.getColumnModel().getColumn(8).setHeaderValue("订单发货时间");
                queryTable.getColumnModel().getColumn(9).setHeaderValue("订单收货时间");
                orderQueryPanel = new JScrollPane(queryTable);
                orderPanel.add(orderQueryPanel, BorderLayout.SOUTH);
//                orderQueryPanel.repaint();
//                orderPanel.repaint();
//                orderPanel.validate();
                tabbedPane.repaint();
                frame.repaint();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } else if (!userName.equals("") && g_idOrG_name.equals("")) {
            System.out.println("g_idOrG_name为空");
            orderQueryPanel.remove(queryTable);
            orderQueryPanel.repaint();
            Connection connection = SqlControler.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , submit_time , confirm_time , emit_time , receipt_time " +
                                "FROM m_order , good " +
                                "WHERE u_id= ? AND m_order.g_id=good.g_id ");
                preparedStatement.setString(1, userName);
                resultSet = preparedStatement.executeQuery();
                resultSetTableModel = new ResultSetTableModel(resultSet);
                queryTable = new JTable(resultSetTableModel);
                queryTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
                queryTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
                queryTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
                queryTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
                queryTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
                queryTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
                queryTable.getColumnModel().getColumn(6).setHeaderValue("提交时间");
                queryTable.getColumnModel().getColumn(7).setHeaderValue("订单确认时间");
                queryTable.getColumnModel().getColumn(8).setHeaderValue("订单发货时间");
                queryTable.getColumnModel().getColumn(9).setHeaderValue("订单收货时间");
                orderQueryPanel = new JScrollPane(queryTable);
                orderPanel.add(orderQueryPanel, BorderLayout.SOUTH);
//                orderQueryPanel.repaint();
//                orderPanel.repaint();
//                orderPanel.validate();
                tabbedPane.repaint();
                frame.repaint();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void run() {
        SalesmanUI UI = new SalesmanUI();
        UI.init();
        searchTable("","");
    }


}
