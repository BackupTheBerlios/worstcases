
import Client.AdminClient;


public class SpawnClient extends Thread {

  public int numClients = 20;
  public long sleepDelay = 1000;

  public void run() {

    AdminClient[] arr = new AdminClient[numClients];

    for (int i = 0; i < arr.length; i++) {
      arr[i] = new AdminClient();

      arr[i].setServerIP("134.169.8.196");
      arr[i].startClient();
      arr[i].loginAsGuest("guest_spawn_" + i);
      arr[i].joinChannel("Foyer");
    }

    while (true) {
      for (int i = 0; i < arr.length; i++) {
        arr[i].sendMsgToChannel("hello");
      }

      try {
        SpawnClient.sleep(sleepDelay);
      } catch (java.lang.InterruptedException e) {
        System.out.println(e);
      }
    }
  }

  public static void main(String[] args) {

    SpawnClient spawnClient = new SpawnClient();

    spawnClient.start();
  }
}
