/**
 * Interface für alle Klassen, die einen Downlink benutzen wollen.
 */
package Util;

public interface DownlinkOwner {

  /**
   * Diese Methode wird vom Client aufgerufen, wenn er eine Nachricht
   * erhält.
   */
  public void processMsg(Command msg);
}
