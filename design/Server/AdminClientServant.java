package Server;

import java.net.Socket;
import Util.*;
import Util.Commands.*;

/** Verarbeitet die Anfragen eines AdminClients. */
public class AdminClientServant extends ClientServant implements DownlinkOwner {
    /** Konstruktor, setzt die entsprechenden Attribute. */
    public AdminClientServant(Uplink paramUplink, Downlink paramDownlink, Server paramServer,
        ChannelAdministration paramChannelAdministration, UserAdministration paramUserAdministration, User paramUser) {
            this.uplink = paramUplink;
            this.downlink = paramDownlink;
            this.server = paramServer;
            this.channelAdministration = paramChannelAdministration;
            this.userAdministration = paramUserAdministration;
            this.user = paramUser;
            System.out.println("AdminClient started");
    }

    /** Sendet eine Liste aller Channelnamen. */
    public void sendChannelList() {
    }

    /** Sendet eine Liste aller Benutzernamen. */
    public void sendUserList() {
    }

    /** Fügt einen Channel hinzu. */
    public void addChannel(String channelSet) {
    }

    /** Löscht den Channel mit dem angegebenen Namen. */
    public void deleteChannel(String channelName) {
        Channel tmpChannel = this.channelAdministration.getFromChannelListByName(channelName);
        this.channelAdministration.removeFromChannelList(tmpChannel);
    }

    /** Verändert die Daten des angegebenen Channels. */
    public void editChannel(String channelName, Channel newChannel) {
        this.channelAdministration.editChannel(channelName, newChannel);
    }

    /** Fügt einen Benutzer hinzu. */
    public void addUser(User paramUser) {
        this.userAdministration.addToUserList(paramUser);
    }

    /** Löscht den Benutzer mit dem angegebenen Namen. */
    public void deleteUser(String userName) {
        System.out.println(userName + " deleted");
    }

    /**
     * Ändert die Benutzerdaten des Benutzers mit dem angegebenen Namen.
     * Setzt die neuen Daten des Benutzers mit den Daten aus newUserSet.
     */
    public void editUser(String oldName, User paramUser) {
        this.deleteUser(oldName);
        this.addUser(paramUser);
    }

    /** Sendet die Benutzerdaten des Benutzers mit dem angegebenen Namen. */
    public void sendUser(String userName) {
        String tmpUserSet = this.userAdministration.getFromUserListByName(userName).toString();
    }

    /** Sendet die Channeldaten des Channels mit dem angegebenen Namen */
    public void sendChannel(String channelName) {
        String tmpChannelSet = this.channelAdministration.getFromChannelListByName(channelName).toString();
    }

    /**
     * @clientCardinality 0..1
     * @supplierCardinality 1
     */
    private ChannelAdministration channelAdministration;
}
