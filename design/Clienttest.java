import Server.*;
import Client.*;
import Util.*;
import java.io.*;


class Clienttest {

  public static void main(String[] args) {

    Client tmpClient = new Client();

    tmpClient.startClient();
    tmpClient.loginAsGuest(args[0]);
    tmpClient.joinChannel("Virtuelle Konferenz");
    tmpClient.sendMsgToChannel("hello world");

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String tmpString;

    while (true) {
      try {
        tmpString = in.readLine();

        tmpClient.sendMsgToChannel(tmpString);
      } catch (java.io.IOException e) {
        System.out.println(e);
      }
    }
  }
}
