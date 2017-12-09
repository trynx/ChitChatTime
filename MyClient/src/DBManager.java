

import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            JSONObject jServerReceive;

           // Read server stream all the time
            while ((responseLine = brObj.readLine()) != null) {
                jServerReceive = new JSONObject(responseLine);


                if (jServerReceive.toString().contains("login")){

                    // Split the session at ":" , so the userName and token can be extract from the JSON receive from server
                    String [] sessionArray = jServerReceive.getString("login").split(":");  // [0] = userName , [1] = token
                    String userName = sessionArray[0];
                    String token = sessionArray[1];

                    login.setSessionUser(token);
                    System.out.println("Welcome " + userName + "\nTo logout the chat type /quit");

                    logged = true;

                } else if (jServerReceive.toString().contains("register")){
                    System.out.println("The registration of " + jServerReceive.getString("register") + " is completed \nPlease login");
                }


                if(responseLine.startsWith("/quit")) break;
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server con err");
        } catch (JSONException e){
            e.printStackTrace();
            System.out.println("JSON error");
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





    // Register information
    void registerServer(String username, String password, int age){
        this.serverConnect("register:"+ username + ":"+ password + ":"+ Integer.toString(age));
    }

    // Login information
    void loginServer(String username, String password){
        // Create the JSON
        JSONObject jSend ;
        try {
            JSONObject user = new JSONObject().put("username", username).put("password", password);
            jSend = new JSONObject().put("login",user);
            // Send to server
            this.serverConnect(jSend.toString());

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    // Chat
    void chatServer(String userInput){
        this.serverConnect(userInput);
    }

    void logout(String userInput){
        this.serverConnect(userInput);
    }

}


