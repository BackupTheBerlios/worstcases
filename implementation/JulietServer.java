import Server.*;
import Util.Debug.Debug;


class JulietServer {

  public static void main(String[] args) {

    Debug.setLevel(Debug.HIGH);

    Server testserver = new Server();

    testserver.startServer();
  }
}
