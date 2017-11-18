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

    // - Singleton Start -
    private static DBManager instance;

    // Private Constructor - to prevent instantiation from outside
    private DBManager() {}

    // Method to initial the class
    static DBManager getInstance() {
        // Lazy Initialization - to initial the instance once
        if (instance == null) instance = new DBManager();
        return instance;
    }
    // - Singleton End -


    // - Connection to Server Start -
    private  Socket socket;

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
            System.out.println("Message received from server " + clientNumber);

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

    // - Connection to Server End -


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
                MyClientReal.connected = false;
                break;
            default:
                System.out.println("Please try again \nType register or login");

        }
    }
    // - Client Entry Response End -

    // Register information
    void registerServer(String userInfo){
        this.serverConnect("register");
        this.serverConnect(userInfo);

    }
    // Depend on which execution the Client does , it will send to the server
    void toServer(String execution){
        switch (execution.toLowerCase()){
            case "register":
               this.serverConnect("register"); // Need to add additional information

                break;
            case "login":
                this.serverConnect("login"); // Which user ?

                break;

        }
    }
}


