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

package de.tu_bs.juliet;

import de.tu_bs.juliet.client.AdminClient;

/** 
 * Testklasse aus der Debug-Phase, dient dazu, eine bestimmte Anzahl von 
 * Clients zu starten, die kontinuierlich Meldungen abgeben.
 */
public class SpawnClient extends Thread {

  // Zahl der gewünschten Clients
  public int numClients = 20;
  
  // Dauer der "Schlafphase" des Threads
  public long sleepDelay = 1000;

  // run-Methode des Threads
  public void run() {
    
    AdminClient[] arr = new AdminClient[numClients];

    /* In der For-Schleife werden die Clients gestartet,
       loggen sich als Gast ein, gehen ins Foyer.
     */
    for (int i = 0; i < arr.length; i++) {
      arr[i] = new AdminClient();

      arr[i].setServerIP("134.169.8.196");
      arr[i].startClient();
      arr[i].loginAsGuest("guest_spawn_" + i);
      arr[i].joinChannel("Foyer");
    }

    while (true) {
      for (int i = 0; i < arr.length; i++) {
        arr[i].sendMsgToChannel("hello");
      }

      try {
        SpawnClient.sleep(sleepDelay);
      } catch (java.lang.InterruptedException e) {
        System.out.println(e);
      }
    }
  }

  public static void main(String[] args) {

    SpawnClient spawnClient = new SpawnClient();

    spawnClient.start();
  }
}
