package Client;

import java.util.Vector;
import java.util.Enumeration;
import java.net.Socket;
import Util.*;
import Util.Debug.*;
import Util.Commands.*;

/**
 * Die Klasse AdminClient liefert dem Administrator die nötigen Methoden,
 * um auf die Benutzer- und Channelverwaltung zuzugreifen, um also
 * Benutzer und Channels anzulegen, zu editieren und zu löschen.
 */
public class AdminClient extends Client {
    /** Liste aller Channelnamen. */
    private Vector channelList = new Vector();

    /** Channeldaten: alter Channelname (wird beim Editieren benötigt). */
    private String tmpOldChannelName;

    /** Channeldaten: Channelname, zur Bearbeitung. */
    private String tmpChannelName;

    /** Channeldaten: Flag öffentlich ja/nein, zur Bearbeitung. */
    private boolean tmpAllowedForGuests;

    /** Channeldaten: Liste der berechtigten Benutzer, zur Bearbeitung. */
    private Vector tmpAllowedUserNames = new Vector();

    /** Liste aller Benutzernamen. */
    private Vector userList = new Vector();

    /** Userdaten: alter Username (wird beim Editieren benötigt). */
    private String tmpOldUserName;

    /** Userdaten: Username, zur Bearbeitung. */
    private String tmpUserName;

    /** Userdaten: Passwort, zur Bearbeitung. */
    private String tmpPassword;

    /** Userdaten: Flag ist Admin ja/nein, zur Bearbeitung. */
    private boolean tmpIsAdmin;

    /** Userdaten: Liste der aktiven Channels, zur Bearbeitung. */
    private Vector tmpAllowedChannelNames = new Vector();

    public Vector getUserListAttribute() {
     return this.userList;
    }

    public String getTmpOldChannelName() {
     return this.tmpOldChannelName;
    }
    
    public String getTmpOldUserName() {
     return this.tmpOldUserName;
    }

    /** Wird vom SetUserDataCommand aufgerufen und setzt die Userdaten auf die entsprechenden Attribute. */
    public void setUserData(String userName, String password, boolean isAdmin, Vector channelNames) {
        this.tmpUserName = userName;
        this.tmpOldUserName = userName;
        this.tmpPassword = password;
        this.tmpIsAdmin = isAdmin;
        this.tmpAllowedChannelNames = channelNames;
        Vector tmpChannelDiffList = new Vector();
        String tmpChannelName = new String();
        Enumeration enum = this.channelList.elements();
        while (enum.hasMoreElements()) {
            tmpChannelName = (String)enum.nextElement();
            if (!channelNames.contains(tmpChannelName)) {
              tmpChannelDiffList.addElement(tmpChannelName);
            }
        }
        this.gui.setUserData(userName, password, isAdmin, Util.Helper.quicksort(channelNames), Util.Helper.quicksort(tmpChannelDiffList));
    }

    /** Wird vom SetChannelDataCommand aufgerufen und setzt die Channeldaten auf die entsprechenden Attribute. */
    public void setChannelData(String channelName, boolean isAllowedForGuest, Vector userNames) {
        this.tmpChannelName = channelName;
        this.tmpOldChannelName = channelName;
        this.tmpAllowedForGuests = isAllowedForGuest;
        this.tmpAllowedUserNames = userNames;
        Vector tmpUserDiffList = new Vector();
        String tmpUserName = new String();
        Enumeration enum = this.userList.elements();
        while (enum.hasMoreElements()) {
            tmpUserName = (String)enum.nextElement();
            if (!userNames.contains(tmpUserName)) {
              tmpUserDiffList.addElement(tmpUserName);
            }
        }
        this.gui.setChannelData(channelName, isAllowedForGuest, Util.Helper.quicksort(userNames), Util.Helper.quicksort(tmpUserDiffList));
    }

    /** Wird vom SetUserListCommand aufgerufen und setzt die Userliste in userList. */
    public void setUserList(Vector list) {
        if(list!=null){
        this.userList = list;
        }
        else{
         this.userList=new Vector();
        }
        if (this.gui != null) {
            this.gui.setUserList(Util.Helper.quicksort(list));
        }
    }

    /** Wird vom SetChannelListCommand aufgerufen und setzt die Channelliste in channelList. */
    public void setChannelList(Vector list) {
        if(list!=null){
        this.channelList = list;
        }
        else{

         this.channelList= new Vector();
        }
        if (this.gui != null) {
            this.gui.setChannelList(Util.Helper.quicksort(list));
        }
    }

    /**
     * Fordert beim AdminClientServant einen bestimmten Userdatensatz an.
     * Benutzt sendCommand() und erzeugt ein neues GetUserDataCommand - Objekt, welches im AdminClientServant ausgeführt wird.
     */
    public void getUserData(String userName) {
        this.sendCommand(
            new GetUserDataCommand(userName));
    }

    /**
     * Fordert beim AdminClientServant einen bestimmten Channeldatensatz an.
     * Benutzt sendCommand() und erzeugt ein neues GetChannelDataCommand - Objekt,
     * welches im AdminClientServant ausgeführt wird.
     */
    public void getChannelData(String channelName) {
        this.sendCommand(
            new GetChannelDataCommand(channelName));
    }

    /**
     * Fordert beim AdminClientServant die komplette Userliste an.
     * Benutzt sendCommand() und erzeugt ein neues GetUserListCommand - Objekt, welches im AdminClientServant ausgeführt wird.
     */
    public void getUserList() {
        this.sendCommand(
            new GetUserListCommand());
    }

    /**
     * Fordert beim AdminClientServant die komplette Channelliste an.
     * Benutzt sendCommand() und erzeugt ein neues GetChannelListCommand - Objekt,
     * welches im AdminClientServant ausgeführt wird.
     */
    public void getChannelList() {
        this.sendCommand(
            new GetChannelListCommand());
    }

    /**
     * Fügt einen Channel mit den (von der GUI) übergebenen Daten hinzu.
     * Benutzt sendCommand() und erzeugt ein neues AddChannelCommand - Objekt, welches im AdminClientServant ausgeführt wird.
     *  @param paramName Name des neuen Channels
     *  @param paramAllowedForGuests Flag, ob der Channel für Gäste zugelassen ist
     *  @param paramAllowedUserNames Liste der zugelassenen User
     */
    public void addChannel(String paramName, boolean paramAllowedForGuests, Vector paramAllowedUserNames) {
        this.sendCommand(
            new AddChannelCommand(paramName, paramAllowedForGuests, paramAllowedUserNames));
    }

    /**
     * Löscht den Channel mit dem Namen name. Benutzt sendCommand() und erzeugt ein neues DeleteChannelCommand - Objekt,
     * welches im AdminClientServant ausgeführt wird.
     */
    public void deleteChannel(String name) {
        this.sendCommand(
            new DeleteChannelCommand(name));
    }

    /**
     * Ändert einen Channel mit den (von der GUI) übergebenen Daten.
     * Benutzt sendCommand() und erzeugt ein neues EditChannelCommand - Objekt, welches im AdminClientServant ausgeführt wird.
     *  @param paramOldName Alter Name des Channels
     *  @param paramName (Neuer) Name des Channels
     *  @param paramAllowedForGuests Flag, ob der Channel für Gäste zugelassen ist
     *  @param paramAllowedUserNames Liste der zugelassenen User
     */
    public void editChannel(String paramOldName, String paramName, boolean paramAllowedForGuests,
        Vector paramAllowedUserNames) {
            this.sendCommand(
                new EditChannelCommand(paramOldName, paramName, paramAllowedForGuests, paramAllowedUserNames));
    }

    /**
     * Fügt einen User mit den (von der GUI) übergebenen Daten hinzu.
     * Benutzt sendCommand() und erzeugt ein neues AddUserCommand - Objekt, welches im AdminClientServant ausgeführt wird.
     *  @param paramName Name des neuen Users
     *  @param paramPassword Passwort des neuen Users
     *  @param paramIsAdmin Flag, ob der Benutzer Administratorrechte besitzt
     *  @param paramAllowedChannelNames Liste der Channels, auf die der User Zugriff hat
     */
    public void addUser(String paramName, String paramPassword, boolean paramIsAdmin, Vector paramAllowedChannelNames) {
        this.sendCommand(
            new AddUserCommand(paramName, paramPassword, paramIsAdmin, paramAllowedChannelNames));
    }

    /**
     * Löscht den Benutzer mit dem Namen name. Benutzt sendCommand() und erzeugt ein neues DeleteUserCommand - Objekt,
     * welches im AdminClientServant ausgeführt wird.
     */
    public void deleteUser(String name) {
        this.sendCommand(
            new DeleteUserCommand(name));
    }

    /**
     * Ändert einen User mit den (von der GUI) übergebenen Daten.
     * Benutzt sendCommand() und erzeugt ein neues EditUserCommand - Objekt, welches im AdminClientServant ausgeführt wird.
     *  @param paramOldName Alter Name des Users
     *  @param paramName (Neuer) Name des Users
     *  @param paramPassword Passwort des Users
     *  @param paramIsAdmin Flag, ob der Benutzer Administratorrechte besitzt
     *  @param paramAllowedChannelNames Liste der Channels, auf die der User Zugriff hat
     */
    public void editUser(String paramOldName, String paramName, String paramPassword, boolean paramIsAdmin,
        Vector paramAllowedChannelNames) {
            this.sendCommand(
                new EditUserCommand(paramOldName, paramName, paramPassword, paramIsAdmin, paramAllowedChannelNames));
    }
}
