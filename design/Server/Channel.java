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
    /** Konstruktur, der die entsprechenden Attribute setzt */
    public Channel(String paramName, boolean paramAllowedForGuests) {
        this.name = paramName;
        this.allowedForGuest = paramAllowedForGuests;
    }

    /** Namensliste der aktuellen Benutzer wird zurückgegeben
      * Benutzt getCurrentUserEnum() und User.getName()
      */
    public Vector getCurrentUserNames(){
     Vector tmpVector=new Vector();
     Enumeration enum=this.getCurrentUserEnum();
     while(enum.hasMoreElements()){
      tmpVector.addElement(((User)enum.nextElement()).getName());

     }
     return tmpVector;
    }

    /** Namensliste der berechtigten Benutzer wird zurückgegeben
      * Benutzt getAllowedUserEnum() und User.getName()
      */
    public Vector getAllowedUserNames(){
     Vector tmpVector=new Vector();
     Enumeration enum=this.getAllowedUserEnum();
     while(enum.hasMoreElements()){
      tmpVector.addElement(((User)enum.nextElement()).getName());
     }
     return tmpVector;
    }

    /** fügt den Benutzer zur Liste der berechtigten Benutzer hinzu
      * Benachrichtigt mittels User.addToAllowedChannelList()
      * das entsprechende User-Objekt
      */
    public void addToAllowedUserList(User paramUser) {
        if (!this.allowedUserList.contains(paramUser)) {
            this.allowedUserList.addElement(paramUser);
            paramUser.addToAllowedChannelList(this);
        }
    }

    /** entfernt den Benutzer aus der Liste der berechtigten Benutzer
      * Benachrichtigt mittels User.removeFromAllowedChannelList()
      * das entsprechende User-Objekt
      */
    public void removeFromAllowedUserList(User paramUser) {
        if (this.allowedUserList.removeElement(paramUser)) {
            paramUser.removeFromAllowedChannelList(this);
        }
    }

    /** entfernt das Channelobjekt aus dem System
      * Benutzt setCurrentUserList(null) und setAllowedUserList(null)
      */
    public void removeYou() {
        this.setCurrentUserList(null);
        this.setAllowedUserList(null);
    }

    /** liefert eine Aufzählung der aktuellen Benutzer zurück*/
    public Enumeration getCurrentUserEnum() {
        return currentUserList.elements();
    }

    /** setzt CurrentUserList auf die in EnumCurrentUser übergebenen Werte
      * Benutzt addToCurrentUserList() und removeFromCurrentUserList()
      */
    public void setCurrentUserList(Enumeration EnumCurrentUser) {
        Vector tmpList = new Vector();
        User tmpUser;
        while (EnumCurrentUser.hasMoreElements()) {
            tmpUser = (User)EnumCurrentUser.nextElement();
            tmpList.addElement(tmpUser);
            this.addToCurrentUserList(tmpUser);
        }
        Enumeration enum = this.getCurrentUserEnum();
        while (enum.hasMoreElements()) {
            tmpUser = (User)enum.nextElement();
            if (!tmpList.contains(tmpUser)) {
                this.removeFromCurrentUserList(tmpUser);
            }
        }
    }

    /** Fügt einen User zu den im Channel befindlichen Usern hinzu.
      * Benachrichtigt den User mittels User.setCurrentChannel()
      */
    public void addToCurrentUserList(User paramUser) {
        if (!this.currentUserList.contains(paramUser)) {
            this.currentUserList.addElement(paramUser);
            paramUser.setCurrentChannel(this);
        }
    }

    /** Entfernt einen User aus den im Channel befindlichen Usern.
      * Benachrichtigt den User mittels User.setCurrentChannel(null)
      */
    public void removeFromCurrentUserList(User paramUser) {
        if (this.currentUserList.removeElement(paramUser)) {
            paramUser.setCurrentChannel(null);
        }
    }

    public void setName(String paramName){
     this.name=paramName;
    }

    public void setAllowedUserList(Enumeration enumAllowedUser) {
        Vector tmpList = new Vector();
        User tmpUser;
        while (enumAllowedUser.hasMoreElements()) {
            tmpUser = (User)enumAllowedUser.nextElement();
            tmpList.addElement(tmpUser);
            this.addToAllowedUserList(tmpUser);
        }
        Enumeration enum = this.getAllowedUserEnum();
        while (enum.hasMoreElements()) {
            tmpUser = (User)enum.nextElement();
            if (!tmpList.contains(tmpUser)) {
                this.removeFromAllowedUserList(tmpUser);
            }
        }
    }

    public Enumeration getAllowedUserEnum() {
        return this.allowedUserList.elements();
    }

    public String getName() {
        return name;
    }

    public String toString() {
        String tmpString = "Name:" + name + " allowed for guests:" + this.allowedForGuest + " allowed user:";
        Enumeration enum = this.getAllowedUserEnum();
        while (enum.hasMoreElements()) {
            tmpString = tmpString + ((User)enum.nextElement()).getName() + " ";
        }
        return tmpString;
    }

    public boolean isAllowedForGuest() {
        return allowedForGuest;
    }

    public void setAllowedForGuest(boolean b) {
        boolean old = this.isAllowedForGuest();
        this.allowedForGuest = b;
        if (old != b) {
            if (old) {
                Enumeration enum = this.getCurrentUserEnum();

                User tmpUser;
                while (enum.hasMoreElements()) {
                    tmpUser = (User)enum.nextElement();
                    if(tmpUser.isGuest()){
                     tmpUser.removeFromAllowedChannelList(this);
                     tmpUser.setCurrentChannel(null);
                    }
                }
            }
        }
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
    private String name;
    private boolean allowedForGuest = false;
}
