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
    public SetCurrentUserDataCommand(String paramUserName,boolean paramIsAdmin,Vector paramChannelNames,String paramCurrentChannelName) {
        this.userName=paramUserName;
        this.isAdmin=paramIsAdmin;
        this.channelNames = paramChannelNames;
        this.currentChannelName =paramCurrentChannelName;
    }

    /** Der Benutzername. */
    String userName;
    /**Adminstatus*/
    boolean isAdmin=false;
    /**Liste der Channelnamen, die der Benutzer betreten darf.*/
    Vector channelNames;
    /**Name des momentan besuchten Channels*/
    String currentChannelName;

    /**Ruft Client.setCurrentUserData auf.*/
    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).setCurrentUserData(userName,isAdmin,channelNames,currentChannelName);
        } // XXX: else Exception auslösen?
    }
}
