/* This file is part of Juliet, a chat system.
   Copyright (C) 2001 Andreas B�the <a.buethe@tu-bs.de>
             (C) 2001 Jan-Henrik Grobe <j-h.grobe@tu-bs.de>
             (C) 2001 Frithjof Hummes <f.hummes@tu-bs.de>
             (C) 2001 Malte Kn�rr <m.knoerr@tu-bs.de>
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
import java.io.*;
import de.tu_bs.juliet.util.commands.*;
import de.tu_bs.juliet.util.debug.Debug;


/**
 * Versendet Nachrichten �ber einen Socket an einen Downlink.
 * Diese Klasse ist die sendende H�lfte eines Kommunikationskanals.
 * Die andere H�lfte, die das Empfangen von Nachrichten �bernimmt,
 * ist der Downlink.
 * Diese Klasse wird z.B. vom Client benutzt, um Nachrichten an seinen
 * ClientServant zu senden.
 * @see de.tu_bs.juliet.util.Downlink
 * @see de.tu_bs.juliet.client.Client
 * @see de.tu_bs.juliet.server.ClientServant
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
    // das Objekt wird vom Output-Stream geschrieben
    objectOutputStream.writeObject(msg);
    /* der Output-Stream wird �ber flush() geleert, somit verl�sst das 
       geschriebene Objekt den Buffer und wird endg�ltig versendet */
    objectOutputStream.flush();
  }
}
