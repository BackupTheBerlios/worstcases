package Util.Commands;

import Server.ClientServant;


/**
 * Wird von einem Client gesendet, wenn eine Nachricht an einen Benutzer
 * gesendet werden soll (Privatchat). Dazu wird von diesem Command die
 * Methode sendMsgToUser der Klasse ClientServant aufgerufen.
 */
public class SendMsgToUserCommand implements Command {

  /**
   * Es werden folgende Parameter an den Konstruktor übergeben. Der Name des
   *   Empfängers der Nachricht (paramName) und die Nachricht selbst
   *   (paramMsg).
   */
  public SendMsgToUserCommand(String paramName, String paramMsg) {
    this.msg = paramMsg;
    this.name = paramName;
  }

  /** Die Nachricht. */
  String msg;

  /** Der Benutzername desEmpfängers. */
  String name;

  /** Ruft beim ClientServant sendMsgToUser() auf. */
  public void execute(Object target) {

    if (target instanceof ClientServant) {
      ((ClientServant) target).sendMsgToUser(name, msg);
    }
  }
}
