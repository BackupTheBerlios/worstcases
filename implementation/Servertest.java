import Server.*;
import Util.Debug.Debug;


class Servertest {

  public static void main(String[] args) {

    Debug.setLevel(Debug.HIGH);

    Server testserver = new Server();

    testserver.startServer();
  }
}
