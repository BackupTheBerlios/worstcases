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
 * Wird von einem AdminClient erzeugt, um die Daten eines Channels
 * zu ändern, indem die Methode editChannel beim AdminClientServant
 * aufgerufen wird.
 */
public class EditChannelCommand implements Command {

  /**
   * Setzt die neuen Attribute, die der Channel bekommen soll. Dabei ist 
   * OldName der alte Name des Channels und dient somit zur Identifizierung 
   * des Channels. Es kann der Channelname verändert werden, der Channel auch 
   * für Gäste geöffnet werden und die Liste der berechtigten Benutzer 
   * aktualisiert werden.
   */
  public EditChannelCommand(String paramOldName, String paramName,
                            boolean paramAllowedForGuests,
                            Vector paramAllowedUserNames) {

    this.oldName = paramOldName;
    this.name = paramName;
    this.allowedForGuests = paramAllowedForGuests;
    this.allowedUserNames = paramAllowedUserNames;
  }

  /**
   * Der alte Name des Channels, damit auch der richtige Channel bearbeitet 
   * wird.
   */
  private String oldName;

  /** Neuer Name des Channels. */
  private String name;

  /** Channel öffentlich oder nicht öffentlich? */
  private boolean allowedForGuests = false;

  /** 
   * Aktualisierte Liste der Namen der User, die den Channel betreten dürfen. 
   */
  private Vector allowedUserNames;

  /** 
   * Führt adminClientServant.editChannel() mit den Attributen des Commands aus. 
   */
  public void execute(Object target) {

    if (target instanceof AdminClientServant) {
      ((AdminClientServant) target).editChannel(oldName, name,
                                                allowedForGuests,
                                                allowedUserNames);
    }
  }
}
