package Server;

import java.util.Vector;
import java.util.Enumeration;


/** 
 * Die Klasse UserAdministration stellt Methoden zur Benutzerverwaltung zur Verf�gung.
 * Neben der Abwicklung des User-Login und des Guest-Login, k�nnen User hinzugef�gt,
 * bearbeitet und gel�scht werden. Au�erdem gibt es Methoden, um sich alle oder einzelne 
 * User anzeigen zu lassen, sowie einige Methoden, welche die Counter-Attribute f�r die
 * angemeldeten User bzw. G�ste hoch/ runter z�hlen
 * Desweitern sind in dieser Klasse die jeweils maximalen Anzahlen f�r z.B. User oder 
 * G�ste festgelegt. 
 */
class UserAdministration {


    public UserAdministration(ChannelAdministration paramChannelAdministration) {
    	this.channelAdministration = paramChannelAdministration;
    }

    /**
     * Meldet einen Benutzer an.
     * @return den Benutzer, falls Authentifizierung klappt, sonst null
     * pr�ft, ob numCurrentUser < maxUsers, l��t Administratoren immer ins System, sonst,
     * falls maximale Anzahl erreicht, return null
     */    
    public synchronized User loginUser(String name, String password) {
        User tmpUser = this.getFromUserListByName(name);
        if (tmpUser == null) {
            System.out.println("login User" + name +" failed");
            return null;
        }
        else {
            if (tmpUser.getPassword().compareTo(password) == 0 && (!tmpUser.isLoggedIn())) {
            	if(!tmpUser.isAdmin() && numCurrentUsers>=maxUsers) {
            		return null;
              	}
            	else {
            		tmpUser.setLoggedIn(true);
            		return tmpUser;
           		}
            }
            else { return null; }
        }
    }


	/** Mittles dieser Methode kann ein User-Objekt bearbeitet werden */
    public void editUser(String oldName,User newUser) {
    }

    
    /** 
     * Liefert eine Liste aller User.
     * Benutzt getUserEnum()
     */
    public Vector getUserNames() {
        Vector tmpVector=new Vector();
    	Enumeration enum=this.getUserEnum();
      	User tmpUser;
      	while(enum.hasMoreElements()) {
       		tmpUser = (User)enum.nextElement();
       		if (!tmpUser.isGuest()) {
        		tmpVector.addElement(tmpUser.getName());
       		}
      	}
    	return tmpVector;
    }


    /**
     * Meldet einen Gast an und f�gt ihn zur UserList hinzu,
     * mittles setAllowedChannelList() wird er zum Betreten der f�r G�ste freie
     * Channels berechtigt.
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


    /** F�gt einen Benutzer mittels setUserAdministration() zur UserList hinzu. */
    public void addToUserList(User paramUser) {
        if (!this.userList.contains(paramUser)) {
            this.userList.addElement(paramUser);
            paramUser.setUserAdministration(this);
        }
    }

    /** Entfernt einen Benutzer mittels setUserAdministration() aus der UserList. */
    public void removeFromUserList(User paramUser) {
        if (this.userList.removeElement(paramUser)) {
            paramUser.setUserAdministration(null);
        }
    }

    /** 
     * Gibt den Benutzer mit dem angegebenen Namen zur�ck. 
     * Benutzt getUserEnum() 
     */
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

    
    /** Gibt eine Aufz�hlung aller User zur�ck */
    public Enumeration getUserEnum() {
        return userList.elements();
    }
    

    /** 
     * Setzt die userList mittels userEnum
	 * Benutzt addTOUserList() und removeFromUserList()
	 */    
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

    /** Anzahl der eingeloggten Benutzer(registrierte und G�ste) im System. */
    private int numCurrentUsers = 0;

    /** Maximale Anzahl von eingeloggten Benutzern im System. */
    private int maxUsers = 90;

    /** Maxmimale Anzahl von eingeloggten G�sten im System*/
    private int maxGuests = 10;

    /** Anzahl der G�ste im System*/
    private int numCurrentGuests=0;


    /** Erh�ht den Z�hler numCurrentUsers um 1 und gibt die aktuelleAnzahl aus */
    public synchronized void incNumCurrentUsers() {
        this.numCurrentUsers++;
        System.out.println("#User:"+this.numCurrentUsers);

    }


	/** Verkleinert den Z�hler numCurrentUsers um 1 und gibt die aktuelleAnzahl aus */
    public synchronized void decNumCurrentUsers() {
        this.numCurrentUsers--;
        System.out.println("#User:"+this.numCurrentUsers);

    }

	/** Erh�ht den Z�hler numCurrentGuests um 1 und gibt die aktuelleAnzahl aus */
    public synchronized void incNumCurrentGuests() {
      this.numCurrentGuests++;
        System.out.println("#User:"+this.numCurrentGuests);
	}

	/** Verringert den Z�hler numCurrentGuests um 1 und gibt die aktuelleAnzahl aus */
    public synchronized void decNumCurrentGuests() {
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
