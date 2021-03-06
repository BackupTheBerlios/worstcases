package Client;

import java.util.Vector;
import java.net.Socket;
import Util.*;
import Util.Commands.*;

/** Die Klasse AdminClient liefert dem Administrator die n�tigen Methoden,
 *  um auf die Benutzer- und Channelverwaltung zuzugreifen, um also
 *  Benutzer und Channels anzulegen, zu editieren und zu l�schen. */
public class AdminClient extends Client {

    /** Liste aller Channelnamen. */
    private Vector channelList;

    /** Channeldaten: alter Channelname (wird beim Editieren ben�tigt).*/
    private String tmpOldChannelName;

    /** Channeldaten: Channelname, zur Bearbeitung.*/
    private String tmpChannelName;

    /** Channeldaten: Flag �ffentlich ja/nein, zur Bearbeitung.*/
    private boolean tmpAllowedForGuests;

    /** Channeldaten: Liste der berechtigten Benutzer, zur Bearbeitung.*/
    private Vector tmpAllowedUserNames;


    /** Liste aller Benutzernamen. */
    private Vector userList;

    /** Userdaten: alter Username (wird beim Editieren ben�tigt).*/
    private String tmpOldUserName;

    /** Userdaten: Username, zur Bearbeitung.*/
    private String tmpUserName;

    /** Userdaten: Passwort, zur Bearbeitung.*/
    private String tmpPassword;

    /** Userdaten: Flag ist Admin ja/nein, zur Bearbeitung.*/
    private boolean tmpIsAdmin;

    /** Userdaten: Liste der aktiven Channels, zur Bearbeitung.*/
    private Vector tmpAllowedChannelNames;


    /** Wird vom SetUserDataCommand aufgerufen und setzt die Userdaten auf die entsprechenden Attribute. */
    public void setUserData(String userName,String password,boolean isAdmin,Vector channelNames) {
      this.tmpUserName=userName;
      this.tmpOldUserName=userName;
      this.tmpPassword=password;
      this.tmpIsAdmin=isAdmin;
      this.tmpAllowedChannelNames=channelNames;
    }

    /** Wird vom SetChannelDataCommand aufgerufen und setzt die Channeldaten auf die entsprechenden Attribute. */
    public void setChannelData(String channelName,boolean isAllowedForGuest,Vector userNames) {
      this.tmpChannelName=channelName;
      this.tmpOldChannelName=channelName;
      this.tmpAllowedForGuests=isAllowedForGuest;
      this.tmpAllowedUserNames=userNames;
    }

    /** Wird vom SetUserListCommand aufgerufen und setzt die Userliste in allUserList. */
    public void setUserList(Vector list) {
      this.userList=list;
    }

    /** Wird vom SetChannelListCommand aufgerufen und setzt die Channelliste in allChannelList. */
    public void setChannelList(Vector list) {
      this.channelList=list;
    }

    /** Fordert beim AdminClientServant einen bestimmten Userdatensatz an.
     *  Benutzt sendCommand() und erzeugt ein neues GetUserDataCommand - Objekt,
     *  welches im AdminClientServant ausgef�hrt wird.
     */
    public void getUserData(String userName) {
      this.sendCommand(new GetUserDataCommand(userName));
    }

    /** Fordert beim AdminClientServant einen bestimmten Channeldatensatz an.
     *  Benutzt sendCommand() und erzeugt ein neues GetChannelDataCommand - Objekt,
     *  welches im AdminClientServant ausgef�hrt wird.
     */
    public void getChannelData(String channelName) {
      this.sendCommand(new GetChannelDataCommand(channelName));
    }

    /** Fordert beim AdminClientServant die komplette Userliste an.
     *  Benutzt sendCommand() und erzeugt ein neues GetUserListCommand - Objekt,
     *  welches im AdminClientServant ausgef�hrt wird.
     */
    public void getUserList() {
      this.sendCommand(new GetUserListCommand());
    }

    /** Fordert beim AdminClientServant die komplette Channelliste an.
     *  Benutzt sendCommand() und erzeugt ein neues GetChannelListCommand - Objekt,
     *  welches im AdminClientServant ausgef�hrt wird.
     */
    public void getChannelList() {
      this.sendCommand(new GetChannelListCommand());
    }

    /** F�gt einen Channel mit den (von der GUI) �bergebenen Daten hinzu.
     *  Benutzt sendCommand() und erzeugt ein neues AddChannelCommand - Objekt, welches
     *  im AdminClientServant ausgef�hrt wird.
     *  @param paramName Name des neuen Channels
     *  @param paramAllowedForGuests Flag, ob der Channel f�r G�ste zugelassen ist
     *  @param paramAllowedUserNames Liste der zugelassenen User*/
    public void addChannel(String paramName,boolean paramAllowedForGuests, Vector paramAllowedUserNames) {
      this.sendCommand(new AddChannelCommand(paramName, paramAllowedForGuests, paramAllowedUserNames));
    }

    /** L�scht den Channel mit dem Namen name.
     *  Benutzt sendCommand() und erzeugt ein neues DeleteChannelCommand - Objekt,
     *  welches im AdminClientServant ausgef�hrt wird.
     */
    public void deleteChannel(String name) {
      this.sendCommand(new DeleteChannelCommand(name));
    }

    /** �ndert einen Channel mit den (von der GUI) �bergebenen Daten.
     *  Benutzt sendCommand() und erzeugt ein neues EditChannelCommand - Objekt, welches
     *  im AdminClientServant ausgef�hrt wird.
     *  @param paramOldName Alter Name des Channels
     *  @param paramName (Neuer) Name des Channels
     *  @param paramAllowedForGuests Flag, ob der Channel f�r G�ste zugelassen ist
     *  @param paramAllowedUserNames Liste der zugelassenen User*/
    public void editChannel(String paramOldName, String paramName,boolean paramAllowedForGuests, Vector paramAllowedUserNames) {
      this.sendCommand(new EditChannelCommand(paramOldName, paramName, paramAllowedForGuests, paramAllowedUserNames));
    }

    /** F�gt einen User mit den (von der GUI) �bergebenen Daten hinzu.
     *  Benutzt sendCommand() und erzeugt ein neues AddUserCommand - Objekt, welches
     *  im AdminClientServant ausgef�hrt wird.
     *  @param paramName Name des neuen Users
     *  @param paramPassword Passwort des neuen Users
     *  @param paramIsAdmin Flag, ob der Benutzer Administratorrechte besitzt
     *  @param paramAllowedChannelNames Liste der Channels, auf die der User Zugriff hat*/
    public void addUser(String paramName, String paramPassword, boolean paramIsAdmin, Vector paramAllowedChannelNames) {
      this.sendCommand(new AddUserCommand(paramName, paramPassword, paramIsAdmin, paramAllowedChannelNames));
    }

    /** L�scht den Benutzer mit dem Namen name.
     *  Benutzt sendCommand() und erzeugt ein neues DeleteUserCommand - Objekt,
     *  welches im AdminClientServant ausgef�hrt wird.
     */
    public void deleteUser(String name) {
      this.sendCommand(new DeleteUserCommand(name));
    }

    /** �ndert einen User mit den (von der GUI) �bergebenen Daten.
     *  Benutzt sendCommand() und erzeugt ein neues EditUserCommand - Objekt, welches
     *  im AdminClientServant ausgef�hrt wird.
     *  @param paramOldName Alter Name des Users
     *  @param paramName (Neuer) Name des Users
     *  @param paramPassword Passwort des Users
     *  @param paramIsAdmin Flag, ob der Benutzer Administratorrechte besitzt
     *  @param paramAllowedChannelNames Liste der Channels, auf die der User Zugriff hat*/
    public void editUser(String paramOldName, String paramName, String paramPassword, boolean paramIsAdmin, Vector paramAllowedChannelNames) {
      this.sendCommand(new EditUserCommand(paramOldName, paramName, paramPassword, paramIsAdmin, paramAllowedChannelNames));
    }
}
