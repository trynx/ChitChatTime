import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/* - Explanation -
 * The DBManager would be the interpreter between the Client and the Server
 * all the operations will go through this class
 */
class DBManager {

    /**
     * Singleton Start
     */
    private static DBManager instance;

    // Private Constructor - to prevent instantiation from outside
    private DBManager() {}

    // Method to initial the class
    static DBManager getInstance() {
        // Lazy Initialization - to initial the instance once
        if (instance == null) instance = new DBManager();
        return instance;
    }
    /**
     * Singleton End
     */


    /**
     * Connection to server Start
     */
    private  Socket socket;
    private boolean logged; // Test for when logged in
    Login login = Login.getInstance();
    // Rest of code for DB Manager


    // Input and Output stream also here , connect to the server
    private void serverConnect(String userInputScanner) {
        try
        {
            // From where to connect
            String host = "localhost";
            // Declare port
            int port = 8900;
            InetAddress address = InetAddress.getByName(host);
            // Client must know from where to connect and which port
            socket = new Socket(address, port);

            /* Insert port
             *ServerSocket serverSocket = new ServerSocket(port);
             *System.out.println("Server started at port " + port);
             */

            // Client send message to the server
            OutputStream osObj = socket.getOutputStream();
            OutputStreamWriter oswObj = new OutputStreamWriter(osObj);
            BufferedWriter bwObj = new BufferedWriter(oswObj);

            // Methods of classes
            // Write everything that is need , it will send to the DBManager
            // Then the DBManager return the stream from the server
            String messageSend = userInputScanner;

            // Message send
            String sendMsg = messageSend + "\n";
            bwObj.write(sendMsg);
            bwObj.flush();
            System.out.print("Message sent to the server " + sendMsg);

            // Get the return message from server
            InputStream isObj = socket.getInputStream();
            InputStreamReader isrObj = new InputStreamReader(isObj);
            BufferedReader brObj = new BufferedReader(isrObj);
            String clientNumber = brObj.readLine();

            // TODO - Maybe do multiple option with switch , as later on it will come with JSON and would need to "clean" that
            // Switch - Logged , Chat , Registered
            // TODO - ** Continue here **
            if(clientNumber.startsWith(login.getUserName())){
                // Split the session at ":" , so the userName and token can be extract
                String [] sessionArray = clientNumber.split(":");  // [0] = userName , [1] = token
                String userName = sessionArray[0];
                String token = sessionArray[1];

               login.setSessionUser(token);

               System.out.println("Welcome " + userName);
            } else {
                System.out.println("Message received from server " + clientNumber);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Server socket closed
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Connection to server End
     */


    // - Client Entry Response Start -
    void clientInput(String clientInputMsg){
        // Check which input , and execute that input
        // Register , Login or Check
        switch (clientInputMsg.toLowerCase()){
            case "register":

                break;
            case "login":
                // Login class
                System.out.println("login\n");
                this.serverConnect("login");
                break;
            case "check":
                System.out.println("Connecting to the server ... ");
                this.serverConnect("check");
                break;
            case "logout":
                System.out.println("Disconnect from chat.\n");
                this.serverConnect("logout");
                break;
            default:
                System.out.println("Please try again \nType register or login");

        }
    }
    // - Client Entry Response End -

    // Register information
    void registerServer(String username, String password, int age){
        this.serverConnect("register");
        this.serverConnect(username);
        this.serverConnect(password);
        this.serverConnect(Integer.toString(age));

    }

    // Login information
    void loginServer(String username, String password){
        this.serverConnect("login");
        this.serverConnect(username);
        this.serverConnect(password);
    }
}


