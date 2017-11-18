
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class MyServer {

    private static Socket socket;

    public static void main(String [] args){

        try{
            // Declare port
            int port = 8900;
            // Insert port
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started at port " + port);


            // Server is always running
            while(true){
                // Setting up the server
                // Read data from client
                socket = serverSocket.accept();
                InputStream isObj = socket.getInputStream();
                // Read the socket
                InputStreamReader isrObj = new InputStreamReader(isObj);
                BufferedReader brObj = new BufferedReader(isrObj);

                // The Message from the Client
                String clientNumber = brObj.readLine();
                System.out.println("Message received from client is " + clientNumber);
                // Done setting

                // Multiply the number be 2 , and send back to the client
                String returnMessage = clientNumber + "\n"; // WTF ... need to do \n every time..
                /*try{
                    int numberIntToFormat = Integer.parseInt(clientNumber);
                    int returnValue = numberIntToFormat * 2;
                    returnMessage = String.valueOf(returnValue) + "\n";

                }
                catch (NumberFormatException e){
                    returnMessage = "Please enter a proper number\n";
                    e.printStackTrace();
                }*/

                // Sending response back to client
                OutputStream osObj = socket.getOutputStream();
                OutputStreamWriter oswObj = new OutputStreamWriter(osObj);
                BufferedWriter bwObj = new BufferedWriter(oswObj);
                bwObj.write(returnMessage);
                System.out.println("Message send to the client is " + returnMessage);
                // Clean everything
                bwObj.flush();
            } // End of while loop



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
