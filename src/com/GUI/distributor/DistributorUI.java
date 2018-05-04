package com.GUI.distributor;

import com.GUI.OperationUI;
import com.GUI.university.ChangeInformationPanel;
import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.sql.*;

public class DistributorUI extends OperationUI {
    private JFrame frame = new JFrame("快递员界面");
    DistributeOrderPanel distributeOrderPanel = new DistributeOrderPanel();

    public DistributorUI(){
        ChangeInformationPanel changeInformationPanel = new ChangeInformationPanel();

//        distributeOrderPanel

        changeInformationPanel.setChangePasswordButtonListener();
        changeInformationPanel.setChangeInformationButtonListener();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("订单揽件", distributeOrderPanel);
        tabbedPane.add("个人信息修改", changeInformationPanel);

        frame.add(tabbedPane);
        frame.pack();
    }

    @Override
    public void run() {
        super.run();
        frame.setVisible(true);
    }

    public void distributeExitedOrder() throws SQLException {
        int o_id = -1;
        //输入为空则直接退出
        if (this.distributeOrderPanel.getO_id().equals("")) {
            System.out.println("请输入待揽件的订单编号");
            JOptionPane.showMessageDialog(null, "请输入待揽件的订单编号", null, JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //输入非纯数字的订单编号直接退出
        try {
            o_id = Integer.valueOf(new String(this.distributeOrderPanel.getO_id()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("请输入正确的订单编号");
            JOptionPane.showMessageDialog(null, "请输入正确的订单编号", "警告", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //先查询是否存在处于出仓状态的订单，确认存在后更新订单信息
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "SELECT * FROM m_order " +
                "WHERE o_id=? AND status='订单已出仓'");
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
            preparedStatement.setString(1, "订单配送中");
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(3, o_id);
            int i = preparedStatement.executeUpdate();
            System.out.println("已揽件" + i + "条订单");
            JOptionPane.showMessageDialog(null, "揽件" + i + "条订单", "揽件成功", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //搜索已经出仓的订单
    public void searchExitOrder() throws SQLException {
        int status = 0;
        int o_id = 0;
        try {
            o_id = Integer.valueOf(this.distributeOrderPanel.getO_id());
        } catch (NumberFormatException e) {//当输入不为数字时
            e.printStackTrace();
            if (!this.distributeOrderPanel.getO_id().equals("")) {
                System.out.println("请确认输入的订单编号格式是否正确");
                JOptionPane.showMessageDialog(null, "请确认输入的订单编号格式是否正确", "警告", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        String u_id = this.distributeOrderPanel.getU_id();
        String goodName = this.distributeOrderPanel.getGoodName();
        PreparedStatement preparedStatement;
        if (!this.distributeOrderPanel.getO_id().equals("")) {
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
                "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , exit_time " +
                "FROM good , m_order " +
                "WHERE status = '订单已出仓' AND m_order.g_id=good.g_id " +
                "ORDER BY o_id DESC");
        if (status == 1) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , exit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已出仓' AND m_order.g_id=good.g_id AND g_name=? " +
                    "ORDER BY o_id DESC");
            preparedStatement.setString(1, goodName);
        }
        if (status == 2) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id ,  u_id , m_order.g_id , g_name , m_order.amount , status , exit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已出仓' AND m_order.g_id=good.g_id AND u_id=? " +
                    "ORDER BY o_id DESC");
            preparedStatement.setString(1, u_id);
        }
        if (status == 3) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , exit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已出仓' AND m_order.g_id=good.g_id AND g_name=? AND u_id=? " +
                    "ORDER BY o_id DESC");
            preparedStatement.setString(1, goodName);
            preparedStatement.setString(2, u_id);
        }
        if (status == 4) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id ,  m_order.g_id , g_name , m_order.amount , status , exit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已出仓' AND m_order.g_id=good.g_id AND o_id=? " +
                    "ORDER BY o_id DESC");
            preparedStatement.setInt(1, o_id);
        }
        if (status == 5) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id ,  u_id , m_order.g_id , g_name , m_order.amount , status , exit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已出仓' AND m_order.g_id=good.g_id AND o_id=? AND g_name=? " +
                    "ORDER BY o_id DESC");
            preparedStatement.setInt(1, o_id);
            preparedStatement.setString(2, goodName);
        }
        if (status == 6) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , exit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已出仓' AND m_order.g_id=good.g_id AND o_id=? AND u_id=? " +
                    "ORDER BY o_id DESC");
            preparedStatement.setInt(1, o_id);
            preparedStatement.setString(2, u_id);
        }
        if (status == 7) {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT o_id , u_id , m_order.g_id , g_name , m_order.amount , status , exit_time " +
                    "FROM good , m_order " +
                    "WHERE status = '订单已出仓' AND m_order.g_id=good.g_id AND o_id=? AND u_id=? AND g_name=? " +
                    "ORDER BY o_id DESC");
            preparedStatement.setInt(1, o_id);
            preparedStatement.setString(2, u_id);
            preparedStatement.setString(3, goodName);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
        JTable jTable = this.distributeOrderPanel.getTable();
        jTable.setModel(resultSetTableModel);
        resultSetTableModel.fireTableStructureChanged();
        resultSetTableModel.fireTableDataChanged();
        jTable.getColumnModel().getColumn(0).setHeaderValue("订单编号");
        jTable.getColumnModel().getColumn(1).setHeaderValue("订单用户");
        jTable.getColumnModel().getColumn(2).setHeaderValue("商品编号");
        jTable.getColumnModel().getColumn(3).setHeaderValue("商品名称");
        jTable.getColumnModel().getColumn(4).setHeaderValue("订购数量");
        jTable.getColumnModel().getColumn(5).setHeaderValue("订单状态");
        jTable.getColumnModel().getColumn(6).setHeaderValue("订单出仓时间");
    }
}
