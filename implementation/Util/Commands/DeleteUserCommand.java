package Util.Commands;

import Server.AdminClientServant;


/**
 * Wird von einem AdminClient gesendet, um einen Benutzer zu l�schen,
 *   indem die Methode deleteUser() der Klasse AdminClientServant auf-
 *   gerufen wird.
 */
public class DeleteUserCommand implements Command {

  /**
   * Setzt den Namen des zu l�schenden Benutzers.
   */
  public DeleteUserCommand(String paramName) {
    this.name = paramName;
  }

  /** Der Benutzername des zu l�schenden Benutzers. */
  String name;

  /**
   * Ruft beim AdminClientServant die Methode deleteUser() auf.
   *    und �bergibt den Namen des zu l�schenden Benutzers.
   */
  public void execute(Object target) {

    if (target instanceof AdminClientServant) {
      ((AdminClientServant) target).deleteUser(name);
    }  // XXX: else Exception ausl�sen?
  }
}
