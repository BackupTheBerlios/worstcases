package Server;

import java.net.ServerSocket;
import java.util.Vector;
import java.net.Socket;
import java.util.Enumeration;

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
     * und ein ClientServantDog gestartet, um inaktive Clients aus dem System zu entfernen.
     * Ruft listen() auf.
     */
    public void startServer() throws java.io.FileNotFoundException, java.io.IOException {
        this.channelAdministration = new ChannelAdministration();
        this.userAdministration = new UserAdministration(this.channelAdministration);
        this.dataBaseIO = new DataBaseIO(this.userAdministration, this.channelAdministration);
        this.dataBaseIO.loadFromDisk();
        this.clientServantWatchDog=new ClientServantWatchDog(this);
        this.clientServantWatchDog.start();
        this.listen();
    }

    private ClientServantWatchDog clientServantWatchDog;

    /** 
     * Stoppt den Server, indem die ClientServants durch eine Schleife mit der Methode removeFromClientServant
     * aus der ClientServantList enfernt werden.
     * Setzt stop=true, um die Listen-Methode zu beenden.
     */
    public void stopServer() {
        this.stop=true;
        Enumeration enum = this.clientServantList.elements();
        while (enum.hasMoreElements()) {
          this.removeFromClientServantList((ClientServant)enum.nextElement());
        }
        System.out.println("Server stopped");
    }

    /** 
     * Entfernt den übergebenen ClientServant durch setServer(null) aus der Liste der aktiven ClientServants.
     */
    public synchronized void removeFromClientServantList(ClientServant paramClientServant) {
        if (this.clientServantList.removeElement(paramClientServant)) {
            paramClientServant.setServer(null);
            System.out.println("removed ClientServant");
            System.out.println(this.clientServantList.size() + " ClientServants active");
        }
    }

    /**Gibt eine Aufzählung der aktiven ClientServants zurück.*/
    public Enumeration getClientServantEnum(){
     return this.clientServantList.elements();
    }
 
    /** In listen() wird zuerst ein neuer ServerSocket angelegt.
     *  In einer Schleife werden, solange der Thread nicht gestoppt wurde, bei ankommenden Verbindungenswünschen von Clients
     *  neue Clientservants erstellt, diese zur Liste der Servants hinzugefügt und gestartet.
     *  Nachdem der Thread beendet wurde, wird der ServerSocket geschlossen.
     *  Falls die Zugriffe auf den ServerSocket nicht möglich sind, werden diese durch try und catch abgefangen.
     * Schleifendurchlauf, solange stop==true.
     * Benutzt ClientServant.startClientServant() und addToClientServantList().
     */
    private void listen() {
        try {
            this.serverSocket = new ServerSocket(this.SERVER_PORT, this.LISTEN_QUEUE_LENGTH);
            System.out.println("Server started on " + this.serverSocket.getInetAddress() + ":" +
                this.serverSocket.getLocalPort());
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
        while (!stop) {
            try {
                Socket tmpSocket = serverSocket.accept();
                System.out.println("incoming connection from " + tmpSocket.getInetAddress() + ":" + tmpSocket.getPort());
                ClientServant tmpClientServant = new ClientServant(tmpSocket, this, userAdministration);
                this.addToClientServantList(tmpClientServant);
                tmpClientServant.startClientServant();
            } catch (java.io.IOException e) {
                System.out.println(e);
            }
        }

                try {
                    this.serverSocket.close();
                    System.out.println("ServerSocket closed");
        } catch (java.io.IOException e) {
            System.out.println(e);
        }

    }

    /** Fügt einen ClientServant zu der Liste aktiver ClientServants hinzu.
      * Benutzt ClientServant.setServer().
      */
    public synchronized void addToClientServantList(ClientServant paramClientServant) {
        if (!this.clientServantList.contains(paramClientServant)) {
            this.clientServantList.addElement(paramClientServant);
            paramClientServant.setServer(this);
            System.out.println("added ClientServant");
            System.out.println(this.clientServantList.size() + " ClientServants active");
        }
    }


    /**Gibt die aktive ChannelAdministration zurück.*/
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

    /**Flag, welches angibt, ob listen() weiter auf Verbindungen warten soll.*/
    private boolean stop=false;

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
