package Server;

import java.util.Vector;
import java.util.Enumeration;

/** Ein Benutzerdatensatz */
public class User {
    /** @param userSet Benutzerdaten als String */
    public User(String paramName, String paramPassword, boolean paramGuest, boolean paramAdmin,
        Vector paramAllowedChannelStringList, UserAdministration paramUserAdministration) {
            this.name = paramName;
            this.password = paramPassword;
            this.isAdmin = paramAdmin;
            this.isGuest = paramGuest;
            this.allowedChannelStringList = paramAllowedChannelStringList;
            this.userAdministration = paramUserAdministration;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
        if ((loggedIn == false) && this.isGuest) {
            this.userAdministration.removeFromUserList(this);
        }
    }

    public Channel getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(Channel paramChannel) {
        this.currentChannel = paramChannel;
        currentChannel.addToCurrentUserList(this);
    }

    /** gibt einen Channel aus der Liste der erlaubten Channels mit dem angegebenen Namen zurück */
    public Channel getFromAllowedChannelByName(String channelName) {
        Enumeration enum = this.allowedChannelList.elements();
        Channel tmpChannel;
        while (enum.hasMoreElements()) {
            tmpChannel = (Channel)enum.nextElement();
            if (tmpChannel.getName().compareTo(channelName) == 0) {
                return tmpChannel;
            }
        }
        return null;
    }

    public ClientServant getClientServant() {
        return ClientServant;
    }

    public void setClientServant(ClientServant ClientServant) {
        this.ClientServant = ClientServant;
    }

    public String toString() {
        String tmpString = "Name:" + name + " password:" + this.password + " is admin:" + this.isAdmin + " is guest:" +
            this.isGuest + " allowed channel:";
        Enumeration enum = this.getAllowedChannelList().elements();
        while (enum.hasMoreElements()) {
            tmpString = tmpString + ((Channel)enum.nextElement()).getName() + " ";
        }
        return tmpString;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setIsGuest(boolean isGuest) {
        this.isGuest = isGuest;
    }

    public Vector getAllowedChannelList() {
        return allowedChannelList;
    }

    public void setAllowedChannelList(Vector allowedChannelList) {
        this.allowedChannelList = allowedChannelList;
    }

    public String getName() {
        return name;
    }

    public Vector getAllowedChannelStringList() {
        return allowedChannelStringList;
    }

    public void setAllowedChannelStringList(Vector allowedChannelStringList) {
        this.allowedChannelStringList = allowedChannelStringList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gibt den Channel an, in dem sich der Benutzer zur Zeit befindet.
     * @supplierCardinality 0..1
     * @clientCardinality 0..
     */
    private Channel currentChannel;

    /**
     * Gibt die Channels an, die der Benutzer betreten darf.
     * @associates <{Channel}>
     * @clientCardinality 0..
     * @supplierCardinality 0..*
     */
    private Vector allowedChannelList = new Vector();

    /** Gibt an, ob der Datensatz seit dem letzten Laden verändert wurde - wird von DataBaseIO benötigt. */
    private boolean modified = false;
    private String name;
    private String password;

    /** Gibt an, ob der Benutzer momentan das System benutzt. */
    private boolean loggedIn = false;

    /** Ist der User Administrator? */
    private boolean isAdmin = false;

    /**
     * @clientCardinality 1
     * @supplierCardinality 0..1
     */
    private ClientServant ClientServant;

    /**
     * @supplierCardinality 1
     * @clientCardinality 0..
     */
    private UserAdministration userAdministration;
    private boolean isGuest = false;
    private Vector allowedChannelStringList = new Vector();
}
