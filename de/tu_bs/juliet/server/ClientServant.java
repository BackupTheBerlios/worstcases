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

import java.net.Socket;
import java.util.Enumeration;

import de.tu_bs.juliet.util.*;
import de.tu_bs.juliet.util.debug.Debug;
import de.tu_bs.juliet.util.commands.*;
import de.tu_bs.juliet.util.Helper;


/**
 * Diese Klasse kümmert sich um die Anfragen, 
 * die von einem Client an den Server gestellt werden. Der Server
 * erzeugt für jeden Client eine Instanz dieser Klasse, 
 * die sich von da an nur
 * noch um diesen Client kümmert und seine Anfragen bearbeitet. 
 * In dieser Klasse steckt die meiste Funktionalität des Servers.
 */
public class ClientServant implements Util.DownlinkOwner {

  /** Gibt den letzten Zeitpunkt an, an dem der ClientServant 
    * eine Nachricht von seinem Client empfangen hat. 
    * Wird die Zeitspanne zu gross, so wird der ClientServant
    * vom ClientServantWatchDog entfernt
    */
  protected long aliveStamp = java.lang.System.currentTimeMillis();

  protected Downlink downlink;

  protected Uplink uplink;

  protected User user;

  protected UserAdministration userAdministration;

  protected Server server;

  protected Socket socket;

  /** Standard-Konstruktor, notwendig, 
    * da der AdminClientServant von dieser Klasse erbt. 
    * Wird sonst <em>nicht</em> benutzt. */
  public ClientServant() {}

  /** Konstruktor, der die entsprechenden Attribute setzt. 
    * Benutzt setServer(paramServer). */
  public ClientServant(Socket paramSocket, Server paramServer,
                       UserAdministration paramUserAdministration) {

    this.socket = paramSocket;

    this.setServer(paramServer);

    this.userAdministration = paramUserAdministration;
  }

  /** Gibt den aktuellen aliveStamp-Wert zurück. */
  public final long getAliveStamp() {
    return this.aliveStamp;
  }

  /** Setzt aliveStamp auf die aktuelle Zeit. 
    * Benutzt java.lang.System.currentTimeMillis(). */
  public final synchronized void setAliveStamp() {
    this.aliveStamp = java.lang.System.currentTimeMillis();
  }

  /**
   * Setzt downlink, benachrichtigt die betroffenen Downlinkobjekte 
   * mittels setDownlinkOwner().
   * Falls der ClientServant NACH dem Aufruf keinen Downlink mehr besitzt, 
   * so wird stopClientServant() aufgerufen.
   */
  public final void setDownlink(Downlink paramDownlink) {

    if (this.downlink != paramDownlink) {
      if (this.downlink != null) {
        Downlink old = this.downlink;

        this.downlink = null;

        old.setDownlinkOwner(null);
      }

      this.downlink = paramDownlink;

      if (paramDownlink != null) {
        paramDownlink.setDownlinkOwner(this);
      } else {
        this.stopClientServant();
      }
    }
  }

  /**
   * Wird vom Downlink aufgerufen, falls beim Empfang von Nachrichten 
   * ein Fehler auftritt, enthält Fehlerbehandlung (stopClientServant())
   * @see Util.DownlinkOwner
   */
  public final void downlinkError() {

    Debug.println(Debug.HIGH,
                  "ClientServant: downlink error: stopping clientServant");
    this.stopClientServant();
  }

  /**
   * Setzt user, benutzt user.setClientServant(). 
   * Falls der ClientServant NACH dem Aufruf keinen User mehr besitzt, 
   * so wird stopClientServant() aufgerufen.
   */
  public final void setUser(User paramUser) {

    if (this.user != paramUser) {
      if (this.user != null) {
        User old = this.user;

        this.user = null;

        old.setClientServant(null);
      }

      this.user = paramUser;

      if (paramUser != null) {
        paramUser.setClientServant(this);
      } else {
        this.stopClientServant();
      }
    }
  }

  /**
   * Startet den ClientServant, danach ist er betriebsbereit und 
   * kann die Anfragen seines Clients bearbeiten.
   * Erzeugt einen neuen Uplink und Downlink. 
   * Benutzt setDownlink(), uplink.startUplink() 
   * und downlink.startDownlink().
   * Fängt Fehler aus startUplink() und startDownlink() ab. 
   * Bei Fehlern wird stopClientServant() aufgerufen.
   */
  public final void startClientServant() {

    // uplink und downlink starten
    try {
      this.uplink = new Uplink(this.socket);

      this.uplink.startUplink();
      this.setDownlink(new Downlink(this.socket, this));
      this.downlink.startDownlink();
    }

    // bei einem Fehler (aus uplink.startUplink()) stopClientServant()
    catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, "ClientServant: error starting uplink:" + e);
      this.stopClientServant();
    }

    Debug.println(Debug.MEDIUM, "ClientServant: started");
  }

  /**
   * Stoppt den ClientServant. Benutzt setDownlink(null) uplink.stopUplink(), 
   * setServer(null) und setUser(null).
   * Sendet ein stopClientCommand() an den Client.
   */
  public final void stopClientServant() {

    Uplink old = this.uplink;

    // verhindert, daß stopClientServant mehrmals läuft
    this.uplink = null;

    // Versuch, ein StopClientCommand() zu senden
    if (old != null) {
      Debug.println(Debug.MEDIUM, "ClientServant: stopping");

      try {
        old.sendMsg(new StopClientCommand());
      } catch (java.io.IOException e) {
        Debug.println(Debug.HIGH,
                      "ClientServant: error sending stopClient:" + e);
      }

      old.stopUplink();
    }

    // assocs lösen
    this.setDownlink(null);
    this.setServer(null);
    this.setUser(null);
  }

  /** Führt den empfangenen Befehl einfach mittels msg.execute(this) aus. 
    * Benutzt setAliveStamp(). */
  public final synchronized void processMsg(Command msg) {

    if (msg != null) {
      msg.execute(this);
      this.setAliveStamp();
    }
  }

  /** Sendet das angegebene CommandObjekt über den Uplink. 
    * Benutzt uplink.sendMsg() Bei einem Fehler: Fehlerbehandlung. 
    * (stopClientServant())
    */
  public final void sendCommand(Command paramCommand) {

    try {
      Uplink old = this.uplink;

      if (old != null) {
        old.sendMsg(paramCommand);
      }
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, "ClientServant: error while sending:" + e);
      this.stopClientServant();
    }
  }

  /**
   * Meldet den Benutzer mit Namen name und mit Passwort password 
   * beim System an.
   * Benutzt userAdministration.loginUser(),
   * um das Userobjekt zu holen und setUser() um es zu setzen.
   * Bei einem Loginfehler wird ein LoginErrorCommand gesendet 
   * und setUser(null) aufgerufen, anschließend beendet sich der
   * clientServant. Falls der User Admin-Rechte hat, 
   * so wird becomeAdminClient() aufgerufen.
   */
  public final void loginUser(String name, String password) {

    UserAdministration tmpUserAdministration = this.userAdministration;

    if (tmpUserAdministration != null) {
      User tmpUser = tmpUserAdministration.loginUser(name, password);

      /* User mit diesen Daten nicht vorhanden,
       * LoginErrorCommand senden, setUser(null) */       
      if (tmpUser == null) {
        this.sendCommand(new Util.Commands.LoginErrorCommand());
        this.setUser(null);
      }
      
      //User vorhanden, sendCurrentUserData() aufrufen 
      else {
        this.setUser(tmpUser);
        this.sendCurrentUserData();
        
        //User ist Admin, becomeAdminClientServant() aufrufen
        if (tmpUser.isAdmin()) {
          this.becomeAdminClientServant();
        }
      }
    }
  }

  /**
   * Meldet einen Gast beim System an. 
   * Benutzt userAdministration.loginGuest() und setUser()
   * Bei einem Loginfehler wird ein LoginErrorCommand gesendet 
   * und setUser(null) aufgerufen anschließend
   * beendet sich clientServant. 
   * Ruft nach erfolgreicher Anmeldung sendCurrentUserData auf.
   */
  public final void loginAsGuest(String name) {

    UserAdministration tmpUserAdministration = this.userAdministration;
    User tmpUser = tmpUserAdministration.loginGuest(name + " [Gast]");

    // Gastanmeldung nicht gelungen, Fehler senden
    if (tmpUser == null) {
      this.sendCommand(new Util.Commands.LoginErrorCommand());
      this.setUser(null);
    }

    // Anmeldung erfolgreich, Client benachrichtigen
    else {
      this.setUser(tmpUser);
      this.sendCurrentUserData();
    }
  }

  /**
   * Wenn der angemeldete Benutzer Admin-Rechte hat, 
   * dann wird aus dem ClientServant automatisch ein AdminClientServant
   * mit erweiterter Funktionalität. 
   * Benutzt den AdminClientServant-Konstruktor, um alle "Resourcen" auf
   * den AdminClientServant zu übertragen
   * Setzt uplink=null und ruft dann this.stopClientServant() auf.
   */
  public final void becomeAdminClientServant() {

    Debug.println(Debug.MEDIUM, "ClientServant: becoming AdminClientServant");

    //Attribute zwischenspeichern
    Uplink oldUplink = this.uplink;
    Downlink oldDownlink = this.downlink;
    User oldUser = this.user;

    /*
     * notwendig, damit die in dem Konstruktor AdminClientServant()
     * verwendeten set-Methoden nicht zu einem 
     * vorzeitigen stopClientServant() 
     * führen - was sonst eigentlich erwünscht ist
     */
    this.uplink = null;
    this.downlink = null;
    this.user = null;

    /*
     * der AdminClientServant - Konstruktor bewirkt über setMethoden, 
     * dass dieser ClientServant beendet wird
     */
    AdminClientServant tmpAdminClientServant =
      new AdminClientServant(oldUplink, oldDownlink, this.server,
                             this.server.getChannelAdministration(),
                             this.userAdministration, oldUser,
                             this.server.getDataBaseIO());
    
    //sicherheitshalber :)
    this.stopClientServant();
  }

  /**
   * Lässt den User in den Channel mit dem angegebenen Namen eintreten.
   * Benutzt user.setCurrentChannel() und user.getFromAllowedChannelByName().
   * Falls der gewünschte Channel nicht existiert,
   * wird ein JoinChannelErrorCommand gesendet
   * Falls der user nicht existiert, wird ein LoginErrorCommand gesendet.
   */
  public final void joinChannel(String name) {

    User old = this.user;

    // User vorhanden
    if (old != null) {
      Channel tmpChannel = old.getFromAllowedChannelByName(name);

      // Channel existent
      if (tmpChannel != null) {
        old.setCurrentChannel(tmpChannel);
      }

      // Channel nicht existent
      else {
        this.sendCommand(new JoinChannelErrorCommand());
      }
    }

    // User nicht vorhanden
    else {
      this.sendCommand(new LoginErrorCommand());
    }
  }

  /**
   * Lässt den User den Channel verlassen. 
   * Benutzt user.setCurrentChannel(null)
   * Falls user nicht existent, so wird LoginErrorCommand() gesendet.
   */
  public final void leaveChannel() {

    User old = this.user;

    if (old != null) {
      old.setCurrentChannel(null);
    } else {
      this.sendCommand(new LoginErrorCommand());
    }
  }

  /**
   * Sendet eine Nachricht des Users an alle anderen User im Channel. 
   * Sendet ggf. joinChannelError(),loginErrorCommand()
   * Benutzt user.getCurrentChannel() und Channel.getCurrentUserEnum(), 
   * getClientServant(),
   * um bei den verantwortlichen ClientServants ein sendMsgFromChannel() 
   * aufzurufen.
   */
  public final void sendMsgToChannel(String msg) {

    User oldUser = this.user;

    if (oldUser != null) {
      Channel oldChannel = oldUser.getCurrentChannel();

      // currentChannel vorhanden
      if (oldChannel != null) {
        Enumeration enum = oldChannel.getCurrentUserEnum();
        User tmpUser;
        ClientServant tmpClientServant;

        /* alle ClientServants der User im Channel 
         * zu einem sendMsgFromChannel veranlassen*/
        while (enum.hasMoreElements()) {
          tmpUser = (User) enum.nextElement();
          tmpClientServant = tmpUser.getClientServant();

          if (tmpClientServant != null) {
            tmpClientServant.sendMsgFromChannel(oldUser.getName(), msg);
          }
        }
      }

      // kein currentChannel vorhanden
      else {
        this.sendCommand(new JoinChannelErrorCommand());
      }
    }

    // Userobjekt nicht vorhanden
    else {
      this.sendCommand(new LoginErrorCommand());
    }
  }

  /**
   * Sendet eine Nachricht, die in den besuchten Channel gesendet wurde, 
   * an den Client (mit einem SendMsgFromChannelCommand).
   * @param fromName Name des Absenders
   * @param msg Nachricht
   */
  public final void sendMsgFromChannel(String fromName, String msg) {
    this.sendCommand(new Util.Commands.SendMsgFromChannelCommand(fromName,
            msg));
  }

  /**
   * Sendet eine private Nachricht eines anderen Users an den Client. 
   * Sendet ein SendMsgFromUserCommand.
   * @param fromName Name des Absenders
   * @param msg Nachricht
   */
  public final void sendMsgFromUser(String fromName, String msg) {
    this.sendCommand(new Util.Commands.SendMsgFromUserCommand(fromName, msg));
  }

  /**
   * Sendet eine private Nachricht des Users an einen anderen User. 
   * Sendet ggf. ein joinChannelErrorCommand, loginErrorCommand()
   * Benutzt user.getCurrentChannel() und Channel.getCurrentUserEnum(), 
   * getClientServant(),
   * um bei dem verantwortlichen ClientServant ein 
   * sendMsgFromChannel() aufzurufen.
   */
  public final void sendMsgToUser(String userName, String msg) {

    User old = this.user;

    // Userobjekt vorhanden
    if (old != null) {
      Channel currentChannel = old.getCurrentChannel();

      // currentChannel vorhanden
      if (currentChannel != null) {
        Enumeration enum = currentChannel.getCurrentUserEnum();
        User tmpUser;

        // User mit userName in den Userobjekten des currentChannel suchen
        while (enum.hasMoreElements()) {
          tmpUser = (User) enum.nextElement();

          // User hat den richtigen Namen
          if (tmpUser.getName().compareTo(userName) == 0) {
            ClientServant tmpClientServant = tmpUser.getClientServant();

            // die Nachricht über den verantwortlichen ClientServant absetzen
            if (tmpClientServant != null) {
              tmpClientServant.sendMsgFromUser(old.getName(), msg);
            }
          }
        }
      }

      // kein currentChannel
      else {
        this.sendCommand(new JoinChannelErrorCommand());
      }
    }

    // kein Userobjekt
    else {
      this.sendCommand(new LoginErrorCommand());
    }
  }

  //sendet ein ErrorCommand an den Client
  public final void sendErrorMsg(String msg) {
    this.sendCommand(new ErrorCommand(msg));
  }

  /**
   * Sendet die Daten des betretenen Channel an den Client. 
   * Sendet ein SetCurrentChannelDataCommand.
   * Benutzt getCurrentChannel().getName() 
   * und getCurrentChannel().getCurrentUserNames().
   */
  public final void sendCurrentChannelData() {

    User old = this.user;

    if (old != null) {
      Channel currentChannel = old.getCurrentChannel();

      if (currentChannel != null) {
        this.sendCommand(new Util.Commands
          .SetCurrentChannelDataCommand(currentChannel
            .getName(), currentChannel.getCurrentUserNames()));
      }
    }
  }

  /**
   * Sendet die Daten des Users an den Client. 
   * Sendet ein SetCurrentUserDataCommand.
   * Benutzt user.getName() und user.getAllowedChannelNames().
   */
  public final void sendCurrentUserData() {

    User old = this.user;
    String currentChannelName;
    Channel tmpChannel = old.getCurrentChannel();

    if (tmpChannel != null) {
      currentChannelName = tmpChannel.getName();
    } else {
      currentChannelName = null;
    }

    if (old != null) {
      this.sendCommand(new Util.Commands
        .SetCurrentUserDataCommand(old.getName(), old.isAdmin(), old
          .getAllowedChannelNames(), currentChannelName));
    }
  }

  /**
   * Setzt server und benachrichtigt das betroffene Serverobjekt 
   * mittels removeFromClientServantList() und
   * addToClientServantList. setServer(null) bewirkt stopClientServant()
   */
  public final void setServer(Server paramServer) {

    if (this.server != paramServer) {
      if (this.server != null) {
        Server oldServer = this.server;

        this.server = null;

        oldServer.removeFromClientServantList(this);
      }

      this.server = paramServer;

      if (paramServer != null) {
        paramServer.addToClientServantList(this);
      } else {
        this.stopClientServant();
      }
    }
  }
}

