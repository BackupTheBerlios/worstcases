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

package de.tu_bs.juliet.util.commands;

import de.tu_bs.juliet.client.Client;


/**
 * Wird von einem ClientServant an einen Client geschickt,
 * falls eine neue Nachricht in dem besuchten Channel
 * gesendet wurde, also ein Benutzer etwas f�r alle anderen
 * Benutzer in dem Channel geschrieben hat. Dazu wird die
 * Methode sendMsgFromChannel der Klasse Client be-
 * nutzt.
 */

public class SendMsgFromChannelCommand implements Command {

    /** Setzt die Nachricht (paramMsg) und den Absender (paramFromName). */
    public SendMsgFromChannelCommand( String paramFromName, String paramMsg ) {
        this.msg = paramMsg;
        this.fromName = paramFromName;
    }

    /** Die Nachricht, die in den Channel gesendet werden soll.- */
    String msg;

    /** Der Name des Benutzers, der diese Nachricht gesendet hat. */
    String fromName;

    /** F�hrt beim Client sendMsgFromChannel() aus */
    public void execute( Object target ) {

        if ( target instanceof Client ) {
            ( ( Client ) target ).sendMsgFromChannel( fromName, msg );
        }
    }
}
