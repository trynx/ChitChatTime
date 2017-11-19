import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public static void jdbcRegister(String username, String password, int age) {

        try {
            Connection db = DriverManager.getConnection(url, user, dbPassword);
            // SQL Prepare statement
            // TODO - Array of Prepare statement
            PreparedStatement stmt;

            stmt = db.prepareStatement("INSERT INTO users (name, password, age) VALUES (?, ?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, age);
            stmt.executeUpdate();
            System.out.println("Successfully registered \n Welcome to Chit Chat !");
            //stmt = db.prepareStatement("INSERT INTO users (name,password,age) VALUES ('Nai','22',33)");


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void jdbcRegister(String userInfo, int counter) {

        try {
            Connection db = DriverManager.getConnection(url, user, dbPassword);
            // SQL Prepare statement
            // TODO - Array of Prepare statement
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
    // - JDBC End -
}
