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
package de.tu_bs.juliet.util.debug;

/**
 * Klasse zur Ausgabe von Debug-Nachrichten. Gegenüber dem üblichen
 * <pre>System.out.println("Wichtiger Fehler!");</pre>
 * haben so ausgegebene Nachrichten u.a. folgende Vorteile:
 * <ul>
 * <li>sie können im Code bleiben, müssen nicht auskommentiert werden</li>
 * <li>sie sind zur Laufzeit ein- und ausschaltbar</li>
 * <li>verschiedene Prioritäten ermöglichen eine nach Debug-Level gefilterte
 * Ausgabe von Debug-Nachrichten</li>
 * </ul>
 */
public class Debug {

  /** Stellt die Ausgabe von Debug-Nachrichten ab. */
  public final static int OFF = 0;

  /** Niedrige Priorität. */
  public final static int LOW = 1;

  /** Mittlere Priorität. */
  public final static int MEDIUM = 2;

  /** Hohe Priorität. */
  public final static int HIGH = 3;

  /**
   * Debug-Level. Je höher er ist, desto mehr und auch unwichtigere
   * Debug-Nachrichten werden ausgegeben.
   * Der Wert Debug.OFF stellt Debug-Nachrichten ab.
   */
  private static int level = HIGH;

  /**
   * Stellt den Debug-Level ein.
   *
   * @param newLevel der neue Debug-Level, bitte die Klassenkonstanten wie
   * z.B. Debug.MEDIUM verwenden.
   */
  public static void setLevel(int newLevel) {
    level = newLevel;
  }

  /**
   * Löst eine Debug-Nachricht mit normaler Priorität aus.
   */
  public static void println(Object msg) {
    println(MEDIUM, msg);
  }

  /**
   * Löst eine Debug-Nachricht mit einer bestimmten Priorität aus.
   * @param priority die Priorität der Nachricht (bitte die Klassenkonstanten
   * wie z.B. Debug.MEDIUM verwenden).
   * @param msg die Debug-Nachricht.
   */
  public static void println(int priority, Object msg) {

    if ((HIGH - level) < priority) {  // XXX
      System.err.println(msg);
    }
  }

  /**
   * Beispiele.
   */
  public static void main(String args[]) {

    Debug.setLevel(Debug.LOW);
    Debug.println(Debug.LOW, "Unwichtiger Fehler!");
    Debug.println("Normaler Fehler!");
    Debug.println(Debug.HIGH, "Wichtiger Fehler!");
  }
}
