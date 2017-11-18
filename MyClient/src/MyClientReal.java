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



            if(userInputScn.next().equalsIgnoreCase("register")){
                System.out.println("Username: ");
                String usernameReg = userInputScn.next();
                System.out.println("Password: ");
                String passwordReg = userInputScn.next();
                System.out.println("Age: ");
                int ageReg = userInputScn.nextInt();
                // Inserting data to Register
                // register.registerMethod(usernameReg, passwordReg, ageReg);
                // Transfer the data from Register to DBManager
                dbManager.registerServer(register.registerMethod(usernameReg, passwordReg, ageReg));
            }
            else dbManager.clientInput(userInputScn.next());

        }

    }
}