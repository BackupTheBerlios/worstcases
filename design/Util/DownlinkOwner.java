/**
 * Interface für alle Klassen, die einen Downlink benutzen wollen.
 */
public interface DownlinkOwner {
  /**
   * Diese Methode wird vom Client aufgerufen, wenn er eine Nachricht
   * erhält.
   */
  public abstract void processMsg(String msg) {}
}
