package de.tu_bs.juliet;

import de.tu_bs.juliet.server.*;
import de.tu_bs.juliet.util.debug.Debug;


class JulietServer {

  public static void main(String[] args) {

    Debug.setLevel(Debug.HIGH);

    Server testserver = new Server();

    testserver.startServer();
  }
}
