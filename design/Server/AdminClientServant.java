package Server;

import java.net.Socket;
import java.util.Vector;
import java.util.Enumeration;
import Util.*;
import Util.Commands.*;

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
      Channel tmpChannel=new Channel(paramName,paramAllowedForGuests);
      Enumeration enum=paramAllowedUserNames.elements();
      while(enum.hasMoreElements()){
       tmpChannel.addToAllowedUserList(this.userAdministration.getFromUserListByName((String)enum.nextElement()));
      }
      this.channelAdministration.addToChannelList(tmpChannel);

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
    public void addUser(String paramName,String paramPassword,boolean paramIsAdmin,Vector paramAllowedChannelNames) {
     User tmpUser=new User(paramName,paramPassword,false,paramIsAdmin,this.userAdministration);
     Enumeration enum=paramAllowedChannelNames.elements();
     while(enum.hasMoreElements()){
      tmpUser.addToAllowedChannelList(this.channelAdministration.getFromChannelListByName((String)enum.nextElement()));

     }
     this.userAdministration.addToUserList(tmpUser);

    }

    /** Löscht den Benutzer mit dem angegebenen Namen. */
    public void deleteUser(String userName) {
      User tmpUser=this.userAdministration.getFromUserListByName(userName);
      this.userAdministration.removeFromUserList(tmpUser);
    }

    /**
     * Ändert die Benutzerdaten des Benutzers mit dem angegebenen Namen.
     * Setzt die neuen Daten des Benutzers mit den Daten aus newUserSet.
     */
    public void editUser(String oldName, String newName, String newPassword,boolean paramIsAdmin,Vector allowedChannelNames) {
       User tmpUser=new User(newName,newPassword,false,paramIsAdmin,this.userAdministration);
       Enumeration enum=allowedChannelNames.elements();
       Vector tmpList=new Vector();
       while(enum.hasMoreElements()){
        tmpList.addElement(this.channelAdministration.getFromChannelListByName((String)enum.nextElement()));
       }
       tmpUser.setAllowedChannelList(tmpList.elements());
       this.userAdministration.editUser(oldName,tmpUser);


    }

    /** Sendet die Benutzerdaten des Benutzers mit dem angegebenen Namen. */
    public void sendUser(String userName) {
      User tmpUser=this.userAdministration.getFromUserListByName(userName);
      String tmpName=tmpUser.getName();
      String tmpPassword=tmpUser.getPassword();
      boolean tmpIsAdmin=tmpUser.isAdmin();
      Vector tmpAllowedChannelList=tmpUser.getAllowedChannelNames();
      this.sendCommand(new Util.Commands.UserDataRequestCommand(tmpName,tmpPassword,tmpIsAdmin,tmpAllowedChannelList));
    }

    /** Sendet die Channeldaten des Channels mit dem angegebenen Namen */
    public void sendChannel(String channelName) {
      Channel tmpChannel=this.channelAdministration.getFromChannelListByName(channelName);
      String tmpName=tmpChannel.getName();
      boolean tmpIsAllowedForGuest=tmpChannel.isAllowedForGuest();
      Vector tmpAllowedUserList=tmpChannel.getAllowedUserNames();
      this.sendCommand(new Util.Commands.ChannelDataRequestCommand(tmpName,tmpIsAllowedForGuest,tmpAllowedUserList));
    }

    /**
     * @clientCardinality 0..1
     * @supplierCardinality 1
     */
    private ChannelAdministration channelAdministration;
}
