import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
/*
public class MyClientBU {

    // Connection between Server and client
    private static Socket socket;

    public static void main(String [] args){

        try{
            // From where to connect
            String host = "localhost";
            // Declare port
            int port = 8900;
            InetAddress address = InetAddress.getByName(host);
            // Client must know from where to connect and which port
            socket = new Socket(address, port);

            /** Insert port
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
            String number = "2";

            // Message send
            String sendMsg = number + "\n";
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


    }
}
*/