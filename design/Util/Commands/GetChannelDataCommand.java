package Util.Commands;

import Client.AdminClient;
import Server.AdminClientServant;

/**
* fordert vom AdminClientServant einen Channeldatensatz an
*/
public class GetChannelDataCommand implements Command {
    /**
     * setzt die Attribute
     */
    public GetChannelDataCommand(String paramChannelName) {
        this.channelName=paramChannelName;
    }

    /** Der Channelname. */
    String channelName;

    /**führt AdminClientServant.sendChannel() aus*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).sendChannel(channelName);
        }
    }
}
