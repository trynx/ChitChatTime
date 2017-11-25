public class Login {

    /**
     * Singleton Start
     */
    private static Login instance;

    // Private Constructor - to prevent instantiation from outside
    private Login() {}

    // Method to initial the class
    static Login getInstance() {
        // Lazy Initialization - to initial the instance once
        if (instance == null) instance = new Login();
        return instance;
    }
    /**
     * Singleton End
     */

    private String sessionUser = "";
    private String userName = "";
    private String password = "";

    // Setters
    void setPassword(String password) {
        if(password != null) {
            this.password = password;
        }
    }

    void setUserName(String userName) {
        if(userName != null){
            this.userName = userName;
        }
    }

    void setSessionUser(String sessionUser) {
        this.sessionUser = sessionUser;
    }

    // Getters
    String getUserName() {
        return userName;
    }

    String getPassword() {
        return password;
    }

    String getSessionUser() {
        return sessionUser;
    }

}
