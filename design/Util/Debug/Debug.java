package Util.Debug;

/**
 * Klasse zur Ausgabe von Debug-Nachrichten. Gegenüber dem üblichen
 * <pre>System.out.println("Gaaaaanz wichtiger Fehler!");</pre> 
 * haben so ausgegebene Nachrichten u.a. folgende Vorteile:
 * <ul>
 * <li>sie können im Code bleiben, müssen nicht auskommentiert werden</li>
 * <li>sie sind zur Laufzeit ein- und ausschaltbar</li>
 * <li>verschiedene Prioritäten ermöglichen eine nach Debug-Level gefilterte 
 * Ausgabe von Debug-Nachrichten</li>
 * </ul>
 */
public class Debug {

  public final static int OFF = 0;
  public final static int LOW = 1;
  public final static int MEDIUM = 2;
  public final static int HIGH = 3;

  /**
   * Debug-Level. Je höher er ist, desto mehr und auch unwichtigere 
   * Debug-Nachrichten werden ausgegeben.
   * Der Wert Debug.OFF stellt Debug-Nachrichten ab.
   */
  private static int level = MEDIUM;

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
  public static void println(String msg) {
    println(MEDIUM, msg);
  }
  
  /**
   * Löst eine Debug-Nachricht mit einer bestimmten Priorität aus.
   *
   * @param priority die Priorität der Nachricht (bitte die Klassenkonstanten
   * wie z.B. Debug.MEDIUM verwenden).
   * @param msg die Debug-Nachricht.
   */
  public static void println(int priority, String msg) {
    if ((HIGH - level) < priority) { //XXX
      System.err.println(msg);
    }
  }

  /**
   * Beispiele.
   */
  public static void main(String args[]) {
    /*
     * Der unwichtige Fehler wird nicht ausgegeben, weil
     * der Debug-Level standardmäßig auf MEDIUM steht. 
     */
    Debug.println(Debug.LOW, "Unwichtiger Fehler!");
    Debug.println(Debug.MEDIUM, "Normaler Fehler!");
    Debug.println(Debug.HIGH, "Wichtiger Fehler!");
  }
}
