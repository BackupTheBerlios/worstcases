package Server;

import java.util.Vector;
import java.util.Enumeration;
import Util.Debug.Debug;
import Util.Helper;

/**
 * Stellt Methoden zur Verwaltung der Benutzer zur Verfügung. Neben der Abwicklung des User-Login und des Guest-Login, können
 * User hinzugefügt, bearbeitet und gelöscht werden. Außerdem gibt es Methoden, um sich alle oder einzelne User anzeigen zu
 * lassen, sowie einige Methoden, welche die Counter-Attribute für die angemeldeten User bzw. Gäste hoch/ runter zählen
 * Desweitern sind in dieser Klasse die jeweils maximalen Anzahlen für User und Gäste festgelegt.
 */
class UserAdministration {
    /** Setzt channelAdministration. */
    public UserAdministration(ChannelAdministration paramChannelAdministration) {
        this.channelAdministration = paramChannelAdministration;
    }

    /**
     * Meldet einen Benutzer an. Prüft, ob numCurrentUser < maxUsers, läßt Administratoren immer ins System, sonst,
     * falls maximale Anzahl erreicht, return null. Benutzt getFromUserListByName(), user.getPassword(),
     * isLoggedIn() und setIsLogged()
     * @return den Benutzer, falls Authentifizierung klappt, sonst null
     */
    public synchronized User loginUser(String name, String password) {
        if (name != null && password != null) {
            User tmpUser = this.getFromUserListByName(name);
            if (tmpUser == null) {
                Debug.println(Debug.MEDIUM, "login User" + name + " failed");
                return null;
            }
            else {
                if (tmpUser.getPassword().compareTo(password) == 0 && (!tmpUser.isLoggedIn())) {
                    if (!tmpUser.isAdmin() && numCurrentUsers >= maxUsers) {
                        return null;
                    }
                    else {
                        tmpUser.setIsLoggedIn(true);
                        return tmpUser;
                    }
                }
                else { return null; }
            }
        }
        return null;
    }

    /**
     * Meldet einen Gast an und fügt ihn zur UserList hinzu, Mittels setAllowedChannelList() wird er zum Betreten der für
     * Gäste freie Channels berechtigt. Legt ein neues Userobjekt an. Benutzt addToUserList und setIsLoggedIn().
     * Prüft mittels getFromUserListByName, ob der gewünschte Gastname noch frei ist
     * @return den Benutzer, falls Authentifizierung klappt, sonst null
     */
    public synchronized User loginGuest(String paramName) {
        if (paramName != null) {
            if ((this.getFromUserListByName(paramName) == null) && (this.numCurrentUsers < this.maxUsers)) {
                User tmpUser = new User(paramName, "guest", true, false, this);
                tmpUser.setAllowedChannelList(this.channelAdministration.getFreeForGuestEnum());
                tmpUser.setIsLoggedIn(true);
                this.addToUserList(tmpUser);
                return tmpUser;
            }
            else {
                Debug.println(Debug.MEDIUM, this + ": guestname " + paramName + " login failed");
                return null;
            }
        }
        return null;
    }

    /** Liefert eine Namensliste aller User, die nicht Gäste sind. Benutzt getUserEnum(). */
    public Vector getUserNames() {
        Vector tmpVector = new Vector();
        Enumeration enum = this.getUserEnum();
        User tmpUser;
        while (enum.hasMoreElements()) {
            tmpUser = (User)enum.nextElement();
            if (!tmpUser.isGuest()) {
                tmpVector.addElement(tmpUser.getName());
            }
        }
        return tmpVector;
    }

    /**
     * Mittels dieser Methode kann ein User-Objekt bearbeitet werden. Setzt die Daten des Userobjektes mit dem Namen oldName
     * auf die in newUser enthaltenen Daten mittels user.setName(),setPassword(),setIsAdmin(),setAllowedChannelList().
     * Benutzt getFromUserListByName().
     */
    public synchronized void editUser(String oldName, User newUser) {
        if (oldName != null && newUser != null) {
            User tmpUser = this.getFromUserListByName(oldName);
            Debug.println(Debug.MEDIUM, this + ": changing: " + tmpUser);
            if (tmpUser != null) {
                tmpUser.setName(newUser.getName());
                tmpUser.setPassword(newUser.getPassword());
                tmpUser.setIsAdmin(newUser.isAdmin());
                tmpUser.setAllowedChannelList(newUser.getAllowedChannelEnum());
            }
            newUser.removeYou();
            Debug.println(Debug.MEDIUM, this + ": changed: " + tmpUser);
        }
    }

    /** Fügt einen Benutzer mittels user.setUserAdministration() zur UserList hinzu. */
    public synchronized void addToUserList(User paramUser) {
        if (paramUser != null && this.getFromUserListByName(paramUser.getName()) == null) {
            this.userList.addElement(paramUser);
            paramUser.setUserAdministration(this);
        }
    }

    /** Entfernt einen Benutzer mittels user.setUserAdministration() aus der UserList. */
    public synchronized void removeFromUserList(User paramUser) {
        if (this.userList.removeElement(paramUser)) {
            paramUser.setUserAdministration(null);
        }
    }

    /** Gibt den Benutzer mit dem angegebenen Namen zurück. Benutzt getUserEnum(), user.getName() */
    public User getFromUserListByName(String name) {
        Enumeration enum = this.getUserEnum();
        User tmpUser;
        while (enum.hasMoreElements()) {
            tmpUser = (User)(enum.nextElement());
            if (tmpUser.getName().compareTo(name) == 0) {
                return tmpUser;
            }
        }
        return null;
    }

    /** Gibt eine Aufzählung aller User zurück */
    public Enumeration getUserEnum() {
        return Helper.vectorCopy(this.userList).elements();
    }

    /** Setzt userList auf die in userEnum enthaltenen Objekte von Typ User Benutzt addToUserList() und removeFromUserList(). */
    public void setUserList(Enumeration userEnum) {
        Enumeration enum = userEnum;
        if (enum == null) {
            enum = (
                new Vector()).elements();
        }
        Vector tmpUserList = new Vector();
        User tmpUser;
        while (enum.hasMoreElements()) {
            tmpUser = (User)enum.nextElement();
            tmpUserList.addElement(tmpUser);
            this.addToUserList(tmpUser);
        }
        enum = tmpUserList.elements();
        while (enum.hasMoreElements()) {
            tmpUser = (User)enum.nextElement();
            if (!tmpUserList.contains(tmpUser)) {
                this.removeFromUserList(tmpUser);
            }
        }
    }

    /**
     * Liste aller Benutzer im System.
     * @link aggregation
     *     @associates <{User}>
     * @clientCardinality 1
     * @supplierCardinality 0..
     */
    private Vector userList = new Vector();

    /**
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    private DataBaseIO dataBaseIO;
    private ChannelAdministration channelAdministration;

    /** Anzahl der eingeloggten Benutzer im System. */
    private int numCurrentUsers = 0;

    /** Maximale Anzahl von eingeloggten Benutzern im System. */
    private int maxUsers = 90;

    /** Anzahl der Gäste im System */
    private int numCurrentGuests = 0;

    /** Maxmimale Anzahl von eingeloggten Gästen im System */
    private int maxGuests = 10;

    /** Erhöht den Zähler numCurrentUsers um 1 */
    public synchronized void incNumCurrentUsers() {
        this.numCurrentUsers++;
        Debug.println(Debug.MEDIUM, "#User:" + this.numCurrentUsers);
    }

    /** Verkleinert den Zähler numCurrentUsers um 1 */
    public synchronized void decNumCurrentUsers() {
        this.numCurrentUsers--;
        Debug.println(Debug.MEDIUM, "#User:" + this.numCurrentUsers);
    }

    /** Erhöht den Zähler numCurrentGuests um 1 */
    public synchronized void incNumCurrentGuests() {
        this.numCurrentGuests++;
        Debug.println(Debug.MEDIUM, "#Guest:" + this.numCurrentGuests);
    }

    /** Verringert den Zähler numCurrentGuests um 1 */
    public synchronized void decNumCurrentGuests() {
        this.numCurrentGuests--;
        Debug.println(Debug.MEDIUM, "#guest:" + this.numCurrentGuests);
    }

    /** Setzt dataBaseIO und benachrichtigt das betroffene Objekt durch DataBaseIO.setUserAdministration */
    public void setDataBaseIO(DataBaseIO paramDataBaseIO) {
        if (this.dataBaseIO != paramDataBaseIO) {
            if (this.dataBaseIO != null) {
                DataBaseIO old = this.dataBaseIO;
                this.dataBaseIO = null;
                old.setUserAdministration(null);
            }
            this.dataBaseIO = paramDataBaseIO;
            if (paramDataBaseIO != null) {
                paramDataBaseIO.setUserAdministration(this);
            }
        }
    }
}
