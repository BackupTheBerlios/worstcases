package Util;

import java.net.Socket;
import java.util.Vector;


/**
 * Empf‰ngt ¸ber einen Socket Nachrichten von einem Uplink und leitet
 * sie an ihren Besitzer weiter.
 * Diese Klasse ist die empfangende H‰lfte eines Kommunikationskanals. Die 
 * andere H‰lfte, die das Senden von Nachrichten ¸bernimmt, ist der Uplink.
 * Diese Klasse wird z.B. vom ClientServant benutzt, um Nachrichten von
 * seinem Client zu empfangen.
 */
class Downlink extends Thread {

  /**
   * Konstruktor.
   * @param socket der zu benutzende Socket.
   * @param owner der Besitzer des Downlinks.
   */
  public Downlink(Socket socket, Object owner) {
    this.socket = socket;
    this.owner = owner;
  }

  /**
   *  ‹ber diesen Socket werden die Nachrichten empfangen.
   */
  private Socket socket;

  /**
   * Der Besitzer des Downlinks, an den die ankommenden Nachrichten
   * weitergeleitet werden sollen.
   */
  private Object owner;

  /**
   * Wartet auf ankommende Nachrichten und leitet sie an den Besitzer weiter. 
   */
  public void listen() { //FIXME: Wieso public?

    String msg = "foo";

    /* Wenn der Besitzer dieses Downlinks ein Client (oder ein
     * davon abgeleiteter ClientServant) ist, werden empfangene Nachrichten
     * an seine Methode processMsg() ¸bergeben.
     *
     * Das ist eigentlich ¸bler Missbrauch des Reflection-APIs (siehe
     * http://www.artima.com/designtechniques/rtci.html).
     */
    if (owner instanceof Client) { //XXX
      Client client = (Client) owner; // Performance-Problem?
      client.processMsg(msg);
    }
    listen(); //FIXME: Rekursion (-> StackOverflowError?), besser while.
  }

  /**
   * ÷ffnet den Input - Stream, danach ist der Downlink betriebsbereit.
   */
  public void startDownlink() {}

  /**
   * Startet den Thread.
   */
  public void run() {
    listen();
  }

  /**
   * Schlieﬂt den Input - Stream
   */
  public void stopDownlink() {}

  public void start() {
    run();
  }
}
