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

    StoreEntryOrExitListPanel storeEntryOrExitListPanel = new StoreEntryOrExitListPanel();
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
                    searchConfirmedOrder();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        shippingPanel.setShippingButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    exitConfirmedOrder();
                    refreshConfirmedOrderList();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //个人信息修改界面按键功能绑定
        changeInformationPanel.setChangeInformationButtonListener();
        changeInformationPanel.setChangePasswordButtonListener();

        tabbedPane.add("出入库查询", storeEntryOrExitListPanel);
        tabbedPane.add("新建入库记录", entryPanel);
        tabbedPane.add("新建出库记录", exitPanel);
        tabbedPane.add("订单出仓", shippingPanel);
        tabbedPane.add("个人信息修改", changeInformationPanel);
        frame.add(tabbedPane);
        frame.pack();
    }

    //搜索已经确认的订单
    private void searchConfirmedOrder() throws SQLException {
        int status = 0;
        int o_id = 0;
        try {
            o_id = Integer.valueOf(this.shippingPanel.getO_id());
        } catch (NumberFormatException e) {//当输入不为数字时
            e.printStackTrace();
            if (!this.shippingPanel.getO_id().equals("")) {
                System.out.println("请确认输入的订单编号格式是否正确");
                JOptionPane.showMessageDialog(null, "请确认输入的订单编号格式是否正确", "警告", JOptionPane.WARNING_MESSAGE);
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
                "WHERE status = '订单已确认' AND m_order.g_id=good.g_id " +
                "ORDER BY o_id DESC ");
        if (status == 1) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND g_name=? " +
                    "ORDER BY o_id DESC ");
            preparedStatement.setString(1, goodName);
        }
        if (status == 2) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id ,  u_id , m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND u_id=? " +
                    "ORDER BY o_id DESC ");
            preparedStatement.setString(1, u_id);
        }
        if (status == 3) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND g_name=? AND u_id=? " +
                    "ORDER BY o_id DESC ");
            preparedStatement.setString(1, goodName);
            preparedStatement.setString(2, u_id);
        }
        if (status == 4) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND o_id=? " +
                    "ORDER BY o_id DESC ");
            preparedStatement.setInt(1, o_id);
        }
        if (status == 5) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id ,  u_id , m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND o_id=? AND g_name=? " +
                    "ORDER BY o_id DESC ");
            preparedStatement.setInt(1, o_id);
            preparedStatement.setString(2, goodName);
        }
        if (status == 6) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND o_id=? AND u_id=? " +
                    "ORDER BY o_id DESC ");
            preparedStatement.setInt(1, o_id);
            preparedStatement.setString(2, u_id);
        }
        if (status == 7) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id AND o_id=? AND u_id=? AND g_name=? " +
                    "ORDER BY o_id DESC ");
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

    private void exitConfirmedOrder() throws SQLException {
        int o_id = -1;
        //输入为空则直接退出
        if (this.shippingPanel.getO_id().equals("")) {
            System.out.println("请输入待确认的订单编号");
            JOptionPane.showMessageDialog(null, "请输入待确认的订单编号", null, JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //输入非纯数字的订单编号直接退出
        try {
            o_id = Integer.valueOf(new String(this.shippingPanel.getO_id()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("请输入正确的订单编号");
            JOptionPane.showMessageDialog(null, "请输入正确的订单编号", "警告", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "无法查询到该订单号", "警告", JOptionPane.WARNING_MESSAGE);
        } else {
            preparedStatement = connection.prepareStatement("" +
                    "UPDATE m_order " +
                    "set status=? , exit_time=? " +
                    "WHERE o_id=?");
            preparedStatement.setString(1, "订单已出仓");
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(3, o_id);
            int i = preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("" +
                    "SELECT phone , g_id , amount , m_order.u_id " +
                    "FROM user , m_order " +
                    "WHERE m_order.u_id=user.u_id AND m_order.o_id=?");
            preparedStatement.setInt(1, o_id);
            ResultSet resultSet1 = preparedStatement.executeQuery();
            String phone = null;
            int g_id = 0;
            int amount = 0;
            String u_id = null;
            if (resultSet1.next()) {
                phone = resultSet1.getString(1);
                g_id = resultSet1.getInt(2);
                amount = resultSet1.getInt(3);
                u_id = resultSet1.getString(4);
                SqlControler.Storehouse.exit(g_id, amount, 0, u_id, phone, Client.u_id);
            } else {
                JOptionPane.showMessageDialog(null, "出仓记录写入失败", "出仓记录失败", JOptionPane.ERROR_MESSAGE);
            }


            System.out.println("出仓" + i + "条订单");
            JOptionPane.showMessageDialog(null, "出仓" + i + "条订单", "出仓成功", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshConfirmedOrderList() {
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , confirm_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已确认' AND m_order.g_id=good.g_id " +
                    "ORDER BY o_id DESC ");
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

    private void refreshGoodList() {
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT * " +
                    "FROM good " +
                    "ORDER BY g_id DESC ");
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
        if (this.entryPanel.isNewGood()) {
            int g_id = 0;
            String goodName = entryPanel.getGoodName();
            int r_amount = 0;
            float price = 0;
            String r_name = entryPanel.getContact();
            String r_phone = entryPanel.getContactPhone();
            if (goodName.equals("")) {
                JOptionPane.showMessageDialog(null, "请输入商品名称", "警告", JOptionPane.WARNING_MESSAGE);
            }
            try {
                r_amount = Integer.valueOf(this.entryPanel.getGoodAmount());//入库数量
            } catch (NumberFormatException e) {
                System.out.println("请输入正确的商品数量");
                JOptionPane.showMessageDialog(null, "请输入正确的商品数量", "警告", JOptionPane.WARNING_MESSAGE);
                e.printStackTrace();
                return;
            }
            if (r_amount <= 0) {
                JOptionPane.showMessageDialog(null, "请输入正确的商品数量", "警告", JOptionPane.WARNING_MESSAGE);
            }
            try {
                price = Float.valueOf(entryPanel.getPrice());
            } catch (NumberFormatException e) {
                System.out.println("请输入正确的商品价格");
                JOptionPane.showMessageDialog(null, "请输入正确的商品价格", "警告", JOptionPane.WARNING_MESSAGE);
                e.printStackTrace();
                return;
            }
            if (price < 0) {
                JOptionPane.showMessageDialog(null, "请输入正确的商品价格", "警告", JOptionPane.WARNING_MESSAGE);
            }
            if (r_name.equals("")) {
                System.out.println("联系人不能为空");
                JOptionPane.showMessageDialog(null, "联系人不能为空", "警告", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (r_phone.equals("")) {
                System.out.println("联系方式不能为空");
                JOptionPane.showMessageDialog(null, "联系方式不能为空", "警告", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Connection connection = SqlControler.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("" +
                        "SELECT * FROM good WHERE g_name=?");
                preparedStatement.setString(1, goodName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    System.out.println("商品已存在，请选择旧商品进行入库");
                    JOptionPane.showMessageDialog(null, "商品已存在，请选择旧商品进行入库", "警告", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("" +
                        "INSERT INTO good(g_name , amount) " +
                        "VALUES( ? , ? )");
                preparedStatement.setString(1, goodName);
                preparedStatement.setInt(2, r_amount);
                int i = preparedStatement.executeUpdate();
                System.out.println("更新了" + i + "条商品信息");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("" +
                        "SELECT * FROM good WHERE g_name=?");
                preparedStatement.setString(1, goodName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    g_id = resultSet.getInt(1);
                    System.out.println("g_id = " + g_id);
                } else {
                    System.out.println("新商品查询商品编号失败");
                    JOptionPane.showMessageDialog(null, "新商品查询商品编号失败", "警告", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                SqlControler.Storehouse.entry(g_id, r_amount, price, r_name, r_phone, Client.u_id);
            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else {
            int g_id;
            int amount;//原来的库存
            int r_amount;//入库新增数量
            float price;
            String goodName = entryPanel.getGoodName();
            String r_name = entryPanel.getContact();
            String r_phone = entryPanel.getContactPhone();
            if (r_name.equals("")) {
                System.out.println("联系人不能为空");
                JOptionPane.showMessageDialog(null, "联系人不能为空", "警告", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (r_phone.equals("")) {
                System.out.println("联系方式不能为空");
                JOptionPane.showMessageDialog(null, "联系方式不能为空", "警告", JOptionPane.WARNING_MESSAGE);
                return;
            }
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
                        if (r_amount <= 0) {
                            JOptionPane.showMessageDialog(null, "商品数额输入错误，请输出正确的数字", "警告", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        try {
                            price = Float.valueOf(entryPanel.getPrice());
                            if (price < 0) {
                                JOptionPane.showMessageDialog(null, "价格输入错误，请输入正确的数字", "警告", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            SqlControler.Storehouse.entry(g_id, r_amount, price, r_name, r_phone, Client.u_id);
                            System.out.println("成功添加一条入库记录");
                            JOptionPane.showMessageDialog(null, "成功添加一条入库记录", "入库成功", JOptionPane.INFORMATION_MESSAGE);

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
                            JOptionPane.showMessageDialog(null, "价格输入错误，请输入正确的数字", "警告", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        System.out.println("商品数额输入错误，请输出正确的数字");
                        JOptionPane.showMessageDialog(null, "商品数额输入错误，请输出正确的数字", "警告", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    System.out.println("查询不到该商品");
                    JOptionPane.showMessageDialog(null, "查询不到该商品", "警告", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
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
        if (r_name.equals("")) {
            System.out.println("联系人不能为空");
            JOptionPane.showMessageDialog(null, "查询不到该商品", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (r_phone.equals("")) {
            System.out.println("联系方式不能为空");
            JOptionPane.showMessageDialog(null, "查询不到该商品", "警告", JOptionPane.WARNING_MESSAGE);
            return;
        }
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
                    if (amount < r_amount) {
                        System.out.println("商品库存不足");
                        JOptionPane.showMessageDialog(null, "商品库存不足", "警告", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    try {
                        price = Float.valueOf(exitPanel.getPrice());
                        SqlControler.Storehouse.exit(g_id, r_amount, price, r_name, r_phone, Client.u_id);
                        System.out.println("成功添加一条出库记录");
                        JOptionPane.showMessageDialog(null, "成功添加一条出库记录", "出库成功", JOptionPane.INFORMATION_MESSAGE);

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
                        JOptionPane.showMessageDialog(null, "价格输入错误，请输入正确的数字", "警告", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    System.out.println("商品数额输入错误，请输出正确的数字");
                    JOptionPane.showMessageDialog(null, "商品数额输入错误，请输出正确的数字", "警告", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                System.out.println("查询不到该商品");
                JOptionPane.showMessageDialog(null, "查询不到该商品", "警告", JOptionPane.WARNING_MESSAGE);
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
