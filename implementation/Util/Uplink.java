package Util;

import java.net.Socket;
import java.io.*;
import Util.Commands.*;
import Util.Debug.Debug;


/**
 * Versendet Nachrichten �ber einen Socket an einen Downlink.
 * Diese Klasse ist die sendende H�lfte eines Kommunikationskanals.
 * Die andere H�lfte, die das Empfangen von Nachrichten �bernimmt,
 * ist der Downlink.
 * Diese Klasse wird z.B. vom Client benutzt, um Nachrichten an seinen
 * ClientServant zu senden.
 * @see Util.Downlink
 * @see Client.Client
 * @see Server.ClientServant
 */
public class Uplink {

  /**
   * Konstruktor.
   * @param socket der zu benutzende Socket.
   */
  public Uplink(Socket paramSocket) {
    this.socket = paramSocket;
  }

  /** �ber diesen Socket werden die Nachrichten versendet. */
  private Socket socket;

  /** Output-Stream f�r Objekte. */
  private ObjectOutputStream objectOutputStream;

  /** �ffnet den Output-Stream. */
  public void startUplink() throws java.io.IOException {

    this.objectOutputStream =
      new ObjectOutputStream(this.socket.getOutputStream());

    Debug.println(Debug.LOW, "Uplink: started");
  }

  /** Schlie�t den Output-Stream. */
  public void stopUplink() {

    try {
      this.objectOutputStream.close();
      Debug.println(Debug.LOW, "Uplink: stopped");
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, "Uplink: error stopping:" + e);
    }
  }

  /**
   * Sendet ein Commandobjekt �ber den Socket. Es wird am anderen
   * Ende des Kommunikationskanals von einem Downlink empfangen.
   * Benutzt objectOutputStream.writeObject()
   * @param msg Das zu versendende Commandobjekt.
   * @see Downlink
   */
  public synchronized void sendMsg(Command msg) throws java.io.IOException {

    Debug.println("Uplink: sending " + msg);
    objectOutputStream.writeObject(msg);
    objectOutputStream.flush();
  }
}
