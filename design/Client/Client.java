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

    /** Wird benötigt, um die Teilnehmer in dem Channel auf dem GUI
      * anzuzeigen. Die Methode bekommt den Channelnamen und die Liste
      * der Benutzer vom ClientServant durch den Downlink übergeben
      * und gibt die Liste der Benutzer auf dem GUI aus.
      */
    public void setChannelData(String paramName,Vector userNames){
    }


    /** Sorgt dafür, daß im Hauptfenster die Meldung erscheint, daß ein
      * User einen Channel betreten hat. Dazu wird der Name des neuen
      * Benutzers an die Methode übergeben, die dann die Nachricht
      * ausgibt.
      */
    public void newUserInChannel(String paramName) {
        Debug.println("Client: " + paramName + " joined channel");
    }

    /** Erzeugt en Objekt Downlink für den zuständigen Client. Über den
      * Downlink ist der Client in der Lage, Nachrichten vom Client-
      * Servant zu empfangen.
      */
    public void setDownlink(Util.Downlink paramDownlink) { };

    /** Wird aufgerufen, falls es Probleme mit dem Downlink gibt, bzw.
      * kein Objekt dieser Klasse erzeugt werden kann.
      */
    public void downlinkError() { };



    /** Wird aufgerufen, wenn vom ClientServant beim Einloggen, die
      * Nachricht kommt, das das Einloggen nicht möglich ist. Die
      * Methode sorgt dafür, das der Benutzer eine Fehlermeldung
      * bekommt und zum Loginscreen zurückgelangt.
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

   /** Dient dazu, eine Nachricht über das für den Client zuständige
   	 * Uplinkobjekt an den ClientServant zu verschicken. Dabei wird
     * die Nachricht direkt an den Uplink übergeben. Über diese
     * Methode läuft alles, was der Client an den ClientServant
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

    /** meldet den Benutzer ab. Die Methode sorgt dafür, das der
      * Benutzer zum Loginscreen zuückgelangt.
      */
    public void logout() { }

    /**
     * setzt availableChannels mit den Daten aus dem String channelSet.
     * Der Client erhält die für den Benutzer zugänglichen Channels.
     */
    public void setAvailableChannelList(String channelSet) { }

    /**
     * setzt currentUsers mit den Daten aus dem String userSet.
     * Der Client erhält die Liste der aktuellen Benutzer in dem betretenen Channel
     */
    public void setCurrentUserInChannelList(String userSet) { }

    /** sendet eine Nachricht an einen einzigen Benutzer. Die Nachricht, sowie
      * der Name des Empfängrs (name) wird an die Methode übergeben, die sie
      * dann via Uplink an den ClientServant sendet.
      */
    public void sendMsgToUser(String name, String msg) { }


    /** Sendet eine Nachricht in einen Channel, die von allen Teilnehmern
      * in diesem Channel empfangen werden kann. Die Nachricht wird an die
      * Methode übergeben und durch anlegen eines neuen Objekts SendMsgToChannel
      * an den ClientServant gesendet.
      */
    public void sendMsgToChannel(String msg) {
        this.sendCommand(
            new SendMsgToChannelCommand(msg));
    }

    /**
     * Verarbeitet eine empfangene Nachricht bzw. führt den empfangenen Befehl einfach aus.
     * Nachrichten vom Server, die durch den Downlink empfangen werden, werden hier als Parameter eingesetzt.
     */
    public void processMsg(Command msg) {
    /* Muss wg. Command überarbeitet werden.
    this.setAvailableChannelList(
      "Mensachat, Virtuelle Konferenz, tubs intern");
    this.channelMsgBuffer=    this.channelMsgBuffer.concat(msg+"\n");
    */

        msg.execute(this);
    }

    /** Dient dazu, einen Client am Server anzumelden. Dazu wird für
      * den neuen Client ein Socket am Server geöffnet, sowie ein
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
      * die für den gesamten Channel bestimmt ist, zu empfangen. Die Nachricht
      * wird vom Downlink an diese Methode übergeben und sie sorgt dafür, daß
      * der empfangene Text in der GUI erscheint.
      */
    public void sendMsgFromChannel(String fromName, String msg) {
        Debug.println("Client: " + fromName + " sayz:" + msg);
    }

    /** Dient dazu, eine Nachricht von einem einzigen Benutzer (fromName)
      * zu empfangen. Die Nachricht wird vom Downlink an diese Methode
      * übergeben und sie sorgt dafür, daß der empfangene Text in der
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

    /** Vector von Strings, repräsentiert die für den Benutzer freigegebenen
      * Die Methode ist nötig, damit der Server erkennt, für welche Channels
      * der Benutzer Zugriff hat.
     */
    protected Vector availableChannelList;

    /**
     * Speichert ankommende Nachrichten in einem Channel. Wird vom GUI benutzt und kann als
     * Protokoll der Unterhaltung in dem Channel dienen.
     */
    public String channelMsgBuffer = new String();
    protected Socket socket;

    /** Vector von Strings, repräsentiert die aktuellen Benutzer in einem
      * Wird im Client Window eingebunden.
      */
    protected Vector currentUserInChannelList;
}
