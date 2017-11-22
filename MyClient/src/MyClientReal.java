import java.util.Scanner;

// This is the client , which will do all the operations and chatting
public class MyClientReal {

    // TODO - Maybe there is a better way ?
    static boolean connected = true;

    public static void main(String [] args){

        DBManager dbManager = DBManager.getInstance();
        Scanner userInputScn = new Scanner(System.in);

        // TODO - Think not the best way , need to check for a better one later
        Register register = new Register();

        System.out.println("Welcome to Chit Chat Time \nWould you like to register ,login or check?");

        // Checking which operation the user wants
        // Register ,Login ,Logout or Check

        // Loop forever for client message
        while(connected){
            System.out.print("~ ");
            // userSC save user input and do validation in the IF control flow
            String userSC = userInputScn.next();

            // - Client login start -
            if(userSC.equalsIgnoreCase("login")){
                System.out.println("Username: ");
                String usernameLog = userInputScn.next();
                System.out.println("Password: ");
                String passwordLog = userInputScn.next();
                // Send the data to DBManager
                dbManager.loginServer(usernameLog,passwordLog);

            }// - Client login end -

            // - Client registration start -
           else if(userSC.equalsIgnoreCase("register")){

                // Update after testing to Get/Set for Register Class
                System.out.println("Username: ");
                String usernameReg = userInputScn.next();
                System.out.println("Password: ");
                String passwordReg = userInputScn.next();
                System.out.println("Age: ");
                int ageReg = userInputScn.nextInt();
                // Inserting data to Register
                // TODO - Make this method work once Register , LogIn class work as necessary
                String [] userArray = register.regUser(String usernameReg, String passwordReg, int ageReg);
                // register.registerMethod(usernameReg, passwordReg, ageReg);
                // Send the data to DBManager
                dbManager.registerServer(userArray);
            } // - Client registration end -
            else dbManager.clientInput(userInputScn.next());

        }

    }
}
