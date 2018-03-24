package com.util;

//import

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetTableModel extends AbstractTableModel {
    private ResultSet resultSet;
    private ResultSetMetaData resultSetMetaData;

    public ResultSetTableModel(ResultSet aResultSet) {
        resultSet = aResultSet;
        try {
            resultSetMetaData = aResultSet.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        try {
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        try {
            return resultSetMetaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            resultSet.absolute(rowIndex + 1);
            return resultSet.getObject(columnIndex + 1);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        try {
            return resultSetMetaData.getColumnName(column + 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }
}
