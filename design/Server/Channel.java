package Server;

import java.util.Vector;
import java.util.Enumeration;

/**
 * Ein Channeldatensatz: Eine Instanz dieser Klasse kann wichtige Channeldaten wie beispielsweise
 * berechtigte User oder im Channel befindliche User speichern.
 * Außerdem stehen Methoden zur Verfügung, mit denen Listen der berechtigten
 * bzw. im Raum befindlichen User abgefragt und übergeben werden können.
 * Desweiteren können einzelne User zu den CurrentUser hinzugefügt bzw.
 * wieder entfernt werden, wenn sie den Channel betreten bzw. verlassen.
 */
class Channel {
    /** Konstruktur, der die entsprechenden Attribute setzt. */
    public Channel(String paramName, boolean paramAllowedForGuests) {
        this.name = paramName;
        this.allowedForGuest = paramAllowedForGuests;
    }

    /** Gibt den Namen des Channels zurück.*/
    public String getName() {
        return name;
    }

    /**
     *Setzt den Namen des Channels.
     *Ruft informCurrentUsers() auf.
     */
    public synchronized void setName(String paramName) {
        if(this.name!=paramName){
        this.name = paramName;
        this.informCurrentUsers();
        }
    }

    /** Gibt an, ob der Channel von Gästen betreten werden darf.*/
    public boolean isAllowedForGuest() {
        return allowedForGuest;
    }

    /** Setzt, ob Gäste den Channel betreten dürfen.
      * Benachrichtig ggf. betroffene User mittels user.isGuest() und user.removeFromAllowedChannelList(this) */
    public void setAllowedForGuest(boolean b) {
        boolean old = this.isAllowedForGuest();
        this.allowedForGuest = b;
        if (old != b) {
            if (old) {
                Enumeration enum = this.getCurrentUserEnum();
                User tmpUser;
                while (enum.hasMoreElements()) {
                    tmpUser = (User)enum.nextElement();
                    if (tmpUser.isGuest()) {
                        tmpUser.removeFromAllowedChannelList(this);
                        tmpUser.setCurrentChannel(null);
                    }
                }
            }
        }
    }

    /** Die Namensliste als Vector von Strings der berechtigten Benutzer wird zurückgegeben Benutzt getAllowedUserEnum() und User.getName(). */
    public Vector getAllowedUserNames() {
        Vector tmpVector = new Vector();
        Enumeration enum = this.getAllowedUserEnum();
        while (enum.hasMoreElements()) {
            tmpVector.addElement(((User)enum.nextElement()).getName());
        }
        return tmpVector;
    }

    /** Gibt eine Aufzählung der aktuellen Benutzerobjekte im Channel zurück. */
    public Enumeration getAllowedUserEnum() {
        return this.allowedUserList.elements();
    }

    /**
     * Setzt allowedUserList auf die in enumAllowedUser übergebenen Werte
     * Benutzt addToAllowedUserList() und removeFromAllowedUserList().
     */
    public void setAllowedUserList(Enumeration enumAllowedUser) {
        Enumeration enum=enumAllowedUser;
        if(enum==null){
          enum=(new Vector()).elements();
        }

        Vector tmpList = new Vector();
        User tmpUser;
        while (enum.hasMoreElements()) {
            tmpUser = (User)enum.nextElement();
            tmpList.addElement(tmpUser);
            this.addToAllowedUserList(tmpUser);
        }
        enum = this.getAllowedUserEnum();
        while (enum.hasMoreElements()) {
            tmpUser = (User)enum.nextElement();
            if (!tmpList.contains(tmpUser)) {
                this.removeFromAllowedUserList(tmpUser);
            }
        }
    }

    /**
     * Fügt einen Benutzer zur Liste der berechtigten Benutzer hinzu.
     * Benachrichtigt mittels User.addToAllowedChannelList()
     * das entsprechende User-Objekt.
     */
    public synchronized void addToAllowedUserList(User paramUser) {
        if(paramUser!=null){
        if (!this.allowedUserList.contains(paramUser)) {
            this.allowedUserList.addElement(paramUser);
            paramUser.addToAllowedChannelList(this);
        }
        }
    }

    /**
     * Entfernt einen Benutzer aus der Liste der berechtigten Benutzer.
     * Benachrichtigt mittels User.removeFromAllowedChannelList() das entsprechende User-Objekt.
     */
    public  synchronized void removeFromAllowedUserList(User paramUser) {
        if (this.allowedUserList.removeElement(paramUser)) {
            paramUser.removeFromAllowedChannelList(this);
        }
    }

    /** Eine Namensliste als Vector von Strings der aktuellen Benutzer wird zurückgegeben. Benutzt getCurrentUserEnum() und User.getName() */
    public Vector getCurrentUserNames() {
        Vector tmpVector = new Vector();
        Enumeration enum = this.getCurrentUserEnum();
        while (enum.hasMoreElements()) {
            tmpVector.addElement(((User)enum.nextElement()).getName());
        }
        return tmpVector;
    }

    /** Liefert eine Aufzählung der aktuellen Benutzer zurück. */
    public Enumeration getCurrentUserEnum() {
        return currentUserList.elements();
    }

    /**
     * Setzt CurrentUserList auf die in enumCurrentUser übergebenen Werte.
     * Benutzt addToCurrentUserList() und removeFromCurrentUserList().
     */
    public  synchronized void setCurrentUserList(Enumeration enumCurrentUser) {
        Enumeration enum=enumCurrentUser;
        if(enumCurrentUser==null){
          enum=(new Vector()).elements();
        }
        Vector tmpList = new Vector();
        User tmpUser;
        while (enum.hasMoreElements()) {
            tmpUser = (User)enum.nextElement();
            tmpList.addElement(tmpUser);
            this.addToCurrentUserList(tmpUser);
        }
        enum = this.getCurrentUserEnum();
        while (enum.hasMoreElements()) {
            tmpUser = (User)enum.nextElement();
            if (!tmpList.contains(tmpUser)) {
                this.removeFromCurrentUserList(tmpUser);
            }
        }
    }

    /**
     * Fügt einen User zu den im Channel befindlichen Usern hinzu. Benachrichtigt den User mittels User.setCurrentChannel().
     * Ruft dann informCurrentUsers() auf.
     */
    public  synchronized void addToCurrentUserList(User paramUser) {
        if (!this.currentUserList.contains(paramUser)) {
            this.currentUserList.addElement(paramUser);
            paramUser.setCurrentChannel(this);
            this.informCurrentUsers();
        }
    }

    /**
     * Entfernt einen User aus den im Channel befindlichen Usern. Benachrichtigt den User mittels User.setCurrentChannel(null).
     * Ruft dann informCurrentUsers() auf.
     */
    public synchronized  void removeFromCurrentUserList(User paramUser) {
        if (this.currentUserList.removeElement(paramUser)) {
            paramUser.setCurrentChannel(null);
            this.informCurrentUsers();

        }
    }

    /** Entfernt das Channelobjekt aus dem System.
      * Benutzt setCurrentUserList(null) und setAllowedUserList(null). */
    public  synchronized void removeYou() {
        this.setCurrentUserList(null);
        this.setAllowedUserList(null);
    }

    /** Dient dem debugging. */
    public String toString() {
        String tmpString = "Name:" + name + " allowed for guests:" + this.allowedForGuest + " allowed user:";
        Enumeration enum = this.getAllowedUserEnum();
        while (enum.hasMoreElements()) {
            tmpString = tmpString + ((User)enum.nextElement()).getName() + " ";
        }
        return tmpString;
    }

    /**
     * Informiert die Clients der aktuellen Benutzer im Channel
     * über Veränderungen im Channel mittels getCurrentUserEnum(),
     * User.getClientServant() und
     * ClientServant.sendCurrentChannelData().
     */
    public synchronized void informCurrentUsers(){
            Enumeration enum = this.getCurrentUserEnum();
            User tmpUser;
            ClientServant tmpClientServant;
            while (enum.hasMoreElements()){
              tmpUser=(User)enum.nextElement();
              tmpClientServant=tmpUser.getClientServant();
              if(tmpClientServant!=null){
               tmpClientServant.sendCurrentChannelData();
              }
            }

    }

    /**Der Name des Channels.*/
    private String name=new String();

    /** "Erlaubt-für-Gäste"-Flag. */
    private boolean allowedForGuest = false;

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
}
