package Server;

import java.net.Socket;
import Util.*;
import Util.Commands.*;
import java.util.StringTokenizer;
import java.util.Enumeration;
import Util.Debug.Debug;


/**
 * Diese Klasse kümmert sich um die Anfragen, die von einem Client an den Server gestellt werden. Der Server
 * erzeugt für jeden Client eine Instanz dieser Klasse, die sich von da an nur
 * noch um diesen Client kümmert und seine Anfragen bearbeitet. In dieser Klasse steckt die meiste Funktionalität des Servers.
 */
public class ClientServant implements Util.DownlinkOwner {

  /** Standard-Konstruktor, notwendig, da der AdminClientServant von dieser Klasse erbt */
  public ClientServant() {}

  /** Konstruktor, der die entsprechenden Attribute setzt. Benutzt setServer(paramServer) */
  public ClientServant(Socket paramSocket, Server paramServer,
                       UserAdministration paramUserAdministration) {

    this.socket = paramSocket;

    this.setServer(paramServer);

    this.userAdministration = paramUserAdministration;
  }

  /** Setzt downlink, benachrichtigt die betroffenen Downlinkobjekte mittels setDownlinkOwner() */
  public void setDownlink(Downlink paramDownlink) {

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

  /** Setzt user, benutzt user.setClientServant() - setUser(null) bewirkt ein stopClientServant() */
  public void setUser(User paramUser) {

    if (this.user != paramUser) {
      if (this.user != null) {
        User old = this.user;

        this.user = null;

        old.setClientServant(null);
      }

      this.user = paramUser;

      if (paramUser != null) {
        paramUser.setClientServant(this);
      } else {
        this.stopClientServant();
      }
    }
  }

  /**
   * siehe auch interface DownlinkOwner: wird vom Downlink aufgerufen, falls beim Empfang von Nachrichten ein
   * Fehler auftritt, enthält Fehlerbehandlung FIXME: Sinnvolle Möglichkeiten außer stopClientServant() ?
   */
  public void downlinkError() {
    Debug.println("ClientServant: downlink error");
    this.stopClientServant();
  }

  /**
   * Startet den ClientObserver, danach ist er betriebsbereit und kann die Anfragen seines Clients bearbeiten.
   * benutzt setDownlink(), uplink.startUplink() und downlink.startDownlink()
   * fängt Fehler aus startUplink() und startDownlink() ab FIXME: stopClientServant() ?
   */
  public void startClientServant() {

    this.uplink = new Uplink(this.socket);

    this.setDownlink(new Downlink(this.socket, this));

    try {
      this.uplink.startUplink();
      this.downlink.startDownlink();
      Debug.println("ClientServant: started");
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, e);
      this.stopClientServant();
    }
  }

  /**
   * Stoppt den ClientServant. benutzt setDownlink(null) uplink.stopUplink(), setServer(null) und setUser(null)
   * sendet ein stopClientCommand an den Client
   */
  protected synchronized void stopClientServant() {

    this.setDownlink(null);

    if (this.uplink != null) {
      this.sendCommand(new StopClientCommand());
      this.uplink.stopUplink();
    }

    this.setUser(null);
    this.setServer(null);
    Debug.println("ClientServant: stopped");
  }

  /** Verarbeitet eine empfangene Nachricht, bzw. führt den empfangenen Befehl einfach aus. */
  public synchronized void processMsg(Command msg) {
    msg.execute(this);
  }

  /** sendet das angegebene CommandObjekt über den Uplink bei einem Fehler: Fehlerbehandlung FIXME: stopClientServant() ? */
  public void sendCommand(Command paramCommand) {

    try {
      this.uplink.sendMsg(paramCommand);
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, e);
      this.stopClientServant();
    }
  }

  /**
   * Meldet den Benutzer name mit Passwort password beim System an
   * benutzt userAdministration.loginUser() um das Userobjekt zu holen und setUser() um es zu setzen
   * bei einem Loginfehler wird ein LoginErrorCommand gesendet und setUser(null) aufgerufen -> clientServant beendet sich
   * falls der User Admin-Rechte hat, so wird becomeAdminClient() aufgerufen
   */
  public void loginUser(String name, String password) {

    User tmpUser = this.userAdministration.loginUser(name, password);

    if (tmpUser == null) {
      Debug.println("ClientServant: login user " + name + " failed");
      this.sendCommand(new Util.Commands.LoginErrorCommand());
      this.setUser(null);
    } else {
      Debug.println("ClientServant: User " + user.getName() + " logged in");
      this.setUser(tmpUser);

      if (this.user.isAdmin()) {
        Debug.println("ClientServant: becoming AdminClientServant");
        this.becomeAdminClientServant();
      }
    }
  }

  /**
   * Meldet einen Gast beim System an benutzt userAdministration.loginGuest() und setUser()
   * sendet LoginErrorCommand, falls login nicht möglich
   */
  public void loginAsGuest(String name) {

    User tmpUser = this.userAdministration.loginGuest(name);

    if (tmpUser == null) {
      Debug.println("ClientServant: login guest " + name + " failed");
      this.sendCommand(new Util.Commands.LoginErrorCommand());
      this.setUser(null);
    } else {
      Debug.println("ClientServant: Guest " + this.user.getName() + " logged in");
      this.setUser(tmpUser);
    }
  }

  /**
   * Wenn der angemeldete Benutzer Administratorrechte hat, dann wird aus dem ClientObserver automatisch ein AdminClientObserver
   * mit erweiterter Funktionalität. benutzt den AdminClientServant - Konstruktor
   * setzt uplink=null und ruft this.stopClientServant() auf
   */
  public void becomeAdminClientServant() {

    Debug.println("ClientServant: becoming AdminClientServant");

    AdminClientServant tmpAdminClientServant =
      new AdminClientServant(this.uplink, this.downlink, this.server,
                             this.server.getChannelAdministration(),
                             this.userAdministration, this.user);

    this.uplink = null;

    this.stopClientServant();
  }

  /**
   * Meldet den Benutzer vom System ab und stoppt den Observer. benutzt nur setUser(null)
   * diese setMethode bewirkt letztendlich ein stopClientServant();
   */
  public void logoutUser() {
    this.setUser(null);
  }

  /**
   * Lässt den User in den Channel mit dem angegebenen Namen eintreten.
   * benutzt user.setCurrentChannel() und user.getFromAllowedChannelByName()
   * falls der gewünschte Channel nicht existiert wird ein JoinChannelErrorCommand gesendet
   * falls user nicht existiert, wird ein LoginErrorCommand gesendet
   */
  public void joinChannel(String name) {

    if (this.user != null) {
      Channel tmpChannel = this.user.getFromAllowedChannelByName(name);

      if (tmpChannel != null) {
        this.user.setCurrentChannel(tmpChannel);
      } else {
        this.sendCommand(new JoinChannelErrorCommand());
      }
    } else {
      this.sendCommand(new LoginErrorCommand());
    }
  }

  /**
   * Lässt den User den Channel verlassen. benutzt user.setCurrentChannel(null)
   * falls user nicht existent, sendet LoginErrorCommand()
   */
  public void leaveChannel() {

    if (this.user != null) {
      this.user.setCurrentChannel(null);
    } else {
      this.sendCommand(new LoginErrorCommand());
    }
  }

  /**
   * Sendet eine Nachricht des Users an alle anderen User im Channel.
   * sendet ggf. sendMsgToChannelErrorCommand(),loginErrorCommand
   */
  public void sendMsgToChannel(String msg) {

    if (this.user != null) {
      if (this.user.getCurrentChannel() != null) {
        Enumeration enum = this.user.getCurrentChannel().getCurrentUserEnum();
        User tmpUser;

        while (enum.hasMoreElements()) {
          tmpUser = (User) enum.nextElement();

          tmpUser.getClientServant().sendMsgFromChannel(this.user.getName(),
                  msg);
        }
      } else {
        this.sendCommand(new SendMsgToChannelErrorCommand());
      }
    } else {
      this.sendCommand(new LoginErrorCommand());
    }
  }

  /**
   * Sendet eine Nachricht, die in den besuchten Channel gesendet wurde, an den Client.
   * sendet SendMsgFromChannelCommand
   */
  public void sendMsgFromChannel(String fromName, String msg) {
    this.sendCommand(new Util.Commands.SendMsgFromChannelCommand(fromName,
            msg));
  }

  /**
   * Sendet eine private Nachricht eines anderen Users an den Client.
   * sendet SendMsgFromUserCommand()
   */
  public void sendMsgFromUser(String fromName, String msg) {
    this.sendCommand(new Util.Commands.SendMsgFromUserCommand(fromName, msg));
  }

  /**
   * Sendet eine private Nachricht des Users an einen anderen User.
   * sendet ggf. sendMsgToUserError, loginErrorCommand()
   */
  public void sendMsgToUser(String userName, String msg) {

    if (this.user != null) {
      if (this.user.getCurrentChannel() != null) {
        Enumeration enum = this.user.getCurrentChannel().getCurrentUserEnum();
        User tmpUser;

        while (enum.hasMoreElements()) {
          tmpUser = (User) enum.nextElement();

          if (tmpUser.getName().compareTo(userName) == 0) {
            tmpUser.getClientServant().sendMsgFromUser(this.user.getName(),
                                                       msg);
          }
        }
      } else {
        this.sendCommand(new SendMsgToUserErrorCommand());
      }
    } else {
      this.sendCommand(new LoginErrorCommand());
    }
  }

  /**
   * Sendet die Daten des betretenen Channel an den Client.
   *  sendet CurrentChannelDataCommand
   * benutzt getCurrentChannel()
   * channel.getName() und channel.getCurrentUserNames()
   */
  public void sendCurrentChannelData() {

    this.sendCommand(new Util.Commands
      .CurrentChannelDataCommand(this.user.getCurrentChannel().getName(), this
        .user.getCurrentChannel().getCurrentUserNames()));
  }

  /**
   * Sendet die Daten des Users an den Client.
   * sendet UserDataCommand
   * benutzt user.getName() und user.getAllowedChannelNames()
   */
  public void sendUserData() {
    this.sendCommand(new Util.Commands.UserDataCommand(this.user.getName(),
            this.user.getAllowedChannelNames()));
  }

  /**
   * @link aggregationByValue
   * @clientCardinality 1
   * @supplierCardinality 1
   */
  protected Util.Uplink uplink;

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
  protected Server server;

  /**
   * setServer(null) bewirkt
   * stopClientServant()
   */
  public void setServer(Server paramServer) {

    if (this.server != paramServer) {
      if (this.server != null) {
        Server oldServer = this.server;

        this.server = null;

        oldServer.removeFromClientServantList(this);
      }

      this.server = paramServer;

      if (paramServer != null) {
        paramServer.addToClientServantList(this);
      } else {
        this.stopClientServant();
      }
    }
  }
}
