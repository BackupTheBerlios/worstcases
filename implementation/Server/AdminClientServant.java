package Server;

import java.util.Vector;
import java.util.Enumeration;
import Util.*;
import Util.Commands.*;

/** Verarbeitet die Anfragen eines AdminClients. */
public class AdminClientServant extends ClientServant implements DownlinkOwner {
    /**
     * Konstruktor, setzt die entsprechenden Attribute. Benutzt setDownlink(),setServer() und setUser().
     * "Entreisst" somit dem aufrufenden ClientServant die Objekte.
     */
    public AdminClientServant(Uplink paramUplink, Downlink paramDownlink, Server paramServer,
        ChannelAdministration paramChannelAdministration, UserAdministration paramUserAdministration, User paramUser,
        DataBaseIO paramDataBaseIO) {
            this.setUser(paramUser);
            this.setDownlink(paramDownlink);
            this.uplink = paramUplink;
            this.setServer(paramServer);
            this.channelAdministration = paramChannelAdministration;
            this.userAdministration = paramUserAdministration;
            this.dataBaseIO = paramDataBaseIO;
    }

    /**
     * Sendet eine Liste aller Channelnamen. Benutzt sendCommand() und erzeugt ein neues SetChannelListCommand - Objekt.
     * Benutzt channelAdministration.getChannelNames(), um die Namensliste zu erzeugen.
     */
    public void sendChannelList() {
        this.sendCommand(
            new SetChannelListCommand(this.channelAdministration.getChannelNames()));
    }

    /**
     * Sendet eine Liste aller Benutzernamen. Benutzt sendCommand und erzeugt ein neues SetUserListCommand-Objekt
     * Benutzt userAdministration.getUserNames(), um die Namensliste zu erzeugen.
     */
    public void sendUserList() {
        this.sendCommand(
            new SetUserListCommand(this.userAdministration.getUserNames()));
    }

    /**
     * Fügt einen Channel hinzu. Erzeugt ein neues Channelobjekt und generiert für dieses Channelobjekt die Referenzen auf die
     * erlaubten Benutzer mittels userAdministration.getFromUserListByName() und channel.addToAllowedUserList().
     * Bewirkt Aufruf von DataBaseIO.saveToDisk().
     * @param paramName Name des Channels
     * @param paramAllowedForGuests Flag, ob Gäste den Channel betreten dürfen
     * @param paramAllowedUserNames Vector von Strings - die Namen der Userobjekte, die den Channel betreten dürfen
     */
    public void addChannel(String paramName, boolean paramAllowedForGuests, Vector paramAllowedUserNames) {
        //"faule" Auswertung!
        if (paramName != null && paramName.compareTo("") != 0) {
            Channel tmpChannel = new Channel(paramName, paramAllowedForGuests);
            Enumeration enum;
            //wenn der Channel nicht für Gäste freigegeben ist, dann benutze paramAllowedUserNames
            if (!paramAllowedForGuests) {
                if (paramAllowedUserNames != null) {
                    enum = paramAllowedUserNames.elements();
                }
                else {
                    enum = (
                        new Vector()).elements();
                }
            }
            //Channel ist für Gäste freigegeben, erlaube alle Benutzer
            else {
                enum = this.userAdministration.getUserNames().elements();
            }
            //füge die Benutzerobjekte hinzu
            while (enum.hasMoreElements()) {
                tmpChannel.addToAllowedUserList(this.userAdministration.getFromUserListByName((String)enum.nextElement()));
            }
            this.channelAdministration.addToChannelList(tmpChannel);
            this.dataBaseIO.saveToDisk();
        }
    }

    /**
     * Löscht den Channel mit dem angegebenen Namen. Benutzt channelAdministration.getFromChannelListByName()
     * und channelAdministration.removeFromChannelList(). Bewirkt Aufruf von DataBaseIO.saveToDisk()
     * Ignoriert deleteChannel(FOYERNAME)
     */
    public void deleteChannel(String channelName) {
        if (channelName != null) {
            if (channelName.compareTo(this.channelAdministration.FOYERNAME) != 0) {
                Channel tmpChannel = this.channelAdministration.getFromChannelListByName(channelName);
                this.channelAdministration.removeFromChannelList(tmpChannel);
                this.dataBaseIO.saveToDisk();
            }
        }
    }

    /**
     * Verändert die Daten des angegebenen Channels. Erzeugt ein neues Channelobjekt mit den angegebenen Daten.
     * Benutzt setAllowedUserList() um die erlaubten Benutzer des Channels einzutragen
     * und ruft dann channelAdministration.editChannel() auf. Bewirkt Aufruf von DataBaseIO.saveToDisk()
     * Ignoriert editChannel(FOYER, ...)
     * @param oldName alter Name des Channels
     * @param newName neuer Name des Channels
     * @param paramAllowedForGuest Flag - ob Gäste den Channel betreten dürfen
     * @param allowedUserNames Vector von Strings - Liste der Usernamen, die den Channel betreten dürfen
     */
    public void editChannel(String oldName, String newName, boolean paramAllowedForGuest, Vector allowedUserNames) {
        if (oldName != null && newName != null && newName.compareTo("") != 0) {
            if (oldName.compareTo(this.channelAdministration.FOYERNAME) != 0) {
                Channel tmpChannel = new Channel(newName, paramAllowedForGuest);
                Enumeration enum;
                //Channel ist nicht für Gäste, benutze allowedUserNames
                if (!paramAllowedForGuest) {
                    if (allowedUserNames != null) {
                        enum = allowedUserNames.elements();
                    }
                    else {
                        enum = (
                            new Vector()).elements();
                    }
                    //füge die berechtigten Benutzer hinzu
                    while (enum.hasMoreElements()) {
                        tmpChannel.addToAllowedUserList(this.userAdministration.getFromUserListByName((String)enum.nextElement()));
                    }
                }
                //Channel ist für Gäste freigegeben, füge alle Benutzer hinzu
                else {
                    tmpChannel.setAllowedUserList(this.userAdministration.getUserEnum());
                }
                this.channelAdministration.editChannel(oldName, tmpChannel);
                this.dataBaseIO.saveToDisk();
            }
        }
    }

    /**
     * Fügt einen Benutzer hinzu. Erzeugt ein neues Userobjekt mit den angegebenen Daten.
     * Benutzt paramAllowedChannelNames und channelAdministration.getFromChannelListByName
     * um mit user.addToAllowedChannelList die für den Benutzer erlaubten Channelobjekte zu referenzieren.
     * Ruft schließlich userAdministration.addToUserList auf. Bewirkt Aufruf von DataBaseIO.saveToDisk().
     */
    public void addUser(String paramName, String paramPassword, boolean paramIsAdmin, Vector paramAllowedChannelNames) {
        //"faule" Auswertung!
        if (paramName != null && paramPassword != null && paramName.compareTo("") != 0 && paramPassword.compareTo("") != 0) {
            User tmpUser = new User(paramName, paramPassword, false, paramIsAdmin, this.userAdministration);
            //gewährt Zugriff auf alle Channel, die für Gäste freigegeben sind
            tmpUser.setAllowedChannelList(this.channelAdministration.getFreeForGuestEnum());
            Enumeration enum;
            if (paramAllowedChannelNames != null) {
                enum = paramAllowedChannelNames.elements();
            }
            else {
                enum = (
                    new Vector()).elements();
            }
            //zusätzlich zu den Gastchannels die Channel aus paramAllowedChannelNames hinzufügen
            while (enum.hasMoreElements()) {
                tmpUser.addToAllowedChannelList(this.channelAdministration.getFromChannelListByName((String)enum.nextElement()));
            }
            //falls der neue Benutzer Admin - Rechte hat, dann berechtige zum Betreten aller Channel
            if (paramIsAdmin) {
                tmpUser.setAllowedChannelList(this.channelAdministration.getChannelEnum());
            }
            this.userAdministration.addToUserList(tmpUser);
            this.dataBaseIO.saveToDisk();
        }
    }

    /**
     * Löscht den Benutzer mit dem angegebenen Namen. Benutzt userAdministration.getFromUserListByName() und
     * userAdministration.removeFromUserList(). Bewirkt Aufruf von DataBaseIO.saveToDisk().
     */
    public void deleteUser(String userName) {
        User tmpUser = this.userAdministration.getFromUserListByName(userName);
        this.userAdministration.removeFromUserList(tmpUser);
        this.dataBaseIO.saveToDisk();
    }

    /**
     * Verändert die Daten des angegebenen Users. Erzeugt ein neues Userobjekt mit den angegebenen Daten.
     * Benutzt setAllowedChannelList() um die erlaubten Channels des Benutzers einzutragen -
     * benutzt dafür channelAdministration.getFromChannelListByName() und ruft dann userAdministration.editUser() auf.
     * Bewirkt Aufruf von DataBaseIO.saveToDisk().
     * @param oldName alter Name des Users
     * @param newName neuer Name des Users
     * @param newPassword neues Passwort
     * @param paramIsAdmin Flag - ob User Admin-Rechte hat
     * @param allowedChannelNames Vector von Strings - Liste der Channelnamen, die der Benutzer betreten darf
     */
    public void editUser(String oldName, String newName, String newPassword, boolean paramIsAdmin,
        Vector allowedChannelNames) {
            //"faule" Auswertung!
            if (newName != null && newPassword != null && newName.compareTo("") != 0 && newPassword.compareTo("") != 0) {
                User tmpUser = new User(newName, newPassword, false, paramIsAdmin, this.userAdministration);
                //gewährt Zugriff auf alle Channel, die für Gäste freigegeben sind
                tmpUser.setAllowedChannelList(this.channelAdministration.getFreeForGuestEnum());
                Enumeration enum;
                if (allowedChannelNames != null) {
                    enum = allowedChannelNames.elements();
                }
                else {
                    enum = (
                        new Vector()).elements();
                }
                //weitere Channel aus allowedChannelNames hinzufügen
                while (enum.hasMoreElements()) {
                    tmpUser.addToAllowedChannelList(this.channelAdministration.getFromChannelListByName((String)enum.nextElement()));
                }
                //falls der neue Benutzer Admin - Rechte hat, dann berechtige zum Betreten aller Channel
                if (paramIsAdmin) {
                    tmpUser.setAllowedChannelList(this.channelAdministration.getChannelEnum());
                }
                this.userAdministration.editUser(oldName, tmpUser);
                this.dataBaseIO.saveToDisk();
            }
    }

    /**
     * Sendet die Benutzerdaten des Benutzers mit dem angegebenen Namen an den Client.
     * Benutzt userAdministration.getFromUserListByName(). Erzeugt und versendet ein entsprechendes SetUserDataCommand().
     */
    public void sendUser(String userName) {
        User tmpUser = this.userAdministration.getFromUserListByName(userName);
        if (tmpUser != null) {
            String tmpName = tmpUser.getName();
            String tmpPassword = tmpUser.getPassword();
            boolean tmpIsAdmin = tmpUser.isAdmin();
            Vector tmpAllowedChannelList = tmpUser.getAllowedChannelNames();
            this.sendCommand(
                new Util.Commands.SetUserDataCommand(tmpName, tmpPassword, tmpIsAdmin, tmpAllowedChannelList));
        }
    }

    /**
     * Sendet die Channeldaten des Channels mit dem angegebenen Namen.
     * Erzeugt und versendet ein neues SetChannelDataCommand(). Benutzt channelAdministration.getFromChannelListByName().
     */
    public void sendChannel(String channelName) {
        Channel tmpChannel = this.channelAdministration.getFromChannelListByName(channelName);
        if (tmpChannel != null) {
            String tmpName = tmpChannel.getName();
            boolean tmpIsAllowedForGuest = tmpChannel.isAllowedForGuest();
            Vector tmpAllowedUserList = tmpChannel.getAllowedUserNames();
            this.sendCommand(
                new SetChannelDataCommand(tmpName, tmpIsAllowedForGuest, tmpAllowedUserList));
        }
    }

    /**
     * Die Channel-Verwaltung.
     * @clientCardinality 0..1
     * @supplierCardinality 1
     */
    private ChannelAdministration channelAdministration;
    private DataBaseIO dataBaseIO;
}
