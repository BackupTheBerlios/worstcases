package Util.Commands;

import Server.ClientServant;
import Client.Client;
import java.util.Vector;

public class UserDataCommand implements Command {
    /**
     * Konstruktor. Erzeugt einen Befehl, mit dem eine Nachricht an einen
     * Channel gesendet werden kann. Dieser Befehl kann nur von einem ClientServant verarbeitet werden.
     * @param msg die Nachricht.
     */
    public UserDataCommand(String paramUserName,Vector paramChannelNames) {
        this.userName=paramUserName;
        this.channelNames = paramChannelNames;
    }

    /** Der Benutzername. */
    String userName;
    Vector channelNames;

    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).setUserData(userName,channelNames);
        } // XXX: else Exception auslösen?
    }
}
