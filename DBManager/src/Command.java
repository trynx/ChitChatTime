public class Command {

    // Not in use for now
    private String cmdLine;

    // All the commands
    private String listCmd ="/help, /smile, /sad, /laugh, /cry";


    private String smileCmd(String cmdLine){
        cmdLine = this.cmdLine;
        switch (cmdLine){
            case "help":
                return "Hello user, this is Chit Chat Time, an experiment chat project for Nikita. " +
                        "There are many more commands , do /list to get it all." +
                        " Hope you would enjoy this chat =]";
            case "list":
                return listCmd;
            case "smile":
                return "=]";
            case "sad":
                return "=[" ;
            case "laugh":
                return "=D" ;
            case "cry":
                return "Y_Y";
        }
        return "non";
    }


    // Set the command line and filter
    void setCmdLine(String cmdLine) {

        if(cmdLine != null ){
            this.cmdLine = cmdLine.substring(1).toLowerCase();
        }

    }

    String getCmdLine() {
        return cmdLine;
    }

}
