package Server;

import java.net.Socket;
import java.util.Vector;
import java.util.Enumeration;
import Util.*;
import Util.Commands.*;

/** Verarbeitet die Anfragen eines AdminClients.*/
public class AdminClientServant extends ClientServant implements DownlinkOwner {
    /** Konstruktor, setzt die entsprechenden Attribute.
     *  Benutzt setDownlink(),setServer() und setUser().
     * "Entreisst" somit dem aufrufenden ClientServant die Objekte.
     */
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


    /**
     * Sendet eine Liste aller Channelnamen. Benutzt sendCommand() und erzeugt ein neues SetChannelListCommand - Objekt.
     * Benutzt channelAdministration.getChannelNames(), um die Namensliste zu erzeugen
     */
    public synchronized void sendChannelList() {
        this.sendCommand(
            new SetChannelListCommand(this.channelAdministration.getChannelNames()));
    }

    /**
     * Sendet eine Liste aller Benutzernamen. Benutzt sendCommand und erzeugt ein neues SetUserListCommand-Objekt
     * Benutzt userAdministration.getUserNames(), um die Namensliste zu erzeugen
     */
    public  synchronized  void sendUserList() {
        this.sendCommand(
            new SetUserListCommand(this.userAdministration.getUserNames()));
    }

    /**
     * Fügt einen Channel hinzu.
     * Erzeugt ein neues Channelobjekt
     * und generiert für dieses Channelobjekt die Referenzen auf die erlaubten Benutzer
     * mittels userAdministration.getFromUserListByName()
     * und channel.addToAllowedUserList()
     * Bewirkt Aufruf von DataBaseIO.saveToDisk()
     * @param paramName Name des Channels
     * @param paramAllowedForGuests Flag, ob Gäste den Channel betreten dürfen
     * @param paramAllowedUserNames Vector von Strings - die Namen der Userobjekte, die den Channel betreten dürfen
     */
    public synchronized  void addChannel(String paramName, boolean paramAllowedForGuests, Vector paramAllowedUserNames) {
        Channel tmpChannel = new Channel(paramName, paramAllowedForGuests);
        Enumeration enum = paramAllowedUserNames.elements();
        while (enum.hasMoreElements()) {
            tmpChannel.addToAllowedUserList(this.userAdministration.getFromUserListByName((String)enum.nextElement()));
        }
        this.channelAdministration.addToChannelList(tmpChannel);
    }

    /**
     * Löscht den Channel mit dem angegebenen Namen. benutzt channelAdministration.getFromChannelListByName()
     * und channelAdministration.removeFromChannelList()
     * Bewirkt Aufruf von DataBaseIO.saveToDisk()
     */
    public synchronized  void deleteChannel(String channelName) {
        Channel tmpChannel = this.channelAdministration.getFromChannelListByName(channelName);
        this.channelAdministration.removeFromChannelList(tmpChannel);
    }

    /** Verändert die Daten des angegebenen Channels.
      * Erzeugt ein neues Channelobjekt mit den angegebenen Daten.
      * Benutzt setAllowedUserList() um die erlaubten Benutzer des Channels einzutragen
      * und ruft dann channelAdministration.editChannel() auf
     * Bewirkt Aufruf von DataBaseIO.saveToDisk()
      * @param oldName alter Name des Channels
      * @param newName neuer Name des Channels
      * @param paramAllowedForGuest Flag - ob Gäste den Channel betreten dürfen
      * @param allowedUserNames Vector von Strings - Liste der Usernamen, die den Channel betreten dürfen
      */
    public synchronized  void editChannel(String oldName, String newName, boolean paramAllowedForGuest, Vector allowedUserNames) {
        Channel tmpChannel = new Channel(newName, paramAllowedForGuest);
        Enumeration enum = allowedUserNames.elements();
        Vector tmpList = new Vector();
        while (enum.hasMoreElements()) {
            tmpList.addElement(this.channelAdministration.getFromChannelListByName((String)enum.nextElement()));
        }
        tmpChannel.setAllowedUserList(tmpList.elements());
        this.channelAdministration.editChannel(oldName, tmpChannel);
    }

    /** Fügt einen Benutzer hinzu.
      * erzeugt ein neues Userobjekt mit den angegebenen Daten
      * benutzt paramAllowedChannelNames und channelAdministration.getFromChannelListByName
      * um mit user.addToAllowedChannelList die für den Benutzer erlaubten Channelobjekte zu referenzieren.
      * Ruft schließlich userAdministration.addToUserList auf
     * Bewirkt Aufruf von DataBaseIO.saveToDisk()
     */
    public synchronized  void addUser(String paramName, String paramPassword, boolean paramIsAdmin, Vector paramAllowedChannelNames) {
        User tmpUser = new User(paramName, paramPassword, false, paramIsAdmin, this.userAdministration);
        Enumeration enum = paramAllowedChannelNames.elements();
        while (enum.hasMoreElements()) {
            tmpUser.addToAllowedChannelList(this.channelAdministration.getFromChannelListByName((String)enum.nextElement()));
        }
        this.userAdministration.addToUserList(tmpUser);
    }

    /** Löscht den Benutzer mit dem angegebenen Namen.
      * Benutzt userAdministration.getFromUserListByName() und
      * userAdministration.removeFromUserList()
     * Bewirkt Aufruf von DataBaseIO.saveToDisk()
      */
    public synchronized  void deleteUser(String userName) {
        User tmpUser = this.userAdministration.getFromUserListByName(userName);
        this.userAdministration.removeFromUserList(tmpUser);
    }

     /** Verändert die Daten des angegebenen Users.
      * Erzeugt ein neues Userobjekt mit den angegebenen Daten.
      * Benutzt setAllowedChannelList() um die erlaubten Channels des Benutzers einzutragen -
      * benutzt dafür channelAdministration.getFromChannelListByName()
      * und ruft dann userAdministration.editUser() auf
     * Bewirkt Aufruf von DataBaseIO.saveToDisk()
      * @param oldName alter Name des Users
      * @param newName neuer Name des Users
      * @param newPassword neues Passwort
      * @param paramIsAdmin Flag - ob User Admin-Rechte hat
      * @param allowedChannelNames Vector von Strings - Liste der Channelnamen, die der Benutzer betreten darf
      */

    public synchronized  void editUser(String oldName, String newName, String newPassword, boolean paramIsAdmin,
        Vector allowedChannelNames) {
            User tmpUser = new User(newName, newPassword, false, paramIsAdmin, this.userAdministration);
            Enumeration enum = allowedChannelNames.elements();
            Vector tmpList = new Vector();
            while (enum.hasMoreElements()) {
                tmpList.addElement(this.channelAdministration.getFromChannelListByName((String)enum.nextElement()));
            }
            tmpUser.setAllowedChannelList(tmpList.elements());
            this.userAdministration.editUser(oldName, tmpUser);
    }

    /** Sendet die Benutzerdaten des Benutzers mit dem angegebenen Namen an den Client.
     * Benutzt userAdministration.getFromUserListByName().
     * Erzeugt und versendet ein entsprechendes SetUserDataCommand()
     */
    public  synchronized void sendUser(String userName) {
        User tmpUser = this.userAdministration.getFromUserListByName(userName);
        String tmpName = tmpUser.getName();
        String tmpPassword = tmpUser.getPassword();
        boolean tmpIsAdmin = tmpUser.isAdmin();
        Vector tmpAllowedChannelList = tmpUser.getAllowedChannelNames();
        this.sendCommand(
            new Util.Commands.SetUserDataCommand(tmpName, tmpPassword, tmpIsAdmin, tmpAllowedChannelList));
    }

    /** Sendet die Channeldaten des Channels mit dem angegebenen Namen.
      * Erzeugt und versendet ein neues SetChannelDataCommand().
      * Benutzt channelAdministration.getFromChannelListByName()
      */
    public synchronized  void sendChannel(String channelName) {
        Channel tmpChannel = this.channelAdministration.getFromChannelListByName(channelName);
        String tmpName = tmpChannel.getName();
        boolean tmpIsAllowedForGuest = tmpChannel.isAllowedForGuest();
        Vector tmpAllowedUserList = tmpChannel.getAllowedUserNames();
        this.sendCommand(
            new SetChannelDataCommand(tmpName, tmpIsAllowedForGuest, tmpAllowedUserList));
    }

    /**
     * @clientCardinality 0..1
     * @supplierCardinality 1
     */
    private ChannelAdministration channelAdministration;
}
