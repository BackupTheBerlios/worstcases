package Client;

import java.util.Vector;
import java.net.Socket;
import Util.*;
import Util.Commands.*;
import Util.Debug.Debug;

/** Die Clientapplikation */
public class Client implements Util.DownlinkOwner {
    /** Betritt den angegebenen Channel. */
    public void joinChannel(String name) {
        this.sendCommand(
            new JoinChannelCommand(name));
    }

    public void joinChannelError(){
    }

    public void stopClient(){
    }

    public void setUserData(String userName,Vector channelNames){
    }

    public void setChannelData(String paramName,Vector userNames){
    }

    public void newUserInChannel(String paramName) {
        Debug.println("Client: " + paramName + " joined channel");
    }

    public void setDownlink(Util.Downlink paramDownlink) { };

    public void downlinkError() { };

    public void loginError() {
        Debug.println("Client: login failed: ");
    }


    public void sendMsgToChannelError(){
    }

    public void sendMsgToUserError(){
    }

    protected void sendCommand(Command paramCommand) {
        try {
            this.uplink.sendMsg(paramCommand);
        }
        catch (java.io.IOException e) {
            Debug.println(Debug.HIGH, e);
        }
    }

    /** Verläßt den Channel. */
    public void leaveChannel() { }

    /** Meldet den Benutzer an. */
    public void login(String name, String password) {
        this.sendCommand(
            new LoginCommand(name, password));
    }

    /** Meldet einen Gast an. */
    public void loginAsGuest(String name) {
        Debug.println("Client: loginGuest " + name);
        this.sendCommand(
            new LoginGuestCommand(name));
    }

    /** Meldet den Benutzer ab. */
    public void logout() { }

    /**
     * setzt availableChannels mit den Daten aus dem String channelSet.
     * Der Client erhält die für den Benutzer zugänglichen Channels.
     */
    public void setAvailableChannelList(String channelSet) { }

    /**
     * Setzt currentUsers mit den Daten aus dem String userSet.
     * Der Client erhält die Liste der aktuellen Benutzer in dem betretenen Channel
     */
    public void setCurrentUserInChannelList(String userSet) { }

    /** Sendet eine Nachricht an einen Benutzer. 1 zu 1 Kommunikation */
    public void sendMsgToUser(String name, String msg) { }

    /** Sendet eine Nachricht in einen Channel. Diese wird dann von allen Teilnehmern im Channel empfangen. */
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

    public void startClient() {
        Debug.println("Client: starting...");
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
        Debug.println("Client: started.");
    }

    public void sendMsgFromChannel(String fromName, String msg) {
        Debug.println("Client: " + fromName + " sayz:" + msg);
    }

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

    /** Vector von Strings, repräsentiert die für den Benutzer freigegebenen Channels. */
    protected Vector availableChannelList;

    /**
     * Speichert ankommende Nachrichten in einem Channel. Wird vom GUI benutzt und kann als
     * Protokoll der Unterhaltung in dem Channel dienen.
     */
    public String channelMsgBuffer = new String();
    protected Socket socket;

    /** Vector von Strings, repräsentiert die aktuellen Benutzer in einem Channel */
    protected Vector currentUserInChannelList;
}
