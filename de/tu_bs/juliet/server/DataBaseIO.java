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
import java.io.*;
import java.util.StringTokenizer;

import de.tu_bs.juliet.util.debug.Debug;


/**
 * Stellt Methoden bereit, um die Benutzer- und Channeldatenbank zu laden
 * und zu speichern. Außerdem wird dafür gesorgt, dass für den Betrieb die
 * relationalen Beziehungen zwischen User- und Channeldatenbank 
 * gesetzt werden.
 */
class DataBaseIO {

  private ChannelAdministration channelAdministration;

  /** Dateiname der Channeldatenbank. */
  private String channelDBFile = "channel.db";

  private UserAdministration userAdministration;

  /** Dateiname der Benutzerdatenbank. */
  private String userDBFile = "user.db";

  /**
   *  Konstruktor, der die Attribute für die ChannelAdministration und
   *  UserAdministration setzt.
   *  Benutzt setChannelAdministration() und setUserAdministration().
   */
  public DataBaseIO(UserAdministration paramUserAdministration,
                    ChannelAdministration paramChannelAdministration) {
    this.setChannelAdministration(paramChannelAdministration);
    this.setUserAdministration(paramUserAdministration);
  }

  /**
   * Konvertiert den Namen, das Password, das isAdmin-Flag und
   * die Namen der für den Benutzer erlaubten Channels eines Userobjekts in
   * einen String, wird von saveToDisk() verwendet.
   * Format des Strings: 
   * "name#password#boolean isAdmin#channel1#channel2#channel3...".
   * Entfernt '#' aus den User-Attributen.
   */
  private String userToString(User paramUser) {

    // in tmpString wird Name, Paßwort und IsAdmin gespeichert
    String tmpString = paramUser.getName().replace('#', ' ') + "#"
                       + paramUser.getPassword().replace('#', ' ') + "#"
                       + paramUser.isAdmin();

    // freigegebene Channels des Benutzers
    Enumeration enum = paramUser.getAllowedChannelEnum();

    // Namen der Channels zu tmpString hinzufügen
    while (enum.hasMoreElements()) {
      tmpString = tmpString + "#"
                  + ((Channel) (enum.nextElement())).getName().replace('#',
                    ' ');
    }

    return tmpString;
  }

  /**
   * Konvertiert den von userToString() erzeugten String in ein Userobjekt.
   * Setzt vorraus, dass die entsprechenden Channelobjekte bereits geladen
   * wurden.
   * Benutzt channelAdministration.getFromChannelListByName()
   * und user.setAllowedChannelList().
   */
  private User stringToUser(String userSet) {

    StringTokenizer tmpTokenizer = new StringTokenizer(userSet, "#", false);

    // Name wird durch das nächste String übergeben
    String name = tmpTokenizer.nextToken();

    // Paßwort wird dem String entnommen
    String password = tmpTokenizer.nextToken();

    // isAdmin wird aus dem String in ein boolean umgewandelt
    boolean isAdmin = (new Boolean(tmpTokenizer.nextToken())).booleanValue();

    // Vector für die Channelobjekte
    Vector tmpChannelList = new Vector();

    //Channelobjekte zu tmpChannelList hinzufügen
    while (tmpTokenizer.hasMoreTokens()) {
      tmpChannelList
        .addElement(this.channelAdministration
          .getFromChannelListByName(tmpTokenizer.nextToken()));
    }

    // neues User Objekt mit den Parametern des Strings
    User tmpUser = new User(name, password, false, isAdmin,
                            this.userAdministration);

    // die Channels, die der User betreten darf, werden gesetzt
    tmpUser.setAllowedChannelList(tmpChannelList.elements());

    return tmpUser;
  }

  /**
   * Konvertiert den Namen und das isAllowedForGuests-Flag
   * eines Channelobjekts in einen String, wird von saveToDisk() verwendet.
   * Format des Strings: "name#boolean isFreeForGuests".
   * '#' werden aus den Attributen entfernt
   */
  private String channelToString(Channel paramChannel) {

    String tmpString = paramChannel.getName().replace('#', ' ') + "#"
                       + paramChannel.isAllowedForGuest();

    return tmpString;
  }

  /**
   * Konvertiert den von channelToString() erzeugten String in ein
   * Channelobjekt.
   */
  private Channel stringToChannel(String channelSet) {

    StringTokenizer tmpTokenizer = new StringTokenizer(channelSet, "#",
                                     false);

    String name = tmpTokenizer.nextToken();

    boolean allowedForGuests =
      (new Boolean(tmpTokenizer.nextToken())).booleanValue();

    Channel tmpChannel = new Channel(name, allowedForGuests);

    return tmpChannel;
  }

  /**
   * Lädt die Benutzer- und Channeldaten aus userDBFile und channelDBFile
   * mittels stringToUser(),stringToChannel,
   * channelAdministration.setChannelList() und
   * userAdministration.setUserList().
   */
  public synchronized void loadFromDisk()
          throws java.io.FileNotFoundException, java.io.IOException {

    String tmpString;
    
    //enthaelt die Channelobjekte
    Vector tmpList = new Vector();

    // Dateizugriff wird initialsiert
    BufferedReader tmpBufferedReader =
      new BufferedReader(new FileReader(new File(this.channelDBFile)));

    // erste Zeile wird aus ChannelDBFile ausgelesen
    tmpString = tmpBufferedReader.readLine();

    // Schleife wird ausgeführt, wenn die ausgelesene Zeile nicht leer ist
    while (tmpString != null) {

      // fügt die Strings in den Vector tmpList hinzu
      tmpList.addElement(this.stringToChannel(tmpString));

      tmpString = tmpBufferedReader.readLine();
    }

    // die Channeliste wird in der ChannelAdministration gesetzt
    this.channelAdministration.setChannelList(tmpList.elements());
    tmpBufferedReader.close();
    Debug.println(Debug.MEDIUM, "DataBaseIO: channeldb loaded");

    // zweiter Vector für die Userliste
    Vector tmpList2 = new Vector();

    tmpBufferedReader =
      new BufferedReader(new FileReader(new File(this.userDBFile)));
    tmpString = tmpBufferedReader.readLine();

    // wenn die erste Zeile in der Datei nicht leer ist....
    while (tmpString != null) {

      // hinzufügen der Strings in tmpList2
      tmpList2.addElement(this.stringToUser(tmpString));

      tmpString = tmpBufferedReader.readLine();
    }

    // die User werden in der userAdministration gesetzt
    this.userAdministration.setUserList(tmpList2.elements());
    tmpBufferedReader.close();
    Debug.println(Debug.MEDIUM, "DataBaseIO: userdb loaded");
    Debug.println(Debug.LOW, "DataBaseIO: loaded the following data: ");
    Debug.println(Debug.LOW, tmpList);
    Debug.println(Debug.LOW, tmpList2);
  }

  /**
   * Speichert die Benutzer- und Channeldaten der aktuellen
   * User- (keine Gäste) und Channelobjekte im System
   * in userDBFile und channelDBFile mittels userToString(),
   * channelToString().
   * Benutzt channelAdministration.getChannelEnum() und
   * userAdministration.getUserEnum()
   */
  public synchronized void saveToDisk() {

    try {

      // Dateischreibzugriff wird initialisiert
      BufferedWriter tmpBufferedWriter =
        new BufferedWriter(new FileWriter(new File(this.channelDBFile)));

      Enumeration enum = this.channelAdministration.getChannelEnum();

      // Schleife, um die Channels als Strings in die Datei zu schreiben
      while (enum.hasMoreElements()) {
        tmpBufferedWriter
          .write(this.channelToString((Channel) (enum.nextElement())) + "\n");
      }

      tmpBufferedWriter.close();
      Debug.println("DataBaseIO: channel data written to disk");

      // Dateizugriff für UserDBFile wird initialisiert
      tmpBufferedWriter =
        new BufferedWriter(new FileWriter(new File(this.userDBFile)));

      User tmpUser;

      enum = this.userAdministration.getUserEnum();

      // schleife, um die User in die Datei als String zu schreiben
      while (enum.hasMoreElements()) {
        tmpUser = (User) (enum.nextElement());

        // ...wird nur gespeichert, wenn User kein Gast ist
        if (!tmpUser.isGuest()) {
          tmpBufferedWriter.write(this.userToString(tmpUser) + "\r\n");
        }
      }

      tmpBufferedWriter.close();
      Debug.println("DataBaseIO: user data written to disk");
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, "DataBaseIO: error while saving data: " + e);
    }
  }

  /**
   * Setzt channelAdministration und benutzt
   * channelAdministration.setDataBaseIO().
   */
  public void setChannelAdministration(
          ChannelAdministration paramChannelAdministration) {

    if (this.channelAdministration != paramChannelAdministration) {
      if (this.channelAdministration != null) {
        ChannelAdministration oldValue = this.channelAdministration;

        this.channelAdministration = null;

        oldValue.setDataBaseIO(null);
      }

      this.channelAdministration = paramChannelAdministration;

      if (paramChannelAdministration != null) {
        paramChannelAdministration.setDataBaseIO(this);
      }
    }
  }

  /**
   * Setzt userAdministration und benutzt userAdministration.setDataBaseIO().
   */
  public void setUserAdministration(
          UserAdministration paramUserAdministration) {

    if (this.userAdministration != paramUserAdministration) {
      if (this.userAdministration != null) {
        UserAdministration oldValue = this.userAdministration;

        this.userAdministration = null;

        oldValue.setDataBaseIO(null);
      }

      this.userAdministration = paramUserAdministration;

      if (paramUserAdministration != null) {
        paramUserAdministration.setDataBaseIO(this);
      }
    }
  }
}
