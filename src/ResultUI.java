// Classname: ResultUI
// Description: This class is used to display complex query result
// Author: Haoji Ni

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class ResultUI {
    private JPanel panel1;
    private JTable table1;

    Query sql = new Query();

    public ResultUI(String query, int type) {
        // initialize the panel
        JFrame frame = new JFrame("Result");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(650, 400);
        frame.setLocation(960, 540);
        frame.setVisible(true);
        frame.setResizable(false);
        System.out.println("Debug: Display result");

        // first complex query
        if (type == 1) {
            Vector<Vector<String>> data = sql.execute_gettable_query(query,3);
            DefaultTableModel table1_model = (DefaultTableModel)table1.getModel();
            table1_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
            table1_model.addColumn("Course ID");
            table1_model.addColumn("UIN");
            table1_model.addColumn("Highest Grade");
            for (int i = 0; i < data.size(); i++) {
                table1_model.insertRow(i, data.get(i));
            }
            table1.setModel(table1_model);
        } else if (type == 2) {
            // second complex query
            Vector<Vector<String>> data = sql.execute_gettable_query(query,2);
            DefaultTableModel table1_model = (DefaultTableModel)table1.getModel();
            table1_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
            table1_model.addColumn("UIN");
            table1_model.addColumn("Grade");
            for (int i = 0; i < data.size(); i++) {
                table1_model.insertRow(i, data.get(i));
            }
            table1.setModel(table1_model);
        } else if (type == 3) {
            // third complex query
            Vector<Vector<String>> data = sql.execute_gettable_query(query,2);
            DefaultTableModel table1_model = (DefaultTableModel)table1.getModel();
            table1_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
            table1_model.addColumn("Course ID");
            table1_model.addColumn("Taken amount");
            for (int i = 0; i < data.size(); i++) {
                table1_model.insertRow(i, data.get(i));
            }
            table1.setModel(table1_model);
        }


    }
}
