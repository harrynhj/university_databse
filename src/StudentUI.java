// Classname: StudentUI
// Description: This class is used to display student's information
// Author: Haoji Ni

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.Vector;

public class StudentUI {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JButton saveButton;
    private JLabel accountimage;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JPasswordField passwordField3;
    private JButton logOutButton;
    private JProgressBar degreeprogressbar;
    private JTabbedPane tabpane1;
    private JTable topstudenttable;
    private JTable courselisttable;
    private JTable mycoursetable;
    private JLabel registerstatuslabel;
    private JLabel dropstatuslabel;
    private JButton dropButton;
    private JButton registerButton;
    private JTextField textField1;
    private JTextField textField2;
    private JLabel welcomelabel;
    private JButton refreshPageButton;
    private JLabel registerdatelabel;
    private JLabel dayslabel;
    private JTable newstudenttable;
    private JLabel titleimagelabel;
    private JTable homeworktable;
    private JTable mygradetable;
    private JButton complexQueryButton;
    Query sql = new Query();
    int uin = 0;

    String selected_course = "";
    String selected_listcourse = "";



    public StudentUI(int std_UIN) {
        // Initialize the components
        uin = std_UIN;
        JFrame frame = new JFrame("Student Panel");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(650, 400);
        frame.setLocation(960, 540);
        frame.setVisible(true);
        frame.setResizable(false);

        // Get user's gender
        String query = "SELECT student.Gender FROM student WHERE UIN = " + std_UIN;
        Vector<Vector<String>> gender_table = sql.execute_gettable_query(query,1);
        String gender = gender_table.get(0).get(0);
        if (Objects.equals(gender, "Male")) {
            accountimage.setIcon(new ImageIcon("src/images/male.png"));
        } else if (Objects.equals(gender, "Female")) {
            accountimage.setIcon(new ImageIcon("src/images/female.png"));
        } else {
            // Hamood?
            hamood();
        }
        System.out.println("Debug: Gender = " + gender);

        //  世界を大いに盛り上げるための涼宮ハルヒの団
        titleimagelabel.setIcon(new ImageIcon("src/images/soslogo.png"));
        System.out.println("おい！世界を大いに盛り上げ為のジョー．スミスをよろしく～");

        // If user is not in any course, disable the homework button
        if(sql.check_if_exist("SELECT * FROM course_member WHERE UIN = " + std_UIN)){
            tabbedPane1.setEnabledAt(1, false);
            System.out.println("Debug: User is in any course");
        }


        // Get user's name
        Vector<Vector<String>> temp2 = sql.execute_gettable_query(
                "SELECT First_name, Middle_name, Last_name FROM student WHERE UIN = " + std_UIN,3);
        String student_name;
        if (temp2.get(0).get(1) == null || temp2.get(0).get(1).equals(""))
            student_name = temp2.get(0).get(0) + " " + temp2.get(0).get(2);
        else
            student_name = temp2.get(0).get(0) + " " + temp2.get(0).get(1) + " " + temp2.get(0).get(2);
        System.out.println("Debug: Student name is " + student_name);
        welcomelabel.setText("Welcome Back, " + student_name);


        // Get current status of register and drop
        if (sql.check_reg_status())
            registerstatuslabel.setText("Open");
        else
            registerstatuslabel.setText("Closed");
        if (sql.check_drop_status())
            dropstatuslabel.setText("Open");
        else
            dropstatuslabel.setText("Closed");
        System.out.println("Debug: Register status is " + registerstatuslabel.getText());
        System.out.println("Debug: Drop status is " + dropstatuslabel.getText());

        // Get user's degree progress
        String query2 = "SELECT course.Credit FROM course INNER JOIN final_grade ON course.Course_ID = final_grade.Course WHERE final_grade.UIN = " + std_UIN;
        Vector<Vector<String>> credit_table = sql.execute_gettable_query(query2,1);
        int sum = 0;
        for (Vector<String> strings : credit_table) {
            sum += Integer.parseInt(strings.get(0));
        }
        degreeprogressbar.setMaximum(128);
        degreeprogressbar.setValue(sum);
        System.out.println("Debug: Degree progress is " + sum);

        // initialize the table
        initialize_topstudenttable();
        initialize_newstudenttable();
        initialize_homeworktable();
        initialize_courselisttable();
        initialize_mycoursetable();
        initialize_mygradetable();
        System.out.println("Debug: All tables initialized");

        // get register time info
        Vector<Vector<String>> reg_time = sql.execute_gettable_query("SELECT Register_time FROM student WHERE UIN = " + std_UIN,1);
        String reg_time_str = reg_time.get(0).get(0);
        dayslabel.setText("You have been registered for " + (get_time() - convert_day(reg_time_str)) + " days");
        registerdatelabel.setText("Register time: " + reg_time_str);

        // When mouse is clicked on the table, get the selected course name and department
        // Display them in the text field
        courselisttable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int row = courselisttable.getSelectedRow();
                String title = courselisttable.getModel().getValueAt(row, 4).toString();
                String department = courselisttable.getModel().getValueAt(row, 5).toString();
                textField1.setText(title);
                textField2.setText(department);
                selected_listcourse = courselisttable.getModel().getValueAt(row, 0).toString();
            }
        });

        // When mouse is clicked on the table, get the selected course id and save for later use
        mycoursetable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int row = mycoursetable.getSelectedRow();
                selected_course = mycoursetable.getModel().getValueAt(row, 0).toString();
            }
        });

        // Refresh all the tables
        refreshPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initialize_topstudenttable();
                initialize_newstudenttable();
                initialize_homeworktable();
                initialize_courselisttable();
                initialize_mycoursetable();
                initialize_mygradetable();
                if (sql.check_reg_status())
                    registerstatuslabel.setText("Open");
                else
                    registerstatuslabel.setText("Closed");
                if (sql.check_drop_status())
                    dropstatuslabel.setText("Open");
                else
                    dropstatuslabel.setText("Closed");
                String query2 = "SELECT course.Credit FROM course INNER JOIN final_grade ON course.Course_ID = final_grade.Course WHERE final_grade.UIN = " + std_UIN;
                Vector<Vector<String>> credit_table = sql.execute_gettable_query(query2,1);
                int sum = 0;
                for (Vector<String> strings : credit_table) {
                    sum += Integer.parseInt(strings.get(0));
                }
                degreeprogressbar.setValue(sum);
            }
        });

        // Logout and generate a new login page
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                LoginUI loginUI = new LoginUI();
            }
        });

        // function for change password, will check if the old password is correct
        // and update the new password to the database
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Objects.equals(String.valueOf(passwordField2.getPassword()), String.valueOf(passwordField3.getPassword()))) {
                    error_message("Confirm password does not match");
                } else if (sql.check_if_exist("SELECT * FROM student WHERE UIN = " + std_UIN + " AND Password = '" + String.valueOf(passwordField1.getPassword()) + "';")) {
                    error_message("Wrong old password");
                } else {
                    sql.update_query("UPDATE student SET Password = '" + String.valueOf(passwordField2.getPassword()) + "' WHERE UIN = " + std_UIN);
                    JOptionPane.showMessageDialog(frame,
                            "Change Saved", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // function for student to register a course
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!sql.check_reg_status()) {
                    error_message("Register is closed at this time");
                    return;
                }
                if (!sql.check_if_exist("SELECT * FROM course_member WHERE UIN = " + std_UIN + " AND Course_ID = '" + selected_listcourse + "';")) {
                    error_message("You have already registered this course");
                    return;
                } else if (!sql.check_if_exist("SELECT * FROM current_grade WHERE UIN = " + std_UIN + " AND Course_ID = '" + selected_listcourse + "';")) {
                    error_message("You can not register this course");
                    return;
                } else if (!sql.check_if_exist("SELECT * FROM final_grade WHERE UIN = " + std_UIN + " AND Course = '" + selected_listcourse + "';")) {
                    error_message("You have already completed this course");
                    return;
                } else if (!sql.check_if_exist("SELECT * FROM course WHERE Course_ID = '" + selected_listcourse + "' AND Seats_Left = 0;")) {
                    error_message("This course is full");
                    return;
                }
                sql.update_query("INSERT INTO course_member (UIN,Course_ID) VALUES (" + std_UIN + ", '" + selected_listcourse + "');");
                sql.update_query("UPDATE course SET Seats_Left = Seats_Left - 1 WHERE Course_ID = '" + selected_listcourse + "';");
                sql.update_query("INSERT INTO current_grade (UIN,Grade,Course_ID) VALUES (" + std_UIN + ", 100, '" + selected_listcourse + "');");
                JOptionPane.showMessageDialog(frame,
                        "Register Saved", "Success", JOptionPane.INFORMATION_MESSAGE);
                initialize_mycoursetable();
                initialize_courselisttable();
                System.out.println("Debug: Student " + std_UIN + " registered course " + selected_listcourse);
            }
        });

        // function for student to drop a course
        dropButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!sql.check_drop_status()) {
                    error_message("Drop is closed at this time");
                    return;
                }
                String temp = (String)JOptionPane.showInputDialog(
                        null,
                        "Whats the reason for dropping this course?",
                        "Drop Course",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        ""
                );
                if (temp == null || temp.equals("")) {
                    error_message("You must enter a reason");
                    return;
                }
                sql.update_query("DELETE FROM course_member WHERE UIN = " +
                        std_UIN + " AND Course_ID = '" + selected_course + "';");
                sql.update_query("UPDATE course SET Seats_Left = " +
                        "Seats_Left + 1 WHERE Course_ID = '" + selected_course + "';");
                sql.update_query("UPDATE current_grade SET Grade = -999 " +
                        "WHERE UIN = " + std_UIN + " AND Course_ID = '" + selected_course + "';");
                sql.update_query("INSERT INTO dropped_student (UIN,Course_ID,Executor,Reason) " +
                        "VALUES (" + std_UIN + ", '" + selected_course + "', " + std_UIN + ", '" + temp + "');");
                JOptionPane.showMessageDialog(frame,
                        "Course Dropped", "Success", JOptionPane.INFORMATION_MESSAGE);
                initialize_mycoursetable();
                initialize_courselisttable();
                System.out.println("Debug: Student " + std_UIN + " dropped course " + selected_course);
            }
        });
        complexQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultUI resultUI = new ResultUI("SELECT Course_ID , COUNT(*)" +
                        " AS count FROM course_member GROUP BY " +
                        "Course_ID ORDER BY count DESC LIMIT 5;", 3);
            }
        });
    }

    // Complex query 1
    public void initialize_topstudenttable() {
        Vector<Vector<String>> temp = sql.execute_gettable_query(
                "SELECT student.First_name, student.Middle_name, student.Last_name, " +
                        "t1.total_credit FROM (SELECT final_grade.UIN, SUM(Credit) AS total_credit " +
                        "FROM course INNER JOIN final_grade ON course.Course_ID = final_grade.Course " +
                        "GROUP BY final_grade.UIN) AS t1 INNER JOIN student ON t1.UIN = student.UIN " +
                        "ORDER BY total_credit DESC LIMIT 10;",4);
        DefaultTableModel topstudent_model = (DefaultTableModel)topstudenttable.getModel();
        topstudent_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        topstudent_model.addColumn("First Name");
        topstudent_model.addColumn("Middle Name");
        topstudent_model.addColumn("Last Name");
        topstudent_model.addColumn("Total Credit");
        for (int i = 0; i < temp.size(); i++) {
            topstudent_model.insertRow(i, temp.get(i));
        }
        topstudenttable.setModel(topstudent_model);
    }

    public void initialize_newstudenttable() {
        Vector<Vector<String>> temp = sql.execute_gettable_query(
                "SELECT First_name,Middle_name,Last_name " +
                        "FROM university.student ORDER BY Register_time DESC " +
                        "LIMIT 10;",3);
        DefaultTableModel newstudent_model = (DefaultTableModel)newstudenttable.getModel();
        newstudent_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        newstudent_model.addColumn("First Name");
        newstudent_model.addColumn("Middle Name");
        newstudent_model.addColumn("Last Name");
        for (int i = 0; i < temp.size(); i++) {
            newstudent_model.insertRow(i, temp.get(i));
        }
        newstudenttable.setModel(newstudent_model);
    }

    public void initialize_homeworktable() {
        Vector<Vector<String>> temp = sql.execute_gettable_query(
                "SELECT homework.Total_points, homework.Title, homework.Due_Date, " +
                        "homework.Status , homework.Course_ID FROM homework " +
                        "INNER JOIN course_member " +
                        "ON course_member.UIN = " + uin + " WHERE homework.Status != 'Hidden';",5);
        DefaultTableModel homework_model = (DefaultTableModel)homeworktable.getModel();
        homework_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        homework_model.addColumn("Total Points");
        homework_model.addColumn("Title");
        homework_model.addColumn("Due Date");
        homework_model.addColumn("Status");
        homework_model.addColumn("Course ID");
        for (int i = 0; i < temp.size(); i++) {
            homework_model.insertRow(i, temp.get(i));
        }
        homeworktable.setModel(homework_model);
    }

    public void initialize_courselisttable() {
        Vector<Vector<String>> temp = sql.execute_gettable_query(
                "SELECT course.Course_ID, course.Credit, course.Total_seats, " +
                        "course.Seats_Left ,course.Title, course.Department FROM course" ,6);
        DefaultTableModel courselist_model = (DefaultTableModel)courselisttable.getModel();
        courselist_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        courselist_model.addColumn("Course ID");
        courselist_model.addColumn("Credit");
        courselist_model.addColumn("Total Seats");
        courselist_model.addColumn("Seats Left");
        courselist_model.addColumn("Title");
        courselist_model.addColumn("Department");
        for (int i = 0; i < temp.size(); i++) {
            courselist_model.insertRow(i, temp.get(i));
        }
        courselisttable.setModel(courselist_model);
    }

    public void initialize_mycoursetable() {
        Vector<Vector<String>> temp = sql.execute_gettable_query(
                "SELECT course_member.Course_ID, course.Credit FROM course_member " +
                        "INNER JOIN course ON course_member.UIN = " + uin +
                        " AND course_member.Course_ID = course.Course_ID;" ,2);
        DefaultTableModel mycourse_model = (DefaultTableModel)mycoursetable.getModel();
        mycourse_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        mycourse_model.addColumn("Course ID");
        mycourse_model.addColumn("Credit");
        for (int i = 0; i < temp.size(); i++) {
            mycourse_model.insertRow(i, temp.get(i));
        }
        mycoursetable.setModel(mycourse_model);
    }

    public void initialize_mygradetable() {
        Vector<Vector<String>> temp = sql.execute_gettable_query(
                "SELECT final_grade.Course, final_grade.Grade FROM final_grade WHERE final_grade.UIN = " + uin + ";" ,2);
        DefaultTableModel mygrade_model = (DefaultTableModel)mygradetable.getModel();
        mygrade_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        mygrade_model.addColumn("Course ID");
        mygrade_model.addColumn("Grade");
        for (int i = 0; i < temp.size(); i++) {
            mygrade_model.insertRow(i, temp.get(i));
        }
        mygradetable.setModel(mygrade_model);
    }

    // Hamood?
    public void hamood () {

        System.out.println("        Hamood?\n" +
                "        Hamood\n" +
                "        Hamood\n" +
                "        Hamood habibi Hamood, Hamood habibi\n" +
                "        Hamood habibi Hamood, Hamood habibi\n" +
                "        \n" +
                "        Hamood?\n" +
                "        Hamood\n" +
                "        \n" +
                "        Goum eg'ad men ennoama!\n" +
                "        Goum eg'ad men ennoama!\n" +
                "        Goum eg'ad men ennoama!\n" +
                "        Ya Ahmad Hamadi\n" +
                "        Ya Ahmad Hamadi");
        accountimage.setIcon(new ImageIcon("src/images/hamood.png"));
    }

    public int get_time () {
        String time = java.time.LocalDate.now().toString();
        String[] time_split = time.split("-");
        int year = Integer.parseInt(time_split[0]);
        int month = Integer.parseInt(time_split[1]);
        int day = Integer.parseInt(time_split[2]);
        return (year - 1) * 365 + (month - 1) * 30 + day;
    }

    public int convert_day (String time) {
        String temp = time.substring(0, 10);
        String[] time_split = temp.split("-");
        int year = Integer.parseInt(time_split[0]);
        int month = Integer.parseInt(time_split[1]);
        int day = Integer.parseInt(time_split[2]);
        return (year - 1) * 365 + (month - 1) * 30 + day;
    }

    public void error_message(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
