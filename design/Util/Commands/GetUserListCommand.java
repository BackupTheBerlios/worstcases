package Util.Commands;

import Client.AdminClient;
import Server.AdminClientServant;

/**
* Fordert vom AdminClientServant die komplette Userliste an.
*/
public class GetUserListCommand implements Command {

    /**Führt AdminClientServant.sendUserList() aus.*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).sendUserList();
        }
    }
}
