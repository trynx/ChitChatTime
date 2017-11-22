import java.util.Scanner;

// This is the client , which will do all the operations and chatting
public class MyClientReal {

    // TODO - Session and keep it saved in the client
    // TODO - Make a method that each outpuststream have the seassion + username together for verification in the server
    // TODO - Method for chat , once a user is added , it will saved it until the client decide to end the chat
    public static void main(String [] args){

        DBManager dbManager = DBManager.getInstance();
        Scanner userInputScn = new Scanner(System.in);

        // TODO - Think not the best way , need to check for a better one later
        Register register = new Register();

        System.out.println("Welcome to Chit Chat Time \nWould you like to register ,login or check?");

        // Checking which operation the user wants
        // Register ,Login ,Logout or Check

        // Loop forever for client message
        while(true){
            System.out.print("~ ");
            // userSC save user input and do validation in the IF control flow
            String userSC = userInputScn.next();

            // - Client login Start -
            if(userSC.equalsIgnoreCase("login")){
                System.out.println("Username: ");
                String usernameLog = userInputScn.next();
                System.out.println("Password: ");
                String passwordLog = userInputScn.next();
                // Send the data to DBManager
                dbManager.loginServer(usernameLog,passwordLog);

            }// - Client login End -

            // - Client registration Start -
            else if(userSC.equalsIgnoreCase("register")){

                // Input user information from client
                System.out.println("Username: ");
                String usernameReg = userInputScn.next();
                System.out.println("Password: ");
                String passwordReg = userInputScn.next();
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

            else dbManager.clientInput(userInputScn.next());

        }

    }
}
