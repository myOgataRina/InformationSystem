package com.GUI.customer;

import com.GUI.OperationUI;
import com.GUI.PasswordChangeUI;
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

    //个人信息修改界面
    private static JPanel informationsPanel;
    private static JLabel u_idLabel;
    private static JLabel phoneLabel;
    private static JLabel addressLabel;
    private static JTextField phoneTextField;
    private static JTextField addressTextField;
    private static JButton changePasswordButton;
    private static JButton changeInformationButton;


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
            showGoodsInNewOrder();
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

        //个人信息修改界面初始化
        informationsPanel = new JPanel();
        phoneLabel = new JLabel("电话");
        phoneTextField = new JTextField(20);
        addressLabel = new JLabel("地址");
        addressTextField = new JTextField(20);
        changeInformationButton = new JButton("更新信息");
        changePasswordButton = new JButton("更改密码");

        //按键绑定Listener
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasswordChangeUI passwordChangeUI = new PasswordChangeUI(Client.u_id);
                passwordChangeUI.init();
            }
        });

        changeInformationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = SqlControler.getConnection();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("" +
                            "UPDATE user " +
                            "SET phone = ?, address = ? " +
                            "WHERE u_id = ?");
                    preparedStatement.setString(1, new String(phoneTextField.getText()));
                    preparedStatement.setString(2, new String(addressTextField.getText()));
                    preparedStatement.setString(3, Client.u_id);

                    int i = preparedStatement.executeUpdate();
                    if (i == 0) {
                        System.out.println("资料更新失败");
                    } else {
                        System.out.println("更新" + i + "条记录");
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        User user = SqlControler.getUser(Client.u_id);
        if (user != null) {
            u_idLabel = new JLabel("目前登陆的用户：" + Client.u_id);
            phoneTextField.setText(user.getPhone());
            addressTextField.setText(user.getAddress());
        }
        informationsPanel.setLayout(gb);
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(u_idLabel, gbc);
        informationsPanel.add(u_idLabel);
        gbc.gridwidth = 1;
        gb.setConstraints(phoneLabel, gbc);
        informationsPanel.add(phoneLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(phoneTextField, gbc);
        informationsPanel.add(phoneTextField);
        gbc.gridwidth = 1;
        gb.setConstraints(addressLabel, gbc);
        informationsPanel.add(addressLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(addressTextField, gbc);
        informationsPanel.add(addressTextField);

        gb.setConstraints(changePasswordButton, gbc);
        informationsPanel.add(changePasswordButton);
        gb.setConstraints(changeInformationButton, gbc);
        informationsPanel.add(changeInformationButton);

        //添加到tabbedPane
        tabbedPane.add("我的订单", myOrderPanel);
        tabbedPane.add("新建订单", newOrderPanel);
        tabbedPane.add("修改个人信息", informationsPanel);
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

    }

    public static void searchMyTable(String para) {
        System.out.println("searchTable.");
        if (para.equals("")) {
            CustomerUI.refreshMyOrderList();
        } else {
            CustomerUI.searchMyOrder();
        }

    }

    public static void showGoodsInNewOrder() {
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
        queryTable.getColumnModel().getColumn(0).setHeaderValue("商品编号");
        queryTable.getColumnModel().getColumn(1).setHeaderValue("商品名称");
        queryTable.getColumnModel().getColumn(2).setHeaderValue("库存");
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
                        statement = connection.prepareStatement("INSERT INTO m_order(amount , g_id , u_id , submit_time ) " +
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

    public static void refreshMyOrderList() {
        myOrderPanel.remove(myOrderQueryPanel);
        Connection connection = SqlControler.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT o_id ,  m_order.g_id , g_name , m_order.amount , status , submit_time , confirm_time , emit_time , receipt_time  " +
                            "FROM m_order , good " +
                            "WHERE u_id=?  AND m_order.g_id=good.g_id");
            preparedStatement.setString(1, Client.u_id);
            resultSet = preparedStatement.executeQuery();
            resultSetTableModel = new ResultSetTableModel(resultSet);
            queryTable = new JTable(resultSetTableModel);
            queryTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            queryTable.getColumnModel().getColumn(1).setHeaderValue("商品编号");
            queryTable.getColumnModel().getColumn(2).setHeaderValue("商品名称");
            queryTable.getColumnModel().getColumn(3).setHeaderValue("订购数量");
            queryTable.getColumnModel().getColumn(4).setHeaderValue("订单状态");
            queryTable.getColumnModel().getColumn(5).setHeaderValue("提交时间");
            queryTable.getColumnModel().getColumn(6).setHeaderValue("订单确认时间");
            queryTable.getColumnModel().getColumn(7).setHeaderValue("订单发货时间");
            queryTable.getColumnModel().getColumn(8).setHeaderValue("订单收货时间");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        myOrderQueryPanel = new JScrollPane(queryTable);
        myOrderPanel.add(myOrderQueryPanel, BorderLayout.SOUTH);
        myOrderPanel.validate();
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
                    "SELECT o_id ,  m_order.g_id , g_name , m_order.amount , status , submit_time , confirm_time , emit_time , receipt_time " +
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
            queryTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            queryTable.getColumnModel().getColumn(1).setHeaderValue("商品编号");
            queryTable.getColumnModel().getColumn(2).setHeaderValue("商品名称");
            queryTable.getColumnModel().getColumn(3).setHeaderValue("订购数量");
            queryTable.getColumnModel().getColumn(4).setHeaderValue("订单状态");
            queryTable.getColumnModel().getColumn(5).setHeaderValue("提交时间");
            queryTable.getColumnModel().getColumn(6).setHeaderValue("订单确认时间");
            queryTable.getColumnModel().getColumn(7).setHeaderValue("订单发货时间");
            queryTable.getColumnModel().getColumn(8).setHeaderValue("订单收货时间");
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

