package com.GUI.customer;

import com.GUI.OperationUI;
import com.GUI.university.ChangeInformationPanel;
import com.GUI.university.PasswordChangeUI;
import com.main.Client;
import com.object.User;
import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CustomerUI extends OperationUI {
    private SearchMyOrderPanel searchMyOrderPanel = new SearchMyOrderPanel();
    private AddMyOrderPanel addMyOrderPanel = new AddMyOrderPanel();
    private ReceiptPanel receiptPanel = new ReceiptPanel();
    private JFrame frame;

    public CustomerUI() {
        frame = new JFrame("客户界面");
        JTabbedPane tabbedPane = new JTabbedPane();

        searchMyOrderPanel.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    searchMyOrder();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        addMyOrderPanel.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addMyOrder();
                    refreshMyOrderList();
                    refreshGoodList();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        receiptPanel.setReceiptButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    receiptOrder();
                    refreshMyOrderList();
                    refreshShippingList();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        receiptPanel.setQueryButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    searchShippingOrder();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        ChangeInformationPanel informationPanel = new ChangeInformationPanel();
        informationPanel.setChangePasswordButtonListener();
        informationPanel.setChangeInformationButtonListener();

        tabbedPane.add("我的订单", searchMyOrderPanel);
        tabbedPane.add("新建订单", addMyOrderPanel);
        tabbedPane.add("订单签收", receiptPanel);
        tabbedPane.add("个人信息修改", informationPanel);

        frame.add(tabbedPane);
        frame.pack();
    }

    private void searchMyOrder() throws SQLException {
        String searchText = this.searchMyOrderPanel.getSearchText();
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement;
        if (searchText.equals("")) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , m_order.g_id , g_name , m_order.amount , status " +
                    "FROM m_order , good " +
                    "WHERE u_id=? AND m_order.g_id=good.g_id " +
                    "ORDER BY o_id");
            preparedStatement.setString(1, Client.u_id);
        } else {
            String goodName = searchText;
            int goodID;
            try {
                goodID = Integer.valueOf(searchText);
            } catch (NumberFormatException e) {
                goodID = 0;
            }
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , m_order.g_id , g_name , m_order.amount , status " +
                    "FROM m_order , good " +
                    "WHERE u_id=? AND m_order.g_id=good.g_id AND (g_name=? OR m_order.g_id=?) " +
                    "ORDER BY o_id ");
            preparedStatement.setString(1, Client.u_id);
            preparedStatement.setString(2, goodName);
            preparedStatement.setInt(3, goodID);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
        JTable table = this.searchMyOrderPanel.getTable();
        table.setModel(resultSetTableModel);
        resultSetTableModel.fireTableStructureChanged();
        resultSetTableModel.fireTableDataChanged();
        table.getColumnModel().getColumn(0).setHeaderValue("订单编号");
        table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
        table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
        table.getColumnModel().getColumn(3).setHeaderValue("订单商品数量");
        table.getColumnModel().getColumn(4).setHeaderValue("订单状态");
    }

    private void addMyOrder() throws SQLException {
        String goodName = this.addMyOrderPanel.getGoodName();
        if (goodName.equals("")) {
            System.out.println("请输入商品名称");
            JOptionPane.showMessageDialog(null, "请输入商品名称", null, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int orderAmount;//
        int g_id;
        try {
            orderAmount = Integer.valueOf(this.addMyOrderPanel.getGoodAmount());
        } catch (NumberFormatException e) {
            System.out.println("订单数量输入错误，请输入正确的订单数量");
            JOptionPane.showMessageDialog(null, "订单数量输入错误，请输入正确的订单数量", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT g_id , amount FROM good " +
                "WHERE g_name=?");
        preparedStatement.setString(1, goodName);
        ResultSet resultSet = preparedStatement.executeQuery();
        int amount;
        if (resultSet.next()) {
            g_id = resultSet.getInt(1);
            amount = resultSet.getInt(2);
        } else {
            System.out.println("查询不到该商品，请确认后重试");
            JOptionPane.showMessageDialog(null, "查询不到该商品，请确认后重试", null, JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (amount >= orderAmount) {
            preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO m_order( g_id , amount , u_id , submit_time , status ) " +
                    "VALUES( ? , ? , ? , ? , ? )");
            preparedStatement.setInt(1, g_id);
            preparedStatement.setInt(2, orderAmount);
            preparedStatement.setString(3, Client.u_id);
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(5, "订单已提交");
            int i = preparedStatement.executeUpdate();//提交订单信息
            System.out.println("新增" + i + "条订单");
            JOptionPane.showMessageDialog(null, "提交" + i + "条订单", "提交成功", JOptionPane.INFORMATION_MESSAGE);

            //更改库存信息
            preparedStatement = connection.prepareStatement("" +
                    "UPDATE good " +
                    "SET amount=? " +
                    "WHERE g_id=? AND g_name=?");
            preparedStatement.setInt(1, amount - orderAmount);
            preparedStatement.setInt(2, g_id);
            preparedStatement.setString(3, goodName);
            i = preparedStatement.executeUpdate();
            System.out.println("更新" + i + "条库存信息");
            return;
        } else {
            System.out.println("商品库存不足，新建订单失败");
            JOptionPane.showMessageDialog(null, "商品库存不足，新建订单失败", "库存不足", JOptionPane.ERROR_MESSAGE);
            return;
        }

    }

    private void refreshMyOrderList() throws SQLException {
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT o_id , m_order.g_id , g_name , m_order.amount , status " +
                "FROM m_order , good " +
                "WHERE u_id=? AND m_order.g_id=good.g_id " +
                "ORDER BY o_id");
        preparedStatement.setString(1, Client.u_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
        JTable table = this.searchMyOrderPanel.getTable();
        table.setModel(resultSetTableModel);
        resultSetTableModel.fireTableStructureChanged();
        resultSetTableModel.fireTableDataChanged();
        table.getColumnModel().getColumn(0).setHeaderValue("订单编号");
        table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
        table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
        table.getColumnModel().getColumn(3).setHeaderValue("订单商品数量");
        table.getColumnModel().getColumn(4).setHeaderValue("订单状态");
    }

    private void refreshGoodList() throws SQLException {
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT * FROM good " +
                "ORDER BY g_id ");
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
        JTable table = this.addMyOrderPanel.getTable();
        table.setModel(resultSetTableModel);
        resultSetTableModel.fireTableStructureChanged();
        resultSetTableModel.fireTableDataChanged();
        table.getColumnModel().getColumn(0).setHeaderValue("商品编号");
        table.getColumnModel().getColumn(1).setHeaderValue("商品名称");
        table.getColumnModel().getColumn(2).setHeaderValue("商品库存");
    }

    private void receiptOrder() throws SQLException {
        int orderID = -1;

        try {
            orderID = Integer.valueOf(receiptPanel.getOrderID());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "请输入正确的订单编号", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT * FROM m_order " +
                "WHERE u_id=? AND status=? AND o_id=?");
        preparedStatement.setString(1, Client.u_id);
        preparedStatement.setString(2, "订单配送中");
        preparedStatement.setInt(3, orderID);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            preparedStatement = connection.prepareStatement("" +
                    "UPDATE m_order " +
                    "SET receipt_time=?,status=? " +
                    "WHERE o_id=?");
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(2, "确认收货");
            preparedStatement.setInt(3, orderID);
            int i = preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "成功签收" + i + "个订单。", "订单签收", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "查询不到该订单的配送信息，请确认后再试", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshShippingList() throws SQLException {
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT o_id , m_order.g_id , g_name , m_order.amount , submit_time , confirm_time , exit_time , ship_time , status " +
                "FROM m_order , good " +
                "WHERE m_order.g_id=good.g_id AND status=? AND u_id=? " +
                "ORDER BY o_id");
        preparedStatement.setString(1, "订单配送中");
        preparedStatement.setString(2, Client.u_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
        JTable table = receiptPanel.getTable();
        table.setModel(resultSetTableModel);
        resultSetTableModel.fireTableDataChanged();
        resultSetTableModel.fireTableStructureChanged();
        table.getColumnModel().getColumn(0).setHeaderValue("订单编号");
        table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
        table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
        table.getColumnModel().getColumn(3).setHeaderValue("订单数量");
        table.getColumnModel().getColumn(4).setHeaderValue("订单提交时间");
        table.getColumnModel().getColumn(5).setHeaderValue("订单确认时间");
        table.getColumnModel().getColumn(6).setHeaderValue("订单出仓时间");
        table.getColumnModel().getColumn(7).setHeaderValue("订单派送时间");
        table.getColumnModel().getColumn(8).setHeaderValue("订单状态");
    }

    private void searchShippingOrder() throws SQLException {
        int orderID = 0;
        String goodName = receiptPanel.getGoodName();
        String orderIDText = receiptPanel.getOrderID();
        if (goodName.equals("") && orderIDText.equals("")) {
            refreshShippingList();
            return;
        }
        if (!orderIDText.equals("") && goodName.equals("")) {
            try {
                orderID = Integer.valueOf(receiptPanel.getOrderID());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "请输入正确的订单号", "查询错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Connection connection = SqlControler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , m_order.g_id , g_name , m_order.amount , submit_time , confirm_time , exit_time , ship_time , status " +
                    "FROM m_order , good " +
                    "WHERE m_order.g_id=good.g_id AND status=? AND u_id=? AND o_id=? " +
                    "ORDER BY o_id");
            preparedStatement.setString(1, "订单配送中");
            preparedStatement.setString(2, Client.u_id);
            preparedStatement.setInt(3, orderID);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            JTable table = receiptPanel.getTable();
            table.setModel(resultSetTableModel);
            resultSetTableModel.fireTableDataChanged();
            resultSetTableModel.fireTableStructureChanged();
            table.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
            table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
            table.getColumnModel().getColumn(3).setHeaderValue("订单数量");
            table.getColumnModel().getColumn(4).setHeaderValue("订单提交时间");
            table.getColumnModel().getColumn(5).setHeaderValue("订单确认时间");
            table.getColumnModel().getColumn(6).setHeaderValue("订单出仓时间");
            table.getColumnModel().getColumn(7).setHeaderValue("订单派送时间");
            table.getColumnModel().getColumn(8).setHeaderValue("订单状态");
            return;
        }
        if (orderIDText.equals("") && !goodName.equals("")) {
            Connection connection = SqlControler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , m_order.g_id , g_name , m_order.amount , submit_time , confirm_time , exit_time , ship_time , status " +
                    "FROM m_order , good " +
                    "WHERE m_order.g_id=good.g_id AND status=? AND u_id=? AND g_name=? " +
                    "ORDER BY o_id");
            preparedStatement.setString(1, "订单配送中");
            preparedStatement.setString(2, Client.u_id);
            preparedStatement.setString(3, goodName);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            JTable table = receiptPanel.getTable();
            table.setModel(resultSetTableModel);
            resultSetTableModel.fireTableDataChanged();
            resultSetTableModel.fireTableStructureChanged();
            table.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
            table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
            table.getColumnModel().getColumn(3).setHeaderValue("订单数量");
            table.getColumnModel().getColumn(4).setHeaderValue("订单提交时间");
            table.getColumnModel().getColumn(5).setHeaderValue("订单确认时间");
            table.getColumnModel().getColumn(6).setHeaderValue("订单出仓时间");
            table.getColumnModel().getColumn(7).setHeaderValue("订单派送时间");
            table.getColumnModel().getColumn(8).setHeaderValue("订单状态");
            return;
        }
        if (!goodName.equals("") && !orderIDText.equals("")) {
            try {
                orderID = Integer.valueOf(receiptPanel.getOrderID());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "请输入正确的订单号", "查询错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Connection connection = SqlControler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , m_order.g_id , g_name , m_order.amount , submit_time , confirm_time , exit_time , ship_time , status " +
                    "FROM m_order , good " +
                    "WHERE m_order.g_id=good.g_id AND status=? AND u_id=? AND o_id=? AND g_name=? " +
                    "ORDER BY o_id");
            preparedStatement.setString(1, "订单配送中");
            preparedStatement.setString(2, Client.u_id);
            preparedStatement.setInt(3, orderID);
            preparedStatement.setString(4, goodName);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            JTable table = receiptPanel.getTable();
            table.setModel(resultSetTableModel);
            resultSetTableModel.fireTableDataChanged();
            resultSetTableModel.fireTableStructureChanged();
            table.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            table.getColumnModel().getColumn(1).setHeaderValue("商品编号");
            table.getColumnModel().getColumn(2).setHeaderValue("商品名称");
            table.getColumnModel().getColumn(3).setHeaderValue("订单数量");
            table.getColumnModel().getColumn(4).setHeaderValue("订单提交时间");
            table.getColumnModel().getColumn(5).setHeaderValue("订单确认时间");
            table.getColumnModel().getColumn(6).setHeaderValue("订单出仓时间");
            table.getColumnModel().getColumn(7).setHeaderValue("订单派送时间");
            table.getColumnModel().getColumn(8).setHeaderValue("订单状态");
            return;
        }

    }

    @Override
    public void run() {
        super.run();
        frame.setVisible(true);
    }

    //    private static PreparedStatement statement;
//    private static ResultSet resultSet;
//
//    private static JFrame frame;
//    private static JTabbedPane tabbedPane;
//    private static JPanel myOrderPanel;
//    private static JPanel newOrderPanel;
//    private static ResultSetTableModel resultSetTableModel;
//    private static JTable queryTable;
//
//    //我的订单页面
//    private JPanel searchPanel;
//    private JPanel combinePanel;
//    private JLabel searchString;
//    private static JTextField searchField;
//    private static JScrollPane myOrderQueryPanel;
//
//    private JButton queryButton;
//
//    //新建订单页面
//    private JPanel submitNewOrderPanel;
//    private JPanel submitPanel;
//    private JPanel namePanel;
//    private JPanel amountPanel;
//    private JLabel goodNameLabel;
//    private static JTextField goodNameTextField;
//    private JLabel amountLabel;
//    private static JTextField amountTextField;
//    private JButton submitNewOrderButton;
//    private static JScrollPane newOrderQueryPanel = new JScrollPane();
//
//    //个人信息修改界面
//    private ChangeInformationPanel informationsPanel = new ChangeInformationPanel();
//
//    @Override
//    public void init() {
//        frame = new JFrame("客户界面");
//        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//        myOrderPanel = new JPanel();
//        newOrderPanel = new JPanel();
//
//        //我的订单标签初始化
//        searchPanel = new JPanel();
//        combinePanel = new JPanel();
//        searchString = new JLabel("订单商品编号或名称：");
//        searchField = new JTextField(20);
//        queryButton = new JButton("查询");
//        myOrderQueryPanel = new JScrollPane();
//
//        //查询按键绑定监听
//        queryButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                CustomerUI.searchMyTable(searchField.getText());
//            }
//        });
//
//
//        //给searchPanel设置GridBagLayout
//        GridBagLayout gb = new GridBagLayout();
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.NONE;
//
//        combinePanel.setLayout(new BorderLayout());
//        combinePanel.add(searchString, BorderLayout.WEST);
//        combinePanel.add(searchField, BorderLayout.CENTER);
//
//
//        searchPanel.setLayout(gb);
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
//        gbc.insets = new Insets(50, 20, 0, 20);
//        gb.setConstraints(combinePanel, gbc);
//        searchPanel.add(combinePanel);
//        gb.setConstraints(queryButton, gbc);
//        searchPanel.add(queryButton);
//        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
//        separator.setPreferredSize(new Dimension(800, 50));
//        gbc.insets = new Insets(10, 0, 10, 0);
//        gb.setConstraints(separator, gbc);
//        searchPanel.add(separator);
//
//        myOrderPanel.setLayout(new BorderLayout());
//        myOrderPanel.add(searchPanel, BorderLayout.CENTER);
//        myOrderPanel.add(myOrderQueryPanel, BorderLayout.SOUTH);
//
//        {
//            queryTable = new JTable();
//            myOrderQueryPanel = new JScrollPane(queryTable);
//            showGoodsInNewOrder();
//        }
//        //新建订单标签初始化
//        submitNewOrderPanel = new JPanel();
//        namePanel = new JPanel(new BorderLayout());
//        amountPanel = new JPanel(new BorderLayout());
//        submitPanel = new JPanel(new BorderLayout());
//        goodNameLabel = new JLabel("订购商品名称：");
//        goodNameTextField = new JTextField(20);
//        amountLabel = new JLabel("订购数量：");
//        amountTextField = new JTextField(20);
//        submitNewOrderButton = new JButton("提交订单");
//
//        //按钮添加功能
//        submitNewOrderButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                CustomerUI.addOrder();
//                CustomerUI.refreshGoodList();
//                CustomerUI.refreshMyOrderList();
//            }
//        });
//        //GridBagLayout装载标签和文本框
//        JPanel submitPanel2 = new JPanel(gb);
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
//        //更改信息按键添加功能
//        informationsPanel.setChangeInformationButtonListener();
//        informationsPanel.setChangePasswordButtonListener();
//
//        //添加到tabbedPane
//        tabbedPane.add("我的订单", myOrderPanel);
//        tabbedPane.add("新建订单", newOrderPanel);
//        tabbedPane.add("修改个人信息", informationsPanel);
//        frame.add(tabbedPane);
//        frame.pack();
//        frame.setVisible(true);
//
//    }
//
//    public static void searchMyTable(String para) {
//        System.out.println("searchTable.");
//        if (para.equals("")) {
//            CustomerUI.refreshMyOrderList();
//        } else {
//            CustomerUI.searchMyOrder();
//        }
//
//    }
//
//    public static void showGoodsInNewOrder() {
//        Connection connection = SqlControler.getConnection();
//        try {
//            statement = connection.prepareStatement(
//                    "SELECT * " +
//                            "FROM good ");
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
//    }
//
//    public static void addOrder() {
//        System.out.println("addOrder");
//        Connection connection = SqlControler.getConnection();
//
//        String goodName;//订购商品名称
//        int orderAmount;//订购数量
//        int goodID;//商品ID
//        int goodAmount;//商品库存
//
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
//                System.out.println("goodName = " + goodName);
//                statement = connection.prepareStatement("SELECT g_id , amount FROM good " +
//                        "WHERE g_name = ?");
//                statement.setString(1, goodName);
//                ResultSet resultSet = statement.executeQuery();
//                if (resultSet.next()) {
//                    //通过商品名字查询是否有该种商品
//                    System.out.println("查询到商品");
//                    goodID = resultSet.getInt(1);
//                    goodAmount = resultSet.getInt(2);
//                    System.out.println("goodID:" + goodID + ", goodAmount:" + goodAmount);
//                    if (orderAmount > goodAmount) {
//                        System.out.println(goodName + "库存不足，无法提交订单");
//                    } else {
//                        //更新新订单到数据库
//                        statement = connection.prepareStatement("INSERT INTO m_order(amount , g_id , u_id , submit_time ) " +
//                                "VALUES( ? , ? , ? , ?)");
//                        System.out.println("orderAmount:" + orderAmount + ", goodID:" + goodID + ", u_id:" + Client.u_id);
//                        statement.setInt(1, orderAmount);
//                        statement.setInt(2, goodID);
//                        statement.setString(3, Client.u_id);
//                        statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
//                        int changedCount = statement.executeUpdate();
//                        System.out.println("成功添加" + changedCount + "个订单");
//
//                        //更新商品库存
//                        goodAmount = goodAmount - orderAmount;
//                        statement = connection.prepareStatement("UPDATE good " +
//                                "SET amount = ? " +
//                                "WHERE g_id = ? AND g_name = ?");
//                        statement.setInt(1, goodAmount);
//                        statement.setInt(2, goodID);
//                        statement.setString(3, goodName);
//                        changedCount = statement.executeUpdate();
//                        System.out.println("更新了" + changedCount + "条库存记录");
//                    }
//                } else {
//                    System.out.println("查询不到商品");
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
//    public static void refreshMyOrderList() {
//        myOrderPanel.remove(myOrderQueryPanel);
//        Connection connection = SqlControler.getConnection();
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(
//                    "SELECT o_id ,  m_order.g_id , g_name , m_order.amount , status , submit_time , confirm_time , emit_time , receipt_time  " +
//                            "FROM m_order , good " +
//                            "WHERE u_id=?  AND m_order.g_id=good.g_id");
//            preparedStatement.setString(1, Client.u_id);
//            resultSet = preparedStatement.executeQuery();
//            resultSetTableModel = new ResultSetTableModel(resultSet);
//            queryTable = new JTable(resultSetTableModel);
//            queryTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
//            queryTable.getColumnModel().getColumn(1).setHeaderValue("商品编号");
//            queryTable.getColumnModel().getColumn(2).setHeaderValue("商品名称");
//            queryTable.getColumnModel().getColumn(3).setHeaderValue("订购数量");
//            queryTable.getColumnModel().getColumn(4).setHeaderValue("订单状态");
//            queryTable.getColumnModel().getColumn(5).setHeaderValue("提交时间");
//            queryTable.getColumnModel().getColumn(6).setHeaderValue("订单确认时间");
//            queryTable.getColumnModel().getColumn(7).setHeaderValue("订单发货时间");
//            queryTable.getColumnModel().getColumn(8).setHeaderValue("订单收货时间");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        myOrderQueryPanel = new JScrollPane(queryTable);
//        myOrderPanel.add(myOrderQueryPanel, BorderLayout.SOUTH);
//        myOrderPanel.validate();
//        tabbedPane.repaint();
//        frame.repaint();
//    }
//
//    public static void searchMyOrder() {
//        System.out.println("删除容器");
////            myOrderPanel.remove(myOrderQueryPanel);
//        myOrderQueryPanel.remove(queryTable);
//        System.out.println("查询");
//        Connection connection = SqlControler.getConnection();
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(
//                    "SELECT o_id ,  m_order.g_id , g_name , m_order.amount , status , submit_time , confirm_time , emit_time , receipt_time " +
//                            "FROM m_order , good " +
//                            "WHERE u_id=? AND m_order.g_id=good.g_id AND (g_name=? OR  m_order.g_id=?)");
//            preparedStatement.setString(1, Client.u_id);
//            preparedStatement.setString(2, searchField.getText());
//            try {
//                preparedStatement.setInt(3, Integer.valueOf(searchField.getText()));
//            } catch (NumberFormatException e2) {
//                e2.printStackTrace();
//                preparedStatement.setInt(3, 0);
//            }
//            resultSet = preparedStatement.executeQuery();
//            resultSetTableModel = new ResultSetTableModel(resultSet);
//            queryTable = new JTable(resultSetTableModel);
//            queryTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
//            queryTable.getColumnModel().getColumn(1).setHeaderValue("商品编号");
//            queryTable.getColumnModel().getColumn(2).setHeaderValue("商品名称");
//            queryTable.getColumnModel().getColumn(3).setHeaderValue("订购数量");
//            queryTable.getColumnModel().getColumn(4).setHeaderValue("订单状态");
//            queryTable.getColumnModel().getColumn(5).setHeaderValue("提交时间");
//            queryTable.getColumnModel().getColumn(6).setHeaderValue("订单确认时间");
//            queryTable.getColumnModel().getColumn(7).setHeaderValue("订单发货时间");
//            queryTable.getColumnModel().getColumn(8).setHeaderValue("订单收货时间");
//            System.out.println("添加容器");
//            myOrderQueryPanel = new JScrollPane(queryTable);
//            myOrderPanel.add(myOrderQueryPanel, BorderLayout.SOUTH);
////                myOrderQueryPanel.repaint();
////                myOrderPanel.repaint();
//            tabbedPane.repaint();
//            frame.repaint();
//        } catch (SQLException e1) {
//            e1.printStackTrace();
//        }
//    }
//
//    public void run() {
//        CustomerUI UI = new CustomerUI();
//        UI.init();
//        searchMyTable(searchField.getText());
//    }
//

}

