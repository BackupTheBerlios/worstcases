package Util;

import java.net.Socket;
import java.util.Vector;


/**
 * Empf�ngt �ber einen Socket Nachrichten von einem Uplink.
 * Diese Klasse ist die empfangende H�lfte eines Kommunikationskanals. Die 
 * andere H�lfte, die das Senden von Nachrichten �bernimmt, ist der Uplink.
 * Diese Klasse wird z.B. vom ClientServant benutzt, um Nachrichten an
 * seinen Client zu senden.
 */
class Downlink extends Thread {

  /**
   * Konstruktor.
   * @param socket der zu benutzende Socket.
   */
  public Downlink(Socket socket) {
    this.socket = socket;
  }

  /**
   *  �ber diesen Socket werden die Nachrichten empfangen.
   */
  private Socket socket;

  /**
   * Wartet auf ankommende Nachrichten.
   */
  public void listen() { //FIXME: Wieso public?

    String msg = "foo";

    listen(); //FIXME: Das steht hier wohl nur wegen der Sequenzdiagramme?
  }

  public String getMsg() {
    return msg;
  }

  /**
   * �ffnet den Input - Stream, danach ist der Downlink betriebsbereit.
   */
  public void startDownlink() {}

  public void run() {
    this.listen();
  }

  /**
   * Schlie�t den Input - Stream
   */
  public void stopDownlink() {}

  public void start() {
    run();
  }
}
