/* This file is part of Juliet, a chat system.
   Copyright (C) 2001 Andreas Büthe <a.buethe@tu-bs.de>
             (C) 2001 Jan-Henrik Grobe <j-h.grobe@tu-bs.de>
             (C) 2001 Frithjof Hummes <f.hummes@tu-bs.de>
             (C) 2001 Malte Knörr <m.knoerr@tu-bs.de>
	     (C) 2001 Fabian Rotte <f.rotte@tu-bs.de>
	     (C) 2001 Quoc Thien Vu <q.vu@tu-bs.de>
   
   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package de.tu_bs.juliet.util;

import java.net.Socket;
import java.util.Vector;
import java.io.*;
import de.tu_bs.juliet.util.commands.*;
import de.tu_bs.juliet.util.debug.Debug;


/**
 * Empfängt über einen Socket Nachrichten von einem Uplink
 * und leitet sie an ihren Besitzer weiter.
 * Diese Klasse ist die empfangende Hälfte eines Kommunikationskanals. Die
 * andere Hälfte, die das Senden von Nachrichten übernimmt, ist der Uplink.
 * Diese Klasse wird z.B. vom ClientServant benutzt, um Nachrichten von 
 * seinem Client zu empfangen.
 */
public class Downlink extends Thread {

  /** Zeit zwischen zwei Listen()-Schleifendurchläufen in Millisekunden. */
  private static int LISTEN_DELAY = 100;

  /**
   * Der Besitzer des Downlinks, an den die ankommenden Nachrichten
   * weitergeleitet werden sollen.
   */
  private DownlinkOwner downlinkOwner;

  /** InputStream für Objekte. */
  private ObjectInputStream objectInputStream;

  /** Über diesen Socket werden die Nachrichten empfangen. */
  private Socket socket;

  /** Wird auf true gesetzt, wenn der Thread beendet werden soll. */
  private boolean stop = false;

  /**
   * Konstruktor, der setDownlinkOwner() benutzt.
   * @param socket der zu benutzende Socket.
   * @param owner der Besitzer des Downlinks.
   */
  public Downlink(Socket socket, DownlinkOwner owner) {

    this.socket = socket;

    this.setDownlinkOwner(owner);
  }

  /** Klassische get-Methode: Gibt den DownlinkOwner zurück. */
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
        } else {
          this.stop = true;
        }
      }
    }
  }

  /**
   * Öffnet den Input - Stream, danach ist der Downlink betriebsbereit.
   * Ruft this.start() auf. Benutzt DownlinkOwner.downlinkError().
   */
  public void startDownlink() {

    Debug.println("Downlink: trying to start...");

    try {
      // öffnet einen neuen ObjectInputSteam
      this.objectInputStream =
        new ObjectInputStream(this.socket.getInputStream());

      Debug.println("Downlink: got ObjectInputStream.");
      this.start(); // ruft die run-Methode auf      
      Debug.println(Debug.LOW, "Downlink: started");
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, "Downlink: error starting downlink:" + e);
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
      // schließt den ObjectInputStream
      this.objectInputStream.close();
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, "Downlink: error while stopping: " + e);
    } catch (NullPointerException e) {
	Debug.println(Debug.HIGH, "Downlink: no InputStream available to close (" +
            e + "). Perhaps somebody tried to telnet the server.");
    }
    

    Debug.println(Debug.LOW, "Downlink: stopped");
    this.setDownlinkOwner(null);
  }
}
