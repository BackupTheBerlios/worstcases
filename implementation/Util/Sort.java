package Util;

import java.util.Vector;
import java.util.Enumeration;

/** Klasse mit Sortieralgorithmen */
public class Sort {
    /** Hilfsmethode für quicksort() */
    private static Vector quicksortHelp(Vector list, int l, int m) {
        int i, j;
        boolean down;
        String w;
        if (l < m) {
            i = l;
            j = m;
            w = (String)list.elementAt(m);
            down = false;
            while (i < j) {
                if (down) {
                    if (((String)list.elementAt(j)).compareTo(w) <= 0) {
                        list.setElementAt(list.elementAt(j), i);
                        down = false;
                    }
                }
                else {
                    if (((String)list.elementAt(i)).compareTo(w) > 0) {
                        list.setElementAt(list.elementAt(i), j);
                        down = true;
                    }
                }
                if (down) { j--; } else { i++; }
            }
            list.setElementAt(w, i);
            quicksortHelp(list, l, i - 1);
            quicksortHelp(list, i + 1, m);
        }
        return list;
    }

    /** sortiert einen Vector von Strings */
    public static Vector quicksort(Vector list) {
        if ((list != null) && (list.size()!=0)) {
            return (quicksortHelp(list, 0, list.size() - 1));
        }
        else {
            return (
                new Vector());
        }
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
