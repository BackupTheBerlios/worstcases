package Client;

import java.util.Vector;
import java.net.Socket;
import Util.*;
import Util.Commands.*;
import Util.Debug.Debug;

/** 
 * Die Klasse Client stellt dem Benutzer alle nötigen Methoden zur 
 * Verfügung, um an einer Virtuellen Konferenz, also einem Chat teilnehmen 
 * zu können. Der Benutzer bedient den Client nicht direkt, sondern über 
 * ein GUI. Der Client
 * leitet alle Nachrichten und Aktionen des Benutzers an seinen
 * ClientServant weiter, und verarbeitet dessen Antworten.
 */
public class Client implements Util.DownlinkOwner {
    /**
     * Betritt den angegebenen Channel. Das entpechende Command mit dem Channelnamen wid an den ClientSevant gesendet und
     * dort mit den entsprechenden Methoden verarbeitet.
     * Benutzt JoinChannelCommand()
     */
    public synchronized final void joinChannel(String name) {
        this.sendCommand(
            new JoinChannelCommand(name));
    }

    /**
     * Wird aufgerufen, falls beim Betreten eines Channels ein Fehler
     * auftritt und dieser an den Client gegeben wird. Der Benutzer
     * befindet sich nicht in dem Channel und kann durch die GUI nicht interagieren.
     * Der Client und seine GUI werden in den Zustand zurückversetzt, der nach dem Login
     * des Users bestand.
     */
    public synchronized final  void joinChannelError() {
    }


    /** Trennt die Verbindung des Clients zum Server. Dazu
      * werden die Referenzen auf das Uplink- bzw. Downlinkobjekt
      * entfernt und eine entsprechende Meldung auf dem GUI ausgegeben.
      */
    public  synchronized final void stopClient() {
      this.setDownlink(null);
      if(uplink!=null){
       this.uplink.stopUplink();
       this.uplink=null;

      }

    }

    /**Der Name des Benutzers des Clients.*/
    String currentUserName;

    /**Namensliste der Channels, die der Benutzer betreten darf.*/
    Vector currentAllowedChannelNames;

    /**Der Name des Channels, der momentan betreten wurde.*/
    String currentChannelName;


    /**
     * Wird gebraucht, um die Channels, die der Benutzer betreten darf
     * auf der GUI auszugeben. Dazu werden die entsprechenden Daten via Downlink vom Server geholt und auf dem GUI ausgegeben.
     * Setzt currentUserName und currentAllowedChannelNames.
     */
    public  synchronized final void setCurrentUserData(String userName, Vector channelNames) {
      this.currentUserName=userName;
      this.currentAllowedChannelNames=channelNames;
    }

    /**
     * Wird benötigt, um die Teilnehmer in dem Channel auf dem GUI
     * anzuzeigen. Die Methode bekommt den Channelnamen und die Liste
     * der Benutzer vom ClientServant durch den Downlink übergeben und gibt die Liste der Benutzer auf dem GUI aus.
     * Setzt currentChannelName und currentUsersInChannelList
     */
    public synchronized final  void setCurrentChannelData(String paramName, Vector userNames) {
      this.currentChannelName=paramName;
      this.currentUsersInChannelList=userNames;

    }

    /**
     * Über den
     * Downlink ist der Client in der Lage, Nachrichten vom Client- Servant zu empfangen.
     * Setzt downlink, benachrichtigt das Downlinkobjekt mittels setDownlinkOwner()
     */
    public synchronized final void setDownlink(Util.Downlink paramDownlink) { };

    /** Wird aufgerufen, falls es Probleme mit dem Downlink gibt,
      *  bzw. kein Objekt dieser Klasse erzeugt werden kann.
      * Ruft stopClient() auf.
      */
    public synchronized final  void downlinkError() {
     stopClient();
    };

    /**
     * Wird aufgerufen, wenn vom ClientServant beim Einloggen, die Nachricht kommt, das das Einloggen nicht möglich ist. Die
     * Methode sorgt dafür, das der Benutzer eine Fehlermeldung bekommt und zum Loginscreen zurückgelangt.
     * Ruft stopClient() auf
     */
    public  synchronized final void loginError() {
        Debug.println("Client: login failed: ");
        stopClient();
    }



    /**
     * Dient dazu, eine Nachricht über das für den Client zuständige
     * Uplinkobjekt an den ClientServant zu verschicken. Dabei wird die Nachricht direkt an den Uplink übergeben. Über diese
     * Methode läuft alles, was der Client an den ClientServant sendet.
     * Benutzt uplink.sendMsg(). Bei einem Fehler wird stopClient() aufgerufen.
     */
    protected  synchronized final void sendCommand(Command paramCommand) {
        try {
            this.uplink.sendMsg(paramCommand);
        }
        catch (java.io.IOException e) {
            Debug.println(Debug.HIGH, e);
            this.stopClient();

        }

    }

    /**
     * Wird aufgerufen, wenn ein Channel verlassen werden soll. Der
     * Benutzer wird zur Auswahl eines neuen Channels aufgeordert.
     * Sendet ein LeaveChannelCommand().
     */
    public synchronized final  void leaveChannel() {
      this.sendCommand(new LeaveChannelCommand());

        }

    /** Meldet den Benutzer an. Name und Passwort werden per Methode sendCommand an den ClientServant gesendet.
      * Sendet LoginCommand().
      */
    public synchronized final void login(String name, String password) {
        this.sendCommand(
            new LoginCommand(name, password));
    }

    /** Meldet einen Gast an. Die Methode funktioniert wie die normale Methode login, jedoch wird kein Passwort erwartet.
      * Sendet LoginGuestCommand().
      */
    public  synchronized final void loginAsGuest(String name) {
        Debug.println("Client: loginGuest " + name);
        this.sendCommand(
            new LoginGuestCommand(name));
    }

    /** Meldet den Benutzer ab. Die Methode sorgt dafür, das der Benutzer zum Loginscreen zuückgelangt.
      * Sendet ein LogoutCommand()
      */
    public synchronized final void logout() {
      this.sendCommand(new LogoutCommand());

    }


    /**
     * Sendet eine Nachricht an einen einzigen Benutzer. Die Nachricht, sowie
     * der Name des Empfängrs (name) wird an die Methode übergeben, die sie dann via Uplink an den ClientServant sendet.
     * Sendet sendMsgToUserCommand.
     */
    public synchronized final  void sendMsgToUser(String name, String msg) {
     this.sendCommand(new SendMsgToUserCommand(name,msg));

    }

    /**
     * Sendet eine Nachricht in einen Channel, die von allen Teilnehmern
     * in diesem Channel empfangen werden kann. Die Nachricht wird an die
     * Methode übergeben und durch anlegen eines neuen Objekts SendMsgToChannel an den ClientServant gesendet.
     */
    public synchronized final void sendMsgToChannel(String msg) {
        this.sendCommand(
            new SendMsgToChannelCommand(msg));
    }

    /**
     * Verarbeitet eine empfangene Nachricht bzw. führt den empfangenen Befehl einfach aus.
     * Nachrichten vom Server, die durch den Downlink empfangen werden, werden hier als Parameter eingesetzt.
     */
    public synchronized final  void processMsg(Command msg) {
        msg.execute(this);
    }

    /**
     * Dient dazu, einen Client am Server anzumelden. Dazu wird für den neuen Client ein Socket am Server geöffnet, sowie ein
     * Uplink Objekt und ein Downlink Objekt zur Kommunikation mit dem ClientServant.
     * Benutzt setDownlink, downlink.startDownlink() und uplink.startUplink().
     */
    public synchronized final  void startClient() {
        Debug.println("Client: starting client");
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            uplink = new Util.Uplink(socket);
            setDownlink (new Util.Downlink(socket, this));
            uplink.startUplink();
            downlink.startDownlink();
        } catch (java.io.IOException e) {
            Debug.println(Debug.HIGH, e);
        }
    }

    /**
     * Dient dazu, eine Nachricht von einem einzigen Benutzer (fromName),
     * die für den gesamten Channel bestimmt ist, zu empfangen. Die Nachricht
     * wird vom Downlink an diese Methode übergeben und sie sorgt dafür, daß der empfangene Text in der GUI erscheint
     * und zum channelMsgBuffer hinzugefügt wird.
     */
    public  synchronized final void sendMsgFromChannel(String fromName, String msg) {
        Debug.println("Client: " + fromName + " sayz:" + msg);
    }

    /**
     * Dient dazu, eine Nachricht von einem einzigen Benutzer (fromName)
     * zu empfangen. Die Nachricht wird vom Downlink an diese Methode
     * übergeben und sie sorgt dafür, daß der empfangene Text in der GUI erscheint
     * und zum channelMsgBuffer hinzugefügt wird.
     */
    public  synchronized final void sendMsgFromUser(String fromName, String msg) {
    }

    /**
     * Der Uplink, über ihn werden Nachrichten gesendet.
     * @directed
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    protected Util.Uplink uplink;

    /**
     * Der Downlink, über ihn werden Nachrichten empfangen.
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
     * Speichert ankommende Nachrichten in einem Channel. Wird vom GUI benutzt und kann als
     * Protokoll der Unterhaltung in dem Channel dienen.
     */
    public String channelMsgBuffer = new String();

    /**
     * Der Socket, über den die Verbindung zum Server aufgenommen wird.
     */
    protected Socket socket;

    /** Vector von Strings, repräsentiert die aktuellen Benutzer in einem Wird im Client Window eingebunden. */
    protected Vector currentUsersInChannelList;
}
