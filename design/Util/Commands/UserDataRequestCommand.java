package Util.Commands;

import Server.ClientServant;
import Client.AdminClient;
import java.util.Vector;

public class UserDataRequestCommand implements Command {
    /**
     * Konstruktor. Erzeugt einen Befehl, mit dem eine Nachricht an einen
     * Channel gesendet werden kann. Dieser Befehl kann nur von einem ClientServant verarbeitet werden.
     * @param msg die Nachricht.
     */
    public UserDataRequestCommand(String paramUserName,String paramUserPassword,boolean paramIsAdmin,Vector paramChannelNames) {
        this.userName=paramUserName;
	this.password=paramUserPassword;
	this.isAdmin=paramIsAdmin;
        this.channelNames = paramChannelNames;
    }

    /** Der Benutzername. */
    String userName;
    String password;
    boolean isAdmin=false;
    Vector channelNames;

    public void execute(Object target) {
        if (target instanceof AdminClient) {
            ((AdminClient)target).setUserRequestData(userName,password,isAdmin,channelNames);
        } // XXX: else Exception auslösen?
    }
}
