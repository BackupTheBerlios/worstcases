package Util.Commands;

import Server.ClientServant;
import Client.AdminClient;
import java.util.Vector;

public class ChannelDataRequestCommand implements Command {
    /**
     * Konstruktor. Erzeugt einen Befehl, mit dem eine Nachricht an einen
     * Channel gesendet werden kann. Dieser Befehl kann nur von einem ClientServant verarbeitet werden.
     * @param msg die Nachricht.
     */
    public ChannelDataRequestCommand(String paramChannelName,boolean paramIsAllowedForGuest,Vector paramUserNames) {
        this.channelName=paramChannelName;
	this.isAllowedForGuest=paramIsAllowedForGuest;
        this.userNames = paramUserNames;
    }

    /** Der Benutzername. */
    String channelName;
    boolean isAllowedForGuest=false;
    Vector userNames;

    public void execute(Object target) {
        if (target instanceof AdminClient) {
            ((AdminClient)target).setChannelRequestData(channelName,isAllowedForGuest,userNames);
        } // XXX: else Exception auslösen?
    }
}
