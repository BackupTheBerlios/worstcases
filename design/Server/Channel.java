package Server;

import java.util.Vector;
import java.util.Enumeration;

/**
 * ein Channeldatensatz: Eine Instanz dieser Klasse kann wichtige Channeldaten wie z.B.
 * berechtigte User oder im Channel befindliche User speichern.
 * Außerdem stehen Methoden zur Verfügung, mit denen Listen der berechtigten
 * bzw. im Raum befindlichen User abgefragt und übergeben werden können.
 * Desweiteren können einzelne User zu den CurrentUser hinzugefügt bzw.
 * wieder entfernt werden, wenn sie den Channel betreten bzw. verlassen.
 */
class Channel {
    public Channel(String paramName, boolean paramAllowedForGuests, Vector paramAllowedUserStringList) {
        this.name = paramName;
        this.allowedForGuest = paramAllowedForGuests;
        this.allowedUserStringList = paramAllowedUserStringList;
    }

    public Channel() { }
    ;

    public Vector getCurrentUserList() {
        return currentUserList;
    }

    public void setCurrentUserList(Vector currentUserList) {
        this.currentUserList = currentUserList;
    }

    /** Fügt einen User zu den im Channel befindlichen Usern hinzu. */
    public void addToCurrentUserList(User paramUser) {
        this.currentUserList.addElement(paramUser);
        Enumeration enum = this.currentUserList.elements();
        while (enum.hasMoreElements()) {
            User tmpUser = (User)(enum.nextElement());
            ClientServant tmpClientServant = tmpUser.getClientServant();
            tmpClientServant.sendNewUserEnteredChannel(paramUser.getName());
        }
    }

    /** Entfernt einen User von den im Channel befindlichen Usern. */
    public void removeFromCurrentUserList(User paramUser) {
        int pos = 0;
        this.currentUserList.removeElement(paramUser);
        User tmpUser = (User)(this.currentUserList.elementAt(pos));
        ClientServant tmpClientServant = tmpUser.getClientServant();
        tmpClientServant.sendChannelData();
    }

    public Vector getAllowedUserList() {
        return allowedUserList;
    }

    public void setAllowedUser(Vector allowedUser) {
        this.allowedUserList = allowedUserList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        String tmpString = "Name:" + name + " allowed for guests:" + this.allowedForGuest + " allowed user:";
        Enumeration enum = this.getAllowedUserList().elements();
        while (enum.hasMoreElements()) {
            tmpString = tmpString + ((User)enum.nextElement()).getName() + " ";
        }
        return tmpString;
    }

    public Vector getAllowedUserStringList() {
        return allowedUserStringList;
    }

    public void setAllowedUserStringList(Vector allowedUserStringList) {
        this.allowedUserStringList = allowedUserStringList;
    }

    public boolean isAllowedForGuest() {
        return allowedForGuest;
    }

    public void setAllowedForGuest(boolean allowedForGuest) {
        this.allowedForGuest = allowedForGuest;
    }

    /**
     * Gibt die Benutzer an, die sich momentan in dem Channel befinden.
     * @associates <{User}>
     * @supplierCardinality 0..
     * @clientCardinality 0..1
     * @supplierRole currentUser
     * @clientRole currentChannel
     * @label current state
     */
    private Vector currentUserList = new Vector();

    /**
     * Gibt die Benutzer an, die den Channel betreten dürfen.
     * @associates <{User}>
     * @clientCardinality 0..
     * @supplierCardinality 0..
     * @supplierRole allowedUser
     * @clientRole allowedChannel
     * @label allowance relation
     */
    private Vector allowedUserList = new Vector();

    /** Gibt an, ob der Datensatz seit dem letzten Laden verändert wurde - wird von DataBaseIO benötigt. */
    private boolean modified;

    /**
     * @supplierCardinality 0..
     * @clientCardinality 0..
     */
    private ChannelAdministration channelAdministration;
    private String name;
    private boolean allowedForGuest = true;
    private Vector allowedUserStringList = new Vector();
}
