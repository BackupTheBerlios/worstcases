package Client;

import java.util.Vector;
import java.net.Socket;
import Util.Commands.*;


/** der erweiterte AdminClient */
public class AdminClient extends Client {
    private String tmpChannelName;
    private boolean tmpAllowedForGuests;
    private Vector tmpAllowedUserNames;

    private String tmpUserName;
    private String tmpUserPassword;
    private boolean tmpIsAdmin;
    private Vector tmpAllowedChannelNames;


    /** fügt einen Channel mit den Daten aus dem String channel hinzu. Hiermit läßt sich ein neuer Channel erzeugen. */
    public void addChannel(String paramName,boolean paramAllowedForGuests,Vector tmpAllowedUserList) {


    }

    public void setUserRequestData(String userName,String password,boolean isAdmin,Vector channelNames){}

    public void setChannelRequestData(String channelName,boolean isAllowedForGuest,Vector userNames){}

    public void setUserList(Vector list){
    }

    public void setChannelList(Vector list){
    }

    /** löscht einen Channel */
    public void deleteChannel(String paramName) {
    }

    /**
     * setzt den Channel mit dem angegebenen Namen auf den neuen Datensatz aus dem String channel.
     * Diese Methode wird verwendet, um Channeldaten zu verändern.
     */
    public void editChannel(String name, String channel) { }

    /** fügt den Benutzer mit den Daten aus user hinzu */
    public void addUser(String user) { }

    /** löscht den Benutzer mit dem Namen name */
    public void deleteUser(String name) {

      this.sendCommand(new DeleteUserCommand(name));

    }

    /** editiert einen Benutzer */
    public void editUser(String name, String channel) { }

    /**
     * verarbeitet erweiterte Nachrichten, d.h. auch Nachrichten die für die normalen
     * Clients irrelevant sind, z.B. Änderungsmeldungen über Benutzer- und Channeldaten.
     * Erweitert also Client.processMsg(String msg)
     */
    public void processMsg(String msg) { }

    /** ein Benutzerdatensatz als String */
    private String userSet;

    /** ein Channeldatensatz als String */
    private String channelSet;

    /** Liste aller Benutzernamen */
    private Vector allUserList;

    /** Liste aller Channelnamen */
    private Vector allChannelList;
}
