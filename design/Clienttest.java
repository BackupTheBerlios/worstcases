import Server.*;
import Client.*;
import Util.*;
import java.io.*;

class Clienttest {
    public static void main(String[] args) {
        Client tmpClient = new Client();
        tmpClient.startClient();
        tmpClient.login(args[0],args[1]);
	tmpClient.joinChannel("Virtuelle Konferenz");
        BufferedReader in = new BufferedReader(
            new InputStreamReader(System.in));
        String tmpString;
	
    }
}
