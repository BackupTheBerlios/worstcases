package Util.Commands;

import Server.ClientServant;
import Client.Client;
import java.util.Vector;

public class CurrentChannelDataCommand implements Command {
    /**
     * Konstruktor. Erzeugt einen Befehl, mit dem eine Nachricht an einen
     * Channel gesendet werden kann. Dieser Befehl kann nur von einem ClientServant verarbeitet werden.
     * @param msg die Nachricht.
     */
    public CurrentChannelDataCommand(String paramChannelName,Vector paramUserNames) {
        this.channelName=paramChannelName;
        this.userNames = paramUserNames;
    }

    /** Der Benutzername. */
    String channelName;
    Vector userNames;

    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).setChannelData(channelName,userNames);
        } // XXX: else Exception auslösen?
    }
}
