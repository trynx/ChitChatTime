class Register {

    private String usernameReg, passwordReg;
    private int ageReg;

   /* String registerMethod(String username, String password, int age){

     if(username == null && password == null){
         System.out.println("Username or password unable\n Try again");
     }
     System.out.println("Registering");

     // Set all the user information into a string
    // String userInfo = "'" + username + "'" + "," + "'" + password + "'" + ","  + Integer.toString(age) ;
    setUsernameReg(username);
    setPasswordReg(password);
    setAgeReg(age);

    return userInfo;
 } */

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
