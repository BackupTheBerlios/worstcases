package Server;

import java.net.Socket;
import Util.*;
import Util.Debug.Debug;
import Util.Commands.*;
import java.util.StringTokenizer;
import java.util.Enumeration;

/**
 * Diese Klasse kümmert sich um die Anfragen, die von einem Client an den Server gestellt werden. Der Server
 * erzeugt für jeden Client eine Instanz dieser Klasse, die sich von da an nur
 * noch um diesen Client kümmert und seine Anfragen bearbeitet. In dieser Klasse steckt die meiste Funktionalität des Servers.
 */
public class ClientServant implements Util.DownlinkOwner {
    /** Standard-Konstruktor, notwendig, da der AdminClientServant von dieser Klasse erbt. Wird sonst <em>nicht</em> benutzt. */
    public ClientServant() { }

    /** Konstruktor, der die entsprechenden Attribute setzt. Benutzt setServer(paramServer). */
    public ClientServant(Socket paramSocket, Server paramServer, UserAdministration paramUserAdministration) {
        this.socket = paramSocket;
        this.setServer(paramServer);
        this.userAdministration = paramUserAdministration;
        Debug.println(this + " initialized");
    }

    /** Gibt den aktuellen aliveStamp-Wert zurück. */
    public final long getAliveStamp() {
        return this.aliveStamp;
    }

    /** Setzt aliveStamp auf die aktuelle Zeit. Benutzt java.lang.System.currentTimeMillis(). */
    public final synchronized void setAliveStamp() {
        this.aliveStamp = java.lang.System.currentTimeMillis();
        Debug.println(this + " timestamp set to :" + aliveStamp);
    }

    /**
     * Setzt downlink, benachrichtigt die betroffenen Downlinkobjekte mittels setDownlinkOwner().
     * Falls der ClientServant NACH dem Aufruf keinen Downlink mehr besitzt, so wird stopClientServant() aufgerufen
     */
    public final synchronized void setDownlink(Downlink paramDownlink) {
        if (this.downlink != paramDownlink) {
            if (this.downlink != null) {
                Downlink old = this.downlink;
                this.downlink = null;
                old.setDownlinkOwner(null);
            }
            this.downlink = paramDownlink;
            Debug.println(this + " setDownlink to " + this.downlink);
            if (paramDownlink != null) {
                paramDownlink.setDownlinkOwner(this);
            }
            else {
                this.stopClientServant();
            }
        }
    }

    /**
     * Wird vom Downlink aufgerufen, falls beim Empfang von Nachrichten ein Fehler auftritt, enthält Fehlerbehandlung.
     * @see Util.DownlinkOwner
     */
    // FIXME: Sinnvolle Möglichkeiten außer stopClientServant() ?
    public final synchronized void downlinkError() {
        Debug.println(Debug.HIGH, this + " downlink error");
        this.stopClientServant();
    }

    /**
     * Setzt user, benutzt user.setClientServant(). Falls der ClientServant NACH dem Aufruf keinen User mehr besitzt, so wird
     * stopClientServant() aufgerufen
     */
    public final synchronized void setUser(User paramUser) {
        if (this.user != paramUser) {
            if (this.user != null) {
                User old = this.user;
                this.user = null;
                old.setClientServant(null);
            }
            this.user = paramUser;
            Debug.println(this + " setUser to " + this.user);
            if (paramUser != null) {
                paramUser.setClientServant(this);
            }
            else {
                this.stopClientServant();
            }
        }
    }

    /**
     * Startet den ClientServant, danach ist er betriebsbereit und kann die Anfragen seines Clients bearbeiten.
     * Erzeugt einen neuen Uplink und Downlink. Benutzt setDownlink(), uplink.startUplink() und downlink.startDownlink().
     * Fängt Fehler aus startUplink() und startDownlink() ab.
     */
    // FIXME: stopClientServant() ?
    public final synchronized void startClientServant() {
        this.uplink = new Uplink(this.socket);
        this.setDownlink(
            new Downlink(this.socket, this));
        try {
            this.uplink.startUplink();
        }
        catch (java.io.IOException e) {
            Debug.println(Debug.HIGH, this + " error starting uplink:" + e);
            this.stopClientServant();
        }
        this.downlink.startDownlink();
        Debug.println(this + " started");
    }

    /**
     * Stoppt den ClientServant. Benutzt setDownlink(null) uplink.stopUplink(), setServer(null) und setUser(null).
     * Sendet ein stopClientCommand() an den Client.
     */
    public final synchronized void stopClientServant() {
        this.setDownlink(null);
        this.setServer(null);
        this.setUser(null);
        Uplink old = this.uplink;
        if (old != null) {
            this.uplink = null;
            try {
                old.sendMsg(
                    new StopClientCommand());
            }
            catch (java.io.IOException e) {
                Debug.println(e);
            }
            old.stopUplink();
        }
    }

    /** Führt den empfangenen Befehl einfach mittels msg.execute(this) aus. Benutzt setAliveStamp(). */
    public final synchronized void processMsg(Command msg) {
        if (msg != null) {
            msg.execute(this);
            this.setAliveStamp();
        }
    }

    /** Sendet das angegebene CommandObjekt über den Uplink. Benutzt uplink.sendMsg() Bei einem Fehler: Fehlerbehandlung. */
    // FIXME: stopClientServant() ?
    public final synchronized void sendCommand(Command paramCommand) {
        try {
            Uplink old = this.uplink;
            if (old != null) {
                old.sendMsg(paramCommand);
            }
        }
        catch (java.io.IOException e) {
            Debug.println(this + " error while sending:" + e);
            this.stopClientServant();
        }
    }

    /**
     * Meldet den Benutzer mit Namen name und mit Passwort password beim System an
     * Benutzt userAdministration.loginUser() um das Userobjekt zu holen und setUser() um es zu setzen.
     * Bei einem Loginfehler wird ein LoginErrorCommand gesendet und setUser(null) aufgerufen, anschließend beendet sich
     * clientServant. Falls der User Admin-Rechte hat, so wird becomeAdminClient() aufgerufen.
     */
    public final synchronized void loginUser(String name, String password) {
        Debug.println(this + " trying to log in user " + name);
        UserAdministration tmpUserAdministration = this.userAdministration;
        if (tmpUserAdministration != null) {
            User tmpUser = tmpUserAdministration.loginUser(name, password);
            if (tmpUser == null) {
                this.sendCommand(
                    new Util.Commands.LoginErrorCommand());
            }
            else {
                this.setUser(tmpUser);
                this.sendCurrentUserData();
                if (tmpUser.isAdmin()) {
                    this.becomeAdminClientServant();
                }
            }
        }
    }

    /**
     * Meldet einen Gast beim System an. Benutzt userAdministration.loginGuest() und setUser()
     * Bei einem Loginfehler wird ein LoginErrorCommand gesendet und setUser(null) aufgerufen anschließend
     * beendet sich clientServant.
     */
    public final synchronized void loginAsGuest(String name) {
        Debug.println(this + " trying to login guest " + name);
        UserAdministration tmpUserAdministration = this.userAdministration;
        User tmpUser = tmpUserAdministration.loginGuest(name);
        if (tmpUser == null) {
            Debug.println("login guest " + name + " failed");
            this.sendCommand(
                new Util.Commands.LoginErrorCommand());
        }
        else {
            this.setUser(tmpUser);
            this.sendCurrentUserData();
            Debug.println("Guest " + this.user.getName() + " logged in");
        }
    }

    /**
     * Wenn der angemeldete Benutzer Admin-Rechte hat, dann wird aus dem ClientServant automatisch ein AdminClientServant
     * mit erweiterter Funktionalität. Benutzt den AdminClientServant-Konstruktor
     * Setzt uplink=null und ruft dann this.stopClientServant() auf.
     */
    public final synchronized void becomeAdminClientServant() {
        Debug.println(this + " becoming AdminClientServant");
        Uplink oldUplink = this.uplink;
        //notwendig, damit stopClientServant() nicht den uplink stoppt
        this.uplink = null;
        AdminClientServant tmpAdminClientServant =
            new AdminClientServant(oldUplink, this.downlink, this.server, this.server.getChannelAdministration(),
            this.userAdministration, this.user, this.server.getDataBaseIO());
    }

    /**
     * Lässt den User in den Channel mit dem angegebenen Namen eintreten.
     * Benutzt user.setCurrentChannel() und user.getFromAllowedChannelByName()
     * Falls der gewünschte Channel nicht existiert wird ein JoinChannelErrorCommand gesendet
     * Falls der user nicht existiert, wird ein LoginErrorCommand gesendet.
     */
    public final synchronized void joinChannel(String name) {
        Debug.println(this + " joining channel:" + name);
        User old = this.user;
        if (old != null) {
            Channel tmpChannel = old.getFromAllowedChannelByName(name);
            if (tmpChannel != null) {
                old.setCurrentChannel(tmpChannel);
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
     * Lässt den User den Channel verlassen. Benutzt user.setCurrentChannel(null)
     * Falls user nicht existent, wird LoginErrorCommand() gesendet.
     */
    public final synchronized void leaveChannel() {
        User old = this.user;
        if (old != null) {
            old.setCurrentChannel(null);
        }
        else {
            this.sendCommand(
                new LoginErrorCommand());
        }
    }

    /**
     * Sendet eine Nachricht des Users an alle anderen User im Channel. Sendet ggf. joinChannelError(),loginErrorCommand()
     * Benutzt user.getCurrentChannel() und Channel.getCurrentUserEnum(), getClientServant()
     * um bei den verantwortlichen ClientServants ein sendMsgFromChannel() aufzurufen.
     */
    public final synchronized void sendMsgToChannel(String msg) {
        User oldUser = this.user;
        if (oldUser != null) {
            Channel oldChannel = oldUser.getCurrentChannel();
            if (oldChannel != null) {
                Enumeration enum = oldChannel.getCurrentUserEnum();
                User tmpUser;
                ClientServant tmpClientServant;
                try {
                    while (enum.hasMoreElements()) {
                        tmpUser = (User)enum.nextElement();
                        tmpClientServant = tmpUser.getClientServant();
                        if (tmpClientServant != null) {
                            tmpClientServant.sendMsgFromChannel(oldUser.getName(), msg);
                        }
                    }
                }
                catch (java.util.NoSuchElementException e) {
                    Debug.println(Debug.HIGH, this + " " + " error while sendMsgToChannel " + e);
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
     * Sendet eine Nachricht, die in den besuchten Channel gesendet wurde, an den Client (mit einem
     * SendMsgFromChannelCommand).
     * @param fromName Name des Absenders
     * @param msg Nachricht
     */
    public final synchronized void sendMsgFromChannel(String fromName, String msg) {
        this.sendCommand(
            new Util.Commands.SendMsgFromChannelCommand(fromName, msg));
    }

    /**
     * Sendet eine private Nachricht eines anderen Users an den Client. Sendet ein SendMsgFromUserCommand.
     * @param fromName Name des Absenders
     * @param msg Nachricht
     */
    public final synchronized void sendMsgFromUser(String fromName, String msg) {
        this.sendCommand(
            new Util.Commands.SendMsgFromUserCommand(fromName, msg));
    }

    /**
     * Sendet eine private Nachricht des Users an einen anderen User. Sendet ggf. joinChannelErrorCommand, loginErrorCommand()
     * Benutzt user.getCurrentChannel() und Channel.getCurrentUserEnum(), getClientServant()
     * um bei den verantwortlichen ClientServants ein sendMsgFromChannel() aufzurufen.
     */
    public final synchronized void sendMsgToUser(String userName, String msg) {
        User old = this.user;
        if (old != null) {
            Channel currentChannel = old.getCurrentChannel();
            if (currentChannel != null) {
                Enumeration enum = currentChannel.getCurrentUserEnum();
                User tmpUser;
                try {
                    while (enum.hasMoreElements()) {
                        tmpUser = (User)enum.nextElement();
                        if (tmpUser.getName().compareTo(userName) == 0) {
                            ClientServant tmpClientServant = tmpUser.getClientServant();
                            if (tmpClientServant != null) {
                                tmpClientServant.sendMsgFromUser(old.getName(), msg);
                            }
                        }
                    }
                }
                catch (java.util.NoSuchElementException e) {
                    Debug.println(this + " " + " error while sendMsgToUser " + e);
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
     * Sendet die Daten des betretenen Channel an den Client. Sendet ein SetCurrentChannelDataCommand.
     * Benutzt getCurrentChannel().getName() und getCurrentChannel().getCurrentUserNames().
     */
    public final synchronized void sendCurrentChannelData() {
        User old = this.user;
        if (old != null) {
            Channel currentChannel = old.getCurrentChannel();
            if (currentChannel != null) {
                this.sendCommand(
                    new Util.Commands.SetCurrentChannelDataCommand(currentChannel.getName(),
                    currentChannel.getCurrentUserNames()));
            }
        }
    }

    /**
     * Sendet die Daten des Users an den Client. Sendet ein SetCurrentUserDataCommand.
     * Benutzt user.getName() und user.getAllowedChannelNames().
     */
    public final synchronized void sendCurrentUserData() {
        User old = this.user;
        if (old != null) {
            this.sendCommand(
                new Util.Commands.SetCurrentUserDataCommand(old.getName(), old.getAllowedChannelNames()));
        }
    }

    /**
     * Der Uplink, über ihn werden Nachrichten gesendet.
     * @link aggregationByValue
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    protected Util.Uplink uplink;

    /**
     * Der Downlink, über ihn werden Nachrichten empfangen.
     * @link aggregationByValue
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    protected Util.Downlink downlink;

    /**
     * Die Benutzerverwaltung.
     * @clientCardinality 0..
     * @supplierCardinality 1
     */
    protected UserAdministration userAdministration;

    /**
     * Der Benutzer der Clientapplikation.
     * @clientCardinality 0..1
     * @supplierCardinality 1
     */
    protected User user;

    /** Über diesen Socket kommuniziert der ClientServant mit seinem Client. */
    protected Socket socket;

    /** Gibt den letzten Zeitpunkt an, an dem der ClientServant eine Nachricht von seinem Client empfangen hat. */
    protected long aliveStamp = java.lang.System.currentTimeMillis();

    /**
     * Der Server.
     * @clientCardinality 0..
     * @supplierCardinality 1
     */
    protected Server server;

    /**
     * Setzt server und benachrichtigt das betroffene Serverobjekt mittels removeFromClientServantList() und
     * addToClientServantList. setServer(null) bewirkt stopClientServant()
     */
    public final synchronized void setServer(Server paramServer) {
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
