package Util;

import java.net.Socket;
import java.util.Vector;
import java.io.*;
import Util.Commands.*;
import Util.Debug.Debug;


/**
 * Empf�ngt �ber einen Socket Nachrichten von einem Uplink und leitet sie an ihren Besitzer weiter.
 * Diese Klasse ist die empfangende H�lfte eines Kommunikationskanals. Die
 * andere H�lfte, die das Senden von Nachrichten �bernimmt, ist der Uplink.
 * Diese Klasse wird z.B. vom ClientServant benutzt, um Nachrichten von seinem Client zu empfangen.
 */
public class Downlink extends Thread {

  private int LISTEN_DELAY = 1000;

  /**
   * Konstruktor.
   * @param socket der zu benutzende Socket.
   * @param owner der Besitzer des Downlinks.
   */
  public Downlink(Socket socket, DownlinkOwner owner) {

    this.socket = socket;

    setDownlinkOwner(owner);
  }

  /** �ber diesen Socket werden die Nachrichten empfangen. */
  private Socket socket;

  /** Der Besitzer des Downlinks, an den die ankommenden Nachrichten weitergeleitet werden sollen. */
  private DownlinkOwner downlinkOwner;
  private ObjectInputStream objectInputStream;
  private boolean stop = false;

  /** Wartet auf ankommende Nachrichten und leitet sie an den Besitzer weiter. */
  private void listen() {

    // String tmpString;
    Command tmpCommand;

    while (!stop) {
      try {
        tmpCommand = (Command) objectInputStream.readObject();

        Debug.println("Downlink: received " + tmpCommand);
        downlinkOwner.processMsg(tmpCommand);

        try {
          sleep(Downlink.LISTEN_DELAY);
        } catch (java.lang.InterruptedException e) {
          Debug.println(Debug.HIGH, e);
        }
      } catch (java.io.IOException e) {
        Debug.println(Debug.HIGH, e);
        stopDownlink();
      } catch (java.lang.ClassNotFoundException e) {
        Debug.println(Debug.HIGH, e);
      }
    }

    try {
      this.objectInputStream.close();
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, e);
    }

    Debug.println("Downlink: stopped");
  }

  /** �ffnet den Input - Stream, danach ist der Downlink betriebsbereit. */
  public void startDownlink() throws java.io.IOException {

    objectInputStream =
      new ObjectInputStream(socket.getInputStream());

    Debug.println("Downlink: started");
  }

  /** Startet den Thread. */
  public void run() {
    listen();
  }

  /** Schlie�t den Input - Stream */
  private void stopDownlink() {
    stop = true;
  }

  public DownlinkOwner getDownlinkOwner() {
    return downlinkOwner;
  }

  public void setDownlinkOwner(DownlinkOwner paramDownlinkOwner) {

    if (downlinkOwner != paramDownlinkOwner) {
      if (downlinkOwner != null) {
        DownlinkOwner old = downlinkOwner;

        downlinkOwner = null;

        old.setDownlink(null);
      }

      this.downlinkOwner = paramDownlinkOwner;

      if (paramDownlinkOwner != null) {
        paramDownlinkOwner.setDownlink(this);
      }
    }
  }
}
