package Server;

import java.net.Socket;
import java.util.Vector;
import java.util.Enumeration;
import Util.*;

/** Verarbeitet die Anfragen eines AdminClients. */
public class AdminClientServant extends ClientServant implements DownlinkOwner {
    /** Konstruktor, setzt die entsprechenden Attribute. */
    public AdminClientServant(Uplink paramUplink, Downlink paramDownlink, Server paramServer,
        ChannelAdministration paramChannelAdministration, UserAdministration paramUserAdministration, User paramUser) {
            this.setDownlink(paramDownlink);
            this.uplink = paramUplink;
            this.setServer(paramServer);
            this.channelAdministration = paramChannelAdministration;
            this.userAdministration = paramUserAdministration;
            this.setUser(paramUser);
            System.out.println("AdminClient started");
    }

    /** Sendet eine Liste aller Channelnamen. */
    public void sendChannelList() {
      this.sendCommand(new ChannelListCommand(this.channelAdministration.getChannelNames()));
    }

    /** Sendet eine Liste aller Benutzernamen. */
    public void sendUserList() {
     this.sendCommand(new UserListCommand(this.userAdministration.getUserNames()));
    }

    /** Fügt einen Channel hinzu. */
    public void addChannel(String paramName,boolean paramAllowedForGuests,Vector paramAllowedUserNames) {
    }

    /** Löscht den Channel mit dem angegebenen Namen. */
    public void deleteChannel(String channelName) {
        Channel tmpChannel = this.channelAdministration.getFromChannelListByName(channelName);
        this.channelAdministration.removeFromChannelList(tmpChannel);
    }

    /** Verändert die Daten des angegebenen Channels. */
    public void editChannel(String oldName,String newName,boolean paramAllowedForGuest,Vector
        allowedUserNames) {
        Channel tmpChannel=new Channel(newName,paramAllowedForGuest);
        Enumeration enum=allowedUserNames.elements();
        Vector tmpList=new Vector();
        while(enum.hasMoreElements()){
         tmpList.addElement(this.channelAdministration.getFromChannelListByName((String)enum.nextElement()));
        }
        tmpChannel.setAllowedUserList(tmpList.elements());

        this.channelAdministration.editChannel(oldName, tmpChannel);
    }

    /** Fügt einen Benutzer hinzu. */
    public void addUser(String paramName,boolean paramIsAdmin,Vector paramAllowedChannelNames) {
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
