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

import java.util.Vector;
import java.util.Enumeration;
import de.tu_bs.juliet.util.*;
import de.tu_bs.juliet.util.Commands.*;


/** Verarbeitet die Anfragen eines AdminClients. */
public class AdminClientServant extends ClientServant
        implements DownlinkOwner {
   
  private ChannelAdministration channelAdministration;

  private DataBaseIO dataBaseIO;


  /**
   * Konstruktor, setzt die entsprechenden Attribute. 
   * Benutzt setDownlink(), setServer() und setUser().
   * "Entreisst" somit dem aufrufenden ClientServant die Objekte.
   */
  public AdminClientServant(Uplink paramUplink, Downlink paramDownlink,
                            Server paramServer,
                            ChannelAdministration paramChannelAdministration, 
                            UserAdministration paramUserAdministration,
                            User paramUser, DataBaseIO paramDataBaseIO) {

    //Die benötigten Referenzen setzen	
    this.setUser(paramUser);
    this.setDownlink(paramDownlink);
    this.uplink = paramUplink;
    this.setServer(paramServer);
    this.channelAdministration = paramChannelAdministration;
    this.userAdministration = paramUserAdministration;
    this.dataBaseIO = paramDataBaseIO;
  }

  /**
   * Sendet eine Liste aller Channelnamen. 
   * Benutzt sendCommand() und erzeugt ein neues 
   * SetChannelListCommand - Objekt.
   * Benutzt channelAdministration.getChannelNames(), 
   * um die Namensliste zu erzeugen.
   */
  public void sendChannelList() {
    this.sendCommand(new SetChannelListCommand(this.channelAdministration
      .getChannelNames()));
  }
  
  /**
   * Sendet die Channeldaten des Channels mit dem angegebenen Namen.
   * Erzeugt und versendet ein neues SetChannelDataCommand(). 
   * Benutzt channelAdministration.getFromChannelListByName().
   */
  public void sendChannel(String channelName) {

    Channel tmpChannel =
      this.channelAdministration.getFromChannelListByName(channelName);

    if (tmpChannel != null) {
      String tmpName = tmpChannel.getName();
      boolean tmpIsAllowedForGuest = tmpChannel.isAllowedForGuest();
      Vector tmpAllowedUserList = tmpChannel.getAllowedUserNames();

      this.sendCommand(new SetChannelDataCommand(tmpName,
                                                 tmpIsAllowedForGuest,
                                                 tmpAllowedUserList));
    }
  }
  

  /**
   * Sendet eine Liste aller Benutzernamen. 
   * Benutzt sendCommand und erzeugt ein neues 
   * SetUserListCommand-Objekt
   * Benutzt userAdministration.getUserNames(), 
   * um die Namensliste zu erzeugen.
   */
  public void sendUserList() {
    // man beachte, daß getUserNames() nur nicht - Gäste Namen liefert
    this.sendCommand(new SetUserListCommand(this.userAdministration
      .getUserNames()));
  }

  /**
   * Sendet die Benutzerdaten des Benutzers mit dem angegebenen Namen 
   * an den Client.
   * Benutzt userAdministration.getFromUserListByName(). 
   * Erzeugt und versendet ein entsprechendes SetUserDataCommand().
   */
  public void sendUser(String userName) {

    User tmpUser = this.userAdministration.getFromUserListByName(userName);

    if (tmpUser != null) {
      String tmpName = tmpUser.getName();
      String tmpPassword = tmpUser.getPassword();
      boolean tmpIsAdmin = tmpUser.isAdmin();
      Vector tmpAllowedChannelList = tmpUser.getAllowedChannelNames();

      this.sendCommand(new Util.Commands.SetUserDataCommand(tmpName,
              tmpPassword, tmpIsAdmin, tmpAllowedChannelList));
    }
  }


  /**
   * Fügt einen Channel hinzu. Erzeugt ein neues Channelobjekt 
   * und generiert für dieses Channelobjekt die Referenzen auf die
   * erlaubten Benutzer mittels userAdministration.getFromUserListByName() 
   * und channel.addToAllowedUserList().
   * Prüft, ob der Channel mit dem angegebenen Namen noch nicht existiert.
   * Fügt automatisch alle Benutzer zu einem Channel hinzu, der für Gäste
   * freigegeben ist.
   * Bewirkt Aufruf von DataBaseIO.saveToDisk().
   * @param paramName Name des Channels
   * @param paramAllowedForGuests Flag, ob Gäste den Channel betreten dürfen
   * @param paramAllowedUserNames Vector von Strings - 
   * die Namen der Userobjekte, die den Channel betreten dürfen
   */
  public void addChannel(String paramName, boolean paramAllowedForGuests,
                         Vector paramAllowedUserNames) {
    // "faule" Auswertung!
    if ((paramName != null) && (paramName.compareTo("") != 0)
            && (this.channelAdministration.getFromChannelListByName(paramName)
                == null)) {
      Channel tmpChannel = new Channel(paramName, paramAllowedForGuests);
      Enumeration enum;
      /*
       * wenn der Channel nicht für Gäste freigegeben ist, dann
       * benutze paramAllowedUserNames als Liste der berechtigten Benutzer
       */
      if (!paramAllowedForGuests) {
        if (paramAllowedUserNames != null) {
          enum = paramAllowedUserNames.elements();
        } else {
          enum = (new Vector()).elements();
        }
        while (enum.hasMoreElements()) {
          tmpChannel
            .addToAllowedUserList(this.userAdministration
              .getFromUserListByName((String) enum.nextElement()));
        }
      }
      // Channel ist für Gäste freigegeben, erlaube alle Benutzer
      else {
        tmpChannel.setAllowedUserList(this.userAdministration.getUserEnum());
      }
      this.channelAdministration.addToChannelList(tmpChannel);
      this.dataBaseIO.saveToDisk();
    }
  }

  /**
   * Löscht den Channel mit dem angegebenen Namen. 
   * Benutzt channelAdministration.getFromChannelListByName()
   * und channelAdministration.removeFromChannelList(). 
   * Bewirkt Aufruf von DataBaseIO.saveToDisk()
   * Ignoriert deleteChannel(FOYERNAME).
   */
  public void deleteChannel(String channelName) {
    if (channelName != null) {
      // das Foyer darf nicht gelöscht werden
      if (channelName.compareTo(this.channelAdministration.FOYERNAME) != 0) {
        Channel tmpChannel =
          this.channelAdministration.getFromChannelListByName(channelName);
        this.channelAdministration.removeFromChannelList(tmpChannel);
        this.dataBaseIO.saveToDisk();
      }
    }
  }

  /**
   * Verändert die Daten des angegebenen Channels. 
   * Benutzt setAllowedUserList(), um die erlaubten Benutzer 
   * des Channels einzutragen
   * und ruft dann channelAdministration.editChannel() auf. 
   * Bewirkt Aufruf von DataBaseIO.saveToDisk()
   * @param oldName alter Name des Channels
   * @param newName neuer Name des Channels
   * @param paramAllowedForGuest Flag - 
   * ob Gäste den Channel betreten dürfen
   * @param allowedUserNames Vector von Strings - 
   * Liste der Usernamen, die den Channel betreten dürfen
   */
  public void editChannel(String oldName, String newName,
                          boolean paramAllowedForGuest,
                          Vector allowedUserNames) {
    //enthält die berechtigten User - Objekte
    Vector tmpUsers = new Vector();
    Enumeration enum;
    
    if (allowedUserNames != null) {
      enum = allowedUserNames.elements();
    } else {
      enum = (new Vector()).elements();
    }
    
    //die Benutzer - Objekte mittels getFromUserListByName() ermitteln
    while (enum.hasMoreElements()) {
      tmpUsers
        .addElement(this.userAdministration
          .getFromUserListByName((String) enum.nextElement()));
    }

    //Channel ist nicht für Gäste freigegeben, benutze tmpUsers
    if (!paramAllowedForGuest) {
      this.channelAdministration.editChannel(oldName, newName,
                                             paramAllowedForGuest,
                                             tmpUsers.elements());
    /* Channel ist für Gäste freigegeben, ignoriere tmpUsers.                                         
     * Berechtige alle Benutzer zum Betreten des Channels
     */
    } else {
      this.channelAdministration
        .editChannel(oldName, newName, paramAllowedForGuest,
                     this.userAdministration.getUserEnum());
    }

    this.dataBaseIO.saveToDisk();
  }

  /**
   * Fügt einen Benutzer hinzu. 
   * Erzeugt ein neues Userobjekt mit den angegebenen Daten. 
   * Benutzt paramAllowedChannelNames und
   * channelAdministration.getFromChannelListByName(), 
   * um mit user.addToAllowedChannelList() die für den Benutzer erlaubten
   * Channelobjekte zu referenzieren. 
   * Ruft schließlich userAdministration.addToUserList auf.
   * Prüft, ob ein Benutzer mit demselben Namen existiert.
   * Stellt sicher, dass ein Benutzer alle Gastchannels betreten darf.
   * Admins erhalten Zugriff auf alle Channels.
   * Bewirkt Aufruf von DataBaseIO.saveToDisk().
   */
  public void addUser(String paramName, String paramPassword,
                      boolean paramIsAdmin, Vector paramAllowedChannelNames) {

    // "faule" Auswertung!
    if ((paramName != null) && (paramPassword != null)
            && (paramName.compareTo("") != 0)
            && (paramPassword.compareTo("") != 0)
            && (this.userAdministration.getFromUserListByName(paramName)
                == null)) {
      User tmpUser = new User(paramName, paramPassword, false, paramIsAdmin,
                              this.userAdministration);

      /*
       * Gewährt Zugriff auf alle Channel,
       * die für Gäste freigegeben sind
       */
      tmpUser
        .setAllowedChannelList(this.channelAdministration
          .getFreeForGuestEnum());

      Enumeration enum;

      if (paramAllowedChannelNames != null) {
        enum = paramAllowedChannelNames.elements();
      } else {
        enum = (new Vector()).elements();
      }

      /*
       * Zusätzlich zu den Gastchannels die Channel
       * aus paramAllowedChannelNames hinzufügen
       */
      while (enum.hasMoreElements()) {
        tmpUser
          .addToAllowedChannelList(this.channelAdministration
            .getFromChannelListByName((String) enum.nextElement()));
      }

      /*
       * falls der neue Benutzer Admin - Rechte hat,
       * dann berechtige zum Betreten aller Channel
       */
      if (paramIsAdmin) {
        tmpUser
          .setAllowedChannelList(this.channelAdministration.getChannelEnum());
      }

      this.userAdministration.addToUserList(tmpUser);
      this.dataBaseIO.saveToDisk();
    }
  }

  /**
   * Löscht den Benutzer mit dem angegebenen Namen. 
   * Benutzt userAdministration.getFromUserListByName() und
   * userAdministration.removeFromUserList(). 
   * Bewirkt Aufruf von DataBaseIO.saveToDisk().
   */
  public void deleteUser(String userName) {

    User tmpUser = this.userAdministration.getFromUserListByName(userName);
    //removeFromUserList() prüft alles Notwendige
    this.userAdministration.removeFromUserList(tmpUser);
    this.dataBaseIO.saveToDisk();
  }

  /**
   * Verändert die Daten des angegebenen Users. 
   * Erzeugt ein neues Userobjekt mit den angegebenen Daten.
   * Benutzt setAllowedChannelList(), 
   * um die erlaubten Channels des Benutzers einzutragen -
   * benutzt dafür channelAdministration.getFromChannelListByName() 
   * und ruft dann userAdministration.editUser() auf.
   * Bewirkt Aufruf von DataBaseIO.saveToDisk().
   * Stellt sicher, dass Admin alle Channels und Benutzer alle Gastchannels
   * betreten dürfen.
   * @param oldName alter Name des Users
   * @param newName neuer Name des Users
   * @param newPassword neues Passwort
   * @param paramIsAdmin Flag - ob User Admin-Rechte hat
   * @param allowedChannelNames Vector von Strings - 
   * Liste der Channelnamen, die der Benutzer betreten darf
   */
  public void editUser(String oldName, String newName, String newPassword,
                       boolean paramIsAdmin, Vector allowedChannelNames) {
    //enthält die für den Benutzer erlaubten Channels
    Vector tmpChannels = new Vector();
    Enumeration enum;

    if (allowedChannelNames != null) {
      enum = allowedChannelNames.elements();
    } else {
      enum = (new Vector()).elements();
    }

    //Füge die Channel - Objekte mit den Namen aus allowedChannelNames hinzu
    while (enum.hasMoreElements()) {
      tmpChannels
        .addElement(this.channelAdministration
          .getFromChannelListByName((String) enum.nextElement()));
    }

    /* jeder User darf auch die Channels betreten, 
     * die für Gäste freigegeben sind*/
    enum = this.channelAdministration.getFreeForGuestEnum();

    //Füge die Gastchannels hinzu
    while (enum.hasMoreElements()) {
      tmpChannels.addElement((Channel) enum.nextElement());
    }

    //normale Benutzer erhalten die Channels aus tmpChannels
    if (!paramIsAdmin) {
      this.userAdministration.editUser(oldName, newName, newPassword,
                                       paramIsAdmin, tmpChannels.elements());
    //Admins erhalten alle Channels                                   
    } else {
      this.userAdministration
        .editUser(oldName, newName, newPassword, paramIsAdmin,
                  this.channelAdministration.getChannelEnum());
    }

    this.dataBaseIO.saveToDisk();
  }
}
