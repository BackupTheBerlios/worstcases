package Server;

import java.net.ServerSocket;
import java.util.Vector;
import java.net.Socket;
import java.util.Enumeration;
import Util.Debug.Debug;

/**
 * Diese Klasse ist der Chat-Server. Der Server horcht an seinem
 * Port, und wenn ein Client eine Verbindungsanfrage stellt, startet er einen
 * ClientServant, der sich ab dann um diesen Client kümmert. Dann fängt der
 * Server wieder an, an seinem Port zu horchen und wartet auf neue Client-Verbindungswünsche.
 */
public class Server {
    /**
     * Diese Methode initialisiert den Server, indem neue Referenzen von channelAdministration, userAdministration,
     * clientServantWatchDog und dataBaseIO erzeugt werden. Ausserdem werden die Benutzer- und Channeldaten geladen
     * und ein ClientServantDog gestartet, um inaktive Clients aus dem System zu entfernen. Ruft listen() auf.
     */
    public void startServer() {
        Debug.println(Debug.LOW, this + ": starting server");
        this.channelAdministration = new ChannelAdministration();
        this.userAdministration = new UserAdministration(this.channelAdministration);
        this.dataBaseIO = new DataBaseIO(this.userAdministration, this.channelAdministration);
        this.clientServantWatchDog = new ClientServantWatchDog(this);
        try {
            this.dataBaseIO.loadFromDisk();
            // wird nur ausgeführt, falls loadFromDisk() keine Exception wirft
            this.clientServantWatchDog.start();
            this.listen();
        }
        catch (java.io.FileNotFoundException e) {
            Debug.println(Debug.HIGH, this + ": DataBase IO could not load database:" + e);
        }
        catch (java.io.IOException e) {
            Debug.println(Debug.HIGH, this + ": DataBase IO could not load database:" + e);
        }
    }

    private ClientServantWatchDog clientServantWatchDog;

    /**
     * Stoppt den Server, indem die ClientServants durch eine Schleife mit der Methode removeFromClientServant
     * aus der ClientServantList enfernt werden. Setzt stop=true, um die Listen-Methode zu beenden.
     */
    public void stopServer() {
        this.stop = true;
        this.clientServantWatchDog.stop = true;
        Enumeration enum = this.clientServantList.elements();
        while (enum.hasMoreElements()) {
            this.removeFromClientServantList((ClientServant)enum.nextElement());
        }
        Debug.println(Debug.LOW, this + ": Server stopped");
    }

    /** Entfernt den übergebenen ClientServant durch setServer(null) aus der Liste der aktiven ClientServants. */
    public synchronized void removeFromClientServantList(ClientServant paramClientServant) {
        if (paramClientServant != null) {
            if (this.clientServantList.removeElement(paramClientServant)) {
                paramClientServant.setServer(null);
                Debug.println(Debug.MEDIUM, this + ": " + this.clientServantList.size() + " ClientServants active");
            }
        }
    }

    /** Gibt eine Aufzählung der aktiven ClientServants zurück. */
    public Enumeration getClientServantEnum() {
        return this.clientServantList.elements();
    }

    public DataBaseIO getDataBaseIO() {
        return this.dataBaseIO;
    }

    /**
     * In listen() wird zuerst ein neuer ServerSocket angelegt. In einer Schleife werden, solange der Thread nicht gestoppt
     * wurde, bei ankommenden Verbindungenswünschen von Clients
     * neue Clientservants erstellt, diese zur Liste der Servants hinzugefügt und gestartet.
     * Nachdem der Thread beendet wurde, wird der ServerSocket geschlossen.
     * Falls die Zugriffe auf den ServerSocket nicht möglich sind, werden diese durch try und catch abgefangen.
     * Schleifendurchlauf, solange stop==true. Benutzt ClientServant.startClientServant() und addToClientServantList().
     */
    private void listen() {
        try {
            this.serverSocket = new ServerSocket(this.SERVER_PORT, this.LISTEN_QUEUE_LENGTH);
            Debug.println(Debug.MEDIUM, this + ": Server started on " + this.serverSocket.getInetAddress() + ":" +
                this.serverSocket.getLocalPort());
                while (!stop) {
                    try {
                        Socket tmpSocket = serverSocket.accept();
                        Debug.println(Debug.MEDIUM,this+": incoming connection from " + tmpSocket.getInetAddress() + ":" + tmpSocket.getPort());
                        ClientServant tmpClientServant = new ClientServant(tmpSocket, this, userAdministration);
                        this.addToClientServantList(tmpClientServant);
                        tmpClientServant.startClientServant();
                    } catch (java.io.IOException e) {
                        Debug.println(this+": error while listening: "+e);
                    }
            }
        } catch (java.io.IOException e) {
            Debug.println(Debug.HIGH, this + ": error opening socket:" + e);
        }
        try {
            this.serverSocket.close();
            Debug.println(Debug.LOW,this+": ServerSocket closed");
        } catch (java.io.IOException e) {
            Debug.println(Debug.HIGH,this+": error closing serverSocket: "+e);
        }
    }

    /** Fügt einen ClientServant zu der Liste aktiver ClientServants hinzu. Benutzt ClientServant.setServer(). */
    public synchronized void addToClientServantList(ClientServant paramClientServant) {
        if(paramClientServant!=null){
        if (!this.clientServantList.contains(paramClientServant)) {
            this.clientServantList.addElement(paramClientServant);
            paramClientServant.setServer(this);
            Debug.println(Debug.MEDIUM,this+": "+this.clientServantList.size() + " ClientServants active");
        }
        }
    }

    /** Gibt die aktive ChannelAdministration zurück. */
    public ChannelAdministration getChannelAdministration() {
        return channelAdministration;
    }

    /**
     * Eine Liste der aktiven ClientServants des Servers.
     * @link aggregation
     *     @associates <{ClientServant}>
     * @clientCardinality 1
     * @supplierCardinality 0..
     */
    private Vector clientServantList = new Vector();

    /** Der Port, auf dem der Server sein ServerSocket öffnet und auf Anfragen der Clients horcht. */
    private int SERVER_PORT = 1500;

    /**
     * Die Länge der Warteschlange, in der Verbindungswünsche von Clients
     * zwischengespeichert werden, die nicht sofort verarbeitet werden können.
     * Verbindungswünsche, die nicht mehr in die Warteschlange  passen, werden
     * automatisch abgewiesen (siehe auch java.net.ServerSocket).
     */
    private int LISTEN_QUEUE_LENGTH = 10;

    /** Der ServerSocket. */
    private ServerSocket serverSocket;

    /** Flag, welches angibt, ob listen() weiter auf Verbindungen warten soll. */
    private boolean stop = false;

    /**
     * Die Datenbank, in der die Informationen über User und Channel gespeichert
     * werden. Von dort werden sie beim Start des Servers ausgelesen.
     * @link aggregation
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    private DataBaseIO dataBaseIO;

    /**
     * Die Benutzerverwaltung.
     * @link aggregation
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    private UserAdministration userAdministration;

    /**
     * Die channelverwaltung.
     * @link aggregation
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    private ChannelAdministration channelAdministration;
}
