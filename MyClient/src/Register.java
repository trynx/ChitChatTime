class Register {

    private String usernameReg, passwordReg;
    private int ageReg;

    // User registration method , gets the user information and conver into a String array.
    // The array is sent to DBManager
   String regUser(String username, String password, int age){
     String strAge = Interget.toString(age);   
     if(username == null || password == null || strAge == null){
         return "Username, password or age are unable \nTry again";
     } 
      
    String [] userInfo = {username, password, strAge};

    return userInfo;
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
