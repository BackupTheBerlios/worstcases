import Util.*;
import Server.*;
import Client.*;

class Servertest {
    public static void main(String[] args) throws java.io.FileNotFoundException, java.io.IOException {
        Server tmpServer = new Server();
        tmpServer.startServer();
	tmpServer.stopServer();
    }
    public void stop(){
     System.out.println("Hallo");
    }

}
