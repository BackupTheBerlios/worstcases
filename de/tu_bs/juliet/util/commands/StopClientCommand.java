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
 * Dieses Command dient dazu, einen Client zu stoppen, also die Verbindung 
 * zu unterbrechen. Dazu wird die Methode stopClient der Klasse Client 
 * verwendet.
 */
public class StopClientCommand implements Command {

  /** Ruft beim Client stopClient() auf */
  public void execute(Object target) {

    if (target instanceof Client) {
      ((Client) target).stopClient();
    }
  }
}
