package Server;

import java.net.Socket;
import Util.*;
import Util.Commands.*;
import java.util.StringTokenizer;
import java.util.Enumeration;

/**
 * Diese Klasse k�mmert sich um die Anfragen, die von einem Client an den Server gestellt werden. Der Server
 * erzeugt f�r jeden Client eine Instanz dieser Klasse, die sich von da an nur
 * noch um diesen Client k�mmert und seine Anfragen bearbeitet. In dieser Klasse steckt die meiste Funktionalit�t des Servers.
 */
public class ClientServant implements Util.DownlinkOwner {
    /** Konstruktor, der die entsprechenden Attribute setzt. */
    public ClientServant(Socket paramSocket, Server paramServer, UserAdministration paramUserAdministration) {
        this.socket = paramSocket;
        this.server = paramServer;
        this.userAdministration = paramUserAdministration;
    }

    public void sendNewUserEnteredChannel(String paramName){
      this.sendCommand(new NewUserInChannelCommand(paramName));
    }

    public ClientServant() { }
    public void stopOwner(){
     this.stopClientServant();
    }

    /** Startet den ClientObserver, danach ist er betriebsbereit und kann die Anfragen seines Clients bearbeiten. */
    public void startClientServant() {
        this.uplink = new Util.Uplink(this.socket);
        this.downlink = new Util.Downlink(this.socket, this);
        try {
            this.uplink.startUplink();
            this.downlink.startDownlink();
        }
        catch (java.io.IOException e) {
            System.out.println(e);
        }
        this.downlink.start();
        System.out.println("ClientServant started");
    }

    /** Stoppt den ClientServant. */
    public synchronized void stopClientServant() {
        if (this.user != null) {
            if (this.user.getCurrentChannel() != null) {
                this.leaveChannel();
            }
            if (this.user.isLoggedIn()) {
                this.user.setLoggedIn(false);
            }
            this.user.setClientServant(null);
        }
        if (this.downlink != null) {
            this.downlink.stopDownlink();
            this.downlink=null;
        }
        if (this.uplink != null) {
            this.uplink.stopUplink();
            this.uplink=null;
        }
	if (this.server !=null){
        this.server.removeFromClientServantList(this);
        this.server=null;
	}
	        System.out.println("ClientServant stopped");

    }

    /** Verarbeitet eine empfangene Nachricht, bzw. f�hrt den empfangenen Befehl einfach aus. */
    public synchronized void processMsg(Command msg) {
        msg.execute(this);
    }

    public void sendCommand(Command paramCommand) {
        try {
            this.uplink.sendMsg(paramCommand);
        }
        catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    /** Meldet den Benutzer beim System an, benutzt daf�r eine vom Client empfangene Zeichenkette mit Benutzerinformationen. */
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
            System.out.println(this.user.getAllowedChannelList());
            this.user.setClientServant(this);
            if (this.user.isAdmin()) {
                this.becomeAdminClientServant();
		System.out.println("becoming AdminClientServant");
            }
        }
    }

    /** Meldet einen Gast beim System an, benutzt daf�r eine vom Client empfangene Zeichenkette mit Benutzerinformationen. */
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
            System.out.println(this.user.getAllowedChannelList());
        }
    }

    /**
     * Wenn der angemeldete Benutzer Administratorrechte hat, dann wird aus dem ClientObserver automatisch ein AdminClientObserver
     * mit erweiterter Funktionalit�t.
     */
    public void becomeAdminClientServant() {
        AdminClientServant tmpAdminClientServant =
            new AdminClientServant(this.socket, this.server, this.server.getChannelAdministration(),
            this.userAdministration, this.user);
            this.user.setClientServant(tmpAdminClientServant);
        this.downlink.setDownlinkOwner(tmpAdminClientServant);
        this.server.addToClientServantList(tmpAdminClientServant);
        this.server.removeFromClientServantList(this);
    }

    /** Meldet den Benutzer vom System ab und stoppt den Observer. */
    public void logoutUser() {
        this.user.setLoggedIn(false);
        this.stopClientServant();
    }

    /** L�sst den User in den Channel mit dem angegebenen Namen eintreten. */
    public void joinChannel(String name) {
        Channel tmpChannel = this.user.getFromAllowedChannelByName(name);
        this.user.setCurrentChannel(tmpChannel);
    }

    /** L�sst den User den Channel verlassen. */
    public void leaveChannel() {
        this.user.setCurrentChannel(null);
    }

    /** Sendet eine Nachricht des Users an alle anderen User im Channel. */
    public void sendMsgToChannel(String msg) {
        Enumeration enum = this.user.getCurrentChannel().getCurrentUserList().elements();
        while (enum.hasMoreElements()) {
            User tmpUser = (User)(enum.nextElement());
            ClientServant tmpClientServant = tmpUser.getClientServant();
            tmpClientServant.sendMsgFromChannel(this.user.getName() + " sayz: " + msg);
        }
    }

    /** Sendet eine Nachricht, die in den besuchten Channel gesendet wurde, an den Client. */
    public void sendMsgFromChannel(String msg) {
    }

    /** Sendet eine private Nachricht eines anderen Users an den Client. */
    public void sendMsgFromUser(String msg) {
    }

    /** Sendet eine private Nachricht des Users an einen anderen User. */
    public void sendMsgToUser(String userName, String msg) {
        int pos = 0;
        Channel tmpChannel = this.user.getCurrentChannel();
        User tmpUser = (User)(tmpChannel.getCurrentUserList().elementAt(pos));
        ClientServant tmpClientServant = tmpUser.getClientServant();
    }

    /** Sendet die Daten des betretenen Channel an den Client. */
    public void sendChannelData() {
        Channel tmpChannel = this.user.getCurrentChannel();
    }

    /** Sendet die Daten des Users an den Client. */
    public void sendUserData() {
        String tmpUserSet = this.user.toString();
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
    private Server server;
}
