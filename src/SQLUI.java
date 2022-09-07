import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SQLUI{
    private JButton LoginButton;
    private JButton Exitbutton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JPanel panel1;

    public String q_url;
    public String q_user;
    public String q_password;



    public SQLUI() {

        JFrame frame = new JFrame("SQL Setting");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(300, 400);
        frame.setLocation(960, 540);
        frame.setVisible(true);
        textField1.setText("localhost");
        textField2.setText("3306");
        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = "jdbc:mysql://" + textField1.getText().trim() + ":" + textField2.getText().trim() + "/university";
                String user = textField3.getText().trim();
                String password = textField4.getText().trim();
                String query = "SELECT VERSION()";  // any SQL query language works

                // Create a connection
                try (Connection con = DriverManager.getConnection(url, user, password);
                     Statement st = con.createStatement();

                     // Execute the query, and return the result set
                     ResultSet rs = st.executeQuery(query)) {

                    // while(rs.next()) works too, and will print all the outputs
                    if (rs.next()) {
                        Main.url = url;
                        Main.user = user;
                        Main.password = password;
                        JOptionPane.showMessageDialog(null, "Successfully connected to server");
                        frame.dispose();
                    }
                } catch (SQLException ex) {
                    // If there is an error, print the error message
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                    error_message("Could not connect to server");
                }


            }
        });

        Exitbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


    }

    public void error_message(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
