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
import de.tu_bs.juliet.util.Helper;
import de.tu_bs.juliet.util.Debug.Debug;


/**
 * Stellt Methoden zur Administration der Channel zur Verfügung.
 * So können unter anderem Channel hinzugefügt, 
 * bearbeitet und gelöscht werden.
 */
class ChannelAdministration {

  //Liste der verfügbaren Channels
  private Vector channelList = new Vector();

  private DataBaseIO dataBaseIO;
  
  //Name des Foyers, ein Channel für alle Benutzer
  public static final String FOYERNAME = "Foyer";


  /** Gibt eine Aufzählung der existierenden Channel zurück. 
    * Benutzt Helper.vectorCopy()
    */
  public Enumeration getChannelEnum() {
    return Helper.vectorCopy(this.channelList).elements();
  }
  
  /** Liefert eine Aufzählung der Namen der existierenden Channel. 
    * Benutzt getChannelEnum(). */
  public Vector getChannelNames() {

    Vector tmpVector = new Vector();
    Enumeration enum = this.getChannelEnum();
    Channel tmpChannel;

    //Namen der Channels in tmpVector eintragen
    while (enum.hasMoreElements()) { 
      tmpChannel = (Channel) enum.nextElement();

      tmpVector.addElement(tmpChannel.getName());

    }

    return tmpVector;  
  }

  /**
   * Gibt den Channel mit dem angegebenen Namen zurück, falls er existiert. 
   * Ansonsten wird null zurückgegeben.
   * Benutzt getChannelEnum().
   * @param name der Name des Channels, dessen Objekt erwartet wird
   */
  public Channel getFromChannelListByName(String name) {

    if (name != null) {  
      Enumeration enum = this.getChannelEnum();
      Channel tmpChannel;
      
      /* Namen vergleichen, wenn ein Channel mit dem gesuchten Namen
       * gefunden wird, diesen zurückgeben*/
      while (enum.hasMoreElements()) { 
        tmpChannel = (Channel) (enum.nextElement());

        if (tmpChannel.getName().compareTo(name) == 0) {
          return tmpChannel;

        }
      }
    }
    
    //wird nur erreicht, wenn kein Channel mit dem Namen gefunden wurde
    return null;  

    // Rückgaben = "null"
  }

  /**
   * Liefert eine Aufzählung der Channel, die für Gäste freigegeben sind.
   * Benutzt getChannelEnum() und channel.isAllowedForGuests().
   */
  public Enumeration getFreeForGuestEnum() {

    //die Liste der Gastchannel
    Vector tmpList = new Vector();
    Enumeration enum = this.getChannelEnum();  
    Channel tmpChannel;

    while (enum.hasMoreElements()) {  // Die Liste wird durchgearbeitet.
      tmpChannel = (Channel) enum.nextElement();

      if (tmpChannel.isAllowedForGuest()) {
        tmpList.addElement(tmpChannel);
        
      }
    }

    return tmpList.elements();  // tmpList wird zurückgegeben.
  }
  

  /** Setzt ChannelList auf die in channelEnum übergebenen Werte.  
    * Benutzt addToChannelList() und removeFromChannelList(). */
  public synchronized void setChannelList(Enumeration channelEnum) {

    Enumeration enum = channelEnum;

    if (enum == null) {  // Wenn channelEnum leer übergeben wurde,
      enum = (new Vector()).elements();  // wird eine neue Liste angelegt.
    }

    //speichert die Channels aus channelEnum
    Vector tmpList = new Vector();
    Channel tmpChannel;

    /*alle Channels aus channelEnum hinzufügen und zusätzlich in
     *tmpList speichern*/
    while (enum.hasMoreElements()) {

      tmpChannel = (Channel) channelEnum.nextElement();

      tmpList.addElement(tmpChannel);  
      this.addToChannelList(tmpChannel);
    }

    enum = this.getChannelEnum(); 

    //Channels, die nicht in tmpList stehen, entfernen
    while (enum.hasMoreElements()) {
      tmpChannel = (Channel) enum.nextElement();

      if (!tmpList.contains(tmpChannel)) {

        this.removeFromChannelList(tmpChannel);

      }
    }
  }


  /**
   * Entfernt einen Channel. Benachrichtigt den betroffenen Channel 
   * mittels Channel.removeYou().
   * @param paramChannel das Channelobjekt, das gelöscht werden soll
   */
  public void removeFromChannelList(Channel paramChannel) {

    if (paramChannel != null) {  // Wenn ein Channel übergeben wurde..
      //removeElement liefert true, falls paramChannel entfernt wurde   
      if (this.channelList.removeElement(paramChannel)) {
        paramChannel.removeYou();
      }

      // wird dieser aus der ChannelList entfernt und
      // mittels removeYou gelöscht.
      Debug.println(Debug.LOW,
                    "ChannelAdministration: removed: " + paramChannel);
    }
  }

  /**
   * Fügt einen Channel hinzu, sofern er noch nicht existiert.
   * @param paramChannel das Channelobjekt, das hinzugefügt werden soll
   */
  public void addToChannelList(Channel paramChannel) {

    if (paramChannel != null) {  // Wenn ein Channel übergeben wurde...
      if (this.getFromChannelListByName(paramChannel.getName()) == null) {

        // Wenn dieser Channel noch nicht existiert...
        this.channelList.addElement(paramChannel);

        // wird er zur channelList hinzugefügt.
        Debug.println(Debug.LOW,
                      "ChannelAdministration: added: " + paramChannel);
      }
    }
  }

  /**
   * Editiert den Channel mit dem angegebenen Namen mit einem neuen 
   * Datensatz aus einem neuen Channelobjekt.
   * Benutzt Channel.setName(),setAllowedForGuest() und setAllowedUserList().
   * @param name Der Name des Channels, der verändert werden soll
   * @param newChannel neues Channelobjekt, das die neuen Daten des 
   * Channels enthält
   */
  public synchronized void editChannel(String oldName, String newName,
                                       boolean paramAllowedForGuest,
                                       Enumeration allowedUser) {

    // Wenn oldName und newName nicht leer übergeben wurden...
    if ((oldName != null) && (newName != null)
            && (newName.compareTo("") != 0)) {

      Channel tmpChannel = this.getFromChannelListByName(oldName);

      //Channel muß existieren und darf nicht das Foyer sein
      if ((tmpChannel != null)
              && (tmpChannel.getName().compareTo(this.FOYERNAME) != 0)) {

        /* wenn der Name verändert wird, 
         *  darf ein Channel mit newName nicht existieren */
        if (tmpChannel.getName().compareTo(newName) == 0
                | this.getFromChannelListByName(newName) == null) {
          Debug.println(Debug.MEDIUM,
                        "ChannelAdministration: changing: " + tmpChannel);
          // ...wird sein Name aktualisiert.
          tmpChannel.setName(newName);

          // ...wird seine AllowedUserList aktualisiert.
          tmpChannel.setAllowedUserList(allowedUser);

          // ...wird sein AllowedForGuest-Status aktualisiert.
          tmpChannel.setAllowedForGuest(paramAllowedForGuest);

        }

        Debug.println(Debug.MEDIUM,
                      "ChannelAdministration: changed: " + tmpChannel);
      }
    }
  }

  /** Setzt dataBaseIO, benutzt DataBaseIO.setChannelAdministration. */
  public void setDataBaseIO(DataBaseIO paramDataBaseIO) {

    if (this.dataBaseIO != paramDataBaseIO) {
      if (this.dataBaseIO != null) {
        DataBaseIO oldValue = this.dataBaseIO;

        this.dataBaseIO = null;

        oldValue.setChannelAdministration(null);
      }

      this.dataBaseIO = paramDataBaseIO;

      if (paramDataBaseIO != null) {
        paramDataBaseIO.setChannelAdministration(this);
      }
    }
  }
}
