package Util.Commands;

import Server.ClientServant;
import Client.Client;


/**
 * Dieses Command wird von ClientServant gesendet, falls ein Benutzer eine
 * Privatnachricht an einen anderen Benutzer verschicken möchte.
 */
public class SendMsgFromUserCommand implements Command {

  /**
   * Setzt die Attribute. Einmal den Text, der versendet werden soll (paramMsg) und
   * den Absender (fromName)
   */
  public SendMsgFromUserCommand(String fromName, String paramMsg) {
    this.name = fromName;
    this.msg = paramMsg;
  }

  /** Die Nachricht, die versendet werden soll. */
  String msg;

  /** Der Benutzername des Absenders. */
  String name;

  /** Führt beim Client sendMsgFromUser() aus. */
  public void execute(Object target) {

    if (target instanceof Client) {
      ((Client) target).sendMsgFromUser(name, msg);
    }
  }
}
