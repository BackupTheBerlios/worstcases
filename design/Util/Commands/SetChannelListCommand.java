package Util.Commands;

import Client.AdminClient;
import java.util.Vector;

/**
* setzt beim AdminClient die Namensliste aller verf�gbaren Channels
*/

public class SetChannelListCommand implements Command {
    /**
    * setzt das entsprechende Attribut
     */
    public SetChannelListCommand(Vector list) {
        this.channelNames = list;
    }

    /**Namensliste aller Channels*/
    Vector channelNames = new Vector();

    /**f�hrt beim AdminClient setChannelList() aus*/
    public void execute(Object target) {
        if (target instanceof AdminClient) {
            ((AdminClient)target).setChannelList(channelNames);
        } // XXX: else Exception ausl�sen?
    }
}
