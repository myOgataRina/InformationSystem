package com.GUI;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{

    JFrame frame = null;
    JMenuBar menuBar = null;
    JPanel  panel = null;


    public MainFrame(String frameName){
        frame = new JFrame(frameName);
        menuBar = new JMenuBar();
        panel = new JPanel();

    }

    public void addToMenuBar(Component component){
        this.menuBar.add(component);
    }

    public void init(){
        frame.add(menuBar,BorderLayout.NORTH);
        frame.add(panel,BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

//    public static void main(String[] args) {
//        JMenuItem menuItem = new JMenuItem("开始");
//        JMenu operation = new JMenu("操作");
//        operation.add(new JMenuItem("删除学生"));
//        MainFrame frame = new MainFrame("frame name");
//        frame.addToMenuBar(menuItem);
//        frame.addToMenuBar(operation);
//        frame.init();
//    }
}
