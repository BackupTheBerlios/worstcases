package Util.Commands;

import Client.AdminClient;
import Server.AdminClientServant;

/**
* fordert vom AdminClientServant einen Userdatensatz an
*/
public class GetUserDataCommand implements Command {
    /**
     * setzt die Attribute
     */
    public GetUserDataCommand(String paramUserName) {
        this.userName=paramUserName;
    }

    /** Der Benutzername. */
    String userName;

    /**führt AdminClientServant.sendUser() aus*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).sendUser(userName);
        }
    }
}
