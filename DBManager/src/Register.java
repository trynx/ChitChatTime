public class Register {

    // - Singleton Start -
    private static Register instance;

    // Private Constructor - to prevent instantiation from outside
    private Register() {}

    // Method to initial the class
    static Register getInstance() {
        // Lazy Initialization - to initial the instance once
        if (instance == null) instance = new Register();
        return instance;
    }
    // - Singleton End -



    private String username, password;
    private int age;

    // Setter Group
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(int age) {
        this.age = age;
    }


    // Getter Group
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }
}
