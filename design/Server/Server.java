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

  /** Startet den Server, danach ist er vollständig betriebsbereit und kann Verbindungswünsche von Clients behandeln. */
  public void startServer()
          throws java.io.FileNotFoundException, java.io.IOException {

    this.channelAdministration = new ChannelAdministration();
    this.userAdministration = new UserAdministration();
    this.dataBaseIO = new DataBaseIO(this.userAdministration,
                                     this.channelAdministration);

    this.dataBaseIO.loadFromDisk();
    this.listen();
  }

  /** Stoppt den Server, alle mit ihm verbundenen Clients werden abgemeldet. */
  public void stopServer() {

    this.stop = true;

    Enumeration enum = this.clientServantList.elements();

    while (enum.hasMoreElements()) {
      this.removeFromClientServantList((ClientServant) enum.nextElement());
    }

    Debug.println("Server: stopped");
  }

  /** entfernt einen ClientServant aus der Liste der aktiven ClientServants. */
  public synchronized void removeFromClientServantList(
          ClientServant paramClientServant) {

    if (this.clientServantList.removeElement(paramClientServant)) {
      paramClientServant.setServer(null);
      Debug.println("Server: removed ClientServant");
      Debug.println("Server: " + this.clientServantList.size()
                         + " ClientServants active");
    }
  }

  /** Wartet auf ankommendene Verbindungswünsche der Clients und leitet diese jeweils an einen neuen ClientObserver weiter. */
  private void listen() {

    try {
      this.serverSocket = new ServerSocket(this.SERVER_PORT,
                                           this.LISTEN_QUEUE_LENGTH);

      Debug.println("Server: started on "
                         + this.serverSocket.getInetAddress() + ":"
                         + this.serverSocket.getLocalPort());
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, e);
    }

    while (!stop) {
      try {
        Socket tmpSocket = serverSocket.accept();

        Debug.println("Server: incoming connection from "
                           + tmpSocket.getInetAddress() + ":"
                           + tmpSocket.getPort());

        ClientServant tmpClientServant = new ClientServant(tmpSocket, this,
                                           userAdministration);

        this.addToClientServantList(tmpClientServant);
        tmpClientServant.startClientServant();
      } catch (java.io.IOException e) {
        Debug.println(Debug.HIGH, e);
      }
    }

    try {
      this.serverSocket.close();
      Debug.println("Server: ServerSocket closed");
    } catch (java.io.IOException e) {
      Debug.println(Debug.HIGH, e);
    }
  }

  /** Fügt einen ClientServant zu der Liste aktiver ClientServants hinzu. */
  public synchronized void addToClientServantList(
          ClientServant paramClientServant) {

    if (!this.clientServantList.contains(paramClientServant)) {
      this.clientServantList.addElement(paramClientServant);
      paramClientServant.setServer(this);
      Debug.println("Server: added ClientServant");
      Debug.println("Server: " + this.clientServantList.size()
                         + " ClientServants active");
    }
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
  private ServerSocket serverSocket;
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
