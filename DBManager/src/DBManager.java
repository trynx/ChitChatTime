

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

    private static Register reg = Register.getInstance();
    private static LogInOut login = LogInOut.getInstance();
    private static JDBC jdbc = JDBC.getInstance();
    private static Socket clientSocket;
    private static ServerSocket serverSocket;
    private static final int maxClientNum = 5;
    private static final ServerThread[] thread = new ServerThread[maxClientNum];
    private static boolean onReg = false;
    private static boolean onLog = false;

    /**
     * Start main
     */
    public static void main(String[] args) {
        // Rest of code for DB Manager

        // TODO - JSON Parse - For chatting text from Chat class

        try {
            // Declare port
            int port = 8900;
            // Insert port
            serverSocket = new ServerSocket(port);
            System.out.println("Server started at port " + port);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server Socket");
        }

        // Server is always running
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i;
                for (i = 0; i < maxClientNum; i++) {
                    if (thread[i] == null) {
                        System.out.println("Bingo");
                        (thread[i] = new ServerThread(clientSocket, thread)).start();
                        break;
                    }
                }
                if (i == maxClientNum) {
                    PrintStream osObj = new PrintStream(clientSocket.getOutputStream());
                    osObj.println("Server to busy. Get off here ~!");
                    osObj.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("Failed Server Start");
                System.exit(-1);
            }

            /**
             * End main
             */
        }
    }
}
