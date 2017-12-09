
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

// TODO - JSON for chat
// This is the client , which will do all the operations and chatting
public class MyClientReal implements Runnable  {

    private static Socket clientSocket = null;
    private static DBManager dbManagerThread;

    private static boolean shutdown;
    private static int portNumber;
    private static String host = "localhost";

    public static void main(String [] args){

//        DBManager dbManager = DBManager.getInstance();
        Scanner userInputScn = new Scanner(System.in);
        Login login = Login.getInstance();
        boolean isLogged = false;
        boolean inServer = false;
        String userSC = "";
        Register register = new Register();

        // Checking which operation the user wants

        System.out.println("Welcome to Chit Chat Time \nWould you like to connect to a specific server? Yes/No");

        while(!inServer) {

            userSC = userInputScn.nextLine();
            // Manual connection
            if (userSC.equalsIgnoreCase("yes")){
                System.out.println("Port number");
                portNumber = userInputScn.nextInt();
                System.out.println("Server IP");
                host = userInputScn.nextLine();
                new Thread(new MyClientReal()).start();
                inServer = true;
            }

            // Default connection
            else if (userSC.equalsIgnoreCase("no")){
                // Set the port of the server
                portNumber = 8900;
                // Set the IP of the server host
                host = "localhost";
                new Thread(new MyClientReal()).start();
                inServer = true;
            }

        }



        // Loop forever for client message
        while (inServer) {

            // userSC save user input and do validation in the IF control flow
            userSC = userInputScn.nextLine();

            // - Client login Start -
            if (userSC.equalsIgnoreCase("login") && !isLogged) {
                System.out.println("Username: ");
                login.setUserName(userInputScn.nextLine());
                System.out.println("Password: ");
                login.setPassword(userInputScn.nextLine());
                // Send the data to DBManager
                dbManagerThread.loginServer(login.getUserName(), login.getPassword());
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
                // Clean up , then the scanner will stop working and stop the client
                dbManagerThread.logout(userSC);
                isLogged = false;
                inServer = false;
                shutdown();
            } // - Client logout End -

            else if(isLogged){
                // TODO - Continue from here - Chat
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

        try {
            clientSocket = new Socket(host, portNumber);

            System.out.println("Connected to server " + host + " " + portNumber + " ..." +
                    "\nWould you like to register or login?");

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
