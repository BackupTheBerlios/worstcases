package Server;

import java.net.Socket;
import Util.*;
import Util.Commands.*;


/**
 * Verarbeitet die Anfragen eines AdminClients.
 */
public class AdminClientServant extends ClientServant implements DownlinkOwner {

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

  }

  /**
   * Sendet eine Liste aller Benutzernamen.
   */
  public void sendUserList() {

    String tmpList = this.userAdministration.getUserList().toString();

  }

  /**
   * Fügt einen Channel hinzu.
   */
  public void addChannel(String channelSet) {

  }

  /**
   * Löscht den Channel mit dem angegebenen Namen.
   */
  public void deleteChannel(String channelName) {

    Channel tmpChannel = this.channelAdministration.getFromChannelListByName(channelName);

    this.channelAdministration.removeFromChannelList(tmpChannel);
  }

  /**
   * Verändert die Daten des angegebenen Channels.
   */
  public void editChannel(String channelName, String newChannelSet) {
    this.channelAdministration.editChannel(channelName, newChannelSet);
  }

  /**
   * Fügt einen Benutzer hinzu.
   */
  public void addUser(User paramUser) {
    this.userAdministration.addToUserList(paramUser);
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
  public void editUser(String oldName, User paramUser) {
    this.deleteUser(oldName);
    this.addUser(paramUser);
  }

  /**
   * Sendet die Benutzerdaten des Benutzers mit dem
   * angegebenen Namen.
   */
  public void sendUser(String userName) {

    String tmpUserSet =
      this.userAdministration.getFromUserListByName(userName).toString();

  }

  /**
   * Sendet die Channeldaten des Channels mit dem
   * angegebenen Namen
   */
  public void sendChannel(String channelName) {

    String tmpChannelSet =
      this.channelAdministration.getFromChannelListByName(channelName).toString();

  }

  /**
   * Prüft den Inhalt einer vom AdminClient empfangenen Nachricht und
   * entscheidet,welche Methode
   * aufgerufen werden muss, um den "Wunsch" des AdminClients zu erfüllen.
   */
  public void processMsg(String msg) {
    this.deleteChannel("channel foo");
  }

  /**
   * @clientCardinality 0..1
   * @supplierCardinality 1
   */
  private ChannelAdministration channelAdministration;
}
