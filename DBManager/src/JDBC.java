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
    private Session session = new Session();


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
                System.out.println(resultSet.getString( "name"));

                // Checks if the user is already registered in the database
                // if yes , will send message to user about it
                // if no , will continue with the registration
                if(resultSet.getString("name").equalsIgnoreCase(username)){
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
    String jdbcLogin(String username, String password,boolean isHere) {

        if(!isHere) {
            try {
                Connection db = DriverManager.getConnection(url, user, dbPassword);
                // SQL Prepare statement

                PreparedStatement stmt;
                // Preparing to verify username and password
                String chkUsername = "SELECT * FROM users;";
                stmt = db.prepareStatement(chkUsername);
                ResultSet resultSet = stmt.executeQuery();


                // Verification of username and password
                for (resultSet.first(); !resultSet.isAfterLast(); resultSet.next()) {

                    if (resultSet.getString("name").equalsIgnoreCase(username) && resultSet.getString("password").equals(password)) {
                        session.createSession(username);
                        String userSession = session.getCurrentSession();

                        jdbcSessions(userSession,isHere);

                        session.encodeSession(userSession);
                        String userEncodedS = session.getEncodedSession();
                        // Checks , delete later
                        System.out.println("userEncodedS : " + userEncodedS);
                        System.out.println("userSession : " + userSession);

                        return username + ":" + userEncodedS; // TODO - Find a way to send to the user without the client actually see it

                    }

                }
                return "Wrong username or password";


            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if(isHere){
            return "User : " + username + " is already logged in";
        }
        return "Login didn't work";
    }
    /**
     * Login End
     */


    /**
     * Session Start
     */
    private void jdbcSessions(String session, boolean isHere) {

        // Split the session at ":" , so the userName and token can be extract
        String[] sessionArray = session.split(":");  // [0] = userName , [1] = token
        String userName = sessionArray[0];
        String token = sessionArray[1];


        try {
            Connection db = DriverManager.getConnection(url, user, dbPassword);
            // SQL Prepare statement

            PreparedStatement stmt;
            // Does a table where the userName show its ID
            String sessionTable = "SELECT id,name FROM users WHERE name=(?)";
            stmt = db.prepareStatement(sessionTable);
            stmt.setString(1, userName);
            ResultSet resultSet = stmt.executeQuery();
            // Extract ID from the table where the user name is
            if (resultSet.next()) {
                int userId = resultSet.getInt("id");

                // When the user_id doesn't exist in Sessions table , will insert it to Sessions
                if (!isHere) {
                    // Add the user_id & token to the sessions table
                    // TODO - Check how to get owns address (realllyy at the end of the project :P )
                    String addSession = "INSERT INTO sessions (user_id, token, remote_addr) VALUE (?,?,'TBA')";
                    stmt = db.prepareStatement(addSession);
                    stmt.setInt(1, userId);
                    stmt.setString(2, token);
                    stmt.executeUpdate();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
        /**
         * Session Start
         */

        /**
         * CheckUserLogged Start
         */
        // Method will do a check on session table if the user already logged
        // If already logged , will return true , if not will return false
        boolean chkUserLogged(String userName){

            try {
                Connection db = DriverManager.getConnection(url, user, dbPassword);
                // SQL Prepare statement

                PreparedStatement stmt;
                // Does a table where the userName show its ID
                String sessionTable = "SELECT id,name FROM users WHERE name=(?)";
                stmt = db.prepareStatement(sessionTable);
                stmt.setString(1, userName);

                ResultSet resultSet = stmt.executeQuery();

                if (resultSet.next()) {
                    // Extract ID from the table where the user name is
                    int userId = resultSet.getInt("id");

                    // Preparing to verify user_id
                    String chkUsername = "SELECT * FROM sessions;";
                    stmt = db.prepareStatement(chkUsername);
                    ResultSet resultSetSession = stmt.executeQuery();


                    if (resultSetSession.next()) {

                        // Verification of username and password
                        for (resultSetSession.first(); !resultSetSession.isAfterLast(); resultSetSession.next()) {

                            // When the loop finds an user_id that is compatible with the user_id in Sessions table
                            if (resultSetSession.getInt("user_id") == userId) {
                                // Means the user is already logged in
                                return true;
                            }
                        }
                    }

                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            // Not logged in
            return false;
    }
    /**
     * CheckUserLogged Start
     */

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


