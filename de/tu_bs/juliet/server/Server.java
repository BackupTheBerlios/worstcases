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

package de.tu_bs.juliet.server;

import java.net.ServerSocket;
import java.util.Vector;
import java.net.Socket;
import java.util.Enumeration;

import de.tu_bs.juliet.util.debug.Debug;


/**
 * Diese Klasse ist der Chat-Server. Der Server horcht an seinem
 * Port, und wenn ein Client eine Verbindungsanfrage stellt, 
 * startet er einen
 * ClientServant, der sich ab dann um diesen Client kümmert. Dann fängt der
 * Server wieder an, an seinem Port zu horchen und wartet auf neue
 * Client-Verbindungswünsche.
 */
public class Server {

  private DataBaseIO dataBaseIO;

  private ChannelAdministration channelAdministration;
  
  private UserAdministration userAdministration;

  /** Eine Liste der aktiven ClientServants des Servers.*/
  private Vector clientServantList = new Vector();

  private ClientServantWatchDog clientServantWatchDog;

  /**
   * Die Länge der Warteschlange, in der Verbindungswünsche von Clients
   * zwischengespeichert werden, die nicht sofort verarbeitet werden können.
   * Verbindungswünsche, die nicht mehr in die Warteschlange  passen, werden
   * automatisch abgewiesen (siehe auch java.net.ServerSocket).
   */
  private int LISTEN_QUEUE_LENGTH = 10;

  /**
   * Der Port, auf dem der Server sein ServerSocket öffnet und auf Anfragen
   * der Clients horcht.
   */
  private int SERVER_PORT = 1500;


  private ServerSocket serverSocket;

  /**
   * Flag, welches angibt, ob listen() weiter auf Verbindungen warten
   * soll.
   */
  private boolean stop = false;

  /**
   * Diese Methode initialisiert den Server, indem neue Referenzen von
   * channelAdministration, userAdministration, clientServantWatchDog
   * und dataBaseIO erzeugt werden. Außerdem werden die Benutzer- und
   * Channeldaten geladen und ein ClientServantDog gestartet, um inaktive
   * Clients aus dem System zu entfernen. Ruft listen() auf.
   */
  public void startServer() {

    Debug.println(Debug.LOW, "Server : starting server");

    this.channelAdministration = new ChannelAdministration();

    this.userAdministration =
      new UserAdministration(this.channelAdministration);

    this.dataBaseIO = new DataBaseIO(this.userAdministration,
                                     this.channelAdministration);

    this.clientServantWatchDog = new ClientServantWatchDog(this);

    try {
      this.dataBaseIO.loadFromDisk();

      // wird nur ausgeführt, falls loadFromDisk() keine Exception wirft
      this.clientServantWatchDog.start();
      this.listen();
    }

    // Fehlerausgabe
    catch (java.io.FileNotFoundException e) {
      Debug.println(Debug.HIGH,
                    "Server: DataBase IO could not load database:" + e);
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH,
                    "Server: DataBase IO could not load database:" + e);
    }
  }

  /**
   * Stoppt den Server, indem die ClientServants durch eine Schleife mit der
   * Methode removeFromClientServant 
   * aus der ClientServantList enfernt werden.
   * Setzt stop=true, um die Listen-Methode zu beenden.
   */
  public void stopServer() {

    this.stop = true;

    // der ClientServantWatchDog wird angehalten
    this.clientServantWatchDog.stop = true;

    Enumeration enum = this.clientServantList.elements();

    // die ClientServants werden aus der Liste entfernt
    while (enum.hasMoreElements()) {
      this.removeFromClientServantList((ClientServant) enum.nextElement());
    }

    Debug.println(Debug.LOW, "Server: Server stopped");
  }

  /**
   * Entfernt den übergebenen ClientServant durch setServer(null) aus der
   * Liste der aktiven ClientServants und benachrichtigt den ClientServant.
   */
  public synchronized void removeFromClientServantList(
          ClientServant paramClientServant) {

    if (paramClientServant != null) {
      if (this.clientServantList.removeElement(paramClientServant)) {
        paramClientServant.setServer(null);
        Debug.println(Debug.MEDIUM,
                      "Server: " + this.clientServantList.size()
                      + " ClientServants active");
      }
    }
  }

  /** Gibt eine Enumeration der aktiven ClientServants zurück. */
  //FIXME Helper.vectorCopy()
  public Enumeration getClientServantEnum() {
    return this.clientServantList.elements();
  }

  public DataBaseIO getDataBaseIO() {
    return this.dataBaseIO;
  }

  /**
   * In listen() wird zuerst ein neuer ServerSocket angelegt. In einer
   * Schleife werden, solange der Thread nicht gestoppt wurde, bei
   * ankommenden Verbindungenswünschen von Clients neue Clientservants
   * erstellt, diese zur Liste der Servants hinzugefügt und gestartet.
   * Nachdem der Thread beendet wurde, wird der ServerSocket geschlossen.
   * Falls die Zugriffe auf den ServerSocket nicht möglich sind, werden
   * diese durch try und catch abgefangen.
   * Schleifendurchlauf, solange stop==true.
   * Benutzt ClientServant.startClientServant() und addToClientServantList().
   */
  private void listen() {

    try {

      // neuer ServerSocket wird angelegt
      this.serverSocket = new ServerSocket(this.SERVER_PORT,
                                           this.LISTEN_QUEUE_LENGTH);

      Debug.println(Debug.MEDIUM,
                    "Server: Server started on "
                    + this.serverSocket.getInetAddress() + ":"
                    + this.serverSocket.getLocalPort());

      // Schleife bis Thread beendet wird
      while (!stop) {
        try {
          Debug.println(
            "Server: ready to wait for more incoming connections.");

          // horcht am Port für eine Verbindung mit einem Client
          Socket tmpSocket = serverSocket.accept();

          Debug.println(Debug.LOW, "Server: new tmpSocket instantiated.");
          Debug.println(Debug.MEDIUM,
                        "Server: incoming connection from "
                        + tmpSocket.getInetAddress() + ":"
                        + tmpSocket.getPort());

          // neuer ClientServant wird erstellt
          ClientServant tmpClientServant = new ClientServant(tmpSocket, this,
                                             userAdministration);

          Debug.println(Debug.LOW, "Server: new ClientServant");

          // neuer ClientServant wird in die Liste hinzugefügt
          this.addToClientServantList(tmpClientServant);
          Debug.println(Debug.LOW, "Server: ClientServant added to list");

          // der neue Clientservant wird gestartet
          tmpClientServant.startClientServant();
          Debug.println(Debug.LOW, "Server: ClientServant started");

          // Fehler beim horchen werden abgefangen
        } catch (java.io.IOException e) {
          Debug.println(Debug.HIGH, "Server: error while listening: " + e);
        }
      }
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, "Server: error opening socket:" + e);
    }

    // Socket wird geschlossen
    try {
      this.serverSocket.close();
      Debug.println(Debug.LOW, "Server: ServerSocket closed");
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, "Server: error closing serverSocket: " + e);
    }
  }

  /**
   * Fügt einen ClientServant zu der Liste aktiver ClientServants hinzu.
   * Benutzt ClientServant.setServer().
   */
  public synchronized void addToClientServantList(
          ClientServant paramClientServant) {

    if (paramClientServant != null) {

      // wenn der ClientServant noch nicht in der Liste ist, dann....
      if (!this.clientServantList.contains(paramClientServant)) {

        // ClientServant wird der Liste hinzugefügt
        this.clientServantList.addElement(paramClientServant);
        paramClientServant.setServer(this);
        Debug.println(Debug.MEDIUM,
                      "Server: " + this.clientServantList.size()
                      + " ClientServants active");
      }
    }
  }

  /** Gibt die aktive ChannelAdministration zurück. */
  public ChannelAdministration getChannelAdministration() {
    return channelAdministration;
  }

}
