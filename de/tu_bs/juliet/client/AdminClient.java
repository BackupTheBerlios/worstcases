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
import java.util.Enumeration;
import java.net.Socket;

import de.tu_bs.juliet.util.*;
import de.tu_bs.juliet.util.debug.*;
import de.tu_bs.juliet.util.commands.*;


/**
 * Die Klasse AdminClient liefert dem Administrator die nötigen Methoden,
 * um auf die Benutzer- und Channelverwaltung zuzugreifen, um also
 * Benutzer und Channels anzulegen, zu editieren und zu löschen.
 */
public class AdminClient extends Client {

  /** Liste aller Channelnamen. */
  private Vector channelList = new Vector();

  /** Channeldaten: alter Channelname (wird beim Editieren benötigt). */
  private String tmpOldChannelName;

  /** Channeldaten: Channelname, zur Bearbeitung. */
  private String tmpChannelName;

  /** Channeldaten: Flag öffentlich ja/nein, zur Bearbeitung. */
  private boolean tmpAllowedForGuests;

  /** Channeldaten: Liste der berechtigten Benutzer, zur Bearbeitung. */
  private Vector tmpAllowedUserNames = new Vector();

  /** Liste aller Benutzernamen. */
  private Vector userList = new Vector();

  /** Userdaten: alter Username (wird beim Editieren benötigt). */
  private String tmpOldUserName;

  /** Userdaten: Username, zur Bearbeitung. */
  private String tmpUserName;

  /** Userdaten: Passwort, zur Bearbeitung. */
  private String tmpPassword;

  /** Userdaten: Flag ist Admin ja/nein, zur Bearbeitung. */
  private boolean tmpIsAdmin;

  /** Userdaten: Liste der aktiven Channels, zur Bearbeitung. */
  private Vector tmpAllowedChannelNames = new Vector();

  /** klassische get-Methode: gibt den Inhalt des Attributs userList zurück */
  public Vector getUserListAttribute() {
    return this.userList;
  }

  /** klassische get-Methode: gibt den alten Channelnamen zurück */
  public String getTmpOldChannelName() {
    return this.tmpOldChannelName;
  }

  /** klassische get-Methode: gibt den alten Benutzernamen zurück */
  public String getTmpOldUserName() {
    return this.tmpOldUserName;
  }

  /** 
   * Wird vom SetUserDataCommand aufgerufen und setzt die Userdaten auf die 
   * entsprechenden Attribute. Außerdem erzeugt die Methode ein Diff aus
   * aktiven Channels und allen in channelList gesetzten Channels, um
   * in der GUI die passiven Channels ausgeben zu können.
   */
  public void setUserData(String userName, String password, boolean isAdmin,
                          Vector channelNames) {
    
    // die übergebenen Daten werden in eigenen Attributen gesetzt 
    this.tmpUserName = userName;
    this.tmpOldUserName = userName;
    this.tmpPassword = password;
    this.tmpIsAdmin = isAdmin;
    this.tmpAllowedChannelNames = channelNames;
    
    // Vector, der mit den Namen der passiven Channels gefüllt werden soll
    Vector tmpChannelDiffList = new Vector();
    String tmpChannelName = new String();
    Enumeration enum = this.channelList.elements();

    /* vergleicht channelList mit channelNames (dort sind die allowed
       channels gespeichert) und fügt alle channel, die in channelList
       aber nicht in channelNames enthalten sind, zur tmpChannelDiffList
       (in der die disallowed channels gespeichert werden) hinzu. */
    while (enum.hasMoreElements()) { 
      tmpChannelName = (String) enum.nextElement();

      if (!channelNames.contains(tmpChannelName)) {
        tmpChannelDiffList.addElement(tmpChannelName);
      }
    }
    
    // setzt die Daten in der GUI, nachdem diese sortiert wurden
    this.gui.setUserData(userName, password, isAdmin,
      de.tu_bs.juliet.util.Helper.quicksort(channelNames),
      de.tu_bs.juliet.util.Helper.quicksort(tmpChannelDiffList));
  }

  /** 
   * Wird vom SetChannelDataCommand aufgerufen und setzt die Channeldaten 
   * auf die entsprechenden Attribute. Außerdem erzeugt die Methode ein Diff 
   * aus aktiven Usern und allen in userList gesetzten Usern, um
   * in der GUI die passiven User ausgeben zu können.
   */
  public void setChannelData(String channelName, boolean isAllowedForGuest,
                             Vector userNames) {

    // die übergebenen Daten werden in eigenen Attributen gesetzt 
    this.tmpChannelName = channelName;
    this.tmpOldChannelName = channelName; // zum Editieren
    this.tmpAllowedForGuests = isAllowedForGuest;
    this.tmpAllowedUserNames = userNames;

    // Vector, der mit den Namen der passiven User gefüllt werden soll
    Vector tmpUserDiffList = new Vector();
    String tmpUserName = new String();
    Enumeration enum = this.userList.elements();

    /* vergleicht userList mit userNames (dort sind die allowed
       users gespeichert) und fügt alle user, die in userList
       aber nicht in userNames enthalten sind, zur tmpUserDiffList
       (in der die disallowed users gespeichert werden) hinzu. */
    while (enum.hasMoreElements()) {
      tmpUserName = (String) enum.nextElement();

      if (!userNames.contains(tmpUserName)) {
        tmpUserDiffList.addElement(tmpUserName);
      }
    }

    // setzt die Daten in der GUI, nachdem diese sortiert wurden
    this.gui.setChannelData(channelName, isAllowedForGuest,
      de.tu_bs.juliet.util.Helper.quicksort(userNames),
      de.tu_bs.juliet.util.Helper.quicksort(tmpUserDiffList));
  }

  /** 
   * Wird vom SetUserListCommand aufgerufen und setzt die Userliste 
   * in userList. Darüberhinaus wird die Userliste sortiert an die GUI
   * weitergegeben.
   */
  public void setUserList(Vector list) {

    if (list != null) {
      this.userList = list;
    } else {
      this.userList = new Vector();
    }

    if (this.gui != null) {
      this.gui.setUserList(de.tu_bs.juliet.util.Helper.quicksort(list));
    }
  }

  /** 
   * Wird vom SetChannelListCommand aufgerufen und setzt die Channelliste 
   * in channelList. Darüberhinaus wird die Channelliste sortiert an die
   * GUI weitergegeben. 
   */
  public void setChannelList(Vector list) {

    if (list != null) {
      this.channelList = list;
    } else {
      this.channelList = new Vector();
    }

    if (this.gui != null) {
      this.gui.setChannelList(de.tu_bs.juliet.util.Helper.quicksort(list));
    }
  }

  /**
   * Fordert beim AdminClientServant einen bestimmten Userdatensatz an.
   * Benutzt sendCommand() und erzeugt ein neues GetUserDataCommand - Objekt, 
   * welches im AdminClientServant ausgeführt wird.
   */
  public void getUserData(String userName) {
    this.sendCommand(new GetUserDataCommand(userName));
  }

  /**
   * Fordert beim AdminClientServant einen bestimmten Channeldatensatz an.
   * Benutzt sendCommand() und erzeugt ein neues GetChannelDataCommand - 
   * Objekt, welches im AdminClientServant ausgeführt wird.
   */
  public void getChannelData(String channelName) {
    this.sendCommand(new GetChannelDataCommand(channelName));
  }

  /**
   * Fordert beim AdminClientServant die komplette Userliste an.
   * Benutzt sendCommand() und erzeugt ein neues GetUserListCommand - Objekt, 
   * welches im AdminClientServant ausgeführt wird.
   */
  public void getUserList() {
    this.sendCommand(new GetUserListCommand());
  }

  /**
   * Fordert beim AdminClientServant die komplette Channelliste an.
   * Benutzt sendCommand() und erzeugt ein neues GetChannelListCommand - 
   * Objekt, welches im AdminClientServant ausgeführt wird.
   */
  public void getChannelList() {
    this.sendCommand(new GetChannelListCommand());
  }

  /**
   * Fügt einen Channel mit den (von der GUI) übergebenen Daten hinzu.
   * Benutzt sendCommand() und erzeugt ein neues AddChannelCommand - Objekt, 
   * welches im AdminClientServant ausgeführt wird.
   *  @param paramName Name des neuen Channels
   *  @param paramAllowedForGuests Flag, ob der Channel für Gäste zugelassen ist
   *  @param paramAllowedUserNames Liste der zugelassenen User
   */
  public void addChannel(String paramName, boolean paramAllowedForGuests,
                         Vector paramAllowedUserNames) {
    this.sendCommand(new AddChannelCommand(paramName, paramAllowedForGuests,
                                           paramAllowedUserNames));
  }

  /**
   * Löscht den Channel mit dem Namen name. Benutzt sendCommand() und erzeugt 
   * ein neues DeleteChannelCommand - Objekt, welches im AdminClientServant 
   * ausgeführt wird.
   */
  public void deleteChannel(String name) {
    this.sendCommand(new DeleteChannelCommand(name));
  }

  /**
   * Ändert einen Channel mit den (von der GUI) übergebenen Daten.
   * Benutzt sendCommand() und erzeugt ein neues EditChannelCommand - Objekt, 
   * welches im AdminClientServant ausgeführt wird.
   *  @param paramOldName Alter Name des Channels
   *  @param paramName (Neuer) Name des Channels
   *  @param paramAllowedForGuests Flag, ob der Channel für Gäste zugelassen ist
   *  @param paramAllowedUserNames Liste der zugelassenen User
   */
  public void editChannel(String paramOldName, String paramName,
                          boolean paramAllowedForGuests,
                          Vector paramAllowedUserNames) {

    this.sendCommand(new EditChannelCommand(paramOldName, paramName,
                                            paramAllowedForGuests,
                                            paramAllowedUserNames));
  }

  /**
   * Fügt einen User mit den (von der GUI) übergebenen Daten hinzu.
   * Benutzt sendCommand() und erzeugt ein neues AddUserCommand - Objekt, 
   * welches im AdminClientServant ausgeführt wird.
   *  @param paramName Name des neuen Users
   *  @param paramPassword Passwort des neuen Users
   *  @param paramIsAdmin Flag, ob der Benutzer Administratorrechte besitzt
   *  @param paramAllowedChannelNames Liste der Channels, auf die der User 
   *  Zugriff hat
   */
  public void addUser(String paramName, String paramPassword,
                      boolean paramIsAdmin, Vector paramAllowedChannelNames) {

    this.sendCommand(new AddUserCommand(paramName, paramPassword,
                                        paramIsAdmin,
                                        paramAllowedChannelNames));
  }

  /**
   * Löscht den Benutzer mit dem Namen name. Benutzt sendCommand() und erzeugt 
   * ein neues DeleteUserCommand - Objekt, welches im AdminClientServant 
   * ausgeführt wird.
   */
  public void deleteUser(String name) {
    this.sendCommand(new DeleteUserCommand(name));
  }

  /**
   * Ändert einen User mit den (von der GUI) übergebenen Daten.
   * Benutzt sendCommand() und erzeugt ein neues EditUserCommand - Objekt, 
   * welches im AdminClientServant ausgeführt wird.
   *  @param paramOldName Alter Name des Users
   *  @param paramName (Neuer) Name des Users
   *  @param paramPassword Passwort des Users
   *  @param paramIsAdmin Flag, ob der Benutzer Administratorrechte besitzt
   *  @param paramAllowedChannelNames Liste der Channels, auf die der User 
   *  Zugriff hat
   */
  public void editUser(String paramOldName, String paramName,
                       String paramPassword, boolean paramIsAdmin,
                       Vector paramAllowedChannelNames) {

    this.sendCommand(new EditUserCommand(paramOldName, paramName,
                                         paramPassword, paramIsAdmin,
                                         paramAllowedChannelNames));
  }
}
