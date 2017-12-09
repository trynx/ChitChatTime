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

    private static String dbName = "ibmx_2c65d51c6a8f71f";
    private static String user = "b6ba4ce7856911";
    private static String dbPassword = "32e2b73a";
    private static int port ;
    private static String hostName = "eu-cdbr-sl-lhr-01.cleardb.net";
    private static String url = "jdbc:mysql://" + hostName +"/" + dbName;

    /**
     * Registration Start
     */
    String jdbcRegister(String username, String password, int age) {

        try {
            System.out.println("Registering");
            Connection db = DriverManager.getConnection(url, user, dbPassword);
            // SQL Prepare statement
            PreparedStatement stmt;

            String chkUsername = "SELECT name FROM users;";
            stmt = db.prepareStatement(chkUsername);
            ResultSet resultSet = stmt.executeQuery();

            // Checks if the user is already registered in the database
            // if yes , will send message to user about it
            // if no , will continue with the registration
            for(resultSet.first(); !resultSet.isAfterLast() && resultSet.wasNull() ; resultSet.next()){
                System.out.println(resultSet.getString( "name")); // Check
                if(resultSet.getString("name").equals(username))

                    return "User name already taken , try another user name" ;
            }

                // Prepare Statement for register an user in the table 'users'
                String prestmt = "INSERT INTO users (name, password, age) VALUES (?, ?, ?)";
                stmt = db.prepareStatement(prestmt);
                // Insert user info
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setInt(3, age);
                stmt.executeUpdate();
                System.out.println("Successfully registered.");
                 db.close();
                return "Successfully registered. Please login";

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

        if(isHere) {
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
                        /*System.out.println("userEncodedS : " + userEncodedS);
                        System.out.println("userSession : " + userSession);*/

                        db.close();
                        return username + ":" + userEncodedS; // TODO - Find a way to send to the user without the client actually see it

                    }

                }
                db.close();
                return "Wrong username or password";


            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            return "User is already logged in";
        }
        return "Login didn't work";
    }
    /**
     * Login End
     */


    /**
     * Session Start
     */
    private void jdbcSessions(String session, boolean notHere) {

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
                if (notHere) {
                    // Add the user_id & token to the sessions table
                    // TODO - Check how to get owns address (realllyy at the end of the project :P )
                    String addSession = "INSERT INTO sessions (user_id, token, remote_addr) VALUE (?,?,'TBA')";
                    stmt = db.prepareStatement(addSession);
                    stmt.setInt(1, userId);
                    stmt.setString(2, token);
                    stmt.executeUpdate();
                    db.close();
                }
            }
            db.close();
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
                            if (resultSetSession.getInt("user_id")== userId) {
                                // Means the user is already logged in
                                db.close();
                                return false;
                            }
                        }
                    }

                }
                db.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            // Not logged in
            return true;
    }
    /**
     * CheckUserLogged Start
     */


    /**
     * Logout Start
     */
    void jdbcLogout(String username){

        // Takes the user name which will logout and delete the session
        // It's used to know that the user is no longer logged

        try {
            Connection db = DriverManager.getConnection(url, user, dbPassword);
            // SQL Prepare statement

            PreparedStatement stmt;
            // Take the id from the right username which will logout
            String logOutTable = "SELECT id,name FROM users WHERE name = ?";
            stmt = db.prepareStatement(logOutTable);
            stmt.setString(1,username);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                // Get the id from the selected user
                int userId = resultSet.getInt("id");
                //  Then delete it from the session table
                logOutTable = "DELETE FROM sessions WHERE user_id = ?";
                stmt = db.prepareStatement(logOutTable);
                stmt.setInt(1,userId);
                stmt.executeUpdate();

                db.close();
                System.out.println(username + " disconnected");

            }
            db.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    /**
     * Logout End
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


