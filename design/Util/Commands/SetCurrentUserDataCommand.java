package Util.Commands;

import Client.Client;
import java.util.Vector;

/**
*Setzt bei einem Client Informationen über den aktuellen Benutzer.
*/

public class SetCurrentUserDataCommand implements Command {
    /**
    *Setzt die Attribute.
     */
    public SetCurrentUserDataCommand(String paramUserName,Vector paramChannelNames) {
        this.userName=paramUserName;
        this.channelNames = paramChannelNames;
    }

    /** Der Benutzername. */
    String userName;
    /**Liste der Channelnamen, die der Benutzer betreten darf.*/
    Vector channelNames;

    /**Ruft Client.setCurrentUserData auf.*/
    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).setCurrentUserData(userName,channelNames);
        } // XXX: else Exception auslösen?
    }
}
