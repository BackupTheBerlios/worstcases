package Util.Commands;

import Client.Client;
import java.util.Vector;

/**
* Gibt einem Client Informationen über den momentan besuchten Channel.
*/

public class SetCurrentChannelDataCommand implements Command {
    /**
     *Setzt folgende Attribute: Name des besuchten Channels (paramChannelName) und
     * die Liste der Benutzer, die sich z.Zt. in diesem Channel befinden (paramUserNames).
     */

    public SetCurrentChannelDataCommand(String paramChannelName,Vector paramUserNames) {
        this.channelName=paramChannelName;
        this.userNames = paramUserNames;
    }

    /** Der Channelname. */
    String channelName;

    /**Die Namen derjenigen Benutzer, die sich momentan im Channel aufhalten.*/
    Vector userNames;

    /**Ruft Client.setCurrentChannelData() auf.*/
    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).setCurrentChannelData(channelName,userNames);
        } 
    }
}
