package Client;

import java.util.Vector;
import java.net.Socket;
import Util.*;


/**
 * Die Clientapplikation
 */
public class Client implements Util.DownlinkOwner {

  /**
   * betritt den angegebenen Channel
   */
  public void joinChannel(String name) {
    this.uplink.sendMsg("joinChannel " + name);
  }

  /**
   * verläßt den Channel
   */
  public void leaveChannel() {}

  /**
   * meldet den Benutzer an
   */
  public void login(String name, String password) {
    this.uplink.sendMsg("login Foo password Bar");
  }

  /**
   * meldet einen Gast an
   */
  public void loginAsGuest(String name) {
    this.uplink.sendMsg("loginGuest " + name);
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
  public void sendMsgToChannel(String msg) {
    this.uplink.sendMsg("channelmsg " + msg);
  }

  /**
   * verarbeitet eine empfangene Nachricht, entscheidet,
   * welche Funktionalität aufgerufen werden muß.
   * Nachrichten vom Server, die durch den Downlink empfangen werden,
   * werden hier als Parameter eingesetzt.
   */
  public void processMsg(String msg) {
    this.setAvailableChannelList(
      "Mensachat, Virtuelle Konferenz, tubs intern");
   this.channelMsgBuffer=    this.channelMsgBuffer.concat(msg+"\n");  
      
  }

  public void startClient() {

    try {
      this.socket = new Socket(this.SERVER_IP, this.SERVER_PORT);
    } catch (java.io.IOException e) {
      System.out.println(e);
    }

    this.uplink = new Uplink(this.socket);
    this.downlink = new Downlink(this.socket, this);

    this.uplink.startUplink();
    this.downlink.startDownlink();
    this.downlink.start();
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
<<<<<<< Client.java
  */
  protected final static String SERVER_IP = "localhost" ;
=======
   */
  protected final static String SERVER_IP = "134.169.8.196";
>>>>>>> 1.4

  /**
   * Vector von Strings, repräsentiert die für den Benutzer freigegebenen Channels
   */
  protected Vector availableChannelList;

  /**
   * speichert ankommende Nachrichten in einem Channel.
   * Wird von der GUI benutzt und kann als
   * Protokoll der Unterhaltung in dem Channel dienen.
   */
  public String channelMsgBuffer=new String();
  protected Socket socket;

  /**
   * Vector von Strings, repräsentiert die aktuellen Benutzer in einem Channel
   */
  protected Vector currentUserInChannelList;
}
