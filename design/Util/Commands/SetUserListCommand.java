package Util.Commands;

import Client.AdminClient;
import java.util.Vector;

public class SetUserListCommand implements Command {
    /**
     */
    public SetUserListCommand(Vector list) {
        this.userNames = list;
    }

    Vector userNames = new Vector();

    public void execute(Object target) {
        if (target instanceof AdminClient) {
            ((AdminClient)target).setUserList(userNames);
        } // XXX: else Exception auslösen?
    }
}
