package Util.Commands;

import Client.AdminClient;
import Server.ClientServant;

/**
* fordert vom AdminClientServant die komplette Userliste an
*/
public class GetUserListCommand implements Command {

    /**führt AdminClientServant.sendUserList() aus*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).sendUserList();
        }
    }
}
