package Client;

/**
 * Dient dem Testen der Klasse Client.
 */
public class Testclient {

  public static void main(String[] args) {

    Client tmpClient = new Client();

    tmpClient.loginAsGuest(args[0]);
    tmpClient.joinChannel("Virtuell Konferenz");
    tmpClient.sendMsgToChannel("Hallo");
  }
}
