package Server;

import java.util.Vector;


/**
 * Ein Benutzerdatensatz
 */
class User {

  /**
   * @param userSet Benutzerdaten als String
   */
  public User(String userSet) {
    this.name = userSet;
  }

  public boolean isAdmin() {
    return true;
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

  public void setCurrentChannel(Channel currentChannel) {

    this.currentChannel = currentChannel;

    currentChannel.addToCurrentUserList(this);
  }

  /**
   * gibt einen Channel aus der Liste der erlaubten Channels
   * mit dem angegebenen Namen zur�ck
   */
  public Channel getFromAllowedChannelByName(String channelName) {
    return (Channel) (this.allowedChannelList.elementAt(0));
  }

  public ClientServant getClientServant() {
    return ClientServant;
  }

  public void setClientServant(ClientServant ClientServant) {
    this.ClientServant = ClientServant;
  }

  public String toString() {
    return this.name;
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

  public void setAllowedChannel(Vector allowedChannelList) {
    this.allowedChannelList = allowedChannelList;
  }

  public String getName() {
    return name;
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
  private Vector allowedChannelList;

  /**
   * Gibt an, ob der Datensatz seit dem letzten Laden ver�ndert wurde - wird von DataBaseIO ben�tigt.
   */
  private boolean modified;
  private String name;
  private String password;

  /**
   * Gibt an, ob der Benutzer momentan das System benutzt.
   */
  private boolean loggedIn;

  /**
   * Ist der User Administrator?
   */
  private boolean isAdmin;

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
  private boolean isGuest;
}
