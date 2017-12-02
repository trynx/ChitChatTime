

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


/* - Explanation -
 * The DBManager would be the interpreter between the Client and the Server
 * all the operations will go through this class
 */
class DBManager {

    private boolean logged = false; // Test for when logged in
    Login login = Login.getInstance();


    private static PrintStream osObj;
    private static BufferedReader brObj;
    private Socket clientSocket;
    private static String responseServer;

    /**
     * Singleton Start
     */
    private static DBManager instance;

    // Private Constructor - to prevent instantiation from outside
    private DBManager() {
    }

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

    void connectMe(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            osObj = new PrintStream(clientSocket.getOutputStream());
            brObj = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String responseLine;
            while ((responseLine = brObj.readLine()) != null) {
                responseServer = responseLine;

                if (responseLine.contains("/quit"))
                    break;
            }
            logged = true;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server con err");
        }
    }

    // Input and Output stream also here , connect to the server
    private void serverConnect(String userInputScanner) { // Testing later return to private
        try {
            osObj = new PrintStream(clientSocket.getOutputStream());
            brObj = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            osObj.println(userInputScanner.trim());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server con err");
        }


            // TODO - Maybe do multiple option with switch , as later on it will come with JSON and would need to "clean" that
            // Switch - Logged , Chat , Registered
         /*   if(responseServer.startsWith(login.getUserName())){
                // Split the session at ":" , so the userName and token can be extract
                String [] sessionArray = responseServer.split(":");  // [0] = userName , [1] = token
                String userName = sessionArray[0];
                String token = sessionArray[1];

               login.setSessionUser(token);
               System.out.println("Welcome " + userName);
            } else {
                System.out.println(responseServer);
            } */
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
        this.serverConnect("register:"+ username + ":"+ password + ":"+ Integer.toString(age));
    }

    // Login information
    void loginServer(String username, String password){
        this.serverConnect("login:"+ username + ":" + password);
    }

}


