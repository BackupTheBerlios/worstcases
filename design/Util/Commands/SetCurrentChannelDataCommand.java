package Util.Commands;

import Client.Client;
import java.util.Vector;

/**
* setzt bei einem Client Informationen �ber den momentan besuchten Channel
*/

public class SetCurrentChannelDataCommand implements Command {
    /**
     *setzt die Attribute
     */
    public SetCurrentChannelDataCommand(String paramChannelName,Vector paramUserNames) {
        this.channelName=paramChannelName;
        this.userNames = paramUserNames;
    }

    /** Der Channelname. */
    String channelName;
    /**die Namen derjenigen Benutzer, die sich momentan im Channel aufhalten*/
    Vector userNames;

    /**ruft Client.setCurrentChannelData() auf*/
    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).setCurrentChannelData(channelName,userNames);
        } // XXX: else Exception ausl�sen?
    }
}
