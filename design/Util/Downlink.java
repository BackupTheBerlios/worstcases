package Util;

import java.net.Socket;
import java.util.Vector;
import java.io.*;
import Util.Commands.*;


/**
 * Empfängt über einen Socket Nachrichten von einem Uplink und leitet
 * sie an ihren Besitzer weiter.
 * Diese Klasse ist die empfangende Hälfte eines Kommunikationskanals. Die
 * andere Hälfte, die das Senden von Nachrichten übernimmt, ist der Uplink.
 * Diese Klasse wird z.B. vom ClientServant benutzt, um Nachrichten von
 * seinem Client zu empfangen.
 */
public class Downlink extends Thread {

  /**
   * Konstruktor.
   * @param socket der zu benutzende Socket.
   * @param owner der Besitzer des Downlinks.
   */
  public Downlink(Socket socket, DownlinkOwner owner) {
    this.socket = socket;
    this.downlinkOwner = owner;
  }

  /**
   *  Über diesen Socket werden die Nachrichten empfangen.
   */
  private Socket socket;

  /**
   * Der Besitzer des Downlinks, an den die ankommenden Nachrichten
   * weitergeleitet werden sollen.
   */
  private DownlinkOwner downlinkOwner;
  private ObjectInputStream objectInputStream;
  private boolean stop = false;

  /**
   * Wartet auf ankommende Nachrichten und leitet sie an den Besitzer weiter.
   */
  private void listen() {

   //  String tmpString;
   Command tmpCommand;

    while (!stop) {
      try {
        /* Muss wg. Command überarbeitet werden (Deserialisierung).
        tmpString = this.bufferedReader.readLine();

        System.out.println("#" + tmpString + "# received!");
        downlinkOwner.processMsg(tmpString);

	        */
        tmpCommand = (Command) objectInputStream.readObject(); // FIXME: Dummy
        downlinkOwner.processMsg(tmpCommand);

      } catch (Exception e) {
      }
    }
  }

  /**
   * Öffnet den Input - Stream, danach ist der Downlink betriebsbereit.
   */
  public void startDownlink() {

    try {
      this.objectInputStream =
        new ObjectInputStream(this.socket.getInputStream());
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  /**
   * Startet den Thread.
   */
  public void run() {
    listen();
  }

  /**
   * Schließt den Input - Stream
   */
  public void stopDownlink() {

    try {
      this.objectInputStream.close();
    } catch (java.io.IOException e) {
      System.out.println(e);
    }
  }

  public DownlinkOwner getDownlinkOwner() {
    return downlinkOwner;
  }

  public void setDownlinkOwner(DownlinkOwner downlinkOwner) {
    this.downlinkOwner = downlinkOwner;
  }
}
