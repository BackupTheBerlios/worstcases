package Util.Commands;

import Client.AdminClient;
import Server.AdminClientServant;

/**
* fordert vom AdminClientServant die komplette Channelliste an
*/
public class GetChannelListCommand implements Command {

    /**führt AdminClientServant.sendChannelList() aus*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).sendChannelList();
        }
    }
}
