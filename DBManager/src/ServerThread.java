

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    private String clientName = null;
    private Socket clientSocket;
    private final ServerThread[] thread;
    private int maxClientNum;
    private BufferedReader brObj = null;
    private PrintStream osObj = null;

    private Register reg = Register.getInstance();
    private LogInOut login = LogInOut.getInstance();
    private JDBC jdbc = JDBC.getInstance();
    private boolean onReg = false;
    private boolean onLog = false;

    private volatile boolean shutdown;


    // Constructor
    ServerThread(Socket clientSocket, ServerThread[] thread) {
        this.clientSocket = clientSocket;
        this.thread = thread;
        maxClientNum = thread.length;
    }

    @Override
    public void run() {
    // All server message to client
    String msgLogin,msgRegister,msgChat;
    // The client will split the msg at '.'
    msgLogin = "Welcome to Chit Chat Time. To chat with someone do /@User, to find user type /who." +
            "For logout do /quit or any additional help /help.";
    msgRegister = "";
    msgChat = "";

        try{
        while (!shutdown) {
            int maxClientNum = this.maxClientNum;
            ServerThread[] thread = this.thread;
            String name = "";
            JSONObject jServerMsg;

            try {
                // Prepare all the I/O
                InputStream isObj = clientSocket.getInputStream();
                InputStreamReader isrObj = new InputStreamReader(isObj);
                brObj = new BufferedReader(isrObj);
                osObj = new PrintStream(clientSocket.getOutputStream());


                // Client login or registration
                while (!onLog) {
                    String clientMsg = brObj.readLine();

                    String responseSwitchy = switchY(clientMsg);

                    // Start Login
                    if (responseSwitchy.startsWith("login")) {
                        name = login.getUsername();

                        jServerMsg = new JSONObject().put("login", responseSwitchy.replace("login","").trim());

                        osObj.println(jServerMsg);

                        synchronized (this) {
                            for (int i = 0; i < maxClientNum; i++) {
                                if (thread[i] != null && thread[i] == this) {
                                    clientName = "@" + name;
                                    onLog = true;
                                    break;
                                }
                            }
                        }
                    }
                    // Start Register
                    else if (responseSwitchy.startsWith("register")) {
                        name = reg.getUsername();

                        jServerMsg = new JSONObject().put("register", name );
                        osObj.println(jServerMsg);


                    }

                }

                // Start conversation
                while (true) {
                    String line = brObj.readLine();
                    if (line.startsWith("/quit")) {
                        break;
                    }
                    // If message private
                    if (line.startsWith("@")) {
                        String[] words = line.split("\\s", 2);
                        if (words.length > 1 && words[1] != null) {
                            words[1] = words[1].trim();
                            if (!words[1].isEmpty()) {
                                synchronized (this) {
                                    for (int i = 0; i < maxClientNum; i++) {
                                        if (thread[i] != null && thread[1] != this && thread[i].clientName != null && thread[i].equals(words[0])) {
                                            thread[i].osObj.println("<@" + name + "> " + words[1]);

                                            // Echo the sent message to the sender , to let know it was sent
                                            this.osObj.println(">" + name + "> " + words[1]);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // Public message
                    else {
                        // If message contains command
                        if(line.startsWith("/")){

                            // In case there is more than one back escape , it will send a message to client as a failed command
                           line = line.substring(1);
                           if (line.startsWith("/")){
                               synchronized (this) {
                                   for (int i = 0; i < maxClientNum; i++) {
                                       if (thread[i] != null && thread[i].clientName == null) {
                                           thread[i].osObj.println("Not found such command");
                                       }
                                   }
                               }
                           }
                           // If it's a legal command , will send to Command class
                           else {

                           }
                        }
                        synchronized (this) {
                            for (int i = 0; i < maxClientNum; i++) {
                                if (thread[i] != null && thread[i].clientName != null) {
                                    thread[i].osObj.println("<" + name + "> " + line);
                                }
                            }
                        }
                    }
                }


                // Left room
                synchronized (this) {
                    for (int i = 0; i < maxClientNum; i++) {
                        if (thread[i] != null && thread[i] != this && thread[i].clientName != null) {
                            thread[i].osObj.println("** The user " + name + " is leaving **");
                        }
                    }
                }
                osObj.println("** Bbye " + name + " **");

                // Clean up
                synchronized (this) {
                    for (int i = 0; i < maxClientNum; i++) {
                        if (thread[i] == this) {
                            System.out.println("left thread " + ServerThread.currentThread());
                            thread[i] = null;
                            shutdown();
                        }
                    }
                }

                // Close all
                jdbc.jdbcLogout(name);
                brObj.close();
                osObj.close();
                clientSocket.close();


            } catch (IOException e) {
                System.out.println("Unknown user disconnected");
                synchronized (this) {
                    for (int i = 0; i < maxClientNum; i++) {
                        if (thread[i] == this) {
                            thread[i] = null;
                            jdbc.jdbcLogout(name);
                            shutdown();
                        }
                    }
                }

            }

            }
        }catch (JSONException e){
            e.printStackTrace();
            System.out.println("JSON exception");
        }

    }


    // Maybe to do a class for this , so it could be change onReg at live without dropping the server ?
    // TODO - Change to a proper name once it's more developed
    private String switchY(String switchy) {
        try {
            // Read JSON
            JSONObject jClientReceived = new JSONObject(switchy);
            System.out.println(jClientReceived.toString());

        // - Login Phase -
        if ((jClientReceived.toString().contains("login"))) {
            // Get information from each node
            String userName = jClientReceived.getJSONObject("login").getString("username");
            String password = jClientReceived.getJSONObject("login").getString("password");

            login.setUsername(userName);
            login.setPassword(password);
            System.out.println(userName + " " + password);
            // return the key 'login' + the result from jdbcLogin
            return "login " + jdbc.jdbcLogin(login.getUsername(), login.getPassword(), jdbc.chkUserLogged(login.getUsername()));
        }

        // - Registration Phase -
        else if (switchy.startsWith("register")) {
            // Split the input at ":" , as the input comes register:username:password:age from the client
            String[] regArr = switchy.split(":");
            String userName = regArr[1];
            String password = regArr[2];
            int age = Integer.parseInt(regArr[3]);
            System.out.println(userName + password + age);

            reg.setUsername(userName);
            reg.setPassword(password);
            reg.setAge(age);

            return "register" + jdbc.jdbcRegister(reg.getUsername(), reg.getPassword(), reg.getAge());
        }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSON exception");
        }

        return "Not yet";
    }

    private void shutdown(){
        shutdown = true;
    }
}


