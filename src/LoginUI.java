// Classname: LoginUI
// Description: This class is used to display login UI
// and send query to database
// Author: Haoji Ni

import com.sun.deploy.panel.NumberDocument;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;


public class LoginUI extends JFrame implements ActionListener {
    // Initialize the components
    JButton Login_button, Register_button, Quit_Button;
    JRadioButton Professor_radio, Student_radio, Admin_radio;
    JPanel jp1,jp2,jp3,jp4;
    JTextField NetID_textbox;
    JLabel Jtext1, Jtext2, Jtext3;
    JPasswordField Password_textbox;
    ButtonGroup Role_selection_group;
    int New_UIN = 0;
    Query sql = new Query();



    public LoginUI()
    {

        // Create components
        Login_button =new JButton("Quit");
        Register_button =new JButton("Register");
        Quit_Button =new JButton("Login");

        // Set listener
        Login_button.addActionListener(this);
        Register_button.addActionListener(this);
        Quit_Button.addActionListener(this);

        // Set radio button
        Professor_radio =new JRadioButton("Professor");
        Student_radio =new JRadioButton("Student");
        Admin_radio =new JRadioButton("Admin");

        // Add to ButtonGroup
        Role_selection_group =new ButtonGroup();
        Role_selection_group.add(Professor_radio);
        Role_selection_group.add(Student_radio);
        Role_selection_group.add(Admin_radio);
        Student_radio.setSelected(true); // Default selection is Student

        // Create JPanel
        jp1=new JPanel();
        jp2=new JPanel();
        jp3=new JPanel();
        jp4=new JPanel();

        // Create JLabel to display text
        Jtext1 =new JLabel("            UIN");
        Jtext2 =new JLabel("Password");
        Jtext3 =new JLabel("Role：");

        NetID_textbox =new JTextField(10); // create text box, Length of text box for NetID
        NetID_textbox.setDocument(new NumberDocument());
        Password_textbox =new JPasswordField(10); // create text box, Length of text box for Password


        /*                  Combine components into one panel                  */

        // NetID panel
        jp1.add(Jtext1);
        jp1.add(NetID_textbox);

        // Password panel
        jp2.add(Jtext2);
        jp2.add(Password_textbox);


        // Role panel
        jp3.add(Jtext3);
        jp3.add(Professor_radio);
        jp3.add(Student_radio);
        jp3.add(Admin_radio);

        // Action panel
        jp4.add(Login_button);
        jp4.add(Register_button);
        jp4.add(Quit_Button);

        /*                  End of combine components into one panel                  */


        // Add all panels into one frame, inorder to display them
        this.add(jp1);
        this.add(jp2);
        this.add(jp3);
        this.add(jp4);

        this.setLayout(new GridLayout(4,1));        // Set GridLayout, 4 rows, 1 column, reason why there are jp1,jp2,jp3,jp4
        this.setTitle("Login");     // Set title
        this.setSize(320,200);      // Set size
        this.setLocation(960, 540);     // Set location
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        // When user click close button, the program will exit
        this.setVisible(true);      // Show the frame
        this.setResizable(false);       // Disable the resize function

        // For test only
        // ProfessorUI professor_ui = new ProfessorUI(285472361);
        // StudentUI student_ui = new StudentUI(271832772);
        // AdminUI admin_ui = new AdminUI();

    }


    // When user click button, this function will be called
    public void actionPerformed(ActionEvent e) {
        // Event if user click Login button
        if (Objects.equals(e.getActionCommand(), "Login")) {
            char[] temp1 = Password_textbox.getPassword();
            // exception handling
            if (NetID_textbox.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "Please enter your UIN");
                return;
            }
            if (temp1.length == 0) {
                JOptionPane.showMessageDialog(null, "Please enter your password");
                return;
            }
            String password = new String(temp1);
            int netid = Integer.parseInt(NetID_textbox.getText().trim());

            // check which radio is selected
            if (Professor_radio.isSelected()) {
                Authentication auth_prof = new Authentication();

                // check if the user is in the professor table
                // if yes, then open the professor UI
                if (auth_prof.login(netid, password, "Professor")) {
                    System.out.println("Debug: Professor login Success");
                    if (sql.check_if_exist("SELECT * FROM course_member WHERE UIN = " + netid)) {
                        error_message("You are not tutor of any course, please contact the administrator");
                    } else {
                        this.dispose();
                        ProfessorUI professor_ui = new ProfessorUI(netid);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid UIN or Password",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    NetID_textbox.setText("");
                    Password_textbox.setText("");
                }

                // check if the user is in the student table
                // if yes, then open the student UI
            } else if (Student_radio.isSelected()) {
                Authentication auth_stu = new Authentication();
                if (auth_stu.login(netid, password, "Student")) {
                    System.out.println("Debug: Student login Success");
                    this.dispose();
                    StudentUI student_ui = new StudentUI(netid);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid UIN or Password",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    NetID_textbox.setText("");
                    Password_textbox.setText("");
                }

                // check if the user is in the admin table
                // if yes, then open the admin UI
            } else if (Admin_radio.isSelected()) {
                Authentication auth_admin = new Authentication();
                if (auth_admin.login(netid, password, "Admin")) {
                    this.dispose();
                    System.out.println("Debug: Admin login Success");
                    AdminUI admin_ui = new AdminUI();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid UIN or Password",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    NetID_textbox.setText("");
                    Password_textbox.setText("");
                }
            }
            // if user click Register button, then open the register UI
        } else if (Objects.equals(e.getActionCommand(), "Register")) {
            this.dispose();
            RegisterUI reg_ui = new RegisterUI();
        } else if (Objects.equals(e.getActionCommand(), "Quit")) {
            System.exit(0);
        }
    }

    // Helper function to display error message window
    public void error_message(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

}