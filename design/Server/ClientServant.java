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
    /** Konstruktor, der die entsprechenden Attribute setzt. */
    public ClientServant(Socket paramSocket, Server paramServer, UserAdministration paramUserAdministration) {
        this.socket = paramSocket;
        this.setServer(paramServer);
        this.userAdministration = paramUserAdministration;
    }

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

    public ClientServant() { }

    public void downlinkError() {
        this.stopClientServant();
    }

    /** Startet den ClientObserver, danach ist er betriebsbereit und kann die Anfragen seines Clients bearbeiten. */
    public void startClientServant() {
        this.uplink = new Uplink(this.socket);
        this.downlink = new Downlink(this.socket, this);
        try {
            this.uplink.startUplink();
            this.downlink.startDownlink();
        }
        catch (java.io.IOException e) {
            System.out.println(e);
        }
        System.out.println("ClientServant started");
    }

    /** Stoppt den ClientServant. */
    protected synchronized void stopClientServant() {
        this.setDownlink(null);
        if (this.uplink != null) {
            this.uplink.stopUplink();
        }
        this.setUser(null);
        System.out.println("ClientServant stopped");
    }

    /** Verarbeitet eine empfangene Nachricht, bzw. führt den empfangenen Befehl einfach aus. */
    public synchronized void processMsg(Command msg) {
        msg.execute(this);
    }

    public void sendCommand(Command paramCommand) {
        try {
            this.uplink.sendMsg(paramCommand);
        }
        catch (java.io.IOException e) {
            System.out.println(e);
            this.stopClientServant();
        }
    }

    /** Meldet den Benutzer beim System an, benutzt dafür eine vom Client empfangene Zeichenkette mit Benutzerinformationen. */
    public void loginUser(String name, String password) {
        // FIXME: an LoginCommand anpassen:
        this.user = this.userAdministration.loginUser(name, password);
        if (this.user == null) {
            System.out.println("login user " + name + " failed");
            this.sendCommand(
                new Util.Commands.LoginErrorCommand("login failed"));
        }
        else {
            System.out.println("User " + this.user.getName() + " logged in");
            this.user.setClientServant(this);
            if (this.user.isAdmin()) {
                System.out.println("becoming AdminClientServant");
                this.becomeAdminClientServant();
            }
        }
    }

    /** Meldet einen Gast beim System an, benutzt dafür eine vom Client empfangene Zeichenkette mit Benutzerinformationen. */
    public void loginAsGuest(String name) {
        this.user = this.userAdministration.loginGuest(name);
        if (this.user == null) {
            System.out.println("login guest " + name + " failed");
            this.sendCommand(
                new Util.Commands.LoginErrorCommand("login failed"));
        }
        else {
            System.out.println("Guest " + this.user.getName() + " logged in");
            this.user.setClientServant(this);
        }
    }

    /**
     * Wenn der angemeldete Benutzer Administratorrechte hat, dann wird aus dem ClientObserver automatisch ein AdminClientObserver
     * mit erweiterter Funktionalität.
     */
    public void becomeAdminClientServant() {
        AdminClientServant tmpAdminClientServant =
            new AdminClientServant(this.uplink, this.downlink, this.server, this.server.getChannelAdministration(),
            this.userAdministration, this.user);
            this.uplink = null;
        this.stopClientServant();
    }

    /** Meldet den Benutzer vom System ab und stoppt den Observer. */
    public void logoutUser() {
        this.user.setLoggedIn(false);
    }

    /** Lässt den User in den Channel mit dem angegebenen Namen eintreten. */
    public void joinChannel(String name) {
        Channel tmpChannel = this.user.getFromAllowedChannelByName(name);
        this.user.setCurrentChannel(tmpChannel);
    }

    /** Lässt den User den Channel verlassen. */
    public void leaveChannel() {
        this.user.setCurrentChannel(null);
    }

    /** Sendet eine Nachricht des Users an alle anderen User im Channel. */
    public void sendMsgToChannel(String msg) {
        if (this.user.getCurrentChannel() != null) {
            Enumeration enum = this.user.getCurrentChannel().getCurrentUserEnum();
            User tmpUser;
            while (enum.hasMoreElements()) {
                tmpUser = (User)enum.nextElement();
                tmpUser.getClientServant().sendMsgFromChannel(this.user.getName(), msg);
            }
        }
    }

    /** Sendet eine Nachricht, die in den besuchten Channel gesendet wurde, an den Client. */
    public void sendMsgFromChannel(String fromName, String msg) {
        this.sendCommand(
            new Util.Commands.SendMsgFromChannelCommand(fromName, msg));
    }

    /** Sendet eine private Nachricht eines anderen Users an den Client. */
    public void sendMsgFromUser(String fromName, String msg) {
        this.sendCommand(
            new Util.Commands.SendMsgFromUserCommand(fromName, msg));
    }

    /** Sendet eine private Nachricht des Users an einen anderen User. */
    public void sendMsgToUser(String userName, String msg) {
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
    }

    /** Sendet die Daten des betretenen Channel an den Client. */
    public void sendCurrentChannelData() {
        this.sendCommand(
            new Util.Commands.CurrentChannelDataCommand(this.user.getCurrentChannel().getName(),
            this.user.getCurrentChannel().getCurrentUserNames()));
    }

    /** Sendet die Daten des Users an den Client. */
    public void sendUserData() {
        this.sendCommand(new Util.Commands.UserDataCommand(this.user.getName(),this.user.getAllowedChannelNames()));

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
        }
    }
}
