package Client;

import java.util.Vector;
import java.net.Socket;
import gui.*;
import Util.*;
import Util.Commands.*;
import Util.Debug.Debug;


/**
 * Die Klasse Client stellt dem Benutzer alle n�tigen Methoden zur
 * Verf�gung, um an einer Virtuellen Konferenz, also einem Chat teilnehmen
 * zu k�nnen. Der Benutzer bedient den Client nicht direkt, sondern �ber ein
 * GUI. Der Client leitet alle Nachrichten und Aktionen des Benutzers an
 * seinen ClientServant weiter, und verarbeitet dessen Antworten.
 */
public class Client implements Util.DownlinkOwner {

  /**
   * Betritt den angegebenen Channel. Das entpechende Command mit dem
   * Channelnamen wid an den ClientSevant gesendet und dort mit den
   * entsprechenden Methoden verarbeitet. Benutzt JoinChannelCommand()
   */
  public synchronized final void joinChannel(String name) {
    this.sendCommand(new JoinChannelCommand(name));
  }

  /**
   * Wird aufgerufen, falls beim Betreten eines Channels ein Fehler
   * auftritt und dieser an den Client gegeben wird. Der Benutzer
   * befindet sich nicht in dem Channel und kann durch die GUI nicht
   * interagieren. Der Client und seine GUI werden in den Zustand
   * zur�ckversetzt, der nach dem Login des Users bestand.
   */
  public synchronized final void joinChannelError() {
    gui.displayError("Der gew�nschte Channel ist nicht verf�gbar.");
  }

  /**
   * Trennt die Verbindung des Clients zum Server. Dazu werden die Referenzen
   * auf das Uplink- bzw. Downlinkobjekt entfernt und eine entsprechende
   * Meldung auf dem GUI ausgegeben.
   */
  public synchronized final void stopClient() {

    if (uplink != null) {
      this.uplink.stopUplink();

      this.uplink = null;
    }

    this.setDownlink(null);
    this.gui.clearGui();
  }

  /** Der Name des Benutzers des Clients. */
  protected String currentUserName;
  protected boolean currentUserIsAdmin = false;

  /** Namensliste der Channels, die der Benutzer betreten darf. */
  protected Vector currentAllowedChannelNames;

  /** Der Name des Channels, der momentan betreten wurde. */
  protected String currentChannelName;

  public Vector getChannelMsgBuffer() {
    return this.channelMsgBuffer;
  }

  public void clearChannelMsgBuffer() {
    this.channelMsgBuffer = new Vector();
  }

  /**
   * Wird gebraucht, um die Channels, die der Benutzer betreten darf
   * auf der GUI auszugeben. Dazu werden die entsprechenden Daten via Downlink
   * vom Server geholt und auf dem GUI ausgegeben. Setzt currentUserName und
   * currentAllowedChannelNames.
   */
  public synchronized final void setCurrentUserData(String userName,
          boolean isAdmin, Vector channelNames,
          String paramCurrentChannelName) {

    this.currentUserName = userName;
    this.currentUserIsAdmin = isAdmin;
    this.currentAllowedChannelNames = Util.Helper.quicksort(channelNames);
    this.currentChannelName = paramCurrentChannelName;

    if (this.gui != null) {
      gui.setCurrentUserData(userName, isAdmin,
                             this.currentAllowedChannelNames,
                             this.currentChannelName);
    }
  }

  /**
   * Wird ben�tigt, um die Teilnehmer in dem Channel auf dem GUI
   * anzuzeigen. Die Methode bekommt den Channelnamen und die Liste
   * der Benutzer vom ClientServant durch den Downlink �bergeben und gibt die
   * Liste der Benutzer auf dem GUI aus. Setzt currentChannelName und
   * currentUsersInChannelList.
   */
  public synchronized final void setCurrentChannelData(String paramName,
          Vector userNames) {

    this.currentChannelName = paramName;
    this.currentUsersInChannelList = Util.Helper.quicksort(userNames);

    if (this.gui != null) {
      this.gui.setCurrentChannelData(this.currentChannelName,
                                     this.currentUsersInChannelList);
    }
  }

  /**
   * �ber den Downlink ist der Client in der Lage, Nachrichten vom Client-
   * Servant zu empfangen. Setzt downlink, benachrichtigt das
   * Downlinkobjekt mittels setDownlinkOwner().
   */
  public synchronized final void setDownlink(Util.Downlink paramDownlink) {

    if (this.downlink != paramDownlink) {
      if (this.downlink != null) {
        Downlink old = this.downlink;

        this.downlink = null;

        old.setDownlinkOwner(null);
      }

      this.downlink = paramDownlink;

      if (paramDownlink != null) {
        paramDownlink.setDownlinkOwner(this);
      }
    }
  }

  /**
   * Wird aufgerufen, falls es Probleme mit dem Downlink gibt,
   * bzw. kein Objekt dieser Klasse erzeugt werden kann. Ruft stopClient() auf.
   */
  public synchronized final void downlinkError() {
    stopClient();
  }

  /**
   * Wird aufgerufen, wenn vom ClientServant beim Einloggen, die Nachricht
   * kommt, das das Einloggen nicht m�glich ist. Die Methode sorgt daf�r, das
   * der Benutzer eine Fehlermeldung bekommt und zum Loginscreen zur�ckgelangt.
   * Ruft stopClient() auf.
   */
  public synchronized final void loginError() {

    Debug.println("Client: login failed: ");
    this.gui.loginError();
    stopClient();
  }

  public synchronized final void displayError(String errorMsg) {
    this.gui.displayError(errorMsg);
  }

  /**
   * Dient dazu, eine Nachricht �ber das f�r den Client zust�ndige
   * Uplinkobjekt an den ClientServant zu verschicken. Dabei wird die Nachricht
   * direkt an den Uplink �bergeben. �ber diese Methode l�uft alles, was der
   * Client an den ClientServant sendet. Benutzt uplink.sendMsg(). Bei einem
   * Fehler wird stopClient() aufgerufen.
   */
  protected synchronized final void sendCommand(Command paramCommand) {

    try {
      Uplink tmpUplink = this.uplink;

      if (tmpUplink != null) {
        this.uplink.sendMsg(paramCommand);
      }
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, "Client: " + e);
      this.stopClient();
    }
  }

  /**
   * Meldet den Benutzer an. Name und Passwort werden per Methode sendCommand
   * an den ClientServant gesendet. Sendet LoginCommand().
   */
  public synchronized final void login(String name, String password) {
    this.sendCommand(new LoginCommand(name, password));
  }

  /**
   * Meldet einen Gast an. Die Methode funktioniert wie die normale Methode
   * login, jedoch wird kein Passwort erwartet. Sendet LoginGuestCommand().
   */
  public synchronized final void loginAsGuest(String name) {
    Debug.println("Client: loginGuest " + name);
    this.sendCommand(new LoginGuestCommand(name));
  }

  /**
   * Meldet den Benutzer ab. Die Methode sorgt daf�r, das der Benutzer zum
   * Loginscreen zu�ckgelangt. Sendet ein LogoutCommand()
   */
  public synchronized final void logout() {
    this.sendCommand(new LogoutCommand());
  }

  /**
   * Sendet eine Nachricht an einen einzigen Benutzer. Die Nachricht, sowie
   * der Name des Empf�ngrs (name) wird an die Methode �bergeben, die sie dann
   * via Uplink an den ClientServant sendet. Sendet sendMsgToUserCommand.
   */
  public synchronized final void sendMsgToUser(String name, String msg) {
    this.sendCommand(new SendMsgToUserCommand(name, msg));
    this.channelMsgBuffer.addElement("An " + name + ": " + msg);
  }

  /**
   * Sendet eine Nachricht in einen Channel, die von allen Teilnehmern
   * in diesem Channel empfangen werden kann. Die Nachricht wird an die
   * Methode �bergeben und durch anlegen eines neuen Objekts SendMsgToChannel
   * an den ClientServant gesendet.
   */
  public synchronized final void sendMsgToChannel(String msg) {
    this.sendCommand(new SendMsgToChannelCommand(msg));
  }

  /**
   * Verarbeitet eine empfangene Nachricht bzw. f�hrt den empfangenen Befehl
   * einfach aus. Nachrichten vom Server, die durch den Downlink empfangen
   * werden, werden hier als Parameter eingesetzt.
   */
  public synchronized final void processMsg(Command msg) {
    msg.execute(this);
  }

  /**
   * Dient dazu, einen Client am Server anzumelden. Dazu wird f�r den neuen
   * Client ein Socket am Server ge�ffnet, sowie ein Uplink Objekt und ein
   * Downlink Objekt zur Kommunikation mit dem ClientServant. Benutzt
   * setDownlink, downlink.startDownlink() und uplink.startUplink().
   */
  public synchronized final void startClient() {

    Debug.println(Debug.MEDIUM, "Client: starting");

    try {
      socket = new Socket(SERVER_IP, SERVER_PORT);
      uplink = new Util.Uplink(socket);

      setDownlink(new Util.Downlink(socket, this));
      uplink.startUplink();
      Debug.println("Client: Uplink started. Trying to start downlink...");
      downlink.startDownlink();
      Debug.println("Client: Downlink started.");
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, "Client: error starting: " + e);
      this.stopClient();
    }
  }

  /**
   * Dient dazu, eine Nachricht von einem einzigen Benutzer (fromName),
   * die f�r den gesamten Channel bestimmt ist, zu empfangen. Die Nachricht
   * wird vom Downlink an diese Methode �bergeben und sie sorgt daf�r, da� der
   * empfangene Text in der GUI erscheint und zum channelMsgBuffer hinzugef�gt
   * wird.
   */
  public synchronized final void sendMsgFromChannel(String fromName,
          String msg) {

    Debug.println("Client: " + fromName + " sayz:" + msg);
    this.channelMsgBuffer.addElement(fromName + ": " + msg);

    if (this.gui != null) {
      this.gui.sendMsgFromChannel(fromName, msg);
    }
  }

  /**
   * Dient dazu, eine Nachricht von einem einzigen Benutzer (fromName)
   * zu empfangen. Die Nachricht wird vom Downlink an diese Methode
   * �bergeben und sie sorgt daf�r, da� der empfangene Text in der GUI
   * erscheint und zum channelMsgBuffer hinzugef�gt wird.
   */
  public synchronized final void sendMsgFromUser(String fromName,
          String msg) {

    Debug.println("Client: " + fromName + " whispers: " + msg);
    this.channelMsgBuffer.addElement(fromName + " fl�stert: " + msg);

    if (this.gui != null) {
      this.gui.sendMsgFromUser(fromName, msg);
    }
  }

  public String getServerIP() {
    return SERVER_IP;
  }

  public void setServerIP(String serverIP) {
    SERVER_IP = serverIP;
  }

  /**
   * Der Uplink, �ber ihn werden Nachrichten gesendet.
   * @directed
   * @clientCardinality 1
   * @supplierCardinality 1
   */
  protected Util.Uplink uplink;

  /**
   * Der Downlink, �ber ihn werden Nachrichten empfangen.
   * @directed
   * @supplierCardinality 1
   * @clientCardinality 1
   */
  protected Util.Downlink downlink;

  /** Der Port des Servers. */
  protected int SERVER_PORT = 1500;

  /** Die IP-Adresse des Servers. */
  protected String SERVER_IP = "localhost";

  /**
   * Speichert ankommende Nachrichten in einem Channel. Wird vom GUI benutzt
   * und kann als Protokoll der Unterhaltung in dem Channel dienen.
   */
  public Vector channelMsgBuffer = new Vector();

  /** Der Socket, �ber den die Verbindung zum Server aufgenommen wird. */
  protected Socket socket;

  /**
   * Vector von Strings, repr�sentiert die aktuellen Benutzer in einem Wird im
   * Client Window eingebunden.
   */
  protected Vector currentUsersInChannelList;
  public ChatGui gui;
}
