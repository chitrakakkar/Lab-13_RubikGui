package com.Chitra;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by chitrakakkar on 4/23/16.
 */
public class RubikModel extends AbstractTableModel
{
    ResultSet resultSet;
    int numberOfRows;
    int numberOfColumns;

    public RubikModel(ResultSet rs)
    {
        this.resultSet = rs;
        setUp();
    }

    private void setUp() {
        countRows();
        try {
            numberOfColumns = resultSet.getMetaData().getColumnCount();
        } catch (SQLException se) {
            System.out.println("Error counting columns" + se);
        }

    }

    public void updateResultSet(ResultSet newRs) {
        resultSet = newRs;
        setUp();

    }


    private void countRows() {
        numberOfRows = 0;
        try {
            resultSet.beforeFirst();
            while (resultSet.next())
            {
                numberOfRows++;

            }
            resultSet.beforeFirst();

        } catch (SQLException se) {
            System.out.println("Error counting rows " + se);
        }

    }

    @Override
    public int getRowCount()
    {

        countRows();
        return numberOfRows;
    }

    @Override
    public int getColumnCount() {
        return numberOfColumns;
    }

    @Override
    //Fetch value for the cell at (row, col).
    //The table will call toString on the object, so it's a good idea
    //to return a String or something that implements toString in a useful way
    public Object getValueAt(int row, int col) {
        try {
            //Move to this row in the result set. Rows are numbered 1, 2, 3...
            resultSet.absolute(row + 1);
            //And get the column at this row. Columns numbered 1, 2, 3...
            Object o = resultSet.getObject(col + 1);
            return o.toString();
        } catch (SQLException sqle) {
            //Display the text of the error message in the cell
            return sqle.toString();
        }


    }

    public void setValueAt(Object newValue, int row, int col) {
        double newTime;
        try {
            newTime = Double.parseDouble(newValue.toString());
            if (newTime < 0) {
                throw new NumberFormatException("Time taken can't be negative");
            }

        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(null, "Please enter a double number");
            return;

        }
        try {
            resultSet.absolute(row + 1);
            resultSet.updateDouble(Main.Time_Taken, newTime);
            resultSet.updateRow();
            fireTableDataChanged();
        } catch (SQLException e) {
            System.out.println("error changing rating " + e);
        }


    }

    public boolean isCellEditable(int row, int col) {
        if (col == 2) {
            return true;
        }
        return false;
    }

    public boolean deleteRow(int row) {
        try {
            resultSet.absolute(row + 1);
            resultSet.deleteRow();
            fireTableDataChanged();
            return true;
        } catch (SQLException se) {
            System.out.println("Delete row error " + se);
            return false;
        }

    }

    public boolean insertRow(String solver, double timeTaken) {
        try {
            resultSet.moveToInsertRow();
            resultSet.updateString(Main.Solver_Name, solver);
            resultSet.updateDouble(Main.Time_Taken, timeTaken);
            resultSet.insertRow();
            resultSet.moveToCurrentRow();
            fireTableDataChanged();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding row");
            System.out.println(e);
            return false;

        }
    }
}
