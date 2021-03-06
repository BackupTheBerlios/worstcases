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
 * Ein Benutzerdatensatz. Enth�lt alle Informationen �ber einen Benutzer wie
 * beispielsweise die Zugriffsrechte.
 */

class User {
    /** Der Name des Benutzers. */
    private String name;

    /** Das Passwort des Benutzers. */
    private String password;

    /** Gaststatus des Benutzers. */
    private boolean isGuest = true;

    /** Ist der User Administrator? */
    private boolean isAdmin = false;

    /** Gibt an, ob der Benutzer momentan das System benutzt. */
    private boolean loggedIn = false;

    /**
     * Gibt die Channels an, die der Benutzer betreten darf.
     */
    private Vector allowedChannelList = new Vector();

    /**
     * Gibt den Channel an, in dem sich der Benutzer zur Zeit befindet.
     */
    private Channel currentChannel;

    /**
     * Der f�r den Benutzer verantwortliche ClientServant.
     */
    private ClientServant clientServant;

    /**
     * Die f�r den Benutzer verantowrtliche UserAdministration.
     */
    private UserAdministration userAdministration;

    /**
     * Konstruktor. Setzt die entsprechenden Attribute, benutzt
     * setUserAdministration().
     */
    public User( String paramName, String paramPassword, boolean paramGuest,
                 boolean paramAdmin,
                 UserAdministration paramUserAdministration ) {

        this.name = paramName;
        this.password = paramPassword;
        this.isAdmin = paramAdmin;
        this.isGuest = paramGuest;

        this.setUserAdministration( paramUserAdministration );
    }

    /** Gibt den Namen des Benutzers zur�ck. */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Benutzers. Ruft ggf. informClient() und
     * currentChannel.informCurrentUsers() auf.
     */

    public synchronized void setName( String paramName ) {

        if ( paramName != null ) {
            if ( this.name != paramName ) {
                this.name = paramName;

                this.informClient();

                Channel tmpChannel = this.getCurrentChannel();

                if ( tmpChannel != null ) {
                    tmpChannel.informCurrentUsers();
                }
            }
        }
    }

    /** Gibt das Passwort des Users zur�ck. */
    public String getPassword() {
        return password;
    }

    /** Setzt das Passwort des Users. */
    public void setPassword( String paramPassword ) {

        if ( paramPassword != null ) {
            this.password = paramPassword;
        }
    }

    /** Gibt true zur�ck, wenn der Benutzer ein Gast ist. */
    public boolean isGuest() {
        return isGuest;
    }

    /** Gibt true zur�ck, wenn der Benutzer Adminrechte hat. */
    public boolean isAdmin() {
        return this.isAdmin;
    }

    /**
     * Setzt das Adminflag, macht allerdings weiter nichts. D.h. ein Benutzer,
     * der eingeloggt ist und Admin-Rechte bekommt mu� sich mittels der
     * AdminClient-Applikation neu einloggen, um diese nutzen zu k�nnen.
     */
    public void setIsAdmin( boolean b ) {
        this.isAdmin = b;
    }

    /** Gibt an, ob sich der Benutzer im System eingeloggt hat. */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Loggt den Benutzer ein oder aus, Benutzt
     * userAdministration.incNumCurrentUsers(),decNumCurrentUsers(),
     * (incNumCurrentGuests(),decNumCurrentGuests() bei G�sten, benutzt ggf.
     * user.setCurrentChannel(null),setClientServant(null) 
     * (und removeYou() bei G�sten, um den Gast aus dem System zu entfernen, 
     * da G�ste nur tempor�r ein Benutzerobjekt zugewiesen bekommen).
     */
    public synchronized void setIsLoggedIn( boolean paramLoggedIn ) {

        // nur wenn sich der Status �ndert
        if ( this.isLoggedIn() != paramLoggedIn ) {
            this.loggedIn = paramLoggedIn;

            // User wird eingeloggt
            if ( paramLoggedIn ) {

                // Gast bzw. Userz�hler in userAdministration erh�hen
                if ( this.isGuest() ) {
                    this.userAdministration.incNumCurrentGuests();
                } else {
                    this.userAdministration.incNumCurrentUsers();
                }

                Debug.println( Debug.MEDIUM, "User: logged in" );
            }

            // User wird ausgeloggt
            else {

                // betroffene Assoziationen aktualisieren
                this.setCurrentChannel( null );
                this.setClientServant( null );

                /* falls Gast, Objekt aus der Userliste entfernen,
                 * Gastz�hler erniedrigen*/

                if ( this.isGuest() ) {
                    this.userAdministration.decNumCurrentGuests();
                    this.removeYou();
                }

                // kein Gast, nur Userz�hler erniedrigen
                else {
                    this.userAdministration.decNumCurrentUsers();
                }

                Debug.println( Debug.MEDIUM, "User: logged out" );
            }
        }
    }

    /**
     * Gibt eine Liste der Namen der Channels zur�ck, 
     * die der Benutzer betreten darf.
     */
    public Vector getAllowedChannelNames() {

        Enumeration enum = this.getAllowedChannelEnum();
        Vector tmpVector = new Vector();

        // Namen der Channel zu tmpVector hinzuf�gen
        while ( enum.hasMoreElements() ) {
            tmpVector.addElement( ( ( Channel ) enum.nextElement() ).getName() );
        }

        return tmpVector;
    }

    /** Gibt eine Aufz�hlung der Channels zur�ck,
      * die der Benutzer betreten darf. */
    public Enumeration getAllowedChannelEnum() {
        return Helper.vectorCopy( allowedChannelList ).elements();
    }

    /**
     * Gibt einen Channel aus der Liste der erlaubten Channels mit dem
     * angegebenen Namen zur�ck. Falls der Channel nicht existiert wird null
     * zur�ckgegeben.
     */
    public Channel getFromAllowedChannelByName( String channelName ) {

        if ( channelName != null ) {
            Enumeration enum = this.getAllowedChannelEnum();
            Channel tmpChannel;

            while ( enum.hasMoreElements() ) {
                tmpChannel = ( Channel ) enum.nextElement();

                if ( tmpChannel.getName().compareTo( channelName ) == 0 ) {
                    return tmpChannel;
                }
            }
        }

        // wird nur erreicht, falls kein Channel gefunden wurde
        return null;
    }

    /**
     * Setzt die Liste der f�r den Benutzer erlaubten Channels mit
     * addToAllowedChannelList() und removeFromAllowedChannelList().
     */
    public synchronized void setAllowedChannelList( Enumeration channelEnum ) {

        Enumeration enum = channelEnum;

        // wurde null �bergeben, dann wird eine leere Enumeration benutzt
        if ( enum == null ) {
            enum = ( new Vector() ).elements();
        }

        // speichert die Channel aus channelEnum
        Vector tmpChannelList = new Vector();

        Channel tmpChannel;

        /*
         * alle Channels aus channelEnum zu den erlaubten Channels hinzufuegen
         * und diese zusaetzlich in tmpChannelList speichern
         */
        while ( enum.hasMoreElements() ) {
            tmpChannel = ( Channel ) enum.nextElement();

            tmpChannelList.addElement( tmpChannel );
            this.addToAllowedChannelList( tmpChannel );
        }

        /*
         * alle Channels aus der Liste der erlaubten Channels entfernen, die nicht
         * in der tmpChannelList, also in channelEnum stehen
         */
        enum = this.getAllowedChannelEnum();

        while ( enum.hasMoreElements() ) {
            tmpChannel = ( Channel ) enum.nextElement();

            if ( !tmpChannelList.contains( tmpChannel ) ) {
                this.removeFromAllowedChannelList( tmpChannel );
            }
        }
    }

    /**
     * F�gt einen Channel zu der Liste der f�r den Benutzer erlaubten Channels
     * hinzu, benutzt Channel.addToAllowedUserList(). Ruft informClient() auf.
     */
    public void addToAllowedChannelList( Channel paramChannel ) {

        if ( paramChannel != null ) {

            // nur Channel aufnehmen, die noch nicht in der Liste stehen
            if ( !this.allowedChannelList.contains( paramChannel ) ) {
                this.allowedChannelList.addElement( paramChannel );
                this.informClient();
                paramChannel.addToAllowedUserList( this );
            }
        }
    }

    /**
     * Entfernt einen Channel aus der Liste der erlaubten Channels,
     * benutzt Channel.removeFromAllowedUserList(). Ruft informClient() auf.
     */
    public void removeFromAllowedChannelList( Channel paramChannel ) {

        if ( paramChannel != null ) {

            // removeElement() gibt true zur�ck, falls das Element wirklich entfernt
            // wurde, d.h. existent war
            if ( this.allowedChannelList.removeElement( paramChannel ) ) {
                paramChannel.removeFromAllowedUserList( this );

                // wenn der betretene Channel nicht mehr erlaubt ist, currentChannel
                // auf null setzen

                if ( paramChannel == this.currentChannel ) {
                    this.setCurrentChannel( null );

                    ClientServant tmpClientServant = this.getClientServant();

                    if ( tmpClientServant != null ) {
                        tmpClientServant.sendErrorMsg(
                            "Der aktuelle Channel darf nicht mehr betreten werden.\nDaher wird ins Foyer gewechselt." );
                    }
                }

                this.informClient();
            }
        }
    }

    /** Gibt den Channel zur�ck, in dem sich der Benutzer momentan befindet. */
    public Channel getCurrentChannel() {
        return currentChannel;
    }

    /**
     * Setzt den Channel, in dem sich der Benutzer befindet.
     * Benutzt Channel.removeFromCurrentUserList() und addToCurrentUserList().
     * benutzt informClient().
     */
    public void setCurrentChannel( Channel paramChannel ) {

        ClientServant tmpClientServant = this.getClientServant();

        if ( this.currentChannel != paramChannel ) {
            if ( this.currentChannel != null ) {
                Channel old = this.currentChannel;

                this.currentChannel = null;

                old.removeFromCurrentUserList( this );

                /* Assoziation wird tempor�r "einseitig" gesetzt, damit die Nachricht
                 * "verl�t den Channel" zwar an den Channel gesendet wird, vom
                 * User aber nicht mehr empfangen wird.
                 */

                this.currentChannel = old;

                if ( tmpClientServant != null ) {
                    tmpClientServant.sendMsgToChannel( "verl�t den Channel" );
                }

                this.currentChannel = null;
            }

            this.currentChannel = paramChannel;

            if ( paramChannel != null ) {
                if ( tmpClientServant != null ) {
                    tmpClientServant.sendMsgToChannel( "betritt den Channel" );
                }

                paramChannel.addToCurrentUserList( this );
            }

            this.informClient();
            Debug.println( Debug.LOW,
                           "User: " + this.getName() + ": setCurrentChannel to: "
                           + paramChannel );
        }
    }

    /**
     * Setzt die f�r den Benutzer verantwortliche UserAdministration.
     * Benutzt UserAdministration.removeFromUserList() und addToUserList().
     */
    public void setUserAdministration(
        UserAdministration paramUserAdministration ) {

        if ( this.userAdministration != paramUserAdministration ) {
            if ( this.userAdministration != null ) {
                UserAdministration old = this.userAdministration;

                this.userAdministration = null;

                old.removeFromUserList( this );
            }

            this.userAdministration = paramUserAdministration;

            if ( paramUserAdministration != null ) {
                paramUserAdministration.addToUserList( this );
            }
        }
    }

    /** Gibt den dem User zugeordneten ClientServant zur�ck. */
    public ClientServant getClientServant() {
        return clientServant;
    }

    /**
     * Setzt den zugeordneten ClientServant und benutzt ClientServant.setUser().
     * Ein setClientServant(null) bewirkt ein setIsLoggedIn(false).
     */
    public void setClientServant( ClientServant paramClientServant ) {

        if ( this.clientServant != paramClientServant ) {
            if ( this.clientServant != null ) {
                ClientServant old = this.clientServant;

                this.clientServant = null;

                old.setUser( null );
            }

            this.clientServant = paramClientServant;

            if ( paramClientServant != null ) {
                paramClientServant.setUser( this );
            }

            // clientServant wurde auf null gesetzt
            else {
                this.setIsLoggedIn( false );
            }

            Debug.println( Debug.LOW,
                           this.getName() + ": setting ClientServant to: "
                           + paramClientServant );
        }
    }

    /**
     * Entfernt das Benutzerobject aus dem System. Benutzt setIsLoggedIn(false),
     * setAllowedChannelList(null) und
     * UserAdministration.removeFromUserList(this).
     */
    public void removeYou() {

        this.setIsLoggedIn( false );
        this.setAllowedChannelList( null );
        this.setUserAdministration( null );
        Debug.println( Debug.LOW, this.getName() + ": has been removed" );
    }

    /** Dient dem debugging. */
    public String toString() {

        String tmpString = "Name:" + this.getName() + " password:"
                           + this.getPassword() + " isAdmin:" + this.isAdmin()
                           + " isGuest:" + this.isGuest() + " currentChannel:"
                           + this.getCurrentChannel() + " allowedChannelEnum: ";
        Enumeration enum = this.getAllowedChannelEnum();

        while ( enum.hasMoreElements() ) {
            tmpString = tmpString + ( ( Channel ) ( enum.nextElement() ) ).getName()
                        + " ";
        }

        return tmpString;
    }

    /**
     * Informiert den Client des Benutzers bei Ver�nderungen der Userdaten 
     * mittels getClientServant() und ClientServant.sendCurrentUserData().
     */
    private void informClient() {

        ClientServant tmpClientServant = this.getClientServant();

        if ( tmpClientServant != null ) {
            tmpClientServant.sendCurrentUserData();
        }
    }
}
