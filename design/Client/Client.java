package Client;

import java.util.Vector;
import java.net.Socket;
import Util.*;
import Util.Commands.*;
import Util.Debug.Debug;

/** Die Clientapplikation */
public class Client implements Util.DownlinkOwner {


    /** Betritt den angegebenen Channel. Das entpechende Command
      * mit dem Channelnamen wid an den ClientSevant gesendet und
      * dort mit den entsprechenden Methoden verarbeitet.
      */
    public void joinChannel(String name) {
        this.sendCommand(
            new JoinChannelCommand(name));
    }

    /** Wird aufgerufen, falls beim Betreten eines Channels ein Fehler
      * auftritt und dieser an den Client gegeben wird. Der Benutzer
      * befindet sich nicht in dem Channel und kann durch die GUI
      * nicht interagieren.
      */
    public void joinChannelError(){
    }

    /** Dient dazu die Verbindung des Clients zum Serve zu trennen. Dazu
      * werden die Referenzen auf das Uplink-, bzw. Downlinkobjekt ge-
      * trennt und eine entsprechende Meldung wird auf dem GUI ausgege-
      * ben.
      */
    public void stopClient(){
    }

    /** Wird gebraucht, um die Channels, die der Benutzer betreten darf
      * auf der GUI auszugeben. Dazu werden die entsprechenden Daten
      * via Downlink vom Server geholt und auf dem GUI ausgegeben.
      */
    public void setUserData(String userName,Vector channelNames){
    }

    /** Wird ben�tigt, um die Teilnehmer in dem Channel auf dem GUI
      * anzuzeigen. Die Methode bekommt den Channelnamen und die Liste
      * der Benutzer vom ClientServant durch den Downlink �bergeben
      * und gibt die Liste der Benutzer auf dem GUI aus.
      */
    public void setChannelData(String paramName,Vector userNames){
    }


    /** Sorgt daf�r, da� im Hauptfenster die Meldung erscheint, da� ein
      * User einen Channel betreten hat. Dazu wird der Name des neuen
      * Benutzers an die Methode �bergeben, die dann die Nachricht
      * ausgibt.
      */
    public void newUserInChannel(String paramName) {
        Debug.println("Client: " + paramName + " joined channel");
    }

    /** Erzeugt en Objekt Downlink f�r den zust�ndigen Client. �ber den
      * Downlink ist der Client in der Lage, Nachrichten vom Client-
      * Servant zu empfangen.
      */
    public void setDownlink(Util.Downlink paramDownlink) { };

    /** Wird aufgerufen, falls es Probleme mit dem Downlink gibt, bzw.
      * kein Objekt dieser Klasse erzeugt werden kann.
      */
    public void downlinkError() { };



    /** Wird aufgerufen, wenn vom ClientServant beim Einloggen, die
      * Nachricht kommt, das das Einloggen nicht m�glich ist. Die
      * Methode sorgt daf�r, das der Benutzer eine Fehlermeldung
      * bekommt und zum Loginscreen zur�ckgelangt.
      */
    public void loginError() {
        Debug.println("Client: login failed: ");
    }

    /** Wird aufgerufen, wenn vom ClientServant die Meldung kommt, das
      * eine Nachricht an alle User im Channel nicht versendet werden
      * konnte. Es wird eine entsprechende Fehlermeldung ausgegeben.
      */
    public void sendMsgToChannelError(){
    }

    /** Wird aufgerufen, wenn vom ClientServant die Meldung kommt, das
      * eine Nachricht an einen anderen User im Channel nicht versendet
      * werden konnte. Es wird eine entsprechende Fehlermeldung ausgegeben.
      */
   public void sendMsgToUserError(){
    }

   /** Dient dazu, eine Nachricht �ber das f�r den Client zust�ndige
   	 * Uplinkobjekt an den ClientServant zu verschicken. Dabei wird
     * die Nachricht direkt an den Uplink �bergeben. �ber diese
     * Methode l�uft alles, was der Client an den ClientServant
     * sendet.
     */
    protected void sendCommand(Command paramCommand) {
        try {
            this.uplink.sendMsg(paramCommand);
        }
        catch (java.io.IOException e) {
            Debug.println(Debug.HIGH, e);
        }
    }

    /** Wird aufgerufen, wenn ein Channel verlassen werden soll. Der
      * Benutzer wird zur Auswahl eines neuen Channels aufgeordert.
      */
    public void leaveChannel() { }

    /** meldet den Benutzer an. Name und Passwort werden per Methode
      * sendCommand an den ClientServant gesendet.
      */
    public void login(String name, String password) {
        this.sendCommand(
            new LoginCommand(name, password));
    }

    /** meldet einen Gast an. Die Methode funktioniert wie die normale
      * Methode login, jedoch wird kein Passwort erwartet.
      */
    public void loginAsGuest(String name) {
        Debug.println("Client: loginGuest " + name);
        this.sendCommand(
            new LoginGuestCommand(name));
    }

    /** meldet den Benutzer ab. Die Methode sorgt daf�r, das der
      * Benutzer zum Loginscreen zu�ckgelangt.
      */
    public void logout() { }

    /**
     * setzt availableChannels mit den Daten aus dem String channelSet.
     * Der Client erh�lt die f�r den Benutzer zug�nglichen Channels.
     */
    public void setAvailableChannelList(String channelSet) { }

    /**
     * setzt currentUsers mit den Daten aus dem String userSet.
     * Der Client erh�lt die Liste der aktuellen Benutzer in dem betretenen Channel
     */
    public void setCurrentUserInChannelList(String userSet) { }

    /** sendet eine Nachricht an einen einzigen Benutzer. Die Nachricht, sowie
      * der Name des Empf�ngrs (name) wird an die Methode �bergeben, die sie
      * dann via Uplink an den ClientServant sendet.
      */
    public void sendMsgToUser(String name, String msg) { }


    /** Sendet eine Nachricht in einen Channel, die von allen Teilnehmern
      * in diesem Channel empfangen werden kann. Die Nachricht wird an die
      * Methode �bergeben und durch anlegen eines neuen Objekts SendMsgToChannel
      * an den ClientServant gesendet.
      */
    public void sendMsgToChannel(String msg) {
        this.sendCommand(
            new SendMsgToChannelCommand(msg));
    }

    /**
     * Verarbeitet eine empfangene Nachricht bzw. f�hrt den empfangenen Befehl einfach aus.
     * Nachrichten vom Server, die durch den Downlink empfangen werden, werden hier als Parameter eingesetzt.
     */
    public void processMsg(Command msg) {
    /* Muss wg. Command �berarbeitet werden.
    this.setAvailableChannelList(
      "Mensachat, Virtuelle Konferenz, tubs intern");
    this.channelMsgBuffer=    this.channelMsgBuffer.concat(msg+"\n");
    */

        msg.execute(this);
    }

    /** Dient dazu, einen Client am Server anzumelden. Dazu wird f�r
      * den neuen Client ein Socket am Server ge�ffnet, sowie ein
      * Uplink Objekt und ein Downlink Objekt zur Kommunikation mit
      * dem ClientServant.
      */
    public void startClient() {
        Debug.println("Client: starting client");
        try {
            socket = new Socket(Client.SERVER_IP, Client.SERVER_PORT);
            uplink = new Util.Uplink(socket);
            downlink = new Util.Downlink(socket, this);
            uplink.startUplink();
            downlink.startDownlink();
            downlink.start();
        } catch (java.io.IOException e) {
            Debug.println(Debug.HIGH, e);
        }
    }
    /** Dient dazu, eine Nachricht von einem einzigen Benutzer (fromName),
      * die f�r den gesamten Channel bestimmt ist, zu empfangen. Die Nachricht
      * wird vom Downlink an diese Methode �bergeben und sie sorgt daf�r, da�
      * der empfangene Text in der GUI erscheint.
      */
    public void sendMsgFromChannel(String fromName, String msg) {
        Debug.println("Client: " + fromName + " sayz:" + msg);
    }

    /** Dient dazu, eine Nachricht von einem einzigen Benutzer (fromName)
      * zu empfangen. Die Nachricht wird vom Downlink an diese Methode
      * �bergeben und sie sorgt daf�r, da� der empfangene Text in der
      * GUI erscheint.
      */
   public void sendMsgFromUser(String fromName,String msg){
    }

    /**
     * @directed
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    protected Util.Uplink uplink;

    /**
     * @directed
     * @supplierCardinality 1
     * @clientCardinality 1
     */
    protected Util.Downlink downlink;

    /** der Port des Servers */
    protected final static int SERVER_PORT = 1500;

    /** die IP - Adresse des Servers */
    protected final static String SERVER_IP = "localhost";

    /** Vector von Strings, repr�sentiert die f�r den Benutzer freigegebenen
      * Die Methode ist n�tig, damit der Server erkennt, f�r welche Channels
      * der Benutzer Zugriff hat.
     */
    protected Vector availableChannelList;

    /**
     * Speichert ankommende Nachrichten in einem Channel. Wird vom GUI benutzt und kann als
     * Protokoll der Unterhaltung in dem Channel dienen.
     */
    public String channelMsgBuffer = new String();
    protected Socket socket;

    /** Vector von Strings, repr�sentiert die aktuellen Benutzer in einem
      * Wird im Client Window eingebunden.
      */
    protected Vector currentUserInChannelList;
}
