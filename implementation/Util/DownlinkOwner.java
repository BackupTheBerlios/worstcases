package Util;

import Util.Commands.Command;


/** Interface für alle Klassen, die einen Downlink benutzen wollen. */
public interface DownlinkOwner {

  /** 
   * Diese Methode wird vom Benutzer des Downlinks aufgerufen, wenn er 
   * eine Nachricht erhält. 
   */
  public void processMsg(Command msg);

  /** 
   * Diese Methode wird vom Benutzer des Downlinks aufgerufen, wenn ein 
   * Fehler im Zusammenhang mit dem Downlink auftritt. 
   */
  public void downlinkError();

  /** 
   * Diese Methode wird vom Benutzer des Downlinks aufgerufen, um den 
   * Downlink zu setzen. 
   */
  public void setDownlink(Downlink paramDownlink);
}
