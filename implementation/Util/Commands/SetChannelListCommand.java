package Util.Commands;

import Client.AdminClient;
import java.util.Vector;

/**
* Setzt beim AdminClient die Namensliste aller verf�gbaren Channels, mit der Methode
* setChannelList.
*/

public class SetChannelListCommand implements Command {
    /**
    * Setzt das entsprechende Attribut.
     */
    public SetChannelListCommand(Vector list) {
        this.channelNames = list;
    }

    /**Namensliste aller Channels.*/

    Vector channelNames = new Vector();

    /**F�hrt beim AdminClient setChannelList() aus.*/

    public void execute(Object target) {
        if (target instanceof AdminClient) {
            ((AdminClient)target).setChannelList(channelNames);
        } // XXX: else Exception ausl�sen?
    }
}
