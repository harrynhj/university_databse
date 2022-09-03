// Classname: Query
// Description: This class is used to send query to database
// Author: Haoji Ni

import javax.swing.*;
import java.sql.*;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;


public class Query {
    // SQL connection function

    static String url = "please replace this line to your own server url";
    static String user = "replace this line";
    static String password = "replace this password";
    public void Test_Connection()
    {

        // Test Command
        String query = "SELECT VERSION()";  // any SQL query language works

        // Create a connection
        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();

             // Execute the query, and return the result set
             ResultSet rs = st.executeQuery(query)) {

            // while(rs.next()) works too, and will print all the outputs
            if (rs.next()) {
                System.out.println("Debug: MySQL server connected");
                System.out.println("Debug: Server is running at MySQL" + rs.getString(1));
            }
        } catch (SQLException ex) {
            // If there is an error, print the error message
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    // This function will generate a random UIN
    public void Generate_Reg_Key (int amount) {
        StringBuilder query = new StringBuilder();
        String[] key_array = generate_random_key(amount);
        // Build up the query
        query = new StringBuilder("INSERT INTO register_key (reg_key) VALUES ");
        for (int j = 0; j < amount; j++) {
            query.append("('").append(key_array[j]).append("')");
            if (j != amount - 1) {
                query.append(",");
            }
        }
        // Execute the query
        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement()) {
            // Execute the query
            st.executeUpdate(query.toString());
        } catch (SQLException ex) {
            // If there is an error, print the error message
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    // Generate random key for register
    public String[] generate_random_key(int amount) {
        String[] key_array = new String[amount];
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int j = 0; j < amount; j++) {
            for (int i = 0; i < 9; i++) {
                int number = random.nextInt(2);
                long result = 0;
                switch (number) {
                    case 0:
                        result = Math.round(Math.random() * 25 + 65);
                        stringBuffer.append(String.valueOf((char) result));
                        break;
                    case 1:
                        stringBuffer.append(String.valueOf(new Random().nextInt(10)));
                        break;
                }
            }
            key_array[j] = stringBuffer.toString();
            stringBuffer.setLength(0);
        }
        return key_array;
    }

    // build up the query to insert a new course into the database
    public boolean addCourse(String Course_ID, int Credit, String Prereq, String Department, int Seats, int Seats_left) {
        String query = "INSERT INTO course VALUE ('" + Course_ID + "'," + Credit + ",'" + Prereq + "','" + Department + "'," + Seats + "," + Seats_left + ");";
        try (Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement()) {
            st.executeUpdate(query.toString());
            System.out.println("Debug: Course " + Course_ID + " added");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return false;
        }
        return true;
    }

    // This function is used for get table from server
    // Will return a 2d array with the result of the query
    // Really helpful for getting the table from server
    public Vector<Vector<String>> execute_gettable_query(String query, int column_amount) {
        Vector<Vector<String>> result = new Vector<Vector<String>>();
        try (Connection con = DriverManager.getConnection(url, user, password);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(query)) {
                while (rs.next()) {
                    Vector<String> row = new Vector<String>();
                    for (int i = 0; i < column_amount; i++) {
                        row.add(rs.getString(i + 1));
                    }
                    result.add(row);
                }
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        return result;
    }


    // This function is used for update table to server
    // Can do update,delete,insert,etc.
    public void update_query(String query) {
        try (Connection con = DriverManager.getConnection(url, user, password);
                Statement st = con.createStatement()) {
                st.executeUpdate(query);
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
    }


    // function to check register status
    public boolean check_reg_status () {
        String query = "SELECT boolean FROM boolean_table WHERE idboolean_table = 1;";
        return check_boolean(query);
    }

    // function to check drop status
    public boolean check_drop_status () {
        String query = "SELECT boolean FROM boolean_table WHERE idboolean_table = 2;";
        return check_boolean(query);
    }

    // if result is 1, return true, else return false
    private boolean check_boolean(String query) {
        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                if (rs.getInt(1) == 1) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return false;
    }

    // Function to force student/professor to register a course
    // Will check if the student/professor is registered in the course
    // If not, will register the student/professor in the course
    public void forceRegister(String uin_id, String c_id,int table_index) {
        String query = "INSERT INTO course_member (UIN,Course_ID)VALUE (" + uin_id + ",'" + c_id + "');";
        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement()) {
            if(!check_if_exist("SELECT * FROM course_member WHERE UIN = " + uin_id + " AND Course_ID = '" + c_id + "';")) {
                JOptionPane.showMessageDialog(null, "User already registered for this course",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (!check_if_exist("SELECT * FROM current_grade WHERE UIN = " + uin_id + " AND Course_ID = '" + c_id + "';")) {
                    st.executeUpdate("UPDATE current_grade SET Grade = 100 WHERE UIN = " + uin_id + " AND Course_ID = '" + c_id + "';");
                } else {
                    st.executeUpdate(query);
                    query = "UPDATE course SET Seats_left = Seats_left - 1 WHERE Course_ID = '" + c_id + "';";
                    st.executeUpdate(query);
                    if (table_index == 1) {
                        query = "INSERT INTO current_grade (UIN , Course_ID) VALUES (" + uin_id + ", '" + c_id + "');";
                        update_query(query);
                    }
                }
                JOptionPane.showMessageDialog(null,
                        "Forced register user " + uin_id + " to course " + c_id +
                                " successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Function for change user role
    // Will swap the role of the user
    // Build the query based on the role of the user and input then execute the query
    public boolean addProfessor(int UIN, String First_n, String Middle_n, String Last_n, String Gender, String Passwd, String type) {
        if (Objects.equals(type, "Professor")) {
            String query = "INSERT INTO professor (UIN, First_name, Middle_name, Last_name, Gender, Password)VALUE (" + UIN + ", '" + First_n + "', '" + Middle_n +
                    "', '" + Last_n + "', '" + Gender + "', '" + Passwd + "');";
            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement()) {
                st.executeUpdate(query);
                System.out.println("Debug: Professor " + UIN + " added");
                return true;
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (Objects.equals(type, "Student")) {
            String query = "INSERT INTO student (UIN, First_name, Middle_name, Last_name, Gender, Password)VALUE (" + UIN + ", '" + First_n + "', '" + Middle_n +
                    "', '" + Last_n + "', '" + Gender + "', '" + Passwd + "');";
            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement()) {
                st.executeUpdate(query);
                System.out.println("Debug: Student " + UIN + " added");
                return true;
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    // Function to check if there is a match result in the database
    // using the query input and execute the query
    // if there are result, return false, else return true
    public boolean check_if_exist (String query) {
        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return true;
    }
}
