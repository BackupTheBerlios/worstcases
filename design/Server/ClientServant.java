package Server;

import java.net.Socket;
import Util.*;
import Util.Commands.*;
import java.util.StringTokenizer;
import java.util.Enumeration;


/**
 * Diese Klasse kümmert sich um die Anfragen, die von einem Client an den
 * Server gestellt werden. Der Server
 * erzeugt für jeden Client eine Instanz dieser Klasse, die sich von da an nur
 * noch um diesen Client kümmert und seine Anfragen bearbeitet. In dieser Klasse
 * steckt die meiste Funktionalität des Servers.
 */
public class ClientServant implements Util.DownlinkOwner {

  /**
   * Konstruktor, der die entsprechenden Attribute setzt.
   */
  public ClientServant(Socket paramSocket, Server paramServer,
                       UserAdministration paramUserAdministration) {

    this.socket = paramSocket;
    this.server = paramServer;
    this.userAdministration = paramUserAdministration;
  }

  public ClientServant() {}

  /**
   * Startet den ClientObserver, danach ist er betriebsbereit und kann
   * die Anfragen seines Clients bearbeiten.
   */
  public void startClientServant() {

    this.uplink = new Uplink(this.socket);
    this.downlink = new Util.Downlink(this.socket, this);

    this.uplink.startUplink();
    this.downlink.startDownlink();
    this.downlink.start();
  }

  /**
   * Stoppt den ClientServant.
   */
  public void stopClientServant() {}

  /**
   * Verarbeitet eine empfangene Nachricht, bzw.
   * führt den empfangenen Befehl einfach aus.
   */
  public synchronized void processMsg(Command msg) {
    msg.execute(this);
  }

  /**
   * Meldet den Benutzer beim System an, benutzt dafür eine vom Client
   * empfangene Zeichenkette mit Benutzerinformationen.
   */
  public void loginUser(String name, String password) { 
    // FIXME: an LoginCommand anpassen:
    this.user = this.userAdministration.loginUser(name,password);

    this.user.setClientServant(this);

    if (this.user.isAdmin()) {
      this.becomeAdminClientServant();
    }

  }

  /**
   * Meldet einen Gast beim System an, benutzt dafür eine vom Client
   * empfangene Zeichenkette mit Benutzerinformationen.
   */
  public void loginAsGuest(String name) {

    this.user = this.userAdministration.loginGuest(name);

    this.user.setClientServant(this);
  }

  /**
   * Wenn der angemeldete Benutzer Administratorrechte hat,
   * dann wird aus dem ClientObserver automatisch ein AdminClientObserver
   * mit erweiterter Funktionalität.
   */
  public void becomeAdminClientServant() {

    AdminClientServant tmpAdminClientServant =
      new AdminClientServant(this.socket, this.server,
                             this.server.getChannelAdministration(),
                             this.userAdministration, this.user);

    this.user.setClientServant(tmpAdminClientServant);
    this.downlink.setDownlinkOwner(tmpAdminClientServant);
    this.server.addToClientServantList(tmpAdminClientServant);
    this.server.removeFromClientServantList(this);
  }

  /**
   * Meldet den Benutzer vom System ab und stoppt den Observer.
   */
  public void logoutUser() {
    this.user.setLoggedIn(false);
    this.stopClientServant();
  }

  /**
   * Lässt den User in den Channel mit dem angegebenen Namen eintreten.
   */
  public void joinChannel(String name) {

    Channel tmpChannel = this.user.getFromAllowedChannelByName(name);

    this.user.setCurrentChannel(tmpChannel);
  }

  /**
   * Lässt den User den Channel verlassen.
   */
  public void leaveChannel() {
    this.user.setCurrentChannel(null);
  }

  /**
   * Sendet eine Nachricht des Users an alle anderen User im
   * Channel.
   */
  public void sendMsgToChannel(String msg) {

    Enumeration enum =
      this.user.getCurrentChannel().getCurrentUserList().elements();

    while (enum.hasMoreElements()) {
      User tmpUser = (User) (enum.nextElement());
      ClientServant tmpClientServant = tmpUser.getClientServant();

      tmpClientServant.sendMsgFromChannel(this.user.getName() + " sayz: "
                                          + msg);
    }
  }

  /**
   * Sendet eine Nachricht, die in den besuchten Channel gesendet wurde,
   * an den Client.
   */
  public void sendMsgFromChannel(String msg) {
  }

  /**
   * Sendet eine private Nachricht eines anderen Users an den Client.
   */
  public void sendMsgFromUser(String msg) {
  }

  /**
   * Sendet eine private Nachricht des Users an einen anderen User.
   */
  public void sendMsgToUser(String userName, String msg) {

    int pos = 0;
    Channel tmpChannel = this.user.getCurrentChannel();
    User tmpUser = (User) (tmpChannel.getCurrentUserList().elementAt(pos));
    ClientServant tmpClientServant = tmpUser.getClientServant();

  }

  /**
   * Sendet die Daten des betretenen Channel an den Client.
   */
  public void sendChannelData() {

    Channel tmpChannel = this.user.getCurrentChannel();

  }

  /**
   * Sendet die Daten des Users an den Client.
   */
  public void sendUserData() {

    String tmpUserSet = this.user.toString();

  }

  /**
   * @link aggregationByValue
   * @clientCardinality 1
   * @supplierCardinality 1
   */
  protected Uplink uplink;

  /**
   * @link aggregationByValue
   * @clientCardinality 1
   * @supplierCardinality 1
   */
  protected Util.Downlink downlink;

  /**
   * @clientCardinality 0..
   * @supplierCardinality 1
   */
  protected UserAdministration userAdministration;

  /**
   * der Benutzer der Clientapplikation
   * @clientCardinality 0..1
   * @supplierCardinality 1
   */
  protected User user;
  protected Socket socket;

  /**
   * @clientCardinality 0..
   * @supplierCardinality 1
   */
  private Server server;
}
