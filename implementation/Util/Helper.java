package Util;

import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Enumeration;
import Util.Debug.Debug;


/** Klasse mit Hilfsalgorithmen. */
public class Helper {

  /** Hilfsmethode f�r quicksort(). */
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

  /** Gibt eine Kopie des Vector zur�ck. */
  public static Vector vectorCopy(Vector vector) {

    Enumeration enum = vector.elements();
    Vector copyVector = new Vector();

    try {
      while (enum.hasMoreElements()) {
        copyVector.addElement(enum.nextElement());
      }
    }

    // tritt evtl. auf, falls aus vector w�hrend der Schleife Elemente entfernt werden
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
