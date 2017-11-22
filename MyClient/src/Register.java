class Register {

    private String usernameReg, passwordReg;
    private int ageReg;

    // Method to register the user information into the class
    void regUser(String username, String password, int age){

        if(username != null || password != null ){
            setUsernameReg(username);
            setPasswordReg(password);
            setAgeReg(age);
        }
        System.out.println("Username, password or age are unable \nTry again");

    }



    private  void setUsernameReg(String usernameReg){
        this.usernameReg = usernameReg;
    }

    String getUsernameReg(){
        return this.usernameReg;
    }

    private void setPasswordReg(String passwordReg){
        this.passwordReg = passwordReg;
    }

    String getPasswordReg(){
        return this.passwordReg;
    }

    private void setAgeReg(int ageReg){
        this.ageReg = ageReg;
    }

    int getAgeReg(){
        return this.ageReg;
    }

}
