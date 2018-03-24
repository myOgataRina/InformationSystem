package com.GUI;

import javax.swing.*;
import java.awt.*;

public class CustomerUI {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JPanel myOrderPanel;
    private JPanel newOrderPanel;
    private JScrollPane queryPanel;

    //我的订单页面
    private JPanel searchPanel;
    private JPanel combinePanel;
    private JLabel searchString;
    private JTextField searchField;
    private JButton queryButton;

    //新建订单页面
    private JPanel submitNewOrderPanel;
    private JLabel goodNameLabel;
    private JTextField goodNameTextField;
    private JLabel amountLabel;
    private JTextField amountTextField;
    private JButton submitNewOrderButton;

    public void init() {
        frame = new JFrame("客户界面");
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        myOrderPanel = new JPanel();
        newOrderPanel = new JPanel();
        queryPanel = new JScrollPane();
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

        combinePanel.add(searchString, BorderLayout.WEST);
        combinePanel.add(searchField, BorderLayout.CENTER);

        searchPanel.add(combinePanel, BorderLayout.CENTER);
        searchPanel.add(queryButton, BorderLayout.NORTH);

        myOrderPanel.add(searchPanel, BorderLayout.NORTH);
        myOrderPanel.add(queryPanel, BorderLayout.CENTER);

        tabbedPane.add("我的订单", myOrderPanel);
        tabbedPane.add("新建订单", newOrderPanel);
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        CustomerUI UI = new CustomerUI();
        UI.init();
    }
}
