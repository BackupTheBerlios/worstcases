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

import de.tu_bs.juliet.server.AdminClientServant;
import java.util.Vector;


/**
 * Ein AddChannelCommand wird von einem AdminClient erzeugt und hat die
 * Funktion, einen neuen Channel anzulegen, indem addChannel beim
 * AdminClientServant aufgerufen wird.
 */

public class AddChannelCommand implements Command {

    /**
     * Dieser Konstruktor setzt die Attribute. paramName ist der Name des 
     * anzulegenden Channels, paramAllowedForGuests sagt, ob auch Gäste in dem 
     * Raum zugelassen sein können und der paramAllowedUserNames enthält die 
     * Liste der für diesen Channel berechtigten Benutzer.
     */
    public AddChannelCommand( String paramName, boolean paramAllowedForGuests,
                              Vector paramAllowedUserNames ) {

        this.name = paramName;
        this.allowedForGuests = paramAllowedForGuests;
        this.allowedUserNames = paramAllowedUserNames;
    }

    /** Name des Channels. */
    private String name;

    /**
     * Channel auch für Gäste, oder nicht für Gäste. Hier zuerst nicht für 
     * Gäste, da false. 
     */
    private boolean allowedForGuests = false;

    /** Liste der Namen der User, die den Channel betreten dürfen */
    private Vector allowedUserNames;

    /**
     * Führt die Methode adminClientServant.addChannel() mit den Attributen des
     * Commands aus.
     */
    public void execute( Object target ) {

        if ( target instanceof AdminClientServant ) {
            ( ( AdminClientServant ) target ).addChannel( name, allowedForGuests,
                    allowedUserNames );
        }
    }
}
