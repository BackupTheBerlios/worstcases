package Server;

import java.util.Vector;
import java.util.Enumeration;

/** Verwaltet die Benutzer */
class UserAdministration {
    /**
     * Meldet einen Benutzer an.
     * @return den Benutzer, falls Authentifizierung klappt, sonst null
     * prüft, ob numCurrentUser<maxUsers, läßt Administratoren immer ins System, sonst,
     * falls maximale Anzahl erreicht, return null
     */

    public UserAdministration(ChannelAdministration paramChannelAdministration){
     this.channelAdministration=paramChannelAdministration;

    }

    public synchronized User loginUser(String name, String password) {
        User tmpUser = this.getFromUserListByName(name);
        if (tmpUser == null) {
            System.out.println("login User" + name +" failed");
            return null;
        }
        else {
            if (tmpUser.getPassword().compareTo(password) == 0 && (!tmpUser.isLoggedIn())) {
              if(!tmpUser.isAdmin() && numCurrentUsers>=maxUsers){
               return null;
              }
              else{
                tmpUser.setLoggedIn(true);
                return tmpUser;
              }
            }
            else { return null; }
        }
    }

    public void editUser(String oldName,User newUser){
    }

    public Vector getUserNames(){
        Vector tmpVector=new Vector();
      Enumeration enum=this.getUserEnum();
      User tmpUser;
      while(enum.hasMoreElements()){
       tmpUser=(User)enum.nextElement();
       if(!tmpUser.isGuest()){
        tmpVector.addElement(tmpUser.getName());
       }


      }
      return tmpVector;

    }

    /**
     * Meldet einen Gast an.
     * @param guestSet die vom Client empfangenen Gastdaten
     * @return den Benutzer, falls Authentifizierung klappt, sonst null
     */
    public synchronized User loginGuest(String paramName) {
        if ((this.getFromUserListByName(paramName) == null) && (this.numCurrentUsers < this.maxUsers)) {
            User tmpUser = new User(paramName, "guest", true, false, this);
            tmpUser.setAllowedChannelList(this.channelAdministration.getFreeForGuestEnum());
            tmpUser.setLoggedIn(true);
            this.addToUserList(tmpUser);
            return tmpUser;
        }
        else {
                        System.out.println("guestname " + paramName + " login failed");
            return null;

        }
    }

    /** Fügt einen Benutzer hinzu. */
    public void addToUserList(User paramUser) {
        if (!this.userList.contains(paramUser)) {
            this.userList.addElement(paramUser);
            paramUser.setUserAdministration(this);
        }
    }

    /** Entfernt einen Benutzer. */
    public void removeFromUserList(User paramUser) {
        if (this.userList.removeElement(paramUser)) {
            paramUser.setUserAdministration(null);
        }
    }

    /** Gibt den Benutzer mit dem angegebenen Namen zurück. */
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

    public Enumeration getUserEnum() {
        return userList.elements();
    }

    public void setUserList(Enumeration userEnum) {
        Vector tmpUserList = new Vector();
        User tmpUser;
        while (userEnum.hasMoreElements()) {
            tmpUser = (User)userEnum.nextElement();
            tmpUserList.addElement(tmpUser);
            this.addToUserList(tmpUser);
        }
        Enumeration enum = tmpUserList.elements();
        while (enum.hasMoreElements()) {
            tmpUser = (User)enum.nextElement();
            if (!tmpUserList.contains(tmpUser)) {
                this.removeFromUserList(tmpUser);
            }
        }
    }

    /**
     * @link aggregation
     *     @associates <{User}>
     * @clientCardinality 1
     * @supplierCardinality 0..
     */
    private Vector userList=new Vector();

    /** Anzahl der eingeloggten Benutzer(registrierte und Gäste) im System. */
    private int numCurrentUsers = 0;

    /** Maximale Anzahl von eingeloggten Benutzern im System. */
    private int maxUsers = 90;

    /** Maxmimale Anzahl von eingeloggten Gästen im System*/
    private int maxGuests = 10;

    /** Anzahl der Gäste im System*/
    private int numCurrentGuests=0;



    public synchronized void incNumCurrentUsers() {
        this.numCurrentUsers++;
        System.out.println("#User:"+this.numCurrentUsers);

    }

    public synchronized void decNumCurrentUsers() {
        this.numCurrentUsers--;
        System.out.println("#User:"+this.numCurrentUsers);

    }


    public synchronized void incNumCurrentGuests(){
      this.numCurrentGuests++;
        System.out.println("#User:"+this.numCurrentGuests);

    }
    public synchronized void decNumCurrentGuests(){
      this.numCurrentGuests--;
        System.out.println("#User:"+this.numCurrentGuests);

    }

    /**
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    private DataBaseIO dataBaseIO;

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

    private ChannelAdministration channelAdministration;
}
