package Util;

import java.net.Socket;
import java.util.Vector;


/**
 * Empfängt über einen Socket Nachrichten von einem Uplink.
 * Diese Klasse ist die empfangende Hälfte eines Kommunikationskanals. Die 
 * andere Hälfte, die das Senden von Nachrichten übernimmt, ist der Uplink.
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
   *  Über diesen Socket werden die Nachrichten empfangen.
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
   * Öffnet den Input - Stream, danach ist der Downlink betriebsbereit.
   */
  public void startDownlink() {}

  public void run() {
    this.listen();
  }

  /**
   * Schließt den Input - Stream
   */
  public void stopDownlink() {}

  public void start() {
    run();
  }
}
