package Util.Commands;

import Client.AdminClient;
import java.util.Vector;

/**Setzt beim AdminClient eine Liste aller vorhandenen Usernamen*/
public class SetUserListCommand implements Command {
    public SetUserListCommand(Vector list) {
        this.userNames = list;
    }

    /**List der Usernamen*/
    Vector userNames = new Vector();

    /**F�hrt beim AdminClient setUserList() aus*/
    public void execute(Object target) {
        if (target instanceof AdminClient) {
            ((AdminClient)target).setUserList(userNames);
        } // XXX: else Exception ausl�sen?
    }
}
