package Server;

import java.net.Socket;
import Util.*;
import Util.Commands.*;
import java.util.StringTokenizer;
import java.util.Enumeration;

/**
 * Diese Klasse kümmert sich um die Anfragen, die von einem Client an den Server gestellt werden. Der Server
 * erzeugt für jeden Client eine Instanz dieser Klasse, die sich von da an nur
 * noch um diesen Client kümmert und seine Anfragen bearbeitet. In dieser Klasse steckt die meiste Funktionalität des Servers.
 */
public class ClientServant implements Util.DownlinkOwner {
    /** Standard-Konstruktor, notwendig, da der AdminClientServant von dieser Klasse erbt */
    public ClientServant() { }

    /** Konstruktor, der die entsprechenden Attribute setzt. Benutzt setServer(paramServer) */
    public ClientServant(Socket paramSocket, Server paramServer, UserAdministration paramUserAdministration) {
        this.socket = paramSocket;
        this.setServer(paramServer);
        this.userAdministration = paramUserAdministration;
    }

    private long aliveStamp=java.lang.System.currentTimeMillis();

    public synchronized void setAliveStamp(){
     this.aliveStamp=java.lang.System.currentTimeMillis();
     System.out.println("timestamp set to :"+aliveStamp);
    }

    public long getAliveStamp(){
      return this.aliveStamp;

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
            }
            else {
                this.stopClientServant();
            }
        }
    }

    /**
     * siehe auch interface DownlinkOwner: wird vom Downlink aufgerufen, falls beim Empfang von Nachrichten ein
     * Fehler auftritt, enthält Fehlerbehandlung FIXME: Sinnvolle Möglichkeiten außer stopClientServant() ?
     */
    public void downlinkError() {
        System.out.println("downlink error");
        this.stopClientServant();
    }

    /**
     * Startet den ClientServant, danach ist er betriebsbereit und kann die Anfragen seines Clients bearbeiten.
     * Erzeugt einen neuen Uplink und Downlink. Benutzt setDownlink(), uplink.startUplink() und downlink.startDownlink()
     * fängt Fehler aus startUplink() und startDownlink() ab FIXME: stopClientServant() ?
     */
    public void startClientServant() {
        this.uplink = new Uplink(this.socket);
        this.setDownlink(
            new Downlink(this.socket, this));
        try {
            this.uplink.startUplink();
        }
        catch (java.io.IOException e) {
            System.out.println("error starting ClientServant:"+e);
            this.stopClientServant();
        }
        this.downlink.startDownlink();
        System.out.println("ClientServant started");
    }

    /**
     * Stoppt den ClientServant. benutzt setDownlink(null) uplink.stopUplink(), setServer(null) und setUser(null)
     * sendet ein stopClientCommand() an den Client
     */
    protected synchronized void stopClientServant() {
        this.setDownlink(null);
        if (this.uplink != null) {
            this.uplink.stopUplink();
            this.uplink=null;
        }

        this.setUser(null);
        this.setServer(null);
        System.out.println("ClientServant stopped");
    }

    /** Führt den empfangenen Befehl einfach mittels msg.execute(this) aus. */
    public synchronized void processMsg(Command msg) {
        msg.execute(this);
    }

    /** Sendet das angegebene CommandObjekt über den Uplink. bei einem Fehler: Fehlerbehandlung FIXME: stopClientServant() ? */
    public void sendCommand(Command paramCommand) {
        try {
            this.uplink.sendMsg(paramCommand);
        }
        catch (java.io.IOException e) {
            System.out.println("error while sending:"+e);
            this.stopClientServant();
        }
    }

    /**
     * Meldet den Benutzer mit Namen name und mit Passwort password beim System an
     * Benutzt userAdministration.loginUser() um das Userobjekt zu holen und setUser() um es zu setzen.
     * Bei einem Loginfehler wird ein LoginErrorCommand gesendet und setUser(null) aufgerufen -> clientServant beendet sich
     * Falls der User Admin-Rechte hat und UserAdministration.getAdminLock()=false ist,
     * so wird becomeAdminClient() aufgerufen.
     */
    public void loginUser(String name, String password) {
        User tmpUser = this.userAdministration.loginUser(name, password);
        if (tmpUser == null) {
            this.sendCommand(
                new Util.Commands.LoginErrorCommand());
            this.setUser(null);
        }
        else {
            this.setUser(tmpUser);
            if (this.user.isAdmin()) {
                System.out.println("becoming AdminClientServant");
                this.becomeAdminClientServant();
            }
        }
    }

    /**
     * Meldet einen Gast beim System an. Benutzt userAdministration.loginGuest() und setUser()
     * Bei einem Loginfehler wird ein LoginErrorCommand gesendet und setUser(null) aufgerufen -> clientServant beendet sich
     */
    public void loginAsGuest(String name) {
        User tmpUser = this.userAdministration.loginGuest(name);
        if (tmpUser == null) {
            System.out.println("login guest " + name + " failed");
            this.sendCommand(
                new Util.Commands.LoginErrorCommand());
            this.setUser(null);
        }
        else {
            this.setUser(tmpUser);
            System.out.println("Guest " + this.user.getName() + " logged in");
        }
    }

    /**
     * Wenn der angemeldete Benutzer Administratorrechte hat, dann wird aus dem ClientServant automatisch ein AdminClientServant
     * mit erweiterter Funktionalität. Benutzt den AdminClientServant - Konstruktor
     * Setzt uplink=null und ruft dann this.stopClientServant() auf
     */
    public void becomeAdminClientServant() {
        System.out.println("becoming AdminClientServant");
        AdminClientServant tmpAdminClientServant =
            new AdminClientServant(this.uplink, this.downlink, this.server, this.server.getChannelAdministration(),
            this.userAdministration, this.user);
            this.uplink = null;
        this.stopClientServant();
    }

    /**
     * Lässt den User in den Channel mit dem angegebenen Namen eintreten.
     * Benutzt user.setCurrentChannel() und user.getFromAllowedChannelByName()
     * Falls der gewünschte Channel nicht existiert wird ein JoinChannelErrorCommand gesendet
     * falls user nicht existiert, wird ein LoginErrorCommand gesendet
     */
    public void joinChannel(String name) {
        if (this.user != null) {
            Channel tmpChannel = this.user.getFromAllowedChannelByName(name);
            if (tmpChannel != null) {
                this.user.setCurrentChannel(tmpChannel);
            }
            else {
                this.sendCommand(
                    new JoinChannelErrorCommand());
            }
        }
        else {
            this.sendCommand(
                new LoginErrorCommand());
        }
    }

    /**
     * Lässt den User den Channel verlassen. benutzt user.setCurrentChannel(null)
     * falls user nicht existent, sendet LoginErrorCommand()
     */
    public void leaveChannel() {
        if (this.user != null) {
            this.user.setCurrentChannel(null);
        }
        else {
            this.sendCommand(
                new LoginErrorCommand());
        }
    }

    /**
     * Sendet eine Nachricht des Users an alle anderen User im Channel. sendet ggf. joinChannelError(),loginErrorCommand()
     * Benutzt user.getCurrentChannel() und Channel.getCurrentUserEnum(), getClientServant()
     * um bei den verantwortlichen ClientServants ein sendMsgFromChannel() aufzurufen.
     */
    public void sendMsgToChannel(String msg) {
        if (this.user != null) {
            if (this.user.getCurrentChannel() != null) {
                Enumeration enum = this.user.getCurrentChannel().getCurrentUserEnum();
                User tmpUser;
                while (enum.hasMoreElements()) {
                    tmpUser = (User)enum.nextElement();
                    tmpUser.getClientServant().sendMsgFromChannel(tmpUser.getName(), msg);
                }
            }
            else {
                this.sendCommand(
                    new JoinChannelErrorCommand());
            }
        }
        else {
            this.sendCommand(
                new LoginErrorCommand());
        }
    }

    /**
     * Sendet eine Nachricht, die in den besuchten Channel gesendet wurde, an den Client. sendet SendMsgFromChannelCommand()
     * @param fromName Name des Absenders
     * @param msg Nachricht
     */
    public void sendMsgFromChannel(String fromName, String msg) {
        this.sendCommand(
            new Util.Commands.SendMsgFromChannelCommand(fromName, msg));
    }

    /**
     * Sendet eine private Nachricht eines anderen Users an den Client. sendet SendMsgFromUserCommand()
     * @param fromName Name des Absenders
     * @param msg Nachricht
     */
    public void sendMsgFromUser(String fromName, String msg) {
        this.sendCommand(
            new Util.Commands.SendMsgFromUserCommand(fromName, msg));
    }

    /**
     * Sendet eine private Nachricht des Users an einen anderen User. sendet ggf. joinChannelErrorCommand, loginErrorCommand()
     * Benutzt user.getCurrentChannel() und Channel.getCurrentUserEnum(), getClientServant()
     * um bei den verantwortlichen ClientServants ein sendMsgFromChannel() aufzurufen.
     */
    public void sendMsgToUser(String userName, String msg) {
        if (this.user != null) {
            if (this.user.getCurrentChannel() != null) {
                Enumeration enum = this.user.getCurrentChannel().getCurrentUserEnum();
                User tmpUser;
                while (enum.hasMoreElements()) {
                    tmpUser = (User)enum.nextElement();
                    if (tmpUser.getName().compareTo(userName) == 0) {
                        tmpUser.getClientServant().sendMsgFromUser(this.user.getName(), msg);
                    }
                }
            }
            else {
                this.sendCommand(
                    new JoinChannelErrorCommand());
            }
        }
        else {
            this.sendCommand(
                new LoginErrorCommand());
        }
    }

    /**
     * Sendet die Daten des betretenen Channel an den Client. Sendet SetCurrentChannelDataCommand
     * benutzt getCurrentChannel().getName() und getCurrentChannel().getCurrentUserNames()
     */
    public void sendCurrentChannelData() {
        this.sendCommand(
            new Util.Commands.SetCurrentChannelDataCommand(this.user.getCurrentChannel().getName(),
            this.user.getCurrentChannel().getCurrentUserNames()));
    }

    /**
     * Sendet die Daten des Users an den Client. Sendet SetCurrentUserDataCommand
     * Benutzt user.getName() und user.getAllowedChannelNames()
     */
    public void sendCurrentUserData() {
        this.sendCommand(
            new Util.Commands.SetCurrentUserDataCommand(this.user.getName(), this.user.getAllowedChannelNames()));
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
     * setServer setzt server und benachrichtigt das betroffene Serverobjekt mittels removeFromClientServantList und
     * addToClientServantList setServer(null) bewirkt stopClientServant()
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
            }
            else {
                this.stopClientServant();
            }
        }
    }
}
