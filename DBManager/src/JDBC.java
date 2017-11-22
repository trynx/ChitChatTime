//import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.UUID;


class JDBC {

    /**
     * Singleton Start
     */
    private static JDBC instance;

    // Private Constructor - to prevent instantiation from outside
    private JDBC() {}

    // Method to initial the class
    static JDBC getInstance() {
        // Lazy Initialization - to initial the instance once
        if (instance == null) instance = new JDBC();
        return instance;
    }
    /**
     * Singleton End
     */

    RandomString randomString = new RandomString(); // Test
    Session session = new Session();

    // TODO - Change to a class (?)

    // TODO - Make a Database user [bad practice to use root]
    private static String dbName = "chitchattime";
    private static String user = "root";
    private static String dbPassword = "";
    private static int port = 3306;
    private static String url = "jdbc:mysql://127.0.0.1:" + port + "/" + dbName;

    /**
     * Registration Start
     */
    String jdbcRegister(String username, String password, int age) {

        try {
            Connection db = DriverManager.getConnection(url, user, dbPassword);
            // SQL Prepare statement
            PreparedStatement stmt;

            String chkUsername = "SELECT name FROM users;";
            stmt = db.prepareStatement(chkUsername);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()){
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
                    return "Successfully registered. Welcome to Chit Chat !";
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "Nothing happened :(";
    }
    /**
     * Registration End
     */

    /**
     * Login Start
     */
    String jdbcLogin(String username, String password) {

        try {
            Connection db = DriverManager.getConnection(url, user, dbPassword);
            // SQL Prepare statement

            PreparedStatement stmt;
            // Preparing to verify username and password
            String chkUsername = "SELECT * FROM users;";
            stmt = db.prepareStatement(chkUsername);
            ResultSet resultSet = stmt.executeQuery();


            // Verification of username and password
            for(resultSet.first();!resultSet.isAfterLast();resultSet.next()){

                if(resultSet.getString("name").equalsIgnoreCase(username) && resultSet.getString("password").equals(password)){
                    session.createSession(username);
                    String userSession = session.getCurrentSession();
                    session.encodeSession(userSession);
                    String userEncodedS = session.getEncodedSession();
                    System.out.println("userEncodedS : " + userEncodedS);
                    System.out.println("userSession : " + userSession);
                    return "Welcome " + userEncodedS ; // TODO - Find a way to send to the user without the client actually see it

                }

            }
            return "Wrong username or password";


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "Login didn't work";
    }
    /**
     * Login End
     */



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
