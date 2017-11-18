import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManager {
   /*TESTING ---- DELETE COMMENT LATER
   // Instance for Singleton DP

    public static DBManager instance;

    // Private Constructor - to prevent instantiation from outside
    private DBManager(){}

    // Method to initial the class
    public static DBManager getInstance(){
        // Lazy Initialization - to initial the instance once
        if(instance == null) instance = new DBManager();
        return instance;
    }
    TESTING ---- DELETE COMMENT LATER*/

   // Here the DBManagerServer will receive the commands from the DBManagerClient and execute them
    // This way is done so the Client doesn't know what exactly is being done


    private static Socket socket;

    // Start of main test -- DELETE LATER
    public static void main(String [] args) {
        // Rest of code for DB Manager
        // TODO - Prepare Statement (need to separate in chunks depends which class? )
        // Should make different method depend on which prepare statement of which function the program should execute
        // TODO - JSON Parse - For chatting text from Chat class
        // TODO - JDBC - Connecting from Login/out class & Register class


        try {
            // Declare port
            int port = 8900;
            // Insert port
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started at port " + port);


            // Server is always running
            while (true) {
                // Setting up the server
                // Read data from client
                socket = serverSocket.accept();
                InputStream isObj = socket.getInputStream();
                // Read the socket
                InputStreamReader isrObj = new InputStreamReader(isObj);
                BufferedReader brObj = new BufferedReader(isrObj);

                // The Message from the Client
                // [ All the commands from the Client]
                String clientNumber = brObj.readLine();
                switchY(clientNumber);
                System.out.println("Message received from client is " + clientNumber);
                // Done setting


                /*
                // Multiply the number be 2 , and send back to the client
                String returnMessage;
                try {
                    int numberIntToFormat = Integer.parseInt(clientNumber);
                    int returnValue = numberIntToFormat * 2;
                    returnMessage = String.valueOf(returnValue) + "\n";

                } catch (NumberFormatException e) {
                    returnMessage = "Please enter a proper number\n";
                    e.printStackTrace();
                }
                */
                String returnMessage = "Done" + "\n";
                // Sending response back to client
                OutputStream osObj = socket.getOutputStream();
                OutputStreamWriter oswObj = new OutputStreamWriter(osObj);
                BufferedWriter bwObj = new BufferedWriter(oswObj);
                bwObj.write(returnMessage);
                System.out.println("Message send to the client is : The data transfer is Done\n");
                // Clean everything
                bwObj.flush();
            } // End of while loop
        }
        catch(Exception e){
                e.printStackTrace();
            }
        finally{
                try {
                    // Server socket closed
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        //}

    } // End of test main

    // JDBC Connect Method - Start
    private static void jdbcConnection() {


        // TODO - Make a Database user [bad practice to use root]
        String dbName = "chitchattime";
        String user = "root";
        String password = "";
        int port = 3306;
        String url = "jdbc:mysql://127.0.0.1:" + port + "/" + dbName;

        // This method will only be execute from DBManager (will be clear later on)


        try {
            Connection db = DriverManager.getConnection(url, user, password);

            // SQL Prepare statement -- Testing connection JDBC
            // TODO - Array of Prepare statement
            PreparedStatement stmt;
            // YEY WORKEDDD :DD
            stmt = db.prepareStatement("INSERT INTO users (name,password,age) VALUES ('Nai','22',33)");
            stmt.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    } // End of JDBC

    // Testing switch with method execute
    // Testing pass ! :D
    // Maybe to do a class for this , so it could be change on at live without dropping the server ?
    // TODO - Change to a proper name once it's more developed
    private static void switchY(String switchy){

        switch(switchy.toLowerCase()){
            case "connect":
                jdbcConnection();
                break;

        }
    }
}
