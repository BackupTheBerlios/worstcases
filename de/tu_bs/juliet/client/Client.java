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

package de.tu_bs.juliet.client;

import java.util.Vector;
import java.net.Socket;

import de.tu_bs.juliet.gui.*;
import de.tu_bs.juliet.util.*;
import de.tu_bs.juliet.util.commands.*;
import de.tu_bs.juliet.util.debug.Debug;


/**
 * Die Klasse Client stellt dem Benutzer alle nötigen Methoden zur
 * Verfügung, um an einer Virtuellen Konferenz, also einem Chat teilnehmen
 * zu können. Der Benutzer bedient den Client nicht direkt, sondern über ein
 * GUI. Der Client leitet alle Nachrichten und Aktionen des Benutzers an
 * seinen ClientServant weiter, und verarbeitet dessen Antworten.
 */
public class Client implements de.tu_bs.juliet.util.DownlinkOwner {

  /**
   * Speichert ankommende Nachrichten in einem Channel. Wird vom GUI benutzt
   * und kann als Protokoll der Unterhaltung in dem Channel dienen.
   */
  public Vector channelMsgBuffer = new Vector();

  /** Die hinter dem Client stehende Chatoberfläche */
  public ChatGui gui;

  /**
   * Der Uplink, über ihn werden Nachrichten gesendet.
   */
  protected Uplink uplink;

  /**
   * Der Downlink, über ihn werden Nachrichten empfangen.
   */
  protected Downlink downlink;

  /** Der Port des Servers. */
  protected int SERVER_PORT = 1500;

  /** Die IP-Adresse des Servers. */
  protected String SERVER_IP;


  /** Der Socket, über den die Verbindung zum Server aufgenommen wird. */
  protected Socket socket;

  /**
   * Vector von Strings, repräsentiert die aktuellen Benutzer in einem Wird im
   * Client Window eingebunden.
   */
  protected Vector currentUsersInChannelList;
  
  /** Der Name des Benutzers des Clients. */
  protected String currentUserName;

  /** 
   * Flag, das setzt, ob der aktuelle Benutzer Admin ist, wird auf false
   * initialisiert
   */
  protected boolean currentUserIsAdmin = false;

  /** Namensliste der Channels, die der Benutzer betreten darf. */
  protected Vector currentAllowedChannelNames;

  /** Der Name des Channels, der momentan betreten wurde. */
  protected String currentChannelName;
  

  /** klassische get-Methode: gibt den Vector channelMsgBuffer zurück */
  public Vector getChannelMsgBuffer() {
    return this.channelMsgBuffer;
  }

  /** leert den channelMsgBuffer (wird von der Gui benötigt) */
  public void clearChannelMsgBuffer() {
    this.channelMsgBuffer = new Vector();
  }

  /** klassische get-Methode: gibt den String SERVER_IP zurück */
  public String getServerIP() {
    return SERVER_IP;
  }

  /** klassische set-Methode: setzt das Attribut SERVER_IP */
  public void setServerIP(String serverIP) {
    SERVER_IP = serverIP;
  }

  /**
   * Wird gebraucht, um die Channels, die der Benutzer betreten darf
   * auf der GUI auszugeben. Dazu werden die entsprechenden Daten via Downlink
   * vom Server geholt und auf dem GUI ausgegeben. 
   */
  public synchronized final void setCurrentUserData(String userName,
          boolean isAdmin, Vector channelNames,
          String paramCurrentChannelName) {

    // setzt die übergebenen Daten in eigene Attribute
    this.currentUserName = userName;
    this.currentUserIsAdmin = isAdmin;
      // ... die übergebenen channelNames werden zunächst noch sortiert
    this.currentAllowedChannelNames = 
      de.tu_bs.juliet.util.Helper.quicksort(channelNames);
    this.currentChannelName = paramCurrentChannelName;

    /* wenn gui gesetzt, wird dort entsprechend die setCurrentUserData-
       Methode aufgerufen */
    if (this.gui != null) {
      gui.setCurrentUserData(userName, isAdmin,
                             this.currentAllowedChannelNames,
                             this.currentChannelName);
    }
  }

  /**
   * Wird benötigt, um die Teilnehmer in dem Channel auf dem GUI
   * anzuzeigen. Die Methode bekommt den Channelnamen und die Liste
   * der Benutzer vom ClientServant durch den Downlink übergeben und gibt die
   * Liste der Benutzer auf dem GUI aus. 
   */
  public synchronized final void setCurrentChannelData(String paramName,
          Vector userNames) {

    // setzt die übergebenen Daten in eigene Attribute
    this.currentChannelName = paramName;
      // ... die übergebenen userNames werden zunächst noch sortiert 
    this.currentUsersInChannelList = 
      de.tu_bs.juliet.util.Helper.quicksort(userNames);

    /* wenn gui gesetzt, wird dort entsprechend die setCurrentChannelData-
       Methode aufgerufen */
    if (this.gui != null) {
      this.gui.setCurrentChannelData(this.currentChannelName,
                                     this.currentUsersInChannelList);
    }
  }

  /**
   * Über den Downlink ist der Client in der Lage, Nachrichten vom Client-
   * Servant zu empfangen. Setzt downlink, benachrichtigt das
   * Downlinkobjekt mittels setDownlinkOwner().
   */
  public synchronized final void setDownlink (de.tu_bs.juliet.util.Downlink 
    paramDownlink) {

    if (this.downlink != paramDownlink) { 
      if (this.downlink != null) { 
        Downlink old = this.downlink; 

        this.downlink = null; // downlink wird entfernt

        old.setDownlinkOwner(null); /* das alte downlink-objekt wird
                                       benachrichtigt */
      }

      this.downlink = paramDownlink; // weist den neuen Downlink zu

      if (paramDownlink != null) { 
        paramDownlink.setDownlinkOwner(this); // setzt den downlinkowner
      }
    }
  }

  /**
   * Wird aufgerufen, falls beim Betreten eines Channels ein Fehler
   * auftritt und dieser an den Client gegeben wird. Der Benutzer
   * befindet sich nicht in dem Channel und kann durch die GUI nicht
   * interagieren. Sollte eigentlich nicht auftreten, da über die GUI
   * nur erlaubte Channel betreten werden können.
   */
  public synchronized final void joinChannelError() {
    gui.displayError("Der gewünschte Channel ist nicht verfügbar.");
  }

  /**
   * Wird aufgerufen, falls es Probleme mit dem Downlink gibt,
   * bzw. kein Objekt dieser Klasse erzeugt werden kann. Ruft stopClient() auf.
   */
  public synchronized final void downlinkError() {
    stopClient();
  }

  /**
   * Wird aufgerufen, wenn vom ClientServant beim Einloggen, die Nachricht
   * kommt, das das Einloggen nicht möglich ist. Die Methode sorgt dafür, das
   * der Benutzer eine Fehlermeldung bekommt und zum Loginscreen zurückgelangt.
   * Ruft stopClient() auf.
   */
  public synchronized final void loginError() {

    Debug.println("Client: login failed: ");
    this.gui.loginError();
    stopClient();
  }


  /**  
   * Methode wird serverseitig (über ein ErrorCommand) aufgerufen,
   * um in der GUI Fehlermeldungen und Statusmeldungen auszugeben
   */
  public synchronized final void displayError(String errorMsg) {
    this.gui.displayError(errorMsg);
  }

  /**
   * Dient dazu, eine Nachricht über das für den Client zuständige
   * Uplinkobjekt an den ClientServant zu verschicken. Dabei wird die Nachricht
   * direkt an den Uplink übergeben. Über diese Methode läuft alles, was der
   * Client an den ClientServant sendet. Benutzt uplink.sendMsg(). Bei einem
   * Fehler wird stopClient() aufgerufen.
   */
  protected synchronized final void sendCommand(Command paramCommand) {

    try {
      Uplink tmpUplink = this.uplink;

      if (tmpUplink != null) {
        this.uplink.sendMsg(paramCommand);
      }
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, "Client: " + e);
      this.stopClient();
    }
  }

  /**
   * Meldet den Benutzer an. Name und Passwort werden per Methode sendCommand
   * an den ClientServant gesendet. Sendet LoginCommand().
   */
  public synchronized final void login(String name, String password) {
    this.sendCommand(new LoginCommand(name, password));
  }

  /**
   * Meldet einen Gast an. Die Methode funktioniert wie die normale Methode
   * login, jedoch wird kein Passwort erwartet. Sendet LoginGuestCommand().
   */
  public synchronized final void loginAsGuest(String name) {
    Debug.println("Client: loginGuest " + name);
    this.sendCommand(new LoginGuestCommand(name));
  }

  /**
   * Betritt den angegebenen Channel. Das entpechende Command mit dem
   * Channelnamen wid an den ClientSevant gesendet und dort mit den
   * entsprechenden Methoden verarbeitet. Benutzt JoinChannelCommand()
   */
  public synchronized final void joinChannel(String name) {
    this.sendCommand(new JoinChannelCommand(name));
  }

  /**
   * Meldet den Benutzer ab. Die Methode sorgt dafür, das der Benutzer zum
   * Loginscreen zuückgelangt. Sendet ein LogoutCommand()
   */
  public synchronized final void logout() {
    this.sendCommand(new LogoutCommand());
  }

  /**
   * Sendet eine Nachricht an einen einzigen Benutzer. Die Nachricht, sowie
   * der Name des Empfängrs (name) wird an die Methode übergeben, die sie dann
   * via Uplink an den ClientServant sendet. Sendet sendMsgToUserCommand.
   */
  public synchronized final void sendMsgToUser(String name, String msg) {
    this.sendCommand(new SendMsgToUserCommand(name, msg));
    // füllt das Protokoll mit den abgeschickten Nachrichten
    this.channelMsgBuffer.addElement("An " + name + ": " + msg);
  }

  /**
   * Sendet eine Nachricht in einen Channel, die von allen Teilnehmern
   * in diesem Channel empfangen werden kann. Die Nachricht wird an die
   * Methode übergeben und durch anlegen eines neuen Objekts SendMsgToChannel
   * an den ClientServant gesendet.
   */
  public synchronized final void sendMsgToChannel(String msg) {
    this.sendCommand(new SendMsgToChannelCommand(msg));
  }

  /**
   * Verarbeitet eine empfangene Nachricht bzw. führt den empfangenen Befehl
   * einfach aus. Nachrichten vom Server, die durch den Downlink empfangen
   * werden, werden hier als Parameter eingesetzt.
   */
  public synchronized final void processMsg(Command msg) {
    msg.execute(this);
  }

  /**
   * Dient dazu, eine Nachricht von einem einzigen Benutzer (fromName),
   * die für den gesamten Channel bestimmt ist, zu empfangen. Die Nachricht
   * wird vom Downlink an diese Methode übergeben und sie sorgt dafür, daß der
   * empfangene Text in der GUI erscheint und zum channelMsgBuffer hinzugefügt
   * wird.
   */
  public synchronized final void sendMsgFromChannel(String fromName,
    String msg) {

    Debug.println("Client: " + fromName + " sayz:" + msg);
    this.channelMsgBuffer.addElement(fromName + ": " + msg);

    if (this.gui != null) {
      this.gui.sendMsgFromChannel(fromName, msg);
    }
  }

  /**
   * Dient dazu, eine Nachricht von einem einzigen Benutzer (fromName)
   * zu empfangen. Die Nachricht wird vom Downlink an diese Methode
   * übergeben und sie sorgt dafür, daß der empfangene Text in der GUI
   * erscheint und zum channelMsgBuffer hinzugefügt wird.
   */
  public synchronized final void sendMsgFromUser(String fromName,
    String msg) {

    Debug.println("Client: " + fromName + " whispers: " + msg);
    this.channelMsgBuffer.addElement(fromName + " flüstert: " + msg);

    if (this.gui != null) {
      this.gui.sendMsgFromUser(fromName, msg);
    }
  }

  /**
   * Dient dazu, einen Client am Server anzumelden. Dazu wird für den neuen
   * Client ein Socket am Server geöffnet, sowie ein Uplink Objekt und ein
   * Downlink Objekt zur Kommunikation mit dem ClientServant. Benutzt
   * setDownlink, downlink.startDownlink() und uplink.startUplink().
   */
  public synchronized final void startClient() {

    Debug.println(Debug.MEDIUM, "Client: starting");

    try {
      socket = new Socket(SERVER_IP, SERVER_PORT);
      uplink = new de.tu_bs.juliet.util.Uplink(socket);
      setDownlink(new de.tu_bs.util.Downlink(socket, this));
      uplink.startUplink();
      Debug.println("Client: Uplink started. Trying to start downlink...");
      downlink.startDownlink();
      Debug.println("Client: Downlink started.");
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, "Client: error starting: " + e);
      this.stopClient();
    }
  }

  /**
   * Trennt die Verbindung des Clients zum Server. Dazu werden die Referenzen
   * auf das Uplink- bzw. Downlinkobjekt entfernt und eine entsprechende
   * Meldung auf dem GUI ausgegeben.
   */
  public synchronized final void stopClient() {

    // wenn der uplink noch offen ist, wird er gestoppt und null gesetzt
    if (uplink != null) {
      this.uplink.stopUplink();

      this.uplink = null;
    }
    
    // veranlasst ein Stoppen des Downlinks
    this.setDownlink(null);
    
    // leert/schließt die Fenster der grafischen Oberfläche
    this.gui.clearGui();
  }
}
