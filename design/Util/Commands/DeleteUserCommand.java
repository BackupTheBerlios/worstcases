package Util.Commands;

import Server.AdminClientServant;

/** wird von einem AdminClient gesendet
 *löscht einen User
 */

public class DeleteUserCommand implements Command {
    /** setzt den Namen
     */
    public DeleteUserCommand(String paramName) {
        this.name = paramName;
    }

    /** Der Benutzername. */
    String name;

    /**ruft beim AdminClientServant deleteUser() auf*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).deleteUser(name);
        } // XXX: else Exception auslösen?
    }
}
