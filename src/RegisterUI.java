// Classname: RegisterUI
// Description: This class can allow user to register new account.
// Author: Haoji Ni

import java.awt.datatransfer.Clipboard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

// Where LoginUI is the class name
// JFrame is the class name of the window
// ActionListener is for the event, must include event listener function or it will not work
public class RegisterUI extends JFrame implements ActionListener {
    // Initialize the components
    JButton Register_button, Back_Button;
    JRadioButton Male_radio, Female_radio, Other_radio;
    JPanel jp1, jp2, jp3, jp4, jp5, jp6, jp7, jp8, jp9;
    JTextField Key_textbox, Firstname_textbox, Middlename_textbox, Lastname_textbox;
    JLabel Jtext1, Jtext2, Jtext3, Jtext4, Jtext5, Jtext6, Jtext7, Jtext8;
    JPasswordField Password_textbox, Confirm_textbox;
    ButtonGroup Gender_selection_group;

    public RegisterUI() {

        // Create components
        Register_button = new JButton("Register");
        Back_Button = new JButton("Back");

        // Set listener
        Register_button.addActionListener(this);
        Back_Button.addActionListener(this);

        // Set radio button
        Male_radio = new JRadioButton("Male");
        Female_radio = new JRadioButton("Female");
        Other_radio = new JRadioButton("Other");

        // Add to ButtonGroup
        Gender_selection_group = new ButtonGroup();
        Gender_selection_group.add(Male_radio);
        Gender_selection_group.add(Female_radio);
        Gender_selection_group.add(Other_radio);

        // Create JPanel
        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();
        jp5 = new JPanel();
        jp6 = new JPanel();
        jp7 = new JPanel();
        jp8 = new JPanel();
        jp9 = new JPanel();


        // Create JLabel to display text
        Jtext1 = new JLabel("*Register Key:");
        Jtext2 = new JLabel("   *First Name:");
        Jtext3 = new JLabel("Middle Name:");
        Jtext4 = new JLabel("   *Last Name:");
        Jtext5 = new JLabel("                *Password:");
        Jtext6 = new JLabel("*Confirm Password:");
        Jtext7 = new JLabel("This is for error message");
        Jtext7.setVisible(false);
        Jtext7.setForeground(Color.red);
        Jtext8 = new JLabel("*Gender");

        // Create text box
        Key_textbox = new JTextField(13);
        Firstname_textbox = new JTextField(13);
        Middlename_textbox = new JTextField(13);
        Lastname_textbox = new JTextField(13);
        Password_textbox = new JPasswordField(10);
        Confirm_textbox = new JPasswordField(10);
        Confirm_textbox.setTransferHandler(null);


        /*                  Combine components into one panel                  */

        // Key panel
        jp1.add(Jtext1);
        jp1.add(Key_textbox);

        // First name panel
        jp2.add(Jtext2);
        jp2.add(Firstname_textbox);

        // Middle name panel
        jp3.add(Jtext3);
        jp3.add(Middlename_textbox);

        // Last name panel
        jp4.add(Jtext4);
        jp4.add(Lastname_textbox);

        // Gender panel
        jp5.add(Jtext8);
        jp5.add(Male_radio);
        jp5.add(Female_radio);
        jp5.add(Other_radio);

        // Password panel
        jp6.add(Jtext5);
        jp6.add(Password_textbox);

        // Confirm password panel
        jp7.add(Jtext6);
        jp7.add(Confirm_textbox);


        // Error message panel
        jp8.add(Jtext7);

        // Action panel
        jp9.add(Register_button);
        jp9.add(Back_Button);

        /*                  End of combine components into one panel                  */


        // Add all panels into one frame, inorder to display them
        this.add(jp1);
        this.add(jp2);
        this.add(jp3);
        this.add(jp4);
        this.add(jp5);
        this.add(jp6);
        this.add(jp7);
        this.add(jp8);
        this.add(jp9);

        this.setLayout(new GridLayout(9, 1));        // Set GridLayout, 4 rows, 1 column, reason why there are jp1,jp2,jp3,jp4
        this.setTitle("Register");     // Set title
        this.setSize(300, 400);      // Set size
        this.setLocation(960, 540);     // Set location
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);        // When user click close button, the program will exit
        this.setVisible(true);      // Show the frame
        this.setResizable(false);       // Disable the resize function
    }

    public void actionPerformed(ActionEvent e) {
        if (Objects.equals(e.getActionCommand(), "Register")) {
            String gender = "";
            reset_color();
            if (Male_radio.isSelected()) {
                gender = "Male";
                collect_info(gender);
            } else if (Female_radio.isSelected()) {
                gender = "Female";
                collect_info(gender);
            } else if (Other_radio.isSelected()) {
                gender = "Other";
                collect_info(gender);
            } else {
                Jtext7.setText("Please select a gender");
                Jtext7.setVisible(true);
            }

        } else if (Objects.equals(e.getActionCommand(), "Back")) {
            this.dispose();
            LoginUI login = new LoginUI();
        }
    }

    public void reset_color() {
        Jtext1.setForeground(Color.black);
        Jtext2.setForeground(Color.black);
        Jtext3.setForeground(Color.black);
        Jtext4.setForeground(Color.black);
        Jtext5.setForeground(Color.black);
        Jtext6.setForeground(Color.black);
        Jtext8.setForeground(Color.black);
    }

    public void collect_info(String gender) {
        String key, firstname, middlename, lastname, password, confirm;
        char[] temp1, temp2;
        key = Key_textbox.getText();
        firstname = Firstname_textbox.getText().trim();
        middlename = Middlename_textbox.getText().trim();
        lastname = Lastname_textbox.getText().trim();
        temp1 = Password_textbox.getPassword();
        temp2 = Confirm_textbox.getPassword();
        password = String.valueOf(temp1);
        confirm = String.valueOf(temp2);
        // Error exception handling
        if (Objects.equals(key, "")) {
            Jtext7.setText("Please enter a key");
            Jtext1.setForeground(Color.red);
            Jtext7.setVisible(true);
        } else if (Objects.equals(firstname, "")) {
            Jtext7.setText("Please enter a first name");
            Jtext2.setForeground(Color.red);
            Jtext7.setVisible(true);
        } else if (Objects.equals(lastname, "")) {
            Jtext7.setText("Please enter a last name");
            Jtext4.setForeground(Color.red);
            Jtext7.setVisible(true);
        } else if (Objects.equals(password, "")) {
            Jtext7.setText("Please enter a password");
            Jtext5.setForeground(Color.red);
            Jtext7.setVisible(true);
        } else if (Objects.equals(confirm, "")) {
            Jtext7.setText("Please reenter your password");
            Jtext6.setForeground(Color.red);
            Jtext7.setVisible(true);
        } else {
            reset_color();
            Authentication auth = new Authentication();
            int UIN = auth.Register_User(key, firstname, middlename, lastname, password, confirm, gender);
            if (UIN == 2) {
                Jtext7.setText("Confirm password does not match");
                Jtext6.setForeground(Color.red);
                Jtext7.setVisible(true);
            } else if (UIN == 3) {
                Jtext7.setText("Password too weak");
                Jtext5.setForeground(Color.red);
                Jtext7.setVisible(true);
            } else if (UIN == 1) {
                Jtext7.setText("Register key does not exist");
                Jtext1.setForeground(Color.red);
                Jtext7.setVisible(true);
            } else {
                System.out.println("Debug: Register successfully, UIN is " + UIN);
                JOptionPane.showMessageDialog(null, "Register successfully, your UIN is " + UIN,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                setClipboardString(Integer.toString(UIN));
                Jtext7.setForeground(Color.black);
                Jtext7.setVisible(true);
                this.dispose();
                LoginUI login = new LoginUI();
                login.NetID_textbox.setText(Integer.toString(UIN));
            }
        }
    }

    // Function to set clipboard contents
    public static void setClipboardString(String text) {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        Transferable trans = new StringSelection(text);

        clipboard.setContents(trans, null);
    }
}
