package Util.Commands;

import Client.AdminClient;
import Server.AdminClientServant;


/**
 * Fordert vom AdminClientServant die komplette Userliste an, indem die
 * Methode sendUserList aufgerufen wird. Parameter sind hier nicht
 * notwendig, da die Liste aller Benutzer angefordert wird.
 */
public class GetUserListCommand implements Command {

  /** Führt AdminClientServant.sendUserList() aus. */
  public void execute(Object target) {

    if (target instanceof AdminClientServant) {
      ((AdminClientServant) target).sendUserList();
    }
  }
}
