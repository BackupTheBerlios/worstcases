package Util.Commands;

import Client.Client;
import java.util.Vector;

/**
*Setzt bei einem Client Informationen über den aktuellen Benutzer. Funktioniert
* mit der Methoder setCurrentUserData der Klasse Client.
*/

public class SetCurrentUserDataCommand implements Command {
    /**
    * Setzt folgende Attribute: Name des Benutzers(paramUserName), hat der Benutzer
    * Administrationsrechte? (paramIsAdmin), Liste der Channels, die der Benutzer
    * betreten darf (paramChannelNames).
    */
    public SetCurrentUserDataCommand(String paramUserName,boolean paramIsAdmin,Vector paramChannelNames) {
        this.userName=paramUserName;
        this.isAdmin=paramIsAdmin;
        this.channelNames = paramChannelNames;
    }

    /** Der Benutzername. */
    String userName;

 /** Ist der Benutzer Administrator? */

    boolean isAdmin=false;

    /**Liste der Channelnamen, die der Benutzer betreten darf.*/
    Vector channelNames;

    /**Ruft Client.setCurrentUserData auf.*/
    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).setCurrentUserData(userName,isAdmin,channelNames);
        }
    }
}
