import java.util.Base64;

class Session {

    // Generate the session with RandomString Class & encode with Base64
    private String currentSession = "";
    private String encodedSession = "";

    // Creation of session with RandomString class
    // Does when login
    void createSession(String username){

        RandomString randomString = new RandomString();
        // Adds username to the session for matching with DB and additional security
        setCurrentSession(username + ":" + randomString.nextString());

    }

    // Encoding the session with Base64
    void encodeSession(String currentSession){

        byte [] encodedBytes = Base64.getEncoder().encode(currentSession.getBytes());

        setEncodedSession(new String(encodedBytes));

    }

    // Setters
    private void setCurrentSession(String currentSession) {
        this.currentSession = currentSession;
    }

    private void setEncodedSession(String encodedSession) {
        this.encodedSession = encodedSession;
    }

    // Getters
    public String getCurrentSession() {

        return currentSession;
    }

    public String getEncodedSession() {
        return encodedSession;
    }
}

