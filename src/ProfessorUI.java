// Classname: ProfessorUI
// Description: This class is used to display the professor's UI
// and handle the professor's actions
// Author: Haoji Ni

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.Vector;



public class ProfessorUI {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTable table1;
    private JTable table2;
    private JButton releaseHomeworkButton;
    private JButton curveGradeButton;
    private JButton dropStudentButton;
    private JButton giveFinalGradeButton;
    private JButton logOutButton;
    private JPanel HiddenJPanel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton undoButton;
    private JButton submitButton;
    private JButton complexQuery2Button;
    private JButton refreshButton;
    private JButton searchButton;
    private JLabel imagelabel1;
    private JButton complexQuery1Button;
    private JLabel welcomemessagelabel;
    private JLabel titlelabel;
    private JButton editCurrentGradeButton;
    private JLabel resultlabel;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JComboBox statuscombobox;
    private JButton deleteHomeworkButton;

    Query sql = new Query();
    Vector<String> data_for_undo = new Vector<String>();
    String Selected_student = "";
    String Selected_student_grade = "";
    String course_n = "";

    // Boolean to check if the user is adding homework
    int adding_course = 0;

    // Place to store the student name
    Vector<Integer> student_list = new Vector<Integer>();

    public ProfessorUI(int UIN) {
        // Initialize the components
        JFrame frame = new JFrame("Professor Panel");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800, 550);
        frame.setLocation(960, 540);
        frame.setVisible(true);
        frame.setResizable(false);
        textField1.setEnabled(false);

        // Get the course name
        Vector<Vector<String>> temp1 = sql.execute_gettable_query(
                "SELECT Course_ID FROM course_member WHERE UIN = " + UIN,1);
        Vector<Vector<String>> temp2 = sql.execute_gettable_query(
                "SELECT First_name, Middle_name, Last_name FROM professor WHERE UIN = " + UIN,3);
        String course_name = temp1.get(0).get(0);
        course_n = course_name;
        System.out.println("Debug: Course ID is " + course_name );

        // Get the professor name and display welcome message
        String professor_name;
        if (temp2.get(0).get(1) == null || temp2.get(0).get(1).equals(""))
            professor_name = temp2.get(0).get(0) + " " + temp2.get(0).get(2);
        else
            professor_name = temp2.get(0).get(0) + " " + temp2.get(0).get(1) + " " + temp2.get(0).get(2);
        System.out.println("Debug: Professor name is " + professor_name);
        welcomemessagelabel.setText("Welcome back " + professor_name + ", you are teaching " + course_name);


        // initialize the table
        initialize_student_panel();
        initialize_homework_panel();

        // initialize the panel

        imagelabel1.setIcon(new ImageIcon("src/images/professor.gif"));
        System.out.println("Debug: Initialize professor panel");
        resultlabel.setText("");
        textField1.setEnabled(false);
        textField2.setEnabled(false);
        textField3.setEnabled(false);
        textField4.setEnabled(false);
        statuscombobox.setEnabled(false);


        // Function to release homework, will disable all buttons but log out and submit
        // clear the textfield and let the user add homework
        releaseHomeworkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adding_course = 1;
                clear_text();
                disable_buttons();
                textField1.setEnabled(false);
                textField2.setEnabled(true);
                textField3.setEnabled(true);
                textField4.setEnabled(true);
                statuscombobox.setEnabled(true);
                submitButton.setActionCommand("release_homework");
                refreshButton.setText("Cancel");
                refreshButton.setActionCommand("cancel_release_homework");
                submitButton.setEnabled(true);
                refreshButton.setEnabled(true);
            }
        });


        // Change student grade, must require user to select a student first
        editCurrentGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If not selected a student, show error message
                if (Selected_student.equals("") || table2.getSelectedRow() == -1) {
                    error_message("Please select a student row");
                    return;
                }
                // professor cant change a student's grade if student dropped the course
                if (Objects.equals(table2.getValueAt(table2.getSelectedRow(), 1).toString(), "W")) {
                    error_message("This student already dropped");
                    return;
                }

                // windows for ask the user to input the new grade
                String temp = (String)JOptionPane.showInputDialog(
                        null,
                        "Please enter the new grade for " + Selected_student,
                        "Change Grade",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        ""
                );
                if (temp == null || temp.equals("") || check_is_digit(temp)) {
                    error_message("Please enter a number in percentage");
                    return;
                }

                // update the grade in the database
                int result = Integer.parseInt(temp.trim());
                if (result < 0 || result > 100) {
                    error_message("Please enter a valid grade");
                    return;
                }
                String query = "UPDATE current_grade SET Grade = " + result +
                        " WHERE UIN = " + student_list.get(table2.getSelectedRow()) +
                        " AND Course_ID = '" + course_n + "';";
                sql.update_query(query);
                initialize_student_panel();
                resultlabel.setText("Grade changed successfully");
            }
        });


        // Function will submit final grade to the database
        // also delete homework, current grade, and course member
        giveFinalGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = "";
                String result = "";
                for (int i = 0; i < student_list.size(); i++) {
                    int grade = 0;
                    if (table2.getValueAt(i, 1).toString().equals("W")) {
                        result = "W";
                    } else {
                        grade = Integer.parseInt(table2.getValueAt(i, 1).toString());
                        if (grade >= 90) {
                            result = "A";
                        } else if (grade >= 80) {
                            result = "B";
                        } else if (grade >= 70) {
                            result = "C";
                        } else if (grade >= 60) {
                            result = "D";
                        } else {
                            result = "F";
                        }
                    }

                    // execute the query
                    query = "INSERT INTO final_grade (UIN, Course, Grade) VALUES (" +
                            student_list.get(i) + ", '" + course_n + "', '" + result + "');";
                    sql.update_query(query);
                    query = "DELETE FROM current_grade WHERE UIN = " + student_list.get(i) +
                            " AND Course_ID = '" + course_n + "';";
                    sql.update_query(query);
                    query = "DELETE FROM course_member WHERE UIN = " + student_list.get(i) +
                            " AND Course_ID = '" + course_n + "';";
                    sql.update_query(query);
                }

                // delete homework and professor from this course
                query = "DELETE FROM homework WHERE Course_ID = '" + course_n + "';";
                sql.update_query(query);
                query = "DELETE FROM course_member WHERE UIN = " + UIN + ";";
                sql.update_query(query);

                // close window
                JOptionPane.showMessageDialog(null, "Grades have been released, course will be closed");
                frame.dispose();
                LoginUI loginUI = new LoginUI();
            }
        });

        // This function allows the professor to drop a student from the course
        // must have a reason for dropping the student
        dropStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // if not selected a student, show error message
                if (Selected_student.equals("") || table2.getSelectedRow() == -1) {
                    error_message("Please select a student row");
                    return;
                }

                // ask for the reason
                String temp = (String)JOptionPane.showInputDialog(
                        null,
                        "Please enter the reason for dropping " + Selected_student,
                        "Drop Student",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        ""
                );
                if (temp == null || temp.equals("")) {
                    error_message("Please enter a reason");
                    return;
                }

                // update the database
                String result = capitalize(temp.trim());
                String query = "INSERT INTO dropped_student (UIN, Course_ID, Executor, Reason) VALUES (" +
                        student_list.get(table2.getSelectedRow()) + ", '" + course_n + "', '" + UIN + "', '" + result + "');";
                sql.update_query(query);
                query = "UPDATE current_grade SET Grade = -999 WHERE UIN = " + student_list.get(table2.getSelectedRow()) +
                        " AND Course_ID = '" + course_n + "';";
                sql.update_query(query);
                query = "DELETE FROM course_member WHERE UIN = " + student_list.get(table2.getSelectedRow()) +
                        " AND Course_ID = '" + course_n + "';";
                sql.update_query(query);
                sql.update_query("UPDATE course SET Seats_left = seats_left + 1 WHERE Course_ID = '" + course_n + "';");
                initialize_student_panel();
                resultlabel.setText("Dropped student successfully");
            }
        });

        // function to call log out UI and do the curve stuff
        curveGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curveUI curve = new curveUI(course_n);
            }
        });


        // Log out to the login UI
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LoginUI();
            }
        });

        // Restore the change that user made
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField1.setText(data_for_undo.get(0));
                textField2.setText(data_for_undo.get(1));
                textField3.setText(data_for_undo.get(2));
                textField4.setText(data_for_undo.get(3));
                statuscombobox.setSelectedItem(data_for_undo.get(4));
            }
        });

        // Save the change that user made
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(e.getActionCommand(), "release_homework")) {
                    // exception handling for empty fields
                    if (textField2.getText().trim().equals("")) {
                        resultlabel.setText("Please enter a valid number");
                        resultlabel.setForeground(Color.red);
                        label2.setForeground(Color.red);
                        textField2.setText("");
                        return;
                    }
                    int total_points = Integer.parseInt(textField2.getText().trim());
                    if (total_points < 0) {
                        resultlabel.setText("Please enter a valid number");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label2.setForeground(Color.red);
                        textField2.setText("");
                        return;
                    }
                    if (textField3.getText().trim().equals("")) {
                        resultlabel.setText("Please enter a valid number");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label3.setForeground(Color.red);
                        textField3.setText("");
                        return;
                    }
                    String homework_title = textField3.getText().trim();
                    if (textField4.getText().trim().equals("")) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    String temp = textField4.getText().trim();
                    if (check_is_digit(temp)) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    int temp1 = Integer.parseInt(temp.substring(0, 4));
                    if (temp1 < 2022 || temp1 > 9999) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    int temp2 = Integer.parseInt(temp.substring(5, 7));
                    if (temp2 < 1 || temp2 > 12) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    int temp3 = Integer.parseInt(temp.substring(8, 10));
                    if (temp3 < 1 || temp3 > 31) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    int temp4 = Integer.parseInt(temp.substring(11, 13));
                    if (temp4 < 0 || temp4 > 23) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    int temp5 = Integer.parseInt(temp.substring(14, 16));
                    if (temp5 < 0 || temp5 > 59) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    int temp6 = Integer.parseInt(temp.substring(17, 19));
                    if (temp6 < 0 || temp6 > 59) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    String due_date = temp.substring(0,4) + "-" + temp.substring(5,7) + "-" +
                            temp.substring(8,10) + " " + temp.substring(11,13) + ":" +
                            temp.substring(14,16) + ":" + temp.substring(17,19);
                    if (Objects.equals(statuscombobox.getSelectedItem(), "")) {
                        resultlabel.setText("Please select a status");
                        resultlabel.setForeground(Color.red);
                        label5.setForeground(Color.red);
                        return;
                    }
                    // update the database
                    String status = Objects.requireNonNull(statuscombobox.getSelectedItem()).toString();
                    String query = "INSERT INTO homework (Total_points, Title, Due_Date, Status, Course_ID) VALUES ( " +
                            total_points + ", '" + homework_title + "', '" + due_date + "', '" + status + "', '" + course_name + "');";
                    sql.update_query(query);

                    // clean up the panel
                    clear_color();
                    clear_text();
                    enable_buttons();
                    resultlabel.setText("Homework released successfully");
                    refreshButton.setText("Refresh");
                    refreshButton.setActionCommand("refresh");
                    submitButton.setText("Submit");
                    submitButton.setActionCommand("Submit");
                    initialize_homework_panel();
                    textField1.setEnabled(false);
                    textField2.setEnabled(false);
                    textField3.setEnabled(false);
                    textField4.setEnabled(false);
                    statuscombobox.setEnabled(false);
                    adding_course = 0;
                } else {
                    // exception handling
                    if (textField2.getText().trim().equals("")) {
                        resultlabel.setText("Please enter a valid number");
                        resultlabel.setForeground(Color.red);
                        label2.setForeground(Color.red);
                        textField2.setText("");
                        return;
                    }
                    int total_points = Integer.parseInt(textField2.getText().trim());
                    if (total_points < 0) {
                        resultlabel.setText("Please enter a valid number");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label2.setForeground(Color.red);
                        textField2.setText("");
                        return;
                    }
                    if (textField3.getText().trim().equals("")) {
                        resultlabel.setText("Please enter a valid number");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label3.setForeground(Color.red);
                        textField3.setText("");
                        return;
                    }
                    String homework_title = textField3.getText().trim();
                    if (textField4.getText().trim().equals("")) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    String temp = textField4.getText().trim();
                    if (check_is_digit(temp)) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    int temp1 = Integer.parseInt(temp.substring(0, 4));
                    if (temp1 < 2022 || temp1 > 9999) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    int temp2 = Integer.parseInt(temp.substring(5, 7));
                    if (temp2 < 1 || temp2 > 12) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    int temp3 = Integer.parseInt(temp.substring(8, 10));
                    if (temp3 < 1 || temp3 > 31) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    int temp4 = Integer.parseInt(temp.substring(11, 13));
                    if (temp4 < 0 || temp4 > 23) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    int temp5 = Integer.parseInt(temp.substring(14, 16));
                    if (temp5 < 0 || temp5 > 59) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    int temp6 = Integer.parseInt(temp.substring(17, 19));
                    if (temp6 < 0 || temp6 > 59) {
                        resultlabel.setText("Format must be in the form of 'YYYY-MM-DD HH:MM:SS'");
                        clear_color();
                        resultlabel.setForeground(Color.red);
                        label4.setForeground(Color.red);
                        textField4.setText("");
                        return;
                    }
                    String due_date = temp.substring(0,4) + "-" + temp.substring(5,7) + "-" +
                            temp.substring(8,10) + " " + temp.substring(11,13) + ":" +
                            temp.substring(14,16) + ":" + temp.substring(17,19);
                    if (Objects.equals(statuscombobox.getSelectedItem(), "")) {
                        resultlabel.setText("Please select a status");
                        resultlabel.setForeground(Color.red);
                        label5.setForeground(Color.red);
                        return;
                    }
                    String status = Objects.requireNonNull(statuscombobox.getSelectedItem()).toString();

                    // update the database
                    String query = "UPDATE homework SET Total_points = " + total_points + ", Title = '" +
                            homework_title + "', Due_Date = '" + due_date + "', Status = '" + status + "" +
                            "' WHERE Homework_ID = " + textField1.getText() + ";";
                    sql.update_query(query);

                    // update the table, clean up
                    clear_color();
                    clear_text();
                    initialize_homework_panel();
                    resultlabel.setText("Homework updated successfully");
                }
            }
        });

        // refresh tables
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(e.getActionCommand(), "cancel_release_homework")) {
                    // clean up when user cancels the release homework
                    textField1.setEnabled(false);
                    textField2.setEnabled(false);
                    textField3.setEnabled(false);
                    textField4.setEnabled(false);
                    statuscombobox.setEnabled(false);
                    clear_text();
                    clear_color();
                    enable_buttons();
                    refreshButton.setText("Refresh");
                    refreshButton.setActionCommand("refresh");
                    submitButton.setText("Submit");
                    submitButton.setActionCommand("Submit");
                    resultlabel.setText("Cancelled");
                } else {
                    // refresh the table
                    initialize_homework_panel();
                    initialize_student_panel();
                    textField1.setEnabled(false);
                    textField2.setEnabled(false);
                    textField3.setEnabled(false);
                    textField4.setEnabled(false);
                    statuscombobox.setEnabled(false);
                    clear_text();
                    clear_color();
                    resultlabel.setText("Refreshed successfully");
                }
            }
        });

        // Complex query 2
        // This query can found the highest score of each course
        // and the student who got the highest score
        complexQuery1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultUI resultUI = new ResultUI("SELECT Course_ID, UIN, Grade FROM (SELECT MAX(Grade) AS " +
                        "max_score FROM current_grade GROUP BY Course_ID) " +
                        "AS t1 INNER JOIN current_grade " +
                        "ON current_grade.Grade = t1.max_score;",1);
            }
        });

        // Complex query 3
        // This query can found students who have lower score
        // than the average score of the course
        complexQuery2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultUI resultUI = new ResultUI("SELECT UIN,Grade FROM current_grade " +
                        "WHERE Grade < (SELECT AVG(Grade) FROM " +
                        "current_grade WHERE Course_ID = '" + course_name + "') AND Course_ID = '" + course_name + "';", 2);
            }
        });

        // Get the data when user clicks the table
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(adding_course == 1) {
                    return;
                }
                enable_textfield();
                int row = table1.getSelectedRow();
                Vector<String> temp = new Vector<String>();
                for (int i = 0; i < table1.getColumnCount(); i++) {
                    temp.add(table1.getValueAt(row, i).toString());
                }
                textField1.setText(temp.get(0));
                textField2.setText(temp.get(1));
                textField3.setText(temp.get(2));
                textField4.setText(temp.get(3));
                statuscombobox.setSelectedItem(temp.get(4));
                data_for_undo = temp;
            }
        });

        // Get the data when user clicks the table
        table2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(adding_course == 1) {
                    return;
                }
                int row = table2.getSelectedRow();
                Vector<String> temp = new Vector<String>();
                for (int i = 0; i < table2.getColumnCount(); i++) {
                    temp.add(table2.getValueAt(row, i).toString());
                }
                Selected_student = temp.get(0);
                Selected_student_grade = temp.get(1);
            }
        });

        // Delete homework from the database
        deleteHomeworkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table1.getSelectedRow() == -1) {
                    resultlabel.setText("Please select a homework to delete");
                    resultlabel.setForeground(Color.red);
                    return;
                }
                int row = table1.getSelectedRow();
                sql.update_query("DELETE FROM homework WHERE Homework_ID = " + table1.getValueAt(row, 0) + ";");
                initialize_homework_panel();
                resultlabel.setText("Homework deleted successfully");
                resultlabel.setForeground(Color.black);
            }
        });
    }

    // initialize the homework table
    public void initialize_homework_panel() {
        String query = "SELECT * FROM homework WHERE Course_ID = '" +  course_n + "';";
        Vector<Vector<String>> result = sql.execute_gettable_query(query,5);
        DefaultTableModel homework_model = (DefaultTableModel)table1.getModel();
        homework_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        homework_model.addColumn("ID");
        homework_model.addColumn("Total Points");
        homework_model.addColumn("Title");
        homework_model.addColumn("Due Date");
        homework_model.addColumn("Status");
        for (int i = 0; i < result.size(); i++) {
            homework_model.insertRow(i, result.get(i));
        }
        table1.setModel(homework_model);
    }


    // initialize the student table
    public void initialize_student_panel() {
        student_list.clear();
        String query = "SELECT student.First_name," +
                "student.Middle_name,student.Last_name,current_grade." +
                "Grade, current_grade.UIN FROM student " +
                "INNER JOIN current_grade ON student.UIN = current_grade.UIN AND current_grade.Course_ID = '" + course_n + "';";
        Vector<Vector<String>> temp = sql.execute_gettable_query(query,5);
        Vector<Vector<String>> result = new Vector<Vector<String>>();
        for (Vector<String> strings : temp) {
            Vector<String> temp1 = new Vector<String>();
            if (Objects.equals(strings.get(1), ""))
                temp1.add(strings.get(0) + " " + strings.get(2));
            else
                temp1.add(strings.get(0) + " " + strings.get(1) + " " + strings.get(2));
            if(Integer.parseInt(strings.get(3)) < 0)
                temp1.add("W");
            else
                temp1.add(strings.get(3));
            student_list.add(Integer.valueOf(strings.get(4)));
            result.add(temp1);
        }

        DefaultTableModel student_model = (DefaultTableModel)table2.getModel();
        student_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        student_model.addColumn("Name");
        student_model.addColumn("Curr Grade(%)");
        for (int i = 0; i < result.size(); i++) {
            student_model.insertRow(i, result.get(i));
        }
        table2.setModel(student_model);

    }

    // function to clear the color
    public void clear_color () {
        label1.setForeground(Color.BLACK);
        label2.setForeground(Color.BLACK);
        label3.setForeground(Color.BLACK);
        label4.setForeground(Color.BLACK);
        label5.setForeground(Color.BLACK);
        resultlabel.setForeground(Color.BLACK);
    }

    // function to clear the textfield
    public void clear_text () {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        statuscombobox.setSelectedItem("");
    }

    // function to disable all buttons
    public void disable_buttons () {
        editCurrentGradeButton.setEnabled(false);
        giveFinalGradeButton.setEnabled(false);
        dropStudentButton.setEnabled(false);
        curveGradeButton.setEnabled(false);
        undoButton.setEnabled(false);
        submitButton.setEnabled(false);
        refreshButton.setEnabled(false);
        complexQuery1Button.setEnabled(false);
        complexQuery2Button.setEnabled(false);
        deleteHomeworkButton.setEnabled(false);
    }

    // function to enable all buttons
    public void enable_buttons () {
        editCurrentGradeButton.setEnabled(true);
        giveFinalGradeButton.setEnabled(true);
        dropStudentButton.setEnabled(true);
        curveGradeButton.setEnabled(true);
        undoButton.setEnabled(true);
        submitButton.setEnabled(true);
        refreshButton.setEnabled(true);
        complexQuery1Button.setEnabled(true);
        complexQuery2Button.setEnabled(true);
        deleteHomeworkButton.setEnabled(true);
    }

    // function to enable all textfields
    public void enable_textfield () {
        textField1.setEnabled(true);
        textField2.setEnabled(true);
        textField3.setEnabled(true);
        textField4.setEnabled(true);
        statuscombobox.setEnabled(true);
    }

    // Helper function to check if a string contains only digits
    public boolean check_is_digit(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isAlphabetic(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    // Helper functions to convert lowercase to uppercase
    public String capitalize(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
        }
        return sb.toString();
    }

    // Helper function to quick generate a error message window
    public void error_message(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
