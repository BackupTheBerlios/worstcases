package Util.Commands;

import Client.AdminClient;
import Server.AdminClientServant;

/**
* Fordert vom AdminClientServant einen Channeldatensatz an.
*/
public class GetChannelDataCommand implements Command {
    /**
     * Setzt die Attribute.
     */
    public GetChannelDataCommand(String paramChannelName) {
        this.channelName=paramChannelName;
    }

    /** Der Channelname. */
    String channelName;

    /**Führt AdminClientServant.sendChannel() aus.*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).sendChannel(channelName);
        }
    }
}
