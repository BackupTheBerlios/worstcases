package Client;

import java.util.Vector;
import java.net.Socket;
import Util.*;
import Util.Commands.*;


/**
 * Die Clientapplikation
 */
public class Client implements Util.DownlinkOwner {

  /**
   * betritt den angegebenen Channel
   */
  public void joinChannel(String name) {
    uplink.sendMsg(new JoinChannelCommand(name));
  }

  /**
   * verläßt den Channel
   */
  public void leaveChannel() {}

  /**
   * meldet den Benutzer an
   */
  public void login(String name, String password) {
    uplink.sendMsg(new LoginCommand(name, password));
  }

  /**
   * meldet einen Gast an
   */
  public void loginAsGuest(String name) {
    uplink.sendMsg("loginGuest " + name);
  }

  /**
   * meldet den Benutzer ab
   */
  public void logout() {}

  /**
   * setzt availableChannels mit den Daten aus dem String channelSet.
   * Der Client erhält die für den Benutzer zugänglichen Channels.
   */
  public void setAvailableChannelList(String channelSet) {}

  /**
   * setzt currentUsers mit den Daten aus dem String userSet.
   * Der Client erhält die Liste der aktuellen Benutzer in dem
   * betretenen Channel
   */
  public void setCurrentUserInChannelList(String userSet) {}

  /**
   * sendet eine Nachricht an einen Benutzer.
   * 1 zu 1 Kommunikation
   */
  public void sendMsgToUser(String name, String msg) {}

  /**
   * sendet eine Nachricht in einen Channel.
   * Diese wird dann von allen Teilnehmern im
   * Channel empfangen.
   */
  public void sendMsgToChannel(String  msg) {
    uplink.sendMsg(new SendMsgToChannelCommand(msg));
  }

  /**
   * Verarbeitet eine empfangene Nachricht bzw. führt den empfangenen
   * Befehl einfach aus.
   * Nachrichten vom Server, die durch den Downlink empfangen werden,
   * werden hier als Parameter eingesetzt.
   */
  public void processMsg(Command  msg) {
    /* Muss wg. Command überarbeitet werden.
    this.setAvailableChannelList(
      "Mensachat, Virtuelle Konferenz, tubs intern");
    this.channelMsgBuffer=    this.channelMsgBuffer.concat(msg+"\n");  
    */
    msg.execute(this);
  }

  public void startClient() {

    try {
      socket = new Socket(Client.SERVER_IP, Client.SERVER_PORT);
    } catch (java.io.IOException e) {
      System.out.println(e);
    }

    uplink = new Uplink(socket);
    downlink = new Downlink(socket, this);

    uplink.startUplink();
    downlink.startDownlink();
    downlink.start();
  }

  /**
   * @directed
   * @clientCardinality 1
   * @supplierCardinality 1
   */
  protected Uplink uplink;

  /**
   * @directed
   * @supplierCardinality 1
   * @clientCardinality 1
   */
  protected Downlink downlink;

  /**
   * der Port des Servers
   */
  protected final static int SERVER_PORT = 1500;

  /**
   * die IP - Adresse des Servers
  */
  protected final static String SERVER_IP = "localhost";

  /**
   * Vector von Strings, repräsentiert die für den Benutzer freigegebenen 
   * Channels.
   */
  protected Vector availableChannelList;

  /**
   * Speichert ankommende Nachrichten in einem Channel.
   * Wird vom GUI benutzt und kann als
   * Protokoll der Unterhaltung in dem Channel dienen.
   */
  public String channelMsgBuffer = new String();
  protected Socket socket;

  /**
   * Vector von Strings, repräsentiert die aktuellen Benutzer in einem Channel
   */
  protected Vector currentUserInChannelList;
}
