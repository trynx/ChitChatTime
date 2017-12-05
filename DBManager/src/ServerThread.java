

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

        while (!shutdown) {
            int maxClientNum = this.maxClientNum;
            ServerThread[] thread = this.thread;
            String name = "";

            try {
                // Prepare all the I/O
                InputStream isObj = clientSocket.getInputStream();
                InputStreamReader isrObj = new InputStreamReader(isObj);
                brObj = new BufferedReader(isrObj);
                osObj = new PrintStream(clientSocket.getOutputStream());

                // Start of client server



            /* Prep of user = My login
            while (true) {

                osObj.println("Enter your name dude");
                name = brObj.readLine().trim();
                if (name.indexOf('@') == -1) {
                    break;
                } else {
                    osObj.println("The name shouldn't include '@'.");
                }
            }*/
                // Start of client server

                // TODO - Thread reset when sudden disconnect
                // Welcome the new client
                while (!onLog) {
                    String clientMsg = brObj.readLine();
                    String responseSwitchy = switchY(clientMsg);

                    if (responseSwitchy.startsWith("login")) {
                        name = login.getUsername();

                        osObj.println("Welcome " + name + ". Type /quit to leave");
                        synchronized (this) {
                            for (int i = 0; i < maxClientNum; i++) {
                                if (thread[i] != null && thread[i] == this) {
                                    System.out.println(ServerThread.currentThread());
                                    System.out.println(ServerThread.activeCount());
                                    clientName = "@" + name;
                                    onLog = true;
                                    break;
                                }
                            }

                            for (int i = 0; i < maxClientNum; i++) {
                                if (thread[i] != null && thread[i] != this) {
                                    thread[i].osObj.println("** New user " + name + " entered **");
                                }
                            }
                        }
                    } else if (responseSwitchy.startsWith("register")) {
                        name = reg.getUsername();
                        osObj.println("The registration of " + name + " is completed. Please login");
                        synchronized (this) {
                            for (int i = 0; i < maxClientNum; i++) {
                                if (thread[i] == this) {
                                    System.out.println("left thread after registered " + ServerThread.currentThread());
                                    System.out.println(ServerThread.activeCount());
                                    thread[i] = null;
                                    shutdown();
                                }
                            }
                        }

                    } else osObj.println("Please login or register.");

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

                            System.out.println("Drop thread " + ServerThread.currentThread());
                            System.out.println(ServerThread.activeCount());
                            thread[i] = null;
                            jdbc.jdbcLogout(name);
                            shutdown();
                        }
                    }
                }

            }


        }
    }


    // Maybe to do a class for this , so it could be change onReg at live without dropping the server ?
    // TODO - Change to a proper name once it's more developed
    private String switchY(String switchy) {

        // - Login Phase -
        if (switchy.startsWith("login")) {
            // Split the input at ":" , as the input comes login:username:password from the client
            String[] loginArr = switchy.split(":", 0);
            String userName = loginArr[1];
            String password = loginArr[2];

            login.setUsername(userName);
            login.setPassword(password);

            return "login" + jdbc.jdbcLogin(login.getUsername(), login.getPassword(), jdbc.chkUserLogged(login.getUsername()));
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

        return "Not yet";
    }

    private void shutdown(){
        shutdown = true;
    }
}


