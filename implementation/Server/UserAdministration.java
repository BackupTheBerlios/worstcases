package Server;

import java.util.Vector;
import java.util.Enumeration;
import Util.Debug.Debug;
import Util.Helper;


/**
 * Stellt Methoden zur Verwaltung der Benutzer zur Verf�gung. Neben der Abwicklung des User-Login und des Guest-Login, k�nnen
 * User hinzugef�gt, bearbeitet und gel�scht werden. Au�erdem gibt es Methoden, um sich alle oder einzelne User anzeigen zu
 * lassen, sowie einige Methoden, welche die Counter-Attribute f�r die angemeldeten User bzw. G�ste hoch/ runter z�hlen
 * Desweitern sind in dieser Klasse die jeweils maximalen Anzahlen f�r User und G�ste festgelegt.
 */
class UserAdministration {

  /** Setzt channelAdministration. */
  public UserAdministration(
          ChannelAdministration paramChannelAdministration) {
    this.channelAdministration = paramChannelAdministration;
  }

  /**
   * Meldet einen Benutzer an. Pr�ft, ob numCurrentUser < maxUsers, l��t Administratoren immer ins System, sonst, falls
   * maximale Anzahl erreicht, return null. Benutzt getFromUserListByName(), user.getPassword(),
   * isLoggedIn() und setIsLogged()
   * @return den Benutzer, falls Authentifizierung klappt, sonst null
   */
  public synchronized User loginUser(String name, String password) {

    if ((name != null) && (password != null)) {

      // wenn name und password eigegeben wurden, wird versucht, das Userobjekt zu "laden".
      User tmpUser = this.getFromUserListByName(name);

      if (tmpUser == null) {

        // Eine Fehlermeldung wird ausgegeben, falls der User nicht existiert,
        Debug.println(Debug.MEDIUM,
                      "UserAdministration: login User" + name + " failed");

        return null;  // und die Methode wird ohne R�ckgabewert beendet.
      } else {
        if ((tmpUser.getPassword().compareTo(password) == 0)
                && (!tmpUser.isLoggedIn())) {

          // Wenn das password korrekt ist und der User noch nicht eingeloggt ist...
          if (!tmpUser.isAdmin() && (numCurrentUsers >= maxUsers)) {

            // wenn maxUser erreicht ist und es sich nicht um einen Admin handelt,
            Debug.println(Debug.MEDIUM,
                          "UserAdministration: maxusers reached: "
                          + maxUsers);

            return null;  // wird die Methode ohne R�ckgabewert beendet.
          } else {

            // alles ok: DerUser wird eingeloggt und tmpUser �bergeben.
            tmpUser.setIsLoggedIn(true);

            return tmpUser;
          }
        } else {
          Debug.println(
            Debug.MEDIUM,
            "UserAdministration: password wrong or user already logged in: "
            + name);

          return null;
        }  // password falsch oder User schon eingeloggt

        // Methode wird mit null beendet.
        // FIXME: Fehlermeldung
        // zus�tzliche if-abfrage zur Fehlerunterscheidung!?
      }
    }

    return null;  // Methode wird beendet, da name und password nicht eigegeben wurden.
  }

  /**
   * Meldet einen Gast an und f�gt ihn zur UserList hinzu, Mittels setAllowedChannelList() wird er zum Betreten der f�r
   * G�ste freie Channels berechtigt. Legt ein neues Userobjekt an. Benutzt addToUserList und setIsLoggedIn().
   * Pr�ft mittels getFromUserListByName, ob der gew�nschte Gastname noch frei ist
   * @return den Benutzer, falls Authentifizierung klappt, sonst null
   */
  public synchronized User loginGuest(String paramName) {

    if (paramName != null) {

      // wenn ein Name eingegeben wurde...
      if ((this.getFromUserListByName(paramName) == null)
              && (this.numCurrentGuests < this.maxGuests)) {

        // wenn es den User noch nicht gibt und maxUsers noch nicht erreicht wurde,
        User tmpUser = new User(paramName, "guest", true, false, this);

        // wird ein neues Userobjekt mit guest-Eigenschaften angelegt und an tmpUser �bergeben.
        tmpUser
          .setAllowedChannelList(this.channelAdministration
            .getFreeForGuestEnum());

        // Die f�r G�ste zug�nglichen Channel werden dem guest zugewiesen.
        tmpUser.setIsLoggedIn(true);  // Der guest wird eingeloggt.
        this.addToUserList(tmpUser);  // Der guest wird zur UserList hinzugef�gt.

        return tmpUser;  // Das Userobjekt wird zur�ckgegeben
      } else {

        // Es existiert bereits ein User mit diesem Namen, oder die maximale Useranzahl
        // wurde erreicht:
        Debug.println(Debug.MEDIUM,
                      "UserAdministration: guestname " + paramName
                      + " login failed");  // Fehlermeldung!

        return null;
      }
    }

    return null;  // Es wurde kein Name eingegeben: Die Methode wird beendet.
  }

  /** Liefert eine Namensliste aller User, die nicht G�ste sind. Benutzt getUserEnum(). */
  public Vector getUserNames() {

    Vector tmpVector = new Vector();
    Enumeration enum = this.getUserEnum();
    User tmpUser;

    while (enum.hasMoreElements()) {
      tmpUser = (User) enum.nextElement();

      if (!tmpUser.isGuest()) {
        tmpVector.addElement(tmpUser.getName());
      }
    }

    return tmpVector;
  }

  /**
   * Mittels dieser Methode kann ein User-Objekt bearbeitet werden. Setzt die Daten des Userobjektes mit dem Namen oldName
   * auf die in newUser enthaltenen Daten mittels user.setName(),setPassword(), setIsAdmin(),setAllowedChannelList().
   * Benutzt getFromUserListByName().
   */
  public synchronized void editUser(String oldName, String newName,
                                    String newPassword, boolean paramIsAdmin,
                                    Enumeration allowedChannelEnum) {

    if ((oldName != null) && (newName != null) && (newPassword != null)
            && (newName.compareTo("") != 0)
            && (newPassword.compareTo("") != 0)) {

      // Wenn oldName und newUser nicht null sind...
      User tmpUser = this.getFromUserListByName(oldName);

      // Das "alte" Userobjekt wird geladen.
      if (tmpUser != null) {  // Wenn das Userobjekt existiert, dann
        if (oldName.compareTo(newName) == 0
                | this.getFromUserListByName(newName) == null) {
          Debug.println(Debug.MEDIUM,
                        "UserAdministration: changing: " + tmpUser);
          tmpUser.setName(newName);  // werden die alten Attribute
          tmpUser.setPassword(newPassword);  // name, password, isAdmin und
          tmpUser.setIsAdmin(paramIsAdmin);  // allowedChannelList mit den neuen
          tmpUser.setAllowedChannelList(allowedChannelEnum);  // Werten �berschrieben
          Debug.println(Debug.MEDIUM,
                        "UserAdministration: changed: " + tmpUser);

          // Client �ber Ver�nderungen informieren
          ClientServant tmpClientServant = tmpUser.getClientServant();

          if (tmpClientServant != null) {
            tmpClientServant
              .sendErrorMsg("Benutzerdaten haben sich ge�ndert!\n" + tmpUser);
          }
        } else {
          Debug.println("UserAdministration: editUser: error: " + newName
                        + " existent");
        }
      } else {
        Debug.println("UserAdministration: editUser error: oldName:"
                      + oldName + " not found!");
      }
    } else {
      Debug.println(Debug.HIGH,
                    "UserAdministration: editUser error: wrong parameter!");
    }
  }

  /** F�gt einen Benutzer mittels user.setUserAdministration() zur UserList hinzu. */
  public synchronized void addToUserList(User paramUser) {

    if ((paramUser != null)
            && (this.getFromUserListByName(paramUser.getName()) == null)) {

      // Wenn ein Userobjekt �bergeben wurde, das nicht in der UserList steht...
      this.userList.addElement(paramUser);  // Dann wird es zur userList hinzugef�gt.
      paramUser.setUserAdministration(this);
      Debug.println(Debug.LOW, "UserAdministration: added: " + paramUser);
    }
  }

  /** Entfernt einen Benutzer mittels user.setUserAdministration() aus der UserList. */
  public synchronized void removeFromUserList(User paramUser) {

    if (this.userList.removeElement(paramUser)) {
      ClientServant tmpClientServant = paramUser.getClientServant();

      if (tmpClientServant != null) {
        tmpClientServant
          .sendErrorMsg("Ihr Account wurde vom Admin gel�scht!");
      }

      paramUser.removeYou();
      Debug.println(Debug.LOW, "UserAdministration: removed: " + paramUser);
    }
  }

  /** Gibt den Benutzer mit dem angegebenen Namen zur�ck. Benutzt getUserEnum(), user.getName() */
  public User getFromUserListByName(String name) {

    Enumeration enum = this.getUserEnum();
    User tmpUser;

    while (enum.hasMoreElements()) {  // Geht die Liste bis zum Ende durch
      tmpUser = (User) (enum.nextElement());

      if (tmpUser.getName().compareTo(name) == 0) {  // Wenn der User gefunden wurde,
        return tmpUser;  // wird er zur�ckgegeben.
      }
    }

    return null;  // Es wurde kein User mit "name" gefunden: Es wird null zur�ckgegeben.
  }

  /** Gibt eine Aufz�hlung aller User zur�ck */
  public Enumeration getUserEnum() {
    return Helper.vectorCopy(this.userList).elements();
  }

  /** Setzt userList auf die in userEnum enthaltenen Objekte von Typ User Benutzt addToUserList() und removeFromUserList(). */
  public void setUserList(Enumeration userEnum) {

    Enumeration enum = userEnum;

    if (enum == null) {
      enum = (new Vector()).elements();
    }

    Vector tmpUserList = new Vector();
    User tmpUser;

    while (enum.hasMoreElements()) {
      tmpUser = (User) enum.nextElement();

      tmpUserList.addElement(tmpUser);
      this.addToUserList(tmpUser);
    }

    enum = this.getUserEnum();

    while (enum.hasMoreElements()) {
      tmpUser = (User) enum.nextElement();

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
  private int maxUsers = 100;

  /** Anzahl der G�ste im System */
  private int numCurrentGuests = 0;

  /** Maxmimale Anzahl von eingeloggten G�sten im System */
  private int maxGuests = 100;

  /** Erh�ht den Z�hler numCurrentUsers um 1 */
  public synchronized void incNumCurrentUsers() {

    this.numCurrentUsers++;

    Debug.println(Debug.MEDIUM,
                  "UserAdministration: #User:" + this.numCurrentUsers);
  }

  /** Verkleinert den Z�hler numCurrentUsers um 1 */
  public synchronized void decNumCurrentUsers() {

    this.numCurrentUsers--;

    Debug.println(Debug.MEDIUM,
                  "UserAdministration: #User:" + this.numCurrentUsers);
  }

  /** Erh�ht den Z�hler numCurrentGuests um 1 */
  public synchronized void incNumCurrentGuests() {

    this.numCurrentGuests++;

    Debug.println(Debug.MEDIUM,
                  "UserAdministration: #Guest:" + this.numCurrentGuests);
  }

  /** Verringert den Z�hler numCurrentGuests um 1 */
  public synchronized void decNumCurrentGuests() {

    this.numCurrentGuests--;

    Debug.println(Debug.MEDIUM,
                  "UserAdministration: #guest:" + this.numCurrentGuests);
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
