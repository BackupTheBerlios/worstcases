package Server;

import java.net.Socket;

/**
* Diese Klasse k�mmert sich um die Anfragen, die von einem Client an den
* Server gestellt werden. Der Server
* erzeugt f�r jeden Client eine Instanz dieser Klasse, die sich von da an nur
* noch um diesen Client k�mmert und seine Anfragen bearbeitet. In dieser Klasse
* steckt die meiste Funktionalit�t des Servers.
*/
class ClientServant implements Util.DownlinkOwner {

  /**
   * Konstruktor, der die entsprechenden Attribute setzt.
   */
  public ClientServant(Socket socket, Server server,
                       UserAdministration paramUserAdministration) {}

  public ClientServant() {}

  /**
   * Startet den ClientObserver, danach ist er betriebsbereit und kann
   * die Anfragen seines Clients bearbeiten.
   */
  public void startClientServant() {

    uplink = new Uplink(socket);

    uplink.startUplink();

    downlink = new Downlink(socket, this);

    downlink.startDownlink();
    downlink.start();
  }

  /**
   * Stoppt den ClientServant.
   */
  public void stopClientServant() {}

  /**
   * Pr�ft den Inhalt einer vom Client empfangenen Nachricht und entscheidet,
   * welche Funktionalit�t des ClientObservers
   * aufgerufen werden muss, um den "Wunsch" des Clients zu erf�llen.
   */
  public synchronized void processMsg(String msg) {
    this.loginUser("UserFoo");
  }

  /**
   * Meldet den Benutzer beim System an, benutzt daf�r eine vom Client
   * empfangene Zeichenkette mit Benutzerinformationen.
   */
  public void loginUser(String userSet) {

    this.user = this.userAdministration.loginUser(userSet);

    this.user.setClientServant(this);

    if (this.user.isAdmin()) {
      this.becomeAdminClientServant();
    }

    this.uplink.send(this.user.getAllowedChannelList().toString());
  }

  /**
   * Meldet einen Gast beim System an, benutzt daf�r eine vom Client
   * empfangene Zeichenkette mit Benutzerinformationen.
   */
  public void loginGuest(String guestSet) {

    this.user = this.userAdministration.loginGuest(guestSet);

    this.user.setClientServant(this);
  }

  /**
   * Wenn der angemeldete Benutzer Administratorrechte hat,
   * dann wird aus dem ClientObserver automatisch ein AdminClientObserver
   * mit erweiterter Funktionalit�t.
   */
  public void becomeAdminClientServant() {

    AdminClientServant tmpAdminClientServant =
      new AdminClientServant(this.socket, this.server,
                             this.server.getChannelAdministration(),
                             this.userAdministration, this.user);

    this.user.setClientServant(tmpAdminClientServant);
    this.downlink.setClientServant(tmpAdminClientServant);
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
   * L�sst den User in den Channel mit dem angegebenen Namen eintreten.
   */
  public void joinChannel(String name) {

    Channel tmpChannel = this.user.getFromAllowedChannelByName(name);

    this.user.setCurrentChannel(tmpChannel);
    this.uplink.send(this.user.getCurrentChannel().toString());
  }

  /**
   * L�sst den User den Channel verlassen.
   */
  public void leaveChannel() {
    this.user.setCurrentChannel(null);
  }

  /**
   * Sendet eine Nachricht des Users an alle anderen User im
   * Channel.
   */
  public void sendMsgToChannel(String msg) {

    int pos = 0;
    Channel tmpChannel = this.user.getCurrentChannel();
    User tmpUser = (User) (tmpChannel.getCurrentUserList().elementAt(pos));
    ClientServant tmpClientServant = tmpUser.getClientServant();

    tmpClientServant.sendMsgFromChannel("hello world");
  }

  /**
   * Sendet eine Nachricht, die in den besuchten Channel gesendet wurde,
   * an den Client.
   */
  public void sendMsgFromChannel(String msg) {
    this.uplink.send("hello world");
  }

  /**
   * Sendet eine private Nachricht eines anderen Users an den Client.
   */
  public void sendMsgFromUser(String msg) {
    this.uplink.send(msg);
  }

  /**
   * Sendet eine private Nachricht des Users an einen anderen User.
   */
  public void sendMsgToUser(String userName, String msg) {

    int pos = 0;
    Channel tmpChannel = this.user.getCurrentChannel();
    User tmpUser = (User) (tmpChannel.getCurrentUserList().elementAt(pos));
    ClientServant tmpClientServant = tmpUser.getClientServant();

    tmpClientServant.sendMsgFromUser(msg);
  }

  /**
   * Sendet die Daten des betretenen Channel an den Client.
   */
  public void sendChannelData() {

    Channel tmpChannel = this.user.getCurrentChannel();
    String tmpChannelSet = tmpChannel.toString();

    this.uplink.send(tmpChannelSet);
  }

  /**
   * Sendet die Daten des Users an den Client.
   */
  public void sendUserData() {

    String tmpUserSet = this.user.toString();

    this.uplink.send(tmpUserSet);
  }

  /** @link aggregationByValue
   * @clientCardinality 1
   * @supplierCardinality 1*/
  protected Uplink uplink;

  /** @link aggregationByValue
   * @clientCardinality 1
   * @supplierCardinality 1*/
  protected Downlink downlink;

  /**
   * @clientCardinality 0..*
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
   * @clientCardinality 0..*
   * @supplierCardinality 1
   */
  private Server server;
}
