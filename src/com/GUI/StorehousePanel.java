package com.GUI;

import com.object.Storehouse;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StorehousePanel extends JPanel {

    private JButton inputButton = new JButton("入库");
    private JButton outputButton = new JButton("出库");
    private JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
    private JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);

    public void init(){
        separator.setPreferredSize(new Dimension(800,50));
        separator2.setPreferredSize(new Dimension(800, 50));


    }


    public static void main(String[] args) {

    }
}
