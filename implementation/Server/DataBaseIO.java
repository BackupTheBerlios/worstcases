package Server;

import java.util.Vector;
import java.util.Enumeration;
import java.io.*;
import java.util.StringTokenizer;
import Util.Debug.Debug;

/**
 * Stellt Methoden bereit, um die Benutzer- und Channeldatenbank zu laden
 * und zu speichern. Außerdem wird dafür gesorgt, dass für den Betrieb die
 * relationalen Beziehungen zwischen User- und Channeldatenbank gesetzt werden.
 */
class DataBaseIO {

    /**
     * Konstruktor, der die Attribute für die ChannelAdministration und UserAdministration setzt.
     * Benutzt setChannelAdministration() und setUserAdministration().
     */
    public DataBaseIO(UserAdministration paramUserAdministration,ChannelAdministration paramChannelAdministration){
     this.setChannelAdministration(paramChannelAdministration);
     this.setUserAdministration(paramUserAdministration);
    }

    /**
     * Konvertiert den Namen, das Password, das isAdmin-Flag und
     * die Namen der für den Benutzer erlaubten Channels eines Userobjekts in einen String, wird von saveToDisk() verwendet.
     * Format des Strings: "name#password#true#channel1#channel2#channel3".
     */
    private String userToString(User paramUser) {
        String tmpString = paramUser.getName() + "#" + paramUser.getPassword() + "#" + paramUser.isAdmin();
        Enumeration enum = paramUser.getAllowedChannelEnum();
        while (enum.hasMoreElements()) {
            tmpString = tmpString + "#" + ((Channel)(enum.nextElement())).getName();
        }
        System.out.println(tmpString);
        return tmpString;
    }

    /**
     * Konvertiert den von userToString() erzeugten String in ein Userobjekt.
     * Setzt vorraus, daß die entsprechenden Channelobjekte bereits geladen wurden.
     * Benutzt channelAdministration.getFromChannelListByName() und user.setAllowedChannelList().
     */

    private User stringToUser(String userSet) {
        StringTokenizer tmpTokenizer = new StringTokenizer(userSet, "#", false);
        String name = tmpTokenizer.nextToken();
        String password = tmpTokenizer.nextToken();
        boolean isAdmin = (
            new Boolean(tmpTokenizer.nextToken())).booleanValue();
        Vector tmpChannelList = new Vector();
        while (tmpTokenizer.hasMoreTokens()) {
            tmpChannelList.addElement(this.channelAdministration.getFromChannelListByName(tmpTokenizer.nextToken()));
        }
        User tmpUser = new User(name, password, false, isAdmin,this.userAdministration);
        tmpUser.setAllowedChannelList(tmpChannelList.elements());
        System.out.println(tmpUser);
        return tmpUser;
    }

    /**
     * Konvertiert den Namen und das isAllowedForGuests-Flag
     * eines Channelobjekts in einen String, wird von saveToDisk() verwendet.
     * Format des Strings: "name#true".
     */
    private String channelToString(Channel paramChannel) {
        String tmpString = paramChannel.getName() + "#" + paramChannel.isAllowedForGuest();
        return tmpString;
    }

    /** Konvertiert den von channelToString() erzeugten String in ein Channelobjekt.
      */
    private Channel stringToChannel(String channelSet) {
        StringTokenizer tmpTokenizer = new StringTokenizer(channelSet, "#", false);
        String name = tmpTokenizer.nextToken();
        boolean allowedForGuests = (
            new Boolean(tmpTokenizer.nextToken())).booleanValue();
        Channel tmpChannel = new Channel(name, allowedForGuests);
        System.out.println(tmpChannel);
        return tmpChannel;
    }

    /**
     * Lädt die Benutzer- und Channeldaten aus userDBFile und channelDBFile
     * mittels stringToUser(),stringToChannel,channelAdministration.setChannelList() und
     * userAdministration.setUserList().
     */
    public synchronized void loadFromDisk() throws java.io.FileNotFoundException, java.io.IOException {
        String tmpString;
        Vector tmpList = new Vector();
        BufferedReader tmpBufferedReader = new BufferedReader(
            new FileReader(
            new File(this.channelDBFile)));
        tmpString = tmpBufferedReader.readLine();
        while (tmpString != null) {
            tmpList.addElement(this.stringToChannel(tmpString));
            tmpString = tmpBufferedReader.readLine();
        }
        this.channelAdministration.setChannelList(tmpList.elements());
        tmpBufferedReader.close();

        System.out.println("channeldb loaded");

        tmpList = new Vector();
        tmpBufferedReader = new BufferedReader(
            new FileReader(
            new File(this.userDBFile)));
        tmpString = tmpBufferedReader.readLine();
        while (tmpString != null) {
            tmpList.addElement(this.stringToUser(tmpString));
            tmpString = tmpBufferedReader.readLine();
        }
        this.userAdministration.setUserList(tmpList.elements());
        tmpBufferedReader.close();

        System.out.println("userdb loaded");
    }

    /**
     * Speichert die Benutzer- und Channeldaten der aktuellen User- (keine Gäste) und Channelobjekte im System
     * in userDBFile und channelDBFile mittels userToString(), channelToString().
     * Benutzt channelAdministration.getChannelEnum() und userAdministration.getUserEnum()
     */
    public synchronized void saveToDisk()  {
        try{
        BufferedWriter tmpBufferedWriter = new BufferedWriter(
            new FileWriter(
            new File(this.channelDBFile)));
        Enumeration enum = this.channelAdministration.getChannelEnum();
        while (enum.hasMoreElements()) {
            tmpBufferedWriter.write(this.channelToString((Channel)(enum.nextElement())) + "\r\n");
        }
        tmpBufferedWriter.close();
        System.out.println("channel data written to disk");

        tmpBufferedWriter = new BufferedWriter(
            new FileWriter(
            new File(this.userDBFile)));
        User tmpUser;
        enum=this.userAdministration.getUserEnum();
        while (enum.hasMoreElements()) {
            tmpUser = (User)(enum.nextElement());
            if (!tmpUser.isGuest()) {
                tmpBufferedWriter.write(this.userToString(tmpUser) + "\r\n");
            }
        }
        tmpBufferedWriter.close();
        System.out.println("user data written to disk");
        }
        catch(java.io.IOException e){
         Debug.println(Debug.HIGH,"error while saving data: "+e);
        }
    }

    /**
     *  Setzt channelAdministration und benutzt channelAdministration.setDataBaseIO().
     */
    public void setChannelAdministration(ChannelAdministration paramChannelAdministration) {
        if (this.channelAdministration != paramChannelAdministration) {
            if (this.channelAdministration != null) {
                ChannelAdministration oldValue = this.channelAdministration;
                this.channelAdministration = null;
                oldValue.setDataBaseIO(null);
            }
            this.channelAdministration = paramChannelAdministration;
            if (paramChannelAdministration != null) {
                paramChannelAdministration.setDataBaseIO(this);
            }
        }
    }

    /*
     * Setzt userAdministration und benutzt userAdministration.setDataBaseIO().
     */

    public void setUserAdministration(UserAdministration paramUserAdministration) {
        if (this.userAdministration != paramUserAdministration) {
            if (this.userAdministration != null) {
                UserAdministration oldValue = this.userAdministration;
                this.userAdministration = null;
                oldValue.setDataBaseIO(null);
            }
            this.userAdministration = paramUserAdministration;
            if (paramUserAdministration != null) {
                paramUserAdministration.setDataBaseIO(this);
            }
        }
    }

    /**
     * Die Benutzerverwaltung.
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    private UserAdministration userAdministration;

    /**
     * Die Channelverwaltung.
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    private ChannelAdministration channelAdministration;

    /** Dateiname der Channeldatenbank. */
    private final static String channelDBFile = "channel.db";

    /** Dateiname der Benutzerdatenbank. */
    private final static String userDBFile = "user.db";
}
