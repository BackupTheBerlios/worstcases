package Util;

import java.net.Socket;
import java.util.Vector;
import java.io.*;
import Util.Commands.*;
import Util.Debug.Debug;


/**
 * Empfängt über einen Socket Nachrichten von einem Uplink 
 * und leitet sie an ihren Besitzer weiter.
 * Diese Klasse ist die empfangende Hälfte eines Kommunikationskanals. Die
 * andere Hälfte, die das Senden von Nachrichten übernimmt, ist der Uplink.
 * Diese Klasse wird z.B. vom ClientServant benutzt, um Nachrichten von seinem Client zu empfangen.
 */
public class Downlink extends Thread {

  /** Zeit zwischen zwei Listen()-Schleifendurchläufen in Millisekunden. */
  private int LISTEN_DELAY = 100;

  /**
   * Konstruktor, der setDownlinkOwner() benutzt.
   * @param socket der zu benutzende Socket.
   * @param owner der Besitzer des Downlinks.
   */
  public Downlink(Socket socket, DownlinkOwner owner) {

    this.socket = socket;

    this.setDownlinkOwner(owner);
  }

  /** Über diesen Socket werden die Nachrichten empfangen. */
  private Socket socket;

  /** 
   * Der Besitzer des Downlinks, an den die ankommenden Nachrichten 
   * weitergeleitet werden sollen. 
   */
  private DownlinkOwner downlinkOwner;

  /** InputStream für Objekte. */
  private ObjectInputStream objectInputStream;

  /** Wird auf true gesetzt, wenn der Thread beendet werden soll. */
  private boolean stop = false;

  /**
   * Wartet auf ankommende Nachrichten mit objectInputStream.readObject() und 
   * leitet sie an den Besitzer weiter.
   * Benutzt DownlinkOwner.downlinkError() und processMsg(). Wartet pro 
   * Schleifendurchlauf gemäß LISTEN_DELAY und beendet
   * die Schleife, falls stop = true;
   */
  private void listen() {

    Debug.println("Downlink: starting...");
    
    Command tmpCommand;
    DownlinkOwner owner = this.downlinkOwner;

    // horchen, bis stop==true (wird von außen gesetzt)
    while (!stop) {
      try {
        tmpCommand = (Command) objectInputStream.readObject();

        Debug.println("Downlink: received: " + tmpCommand);

        // owner aktualisieren
        owner = this.downlinkOwner;

        // beim owner das Command ausführen
        if (owner != null) {
          owner.processMsg(tmpCommand);
        }

        // pausieren
        this.sleep(Downlink.LISTEN_DELAY);
      } catch (Exception e) {
        Debug.println(Debug.HIGH, "Downlink: error while listening :" + e);
        e.printStackTrace();

        if (owner != null) {
          owner.downlinkError();
        }
      }
    }
  }

  /**
   * Öffnet den Input - Stream, danach ist der Downlink betriebsbereit. 
   * Ruft this.start() auf.
   * Benutzt DownlinkOwner.downlinkError().
   */
  public void startDownlink() {

    Debug.println("Downlink: trying to start...");
    
    try {
      this.objectInputStream =
        new ObjectInputStream(this.socket.getInputStream());

      Debug.println("Downlink: got ObjectInputStream.");
      
      this.start();
      Debug.println(Debug.LOW, "Downlink: started");
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH,  "Downlink: error starting downlink:" + e);
      this.downlinkOwner.downlinkError();
    }
  }

  /** Startet den Thread. */
  public void run() {
    listen();
  }

  /** Schließt den Input-Stream. Benutzt setDownlinkOwner(null). */
  public void stopDownlink() {

    // setzen, um listen() zu stoppen
    this.stop = true;

    try {
      this.objectInputStream.close();
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, "Downlink: error while stopping: " + e);
    }

    Debug.println(Debug.LOW, "Downlink: stopped");
    this.setDownlinkOwner(null);
  }

  /** Gibt den DownlinkOwner zurück. */
  public DownlinkOwner getDownlinkOwner() {
    return downlinkOwner;
  }

  /**
   * Benachrichtigt die betroffenen Objekte mittels setDownlink().
   * Hat der Downlink keinen DownlinkOwner mehr, so wird stopDownlink() 
   * aufgerufen.
   */
  public void setDownlinkOwner(DownlinkOwner paramDownlinkOwner) {

    if (this.downlinkOwner != paramDownlinkOwner) {
      if (this.downlinkOwner != null) {
        DownlinkOwner old = this.downlinkOwner;

        this.downlinkOwner = null;

        old.setDownlink(null);
      }

      this.downlinkOwner = paramDownlinkOwner;

      Debug.println("Downlink: setDownlinkOwner to " + this.downlinkOwner);

      if (paramDownlinkOwner != null) {
        paramDownlinkOwner.setDownlink(this);
      } else {
        this.stopDownlink();
      }
    }
  }
}
