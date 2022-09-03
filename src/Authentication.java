// Classname: Authentication
// Description: This class is used to authenticate any login request.
// Author: Haoji Ni


import java.sql.*;

// This Class is made for login and register function
public class Authentication {
    static String url = "please replace this line to your own server url";
    static String user = "replace this line";
    static String password = "replace this password";



    // This function takes register key and generate a random UIN
    // If the register key invalid, return 1, invalid passwd return 2
    // Otherwise, return UIN

    public int Register_User(String reg_key, String First_name, String Middle_name,
                             String Last_name, String Passwd, String Confirm, String Gender)
    {
        String query = "";

        // check if confirm password is correct
        if (!check_confirm_password(Passwd, Confirm)) {
            return 2;
        }
        // Check if password meets the requirements
        if (!password_Check(Passwd)) {
            return 3;
        }
        // Check if the register key is valid
        query = "SELECT * FROM register_key WHERE reg_key ='" + reg_key + "' AND Used_time = '1983-01-01 00:00:01'";
        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            // if register key could not match any record in the database, return 1
            if (!rs.next()) {
                return 1;
            } else {
                // If the register key is valid, update the used time
                query = "UPDATE register_key SET Used_time = NOW() WHERE reg_key ='" + reg_key + "'";
                st.executeUpdate(query);
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        int flag = 0;
        int UIN = 0;
        // Generate a random UIN
        while (flag == 0) {
            UIN = getRandomNumber(100000000, 999999999);
            query = "SELECT * FROM student WHERE UIN = " + UIN + ";";
            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(query)) {
                // If the UIN is already in the database, continue to generate a new UIN
                if (!rs.next())
                    flag = 1;
            } catch (SQLException ex) {
                // If there is an error, print the error message
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }

        // Insert the new user into the database
        query = "INSERT INTO student (UIN, First_name, Middle_name, Last_name, Gender, Password)VALUE (" + UIN + ", '" + First_name + "', '" + Middle_name +
                "', '" + Last_name + "', '" + Gender + "', '" + Passwd + "');";
        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement()) {
            // Execute the query
            st.executeUpdate(query);
        } catch (SQLException ex) {
            // If there is an error, print the error message
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return UIN;
    }

    // Check if the password is strong enough
    // This will require user's password to be at least 8 characters long,
    // and contain at least one number, one uppercase letter, and one lowercase letter
    public boolean password_Check(String Password)
    {
        // Check if the password meets the requirements
        boolean flag = false;
        int length = Password.length();
        int digit = 0;
        int upper = 0;
        int lower = 0;
        int special = 0;
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(Password.charAt(i))) {
                digit = 1;
            } else if (Character.isUpperCase(Password.charAt(i))) {
                upper = 1;
            } else if (Character.isLowerCase(Password.charAt(i))) {
                lower = 1;
            } else {
                special = 1;
            }
        }
        if (digit == 1 && upper == 1 && lower == 1 && special == 1 && length > 7) {
            flag = true;
        }
        return flag;
    }

    // Login function, used to check if the UIN is existed and the password is match
    public boolean login (int UIN, String Password, String role) {
        String query = "";
        switch (role) {
            case "Student":
                query = "SELECT * FROM student WHERE UIN = " + UIN + " AND password = '" + Password + "';";
                break;
            case "Professor":
                query = "SELECT * FROM professor WHERE UIN = " + UIN + " AND password = '" + Password + "';";
                break;
            case "Admin":
                query = "SELECT * FROM admin WHERE Admin_key = " + UIN + " AND password = '" + Password + "';";
                break;
        }
        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            // return true if the user is existed and the password is match
            return rs.next();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return false;
    }

    // This function will generate a random number between min and max
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    // This function will check if the confirm password is correct
    public boolean check_confirm_password(String Password, String Confirm_Password)
    {
        return Password.equals(Confirm_Password);
    }

    // Function to generate random UIN
    public int generateUIN() {
        String query;
        int flag = 0;
        int UIN = 0;
        // Generate a random UIN
        while (flag == 0) {
            UIN = getRandomNumber(100000000, 999999999);
            query = "SELECT * FROM student WHERE UIN = " + UIN + ";";
            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(query)) {
                // If the UIN is already in the database, continue to generate a new UIN
                if (!rs.next())
                    flag = 1;
            } catch (SQLException ex) {
                // If there is an error, print the error message
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }
        return UIN;
    }
}
