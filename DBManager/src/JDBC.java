//import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.UUID;

public class JDBC {

    // - Singleton Start -
    private static JDBC instance;

    // Private Constructor - to prevent instantiation from outside
    private JDBC() {}

    // Method to initial the class
    static JDBC getInstance() {
        // Lazy Initialization - to initial the instance once
        if (instance == null) instance = new JDBC();
        return instance;
    }
    // - Singleton End -

    // - JDBC Start -
    // TODO - Change to a class (?)

    // TODO - Make a Database user [bad practice to use root]
    static String dbName = "chitchattime";
    static String user = "root";
    static String dbPassword = "";
    static int port = 3306;
    static String url = "jdbc:mysql://127.0.0.1:" + port + "/" + dbName;

    // -- Registration method Start --
     String jdbcRegister(String username, String password, int age) {

        try {
            Connection db = DriverManager.getConnection(url, user, dbPassword);
            // SQL Prepare statement
            // TODO - Array of Prepare statement
            PreparedStatement stmt;

            String chkUsername = "SELECT name FROM users;";
            stmt = db.prepareStatement(chkUsername);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()){
                System.out.println(resultSet.getString( 1));

                // Checks if the user is already registered in the database
                // if yes , will send message to user about it
                // if no , will continue with the registration
                if(resultSet.getString(1).equalsIgnoreCase(username)){
                    System.out.println("This username is taken , please try a new one");
                    return "This username is taken , please try a new one";

                } else {

                    // Prepare Statement for register an user in the table 'users'
                    String prestmt = "INSERT INTO users (name, password, age) VALUES (?, ?, ?)";
                    stmt = db.prepareStatement(prestmt);


                    // Insert user info
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    stmt.setInt(3, age);
                    stmt.executeUpdate();

                    System.out.println("Successfully registered \nWelcome to Chit Chat !");
                    return "Successfully registered \nWelcome to Chit Chat !";
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "Nothing happened :(";
    }
    // -- Registration method End --

    // -- Login method Start --
    String jdbcLogin(String username, String password) {

        try {
            Connection db = DriverManager.getConnection(url, user, dbPassword);
            // SQL Prepare statement

            PreparedStatement stmt;
            // Preparing for check username and password
            String chkUsername = "SELECT * FROM users;";
            stmt = db.prepareStatement(chkUsername);
            ResultSet resultSet = stmt.executeQuery();

            // Index the name and password column
            int nameIndex   = resultSet.findColumn("name");
            int passwordIndex   = resultSet.findColumn("password");

            // Check of username and password
            while (resultSet.next()){

                if(resultSet.getString(nameIndex).equalsIgnoreCase(username) && resultSet.getString(passwordIndex).equals(password)){
                    return "Welcome " + username;
                } else {
                    return "Wrong username or password , try again :(";
                }

            }



        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "Didn't work";
    }
    // -- Registration method End --

    // The encryption for token
    // Use Hashing with MD5
    // TODO - Add UUID check
    private String tokenToDo(String toToken){


            MessageDigest md = MessageDigest.getInstance("MD5");



        return "";
    }

    /* -- Mini Dummy to test registration --
    public static void jdbcRegister(String userInfo, int counter) {

        try {
            Connection db = DriverManager.getConnection(url, user, dbPassword);
            // SQL Prepare statement

            PreparedStatement stmt;

            stmt = db.prepareStatement("INSERT INTO users (name,password,age) VALUES (?, ?, ?)");
            stmt.setString(counter, userInfo);
            stmt.executeUpdate();
            System.out.println("Successfully registered \n Welcome to Chit Chat !");
            //stmt = db.prepareStatement("INSERT INTO users (name,password,age) VALUES ('Nai','22',33)");


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    *///  -- End of Mini Dummy --

}
