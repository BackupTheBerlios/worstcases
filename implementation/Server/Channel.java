package Server;

import java.util.Vector;
import java.util.Enumeration;
import Util.Debug.Debug;
import Util.Helper;


/**
 * Ein Channeldatensatz. Eine Instanz dieser Klasse kann wichtige Channeldaten wie beispielsweise
 * berechtigte User oder im Channel befindliche User speichern.
 * Au�erdem stehen Methoden zur Verf�gung, mit denen Listen der berechtigten
 * bzw. im Raum befindlichen User abgefragt und �bergeben werden k�nnen.
 * Desweiteren k�nnen einzelne User zu den CurrentUser hinzugef�gt bzw.
 * wieder entfernt werden, wenn sie den Channel betreten bzw. verlassen.
 */
class Channel {

  /** Konstruktur, der die entsprechenden Attribute setzt. */
  public Channel(String paramName, boolean paramAllowedForGuests) {
    this.name = paramName;
    this.allowedForGuest = paramAllowedForGuests;
  }

  /** Gibt den Namen des Channels zur�ck. */
  public String getName() {
    return name;
  }

  /** Setzt den Namen des Channels. Ruft informCurrentUsers() auf. */
  public synchronized void setName(String paramName) {

    if (paramName != null) {
      if (this.name != paramName) {
        this.name = paramName;

        this.informCurrentUsers();
      }
    }
  }

  /** Gibt an, ob der Channel von G�sten betreten werden darf. */
  public boolean isAllowedForGuest() {
    return allowedForGuest;
  }

  /**
   * Setzt, ob G�ste den Channel betreten d�rfen. Benachrichtig ggf. betroffene User mittels user.isGuest() und
   * user.removeFromAllowedChannelList(this)
   */
  public synchronized void setAllowedForGuest(boolean b) {

    boolean oldValue = this.isAllowedForGuest();

    this.allowedForGuest = b;

    if (oldValue != b) {

      // Channel ist nicht mehr f�r G�ste frei
      if (oldValue) {
        Enumeration enum = this.getCurrentUserEnum();
        User tmpUser;

        // alle G�ste aus der Liste der berechtigten User entfernen
        while (enum.hasMoreElements()) {
          tmpUser = (User) enum.nextElement();

          if (tmpUser.isGuest()) {
            tmpUser.removeFromAllowedChannelList(this);
          }
        }
      }
    }
  }

  /**
   * Die Namensliste als Vector von Strings der berechtigten Benutzer wird zur�ckgegeben Benutzt
   * getAllowedUserEnum() und User.getName().
   */
  public Vector getAllowedUserNames() {

    Vector tmpVector = new Vector();
    Enumeration enum = this.getAllowedUserEnum();

    // alle Namen zu tmpVector hinzuf�gen
    while (enum.hasMoreElements()) {
      tmpVector.addElement(((User) enum.nextElement()).getName());
    }

    return tmpVector;
  }

  /** Gibt eine Aufz�hlung der aktuellen Benutzerobjekte im Channel zur�ck. */
  public Enumeration getAllowedUserEnum() {
    return Helper.vectorCopy(this.allowedUserList).elements();
  }

  /**
   * Setzt allowedUserList auf die in enumAllowedUser �bergebenen Werte
   * Benutzt addToAllowedUserList() und removeFromAllowedUserList().
   */
  public synchronized void setAllowedUserList(Enumeration enumAllowedUser) {

    Enumeration enum = enumAllowedUser;

    // mit einer leeren Aufz�hlung beginnen, falls null �bergeben wurde
    if (enum == null) {
      enum = (new Vector()).elements();
    }

    Vector tmpList = new Vector();
    User tmpUser;

    // alle User aus enum hinzuf�gen, zus�tzlich in tmpList speichern
    while (enum.hasMoreElements()) {
      tmpUser = (User) enum.nextElement();

      tmpList.addElement(tmpUser);
      this.addToAllowedUserList(tmpUser);
    }

    enum = this.getAllowedUserEnum();

    /*
     * alle Benutzer aus allowedUserList entfernen, die nicht in enumAllowedUser stehen
     */
    while (enum.hasMoreElements()) {
      tmpUser = (User) enum.nextElement();

      if (!tmpList.contains(tmpUser)) {
        this.removeFromAllowedUserList(tmpUser);
      }
    }
  }

  /**
   * F�gt einen Benutzer zur Liste der berechtigten Benutzer hinzu. Benachrichtigt mittels User.addToAllowedChannelList()
   * das entsprechende User-Objekt.
   */
  public void addToAllowedUserList(User paramUser) {

    if (paramUser != null) {
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
  public void removeFromAllowedUserList(User paramUser) {

    if (paramUser != null) {

      // removeElement() gibt true zur�ck, falls paramUser entfernt wurde
      if (this.allowedUserList.removeElement(paramUser)) {
        paramUser.removeFromAllowedChannelList(this);
      }
    }
  }

  /**
   * Eine Namensliste als Vector von Strings der aktuellen Benutzer wird zur�ckgegeben. Benutzt
   * getCurrentUserEnum() und User.getName()
   */
  public Vector getCurrentUserNames() {

    Vector tmpVector = new Vector();
    Enumeration enum = this.getCurrentUserEnum();

    while (enum.hasMoreElements()) {
      tmpVector.addElement(((User) enum.nextElement()).getName());
    }

    return tmpVector;
  }

  /** Liefert eine Aufz�hlung der aktuellen Benutzer zur�ck. */
  public Enumeration getCurrentUserEnum() {
    return Helper.vectorCopy(currentUserList).elements();
  }

  /**
   * Setzt CurrentUserList auf die in enumCurrentUser �bergebenen Werte.
   * Benutzt addToCurrentUserList() und removeFromCurrentUserList().
   */
  public void setCurrentUserList(Enumeration enumCurrentUser) {

    Enumeration enum = enumCurrentUser;

    // falls null �bergeben wurde wird eine leere Aufz�hlung benutzt
    if (enumCurrentUser == null) {
      enum = (new Vector()).elements();
    }

    Vector tmpList = new Vector();
    User tmpUser;

    // alle User aus enumCurrentUser hinzuf�gen
    while (enum.hasMoreElements()) {
      tmpUser = (User) enum.nextElement();

      tmpList.addElement(tmpUser);
      this.addToCurrentUserList(tmpUser);
    }

    enum = this.getCurrentUserEnum();

    // alle Benutzer, die nicht in tmpList, also in enumCurrentUser stehen aus der Liste entfernen
    while (enum.hasMoreElements()) {
      tmpUser = (User) enum.nextElement();

      if (!tmpList.contains(tmpUser)) {
        this.removeFromCurrentUserList(tmpUser);
      }
    }
  }

  /**
   * F�gt einen User zu den im Channel befindlichen Usern hinzu. Benachrichtigt den User mittels User.setCurrentChannel().
   * Ruft dann informCurrentUsers() auf.
   */
  public void addToCurrentUserList(User paramUser) {

    if (paramUser != null) {
      if (!this.currentUserList.contains(paramUser)) {
        this.currentUserList.addElement(paramUser);
        paramUser.setCurrentChannel(this);
        this.informCurrentUsers();
      }
    }
  }

  /**
   * Entfernt einen User aus den im Channel befindlichen Usern. Benachrichtigt den User mittels User.setCurrentChannel(null).
   * Ruft dann informCurrentUsers() auf.
   */
  public void removeFromCurrentUserList(User paramUser) {

    // removeElement() gibt true zur�ck, falls paramUser entfernt wurde
    if (this.currentUserList.removeElement(paramUser)) {
      paramUser.setCurrentChannel(null);
      this.informCurrentUsers();
    }
  }

  /** Entfernt das Channelobjekt aus dem System. Benutzt setCurrentUserList(null) und setAllowedUserList(null). */
  public void removeYou() {
    this.setCurrentUserList(null);
    this.setAllowedUserList(null);
  }

  /** Dient dem debugging. */
  public String toString() {

    String tmpString = " Name:" + name + " allowed for guests:"
                       + this.allowedForGuest + " allowed user: ";
    Enumeration enum = this.getAllowedUserEnum();

    while (enum.hasMoreElements()) {
      tmpString = tmpString + ((User) enum.nextElement()).getName() + " ";
    }

    return tmpString;
  }

  /**
   * Informiert die Clients der aktuellen Benutzer im Channel �ber Ver�nderungen im Channel mittels getCurrentUserEnum(),
   * User.getClientServant() und ClientServant.sendCurrentChannelData().
   */
  public void informCurrentUsers() {

    Enumeration enum = this.getCurrentUserEnum();
    User tmpUser;
    ClientServant tmpClientServant;

    // bei allen ClientServants sendCurrentChannelData() ausf�hren
    while (enum.hasMoreElements()) {
      tmpUser = (User) enum.nextElement();
      tmpClientServant = tmpUser.getClientServant();

      if (tmpClientServant != null) {
        tmpClientServant.sendCurrentChannelData();
      }
    }
  }

  /** Der Name des Channels. */
  private String name = new String();

  /** "Erlaubt-f�r-G�ste"-Flag. */
  private boolean allowedForGuest = false;

  /**
   * Gibt die Benutzer an, die den Channel betreten d�rfen.
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
