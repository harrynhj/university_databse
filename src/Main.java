// Classname: Main
// Description: This is where everything starts.
// Author: Haoji Ni

// Please Use open jdk 1.8.0_231 to compile this program.
// Need mysql-connector-java-8.0.29 as a dependency.
// Please use windows system to run this program
// Mac system could cause little GUI issues

// If you want run this program locally, you need change the url, user, password to your own database.
// Change line 11-13 in Query.java to your own url, user, password.
// Change line 6-8 in Authentication.java to your own url, user, password.


// admin uin and password is 1337 and 1337

public class Main {
    public static String url;
    public static String user;
    public static String password;


    public static void main(String[] args) {
        // Create a GUI
        LoginUI gui = new LoginUI();
        SQLUI sqlui = new SQLUI();
        // Try to connect to the database
    }
}


