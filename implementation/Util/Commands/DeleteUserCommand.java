package Util.Commands;

import Server.AdminClientServant;

/** Wird von einem AdminClient gesendet,
 *löscht einen User.
 */

public class DeleteUserCommand implements Command {
    /** Setzt den Namen.
     */
    public DeleteUserCommand(String paramName) {
        this.name = paramName;
    }

    /** Der Benutzername. */
    String name;

    /**Ruft beim AdminClientServant deleteUser() auf.*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).deleteUser(name);
        } // XXX: else Exception auslösen?
    }
}
