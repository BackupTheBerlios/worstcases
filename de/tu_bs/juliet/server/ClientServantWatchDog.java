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

import java.util.Enumeration;

import de.tu_bs.juliet.util.debug.Debug;


/**
 * Ein Thread, der dafür zuständig ist, 
 * ClientServants aus dem System zu entfernen, 
 * die seit einer bestimmten Zeitspanne
 * keine Nachrichten mehr von ihrem Client empfangen haben.
 * (oder abgestürzt sind)
 */
class ClientServantWatchDog extends Thread {

  private Server server;

  /** Flag, welches angibt, ob der Thread beendet werden soll. */
  public boolean stop = false;

  /** Zeit in Millisekunden, die angibt, 
    * wie lange eine ClientServant inaktiv sein darf. */
  private int timeToLive = 600000;

  /** Zeitspanne in Millisekunden, die vergeht, 
    * bis der ClientServantWatchDog erneut alle ClientServants überprüft. */
  private int updateDelay = 6000;

  /** Konstruktor - Setzt das Server-Attribut. */
  public ClientServantWatchDog(Server paramServer) {
    this.server = paramServer;
  }

  /**
   * Die Runmethode enthält im Wesentlichen eine Schleife, 
   * die solange ausgeführt wird bis stop auf true gesetzt wird,
   * und mittels servant.getClientServantEnum() 
   * die entsprechenden Clientservants überprüft.
   * Für die Überprüfung wird ClientServant.getAliveStamp() 
   * und java.lang.System.currentTimeMillis() benutzt.
   * ClientServants werden ggf. durch ClientServant.stopClientServant() 
   * entfernt.
   * Zur Kontrolle der Schleifendurchläufe wird timeToLive 
   * und updateDelay benutzt.
   */
  public void run() {

    Enumeration enum;
    ClientServant tmpClientServant;

    Debug.println(Debug.MEDIUM,
                  "ClientServantWatchDog: watching: timeToLive: "
                  + this.timeToLive + " updateDelay: " + this.updateDelay);

    while (!stop) {
      enum = this.server.getClientServantEnum();

      while (enum.hasMoreElements()) {
        tmpClientServant = (ClientServant) enum.nextElement();

        /* wenn der ClientServant laenger als timeToLive inaktiv war, 
         * so wird er gestoppt*/       
        if ((java.lang.System.currentTimeMillis()
                - tmpClientServant.getAliveStamp()) > timeToLive) {
          tmpClientServant.stopClientServant();
          Debug.println(Debug.MEDIUM,
                        "ClientServantWatchDog: " + tmpClientServant
                        + ": stopped");
        }
      }

      try {
        sleep(updateDelay);
      } catch (java.lang.InterruptedException e) {
        Debug.println(Debug.HIGH, "ClientServantWatchDog: interrupted: " + e);
      }
    }

    Debug.println(Debug.MEDIUM, "ClientServantWatchDog: stopped");
  }
}
