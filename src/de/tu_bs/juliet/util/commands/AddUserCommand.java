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

package de.tu_bs.juliet.util.commands;

import java.util.Vector;
import de.tu_bs.juliet.server.AdminClientServant;


/**
 * Wird von einem AdminClient erzeugt, um einen neuen User
 * anzulegen. Dieses Command ruft die Methode addUser
 * der Klasse AdminClientServant auf.
 */

public class AddUserCommand implements Command {

    /**
     * Setzt die Attribute des Users. paramName ist der Benutzername, 
     * paramPassword ist das Passwort des Benutzers. paramIsAdmin sagt aus, ob 
     * der Benutzer Administratorrechte hat und paramAllowedChannelNames gibt 
     * an, welche Channels dieser Benutzer betreten darf.
     */
    public AddUserCommand( String paramName, String paramPassword,
                           boolean paramIsAdmin,
                           Vector paramAllowedChannelNames ) {

        this.name = paramName;
        this.password = paramPassword;
        this.isAdmin = paramIsAdmin;
        this.allowedChannelNames = paramAllowedChannelNames;
    }

    /** Name des neuen Users. */
    private String name;

    /** Passwort des neuen Users. */
    private String password;

    /** Admin-Status des Users. Zuerst nein, da false. */
    private boolean isAdmin = false;

    /** Liste der Namen der Channels, die der neue Benutzer betreten darf. */
    private Vector allowedChannelNames;

    /**
     * Führt die Methode adminClientServant.addUser() mit den Attributen
     * des Commands aus.
     */
    public void execute( Object target ) {

        if ( target instanceof AdminClientServant ) {
            ( ( AdminClientServant ) target ).addUser( name, password, isAdmin,
                    allowedChannelNames );
        }
    }
}
