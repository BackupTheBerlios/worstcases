package Util.Commands;

import Client.AdminClient;
import java.util.Vector;

/**Setzt beim AdminClient eine Liste aller vorhandenen Usernamen*/

public class SetUserListCommand implements Command {

/** Der Konstruktor bekommt alle vorhandenen User übergeben. */

    public SetUserListCommand(Vector list) {
	this.userNames = list;
    }

    /**Liste der Usernamen*/
    Vector userNames = new Vector();

    /**Führt beim AdminClient setUserList() aus*/

    public void execute(Object target) {
        if (target instanceof AdminClient) {
            ((AdminClient)target).setUserList(userNames);
        } 
    }
}
