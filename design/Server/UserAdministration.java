package Server;

import java.util.Vector;
import java.util.Enumeration;


/**
 * Verwaltet die Benutzer
 */
class UserAdministration {

  public UserAdministration(
          DataBaseIO paramDataBaseIO,
          ChannelAdministration paramChannelAdministration) {
    this.channelAdministration = paramChannelAdministration;
  }

  /**
   * Meldet einen Benutzer an.
   * @param userSet die vom Client empfangenen Benutzerdaten
   * @return den Benutzer, falls Authentifizierung klappt, sonst null
   */
  public synchronized User loginUser(String name,String password) {
    User tmpUser=this.getFromUserListByName(name);
    if(tmpUser==null){
     return null;
    }
    else{
     if(tmpUser.getPassword().compareTo(password)==0&&(!tmpUser.isLoggedIn())){
      tmpUser.setLoggedIn(true);
      return tmpUser;
     }
     else{return null;}

    }

  }

  /**
   * Meldet einen Gast an.
   * @param guestSet die vom Client empfangenen Gastdaten
   * @return den Benutzer, falls Authentifizierung klappt, sonst null
   */
  public synchronized User loginGuest(String paramName) {
    if(this.getFromUserListByName(paramName)==null){
      User tmpUser=new User(paramName,"guest",true,false,this.channelAdministration.getFreeForGuestList());
      tmpUser.setLoggedIn(true);
      this.userList.addElement(tmpUser);
      return tmpUser;
    }
    else{return null;}

  }

  /**
   * Fügt einen Benutzer hinzu.
   */
  public void addToUserList(User paramUser) {}

  /**
   * Entfernt einen Benutzer.
   */
  public void removeFromUserList(User paramUser) {
    paramUser.getClientServant().stopClientServant();
    this.userList.removeElement(paramUser);
  }

  /**
   * Gibt den Benutzer mit dem angegebenen Namen zurück.
   */
  public User getFromUserListByName(String name) {
    Enumeration enum=this.userList.elements();
    User tmpUser;
    while (enum.hasMoreElements()){
     tmpUser=(User)(enum.nextElement());
     if(tmpUser.getName().compareTo(name)==0){
      return tmpUser;
     }

    }
    return null;
  }

  public java.util.Vector getUserList() {
    return userList;
  }

  public void setUserList(Vector userList) {
    this.userList = userList;
  }

  /**
   * @link aggregation
   *     @associates <{User}>
   * @clientCardinality 1
   * @supplierCardinality 0..
   */
  private Vector userList;

  /**
   * Anzahl der eingeloggten Benutzer(registrierte und Gäste) im System.
   */
  private int numCurrentUsers;

  /**
   * Maximale Anzahl von eingeloggten Benutzern im System.
   */
  private int maxUsers;

  /**
   * @clientCardinality 1
   * @supplierCardinality 1
   */
  private DataBaseIO dataBaseIO;
  private ChannelAdministration channelAdministration;
}
