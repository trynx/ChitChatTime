
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

// TODO - JSON for chat
// This is the client , which will do all the operations and chatting
public class MyClientReal implements Runnable  {

    private static Socket clientSocket = null;
    private static DBManager dbManagerThread;
    private static boolean shutdown;
    public static void main(String [] args){

//        DBManager dbManager = DBManager.getInstance();
        Scanner userInputScn = new Scanner(System.in);
        Login login = Login.getInstance();
        boolean isLogged = false;

        Register register = new Register();



        // Checking which operation the user wants
        // Register ,Login ,Logout or Check
        System.out.println("Welcome to Chit Chat Time \nWould you like to register or login?");

//        new Thread(new MyClientReal()).start();


        // Loop forever for client message
            while (true) {

                // userSC save user input and do validation in the IF control flow
//                System.out.println("~ ");
                String userSC = userInputScn.nextLine();
                // Testing dbManager.serverConnect(userSC, clientSocket);
            // - Client login Start -
            if(userSC.equalsIgnoreCase("login") && !isLogged){
                new Thread(new MyClientReal()).start(); // TODO - Fix later the delay
                System.out.println("Username: ");
                login.setUserName(userInputScn.nextLine());
                System.out.println("Password: ");
                login.setPassword(userInputScn.nextLine());


                // Send the data to DBManager
                dbManagerThread.loginServer(login.getUserName(),login.getPassword());
                isLogged = true;

            }// - Client login End -

            // - Client registration Start -
            else if(userSC.equalsIgnoreCase("register") && !isLogged){
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
                dbManagerThread.registerServer(usernameReg,passwordReg,ageReg);
            } // - Client registration End -

            // - Client logout Start -
            else if(userSC.equalsIgnoreCase("/quit")) {
                // Break the loop of while , then the scanner will stop working and stop the client
                dbManagerThread.logout(userSC);
                isLogged = false;
                shutdown();
            } // - Client logout End -

                else if(isLogged){
                dbManagerThread.chatServer(userSC);
            }
                else {
                System.out.println("Please either login or register");
            }
            }
        }


    @Override
    public void run() {
        // Open socket
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

        shutdown = false;
        while (!shutdown) {
            dbManagerThread = DBManager.getInstance();

            dbManagerThread.connectMe(clientSocket);
        }
    }

    // Shut down the current thread
    private static void shutdown(){
        shutdown = true;
    }
}
