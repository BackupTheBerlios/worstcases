package Util.Commands;

import Client.AdminClient;
import Server.AdminClientServant;


/**
 * Fordert vom AdminClientServant einen Userdatensatz an, indem die Methode
 * sendUser ausgeführt wird.
 */
public class GetUserDataCommand implements Command {

  /**
   * Setzt die Attribute, hier also den Namen der Benutzers, dessen
   * daten gefordert werden.
   */
  public GetUserDataCommand(String paramUserName) {
    this.userName = paramUserName;
  }

  /** Der Benutzername des Benutzers, dessen Daten gefordert werden. */
  String userName;

  /** Führt AdminClientServant.sendUser() aus. */
  public void execute(Object target) {

    if (target instanceof AdminClientServant) {
      ((AdminClientServant) target).sendUser(userName);
    }
  }
}
