package Server;

import java.net.Socket;
import Util.*;


/**
 * Verarbeitet die Anfragen eines AdminClients.
 */
class AdminClientServant extends ClientServant implements DownlinkOwner {

  /**
   * Konstruktor, setzt die entsprechenden Attribute.
   */
  public AdminClientServant(Socket socket, Server server,
                            ChannelAdministration paramChannelAdministration,
                            UserAdministration paramUserAdministration,
                            User paramUser) {}

  /**
   * Sendet eine Liste aller Channelnamen.
   */
  public void sendChannelList() {

    String tmpList = this.channelAdministration.getChannelList().toString();

    this.uplink.sendMsg(tmpList);
  }

  /**
   * Sendet eine Liste aller Benutzernamen.
   */
  public void sendUserList() {

    String tmpList = this.userAdministration.getUserList().toString();

    this.uplink.sendMsg(tmpList);
  }

  /**
   * Fügt einen Channel hinzu.
   */
  public void addChannel(String channelSet) {

    Channel tmpChannel = new Channel(channelSet);

    this.channelAdministration.addToChannelList(tmpChannel);
  }

  /**
   * Löscht den Channel mit dem angegebenen Namen.
   */
  public void deleteChannel(String channelName) {

    Channel tmpChannel = this.channelAdministration.getByName(channelName);

    this.channelAdministration.removeFromChannelList(tmpChannel);
  }

  /**
   * Verändert die Daten des angegebenen Channels.
   */
  public void editChannel(String channelName, String newChannelSet) {
    this.channelAdministration.editChannel(channelName, newChannelSet);
    this.uplink.sendMsg("channel Bar changed");
  }

  /**
   * Fügt einen Benutzer hinzu.
   */
  public void addUser(String userSet) {

    User tmpUser = new User(userSet);

    this.userAdministration.addToUserList(tmpUser);
  }

  /**
   * Löscht den Benutzer mit dem angegebenen Namen.
   */
  public void deleteUser(String userName) {

    User tmpUser = this.userAdministration.getFromUserListByName(userName);

    this.userAdministration.removeFromUserList(tmpUser);
  }

  /**
   * Ändert die Benutzerdaten des Benutzers mit dem
   * angegebenen Namen.
   * Setzt die neuen Daten des Benutzers mit den
   * Daten aus newUserSet.
   */
  public void editUser(String userName, String newUserSet) {
    this.deleteUser(userName);
    this.addUser(newUserSet);
  }

  /**
   * Sendet die Benutzerdaten des Benutzers mit dem
   * angegebenen Namen.
   */
  public void sendUser(String userName) {

    String tmpUserSet =
      this.userAdministration.getFromUserListByName(userName).toString();

    this.uplink.sendMsg(tmpUserSet);
  }

  /**
   * Sendet die Channeldaten des Channels mit dem
   * angegebenen Namen
   */
  public void sendChannel(String channelName) {

    String tmpChannelSet =
      this.channelAdministration.getByName(channelName).toString();

    this.uplink.sendMsg(tmpChannelSet);
  }

  /**
   * Prüft den Inhalt einer vom AdminClient empfangenen Nachricht und
   * entscheidet,welche Methode
   * aufgerufen werden muss, um den "Wunsch" des AdminClients zu erfüllen.
   */
  public void processMsg(String msg) {
    this.deleteChannel("channel foo");
    this.uplink.sendMsg("channel foo deleted");
  }

  /**
   * @clientCardinality 0..1
   * @supplierCardinality 1
   */
  private ChannelAdministration channelAdministration;
}
