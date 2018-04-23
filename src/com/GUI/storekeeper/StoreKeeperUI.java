package com.GUI.storekeeper;

import com.GUI.OperationUI;
import com.GUI.university.ChangeInformationPanel;
import com.main.Client;
import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StoreKeeperUI extends OperationUI {
    JFrame frame = new JFrame("仓库管理员界面");
    JTabbedPane tabbedPane = new JTabbedPane();

    EntryOrExitPanel entryPanel = new EntryOrExitPanel(true);
    EntryOrExitPanel exitPanel = new EntryOrExitPanel(false);
    ShippingPanel shippingPanel = new ShippingPanel();
    ChangeInformationPanel changeInformationPanel = new ChangeInformationPanel();

    public StoreKeeperUI() {
        //新建入库记录界面按键功能绑定
        entryPanel.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entry();
                refreshGoodList();
            }
        });

        //新建出仓记录界面按钮功能绑定
        exitPanel.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
                refreshGoodList();
            }
        });

        //订单出仓界面按键功能绑定
        shippingPanel.setSearchButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    searchSubmittedOrder();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        shippingPanel.setShippingButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    shipConfirmedOrder();
                    refreshConfirmedOrderList();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //个人信息修改界面按键功能绑定
        changeInformationPanel.setChangeInformationButtonListener();
        changeInformationPanel.setChangePasswordButtonListener();

        tabbedPane.add("新建入库记录", entryPanel);
        tabbedPane.add("新建出库记录", exitPanel);
        tabbedPane.add("订单出仓", shippingPanel);
        tabbedPane.add("个人信息修改", changeInformationPanel);
        frame.add(tabbedPane);
        frame.pack();
    }

    //搜索已经提交的订单
    public void searchSubmittedOrder() throws SQLException {
        int status = 0;
        int o_id = 0;
        try {
            o_id = Integer.valueOf(this.shippingPanel.getO_id());
        } catch (NumberFormatException e) {//当输入不为数字时
            e.printStackTrace();
            if (!this.shippingPanel.getO_id().equals("")) {
                System.out.println("请确认输入的订单编号格式是否正确");
                return;
            }
        }
        String u_id = this.shippingPanel.getU_id();
        String goodName = this.shippingPanel.getGoodName();
        PreparedStatement preparedStatement;
        if (!this.shippingPanel.getO_id().equals("")) {
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
                "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                "FROM good , m_order " +
                "WHERE status = '订单已确认' AND m_order.g_id=good.g_id");
        if (status == 1) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND g_name=?");
            preparedStatement.setString(1, goodName);
        }
        if (status == 2) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id ,  u_id , m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND u_id=?");
            preparedStatement.setString(1, u_id);
        }
        if (status == 3) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND g_name=? AND u_id=?");
            preparedStatement.setString(1, goodName);
            preparedStatement.setString(2, u_id);
        }
        if (status == 4) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND o_id=?");
            preparedStatement.setInt(1, o_id);
        }
        if (status == 5) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id ,  u_id , m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND o_id=? AND g_name=?");
            preparedStatement.setInt(1, o_id);
            preparedStatement.setString(2, goodName);
        }
        if (status == 6) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND o_id=? AND u_id=?");
            preparedStatement.setInt(1, o_id);
            preparedStatement.setString(2, u_id);
        }
        if (status == 7) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND o_id=? AND u_id=? AND g_name=?");
            preparedStatement.setInt(1, o_id);
            preparedStatement.setString(2, u_id);
            preparedStatement.setString(3, goodName);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
        JTable jTable = this.shippingPanel.getTable();
        jTable.setModel(resultSetTableModel);
        resultSetTableModel.fireTableStructureChanged();
        resultSetTableModel.fireTableDataChanged();
        jTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
        jTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
        jTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
        jTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
        jTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
        jTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
        jTable.getColumnModel().getColumn(6).setHeaderValue("订单确认时间");
    }

    public void shipConfirmedOrder() throws SQLException {
        int o_id = -1;
        //输入为空则直接退出
        if (this.shippingPanel.getO_id().equals("")) {
            System.out.println("请输入待确认的订单编号");
            return;
        }

        //输入非纯数字的订单编号直接退出
        try {
            o_id = Integer.valueOf(new String(this.shippingPanel.getO_id()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("请输入正确的订单编号");
            return;
        }

        //先查询是否存在处于确认状态的订单，确认存在后更新订单信息
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT * FROM m_order " +
                "WHERE o_id=? AND status='订单已确认'");
        preparedStatement.setInt(1, o_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            System.out.println("无法查询到该订单号");
        } else {
            preparedStatement = connection.prepareStatement("" +
                    "UPDATE m_order " +
                    "set status=? , confirm_time=? " +
                    "WHERE o_id=?");
            preparedStatement.setString(1, "订单已出仓");
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(3, o_id);
            int i = preparedStatement.executeUpdate();
            System.out.println("出仓" + i + "条订单");
        }
    }

    private void refreshConfirmedOrderList() {
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            JTable jTable = this.shippingPanel.getTable();
            jTable.setModel(resultSetTableModel);
            resultSetTableModel.fireTableStructureChanged();
            resultSetTableModel.fireTableDataChanged();
            jTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
            jTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
            jTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
            jTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
            jTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
            jTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
            jTable.getColumnModel().getColumn(6).setHeaderValue("订单确认时间");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshGoodList(){
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT * " +
                    "FROM good ");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
            JTable jTable = this.entryPanel.getTable();
            JTable jTable1 = this.exitPanel.getTable();
            jTable.setModel(resultSetTableModel);
            jTable1.setModel(resultSetTableModel);
            resultSetTableModel.fireTableStructureChanged();
            resultSetTableModel.fireTableDataChanged();
            jTable.getColumnModel().getColumn(0).setHeaderValue("商品编号");
            jTable.getColumnModel().getColumn(1).setHeaderValue("商品名称");
            jTable.getColumnModel().getColumn(2).setHeaderValue("库存");
            jTable1.getColumnModel().getColumn(0).setHeaderValue("商品编号");
            jTable1.getColumnModel().getColumn(1).setHeaderValue("商品名称");
            jTable1.getColumnModel().getColumn(2).setHeaderValue("库存");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void entry() {
        int g_id;
        int amount;//原来的库存
        int r_amount;//入库新增数量
        float price;
        String goodName = entryPanel.getGoodName();
        String r_name = entryPanel.getContact();
        String r_phone = entryPanel.getContactPhone();
        try {
            Connection connection = SqlControler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT * FROM good " +
                    "WHERE g_name = ? ");
            preparedStatement.setString(1, goodName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                g_id = resultSet.getInt(1);
                amount = resultSet.getInt(3);
                try {
                    r_amount = Integer.valueOf(entryPanel.getGoodAmount());
                    try {
                        price = Float.valueOf(entryPanel.getPrice());
                        SqlControler.Storehouse.entry(g_id, r_amount, price, r_name, r_phone, Client.u_id);
                        System.out.println("成功添加一条入库记录");

                        amount = amount + r_amount;
                        System.out.println("g_id = " + g_id + " amount = " + amount);
                        preparedStatement = connection.prepareStatement("" +
                                "UPDATE good " +
                                "SET amount=? " +
                                "WHERE g_id=?");
                        preparedStatement.setInt(1, amount);
                        preparedStatement.setInt(2, g_id);
                        int i = preparedStatement.executeUpdate();
                        System.out.println("更新" + i + "条库存信息");

                    } catch (NumberFormatException e1) {
                        System.out.println("价格输入错误，请输入正确的数字");
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    System.out.println("商品数额输入错误，请输出正确的数字");
                }
            } else {
                System.out.println("查询不到该商品");
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    private void exit() {
        int g_id;
        int amount;//原来的库存
        int r_amount;//出库数量
        float price;
        String goodName = exitPanel.getGoodName();
        String r_name = exitPanel.getContact();
        String r_phone = exitPanel.getContactPhone();
        try {
            Connection connection = SqlControler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT * FROM good " +
                    "WHERE g_name = ? ");
            preparedStatement.setString(1, goodName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                g_id = resultSet.getInt(1);
                amount = resultSet.getInt(3);
                try {
                    r_amount = Integer.valueOf(exitPanel.getGoodAmount());
                    if(amount < r_amount){
                        System.out.println("商品库存不足");
                        return;
                    }
                    try {
                        price = Float.valueOf(exitPanel.getPrice());
                        SqlControler.Storehouse.exit(g_id, r_amount, price, r_name, r_phone, Client.u_id);
                        System.out.println("成功添加一条出库记录");

                        amount = amount - r_amount;
                        System.out.println("g_id = " + g_id + " amount = " + amount);
                        preparedStatement = connection.prepareStatement("" +
                                "UPDATE good " +
                                "SET amount=? " +
                                "WHERE g_id=?");
                        preparedStatement.setInt(1, amount);
                        preparedStatement.setInt(2, g_id);
                        int i = preparedStatement.executeUpdate();
                        System.out.println("更新" + i + "条库存信息");

                    } catch (NumberFormatException e1) {
                        System.out.println("价格输入错误，请输入正确的数字");
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    System.out.println("商品数额输入错误，请输出正确的数字");
                }
            } else {
                System.out.println("查询不到该商品");
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        frame.setVisible(true);
    }
}
