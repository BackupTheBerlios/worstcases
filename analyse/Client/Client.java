/* Generated by Together */

package Client;
import java.util.Vector;
import java.net.Socket;

/**
 * die Clientapplikation 
 */
public class Client {
    /**
     * betritt den angegebenen Channel 
     */
    public void joinChannel(String name) {
    }

    /**
     * verl��t den Channel 
     */
    public void leaveChannel() {
    }

    /**
     * meldet den Benutzer an 
     */
    public void login(String name, String password) {
     this.uplink.sendMsg("login Foo password Bar");
    }

    /**
     * meldet einen Gast an 
     */
    public void loginAsGuest(String name) {
    }

    /**
     * meldet den Benutzer ab 
     */
    public void logout() {
    }

    /**
     * setzt availableChannels mit den Daten aus dem String channelSet.
     * Der Client erh�lt die f�r den Benutzer zug�nglichen Channels. 
     */
    public void setAvailableChannelList(String channelSet) {
    }

    /**
     * setzt currentUsers mit den Daten aus dem String userSet.
     * Der Client erh�lt die Liste der aktuellen Benutzer in dem
     * betretenen Channel
     */
    public void setCurrentUserInChannelList(String userSet) {
    }

    /**
     * sendet eine Nachricht an einen Benutzer.
     * 1 zu 1 Kommunikation 
     */
    public void sendMsgToUser(String name, String msg) {
    }

    /**
     * sendet eine Nachricht in einen Channel.
     * Diese wird dann von allen Teilnehmern im 
     * Channel empfangen.
     */
    public void sendMsgToChannel(String msg) {
    }

    /**
     * verarbeitet eine empfangene Nachricht, entscheidet, 
     * welche Funktionalit�t aufgerufen werden mu�.
     * Nachrichten vom Server, die durch den Downlink empfangen werden,
     * werden hier als Parameter eingesetzt.
     */
    public void processMsg(String msg) {
     this.setAvailableChannelList("Mensachat, Virtuelle Konferenz, tubs intern");
    }

    /**
     * @directed
     * @clientCardinality 1
     * @supplierCardinality 1 
     */
    protected Uplink uplink;

    /**
     * @directed
     * @supplierCardinality 1
     * @clientCardinality 1 
     */
    protected Downlink downlink;
    /**
    * der Port des Servers
    */
    protected final static int SERVER_PORT=2000;
    /**
     * die IP - Adresse des Servers
    */
    protected final static String SERVER_IP = "192.168.0.15";

    /**
     * Vector von Strings, repr�sentiert die f�r den Benutzer freigegebenen Channels 
     */
    protected Vector availableChannelList;

    /**
     * speichert ankommende Nachrichten in einem Channel.
     * Wird von der GUI benutzt und kann als 
     * Protokoll der Unterhaltung in dem Channel dienen. 
     */
    protected Vector channelMsgBuffer;
    protected Socket socket;

    /**
     * Vector von Strings, repr�sentiert die aktuellen Benutzer in einem Channel 
     */
    protected Vector currentUserInChannelList;
}
