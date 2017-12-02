
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

// This is the client , which will do all the operations and chatting
public class MyClientReal implements Runnable  {

    private static Socket clientSocket = null;
    private static PrintStream osObj = null;
    private static BufferedReader brObj = null;
    private static boolean logged = false;
    private static DBManager dbManagerThread;
    // TODO - Session and keep it saved in the client
    // TODO - Make a method that each outpuststream have the seassion + username together for verification in the server
    // TODO - Method for chat , once a user is added , it will saved it until the client decide to end the chat
    public static void main(String [] args){

        DBManager dbManager = DBManager.getInstance();
        Scanner userInputScn = new Scanner(System.in);
        Login login = Login.getInstance();

        // port
        int portNumber = 8900;
        String host = "localhost";

        try {
            clientSocket = new Socket(host, portNumber);
           // osObj = new PrintStream(clientSocket.getOutputStream());
           // brObj = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Client connected at port " + portNumber);
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Unknown host or port");
        }
        // TODO - Think not the best way , need to check for a better one later
        Register register = new Register();

        new Thread(new MyClientReal()).start();

        // Checking which operation the user wants
        // Register ,Login ,Logout or Check
        System.out.println("Welcome to Chit Chat Time \nWould you like to register or login?");




        // Loop forever for client message
            while (true) {

                // userSC save user input and do validation in the IF control flow
                System.out.println("~ ");
                String userSC = userInputScn.nextLine();
                // Testing dbManager.serverConnect(userSC, clientSocket);
            // - Client login Start -
            if(userSC.equalsIgnoreCase("login")){
                System.out.println("Username: ");
                login.setUserName(userInputScn.nextLine());
                System.out.println("Password: ");
                login.setPassword(userInputScn.nextLine());
                // Send the data to DBManager
                dbManager.loginServer(login.getUserName(),login.getPassword());

            }// - Client login End -

            // - Client registration Start -
            else if(userSC.equalsIgnoreCase("register")){

                // Input user information from client
                System.out.println("Username: ");
                String usernameReg = userInputScn.nextLine();
                System.out.println("Password: ");
                String passwordReg = userInputScn.nextLine();
                System.out.println("Age: ");
                int ageReg = userInputScn.nextInt();

                // Insert user information to Register
                register.regUser(usernameReg,passwordReg,ageReg);

                // Declare the user information again from the class Register
                usernameReg = register.getUsernameReg();
                passwordReg = register.getPasswordReg();
                ageReg = register.getAgeReg();

                // Send the user information to DBManager
                dbManager.registerServer(usernameReg,passwordReg,ageReg);
            } // - Client registration End -

            // - Client logout Start -
            else if(userSC.equalsIgnoreCase("logout")) {
                // Break the loop of while , then the scanner will stop working and stop the client
                break;

            } // - Client logout End -

            else  {
              //  dbManagerThread.serverConnect(userSC);
            }
            }
        }


    @Override
    public void run() {
        dbManagerThread = DBManager.getInstance();

        dbManagerThread.connectMe(clientSocket);
    }
}
