package Server;
import Util.Debug.Debug;



/** Dient zum Testen der Klasse Server. */

public class Servertest {

    public static void main(String[] args) throws java.io.FileNotFoundException, java.io.IOException {
        Debug.setLevel(Debug.HIGH);

        Server tmpServer = new Server();

        tmpServer.startServer();

    }

}

