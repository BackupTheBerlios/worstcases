package Util.Commands;

import Client.AdminClient;
import Server.AdminClientServant;

/**
* Fordert vom AdminClientServant einen Userdatensatz an.
*/
public class GetUserDataCommand implements Command {
    /**
     * Setzt die Attribute.
     */
    public GetUserDataCommand(String paramUserName) {
        this.userName=paramUserName;
    }

    /** Der Benutzername. */
    String userName;

    /**Führt AdminClientServant.sendUser() aus.*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).sendUser(userName);
        }
    }
}
