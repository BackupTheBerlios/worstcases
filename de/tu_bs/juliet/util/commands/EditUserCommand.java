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

package de.tu_bs.juliet.util.Commands;

import de.tu_bs.juliet.server.AdminClientServant;
import java.util.Vector;

/**
 * Wird von einem AdminClient erzeugt, um Benutzerdaten eines Benutzers zu
 * verändern, indem die Methode editUser der Klasse AdminClientServant
 * ausgeführt wird.
 */
public class EditUserCommand implements Command {

  /**
   * Setzt die neuen Attribute des Benutzers. paramOldName ist der alte Name 
   * des Benutzers, der zur Identifizierung benötigt wird. Es kann der 
   * Benutzername, das Passwort, sowie die Channels, auf die der entsprechende 
   * Benutzer Zugriff hat, verändert werden. Außerdem können dem Benutzer 
   * Administrationsrechte gegeben, bzw. entzogen werden.
   */
  public EditUserCommand(String paramOldName, String paramName,
                         String paramPassword, boolean paramIsAdmin,
                         Vector paramAllowedChannelNames) {

    this.oldName = paramOldName;
    this.name = paramName;
    this.password = paramPassword;
    this.isAdmin = paramIsAdmin;
    this.allowedChannelNames = paramAllowedChannelNames;
  }

  /** Der alte Name des Users. */
  private String oldName;

  /** Name des Users. */
  private String name;

  /** Passwort des Users. */
  private String password;

  /** Admin-Status des Users. */
  private boolean isAdmin = false;

  /** Liste der Namen der Channels, die der Benutzer betreten darf. */
  private Vector allowedChannelNames;

  /**
   * Führt adminClientServant.editUser() mit den Attributen des Commands aus, 
   * also die Methode editUser der Klasse adminClientServant.
   */
  public void execute(Object target) {

    if (target instanceof AdminClientServant) {
      ((AdminClientServant) target).editUser(oldName, name, password,
                                             isAdmin, allowedChannelNames);
    }
  }
}
