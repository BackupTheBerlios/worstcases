package Util.Debug;

/**
 * Klasse zur Ausgabe von Debug-Nachrichten. Gegen�ber dem �blichen
 * <pre>Debug.println("Wichtiger Fehler!");</pre> 
 * haben so ausgegebene Nachrichten u.a. folgende Vorteile:
 * <ul>
 * <li>sie k�nnen im Code bleiben, m�ssen nicht auskommentiert werden</li>
 * <li>sie sind zur Laufzeit ein- und ausschaltbar</li>
 * <li>verschiedene Priorit�ten erm�glichen eine nach Debug-Level gefilterte 
 * Ausgabe von Debug-Nachrichten</li>
 * </ul>
 */
public class Debug {

  /** Stellt die Ausgabe von Debug-Nachrichten ab. */
  public final static int OFF = 0;
  
  /** Niedrige Priorit�t. */
  public final static int LOW = 1;
  
  /** Mittlere Priorit�t. */
  public final static int MEDIUM = 2;
  
  /** Hohe Priorit�t. */
  public final static int HIGH = 3;

  /**
   * Debug-Level. Je h�her er ist, desto mehr und auch unwichtigere 
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
   * L�st eine Debug-Nachricht mit normaler Priorit�t aus.
   */
  public static void println(Object msg) {
    println(MEDIUM, msg);
  }
  
  /**
   * L�st eine Debug-Nachricht mit einer bestimmten Priorit�t aus.
   *
   * @param priority die Priorit�t der Nachricht (bitte die Klassenkonstanten
   * wie z.B. Debug.MEDIUM verwenden).
   * @param msg die Debug-Nachricht.
   */
  public static void println(int priority, Object msg) {
    if ((HIGH - level) < priority) { //XXX
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
