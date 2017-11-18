import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/* public class DBManagerBU {
   /* TESTING ---- DELETE COMMENT LATER
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
    TESTING ---- DELETE COMMENT LATER //*


    private static Socket socket;
   // Start of main test -- DELETE LATER
   public static void main(String [] args) {
       // Rest of code for DB Manager
       // TODO - Prepare Statement (need to separate in chunks depends which class? )
       // Should make different method depend on which prepare statement of which function the program should execute
       // TODO - JSON Parse - For chatting text from Chat class
       // TODO - JDBC - Connecting from Login/out class & Register class


       // Input and Output stream also here , connect to the server
       try{
           // From where to connect
           String host = "localhost";
           // Declare port
           int port = 8900;
           InetAddress address = InetAddress.getByName(host);
           // Client must know from where to connect and which port
           socket = new Socket(address, port);

           /*Insert port
            *ServerSocket serverSocket = new ServerSocket(port);
            *System.out.println("Server started at port " + port);
           //*

           // Client send message to the server
           OutputStream osObj = socket.getOutputStream();
           OutputStreamWriter oswObj = new OutputStreamWriter(osObj);
           BufferedWriter bwObj = new BufferedWriter(oswObj);
           // Methods of classes
           // Write everything that is need , it will send to the DBManager
           // Then the DBManager return the stream from the server
           // TODO - Add scanner to farther testing
           String  messageSend = "connect";

           // Message send
           String sendMsg = messageSend + "\n";
           bwObj.write(sendMsg);
           bwObj.flush();
           System.out.println("Message sent to the server " + sendMsg);

           // Get the return message from server
           InputStream isObj = socket.getInputStream();
           InputStreamReader isrObj = new InputStreamReader(isObj);
           BufferedReader brObj = new BufferedReader(isrObj);
           String clientNumber = brObj.readLine();
           System.out.println("Message received from server " + clientNumber);

       }
       catch (Exception e){
           e.printStackTrace();
       }
       finally {
           try {
               // Server socket closed
               socket.close();
           }
           catch (Exception e){
               e.printStackTrace();
           }
       }

       //}

   } // End of test main

}
*/