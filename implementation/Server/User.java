package Server;

import java.util.Vector;
import java.util.Enumeration;
import Util.Debug.Debug;

/** Ein Benutzerdatensatz. Enthält alle Informationen über einen Benutzer wie beispielsweise die Zugriffsrechte. */
class User {
    /** Setzt die entsprechenden Attribute, benutzt setUserAdministration(). */
    public User(String paramName, String paramPassword, boolean paramGuest, boolean paramAdmin,
        UserAdministration paramUserAdministration) {
            this.name = paramName;
            this.password = paramPassword;
            this.isAdmin = paramAdmin;
            this.isGuest = paramGuest;
            this.setUserAdministration(paramUserAdministration);
    }

    /** Der Name des Benutzers. */
    private String name;

    /** Das Passwort des Benutzers. */
    private String password;

    /** Gaststatus des Benutzers. */
    private boolean isGuest = true;

    /** Ist der User Administrator? */
    private boolean isAdmin = false;

    /** Gibt an, ob der Benutzer momentan das System benutzt. */
    private boolean loggedIn = false;

    /**
     * Gibt die Channels an, die der Benutzer betreten darf.
     * @associates <{Channel}>
     * @clientCardinality 0..
     * @supplierCardinality 0..*
     */
    private Vector allowedChannelList = new Vector();

    /**
     * Gibt den Channel an, in dem sich der Benutzer zur Zeit befindet.
     * @supplierCardinality 0..1
     * @clientCardinality 0..
     */
    private Channel currentChannel;

    /**
     * Der für den Benutzer verantwortliche ClientServant.
     *@clientCardinality 1
     * @supplierCardinality 0..1
     */
    private ClientServant clientServant;
    private UserAdministration userAdministration;

    /** Gibt den Namen des Benutzers zurück. */
    public String getName() {
        return name;
    }

    /** Setzt den Namen des Benutzers. Ruft ggf. informClient() und currentChannel.informCurrentUsers() auf. */
    public synchronized void setName(String paramName) {
        if (paramName != null) {
            if (this.name != paramName) {
                this.name = paramName;
                this.informClient();
                Channel tmpChannel = this.getCurrentChannel();
                if (tmpChannel != null) {
                    tmpChannel.informCurrentUsers();
                }
            }
        }
    }

    /** Gibt das Passwort zurück. */
    public String getPassword() {
        return password;
    }

    /** Setzt das Passwort. */
    public synchronized void setPassword(String paramPassword) {
        if (this.password != paramPassword) {
            this.password = paramPassword;
        }
    }

    /** Gibt true zurück, wenn der Benutzer ein Gast ist. */
    public boolean isGuest() {
        return isGuest;
    }

    /** Gibt true zurück, wenn der Benutzer Adminrechte hat. */
    public boolean isAdmin() {
        return this.isAdmin;
    }

    /**
     *Setzt das Adminflag, macht allerdings weiter nichts. D.h. ein Benutzer, der eingeloggt ist und
     * Admin-Rechte bekommt muß sich mittels der AdminClient-Applikation neu einloggen, um diese nutzen zu können.
     */
    public synchronized void setIsAdmin(boolean b) {
        this.isAdmin = b;
    }

    /** Gibt an, ob sich der Benutzer im System eingeloggt hat. */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Loggt den Benutzer ein oder aus, Benutzt userAdministration.incNumCurrentUsers(),decNumCurrentUsers(),
     * (incNumCurrentGuests(),decNumCurrentGuests() bei Gästen,
     * benutzt ggf. user.setCurrentChannel(null),setClientServant(null)
     * (und userAdministration.removeFromUserList() bei Gästen, um den Gast aus dem System zu entfernen, da
     * Gäste nur temporär ein Benutzerobjekt zugewiesen bekommen).
     */
    public synchronized void setIsLoggedIn(boolean paramLoggedIn) {
        if (this.isLoggedIn() != paramLoggedIn) {
            boolean old = this.loggedIn;
            this.loggedIn = paramLoggedIn;
            if (paramLoggedIn) {
                if (this.isGuest()) {
                    this.userAdministration.incNumCurrentGuests();
                }
                else {
                    this.userAdministration.incNumCurrentUsers();
                }
                Debug.println(Debug.MEDIUM, this + " logged in");
            }
            else {
                this.setCurrentChannel(null);
                this.setClientServant(null);
                if (this.isGuest()) {
                    this.userAdministration.decNumCurrentGuests();
                    this.userAdministration.removeFromUserList(this);
                }
                else {
                    this.userAdministration.decNumCurrentUsers();
                }
                Debug.println(Debug.MEDIUM, this + ": logged out");
            }
        }
    }

    /** Gibt eine Liste der Namen der Channels zurück, die der Benutzer betreten darf. */
    public Vector getAllowedChannelNames() {
        Vector tmpVector = new Vector();
        Enumeration enum = this.getAllowedChannelEnum();
        try {
            while (enum.hasMoreElements()) {
                tmpVector.addElement(((Channel)enum.nextElement()).getName());
            }
        }
        catch (java.util.NoSuchElementException e) {
            Debug.println(Debug.HIGH, this + ": error while getAllowedChannelNames: " + e);
        }
        return tmpVector;
    }

    /** Gibt eine Aufzählung der Channels zurück, die der Benutzer betreten darf. */
    public Enumeration getAllowedChannelEnum() {
        return allowedChannelList.elements();
    }

    /** Gibt einen Channel aus der Liste der erlaubten Channels mit dem angegebenen Namen zurück. */
    public Channel getFromAllowedChannelByName(String channelName) {
        Enumeration enum = this.getAllowedChannelEnum();
        Channel tmpChannel;
        try {
            while (enum.hasMoreElements()) {
                tmpChannel = (Channel)enum.nextElement();
                if (tmpChannel.getName().compareTo(channelName) == 0) {
                    return tmpChannel;
                }
            }
        }
        catch (java.util.NoSuchElementException e) {
            Debug.println(Debug.HIGH, this + ": error while getFromAllowedChannelByName: " + e);
        }
        return null;
    }

    /**
     *Setzt die Liste der für den Benutzer erlaubten Channels mit
     * addToAllowedChannelList() und removeFromAllowedChannelList().
     */
    public void setAllowedChannelList(Enumeration channelEnum) {
        Enumeration enum = channelEnum;
        if (enum == null) {
            enum = (
                new Vector()).elements();
        }
        Vector tmpChannelList = new Vector();
        Channel tmpChannel;

        /*alle Channels aus channelEnum zu den erlaubten Channels hinzufuegen
         *und diese zusaetzlich in tmpChannelList speichern
         */

        try {
            while (enum.hasMoreElements()) {
                tmpChannel = (Channel)enum.nextElement();
                tmpChannelList.addElement(tmpChannel);
                this.addToAllowedChannelList(tmpChannel);
            }
        }
        catch (java.util.NoSuchElementException e) {
            Debug.println(Debug.HIGH, this + ": error while setAllowedChannelList: " + e);
        }

        /*
         * alle Channels aus der Liste der erlaubten Channels entfernen, die nicht in
         * der tmpChannelList, also in channelEnum stehen
         */

        enum = tmpChannelList.elements();
        try {
            while (enum.hasMoreElements()) {
                tmpChannel = (Channel)enum.nextElement();
                if (!tmpChannelList.contains(tmpChannel)) {
                    this.removeFromAllowedChannelList(tmpChannel);
                }
            }
        }
        catch (java.util.NoSuchElementException e) {
            Debug.println(Debug.HIGH, this + ": error while setAllowedChannelList: " + e);
        }
    }

    /**
     *Fügt einen Channel zu der Liste der für den Benutzer erlaubten Channels
     * hinzu, benutzt Channel.addToAllowedUserList(), falls der Benutzer kein Gast ist. Ruft informClient() auf.
     */
    public void addToAllowedChannelList(Channel paramChannel) {
        if (!this.allowedChannelList.contains(paramChannel)) {
            this.allowedChannelList.addElement(paramChannel);
            this.informClient();
            paramChannel.addToAllowedUserList(this);
            Debug.println(Debug.LOW, this.getName() + ": add channel: " + paramChannel);
        }
    }

    /**
     * Entfernt einen Channel aus der Liste der erlaubten Channels,
     * benutzt Channel.removeFromAllowedUserList(). Ruft informClient() auf.
     */
    public void removeFromAllowedChannelList(Channel paramChannel) {
        if (this.allowedChannelList.removeElement(paramChannel)) {
            paramChannel.removeFromAllowedUserList(this);
            if (paramChannel == this.currentChannel) {
                this.setCurrentChannel(null);
            }
            this.informClient();
            Debug.println(Debug.LOW, this.getName() + ": remove Channel: " + paramChannel);
        }
    }

    /** Gibt den Channel zurück, in dem sich der Benutzer momentan befindet. */
    public Channel getCurrentChannel() {
        return currentChannel;
    }

    /**
     *Setzt den Channel, in dem sich der Benutzer befindet.
     * Benutzt Channel.removeFromCurrentUserList() und addToCurrentUserList().
     */
    public void setCurrentChannel(Channel paramChannel) {
        if (this.currentChannel != paramChannel) {
            if (this.currentChannel != null) {
                Channel old = this.currentChannel;
                this.currentChannel = null;
                old.removeFromCurrentUserList(this);
            }
            this.currentChannel = paramChannel;
            if (paramChannel != null) {
                paramChannel.addToCurrentUserList(this);
            }
            Debug.println(Debug.LOW, this.getName() + ": setCurrentChannel to: " + paramChannel);
        }
    }

    /**
     * Setzt die für den Benutzer verantwortliche UserAdministration.
     * Benutzt UserAdministration.removeFromUserList() und addToUserList()
     */
    public void setUserAdministration(UserAdministration paramUserAdministration) {
        if (this.userAdministration != paramUserAdministration) {
            if (this.userAdministration != null) {
                UserAdministration old = this.userAdministration;
                this.userAdministration = null;
                old.removeFromUserList(this);
            }
            this.userAdministration = paramUserAdministration;
            if (paramUserAdministration != null) {
                paramUserAdministration.addToUserList(this);
            }
            Debug.println(Debug.LOW, this.getName() + ": set UserAdministration to: " + paramUserAdministration);
        }
    }

    /** Gibt den dem User zugeordneten ClientServant zurück. */
    public ClientServant getClientServant() {
        return clientServant;
    }

    /**
     * Setzt den zugeordneten ClientServant und benutzt ClientServant.setUser().
     * Ein setClientServant(null) bewirkt ein setIsLoggedIn(false).
     */
    public void setClientServant(ClientServant paramClientServant) {
        if (this.clientServant != paramClientServant) {
            if (this.clientServant != null) {
                ClientServant old = this.clientServant;
                this.clientServant = null;
                old.setUser(null);
            }
            this.clientServant = paramClientServant;
            if (paramClientServant != null) {
                paramClientServant.setUser(this);
            }
            else {
                this.setIsLoggedIn(false);
            }
            Debug.println(Debug.LOW, this.getName() + ": setting ClientServant to: " + paramClientServant);
        }
    }

    /**
     *Entfernt das Benutzerobject aus dem System. Benutzt setIsLoggedIn(false),
     * setAllowedChannelList(null) und UserAdministration.removeFromUserList(this)
     */
    public void removeYou() {
        this.setIsLoggedIn(false);
        this.setAllowedChannelList(null);
        if (this.userAdministration != null) {
            this.userAdministration.removeFromUserList(this);
        }
        Debug.println(Debug.LOW, this.getName() + ": has been removed");
    }

    /** Dient dem debugging. */
    public String toString() {
        String tmpString = "Name:" + this.getName() + " isAdmin:" + this.isAdmin() + " isGuest:" + this.isGuest() +
            " currentChannel:" + this.getCurrentChannel() + " allowedChannelEnum: ";
        Enumeration enum = this.getAllowedChannelEnum();
        while (enum.hasMoreElements()) {
            tmpString = tmpString + ((Channel)(enum.nextElement())).getName() + " ";
        }
        return tmpString;
    }

    /**
     *Informiert den Client des Benutzers bei Veränderungen der Userdaten mittels
     * getClientServant() und ClientServant.sendCurrentUserData().
     */
    private void informClient() {
        ClientServant tmpClientServant = this.getClientServant();
        if (tmpClientServant != null) {
            tmpClientServant.sendCurrentUserData();
        }
        Debug.println(Debug.LOW, this.getName() + ": informed ClientServant");
    }
}
