import java.util.Scanner;

// This is the client , which will do all the operations and chatting
public class MyClientReal {

    // TODO - Maybe there is a better way ?
    static boolean connected = true;

    public static void main(String [] args){

        DBManager dbManager = DBManager.getInstance();
        Scanner userInputScn = new Scanner(System.in);

        System.out.println("Welcome to Chit Chat Time \nWould you like to register ,login or check?");

        // Checking which operation the user wants
        // Register ,Login ,Logout or Check

        // Loop forever for client message
        while(connected){
            System.out.print("~ ");
            dbManager.clientInput(userInputScn.next());

        }

    }
}