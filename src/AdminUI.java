// Classname: AdminUI
// Description: This class is used to display admin UI
// and send query to database, this class is like a junkyard
// contain many duplicated code, please be easy on it
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
import javax.swing.JTable;


public class AdminUI  {

    public JPanel panel1;
    public JButton generateRegisterKeyButton;
    public JButton changeStudentGradeButton;
    public JButton changeUserRoleButton;
    public JButton addCourseButton;
    public JButton forceRegisterButton;
    public JButton refreshTableButton;
    private JTable table1;
    private JButton addProfessorButton;
    private JTabbedPane tablePane1;
    private JPanel hiddenJpanel1;
    private JButton searchButton;
    private JTextField searchtextField;
    private JComboBox<String> searchcomboBox;
    private JComboBox sortcomboBox1;
    private JButton addStudentButton;
    private JTable table2;
    private JTable table3;
    private JButton logoutButton;
    private JButton saveChangeButton;
    private JTextField textField1;
    private JLabel Jlabel1;
    private JLabel Jlabel2;
    private JLabel Jlabel3;
    private JLabel Jlabel4;
    private JLabel Jlabel5;
    private JLabel Jlabel6;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTable table4;
    private JButton undoButton;
    private JRadioButton regcloseradio;
    private JRadioButton regopenradio;
    private JRadioButton dropcloseradio;
    private JRadioButton dropopenradio;
    private JLabel Jlabel0;
    private JComboBox departmentCombobox;
    private JLabel resultLabel;
    private JTable table5;
    private JTable table6;
    private JTable table7;
    private JButton deleteRowButton;
    private JLabel imagelabel;

    private final Query sql = new Query();

    private Vector<String> data_for_undo = new Vector<>();
    private int table_index = 0;
    private int adding_course = 0;


    public AdminUI() {
        // initialize the panel
        JFrame frame = new JFrame("AdminUI");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(900, 500);
        frame.setLocation(960, 540);
        frame.setVisible(true);
        frame.setResizable(false);
        textField1.setEnabled(false);
        textField2.setEnabled(false);
        textField3.setEnabled(false);
        textField4.setEnabled(false);
        textField5.setEnabled(false);
        textField6.setEnabled(false);
        departmentCombobox.setVisible(false);
        Jlabel0.setVisible(false);
        resultLabel.setText("");

        // initialize the table
        initialize_Student_table();
        initialize_Professor_table();
        initialize_Course_table();
        initialize_Homework_table();
        initialize_coursemember_table();
        initialize_registerkey_table();
        textField1.setEnabled(true);
        textField5.setEnabled(true);
        textField6.setEnabled(true);
        tablePane1.setSelectedIndex(0);
        student_format();

        // If you are a weeb, change admin.png to anime.png
        imagelabel.setIcon(new ImageIcon("src/images/gowork.png"));


        // check reg and drop status
        if (sql.check_reg_status()) {
            System.out.println("Debug: Registration is open");
            regopenradio.setSelected(true);
        } else {
            System.out.println("Debug: Registration is closed");
            regcloseradio.setSelected(true);
        }
        if (sql.check_drop_status()) {
            System.out.println("Debug: Drop is open");
            dropopenradio.setSelected(true);
        } else {
            System.out.println("Debug: Drop is closed");
            dropcloseradio.setSelected(true);
        }
        logoutButton.setIcon(new ImageIcon("src/images/quit.png"));

        System.out.println("Debug: AdminUI is initialized");



        // add action listener to buttons
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // logout button, close the window
                if (Objects.equals(e.getActionCommand(), "Log Out")) {
                    System.out.println("Debug: User logged out");
                    frame.dispose();
                    LoginUI gui = new LoginUI();
                } else if (Objects.equals(e.getActionCommand(), "Generate Register Key")) {
                    // Generate a new register key
                    int result_amount;
                    String result = (String)JOptionPane.showInputDialog(
                            frame,
                            "Please enter generate amount",
                            "Input",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            ""
                    );
                    // Error exception handling
                    if (result == null) {
                        error_message("Please enter a number");
                    } else if (check_is_digit(result)) {
                        error_message("Please enter a number");
                    } else {
                        if(result.equals("")) {
                            error_message("Please enter a number");
                        } else {
                            result_amount = Integer.parseInt(result);
                            if (result_amount > 0 && result_amount < 1000) {
                                sql.Generate_Reg_Key(result_amount);
                                initialize_registerkey_table();
                                System.out.println("Debug: Generate " + result_amount + " Register Key Successfully");
                                JOptionPane.showMessageDialog(null, "Generate Register Key Successfully",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                error_message("Please enter a number between 1 and 999");
                            }
                        }
                    }
                } else if (Objects.equals(e.getActionCommand(), "Change Student Grade")) {
                    // Display name, course and grade
                    clear_table(table4);
                    String query = "SELECT student.UIN,student.First_name," +
                            "student.Middle_name,student.Last_name,final_grade." +
                            "Course,final_grade.Grade FROM student " +
                            "INNER JOIN final_grade ON student.UIN = final_grade.UIN;";
                    Vector<Vector<String>> result = sql.execute_gettable_query(query,6);
                    DefaultTableModel result_model = (DefaultTableModel)table1.getModel();
                    result_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
                    result_model.addColumn("UIN");
                    result_model.addColumn("First name");
                    result_model.addColumn("Middle name");
                    result_model.addColumn("Last name");
                    result_model.addColumn("Course");
                    result_model.addColumn("Grade");
                    for (int i = 0; i < result.size(); i++) {
                        result_model.insertRow(i, result.get(i));
                    }
                    table4.setModel(result_model);
                    tablePane1.setSelectedIndex(6);
                    Jlabel1.setText("UIN");
                    Jlabel2.setText("First name");
                    Jlabel3.setText("Middle name");
                    Jlabel4.setText("Last name");
                    Jlabel5.setText("Course");
                    Jlabel6.setText("Grade");
                    textField5.setEnabled(true);
                    textField6.setEnabled(true);
                    grade_format();
                    System.out.println("Debug: Student Grade Table is initialized");
                } else if (Objects.equals(e.getActionCommand(), "Change User Role")) {
                    // if user is not selected student or professor, error message
                    if (data_for_undo.size() == 0 || (table1.getSelectedRow() == -1 && table2.getSelectedRow() == -1)) {
                        error_message("Please select a row");
                    } else if (table_index > 2) {
                        error_message("Please a row from either Student or Professor table");
                    } else {
                        if (table_index == 1) {
                            // student -> professor case
                            if(!sql.check_if_exist("SELECT * FROM professor WHERE UIN = " + data_for_undo.get(0) + ";")) {
                                error_message("This UIN is already a professor");
                            } else {
                                // delete student from student table, add same data into professor table
                                String query = "INSERT INTO professor (UIN,First_name,Middle_name,Last_name,Gender,Password) " +
                                        "VALUE (" + data_for_undo.get(0) + ",'" + data_for_undo.get(1) +
                                        "','" + data_for_undo.get(2) + "','" + data_for_undo.get(3) + "','" +
                                        data_for_undo.get(4) + "','" + data_for_undo.get(5) + "');";
                                sql.update_query(query);
                                query = "DELETE FROM student WHERE UIN = '" + data_for_undo.get(0) + "';";
                                sql.update_query(query);
                                System.out.println("Debug: Change User " + data_for_undo.get(0) + " Role to Professor Successfully");
                                JOptionPane.showMessageDialog(null, "Change User " + data_for_undo.get(0) +
                                        " Role to Professor Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                                initialize_Student_table();
                                initialize_Professor_table();
                                System.out.println("Debug: User: " + data_for_undo.get(0) + " is promoted to Professor");
                                tablePane1.setSelectedIndex(1);
                            }
                        } else if (table_index == 2) {
                            // professor -> student case
                            if(!sql.check_if_exist("SELECT * FROM student WHERE UIN = " + data_for_undo.get(0) + ";")) {
                                error_message("This UIN is already a student");
                            } else {
                                // delete professor from professor table, add same data into student table
                                String query = "INSERT INTO student (UIN,First_name,Middle_name,Last_name,Gender,Password) " +
                                        "VALUE (" + data_for_undo.get(0) + ",'" + data_for_undo.get(1) +
                                        "','" + data_for_undo.get(2) + "','" + data_for_undo.get(3) + "','" +
                                        data_for_undo.get(4) + "','" + data_for_undo.get(5) + "');";
                                sql.update_query(query);
                                query = "DELETE FROM professor WHERE UIN = '" + data_for_undo.get(0) + "';";
                                sql.update_query(query);

                                System.out.println("Debug: Change User " + data_for_undo.get(0) + " Role to Student Successfully");
                                JOptionPane.showMessageDialog(null, "Change User " + data_for_undo.get(0) +
                                        " Role to Student Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

                                // refresh table
                                initialize_Student_table();
                                initialize_Professor_table();
                                System.out.println("Debug: User: " + data_for_undo.get(0) + " is demote to Student");
                                tablePane1.setSelectedIndex(0);
                            }
                        }
                    }
                } else if (Objects.equals(e.getActionCommand(), "Add Course")) {
                    // Change the panel layout when user clicked Add Course button
                    textField1.setText("");
                    textField2.setText("");
                    textField3.setText("");
                    textField4.setText("");
                    textField5.setText("");
                    textField6.setText("");
                    adding_course = 1;
                    tablePane1.setSelectedIndex(2);
                    Jlabel5.setVisible(false);
                    textField5.setVisible(false);
                    Jlabel6.setVisible(false);
                    textField6.setVisible(false);
                    undoButton.setVisible(false);
                    disable_everything();
                    course_format();
                    Jlabel0.setVisible(true);
                    departmentCombobox.setVisible(true);
                    saveChangeButton.setEnabled(true);
                    refreshTableButton.setEnabled(true);
                    textField1.setEnabled(true);
                    textField2.setEnabled(true);
                    textField3.setEnabled(true);
                    textField4.setEnabled(true);
                    textField5.setVisible(false);
                    textField6.setVisible(false);
                    searchcomboBox.setEnabled(true);
                    searchtextField.setEnabled(true);
                    searchButton.setEnabled(true);
                    deleteRowButton.setVisible(false);
                    Jlabel4.setText("Seat Capacity");

                    // Change the title and the action command for the button
                    saveChangeButton.setText("Submit");
                    saveChangeButton.setActionCommand("Submit_course");
                    refreshTableButton.setText("Cancel");
                    refreshTableButton.setActionCommand("Cancel");
                    System.out.println("Debug: Add Course textFields are initialized");
                } else if (Objects.equals(e.getActionCommand(), "Submit_course")) {
                    // function for adding course
                    // Error exception handling, long if else statement, my bad
                    if (textField1.getText().trim().equals("")) {
                        resultLabel.setText("Please enter a course ID");
                        clear_color();
                        resultLabel.setForeground(Color.red);
                        Jlabel1.setForeground(Color.red);
                    } else {
                        String c_id = textField1.getText().trim();
                        if (check_is_digit(textField2.getText().trim()) || textField2.getText().trim().equals("")) {
                            resultLabel.setText("Please enter a number for credit");
                            clear_color();
                            resultLabel.setForeground(Color.red);
                            Jlabel2.setForeground(Color.red);
                            textField2.setText("");
                        } else {
                            int credit = Integer.parseInt(textField2.getText().trim());
                            if (textField3.getText().trim().equals("")) {
                                resultLabel.setText("Please enter a course name");
                                clear_color();
                                resultLabel.setForeground(Color.red);
                                Jlabel3.setForeground(Color.red);
                            } else {
                                String name = textField3.getText().trim();
                                if (Objects.equals(departmentCombobox.getSelectedItem().toString(), "")) {
                                    resultLabel.setText("Please select a department");
                                    clear_color();
                                    resultLabel.setForeground(Color.red);
                                    Jlabel0.setForeground(Color.red);
                                } else {
                                    String department = departmentCombobox.getSelectedItem().toString();
                                    if(check_is_digit(textField4.getText().trim()) || textField4.getText().trim().equals("")) {
                                        resultLabel.setText("Please enter a number for seat capacity");
                                        clear_color();
                                        resultLabel.setForeground(Color.red);
                                        Jlabel4.setForeground(Color.red);
                                        textField4.setText("");
                                    } else {
                                        int seats = Integer.parseInt(textField4.getText().trim());
                                        // execute query to add course
                                        if(sql.addCourse(c_id, credit, name, department, seats, seats)) {
                                            // refresh table, reset layout, reset textFields, change back button
                                            resultLabel.setText("Add Course Successfully");
                                            resultLabel.setForeground(Color.GREEN);
                                            saveChangeButton.setText("Save Change");
                                            saveChangeButton.setActionCommand("Save Change");
                                            refreshTableButton.setText("Refresh Table");
                                            refreshTableButton.setActionCommand("Refresh Table");
                                            initialize_Course_table();
                                            enable_everything();
                                            student_format();
                                            clear_color();
                                            clear_text();
                                            tablePane1.setSelectedIndex(2);
                                            adding_course = 0;
                                            course_format();
                                            Jlabel5.setVisible(true);
                                            Jlabel6.setVisible(true);
                                            System.out.println("Debug: Add Course Successfully");
                                        } else {
                                            // for some reason query failed, reset layout, and ignore error message
                                            resultLabel.setText("Add Course Failed, duplicate course id");
                                            saveChangeButton.setText("Save Change");
                                            saveChangeButton.setActionCommand("Save Change");
                                            refreshTableButton.setText("Refresh Table");
                                            refreshTableButton.setActionCommand("Refresh Table");
                                            resultLabel.setForeground(Color.RED);
                                            enable_everything();
                                            clear_color();
                                            textField1.setText("");
                                            textField2.setText("");
                                            textField3.setText("");
                                            textField4.setText("");
                                            textField5.setText("");
                                            textField6.setText("");
                                            adding_course = 0;
                                            course_format();
                                            System.out.println("Debug: Add Course Failed");
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (Objects.equals(e.getActionCommand(), "Cancel" )) {
                    // If user decide to cancel, clear the textFields, reset the layout, and change the button
                    saveChangeButton.setText("Save Change");
                    saveChangeButton.setActionCommand("Save Change");
                    refreshTableButton.setText("Refresh Table");
                    refreshTableButton.setActionCommand("Refresh Table");
                    enable_everything();
                    student_format();
                    clear_color();
                    clear_text();
                    adding_course = 0;
                    System.out.println("Debug: User canceled adding course");
                } else if (Objects.equals(e.getActionCommand(), "Force Register Course")) {
                    // first check if user selected student or professor table, if not show error message
                    if (data_for_undo.size() == 0 || (table1.getSelectedRow() == -1 && table2.getSelectedRow() == -1)) {
                        error_message("Please select a row");
                    } else if (table_index > 2) {
                        error_message("Please a row from either Student or Professor table");
                    }

                    // Ask for input
                    String result = (String)JOptionPane.showInputDialog(
                            null,
                            "Please enter the course ID",
                            "Force Register",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            ""
                    );
                    // error exception handling
                    if (result == null) {
                        error_message("Please enter a course ID");
                    } else {
                        // execute query to force register course
                        sql.forceRegister(data_for_undo.get(0), result,table_index);
                    }
                    // refresh table, reset layout
                    initialize_coursemember_table();
                    tablePane1.setSelectedIndex(4);
                    coursemember_format();
                    clear_text();
                    System.out.println("Debug: User: " + data_for_undo.get(0) + " forced register to course " + result);
                } else if (Objects.equals(e.getActionCommand(), "Refresh Table")) {
                    // refresh all the tables, reset the panel to default
                    // TAKES FORVER TO WAIT SERVER TO RESPOND
                    initialize_Professor_table();
                    initialize_Course_table();
                    initialize_coursemember_table();
                    initialize_Homework_table();
                    initialize_registerkey_table();
                    initialize_Student_table();
                    enable_everything();
                    clear_color();
                    tablePane1.setSelectedIndex(0);
                    student_format();
                    clear_text();
                    System.out.println("Debug: User refreshed all tables(this takes a while)");
                } else if (Objects.equals(e.getActionCommand(), "Add Professor")) {
                    // set layout for add professor, change button action command
                    clear_text();
                    adding_course = 1;
                    tablePane1.setSelectedIndex(1);
                    saveChangeButton.setText("Submit");
                    saveChangeButton.setActionCommand("Submit_professor");
                    refreshTableButton.setText("Cancel");
                    refreshTableButton.setActionCommand("Cancel");
                    undoButton.setVisible(false);
                    disable_everything();
                    Jlabel1.setText("First name");
                    Jlabel2.setText("Middle name");
                    Jlabel3.setText("Last name");
                    Jlabel4.setText("Gender");
                    Jlabel5.setText("Password");
                    Jlabel0.setVisible(false);
                    Jlabel6.setVisible(false);
                    departmentCombobox.setVisible(false);
                    saveChangeButton.setEnabled(true);
                    refreshTableButton.setEnabled(true);
                    textField1.setEnabled(true);
                    textField2.setEnabled(true);
                    textField3.setEnabled(true);
                    textField4.setEnabled(true);
                    textField5.setVisible(true);
                    textField6.setVisible(false);
                    textField5.setEnabled(true);
                    textField6.setEnabled(true);
                    searchcomboBox.setEnabled(true);
                    searchtextField.setEnabled(true);
                    searchButton.setEnabled(true);
                    deleteRowButton.setVisible(false);
                    System.out.println("Debug: Initialized add professor text fields");
                } else if (Objects.equals(e.getActionCommand(), "Submit_professor")){
                    // error exception handling
                    if (textField1.getText().trim().equals("")) {
                        resultLabel.setText("Please enter a first name");
                        clear_color();
                        resultLabel.setForeground(Color.red);
                        Jlabel1.setForeground(Color.red);
                    } else {
                        String first_n = textField1.getText().trim();
                        String middle_n = textField2.getText().trim();
                        if (textField3.getText().trim().equals("")) {
                            resultLabel.setText("Please enter a last name");
                            clear_color();
                            resultLabel.setForeground(Color.red);
                            Jlabel3.setForeground(Color.red);
                            textField3.setText("");
                        } else {
                            String last_n = textField3.getText().trim();
                            if (!textField4.getText().trim().equals("Male") &&
                                    !textField4.getText().trim().equals("Female") &&
                                    !textField4.getText().trim().equals("Other")) {
                                resultLabel.setText("Please enter Male, Female or Other");
                                clear_color();
                                resultLabel.setForeground(Color.red);
                                Jlabel4.setForeground(Color.red);
                                textField4.setText("");
                            } else {
                                String gender = textField4.getText().trim();
                                if(textField5.getText().trim().equals("")) {
                                    resultLabel.setText("Please enter a password");
                                    clear_color();
                                    resultLabel.setForeground(Color.red);
                                    Jlabel5.setForeground(Color.red);
                                    textField5.setText("");
                                } else {
                                    String passwd = textField5.getText().trim();
                                    Authentication auth = new Authentication();
                                    int UIN = auth.generateUIN();
                                    if(sql.addProfessor(UIN, first_n, middle_n, last_n, gender, passwd, "Professor")) {
                                        // execute the query, reset panel layout
                                        resultLabel.setText("Add Professor Successfully");
                                        resultLabel.setForeground(Color.GREEN);
                                        saveChangeButton.setText("Save Change");
                                        saveChangeButton.setActionCommand("Save Change");
                                        refreshTableButton.setText("Refresh Table");
                                        refreshTableButton.setActionCommand("Refresh Table");
                                        initialize_Professor_table();
                                        enable_everything();
                                        student_format();
                                        clear_color();
                                        clear_text();
                                        tablePane1.setSelectedIndex(1);
                                        adding_course = 0;
                                        System.out.println("Debug: User added professor " + first_n + " " + middle_n + " " + last_n + " with UIN " + UIN);
                                    } else {
                                        // bad things happened, reset panel layout
                                        resultLabel.setText("Add Professor Failed, Please try again");
                                        resultLabel.setForeground(Color.RED);
                                        enable_everything();
                                        clear_color();
                                        clear_text();
                                        adding_course = 0;
                                        System.out.println("Debug: User failed to add professor " + first_n + " " + middle_n + " " + last_n + " with UIN " + UIN);
                                    }
                                }
                            }
                        }
                    }
                } else if (Objects.equals(e.getActionCommand(), "Add Student")) {
                    // set layout for add student, change button action command
                    clear_text();
                    adding_course = 1;
                    tablePane1.setSelectedIndex(0);
                    saveChangeButton.setText("Submit");
                    saveChangeButton.setActionCommand("Submit_student");
                    refreshTableButton.setText("Cancel");
                    refreshTableButton.setActionCommand("Cancel");
                    undoButton.setVisible(false);
                    disable_everything();
                    Jlabel1.setText("First name");
                    Jlabel2.setText("Middle name");
                    Jlabel3.setText("Last name");
                    Jlabel4.setText("Gender");
                    Jlabel5.setText("Password");
                    Jlabel0.setVisible(false);
                    Jlabel6.setVisible(false);
                    departmentCombobox.setVisible(false);
                    saveChangeButton.setEnabled(true);
                    refreshTableButton.setEnabled(true);
                    textField1.setEnabled(true);
                    textField2.setEnabled(true);
                    textField3.setEnabled(true);
                    textField4.setEnabled(true);
                    textField5.setVisible(true);
                    textField6.setVisible(false);
                    textField5.setEnabled(true);
                    textField6.setEnabled(true);
                    searchcomboBox.setEnabled(true);
                    searchtextField.setEnabled(true);
                    searchButton.setEnabled(true);
                    deleteRowButton.setVisible(false);
                } else if (Objects.equals(e.getActionCommand(), "Submit_student")) {
                    // error exception handling
                    if (textField1.getText().trim().equals("")) {
                        resultLabel.setText("Please enter a first name");
                        clear_color();
                        resultLabel.setForeground(Color.red);
                        Jlabel1.setForeground(Color.red);
                    } else {
                        String first_n = textField1.getText().trim();
                        String middle_n = textField2.getText().trim();
                        if (textField3.getText().trim().equals("")) {
                            resultLabel.setText("Please enter a last name");
                            clear_color();
                            resultLabel.setForeground(Color.red);
                            Jlabel3.setForeground(Color.red);
                            textField3.setText("");
                        } else {
                            String last_n = textField3.getText().trim();
                            if (!textField4.getText().trim().equals("Male") &&
                                    !textField4.getText().trim().equals("Female") &&
                                    !textField4.getText().trim().equals("Other")) {
                                resultLabel.setText("Please enter Male, Female or Other");
                                clear_color();
                                resultLabel.setForeground(Color.red);
                                Jlabel4.setForeground(Color.red);
                                textField4.setText("");
                            } else {
                                String gender = textField4.getText().trim();
                                if(textField5.getText().trim().equals("")) {
                                    resultLabel.setText("Please enter a password");
                                    clear_color();
                                    resultLabel.setForeground(Color.red);
                                    Jlabel5.setForeground(Color.red);
                                    textField5.setText("");
                                } else {
                                    String passwd = textField5.getText().trim();
                                    Authentication auth = new Authentication();
                                    int UIN = auth.generateUIN();
                                    if(sql.addProfessor(UIN, first_n, middle_n, last_n, gender, passwd, "Student")) {
                                        // execute query, reset layout
                                        resultLabel.setText("Add Student Successfully");
                                        resultLabel.setForeground(Color.GREEN);
                                        saveChangeButton.setText("Save Change");
                                        saveChangeButton.setActionCommand("Save Change");
                                        refreshTableButton.setText("Refresh Table");
                                        refreshTableButton.setActionCommand("Refresh Table");
                                        initialize_Student_table();
                                        enable_everything();
                                        student_format();
                                        clear_color();
                                        clear_text();
                                        tablePane1.setSelectedIndex(0);
                                        adding_course = 0;
                                        System.out.println("Debug: User added student " + first_n + " " + middle_n + " " + last_n + " with UIN " + UIN);
                                    } else {
                                        // bad things happened, reset layout
                                        resultLabel.setText("Add Professor Failed, Please try again");
                                        resultLabel.setForeground(Color.RED);
                                        enable_everything();
                                        clear_color();
                                        textField1.setText("");
                                        textField2.setText("");
                                        textField3.setText("");
                                        textField4.setText("");
                                        textField5.setText("");
                                        textField6.setText("");
                                        adding_course = 0;
                                        System.out.println("Debug: User failed to add student " + first_n + " " + middle_n + " " + last_n + " with UIN " + UIN);
                                    }
                                }
                            }
                        }
                    }
                } else if (Objects.equals(e.getActionCommand(), "Search")) {
                    // search for all tables
                    int table = tablePane1.getSelectedIndex();
                    String search_text = searchtextField.getText().trim();
                    String search_type = searchcomboBox.getSelectedItem().toString();
                    if (search_text.equals("") && !search_type.equals("Middle_name")) {
                        // if search text is empty, show error message
                        resultLabel.setText("Please enter a search text");
                        clear_color();
                        resultLabel.setForeground(Color.red);
                        searchtextField.setText("");
                    } else {
                        //build up query
                        String query = "";
                        query += "SELECT * FROM ";
                        // add table to query
                        if (table == 0) {
                            query += "student";
                        } else if (table == 1) {
                            query += "professor";
                        } else if (table == 2) {
                            query += "course";
                        } else if (table == 3) {
                            query += "homework";
                        } else if (table == 4) {
                            query += "course_member";
                        } else if (table == 5) {
                            query += "register_key";
                        } else if (table == 6) {
                            query += "(SELECT student.UIN,student.First_name," +
                                    "student.Middle_name,student.Last_name,final_grade." +
                                    "Course,final_grade.Grade FROM student " +
                                    "INNER JOIN final_grade ON student.UIN = final_grade.UIN) AS t1 ";
                        }
                        query += " WHERE ";
                        if (table == 0 || table == 1) {
                            // add where case for student and professor
                            switch (search_type) {
                                case "UIN":
                                    query += "UIN = " + search_text + ";";
                                    break;
                                case "First_name":
                                    query += "first_name = '" + search_text + "';";
                                    break;
                                case "Middle_name":
                                    query += "middle_name = '" + search_text + "';";
                                    break;
                                case "Last_name":
                                    query += "last_name = '" + search_text + "';";
                                    break;
                                case "Gender":
                                    query += "Gender = '" + search_text + "';";
                                    break;
                                case "Password":
                                    query += "Password = '" + search_text + "';";
                                    break;
                            }
                            // execute query and show result for student
                            Vector<Vector<String>> temp = sql.execute_gettable_query(query,6);
                            if (table == 0) {
                                DefaultTableModel student_model = (DefaultTableModel)table1.getModel();
                                student_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
                                student_model.addColumn("UIN");
                                student_model.addColumn("First name");
                                student_model.addColumn("Middle name");
                                student_model.addColumn("Last name");
                                student_model.addColumn("Gender");
                                student_model.addColumn("Password");
                                for (int i = 0; i < temp.size(); i++) {
                                    student_model.insertRow(i, temp.get(i));
                                }
                                table1.setModel(student_model);
                                student_format();
                                System.out.println("Debug: User searched student with keyword " + search_text);
                            } else {
                                // execute query and show result for professor
                                DefaultTableModel professor_model = (DefaultTableModel)table2.getModel();
                                professor_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
                                professor_model.addColumn("UIN");
                                professor_model.addColumn("First name");
                                professor_model.addColumn("Middle name");
                                professor_model.addColumn("Last name");
                                professor_model.addColumn("Gender");
                                professor_model.addColumn("Password");
                                for (int i = 0; i < temp.size(); i++) {
                                    professor_model.insertRow(i, temp.get(i));
                                }
                                table2.setModel(professor_model);
                                student_format();
                                System.out.println("Debug: User searched professor with keyword " + search_text);
                            }
                        } else if (table == 2) {

                            /*
                             START FROM NOW ITS JUST ALL SAME CODE BUT DIFFERENT TABLE, NO COMMENT NEEDED
                             */


                            if (search_type.equals("Course_ID")) {
                                query += "Course_ID = '" + search_text + "';";
                            } else if (search_text.equals("Credit")) {
                                query += "Credit = " + search_text + ";";
                            } else if (search_text.equals("Title")) {
                                query += "Title = '" + search_text + "';";
                            } else if (search_text.equals("Department")) {
                                query += "Department = '" + search_text + "';";
                            } else if (search_text.equals("Total_seats")) {
                                query += "Total_seats = " + search_text + ";";
                            } else if (search_text.equals("Seats_Left")) {
                                query += "Seats_Left = " + search_text + ";";
                            }
                            Vector<Vector<String>> temp = sql.execute_gettable_query(query,6);
                            DefaultTableModel course_model = (DefaultTableModel)table3.getModel();
                            course_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
                            course_model.addColumn("Course ID");
                            course_model.addColumn("Credit");
                            course_model.addColumn("Title");
                            course_model.addColumn("Department");
                            course_model.addColumn("Total Seats");
                            course_model.addColumn("Seats Available");
                            for (int i = 0; i < temp.size(); i++) {
                                course_model.insertRow(i, temp.get(i));
                            }
                            table3.setModel(course_model);
                            course_format();
                            System.out.println("Debug: User searched course with keyword " + search_text);
                        } else if (table == 3) {
                            switch (search_type) {
                                case "Homework_ID":
                                    query += "Homework_ID = " + search_text + ";";
                                    break;
                                case "Total_points":
                                    query += "Total_points = " + search_text + ";";
                                    break;
                                case "Due_Date":
                                    query += "Due_Date = '" + search_text + "';";
                                    break;
                                case "Title":
                                    query += "Title = '" + search_text + "';";
                                    break;
                                case "Status":
                                    query += "Status = '" + search_text + "';";
                                    break;
                                case "Course_ID":
                                    query += "Course_ID = '" + search_text + "';";
                                    break;
                            }
                            Vector<Vector<String>> temp = sql.execute_gettable_query(query, 6);
                            DefaultTableModel homework_model = (DefaultTableModel) table5.getModel();
                            homework_model = new DefaultTableModel() {
                                public boolean isCellEditable(int row, int column) {
                                    return false;
                                }
                            };
                            homework_model.addColumn("Homework ID");
                            homework_model.addColumn("Total points");
                            homework_model.addColumn("Title");
                            homework_model.addColumn("Due date");
                            homework_model.addColumn("Status");
                            homework_model.addColumn("Course ID");
                            for (int i = 0; i < temp.size(); i++) {
                                homework_model.insertRow(i, temp.get(i));
                            }
                            table5.setModel(homework_model);
                            homework_format();
                            System.out.println("Debug: User searched homework with keyword " + search_text);
                        } else if (table == 4) {
                            if (search_type.equals("Course_ID")) {
                                query += "Course_ID = '" + search_text + "';";
                            } else if (search_type.equals("UIN")) {
                                query += "UIN = " + search_text + ";";
                            }
                            Vector<Vector<String>> temp = sql.execute_gettable_query(query, 3);
                            DefaultTableModel homework_model = (DefaultTableModel) table6.getModel();
                            homework_model = new DefaultTableModel() {
                                public boolean isCellEditable(int row, int column) {
                                    return false;
                                }
                            };
                            homework_model.addColumn("ID");
                            homework_model.addColumn("UIN");
                            homework_model.addColumn("Course ID");
                            for (int i = 0; i < temp.size(); i++) {
                                homework_model.insertRow(i, temp.get(i));
                            }
                            table6.setModel(homework_model);
                            coursemember_format();
                            System.out.println("Debug: User searched course member with keyword " + search_text);
                        } else if (table == 5) {
                            if (search_type.equals("register_key")) {
                                query += "reg_key = '" + search_text + "';";
                            } else if (search_type.equals("Generated_time")) {
                                query += "Generated_time = '" + search_text + "';";
                            } else if (search_type.equals("Used_time")) {
                                query += "Used_time = '" + search_text + "';";
                            }
                            Vector<Vector<String>> temp = sql.execute_gettable_query(query, 3);
                            DefaultTableModel register_model = (DefaultTableModel) table7.getModel();
                            register_model = new DefaultTableModel() {
                                public boolean isCellEditable(int row, int column) {
                                    return false;
                                }
                            };
                            register_model.addColumn("Register key");
                            register_model.addColumn("Created time");
                            register_model.addColumn("Used time");
                            for (int i = 0; i < temp.size(); i++) {
                                register_model.insertRow(i, temp.get(i));
                            }
                            table7.setModel(register_model);
                            register_format();
                            System.out.println("Debug: User searched register key with keyword " + search_text);
                        } else if (table == 6) {
                            switch (search_type) {
                                case "Course":
                                    query += "t1.Course = '" + search_text + "';";
                                    break;
                                case "UIN":
                                    query += "t1.UIN = " + search_text + ";";
                                    break;
                                case "First_name":
                                    query += "t1.First_name = '" + search_text + "';";
                                    break;
                                case "Last_name":
                                    query += "t1.Last_name = '" + search_text + "';";
                                    break;
                                case "Middle_name":
                                    query += "t1.Middle_name = '" + search_text + "';";
                                    break;
                                case "Grade":
                                    query += "t1.Grade = '" + search_text + "';";
                                    break;
                            }
                            Vector<Vector<String>> temp = sql.execute_gettable_query(query, 6);
                            DefaultTableModel result_model = (DefaultTableModel) table4.getModel();
                            result_model = new DefaultTableModel() {
                                public boolean isCellEditable(int row, int column) {
                                    return false;
                                }
                            };
                            result_model.addColumn("UIN");
                            result_model.addColumn("First name");
                            result_model.addColumn("Middle name");
                            result_model.addColumn("Last name");
                            result_model.addColumn("Course");
                            result_model.addColumn("Grade");
                            for (int i = 0; i < temp.size(); i++) {
                                result_model.insertRow(i, temp.get(i));
                            }
                            table4.setModel(result_model);
                            grade_format();
                            System.out.println("Debug: User searched final grade with keyword " + search_text);
                        }
                    }



                    /*
                     END OF THE INFINITY LONG SEARCH CODE
                    */
                } else if (Objects.equals(e.getActionCommand(), "Delete Row")) {
                    // This function will take the primary key and generate query to delete the row from the table
                    // There are 6 different case for 6 different tables
                    // I will only comment one of them because they are similar

                    // if user didnt select any row, show error message
                    if (data_for_undo.size() == 0) {
                        error_message("Please select a row to delete");
                    }

                    // Build up query
                    String query = "DELETE FROM ";
                    // If is student table, add student into the query
                    if (tablePane1.getSelectedIndex() == 0) {
                        query += "student WHERE UIN = " + data_for_undo.get(0) + ";";
                        sql.update_query(query);
                        query = "DELETE FROM course_member WHERE UIN = " + data_for_undo.get(0) + ";";
                        sql.update_query(query);
                        query = "DELETE FROM final_grade WHERE UIN = " + data_for_undo.get(0) + ";";
                        sql.update_query(query);
                        resultLabel.setText("Deleted");
                        initialize_coursemember_table();
                        initialize_Student_table();
                        tablePane1.setSelectedIndex(0);
                        System.out.println("Debug: Student deleted");
                        student_format();
                    } else if(tablePane1.getSelectedIndex() == 1) {
                        query += "professor WHERE UIN = " + data_for_undo.get(0) + ";";
                        sql.update_query(query);
                        query = "DELETE FROM course_member WHERE UIN = " + data_for_undo.get(0) + ";";
                        sql.update_query(query);
                        resultLabel.setText("Deleted");
                        initialize_coursemember_table();
                        initialize_Professor_table();
                        tablePane1.setSelectedIndex(1);
                        student_format();
                        System.out.println("Debug: Professor deleted");
                    } else if (tablePane1.getSelectedIndex() == 2) {
                        query += "course WHERE Course_ID = '" + data_for_undo.get(0) + "';";
                        sql.update_query(query);
                        query = "DELETE FROM course_member WHERE Course_ID = '" + data_for_undo.get(0) + "';";
                        sql.update_query(query);
                        query = "DELETE FROM final_grade WHERE Course = '" + data_for_undo.get(0) + "';";
                        sql.update_query(query);
                        resultLabel.setText("Deleted");
                        initialize_coursemember_table();
                        initialize_Course_table();
                        tablePane1.setSelectedIndex(2);
                        System.out.println("Debug: Course deleted");
                        course_format();
                    } else if (tablePane1.getSelectedIndex() == 3) {
                        query += "homework WHERE Homework_ID = " + data_for_undo.get(0) + ";";
                        sql.update_query(query);
                        resultLabel.setText("Deleted");
                        initialize_Homework_table();
                        tablePane1.setSelectedIndex(3);
                        System.out.println("Debug: Homework deleted");
                        homework_format();
                    } else if (tablePane1.getSelectedIndex() == 4) {
                        query += "course_member WHERE PK = " + data_for_undo.get(0) + ";";
                        sql.update_query(query);
                        resultLabel.setText("Deleted");
                        initialize_coursemember_table();
                        tablePane1.setSelectedIndex(4);
                        System.out.println("Debug: Course Member deleted");
                        coursemember_format();
                    } else if (tablePane1.getSelectedIndex() == 5) {
                        query += "register_key WHERE reg_key = '" + data_for_undo.get(0) + "';";
                        sql.update_query(query);
                        resultLabel.setText("Deleted");
                        initialize_registerkey_table();
                        tablePane1.setSelectedIndex(5);
                        System.out.println("Debug: Register Key deleted");
                        register_format();
                    }
                    clear_text();
                } else if (Objects.equals(e.getActionCommand(), "Undo")) {
                    // Revert the user change in textfield, cant undo the change in server
                    textField1.setText(data_for_undo.get(0));
                    textField2.setText(data_for_undo.get(1));
                    textField3.setText(data_for_undo.get(2));
                    textField4.setText(data_for_undo.get(3));
                    textField5.setText(data_for_undo.get(4));
                    textField6.setText(data_for_undo.get(5));
                    System.out.println("Debug: Undo");
                } else if (Objects.equals(e.getActionCommand(), "comboBoxChanged"))  {
                    // This function can change the order of the table
                    String query = "";
                    if (searchcomboBox.getSelectedItem() == "") {
                        error_message("Please select a search type");
                    } else if (sortcomboBox1.getSelectedItem() == "A→Z") {
                        switch (tablePane1.getSelectedIndex()) {
                            case 0:
                                query = "SELECT * FROM student ORDER BY " + searchcomboBox.getSelectedItem() + " ASC;";
                                break;
                            case 1:
                                query = "SELECT * FROM professor ORDER BY " + searchcomboBox.getSelectedItem() + " ASC;";
                                break;
                            case 2:
                                query = "SELECT * FROM course ORDER BY " + searchcomboBox.getSelectedItem() + " ASC;";
                                break;
                            case 3:
                                query = "SELECT * FROM homework ORDER BY " + searchcomboBox.getSelectedItem() + " ASC;";
                                break;
                            case 4:
                                query = "SELECT * FROM course_member ORDER BY " + searchcomboBox.getSelectedItem() + " ASC;";
                                break;
                            case 5:
                                query = "SELECT * FROM register_key ORDER BY " + searchcomboBox.getSelectedItem() + " ASC;";
                                break;
                            case 6:
                                query = "SELECT student.UIN,student.First_name," +
                                        "student.Middle_name,student.Last_name,final_grade." +
                                        "Course,final_grade.Grade FROM student " +
                                        "INNER JOIN final_grade ON student.UIN = final_grade.UIN " + "ORDER BY " + searchcomboBox.getSelectedItem() + " ASC;";
                                break;
                        }
                    } else if (sortcomboBox1.getSelectedItem() == "Z→A") {
                        // Same as above, but in reverse order
                        switch (tablePane1.getSelectedIndex()) {
                            case 0:
                                query = "SELECT * FROM student ORDER BY " + searchcomboBox.getSelectedItem() + " DESC;";
                                break;
                            case 1:
                                query = "SELECT * FROM professor ORDER BY " + searchcomboBox.getSelectedItem() + " DESC;";
                                break;
                            case 2:
                                query = "SELECT * FROM course ORDER BY " + searchcomboBox.getSelectedItem() + " DESC;";
                                break;
                            case 3:
                                query = "SELECT * FROM homework ORDER BY " + searchcomboBox.getSelectedItem() + " DESC;";
                                break;
                            case 4:
                                query = "SELECT * FROM course_member ORDER BY " + searchcomboBox.getSelectedItem() + " DESC;";
                                break;
                            case 5:
                                query = "SELECT * FROM register_key ORDER BY " + searchcomboBox.getSelectedItem() + " DESC;";
                                break;
                            case 6:
                                query = "SELECT student.UIN,student.First_name," +
                                        "student.Middle_name,student.Last_name,final_grade." +
                                        "Course,final_grade.Grade FROM student " +
                                        "INNER JOIN final_grade ON student.UIN = final_grade.UIN " + "ORDER BY " + searchcomboBox.getSelectedItem() + " DESC;";
                                break;
                        }
                    }
                    switch (tablePane1.getSelectedIndex()) {
                        // change the search type of the table
                        // different case are similar
                        case 0: {
                            Vector<Vector<String>> result = sql.execute_gettable_query(query, 8);
                            DefaultTableModel student_model = (DefaultTableModel) table1.getModel();
                            student_model = new DefaultTableModel() {
                                public boolean isCellEditable(int row, int column) {
                                    return false;
                                }
                            };
                            student_model.addColumn("UIN");
                            student_model.addColumn("First name");
                            student_model.addColumn("Middle name");
                            student_model.addColumn("Last name");
                            student_model.addColumn("Gender");
                            student_model.addColumn("Password");
                            student_model.addColumn("Register time");
                            student_model.addColumn("Update time");
                            for (int i = 0; i < result.size(); i++) {
                                student_model.insertRow(i, result.get(i));
                            }
                            table1.setModel(student_model);
                            student_format();
                            break;
                        }
                        case 1:{
                            Vector<Vector<String>> result = sql.execute_gettable_query(query,8);
                            DefaultTableModel professor_model = (DefaultTableModel)table2.getModel();
                            professor_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
                            professor_model.addColumn("UIN");
                            professor_model.addColumn("First name");
                            professor_model.addColumn("Middle name");
                            professor_model.addColumn("Last name");
                            professor_model.addColumn("Gender");
                            professor_model.addColumn("Password");
                            professor_model.addColumn("Register time");
                            professor_model.addColumn("Update time");
                            for (int i = 0; i < result.size(); i++) {
                                professor_model.insertRow(i, result.get(i));
                            }
                            student_format();
                            table2.setModel(professor_model);
                            break;
                        }
                        case 2:{
                            Vector<Vector<String>> result = sql.execute_gettable_query(query,6);
                            DefaultTableModel course_model = (DefaultTableModel)table3.getModel();
                            course_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
                            course_model.addColumn("Course ID");
                            course_model.addColumn("Credit");
                            course_model.addColumn("Title");
                            course_model.addColumn("Department");
                            course_model.addColumn("Total Seats");
                            course_model.addColumn("Seats Available");
                            for (int i = 0; i < result.size(); i++) {
                                course_model.insertRow(i, result.get(i));
                            }
                            table3.setModel(course_model);
                            course_format();
                            break;
                        }
                        case 3:{
                            Vector<Vector<String>> result = sql.execute_gettable_query(query,6);
                            DefaultTableModel homework_model = (DefaultTableModel)table5.getModel();
                            homework_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
                            homework_model.addColumn("Homework ID");
                            homework_model.addColumn("Total points");
                            homework_model.addColumn("Title");
                            homework_model.addColumn("Due date");
                            homework_model.addColumn("Status");
                            homework_model.addColumn("Course ID");
                            for (int i = 0; i < result.size(); i++) {
                                homework_model.insertRow(i, result.get(i));
                            }
                            table5.setModel(homework_model);
                            homework_format();
                            textField1.setEnabled(false);
                            break;
                        }

                        case 4:{
                            Vector<Vector<String>> result = sql.execute_gettable_query(query,3);
                            DefaultTableModel coursemember_model = (DefaultTableModel)table6.getModel();
                            coursemember_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
                            coursemember_model.addColumn("ID");
                            coursemember_model.addColumn("UIN");
                            coursemember_model.addColumn("Course ID");
                            for (int i = 0; i < result.size(); i++) {
                                coursemember_model.insertRow(i, result.get(i));
                            }
                            table6.setModel(coursemember_model);
                            coursemember_format();
                            textField4.setVisible(false);
                            textField5.setVisible(false);
                            textField6.setVisible(false);
                            textField1.setEnabled(false);
                            textField2.setEnabled(false);
                            break;
                        }
                        case 5:{
                            Vector<Vector<String>> result = sql.execute_gettable_query(query,3);
                            DefaultTableModel registerkey_model = (DefaultTableModel)table7.getModel();
                            registerkey_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
                            registerkey_model.addColumn("Register key");
                            registerkey_model.addColumn("Created time");
                            registerkey_model.addColumn("Used time");
                            for (int i = 0; i < result.size(); i++) {
                                registerkey_model.insertRow(i, result.get(i));
                            }
                            table7.setModel(registerkey_model);
                            register_format();
                            textField2.setEnabled(false);
                            textField3.setEnabled(false);
                            break;
                        }
                        case 6:{
                            Vector<Vector<String>> result = sql.execute_gettable_query(query,6);
                            DefaultTableModel changegrade_model = (DefaultTableModel)table4.getModel();
                            changegrade_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
                            changegrade_model.addColumn("UIN");
                            changegrade_model.addColumn("First name");
                            changegrade_model.addColumn("Middle name");
                            changegrade_model.addColumn("Last name");
                            changegrade_model.addColumn("Course");
                            changegrade_model.addColumn("Grade");
                            for (int i = 0; i < result.size(); i++) {
                                changegrade_model.insertRow(i, result.get(i));
                            }
                            table4.setModel(changegrade_model);
                            grade_format();
                            textField1.setEnabled(false);
                            textField2.setEnabled(false);
                            textField3.setEnabled(false);
                            textField4.setEnabled(false);
                            break;
                        }
                    }
                    clear_color();
                    System.out.println("Debug: Order switch finished");
                    resultLabel.setText("Order switch finished");
                }
            }
        };
        // Add action listeners to the buttons
        generateRegisterKeyButton.addActionListener(listener);
        changeStudentGradeButton.addActionListener(listener);
        changeUserRoleButton.addActionListener(listener);
        logoutButton.addActionListener(listener);
        forceRegisterButton.addActionListener(listener);
        addProfessorButton.addActionListener(listener);
        addCourseButton.addActionListener(listener);
        refreshTableButton.addActionListener(listener);
        regcloseradio.addActionListener(listener);
        regopenradio.addActionListener(listener);
        dropcloseradio.addActionListener(listener);
        dropopenradio.addActionListener(listener);
        sortcomboBox1.addActionListener(listener);
        searchButton.addActionListener(listener);
        deleteRowButton.addActionListener(listener);
        addStudentButton.addActionListener(listener);
        undoButton.addActionListener(listener);
        saveChangeButton.addActionListener(listener);

        // Get data from table and put it into the textfields
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(adding_course == 1) {
                    return;
                }
                int row = table1.getSelectedRow();
                Vector<String> temp = new Vector<String>();
                for (int i = 0; i < table1.getColumnCount(); i++) {
                    temp.add(table1.getValueAt(row, i).toString());
                }
                textField1.setText(temp.get(0));
                textField2.setText(temp.get(1));
                textField3.setText(temp.get(2));
                textField4.setText(temp.get(3));
                textField5.setText(temp.get(4));
                textField6.setText(temp.get(5));
                textField1.setEnabled(true);
                textField2.setEnabled(true);
                textField3.setEnabled(true);
                textField4.setEnabled(true);
                textField5.setEnabled(true);
                textField6.setEnabled(true);
                data_for_undo = temp;
                table_index = 1;
                student_format();
                deleteRowButton.setEnabled(true);
            }
        });

        // Get data from table and put it into the textfields
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
                textField1.setText(temp.get(0));
                textField2.setText(temp.get(1));
                textField3.setText(temp.get(2));
                textField4.setText(temp.get(3));
                textField5.setText(temp.get(4));
                textField6.setText(temp.get(5));
                textField1.setEnabled(true);
                textField2.setEnabled(true);
                textField3.setEnabled(true);
                textField4.setEnabled(true);
                textField5.setEnabled(true);
                textField6.setEnabled(true);
                data_for_undo = temp;
                table_index = 2;
                student_format();
                deleteRowButton.setEnabled(true);
            }
        });

        // Get data from table and put it into the textfields
        table3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(adding_course == 1) {
                    return;
                }
                int row = table3.getSelectedRow();
                Vector<String> temp = new Vector<String>();
                for (int i = 0; i < table3.getColumnCount(); i++) {
                    temp.add(table3.getValueAt(row, i).toString());
                }
                textField1.setText(temp.get(0));
                textField2.setText(temp.get(1));
                textField3.setText(temp.get(2));
                textField4.setText(temp.get(3));
                textField5.setText(temp.get(4));
                textField6.setText(temp.get(5));
                textField1.setEnabled(true);
                textField2.setEnabled(true);
                textField3.setEnabled(true);
                textField4.setEnabled(true);
                textField5.setEnabled(true);
                textField6.setEnabled(true);
                data_for_undo = temp;
                table_index = 3;
                course_format();
                deleteRowButton.setEnabled(true);
            }
        });

        // Get data from table and put it into the textfields
        table4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(adding_course == 1) {
                    return;
                }
                int row = table4.getSelectedRow();
                Vector<String> temp = new Vector<String>();
                for (int i = 0; i < table4.getColumnCount(); i++) {
                    temp.add(table4.getValueAt(row, i).toString());
                }
                textField1.setText(temp.get(0));
                textField2.setText(temp.get(1));
                textField3.setText(temp.get(2));
                textField4.setText(temp.get(3));
                textField5.setText(temp.get(4));
                textField6.setText(temp.get(5));
                textField1.setEnabled(false);
                textField2.setEnabled(false);
                textField3.setEnabled(false);
                textField4.setEnabled(false);
                deleteRowButton.setEnabled(false);
                data_for_undo = temp;
                table_index = 4;

            }
        });

        // Get data from table and put it into the textfields
        table5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(adding_course == 1) {
                    return;
                }
                int row = table5.getSelectedRow();
                Vector<String> temp = new Vector<String>();
                for (int i = 0; i < table5.getColumnCount(); i++) {
                    temp.add(table5.getValueAt(row, i).toString());
                }
                textField1.setText(temp.get(0));
                textField2.setText(temp.get(1));
                textField3.setText(temp.get(2));
                textField4.setText(temp.get(3));
                textField5.setText(temp.get(4));
                textField6.setText(temp.get(5));
                textField1.setEnabled(false);
                textField2.setEnabled(true);
                textField3.setEnabled(true);
                textField4.setEnabled(true);
                textField5.setEnabled(true);
                textField6.setEnabled(true);
                data_for_undo = temp;
                table_index = 5;
                homework_format();
                textField1.setEnabled(false);
                deleteRowButton.setEnabled(true);
            }
        });

        // Get data from table and put it into the textfields
        table6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(adding_course == 1) {
                    return;
                }
                int row = table6.getSelectedRow();
                Vector<String> temp = new Vector<String>();
                for (int i = 0; i < table6.getColumnCount(); i++) {
                    temp.add(table6.getValueAt(row, i).toString());
                }
                textField1.setText(temp.get(0));
                textField2.setText(temp.get(1));
                textField3.setText(temp.get(2));
                textField3.setEnabled(false);
                data_for_undo = temp;
                table_index = 6;
                coursemember_format();
                textField1.setEnabled(false);
                textField2.setEnabled(false);
                deleteRowButton.setEnabled(true);
            }
        });


        // Change the search type when user switches the table
        tablePane1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (tablePane1.getSelectedIndex() == 0 || tablePane1.getSelectedIndex() == 1) {
                    searchcomboBox.removeAllItems();
                    searchcomboBox.addItem("UIN");
                    searchcomboBox.addItem("First_name");
                    searchcomboBox.addItem("Middle_name");
                    searchcomboBox.addItem("Last_name");
                    searchcomboBox.addItem("Gender");
                    searchcomboBox.addItem("Password");
                    student_format();
                    Jlabel1.setVisible(true);
                    Jlabel2.setVisible(true);
                    Jlabel3.setVisible(true);
                    Jlabel4.setVisible(true);
                    Jlabel5.setVisible(true);
                    Jlabel6.setVisible(true);
                    deleteRowButton.setEnabled(true);
                } else if (tablePane1.getSelectedIndex() == 2) {
                    searchcomboBox.removeAllItems();
                    searchcomboBox.addItem("Course_ID");
                    searchcomboBox.addItem("Credit");
                    searchcomboBox.addItem("Title");
                    searchcomboBox.addItem("Department");
                    searchcomboBox.addItem("Total_seats");
                    searchcomboBox.addItem("Seats_Left");
                    homework_format();
                    Jlabel1.setVisible(true);
                    Jlabel2.setVisible(true);
                    Jlabel3.setVisible(true);
                    Jlabel4.setVisible(true);
                    Jlabel5.setVisible(true);
                    Jlabel6.setVisible(true);
                    deleteRowButton.setEnabled(true);
                } else if (tablePane1.getSelectedIndex() == 3) {
                    searchcomboBox.removeAllItems();
                    searchcomboBox.addItem("Homework_ID");
                    searchcomboBox.addItem("Total_points");
                    searchcomboBox.addItem("Title");
                    searchcomboBox.addItem("Due_Date");
                    searchcomboBox.addItem("Status");
                    searchcomboBox.addItem("Course_ID");
                    homework_format();
                    Jlabel1.setVisible(true);
                    Jlabel2.setVisible(true);
                    Jlabel3.setVisible(true);
                    Jlabel4.setVisible(true);
                    Jlabel5.setVisible(true);
                    Jlabel6.setVisible(true);
                    deleteRowButton.setEnabled(true);
                } else if (tablePane1.getSelectedIndex() == 4) {
                    searchcomboBox.removeAllItems();
                    searchcomboBox.addItem("Course_ID");
                    searchcomboBox.addItem("UIN");
                    coursemember_format();
                    Jlabel1.setVisible(true);
                    Jlabel2.setVisible(true);
                    Jlabel3.setVisible(true);
                    deleteRowButton.setEnabled(true);
                } else if (tablePane1.getSelectedIndex() == 5) {
                    searchcomboBox.removeAllItems();
                    searchcomboBox.addItem("register_key");
                    searchcomboBox.addItem("Generated_time");
                    searchcomboBox.addItem("Used_time");
                    register_format();
                    Jlabel1.setVisible(true);
                    Jlabel2.setVisible(true);
                    Jlabel3.setVisible(true);
                    deleteRowButton.setEnabled(true);
                } else if (tablePane1.getSelectedIndex() == 6) {
                    searchcomboBox.removeAllItems();
                    searchcomboBox.addItem("UIN");
                    searchcomboBox.addItem("First_name");
                    searchcomboBox.addItem("Middle_name");
                    searchcomboBox.addItem("Last_name");
                    searchcomboBox.addItem("Course");
                    searchcomboBox.addItem("Grade");
                    grade_format();
                    Jlabel1.setVisible(true);
                    Jlabel2.setVisible(true);
                    Jlabel3.setVisible(true);
                    Jlabel4.setVisible(true);
                    Jlabel5.setVisible(true);
                    Jlabel6.setVisible(true);
                    deleteRowButton.setEnabled(false);
                }
            }
        });

        // Get data from table and put it into the textfields
        table7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(adding_course == 1) {
                    return;
                }
                int row = table7.getSelectedRow();
                Vector<String> temp = new Vector<String>();
                for (int i = 0; i < table7.getColumnCount(); i++) {
                    temp.add(table7.getValueAt(row, i).toString());
                }
                textField1.setText(temp.get(0));
                textField2.setText(temp.get(1));
                textField3.setText(temp.get(2));
                data_for_undo = temp;
                register_format();
                table_index = 7;
                textField1.setEnabled(true);
                textField2.setEnabled(false);
                textField3.setVisible(true);
                textField3.setEnabled(false);
                textField4.setVisible(false);
                textField5.setVisible(false);
                textField6.setVisible(false);
                textField2.setEnabled(false);
                Jlabel3.setVisible(true);
                deleteRowButton.setEnabled(true);
            }
        });

        // Save the change what ever user made to the table
        saveChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(e.getActionCommand(), "Save Change")) {
                    String query;
                    // update reg and drop radio first
                    if (regopenradio.isSelected()) {
                        query = "UPDATE boolean_table SET boolean = 1 WHERE idboolean_table = 1";
                    } else {
                        query = "UPDATE boolean_table SET boolean = 0 WHERE idboolean_table = 1";
                    }
                    sql.update_query(query);
                    if (dropopenradio.isSelected()) {
                        query = "UPDATE boolean_table SET boolean = 1 WHERE idboolean_table = 2";
                    } else {
                        query = "UPDATE boolean_table SET boolean = 0 WHERE idboolean_table = 2";
                    }
                    sql.update_query(query);
                    // if user only changed reg and drop radio, then return
                    if (textField1.getText().equals("")) {
                        System.out.println("Debug: Change Saved");
                        return;
                    }


                    // error exception handling
                    if (table_index == 1 || table_index == 2) {
                        int UIN;
                        String temp = textField1.getText().trim();

                        if (check_is_digit(temp) && !temp.equals("")) {
                            resultLabel.setText("Please enter a valid UIN");
                            clear_color();
                            resultLabel.setForeground(Color.red);
                            Jlabel1.setForeground(Color.red);
                            textField1.setText("");
                        } else {
                            UIN = Integer.parseInt(textField1.getText().trim());
                            if (textField2.getText().trim().equals("")) {
                                resultLabel.setText("Please enter a first name");
                                clear_color();
                                resultLabel.setForeground(Color.red);
                                Jlabel2.setForeground(Color.red);
                            } else {
                                String first_n = textField2.getText().trim();
                                String middle_n = textField3.getText().trim();
                                if (textField4.getText().trim().equals("")) {
                                    resultLabel.setText("Please enter a last name");
                                    clear_color();
                                    resultLabel.setForeground(Color.red);
                                    Jlabel3.setForeground(Color.red);
                                    textField4.setText("");
                                } else {
                                    String last_n = textField4.getText().trim();
                                    if (!textField5.getText().trim().equals("Male") &&
                                            !textField5.getText().trim().equals("Female") &&
                                            !textField5.getText().trim().equals("Other")) {
                                        resultLabel.setText("Please enter Male, Female or Other");
                                        clear_color();
                                        resultLabel.setForeground(Color.red);
                                        Jlabel4.setForeground(Color.red);
                                        textField5.setText("");
                                    } else {
                                        String gender = textField5.getText().trim();
                                        if (textField6.getText().trim().equals("")) {
                                            resultLabel.setText("Please enter a password");
                                            clear_color();
                                            resultLabel.setForeground(Color.red);
                                            Jlabel5.setForeground(Color.red);
                                            textField6.setText("");
                                        } else {
                                            String passwd = textField6.getText().trim();
                                            // do the update query for student table
                                            if (table_index == 1 ) {
                                                query = "UPDATE student SET UIN =" + UIN + ", First_name = '" + first_n +
                                                        "', Middle_name = '" + middle_n + "', Last_name = '" + last_n +
                                                        "', Gender = '" + gender + "', Password = '" + passwd + "' WHERE UIN =" +
                                                        data_for_undo.get(0) + ";";
                                                sql.update_query(query);
                                                resultLabel.setText("Change Saved");
                                                enable_everything();
                                                clear_color();
                                                initialize_Student_table();
                                            } else if (table_index == 2) {
                                                // do the update query for professor table
                                                query = "UPDATE professor SET UIN =" + UIN + ", First_name = '" + first_n +
                                                        "', Middle_name = '" + middle_n + "', Last_name = '" + last_n +
                                                        "', Gender = '" + gender + "', Password = '" + passwd + "' WHERE UIN =" +
                                                        data_for_undo.get(0) + ";";

                                                sql.update_query(query);
                                                resultLabel.setText("Change Saved");
                                                enable_everything();
                                                clear_color();
                                                initialize_Professor_table();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (table_index == 3) {
                        // error exception handling
                        if (textField1.getText().trim().equals("")) {
                            resultLabel.setText("Please enter a course ID");
                            clear_color();
                            resultLabel.setForeground(Color.red);
                            Jlabel1.setForeground(Color.red);
                        } else {
                            String c_id = textField1.getText().trim();
                            if (check_is_digit(textField2.getText().trim()) || textField2.getText().trim().equals("")) {
                                resultLabel.setText("Please enter a number for credit");
                                clear_color();
                                resultLabel.setForeground(Color.red);
                                Jlabel2.setForeground(Color.red);
                                textField2.setText("");
                            } else {
                                int credit = Integer.parseInt(textField2.getText().trim());
                                if (textField3.getText().trim().equals("")) {
                                    resultLabel.setText("Please enter a title");
                                    clear_color();
                                    resultLabel.setForeground(Color.red);
                                    Jlabel3.setForeground(Color.red);
                                } else {
                                    String name = textField3.getText().trim();
                                    if (textField3.getText().trim().equals("")) {
                                        resultLabel.setText("Please enter a title");
                                        clear_color();
                                        resultLabel.setForeground(Color.red);
                                        Jlabel0.setForeground(Color.red);
                                    } else {
                                        String title = textField3.getText().trim();
                                        query = "SELECT * FROM department WHERE Name = '" + textField4.getText() + "';";
                                        if (sql.check_if_exist(query)) {
                                            resultLabel.setText("Invalid Department");
                                            clear_color();
                                            resultLabel.setForeground(Color.red);
                                            Jlabel4.setForeground(Color.red);
                                        } else {
                                            String department = textField4.getText().trim();
                                            if(check_is_digit(textField5.getText().trim()) || textField5.getText().trim().equals("")) {
                                                resultLabel.setText("Please enter a number for seat capacity");
                                                clear_color();
                                                resultLabel.setForeground(Color.red);
                                                Jlabel4.setForeground(Color.red);
                                                textField5.setText("");
                                            } else {
                                                int seats = Integer.parseInt(textField5.getText().trim());
                                                if(check_is_digit(textField6.getText().trim()) || textField6.getText().trim().equals("")) {
                                                    resultLabel.setText("Invalid number of seats available");
                                                    clear_color();
                                                    resultLabel.setForeground(Color.red);
                                                    Jlabel4.setForeground(Color.red);
                                                    textField4.setText("");
                                                } else {
                                                    int seats_left = Integer.parseInt(textField6.getText().trim());
                                                    if (seats_left > seats) {
                                                        resultLabel.setText("Invalid number of seats available");
                                                        clear_color();
                                                        resultLabel.setForeground(Color.red);
                                                        Jlabel4.setForeground(Color.red);
                                                        textField6.setText("");
                                                    } else {
                                                        // do the update query for course table
                                                        query = "UPDATE course SET Course_ID = '" + c_id + "', Credit = " + credit +
                                                                ", Title = '" + name + "', Title = '" + title + "', Department = '" + department +
                                                                "', Total_seats = " + seats + ", Seats_left = " + seats_left + " WHERE Course_ID = '" +
                                                                data_for_undo.get(0) + "';";
                                                        sql.update_query(query);
                                                        resultLabel.setText("Change Saved");
                                                        enable_everything();
                                                        clear_color();
                                                        initialize_Course_table();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (table_index == 4) {
                        query = "SELECT * FROM course WHERE Course_ID = '" + textField5.getText() + "';";
                        // complex query
                        // error exception handling
                        if (sql.check_if_exist(query) || sql.check_if_exist("SELECT student.UIN,student.First_name," +
                                "student.Middle_name,student.Last_name,final_grade." +
                                "Course,final_grade.Grade FROM student " +
                                "INNER JOIN final_grade ON student.UIN = final_grade.UIN WHERE student.UIN =" +
                                data_for_undo.get(0) + " AND final_grade.Course = '" + textField5.getText() + "';")) {
                            resultLabel.setText("Invalid or duplicate course ID");
                            clear_color();
                            resultLabel.setForeground(Color.red);
                            Jlabel5.setForeground(Color.red);
                        } else {
                            String c_id = textField5.getText().trim();
                            if (textField6.getText().trim().equals("") || textField6.getText().length() > 1) {
                                resultLabel.setText("Please enter a single character for the grade");
                                clear_color();
                                resultLabel.setForeground(Color.red);
                                Jlabel5.setForeground(Color.red);
                            } else {
                                // do the update query for final_grade table
                                String grade = textField6.getText().trim();
                                int UIN = Integer.parseInt(textField1.getText().trim());
                                query = "UPDATE final_grade SET Course = '" + c_id + "', Grade = '" + grade + "' WHERE UIN =" + UIN + " AND Course = '" + data_for_undo.get(4) + "';";
                                sql.update_query(query);
                                resultLabel.setText("Change Saved");
                                enable_everything();
                                clear_color();
                                initialize_changegrade_table();
                            }
                        }
                    } else if (table_index == 5) {
                        // error exception handling
                        if (check_is_digit(textField2.getText().trim()) || textField2.getText().trim().equals("")) {
                            resultLabel.setText("Please enter a number for total points");
                            clear_color();
                            resultLabel.setForeground(Color.red);
                            Jlabel2.setForeground(Color.red);
                            textField2.setText("");
                        } else {
                            int Total_points = Integer.parseInt(textField2.getText().trim());
                            if (textField3.getText().trim().equals("")) {
                                resultLabel.setText("Please enter a title");
                                clear_color();
                                resultLabel.setForeground(Color.red);
                                Jlabel3.setForeground(Color.red);
                            } else {
                                String Title = textField3.getText().trim();
                                if (textField4.getText().trim().equals("")) {
                                    resultLabel.setText("Please enter a due date");
                                    clear_color();
                                    resultLabel.setForeground(Color.red);
                                    Jlabel0.setForeground(Color.red);
                                } else {
                                    String Due_Date = textField4.getText().trim();
                                    if (textField5.getText().trim().equals("")) {
                                        resultLabel.setText("Please enter a status");
                                        clear_color();
                                        resultLabel.setForeground(Color.red);
                                        Jlabel4.setForeground(Color.red);
                                    } else {
                                        String Status = textField5.getText().trim();
                                        query = "SELECT * FROM homework WHERE Course_ID = '" + textField6.getText().trim() + "';";
                                        if (sql.check_if_exist(query)) {
                                            resultLabel.setText("Invalid course ID");
                                            clear_color();
                                            resultLabel.setForeground(Color.red);
                                            Jlabel6.setForeground(Color.red);
                                        } else {
                                            // do the update query for homework table
                                            String Course_ID = textField6.getText().trim();
                                            query = "UPDATE homework SET Course_ID = '" + Course_ID + "', Total_points = " + Total_points +
                                                    ", Title = '" + Title + "', Due_Date = '" + Due_Date + "', Status = '" + Status + "' WHERE Course_ID = '" +
                                                    data_for_undo.get(5) + "';";
                                            sql.update_query(query);
                                            resultLabel.setText("Change Saved");
                                            enable_everything();
                                            clear_color();
                                            initialize_Homework_table();
                                        }
                                    }
                                }
                            }
                        }
                    } else if (table_index == 6) {
                        // error exception handling
                        if (sql.check_if_exist("SELECT * FROM course WHERE Course_ID = '" + textField3.getText() + "';")) {
                            resultLabel.setText("Invalid course ID");
                            clear_color();
                            resultLabel.setForeground(Color.red);
                            Jlabel3.setForeground(Color.red);
                        } else {
                            // update the course member table
                            query = "UPDATE course_member SET Course_ID = '" + textField3.getText() + "' WHERE UIN = " + data_for_undo.get(1) + " AND Course_ID = '" + data_for_undo.get(2) + "';";
                            sql.update_query(query);
                            resultLabel.setText("Change Saved");
                            enable_everything();
                            clear_color();
                            initialize_coursemember_table();
                        }
                    } else if (table_index == 7) {
                        // error exception handling
                        if (textField1.getText().equals("")) {
                            resultLabel.setText("Please enter a key");
                            clear_color();
                            resultLabel.setForeground(Color.red);
                            Jlabel1.setForeground(Color.red);
                        } else {
                            // update the register key table
                            query = "UPDATE register_key SET reg_key = '" + textField1.getText() + "' WHERE reg_key = '" + data_for_undo.get(0) + "';";
                            sql.update_query(query);
                            resultLabel.setText("Change Saved");
                            enable_everything();
                            clear_color();
                            initialize_registerkey_table();
                            register_format();
                        }
                    }
                    System.out.println("Debug: Change Saved");
                }
            }
        });
    }

    // helper function to check if the input is a digit
    public boolean check_is_digit(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    // helper function to quick generate a error window
    public void error_message(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    // function to clear the table you want
    public void clear_table(JTable table) {
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.setRowCount(0);
        model.setColumnCount(0);
        table.setModel(model);
    }

    // disable everything
    public void disable_everything () {
        generateRegisterKeyButton.setEnabled(false);
        addCourseButton.setEnabled(false);
        addProfessorButton.setEnabled(false);
        addStudentButton.setEnabled(false);
        changeUserRoleButton.setEnabled(false);
        changeStudentGradeButton.setEnabled(false);
        forceRegisterButton.setEnabled(false);
        textField1.setEnabled(false);
        textField2.setEnabled(false);
        textField3.setEnabled(false);
        textField4.setEnabled(false);
        textField5.setEnabled(false);
        textField6.setEnabled(false);
        regcloseradio.setEnabled(false);
        regopenradio.setEnabled(false);
        dropcloseradio.setEnabled(false);
        dropopenradio.setEnabled(false);
        saveChangeButton.setEnabled(false);
        refreshTableButton.setEnabled(false);
        searchcomboBox.setEnabled(false);
        searchtextField.setEnabled(false);
        searchButton.setEnabled(false);
        table1.setEnabled(false);
        table2.setEnabled(false);
        table3.setEnabled(false);
        table4.setEnabled(false);
    }

    // enable everything
    public void enable_everything () {
        generateRegisterKeyButton.setEnabled(true);
        addCourseButton.setEnabled(true);
        addProfessorButton.setEnabled(true);
        addStudentButton.setEnabled(true);
        changeStudentGradeButton.setEnabled(true);
        changeUserRoleButton.setEnabled(true);
        forceRegisterButton.setEnabled(true);
        textField1.setEnabled(true);
        textField2.setEnabled(true);
        textField3.setEnabled(true);
        textField4.setEnabled(true);
        textField5.setEnabled(true);
        textField6.setEnabled(true);
        regcloseradio.setEnabled(true);
        regopenradio.setEnabled(true);
        dropcloseradio.setEnabled(true);
        dropopenradio.setEnabled(true);
        saveChangeButton.setEnabled(true);
        refreshTableButton.setEnabled(true);
        searchcomboBox.setEnabled(true);
        searchtextField.setEnabled(true);
        searchButton.setEnabled(true);
        table1.setEnabled(true);
        table2.setEnabled(true);
        table3.setEnabled(true);
        table4.setEnabled(true);
        textField6.setVisible(true);
        undoButton.setVisible(true);
        deleteRowButton.setVisible(true);
        Jlabel0.setVisible(false);
        departmentCombobox.setVisible(false);
        Jlabel1.setEnabled(true);
        Jlabel2.setEnabled(true);
        Jlabel3.setEnabled(true);
        Jlabel4.setEnabled(true);
        Jlabel5.setEnabled(true);
        Jlabel6.setEnabled(true);
    }

    // initialize student table and display it
    public void initialize_Student_table() {
        String query = "SELECT UIN,First_name,Middle_name,Last_name,Gender,Password,Register_time,Update_time FROM student;";
        Vector<Vector<String>> result = sql.execute_gettable_query(query,8);
        DefaultTableModel student_model = (DefaultTableModel)table1.getModel();
        student_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        student_model.addColumn("UIN");
        student_model.addColumn("First name");
        student_model.addColumn("Middle name");
        student_model.addColumn("Last name");
        student_model.addColumn("Gender");
        student_model.addColumn("Password");
        student_model.addColumn("Register time");
        student_model.addColumn("Update time");
        for (int i = 0; i < result.size(); i++) {
            student_model.insertRow(i, result.get(i));
        }
        table1.setModel(student_model);
        student_format();

    }

    // initialize professor table and display it
    public void initialize_Professor_table() {
        String query = "SELECT UIN,First_name,Middle_name,Last_name,Gender,Password,Register_time,Update_time FROM professor;";
        Vector<Vector<String>> result = sql.execute_gettable_query(query,8);
        DefaultTableModel professor_model = (DefaultTableModel)table1.getModel();
        professor_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        professor_model.addColumn("UIN");
        professor_model.addColumn("First name");
        professor_model.addColumn("Middle name");
        professor_model.addColumn("Last name");
        professor_model.addColumn("Gender");
        professor_model.addColumn("Password");
        professor_model.addColumn("Register time");
        professor_model.addColumn("Update time");
        for (int i = 0; i < result.size(); i++) {
            professor_model.insertRow(i, result.get(i));
        }
        table2.setModel(professor_model);
    }

    // initialize course table and display it
    public void initialize_Course_table() {
        String query = "SELECT * FROM course;";
        Vector<Vector<String>> result = sql.execute_gettable_query(query,6);
        DefaultTableModel course_model = (DefaultTableModel)table1.getModel();
        course_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        course_model.addColumn("Course ID");
        course_model.addColumn("Credit");
        course_model.addColumn("Title");
        course_model.addColumn("Department");
        course_model.addColumn("Total Seats");
        course_model.addColumn("Seats Available");
        for (int i = 0; i < result.size(); i++) {
            course_model.insertRow(i, result.get(i));
        }
        table3.setModel(course_model);
    }

    // initialize homework table and display it
    public void initialize_Homework_table () {
        String query = "SELECT * FROM homework;";
        Vector<Vector<String>> result = sql.execute_gettable_query(query,6);
        DefaultTableModel homework_model = (DefaultTableModel)table1.getModel();
        homework_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        homework_model.addColumn("Homework ID");
        homework_model.addColumn("Total points");
        homework_model.addColumn("Title");
        homework_model.addColumn("Due date");
        homework_model.addColumn("Status");
        homework_model.addColumn("Course ID");
        for (int i = 0; i < result.size(); i++) {
            homework_model.insertRow(i, result.get(i));
        }
        table5.setModel(homework_model);
        textField1.setEnabled(false);
    }

    // initialize course member table and display it
    public void initialize_coursemember_table () {
        String query = "SELECT * FROM course_member;";
        Vector<Vector<String>> result = sql.execute_gettable_query(query,3);
        DefaultTableModel coursemember_model = (DefaultTableModel)table1.getModel();
        coursemember_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        coursemember_model.addColumn("ID");
        coursemember_model.addColumn("UIN");
        coursemember_model.addColumn("Course ID");
        for (int i = 0; i < result.size(); i++) {
            coursemember_model.insertRow(i, result.get(i));
        }
        table6.setModel(coursemember_model);
        textField4.setVisible(false);
        textField5.setVisible(false);
        textField6.setVisible(false);
        textField1.setEnabled(false);
        textField2.setEnabled(false);
    }

    // initialize register key table and display it
    public void initialize_registerkey_table () {
        String query = "SELECT * FROM register_key;";
        Vector<Vector<String>> result = sql.execute_gettable_query(query,3);
        DefaultTableModel registerkey_model = (DefaultTableModel)table1.getModel();
        registerkey_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        registerkey_model.addColumn("Register key");
        registerkey_model.addColumn("Created time");
        registerkey_model.addColumn("Used time");
        for (int i = 0; i < result.size(); i++) {
            registerkey_model.insertRow(i, result.get(i));
        }
        table7.setModel(registerkey_model);
    }

    // initialize grade table and display it
    public void initialize_changegrade_table () {
        clear_table(table4);
        String query = "SELECT student.UIN,student.First_name," +
                "student.Middle_name,student.Last_name,final_grade." +
                "Course,final_grade.Grade FROM student " +
                "INNER JOIN final_grade ON student.UIN = final_grade.UIN;";
        Vector<Vector<String>> result = sql.execute_gettable_query(query,6);
        DefaultTableModel changegrade_model = (DefaultTableModel)table1.getModel();
        changegrade_model = new DefaultTableModel(){ public boolean isCellEditable(int row, int column) { return false; }};
        changegrade_model.addColumn("UIN");
        changegrade_model.addColumn("First name");
        changegrade_model.addColumn("Middle name");
        changegrade_model.addColumn("Last name");
        changegrade_model.addColumn("Course");
        changegrade_model.addColumn("Grade");
        for (int i = 0; i < result.size(); i++) {
            changegrade_model.insertRow(i, result.get(i));
        }
        table4.setModel(changegrade_model);
    }

    // quick setup for student layout
    public void student_format () {
        Jlabel1.setText("UIN");
        Jlabel2.setText("First name");
        Jlabel3.setText("Middle name");
        Jlabel4.setText("Last name");
        Jlabel5.setText("Gender");
        Jlabel6.setText("Password");
        textField4.setVisible(true);
        textField5.setVisible(true);
        textField6.setVisible(true);
        textField1.setEnabled(true);
        textField2.setEnabled(true);
        textField3.setEnabled(true);
        textField4.setEnabled(true);
    }

    // quick setup for professor layout
    public void course_format () {
        Jlabel1.setText("Course ID");
        Jlabel2.setText("Credit");
        Jlabel3.setText("Name");
        Jlabel4.setText("Department");
        Jlabel5.setText("Total Seats");
        Jlabel6.setText("Seats Available");
        textField4.setVisible(true);
        textField5.setVisible(true);
        textField6.setVisible(true);
        textField1.setEnabled(true);
        textField2.setEnabled(true);
        textField3.setEnabled(true);
        textField4.setEnabled(true);
    }

    // quick setup for course member layout
    public void homework_format () {
        Jlabel1.setText("Homework ID");
        Jlabel2.setText("Total points");
        Jlabel3.setText("Title");
        Jlabel4.setText("Due date");
        Jlabel5.setText("Status");
        Jlabel6.setText("Course ID");
        textField4.setVisible(true);
        textField5.setVisible(true);
        textField6.setVisible(true);
        textField1.setEnabled(true);
        textField2.setEnabled(true);
        textField3.setEnabled(true);
        textField4.setEnabled(true);
    }

    // quick setup for grade layout
    public void grade_format () {
        Jlabel1.setText("UIN");
        Jlabel2.setText("First name");
        Jlabel3.setText("Middle name");
        Jlabel4.setText("Last name");
        Jlabel5.setText("Course");
        Jlabel6.setText("Grade");
        textField1.setVisible(true);
        textField2.setVisible(true);
        textField3.setVisible(true);
        textField4.setVisible(true);
        textField5.setVisible(true);
        textField6.setVisible(true);

        textField1.setEnabled(false);
        textField2.setEnabled(false);
        textField3.setEnabled(false);
        textField4.setEnabled(false);
    }

    // quick setup for course member layout
    public void coursemember_format () {
        Jlabel1.setText("ID");
        Jlabel2.setText("UIN");
        Jlabel3.setText("Course ID");
        Jlabel4.setText("");
        Jlabel5.setText("");
        Jlabel6.setText("");
        textField4.setVisible(false);
        textField5.setVisible(false);
        textField6.setVisible(false);
        textField1.setEnabled(true);
        textField2.setEnabled(true);
        textField3.setEnabled(true);
        textField4.setEnabled(true);
    }


    // quick setup for register key layout
    public void register_format () {
        Jlabel1.setText("Register key");
        Jlabel2.setText("Created time");
        Jlabel3.setText("Used time");
        Jlabel4.setText("");
        Jlabel5.setText("");
        Jlabel6.setText("");
        textField3.setVisible(true);
        textField4.setVisible(false);
        textField5.setVisible(false);
        textField6.setVisible(false);
        textField1.setEnabled(true);
        textField2.setEnabled(false);
    }

    // clear all the colors
    public void clear_color () {
        Jlabel0.setForeground(Color.black);
        Jlabel1.setForeground(Color.black);
        Jlabel2.setForeground(Color.black);
        Jlabel3.setForeground(Color.black);
        Jlabel4.setForeground(Color.black);
        Jlabel5.setForeground(Color.black);
        Jlabel6.setForeground(Color.black);
    }

    // clear all the textfields
    public void clear_text () {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
    }
}
