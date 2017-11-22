class LogInOut {

    // - Singleton Start -
    private static LogInOut instance;

    // Private Constructor - to prevent instantiation from outside
    private LogInOut() {}

    // Method to initial the class
    static LogInOut getInstance() {
        // Lazy Initialization - to initial the instance once
        if (instance == null) instance = new LogInOut();
        return instance;
    }
    // - Singleton End -



    private String username, password;

    // Setter Group
    void setUsername(String username) {
        this.username = username;
    }

    void setPassword(String password) {
        this.password = password;
    }



    // Getter Group
    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

}

