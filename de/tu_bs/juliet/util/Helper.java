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

package de.tu_bs.juliet.util;

import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Enumeration;
import de.tu_bs.juliet.util.debug.Debug;


/** Klasse mit Hilfsalgorithmen. */
public class Helper {

  /** Hilfsmethode für quicksort(). */
  private static Vector quicksortHelp(Vector list, int l, int m) {

    // Standard-Implementation
    int i, j;
    boolean down;
    String w;

    if (l < m) {
      i = l;
      j = m;
      w = (String) list.elementAt(m);
      down = false;

      while (i < j) {
        if (down) {
          if (((String) list.elementAt(j)).compareTo(w) <= 0) {
            list.setElementAt(list.elementAt(j), i);

            down = false;
          }
        } else {
          if (((String) list.elementAt(i)).compareTo(w) > 0) {
            list.setElementAt(list.elementAt(i), j);

            down = true;
          }
        }

        if (down) {
          j--;
        } else {
          i++;
        }
      }

      list.setElementAt(w, i);
      quicksortHelp(list, l, i - 1);
      quicksortHelp(list, i + 1, m);
    }

    return list;
  }

  /** Sortiert einen Vector von Strings. */
  public static Vector quicksort(Vector list) {

    Vector tmpVector = vectorCopy(list);

    if ((tmpVector != null) && (tmpVector.size() != 0)) {
      return (quicksortHelp(tmpVector, 0, tmpVector.size() - 1));
    } else {
      return (new Vector());
    }
  }

  /** Gibt eine Kopie des Vector zurück. */
  public static Vector vectorCopy(Vector vector) {

    Enumeration enum = vector.elements();
    Vector copyVector = new Vector();

    try {
      while (enum.hasMoreElements()) {
        copyVector.addElement(enum.nextElement());
      }
    }

    // tritt evtl. auf, falls aus vector während der Schleife Elemente entfernt werden
    catch (java.util.NoSuchElementException e) {
      Debug.println(Debug.HIGH, "copyVector: error while copying: " + e);
    }

    return copyVector;
  }

  public static void main(String[] args) {

    Vector tmpVector = new Vector();

    for (int i = 0; i < args.length; i++) {
      tmpVector.addElement(args[i]);
    }

    Enumeration enum = quicksort(tmpVector).elements();

    while (enum.hasMoreElements()) {
      System.out.println(enum.nextElement());
    }
  }
}
