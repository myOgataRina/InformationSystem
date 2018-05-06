package com.GUI.manager;

import com.GUI.OperationUI;
import com.GUI.university.ChangeInformationPanel;
import com.util.ResultSetTableModel;
import com.util.SqlControler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerUI extends OperationUI {
    private JFrame frame = new JFrame("经理界面");
    private String userName = null;
    private AdministrateEmployeePanel administrateEmployeePanel = new AdministrateEmployeePanel();

    public ManagerUI() {
        JTabbedPane tabbedPane = new JTabbedPane();

        administrateEmployeePanel.setSearchButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    searchEmployee();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        administrateEmployeePanel.setAddButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEmployeeFrame addEmployeeFrame = new AddEmployeeFrame(administrateEmployeePanel.getUserName());
                addEmployeeFrame.addWindowListener(new WindowListener() {
                    @Override
                    public void windowOpened(WindowEvent e) {

                    }

                    @Override
                    public void windowClosing(WindowEvent e) {

                    }

                    @Override
                    public void windowClosed(WindowEvent e) {
                        System.out.println("窗口关闭");
                        try {
                            refreshEmployeeList();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void windowIconified(WindowEvent e) {

                    }

                    @Override
                    public void windowDeiconified(WindowEvent e) {

                    }

                    @Override
                    public void windowActivated(WindowEvent e) {

                    }

                    @Override
                    public void windowDeactivated(WindowEvent e) {

                    }
                });
            }
        });

        administrateEmployeePanel.setRemoveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RemoveEmployeeFrame removeEmployeeFrame = new RemoveEmployeeFrame(administrateEmployeePanel.getUserName());
                    removeEmployeeFrame.addWindowListener(new WindowListener() {
                        @Override
                        public void windowOpened(WindowEvent e) {

                        }

                        @Override
                        public void windowClosing(WindowEvent e) {

                        }

                        @Override
                        public void windowClosed(WindowEvent e) {
                            try {
                                refreshEmployeeList();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }

                        @Override
                        public void windowIconified(WindowEvent e) {

                        }

                        @Override
                        public void windowDeiconified(WindowEvent e) {

                        }

                        @Override
                        public void windowActivated(WindowEvent e) {

                        }

                        @Override
                        public void windowDeactivated(WindowEvent e) {

                        }
                    });
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        administrateEmployeePanel.setAdjustButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TransferEmployeeFrame transferEmployeeFrame = new TransferEmployeeFrame(administrateEmployeePanel.getUserName());
                    transferEmployeeFrame.addWindowListener(new WindowListener() {
                        @Override
                        public void windowOpened(WindowEvent e) {

                        }

                        @Override
                        public void windowClosing(WindowEvent e) {

                        }

                        @Override
                        public void windowClosed(WindowEvent e) {
                            try {
                                refreshEmployeeList();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }

                        @Override
                        public void windowIconified(WindowEvent e) {

                        }

                        @Override
                        public void windowDeiconified(WindowEvent e) {

                        }

                        @Override
                        public void windowActivated(WindowEvent e) {

                        }

                        @Override
                        public void windowDeactivated(WindowEvent e) {

                        }
                    });
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        });

        ChangeInformationPanel changeInformationPanel = new ChangeInformationPanel();
        changeInformationPanel.setChangeInformationButtonListener();
        changeInformationPanel.setChangePasswordButtonListener();

        tabbedPane.add("员工管理", administrateEmployeePanel);
        tabbedPane.add("个人信息修改", changeInformationPanel);

        frame.add(tabbedPane);
        frame.pack();
    }

    private void searchEmployee() throws SQLException {
        userName = administrateEmployeePanel.getUserName();
        String selectedType = administrateEmployeePanel.getSelectedType();
        String sql;

        if (userName.equals("")) {
            sql = "" +
                    "SELECT u_id , power_cn , phone " +
                    "FROM user , translate " +
                    "WHERE power=power_en ";
        } else {
            sql = "" +
                    "SELECT u_id , power_cn , phone " +
                    "FROM user , translate " +
                    "WHERE power=power_en AND u_id=? ";
        }
        if (selectedType.equals("全体员工")) {
            sql = sql + "AND power IN ('manager','salesman','storekeeper','distributor')";
        } else if (selectedType.equals("经理")) {
            sql = sql + "AND power='manager' ";
        } else if (selectedType.equals("业务员")) {
            sql = sql + "AND power='salesman' ";
        } else if (selectedType.equals("仓库管理员")) {
            sql = sql + "AND power='storekeeper' ";
        } else if (selectedType.equals("配送员")) {
            sql = sql + "AND power='distributor' ";
        }
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (!userName.equals("")) {
            preparedStatement.setString(1, userName);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
        JTable table = administrateEmployeePanel.getTable();
        table.setModel(resultSetTableModel);
        resultSetTableModel.fireTableDataChanged();
        resultSetTableModel.fireTableStructureChanged();
        table.getColumnModel().getColumn(0).setHeaderValue("员工名");
        table.getColumnModel().getColumn(1).setHeaderValue("岗位");
        table.getColumnModel().getColumn(2).setHeaderValue("联系电话");
    }

    private void refreshEmployeeList() throws SQLException {
        String sql = "" +
                "SELECT u_id , power_cn , phone " +
                "FROM user , translate " +
                "WHERE power=power_en AND power IN ('manager','salesman','storekeeper','distributor')";
        Connection connection = SqlControler.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetTableModel resultSetTableModel = new ResultSetTableModel(resultSet);
        JTable table = administrateEmployeePanel.getTable();
        table.setModel(resultSetTableModel);
        resultSetTableModel.fireTableDataChanged();
        resultSetTableModel.fireTableStructureChanged();
        table.getColumnModel().getColumn(0).setHeaderValue("员工名");
        table.getColumnModel().getColumn(1).setHeaderValue("岗位");
        table.getColumnModel().getColumn(2).setHeaderValue("联系电话");
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void run() {
        super.run();
        frame.setVisible(true);
    }
}
