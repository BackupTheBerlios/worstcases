import Server.*;
import Client.*;
import Util.*;
import java.io.*;

class Clienttest {
    public static void main(String[] args) {
        AdminClient tmpClient = new AdminClient();
        tmpClient.startClient();
        tmpClient.login(args[0],args[1]);
        tmpClient.deleteUser("mauper");
	tmpClient.joinChannel("TUBS");
	tmpClient.sendMsgToChannel("hallo");
        BufferedReader in = new BufferedReader(
            new InputStreamReader(System.in));
        String tmpString;
	
    }
}
