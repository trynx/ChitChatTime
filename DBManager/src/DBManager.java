import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


// TODO - Method to generate session & add to DB (only does when login)
// TODO - Method for chat , gets the name whom the client wants to chat, verify the receiver name with the session
public class DBManager {

    // Here the DBManagerServer will receive the commands from the DBManagerClient and execute them
    // This way is done so the Client doesn't know what exactly is being done

    private static  Register reg = Register.getInstance();
    private static  LogInOut login = LogInOut.getInstance();
    private static  JDBC jdbc = JDBC.getInstance();
    private static Socket socket;
    private static ServerSocket serverSocket;
    private static boolean onReg = false;
    private static boolean onLog = false;

    /**
     * Start main
     */
    public static void main(String [] args) {
        // Rest of code for DB Manager

        // TODO - JSON Parse - For chatting text from Chat class

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
                // [All the commands from the Client]
                String clientNumber = brObj.readLine();


                System.out.println("Message received from client is " + clientNumber);
                // Done setting

                // Response from the method switchY , which return what was the action taken
                String returnMessage = switchY(clientNumber) + "\n";

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
    }
    /**
     * End main
     */




    // Maybe to do a class for this , so it could be change onReg at live without dropping the server ?
    // TODO - Change to a proper name once it's more developed

    private static int counterReg = 0; // Counter for Registration switch
    private static int counterLog = 0; // Counter for Registration switch
    private static boolean loggedIn = false;
    private static String switchY(String switchy){

        // - Chat Phase -
        if (false) {

        }

        // - Login Phase -
        else if(switchy.equalsIgnoreCase("login") || onLog){
            if (onLog){
                switch(counterLog) {
                    case 0:
                        login.setUsername(switchy);
                        counterLog++;
                        break;
                    case 1:
                        login.setPassword(switchy);
                        counterLog = 0;
                        onLog = false;
                        loggedIn = true;
                        return jdbc.jdbcLogin(login.getUsername(),login.getPassword(),jdbc.chkUserLogged(login.getUsername()));
                }
            }
            onLog = true;
            return "In login";
        }

        // - Registration Phase -
        // Gets each information from the client in order User , Password and Age
        // Then add it depend onReg the counterReg
        else if(switchy.equalsIgnoreCase("register") || onReg) {

            // Start the loop for the registration
            if (onReg){
                switch(counterReg) {
                    case 0:
                        reg.setUsername(switchy);
                        counterReg++;
                        break;
                    case 1:
                        reg.setPassword(switchy);
                        counterReg++;
                        break;
                    case 2:
                        reg.setAge(Integer.parseInt(switchy));
                        onReg = false;
                        counterReg = 0;
                        return  jdbc.jdbcRegister(reg.getUsername(),reg.getPassword(),reg.getAge());
                }
            }
            onReg = true;

        }

        return "Not yet";

    }
}
