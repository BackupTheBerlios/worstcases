/**
 * Interface f�r alle Klassen, die einen Downlink benutzen wollen.
 */
public interface DownlinkOwner {
  /**
   * Diese Methode wird vom Client aufgerufen, wenn er eine Nachricht
   * erh�lt.
   */
  public abstract void processMsg(String msg) {}
}
