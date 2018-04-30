package com.GUI.salesman;

import com.GUI.OperationUI;
import com.GUI.university.ChangeInformationPanel;
import com.main.Client;
import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SalesmanUI extends OperationUI {
    private ProcessingOrderPanel processingOrderPanel = new ProcessingOrderPanel();
    private NewOrderPanel newOrderPanel = new NewOrderPanel();
    private ConfirmationPanel confirmationPanel = new ConfirmationPanel();
    private ChangeInformationPanel changeInformationPanel = new ChangeInformationPanel();
    private JFrame frame = new JFrame("业务员界面");

    public SalesmanUI() {
        //查询订单界面功能实现
        processingOrderPanel.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = processingOrderPanel.getUserID();
                String searchText = processingOrderPanel.getSearchText();
                if (userID.equals("") && searchText.equals("")) {
                    try {
                        refreshProcessingOrder();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    try {
                        searchProcessingOrder();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        //新建订单功能实现
        newOrderPanel.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addNewOrder();
                    refreshGoodList();
                    refreshProcessingOrder();
                    refreshSubmittedOrderList();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        });

        //确认订单功能实现
        confirmationPanel.setSearchButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    searchSubmittedOrder();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        confirmationPanel.setConfirmButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    confirmSubmittedOrder();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });


        //个人信息修改功能实现
        changeInformationPanel.setChangeInformationButtonListener();
        changeInformationPanel.setChangePasswordButtonListener();


        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("进行中的订单", processingOrderPanel);
        tabbedPane.add("新建订单", newOrderPanel);
        tabbedPane.add("确认订单", confirmationPanel);
        tabbedPane.add("个人信息修改", changeInformationPanel);
        frame.add(tabbedPane);
        frame.pack();
    }


//
//
//    private static PreparedStatement statement;
//    private static ResultSet resultSet;
//
//    private static JFrame frame;
//    private static JTabbedPane tabbedPane;
//    private static JPanel orderPanel;
//    private static JPanel newOrderPanel;
//    private static ResultSetTableModel resultSetTableModel;
//    private static JTable queryTable;
//
//    //进行中的订单页面
//    private JPanel processingPanel;
//    private JPanel searchPanel;
//    private JPanel combinePanel;
//    private JLabel searchUserNameLabel;
//    private static JTextField searchUserNameTextField;
//    private JLabel searchGoodLabel;
//    private static JTextField searchField;
//    private static JScrollPane orderQueryPanel;
//
//    private JButton queryButton;
//
//    //新建订单页面
//    private JPanel submitNewOrderPanel;
//    private JPanel submitPanel;
//    private JPanel namePanel;
//    private JPanel amountPanel;
//    private JLabel newOrderUserNameLabel;
//    private JLabel goodNameLabel;
//    private static JTextField newOrderUserNameTextField;
//    private static JTextField goodNameTextField;
//    private JLabel amountLabel;
//    private static JTextField amountTextField;
//    private JButton submitNewOrderButton;
//    private static JScrollPane newOrderQueryPanel = new JScrollPane();

//    //确认订单
//    ConfirmationPanel confirmationPanel = new ConfirmationPanel();
//
//    //个人信息修改界面
//    ChangeInformationPanel changeInformationPanel = new ChangeInformationPanel();

//    @Override
//    public void init() {
//        frame = new JFrame("员工界面");
//        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//        orderPanel = new JPanel();
//        newOrderPanel = new JPanel();
//
//        //进行中订单标签初始化
//        processingPanel = new JPanel();
//        searchPanel = new JPanel();
//        combinePanel = new JPanel();
//        searchUserNameLabel = new JLabel("订单收货人");
//        searchUserNameTextField = new JTextField(20);
//        searchGoodLabel = new JLabel("订单商品编号或名称：");
//        searchField = new JTextField(20);
//        queryButton = new JButton("查询");
//        orderQueryPanel = new JScrollPane();
//
//        //查询按键绑定监听
//        queryButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                SalesmanUI.searchTable(searchUserNameTextField.getText(), searchField.getText());
//            }
//        });
//
//
//        //给searchPanel设置GridBagLayout
//        GridBagLayout gb = new GridBagLayout();
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.NONE;
//
//        processingPanel.setLayout(gb);
//        combinePanel.setLayout(new BorderLayout());
////        combinePanel.add(searchGoodLabel, BorderLayout.WEST);
////        combinePanel.add(searchField, BorderLayout.CENTER);
//
//
//        searchPanel.setLayout(gb);
//        gbc.gridwidth = 1;
//        gbc.insets = new Insets(20, 20, 0, 20);
//        gb.setConstraints(searchUserNameLabel, gbc);
//        searchPanel.add(searchUserNameLabel);
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
//        gb.setConstraints(searchUserNameTextField, gbc);
//        searchPanel.add(searchUserNameTextField);
//        gbc.gridwidth = 1;
//        gb.setConstraints(searchGoodLabel, gbc);
//        searchPanel.add(searchGoodLabel);
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
//        gb.setConstraints(searchField, gbc);
//        searchPanel.add(searchField);
//        gb.setConstraints(queryButton, gbc);
//        searchPanel.add(queryButton);
//        combinePanel.add(searchPanel);
//
//        gb.setConstraints(combinePanel, gbc);
//        processingPanel.add(combinePanel);
//
//        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
//        separator.setPreferredSize(new Dimension(800, 50));
//        gbc.insets = new Insets(20, 0, 20, 0);
//        gb.setConstraints(separator, gbc);
//        processingPanel.add(separator);
//
//        orderPanel.setLayout(new BorderLayout());
//        orderPanel.add(processingPanel, BorderLayout.CENTER);
//        orderPanel.add(orderQueryPanel, BorderLayout.SOUTH);
//
//
//        queryTable = new JTable();
//        orderQueryPanel = new JScrollPane(queryTable);
//        Connection connection = SqlControler.getConnection();
//        try {
//            statement = connection.prepareStatement(
//                    "SELECT * FROM good ");
//            resultSet = statement.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        resultSetTableModel = new ResultSetTableModel(resultSet);
//        queryTable = new JTable(resultSetTableModel);
//        queryTable.getColumnModel().getColumn(0).setHeaderValue("商品编号");
//        queryTable.getColumnModel().getColumn(1).setHeaderValue("商品名称");
//        queryTable.getColumnModel().getColumn(2).setHeaderValue("库存");
//        newOrderQueryPanel = new JScrollPane(queryTable);
//
//        //新建订单标签初始化
//        submitNewOrderPanel = new JPanel();
//        namePanel = new JPanel(new BorderLayout());
//        amountPanel = new JPanel(new BorderLayout());
//        submitPanel = new JPanel(new BorderLayout());
//        newOrderUserNameLabel = new JLabel("订单收货人：");
//        newOrderUserNameTextField = new JTextField(20);
//        goodNameLabel = new JLabel("订购商品名称：");
//        goodNameTextField = new JTextField(20);
//        amountLabel = new JLabel("订购数量：");
//        amountTextField = new JTextField(20);
//        submitNewOrderButton = new JButton("提交订单");
//
//        //新建订单功能
//        submitNewOrderButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                SalesmanUI.addOrder();
//                SalesmanUI.refreshGoodList();
//                SalesmanUI.refreshOrderList();
//                refreshSubmittedOrderList();
//            }
//        });
//        //GridBagLayout装载标签和文本框
//        JPanel submitPanel2 = new JPanel(gb);
//        gbc.gridwidth = 1;
//        gbc.insets = new Insets(0, 0, 30, 0);
//        gb.setConstraints(newOrderUserNameLabel, gbc);
//        submitPanel2.add(newOrderUserNameLabel);
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
//        gb.setConstraints(newOrderUserNameTextField, gbc);
//        submitPanel2.add(newOrderUserNameTextField);
//        gbc.gridwidth = 1;
//        gbc.insets = new Insets(0, 0, 30, 0);
//        gb.setConstraints(goodNameLabel, gbc);
//        submitPanel2.add(goodNameLabel);
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
//        gb.setConstraints(goodNameTextField, gbc);
//        submitPanel2.add(goodNameTextField);
//        gbc.gridwidth = 1;
//        gbc.insets = new Insets(0, 0, 0, 0);
//        gb.setConstraints(amountLabel, gbc);
//        submitPanel2.add(amountLabel);
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
//        gb.setConstraints(amountTextField, gbc);
//        submitPanel2.add(amountTextField);
//
//        submitPanel.add(submitPanel2);
//
//        submitNewOrderPanel.setLayout(gb);
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
//        gbc.insets = new Insets(50, 0, 0, 0);
//
//        gb.setConstraints(submitPanel, gbc);
//        submitNewOrderPanel.add(submitPanel);
//        gb.setConstraints(submitNewOrderButton, gbc);
//        submitNewOrderPanel.add(submitNewOrderButton);
//
//
//        JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
//        separator2.setPreferredSize(new Dimension(800, 50));
//        gbc.insets = new Insets(10, 0, 10, 0);
//        gb.setConstraints(separator2, gbc);
//        submitNewOrderPanel.add(separator2);
//
//
//        newOrderPanel.setLayout(new BorderLayout());
//        newOrderPanel.add(submitNewOrderPanel, BorderLayout.CENTER);
//        newOrderPanel.add(newOrderQueryPanel, BorderLayout.SOUTH);
//
//        //确认订单界面按键绑定
//        confirmationPanel.setSearchButtonListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    searchSubmittedOrder();
//                } catch (SQLException e1) {
//                    e1.printStackTrace();
//                }
//                tabbedPane.repaint();
//                frame.repaint();
//            }
//        });
//
//        confirmationPanel.setConfirmButtonListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    confirmSubmittedOrder();
//                } catch (SQLException e1) {
//                    e1.printStackTrace();
//                }
//                refreshOrderList();
//                refreshSubmittedOrderList();
//            }
//        });
//
//        //个人信息修改界面按键绑定功能
//        changeInformationPanel.setChangePasswordButtonListener();
//        changeInformationPanel.setChangeInformationButtonListener();
//
//        //添加到tabbedPane
//        tabbedPane.add("进行中的订单", orderPanel);
//        tabbedPane.add("新建订单", newOrderPanel);
//        tabbedPane.add("确认订单", confirmationPanel);
//        tabbedPane.add("修改个人信息", changeInformationPanel);
//
//        frame.add(tabbedPane);
//        frame.pack();
//        frame.setVisible(true);
//
//    }

//    public static void searchTable(String userName, String g_idOrG_name) {
//        System.out.println("searchTable.");
//        if (userName.equals("") && g_idOrG_name.equals("")) {
//            SalesmanUI.refreshOrderList();
//        } else {
//            SalesmanUI.searchOrder(userName, g_idOrG_name);
//        }
//
//    }

//    public static void addOrder() {
//        System.out.println("addOrder");
//        Connection connection = SqlControler.getConnection();
//
//        String userName;
//        String goodName;//订购商品名称
//        int orderAmount;//订购数量
//        int goodID;//商品ID
//        int goodAmount;//商品库存
//
//        userName = newOrderUserNameTextField.getText();
//        goodName = goodNameTextField.getText();
//        try {
//            orderAmount = Integer.parseInt(amountTextField.getText());
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//            System.out.println("数量输入非数字");
//            orderAmount = -1;
//        }
//        if (orderAmount < 1) {
//            System.out.println("请输入大于0的数字");
//        } else {
//            try {
//                System.out.println("userName = " + userName + ", goodName = " + goodName);
//                statement = connection.prepareStatement("" +
//                        "SELECT * FROM user " +
//                        "WHERE u_id = ?");
//                statement.setString(1, userName);
//                ResultSet rs = statement.executeQuery();
//                if (rs.next()) {
//                    System.out.println("用户存在。");
//
//                    statement = connection.prepareStatement("" +
//                            "SELECT g_id , amount FROM good " +
//                            "WHERE g_name = ?");
//                    statement.setString(1, goodName);
//                    ResultSet resultSet = statement.executeQuery();
//                    if (resultSet.next()) {
//                        //通过商品名字查询是否有该种商品
//                        System.out.println("查询到商品");
//                        goodID = resultSet.getInt(1);
//                        goodAmount = resultSet.getInt(2);
//                        System.out.println("goodID:" + goodID + ", goodAmount:" + goodAmount);
//                        if (orderAmount > goodAmount) {
//                            System.out.println(goodName + "库存不足，无法提交订单");
//                        } else {
//                            //更新新订单到数据库
//                            statement = connection.prepareStatement("INSERT INTO m_order(amount , g_id , u_id , submit_time) " +
//                                    "VALUES( ? , ? , ? , ?)");
//                            System.out.println("orderAmount:" + orderAmount + ", goodID:" + goodID + ", u_id:" + Client.u_id);
//                            statement.setInt(1, orderAmount);
//                            statement.setInt(2, goodID);
//                            statement.setString(3, userName);
//                            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
//                            int changedCount = statement.executeUpdate();
//                            System.out.println("成功添加" + changedCount + "个订单");
//
//                            //更新商品库存
//                            goodAmount = goodAmount - orderAmount;
//                            statement = connection.prepareStatement("UPDATE good " +
//                                    "SET amount = ? " +
//                                    "WHERE g_id = ? AND g_name = ?");
//                            statement.setInt(1, goodAmount);
//                            statement.setInt(2, goodID);
//                            statement.setString(3, goodName);
//                            changedCount = statement.executeUpdate();
//                            System.out.println("更新了" + changedCount + "条库存记录");
//                        }
//                    } else {
//                        System.out.println("查询不到商品");
//                    }
//                } else {
//                    System.out.println("用户不存在");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static void refreshGoodList() {
//        Connection connection = SqlControler.getConnection();
//        newOrderPanel.remove(newOrderQueryPanel);
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(
//                    "SELECT * FROM good ");
//            resultSet = preparedStatement.executeQuery();
//            resultSetTableModel = new ResultSetTableModel(resultSet);
//            queryTable = new JTable(resultSetTableModel);
//            queryTable.getColumnModel().getColumn(0).setHeaderValue("商品编号");
//            queryTable.getColumnModel().getColumn(1).setHeaderValue("商品名称");
//            queryTable.getColumnModel().getColumn(2).setHeaderValue("库存");
//        } catch (SQLException e1) {
//            e1.printStackTrace();
//        }
//        newOrderQueryPanel = new JScrollPane(queryTable);
//        newOrderPanel.add(newOrderQueryPanel, BorderLayout.SOUTH);
//        newOrderPanel.validate();
//        tabbedPane.repaint();
//        frame.repaint();
//    }
//
//    public static void refreshOrderList() {
//        orderPanel.remove(orderQueryPanel);
//        orderPanel.repaint();
//
//        Connection connection = SqlControler.getConnection();
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(
//                    "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , submit_time , confirm_time , emit_time , receipt_time " +
//                            "FROM m_order , good " +
//                            "WHERE m_order.g_id=good.g_id");
//            resultSet = preparedStatement.executeQuery();
//            resultSetTableModel = new ResultSetTableModel(resultSet);
//            queryTable = new JTable(resultSetTableModel);
//            queryTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
//            queryTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
//            queryTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
//            queryTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
//            queryTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
//            queryTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
//            queryTable.getColumnModel().getColumn(6).setHeaderValue("提交时间");
//            queryTable.getColumnModel().getColumn(7).setHeaderValue("订单确认时间");
//            queryTable.getColumnModel().getColumn(8).setHeaderValue("订单发货时间");
//            queryTable.getColumnModel().getColumn(9).setHeaderValue("订单收货时间");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        orderQueryPanel = new JScrollPane(queryTable);
//        orderPanel.add(orderQueryPanel, BorderLayout.SOUTH);
//        orderPanel.validate();
//        tabbedPane.repaint();
//        frame.repaint();
//    }
//
//    public static void searchOrder(String userName, String g_idOrG_name) {
//        if (!(userName.equals("") || g_idOrG_name.equals(""))) {
//            System.out.println("userName,g_idOrG_name都不为空");
//            orderQueryPanel.remove(queryTable);
//            orderQueryPanel.repaint();
//            Connection connection = SqlControler.getConnection();
//            try {
//                PreparedStatement preparedStatement = connection.prepareStatement(
//                        "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , submit_time , confirm_time , emit_time , receipt_time " +
//                                "FROM m_order , good " +
//                                "WHERE u_id= ? AND m_order.g_id=good.g_id AND (g_name=? OR  m_order.g_id=?)");
//                preparedStatement.setString(1, userName);
//                preparedStatement.setString(2, searchField.getText());
//                try {
//                    preparedStatement.setInt(3, Integer.valueOf(searchField.getText()));
//                } catch (NumberFormatException e2) {
//                    e2.printStackTrace();
//                    preparedStatement.setInt(3, 0);
//                }
//                resultSet = preparedStatement.executeQuery();
//                resultSetTableModel = new ResultSetTableModel(resultSet);
//                queryTable = new JTable(resultSetTableModel);
//                queryTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
//                queryTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
//                queryTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
//                queryTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
//                queryTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
//                queryTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
//                queryTable.getColumnModel().getColumn(6).setHeaderValue("提交时间");
//                queryTable.getColumnModel().getColumn(7).setHeaderValue("订单确认时间");
//                queryTable.getColumnModel().getColumn(8).setHeaderValue("订单发货时间");
//                queryTable.getColumnModel().getColumn(9).setHeaderValue("订单收货时间");
//                orderQueryPanel = new JScrollPane(queryTable);
//                orderPanel.add(orderQueryPanel, BorderLayout.SOUTH);
////                orderQueryPanel.repaint();
////                orderPanel.repaint();
////                orderPanel.validate();
//                tabbedPane.repaint();
//                frame.repaint();
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        } else if (userName.equals("") && !g_idOrG_name.equals("")) {
//            System.out.println("userName为空");
//            orderQueryPanel.remove(queryTable);
//            orderQueryPanel.repaint();
//            Connection connection = SqlControler.getConnection();
//            try {
//                PreparedStatement preparedStatement = connection.prepareStatement(
//                        "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , submit_time , confirm_time , emit_time , receipt_time " +
//                                "FROM m_order , good " +
//                                "WHERE  m_order.g_id=good.g_id AND (g_name=? OR  m_order.g_id=?)");
//                preparedStatement.setString(1, searchField.getText());
//                try {
//                    preparedStatement.setInt(2, Integer.valueOf(searchField.getText()));
//                } catch (NumberFormatException e2) {
//                    e2.printStackTrace();
//                    preparedStatement.setInt(2, 0);
//                }
//                resultSet = preparedStatement.executeQuery();
//                resultSetTableModel = new ResultSetTableModel(resultSet);
//                queryTable = new JTable(resultSetTableModel);
//                queryTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
//                queryTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
//                queryTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
//                queryTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
//                queryTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
//                queryTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
//                queryTable.getColumnModel().getColumn(6).setHeaderValue("提交时间");
//                queryTable.getColumnModel().getColumn(7).setHeaderValue("订单确认时间");
//                queryTable.getColumnModel().getColumn(8).setHeaderValue("订单发货时间");
//                queryTable.getColumnModel().getColumn(9).setHeaderValue("订单收货时间");
//                orderQueryPanel = new JScrollPane(queryTable);
//                orderPanel.add(orderQueryPanel, BorderLayout.SOUTH);
////                orderQueryPanel.repaint();
////                orderPanel.repaint();
////                orderPanel.validate();
//                tabbedPane.repaint();
//                frame.repaint();
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        } else if (!userName.equals("") && g_idOrG_name.equals("")) {
//            System.out.println("g_idOrG_name为空");
//            orderQueryPanel.remove(queryTable);
//            orderQueryPanel.repaint();
//            Connection connection = SqlControler.getConnection();
//            try {
//                PreparedStatement preparedStatement = connection.prepareStatement(
//                        "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , submit_time , confirm_time , emit_time , receipt_time " +
//                                "FROM m_order , good " +
//                                "WHERE u_id= ? AND m_order.g_id=good.g_id ");
//                preparedStatement.setString(1, userName);
//                resultSet = preparedStatement.executeQuery();
//                resultSetTableModel = new ResultSetTableModel(resultSet);
//                queryTable = new JTable(resultSetTableModel);
//                queryTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
//                queryTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
//                queryTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
//                queryTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
//                queryTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
//                queryTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
//                queryTable.getColumnModel().getColumn(6).setHeaderValue("提交时间");
//                queryTable.getColumnModel().getColumn(7).setHeaderValue("订单确认时间");
//                queryTable.getColumnModel().getColumn(8).setHeaderValue("订单发货时间");
//                queryTable.getColumnModel().getColumn(9).setHeaderValue("订单收货时间");
//                orderQueryPanel = new JScrollPane(queryTable);
//                orderPanel.add(orderQueryPanel, BorderLayout.SOUTH);
////                orderQueryPanel.repaint();
////                orderPanel.repaint();
////                orderPanel.validate();
//                tabbedPane.repaint();
//                frame.repaint();
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }

    //刷新所有进行中的订单
    private void refreshProcessingOrder() throws SQLException {
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT o_id , m_order.g_id , g_name , m_order.amount , u_id , submit_time , ship_time , receipt_time , status " +
                "FROM m_order , good " +
                "WHERE m_order.g_id=good.g_id " +
                "ORDER BY o_id");
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
        JTable table = this.processingOrderPanel.getTable();
        table.setModel(resultSetTableModel);
        resultSetTableModel.fireTableStructureChanged();
        resultSetTableModel.fireTableDataChanged();
        table.getColumnModel().getColumn(0).setHeaderValue("订单编号");
        table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
        table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
        table.getColumnModel().getColumn(3).setHeaderValue("订单数量");
        table.getColumnModel().getColumn(4).setHeaderValue("订单用户");
        table.getColumnModel().getColumn(5).setHeaderValue("提交时间");
        table.getColumnModel().getColumn(6).setHeaderValue("确认时间");
        table.getColumnModel().getColumn(7).setHeaderValue("收货时间");
        table.getColumnModel().getColumn(8).setHeaderValue("订单状态");
    }

    //查询正在进行中的订单
    private void searchProcessingOrder() throws SQLException {
        String userID = this.processingOrderPanel.getUserID();
        String searchText = this.processingOrderPanel.getSearchText();
        String goodName = searchText;
        int goodID;
        try {
            goodID = Integer.valueOf(searchText);
        } catch (NumberFormatException e) {
            goodID = 0;
            e.printStackTrace();
        }
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement;
//        PreparedStatement preparedStatement = connection.prepareStatement("" +
//                "SELECT g_id FROM good " +
//                "WHERE g_name=?");
//        preparedStatement.setString(1, goodName);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        if(resultSet.next()){
//            goodID = resultSet.getInt(1);
//        }
        if (userID.equals("") && !searchText.equals("")) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , m_order.g_id , g_name , m_order.amount , u_id , submit_time , ship_time , receipt_time , status " +
                    "FROM m_order , good " +
                    "WHERE m_order.g_id=good.g_id AND (g_name=? OR m_order.g_id=?) " +
                    "ORDER BY o_id");
            preparedStatement.setString(1, goodName);
            preparedStatement.setInt(2, goodID);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            JTable table = this.processingOrderPanel.getTable();
            table.setModel(resultSetTableModel);
            resultSetTableModel.fireTableStructureChanged();
            resultSetTableModel.fireTableDataChanged();
            table.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
            table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
            table.getColumnModel().getColumn(3).setHeaderValue("订单数量");
            table.getColumnModel().getColumn(4).setHeaderValue("订单用户");
            table.getColumnModel().getColumn(5).setHeaderValue("提交时间");
            table.getColumnModel().getColumn(6).setHeaderValue("确认时间");
            table.getColumnModel().getColumn(7).setHeaderValue("收货时间");
            table.getColumnModel().getColumn(8).setHeaderValue("订单状态");
        } else if (!userID.equals("") && searchText.equals("")) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , m_order.g_id , g_name , m_order.amount , u_id , submit_time , ship_time , receipt_time , status " +
                    "FROM m_order , good " +
                    "WHERE m_order.g_id=good.g_id AND u_id=? " +
                    "ORDER BY o_id");
            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            JTable table = this.processingOrderPanel.getTable();
            table.setModel(resultSetTableModel);
            resultSetTableModel.fireTableStructureChanged();
            resultSetTableModel.fireTableDataChanged();
            table.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
            table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
            table.getColumnModel().getColumn(3).setHeaderValue("订单数量");
            table.getColumnModel().getColumn(4).setHeaderValue("订单用户");
            table.getColumnModel().getColumn(5).setHeaderValue("提交时间");
            table.getColumnModel().getColumn(6).setHeaderValue("确认时间");
            table.getColumnModel().getColumn(7).setHeaderValue("收货时间");
            table.getColumnModel().getColumn(8).setHeaderValue("订单状态");
        } else if (!userID.equals("") && !searchText.equals("")) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , m_order.g_id , g_name , m_order.amount , u_id , submit_time , ship_time , receipt_time , status " +
                    "FROM m_order , good " +
                    "WHERE m_order.g_id=good.g_id AND u_id=? AND (g_name=? OR m_order.g_id=?) " +
                    "ORDER BY o_id");
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, goodName);
            preparedStatement.setInt(3, goodID);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            JTable table = this.processingOrderPanel.getTable();
            table.setModel(resultSetTableModel);
            resultSetTableModel.fireTableStructureChanged();
            resultSetTableModel.fireTableDataChanged();
            table.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
            table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
            table.getColumnModel().getColumn(3).setHeaderValue("订单数量");
            table.getColumnModel().getColumn(4).setHeaderValue("订单用户");
            table.getColumnModel().getColumn(5).setHeaderValue("提交时间");
            table.getColumnModel().getColumn(6).setHeaderValue("确认时间");
            table.getColumnModel().getColumn(7).setHeaderValue("收货时间");
            table.getColumnModel().getColumn(8).setHeaderValue("订单状态");
        }
    }

    //新建订单功能
    //添加新订单，需要更改库存，添加订单
    private void addNewOrder() throws SQLException {
        String userID = newOrderPanel.getUserID();
        String goodName = newOrderPanel.getGoodName();
        int orderAmount = 0;
        int goodID = 0;
        int goodAmount = -1;
        try {
            orderAmount = Integer.valueOf(newOrderPanel.getGoodAmount());
        } catch (NumberFormatException e) {
            System.out.println("请输入正确的订单数量");
        }

        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT * FROM user " +
                "WHERE u_id=?");
        preparedStatement.setString(1, userID);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            System.out.println("该用户不存在，请确认用户名是否正确");
            return;
        }
        if (orderAmount > 0) {
             connection = SqlControler.getConnection();
            preparedStatement = connection.prepareStatement("" +
                    "SELECT * FROM good " +
                    "WHERE g_name=?");
            preparedStatement.setString(1, goodName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                goodID = resultSet.getInt(1);
                goodAmount = resultSet.getInt(3);
            } else {
                System.out.println("该商品不存在，请确认商品名称是否正确");
                return;
            }
            if (orderAmount <= goodAmount) { // 0 < orderAmount <= goodAmount
                //在m_order表新建一条记录
                preparedStatement = connection.prepareStatement("" +
                        "INSERT INTO m_order( g_id , amount , u_id , submit_time , status ) " +
                        "VALUES (?,?,?,?,'订单已提交')");
                preparedStatement.setInt(1, goodID);
                preparedStatement.setInt(2, orderAmount);
                preparedStatement.setString(3, userID);
                preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                int i = preparedStatement.executeUpdate();
                System.out.println("在m_order表插入了" + i + "条记录");

                //更新good表库存
                preparedStatement = connection.prepareStatement("" +
                        "UPDATE good " +
                        "SET amount=? " +
                        "WHERE g_name=? AND g_id=?");
                preparedStatement.setInt(1, goodAmount - orderAmount);
                preparedStatement.setString(2, goodName);
                preparedStatement.setInt(3, goodID);
                i = preparedStatement.executeUpdate();
                System.out.println("在good表更新了" + i + "条记录");
            } else{
                System.out.println("商品库存不足");
            }
        }
    }

    //刷新新建订单的商品列表
    private void refreshGoodList() throws SQLException {
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT * FROM good " +
                "ORDER BY g_id");
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
        JTable table = this.newOrderPanel.getTable();
        table.setModel(resultSetTableModel);
        resultSetTableModel.fireTableStructureChanged();
        resultSetTableModel.fireTableDataChanged();
        table.getColumnModel().getColumn(0).setHeaderValue("商品编号");
        table.getColumnModel().getColumn(1).setHeaderValue("商品名称");
        table.getColumnModel().getColumn(2).setHeaderValue("商品库存");
    }

    //搜索已经提交的订单
    private void searchSubmittedOrder() throws SQLException {
        int status = 0;
        int o_id = 0;
        try {
            o_id = Integer.valueOf(this.confirmationPanel.getO_id());
        } catch (NumberFormatException e) {//当输入不为数字时
            e.printStackTrace();
            if (!this.confirmationPanel.getO_id().equals("")) {
                System.out.println("请确认输入的订单编号格式是否正确");
                return;
            }
        }
        String u_id = this.confirmationPanel.getU_id();
        String goodName = this.confirmationPanel.getGoodName();
        PreparedStatement preparedStatement;
        if (!this.confirmationPanel.getO_id().equals("")) {
            status = status + 4;
        }
        if (!u_id.equals("")) {
            status = status + 2;
        }
        if (!goodName.equals("")) {
            status = status + 1;
        }
        Connection connection = SqlControler.getConnection();
        preparedStatement = connection.prepareStatement("" +
                "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , submit_time " +
                "FROM good , m_order " +
                "WHERE status = '订单已提交' AND m_order.g_id=good.g_id " +
                "ORDER BY o_id");
        if (status == 1) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , submit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已提交' AND m_order.g_id=good.g_id AND g_name=? " +
                    "ORDER BY o_id");
            preparedStatement.setString(1, goodName);
        }
        if (status == 2) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id ,  u_id , m_order.g_id , g_name , m_order.amount , status , submit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已提交' AND m_order.g_id=good.g_id AND u_id=? " +
                    "ORDER BY o_id");
            preparedStatement.setString(1, u_id);
        }
        if (status == 3) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , submit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已提交' AND m_order.g_id=good.g_id AND g_name=? AND u_id=? " +
                    "ORDER BY o_id");
            preparedStatement.setString(1, goodName);
            preparedStatement.setString(2, u_id);
        }
        if (status == 4) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , submit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已提交' AND m_order.g_id=good.g_id AND o_id=? " +
                    "ORDER BY o_id");
            preparedStatement.setInt(1, o_id);
        }
        if (status == 5) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id ,  u_id , m_order.g_id , g_name , m_order.amount , status , submit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已提交' AND m_order.g_id=good.g_id AND o_id=? AND g_name=? " +
                    "ORDER BY o_id");
            preparedStatement.setInt(1, o_id);
            preparedStatement.setString(2, goodName);
        }
        if (status == 6) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , submit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已提交' AND m_order.g_id=good.g_id AND o_id=? AND u_id=? " +
                    "ORDER BY o_id");
            preparedStatement.setInt(1, o_id);
            preparedStatement.setString(2, u_id);
        }
        if (status == 7) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , submit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已提交' AND m_order.g_id=good.g_id AND o_id=? AND u_id=? AND g_name=? " +
                    "ORDER BY o_id");
            preparedStatement.setInt(1, o_id);
            preparedStatement.setString(2, u_id);
            preparedStatement.setString(3, goodName);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
        JTable jTable = this.confirmationPanel.getTable();
        jTable.setModel(resultSetTableModel);
        resultSetTableModel.fireTableStructureChanged();
        resultSetTableModel.fireTableDataChanged();
        jTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
        jTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
        jTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
        jTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
        jTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
        jTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
        jTable.getColumnModel().getColumn(6).setHeaderValue("提交时间");
    }

    private void confirmSubmittedOrder() throws SQLException {
        int o_id = -1;
        //输入为空则直接退出
        if (this.confirmationPanel.getO_id().equals("")) {
            System.out.println("请输入待确认的订单编号");
            return;
        }

        //输入非纯数字的订单编号直接退出
        try {
            o_id = Integer.valueOf(new String(this.confirmationPanel.getO_id()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("请输入正确的订单编号");
            return;
        }

        //先查询是否存在处于提交状态的订单，确认存在后更新订单信息
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT * FROM m_order " +
                "WHERE o_id=? AND status='订单已提交'");
        preparedStatement.setInt(1, o_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            System.out.println("无法查询到该订单号");
        } else {
            preparedStatement = connection.prepareStatement("" +
                    "UPDATE m_order " +
                    "set status=? , confirm_time=? " +
                    "WHERE o_id=? AND status='订单已提交'");
            preparedStatement.setString(1, "订单已确认");
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(3, o_id);
            int i = preparedStatement.executeUpdate();
            System.out.println("确认" + i + "条订单");
        }
    }

    private void refreshSubmittedOrderList() {
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , submit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已提交' AND m_order.g_id=good.g_id " +
                    "ORDER BY o_id");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            JTable jTable = this.confirmationPanel.getTable();
            jTable.setModel(resultSetTableModel);
            resultSetTableModel.fireTableStructureChanged();
            resultSetTableModel.fireTableDataChanged();
            jTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            jTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
            jTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
            jTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
            jTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
            jTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
            jTable.getColumnModel().getColumn(6).setHeaderValue("提交时间");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SalesmanUI salesmanUI = new SalesmanUI();
        salesmanUI.run();
    }
}
