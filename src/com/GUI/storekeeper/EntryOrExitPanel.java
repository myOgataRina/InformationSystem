package com.GUI.storekeeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EntryOrExitPanel extends JPanel {
    private JTextField goodNameTextField = new JTextField(50);
    private JTextField goodAmountTextField = new JTextField(20);
    private JTextField goodPriceTextField = new JTextField(20);
    private JTextField contactTextField = new JTextField(20);
    private JTextField contactPhoneTextField = new JTextField(20);
    private JButton button;

    public EntryOrExitPanel(boolean isEntry) {
        if (isEntry) {
            button = new JButton("确认入库");
        } else {
            button = new JButton("确认出库");
        }
        JLabel goodNameLabel = new JLabel("商品名称：");
        JLabel goodAmountLabel = new JLabel("商品数量：");
        JLabel goodPriceLabel = new JLabel("商品价格：");
        JLabel contactLabel = new JLabel("联系人：");
        JLabel contactPhoneLabel = new JLabel("联系方式：");


        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(gb);
        gbc.insets = new Insets(30, 10, 0, 10);
        gbc.gridwidth = 1;
        gb.setConstraints(goodNameLabel, gbc);
        this.add(goodNameLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(goodNameTextField, gbc);
        this.add(goodNameTextField);
        gbc.gridwidth = 1;
        gb.setConstraints(goodAmountLabel, gbc);
        this.add(goodAmountLabel);
        gb.setConstraints(goodAmountTextField, gbc);
        this.add(goodAmountTextField);
        gb.setConstraints(goodPriceLabel, gbc);
        this.add(goodPriceLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(goodPriceTextField, gbc);
        this.add(goodPriceTextField);
        gbc.gridwidth = 1;
        gb.setConstraints(contactLabel, gbc);
        this.add(contactLabel);
        ;
        gb.setConstraints(contactTextField, gbc);
        this.add(contactTextField);
        gb.setConstraints(contactPhoneLabel, gbc);
        this.add(contactPhoneLabel);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(contactPhoneTextField, gbc);
        this.add(contactPhoneTextField);
        gb.setConstraints(button, gbc);
        this.add(button);
    }

    public void setButtonListener(ActionListener listener){
        button.addActionListener(listener);
    }

    public String getGoodName(){
        return goodNameTextField.getText();
    }

    public String getGoodAmount(){
        return goodAmountTextField.getText();
    }

    public String getPrice(){
        return goodPriceTextField.getText();
    }

    public String getContact(){
        return contactTextField.getText();
    }

    public String getContactPhone(){
        return contactPhoneTextField.getText();
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        EntryOrExitPanel entryOrExitPanel = new EntryOrExitPanel(true);
        jFrame.add(entryOrExitPanel);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}

