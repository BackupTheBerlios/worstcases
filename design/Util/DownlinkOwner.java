/**
 * Interface f�r alle Klassen, die einen Downlink benutzen wollen.
 */
package Util;

public interface DownlinkOwner {

  /**
   * Diese Methode wird vom Client aufgerufen, wenn er eine Nachricht
   * erh�lt.
   */
  public void processMsg(Command msg);
}
