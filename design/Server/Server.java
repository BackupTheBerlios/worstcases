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
    /** Startet den Server, danach ist er vollständig betriebsbereit und kann Verbindungswünsche von Clients behandeln. */
    public void startServer() throws java.io.FileNotFoundException, java.io.IOException {
        this.clientServantList = new Vector();
        this.dataBaseIO = new DataBaseIO();
        this.channelAdministration = new ChannelAdministration(this.dataBaseIO);
        this.userAdministration = new UserAdministration(this.dataBaseIO, this.channelAdministration);
        this.dataBaseIO.setUserAdministration(this.userAdministration);
        this.dataBaseIO.setChannelAdministration(this.channelAdministration);
        this.dataBaseIO.loadFromDisk();
        try {
            this.serverSocket = new ServerSocket(this.SERVER_PORT, this.LISTEN_QUEUE_LENGTH);
            System.out.println("Server started on " + this.serverSocket.getInetAddress() + ":" +
                this.serverSocket.getLocalPort());
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
        System.out.println("free for guests: "+this.channelAdministration.getFreeForGuestList());
        this.listen();
    }

    /** Stoppt den Server, alle mit ihm verbundenen Clients werden abgemeldet. */
    public void stopServer() throws java.io.IOException {
        this.serverSocket.close();
        Enumeration enum = this.clientServantList.elements();
        while (enum.hasMoreElements()) {
            ((ClientServant)(enum.nextElement())).stopClientServant();
        }
    }

    /** Stoppt und entfernt einen ClientServant aus der Liste der aktiven ClientServants. */
    public synchronized void removeFromClientServantList(ClientServant paramClientServant) {
        if(this.clientServantList.removeElement(paramClientServant)){
                paramClientServant.stopClientServant();
                System.out.println("removed ClientServant");
                System.out.println(this.clientServantList.size()+" ClientServants active");
        }

    }

    /** Wartet auf ankommendene Verbindungswünsche der Clients und leitet diese jeweils an einen neuen ClientObserver weiter. */
    private void listen() {
        while (true) {
            try {
                Socket tmpSocket = serverSocket.accept();
                System.out.println("incoming connection from " + tmpSocket.getInetAddress() + ":" + tmpSocket.getPort());
                ClientServant tmpClientServant = new ClientServant(tmpSocket, this, userAdministration);
                this.clientServantList.addElement(tmpClientServant);
                tmpClientServant.startClientServant();
                System.out.println(this.clientServantList.size() + " ClientServants active");
            } catch (java.io.IOException e) {
                System.out.println(e);
            }
        }
    }

    /** Fügt einen ClientServant zu der Liste aktiver ClientServants hinzu. */
    public synchronized void addToClientServantList(ClientServant paramClientServant) {
        this.clientServantList.addElement(paramClientServant);
    }

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
    private Vector clientServantList;

    /** Der Port, auf dem der Server sein ServerSocket öffnet und auf Anfragen der Clients horcht. */
    private final static int SERVER_PORT = 1500;

    /**
     * Die Länge der Warteschlange, in der Verbindungswünsche von Clients
     * zwischengespeichert werden, die nicht sofort verarbeitet werden können.
     * Verbindungswünsche, die nicht mehr in die Warteschlange  passen, werden
     * automatisch abgewiesen (siehe auch java.net.ServerSocket).
     */
    private final static int LISTEN_QUEUE_LENGTH = 10;
    private ServerSocket serverSocket;

    /**
     * Die Datenbank, in der die Informationen über User und Channel gespeichert
     * werden. Von dort werden sie beim Start des Servers ausgelesen.
     * @link aggregation
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    private DataBaseIO dataBaseIO;

    /**
     * @link aggregation
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    private UserAdministration userAdministration;

    /**
     * @link aggregation
     * @clientCardinality 1
     * @supplierCardinality 1
     */
    private ChannelAdministration channelAdministration;
}
