// Classname: curveUI
// Description: This class is used curve grade for professor UI
// Author: Haoji Ni

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Vector;

public class curveUI {
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JButton averageStudentButton;
    private JButton goodStudentButton;
    private JButton failingStudentButton;
    private JButton cancelButton;
    private JButton confirmButton;
    private JComboBox comboBox1;
    private JLabel label4;
    private JPanel panel1;
    private JButton everyonebutton;

    int selected_mode = 0;


    Query sql = new Query();

    public curveUI(String course_name) {
        // get original data from database
        Vector<Vector<String>> before_change = initialize_students(course_name);

        // initialize the GUI
        JFrame frame = new JFrame("Curve");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 400);
        frame.setLocation(960, 540);
        frame.setVisible(true);
        frame.setResizable(false);
        Vector<Integer> display_data = counter(before_change);
        label1.setText("Number of people will get A: " + display_data.get(0));
        label2.setText("Number of people will get B: " + display_data.get(1));
        label3.setText("Number of people will get C: " + display_data.get(2));
        label4.setText("Number of people will get D or below: " + display_data.get(3));


        // add curve grade to only failing students
        failingStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<Vector<String>> after_change = before_change;
                int percent = get_percentage();
                for (int i = 0; i < after_change.size(); i++) {

                    int temp = 0;
                    if (after_change.get(i).get(1).equals("W")) {
                        temp = -999;
                    } else {
                        temp = Integer.parseInt(after_change.get(i).get(1));
                    }
                    if (temp < 70 && temp >= 0) {
                        temp += percent;
                        if (temp > 100) {
                            temp = 100;
                        }
                        after_change.get(i).set(1, String.valueOf(temp));
                    }
                }
                Vector<Integer> after_display_data = counter(after_change);
                label1.setText("Number of people will get A: " + after_display_data.get(0));
                label2.setText("Number of people will get B: " + after_display_data.get(1));
                label3.setText("Number of people will get C: " + after_display_data.get(2));
                label4.setText("Number of people will get D or below: " + after_display_data.get(3));
                selected_mode = 1;
            }
        });

        // add curve grade to only good students(A)
        goodStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<Vector<String>> after_change = before_change;
                int percent = get_percentage();
                for (int i = 0; i < after_change.size(); i++) {

                    int temp = 0;
                    if (after_change.get(i).get(1).equals("W")) {
                        temp = -999;
                    } else {
                        temp = Integer.parseInt(after_change.get(i).get(1));
                    }
                    if (temp >= 90) {
                        temp += percent;
                        if (temp > 100) {
                            temp = 100;
                        }
                        if (temp < -100) {
                            after_change.get(i).set(1, "W");
                        } else {
                            after_change.get(i).set(1, String.valueOf(temp));
                        }
                    }
                }
                Vector<Integer> after_display_data = counter(after_change);
                label1.setText("Number of people will get A: " + after_display_data.get(0));
                label2.setText("Number of people will get B: " + after_display_data.get(1));
                label3.setText("Number of people will get C: " + after_display_data.get(2));
                label4.setText("Number of people will get D or below: " + after_display_data.get(3));
                selected_mode = 2;
            }
        });

        // add curve grade to only average students(B,C)
        averageStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<Vector<String>> after_change = before_change;
                int percent = get_percentage();
                for (int i = 0; i < after_change.size(); i++) {

                    int temp = 0;
                    if (after_change.get(i).get(1).equals("W")) {
                        temp = -999;
                    } else {
                        temp = Integer.parseInt(after_change.get(i).get(1));
                    }
                    if (temp >= 70 && temp < 90) {
                        temp += percent;
                        if (temp > 100) {
                            temp = 100;
                        }
                        after_change.get(i).set(1, String.valueOf(temp));
                    }
                }
                Vector<Integer> after_display_data = counter(after_change);
                label1.setText("Number of people will get A: " + after_display_data.get(0));
                label2.setText("Number of people will get B: " + after_display_data.get(1));
                label3.setText("Number of people will get C: " + after_display_data.get(2));
                label4.setText("Number of people will get D or below: " + after_display_data.get(3));
                selected_mode = 3;
            }
        });

        // add curve grade to everyone
        everyonebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<Vector<String>> after_change = before_change;
                int percent = get_percentage();
                for (int i = 0; i < after_change.size(); i++) {

                    int temp = 0;
                    if (after_change.get(i).get(1).equals("W")) {
                        temp = -999;
                    } else {
                        temp = Integer.parseInt(after_change.get(i).get(1));
                    }
                    temp += percent;
                    if (temp > 100) {
                        temp = 100;
                    }
                    after_change.get(i).set(1, String.valueOf(temp));
                }
                Vector<Integer> after_display_data = counter(after_change);
                label1.setText("Number of people will get A: " + after_display_data.get(0));
                label2.setText("Number of people will get B: " + after_display_data.get(1));
                label3.setText("Number of people will get C: " + after_display_data.get(2));
                label4.setText("Number of people will get D or below: " + after_display_data.get(3));
                selected_mode = 4;
            }
        });

        // close the window
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // save the changes, send query to database and update the current grade
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (selected_mode) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "Please select a mode");
                        break;
                    case 1:
                        sql.update_query("UPDATE current_grade SET Grade = current_grade.Grade + " +
                                get_percentage() + " WHERE Course_ID = '" + course_name + "' AND Grade < 70;");
                        break;
                    case 2:
                        sql.update_query("UPDATE current_grade SET Grade = current_grade.Grade + " +
                                get_percentage() + " WHERE Course_ID = '" + course_name + "' AND Grade >= 90;");
                        break;
                    case 3:
                        sql.update_query("UPDATE current_grade SET Grade = current_grade.Grade + " +
                                get_percentage() + " WHERE Course_ID = '" + course_name + "' AND Grade >= 70 AND Grade < 90;");
                        break;
                    case 4:
                        sql.update_query("UPDATE current_grade SET Grade = current_grade.Grade + " +
                                get_percentage() + " WHERE Course_ID = '" + course_name + "';");
                        break;
                }
            frame.dispose();
            }
        });
    }

    // initialize the table and get the data from database
    public Vector<Vector<String>> initialize_students(String course_name) {
        String query = "SELECT student.First_name," +
                "student.Middle_name,student.Last_name,current_grade." +
                "Grade, current_grade.UIN FROM student " +
                "INNER JOIN current_grade ON student.UIN = current_grade.UIN AND current_grade.Course_ID = '" + course_name + "';";
        Vector<Vector<String>> temp = sql.execute_gettable_query(query,5);
        Vector<Vector<String>> result = new Vector<Vector<String>>();
        for (Vector<String> strings : temp) {
            Vector<String> temp1 = new Vector<String>();
            if (Objects.equals(strings.get(1), ""))
                temp1.add(strings.get(0) + " " + strings.get(2));
            else
                temp1.add(strings.get(0) + " " + strings.get(1) + " " + strings.get(2));
            if(strings.get(3).equals("-999"))
                temp1.add("W");
            else
                temp1.add(strings.get(3));
            result.add(temp1);
        }
        return result;
    }

    // Braindead way to get the percentage of curve grade
    public int get_percentage() {
        int result = 0;
        if (comboBox1.getSelectedItem() == "1%") {
            return 1;
        } else if (comboBox1.getSelectedItem() == "2%") {
            return 2;
        } else if (comboBox1.getSelectedItem() == "5%") {
            return 5;
        } else if (comboBox1.getSelectedItem() == "10%") {
            return 10;
        } else if (comboBox1.getSelectedItem() == "15%") {
            return 15;
        } else if (comboBox1.getSelectedItem() == "20%") {
            return 20;
        } else if (comboBox1.getSelectedItem() == "100%") {
            return 100;
        }
        return result;
    }

    // Simple counter to count the number of people who get A, B, C, D or below
    public Vector<Integer> counter(Vector<Vector<String>> source) {
        int A = 0;
        int B = 0;
        int C = 0;
        int DF = 0;
        for(int i =0; i < source.size(); i++){
            int temp = 0;
            if (source.get(i).get(1).equals("W")) {
                temp = -999;
            } else {
                temp = Integer.parseInt(source.get(i).get(1));
            }
            if(temp >=90)
                A++;
            else if(temp >=80)
                B++;
            else if(temp >=70)
                C++;
            else if(temp >=0)
                DF++;
        }
        Vector<Integer> result = new Vector<Integer>();
        result.add(A);
        result.add(B);
        result.add(C);
        result.add(DF);
        return result;
    }
}


