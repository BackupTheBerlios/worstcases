package Util.Commands;

import Server.ClientServant;
import Client.Client;
public class SendMsgFromChannelCommand implements Command {
  
  /**
   * Konstruktor. Erzeugt einen Befehl, mit dem eine Nachricht an einen
   * Channel gesendet werden kann. Dieser Befehl kann nur von einem
   * ClientServant verarbeitet werden.
   *
   * @param msg die Nachricht.
   */
  public SendMsgFromChannelCommand(String paramMsg) {
   this.msg=paramMsg;
  }

  /** Der Benutzername. */
  String msg;
   
  public void execute(Object target) {
    if (target instanceof Client) {
      ((Client) target).sendMsgFromChannel(msg);
    } // XXX: else Exception auslösen?
  }
}
