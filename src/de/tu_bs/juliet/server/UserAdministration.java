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

package de.tu_bs.juliet.server;

import java.util.Vector;
import java.util.Enumeration;
import de.tu_bs.juliet.util.debug.Debug;
import de.tu_bs.juliet.util.Helper;


/**
 * Stellt Methoden zur Verwaltung der Benutzer zur Verf�gung. Neben der 
 * Abwicklung des User-Login und des Guest-Login, k�nnen User hinzugef�gt, 
 * bearbeitet und gel�scht werden. Au�erdem gibt es Methoden, um sich alle 
 * oder einzelne User anzeigen zu lassen, sowie einige Methoden, welche die 
 * Counter-Attribute f�r die angemeldeten User bzw. G�ste hoch/ runter z�hlen
 * Desweitern sind in dieser Klasse die jeweils maximalen Anzahlen f�r User 
 * und G�ste festgelegt.
 */

class UserAdministration {

    /** Liste aller Benutzer im System. */
    private Vector userList = new Vector();

    /** Die interne Datenbank */
    private DataBaseIO dataBaseIO;

    /** Die ChannelAdministration */
    private ChannelAdministration channelAdministration;

    /** Anzahl der eingeloggten Benutzer im System. */
    private int numCurrentUsers = 0;

    /** Maximale Anzahl von eingeloggten Benutzern im System. */
    private int maxUsers = 100;

    /** Anzahl der G�ste im System */
    private int numCurrentGuests = 0;

    /** Maxmimale Anzahl von eingeloggten G�sten im System */
    private int maxGuests = 100;

    /** Konstruktor, setzt channelAdministration. */
    public UserAdministration(
        ChannelAdministration paramChannelAdministration ) {
        this.channelAdministration = paramChannelAdministration;
    }

    /**
     * Meldet einen Benutzer an. Pr�ft, ob numCurrentUser < maxUsers, l�t 
     * Administratoren immer ins System, sonst, falls maximale Anzahl erreicht, 
     * return null. Benutzt getFromUserListByName(), user.getPassword(),
     * isLoggedIn() und setIsLogged().
     * @return den Benutzer, falls Authentifizierung klappt, sonst null
     */

    public synchronized User loginUser( String name, String password ) {

        if ( ( name != null ) && ( password != null ) ) {

            // das Userobjekt wird anhand des Namen initialisiert.
            User tmpUser = this.getFromUserListByName( name );

            if ( tmpUser == null ) {

                Debug.println( Debug.MEDIUM,
                               "UserAdministration: login User" + name + " failed" );

                return null;
            } else {

                // wenn das password korrekt und der User noch nicht eingeloggt ist...
                if ( ( tmpUser.getPassword().compareTo( password ) == 0 )
                        && ( !tmpUser.isLoggedIn() ) ) {

                    /* wenn die max. Anzahl der Benutzer im System erreicht ist und es
                       sich nicht um einen Admin handelt... */

                    if ( !tmpUser.isAdmin() && ( numCurrentUsers >= maxUsers ) ) {

                        Debug.println( Debug.MEDIUM,
                                       "UserAdministration: maxusers reached: "
                                       + maxUsers );

                        return null;
                    } else {

                        // alles ok: DerUser wird eingeloggt und tmpUser �bergeben.
                        tmpUser.setIsLoggedIn( true );

                        return tmpUser;
                    }
                } else {
                    Debug.println(
                        Debug.MEDIUM,
                        "UserAdministration: password wrong or user already logged in: "
                        + name );

                    return null;
                }

                /* FIXME: Fehlermeldung, zus�tzliche if-abfrage zur
                   Fehlerunterscheidung!? */
            }
        }

        return null;
    }

    /**
     * Meldet einen Gast an und f�gt ihn zur UserList hinzu, Mittels
     * setAllowedChannelList() wird er zum Betreten der f�r G�ste freie Channels 
     * berechtigt. Legt ein neues Userobjekt an. Benutzt addToUserList und 
     * setIsLoggedIn(). Pr�ft mittels getFromUserListByName, ob der gew�nschte 
     * Gastname noch frei ist.
     * @return den Benutzer, falls Authentifizierung klappt, sonst null
     */
    public synchronized User loginGuest( String paramName ) {

        if ( paramName != null ) {

            /* wenn es den User noch nicht gibt und die max. Anzahl der G�ste noch
               nicht erreicht wurde... */

            if ( ( this.getFromUserListByName( paramName ) == null )
                    && ( this.numCurrentGuests < this.maxGuests ) ) {

                /* Ein neues Userobjekt mit guest-Eigenschaften wird angelegt und an
                   tmpUser �bergeben. */
                User tmpUser = new User( paramName, "guest", true, false, this );

                // Die f�r G�ste zug�nglichen Channel werden dem guest zugewiesen.
                tmpUser
                .setAllowedChannelList( this.channelAdministration
                                        .getFreeForGuestEnum() );

                // Der guest wird eingeloggt.
                tmpUser.setIsLoggedIn( true );
                this.addToUserList( tmpUser );

                // Das Userobjekt wird zur�ckgegeben
                return tmpUser;
            } else {

                Debug.println( Debug.MEDIUM,
                               "UserAdministration: guestname " + paramName
                               + " login failed" );

                return null;
            }
        }

        return null;
    }

    /**
     * Liefert eine Namensliste aller User, die nicht G�ste sind. Benutzt 
     * getUserEnum(). 
     */
    public Vector getUserNames() {

        Vector tmpVector = new Vector();
        Enumeration enum = this.getUserEnum();
        User tmpUser;

        while ( enum.hasMoreElements() ) {
            tmpUser = ( User ) enum.nextElement();

            if ( !tmpUser.isGuest() ) {
                tmpVector.addElement( tmpUser.getName() );
            }
        }

        return tmpVector;
    }

    /**
     * Mittels dieser Methode kann ein User-Objekt bearbeitet werden. Setzt die 
     * Daten des Userobjektes mit dem Namen oldName auf die in newUser 
     * enthaltenen Daten mittels user.setName(),setPassword(), setIsAdmin(),
     * setAllowedChannelList(). Benutzt getFromUserListByName().
     */
    public synchronized void editUser( String oldName, String newName,
                                       String newPassword, boolean paramIsAdmin,
                                       Enumeration allowedChannelEnum ) {

        if ( ( oldName != null ) && ( newName != null ) && ( newPassword != null )
                && ( newName.compareTo( "" ) != 0 )
                && ( newPassword.compareTo( "" ) != 0 ) ) {

            // Das betreffende Userobjekt wird anhand des alten Namens geladen.
            User tmpUser = this.getFromUserListByName( oldName );

            // Wenn das Userobjekt existiert, ...
            if ( tmpUser != null ) {
                // Wenn der Name des Users g�ltig ist, ...
                if ( oldName.compareTo( newName ) == 0
                        | this.getFromUserListByName( newName ) == null ) {
                    Debug.println( Debug.MEDIUM,
                                   "UserAdministration: changing: " + tmpUser );
                    // ... werden die �bergebenen Attribute gesetzt.
                    tmpUser.setName( newName );
                    tmpUser.setPassword( newPassword );
                    tmpUser.setIsAdmin( paramIsAdmin );
                    tmpUser.setAllowedChannelList( allowedChannelEnum );
                    Debug.println( Debug.MEDIUM,
                                   "UserAdministration: changed: " + tmpUser );

                    // Client �ber Ver�nderungen informieren.
                    ClientServant tmpClientServant = tmpUser.getClientServant();

                    if ( tmpClientServant != null ) {
                        tmpClientServant
                        .sendErrorMsg( "Benutzerdaten haben sich ge�ndert!\n" + tmpUser );
                    }
                } else {
                    Debug.println( "UserAdministration: editUser: error: " + newName
                                   + " existent" );
                }
            } else {
                Debug.println( "UserAdministration: editUser error: oldName:"
                               + oldName + " not found!" );
            }
        } else {
            Debug.println( Debug.HIGH,
                           "UserAdministration: editUser error: wrong parameter!" );
        }
    }

    /**
     * F�gt einen Benutzer mittels user.setUserAdministration() zur UserList 
     * hinzu. 
     */
    public synchronized void addToUserList( User paramUser ) {

        /* Wenn ein Userobjekt �bergeben wurde, das nicht in der
           UserList steht... */

        if ( ( paramUser != null )
                && ( this.getFromUserListByName( paramUser.getName() ) == null ) ) {

            // ... dann wird es zur userList hinzugef�gt.
            this.userList.addElement( paramUser );
            paramUser.setUserAdministration( this );
            Debug.println( Debug.LOW, "UserAdministration: added: " + paramUser );
        }
    }

    /** Entfernt einen Benutzer aus der UserList. */
    public synchronized void removeFromUserList( User paramUser ) {

        /* removeElement() gibt true zur�ck, falls der user aus der userList
           entfernt wurde */

        if ( this.userList.removeElement( paramUser ) ) {

            // besorgt sich den ClientServant.
            ClientServant tmpClientServant = paramUser.getClientServant();

            // benachrichtigt, falls existent, �ber den ClientServant den User
            if ( tmpClientServant != null ) {
                tmpClientServant
                .sendErrorMsg( "Ihr Account wurde vom Admin gel�scht!" );
            }

            // das Userobjekt "entfernt sich selbst" aus dem System
            paramUser.removeYou();

            Debug.println( Debug.LOW, "UserAdministration: removed: " + paramUser );
        }
    }

    /**
     * Gibt den Benutzer mit dem angegebenen Namen zur�ck. 
     * Benutzt getUserEnum(), user.getName() 
     */
    public User getFromUserListByName( String name ) {

        Enumeration enum = this.getUserEnum();
        User tmpUser;

        /* sucht in der UserEnum nach dem angegebenen Benutzer
           und gibt bei Erfolg das Userobjekt zur�ck */

        while ( enum.hasMoreElements() ) {
            tmpUser = ( User ) ( enum.nextElement() );

            if ( tmpUser.getName().compareTo( name ) == 0 ) {
                return tmpUser;
            }
        }

        // Es wurde kein User mit "name" gefunden: Es wird null zur�ckgegeben.
        return null;
    }

    /** Gibt eine Aufz�hlung aller User zur�ck */
    public Enumeration getUserEnum() {
        return Helper.vectorCopy( this.userList ).elements();
    }

    /**
     * Setzt userList auf die in userEnum enthaltenen Objekte von Typ User. 
     * Benutzt addToUserList() und removeFromUserList(). */
    public void setUserList( Enumeration userEnum ) {

        Enumeration enum = userEnum;

        if ( enum == null ) {
            enum = ( new Vector() ).elements();
        }

        Vector tmpUserList = new Vector();
        User tmpUser;

        while ( enum.hasMoreElements() ) {
            tmpUser = ( User ) enum.nextElement();

            tmpUserList.addElement( tmpUser );
            this.addToUserList( tmpUser );
        }

        enum = this.getUserEnum();

        while ( enum.hasMoreElements() ) {
            tmpUser = ( User ) enum.nextElement();

            if ( !tmpUserList.contains( tmpUser ) ) {
                this.removeFromUserList( tmpUser );
            }
        }
    }

    /** Erh�ht den Z�hler numCurrentUsers um 1 */
    public synchronized void incNumCurrentUsers() {

        this.numCurrentUsers++;

        Debug.println( Debug.MEDIUM,
                       "UserAdministration: #User:" + this.numCurrentUsers );
    }

    /** Verkleinert den Z�hler numCurrentUsers um 1 */
    public synchronized void decNumCurrentUsers() {

        this.numCurrentUsers--;

        Debug.println( Debug.MEDIUM,
                       "UserAdministration: #User:" + this.numCurrentUsers );
    }

    /** Erh�ht den Z�hler numCurrentGuests um 1 */
    public synchronized void incNumCurrentGuests() {

        this.numCurrentGuests++;

        Debug.println( Debug.MEDIUM,
                       "UserAdministration: #Guest:" + this.numCurrentGuests );
    }

    /** Verringert den Z�hler numCurrentGuests um 1 */
    public synchronized void decNumCurrentGuests() {

        this.numCurrentGuests--;

        Debug.println( Debug.MEDIUM,
                       "UserAdministration: #guest:" + this.numCurrentGuests );
    }

    /**
     * Setzt dataBaseIO und benachrichtigt das betroffene Objekt durch 
     * DataBaseIO.setUserAdministration 
     */
    public void setDataBaseIO( DataBaseIO paramDataBaseIO ) {

        if ( this.dataBaseIO != paramDataBaseIO ) {
            if ( this.dataBaseIO != null ) {
                DataBaseIO old = this.dataBaseIO;

                this.dataBaseIO = null;

                old.setUserAdministration( null );
            }

            this.dataBaseIO = paramDataBaseIO;

            if ( paramDataBaseIO != null ) {
                paramDataBaseIO.setUserAdministration( this );
            }
        }
    }
}
