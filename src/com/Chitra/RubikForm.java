package com.Chitra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by chitrakakkar on 4/23/16.
 */
public class RubikForm extends JFrame implements WindowListener
{
    private JTable rubikTable;
    private JPanel rootPanel;
    private JButton ADDButton;
    private JButton DELETEButton;
    private JButton quitButton;
    private JTextField rubikSolverNameText;
    private JLabel RubikSolverLabel;
    private JTextField timeTakenText;

    protected RubikForm (RubikModel rm)
    {
        setContentPane(rootPanel);
        pack();
       setTitle("rubikSolverGui");
        addWindowListener(this);
        setVisible(true);
        setSize(500,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        rubikTable.setModel(rm);
        rubikTable.setGridColor(Color.black);

        ADDButton.addActionListener(e1 ->
        {

            String SolverName = rubikSolverNameText.getText();
            if(SolverName == null || SolverName.trim().equals(""))
            {
                JOptionPane.showMessageDialog(rootPane,"Please enter the solver name");
                return;
            }
            double timeTAKEN;
            try
            {
                timeTAKEN = Double.parseDouble(timeTakenText.getText());
                if(timeTAKEN <0.0)
                {
                   throw new NumberFormatException("Time can't be negative") ;
                }
            }
            catch (NumberFormatException ne)
            {
               JOptionPane.showMessageDialog(rootPane,"Please enter a double number");
                return;
            }
            boolean insertedRow = rm.insertRow(SolverName,timeTAKEN);

            if(!insertedRow)
            {
                JOptionPane.showMessageDialog(rootPane, "Error adding new row");
            }
        }
        );

        quitButton.addActionListener(e ->
        {
        Main.shutdown();
    });

        DELETEButton.addActionListener(e ->
        {
            int currentRow = rubikTable.getSelectedRow();
            if(currentRow == -1)
            {
                JOptionPane.showMessageDialog(rootPane,"Please choose a row to delete");
            }
            boolean deleted = rm.deleteRow(currentRow);
            if(deleted)
            {
               Main.loadAllRowAndColumns();
            }
            else {
                JOptionPane.showMessageDialog(rootPane, "Error deleting movie");
            }

        });
    }

    public void windowClosing(WindowEvent e) {
        System.out.println("Window closing");
        Main.shutdown();
    }
    public void windowClosed(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}

}
