package Server;

import java.util.Vector;


/**
 * Stellt Methoden bereit, um die Benutzer- und Channeldatenbank zu laden
 * und zu speichern. Außerdem wird dafür gesorgt, daß für den Betrieb die
 * relationalen Beziehungen zwischen User- und Channeldatenbank gesetzt werden.
 */
class DataBaseIO {

  /**
   * Lädt die Benutzer- und Channeldaten aus userDBFile und channelDBFile.
   */
  public void loadFromDisk() {

    Channel tmpChannel = new Channel();

    tmpChannel.setName("Virtuelle Konferenz");

    Vector tmpVector = new Vector();

    tmpVector.add(tmpChannel);
    this.channelAdministration.setChannelList(tmpVector);
    doLinks();
  }

  /**
   * Speichert die Benutzer- und Channeldaten in userDBFile und channelDBFile.
   */
  public void saveToDisk() {}

  /**
   * Stellt die relationalen Beziehungen zwische User- und Channeldatensätzen her.
   */
  private void doLinks() {

    /*
     * int pos=0;
     * Channel tmpChannel=(Channel)(this.channelAdministration.getChannelList().elementAt(pos));
     * User tmpUser=(User)(this.userAdministration.getUserList().elementAt(pos));
     * tmpChannel.setAllowedUser(new Vector());
     * tmpUser.setAllowedChannel(new Vector());
     */
  }

  public ChannelAdministration getChannelAdministration() {
    return channelAdministration;
  }

  public void setChannelAdministration(
          ChannelAdministration channelAdministration) {
    this.channelAdministration = channelAdministration;
  }

  public UserAdministration getUserAdministration() {
    return userAdministration;
  }

  public void setUserAdministration(UserAdministration userAdministration) {
    this.userAdministration = userAdministration;
  }

  /**
   * @clientCardinality 1
   * @supplierCardinality 1
   */
  private UserAdministration userAdministration;

  /**
   * @clientCardinality 1
   * @supplierCardinality 1
   */
  private ChannelAdministration channelAdministration;

  /**
   * Dateiname der Channeldatenbank.
   */
  private final static String channelDBFile = "channel.db";

  /**
   * Dateiname der Benutzerdatenbank.
   */
  private final static String userDBFile = "user.db";
}
