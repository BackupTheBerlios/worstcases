package Client;

import java.util.Vector;
import java.net.Socket;


/**
 * der erweiterte AdminClient
 */
public class AdminClient extends Client {

  /**
   * f�gt einen Channel mit den Daten aus dem String channel hinzu.
   * Hiermit l��t sich ein neuer Channel erzeugen.
   */
  public void addChannel(String channel) {}

  /**
   * l�scht einen Channel
   */
  public void deleteChannel(String name) {}

  /**
   * setzt den Channel mit dem angegebenen Namen auf
   * den neuen Datensatz aus dem String channel.
   * Diese Methode wird verwendet, um Channeldaten zu ver�ndern.
   */
  public void editChannel(String name, String channel) {}

  /**
   * f�gt den Benutzer mit den Daten aus user hinzu
   */
  public void addUser(String user) {}

  /**
   * l�scht den Benutzer mit dem Namen name
   */
  public void deleteUser(String name) {}

  /**
   * editiert einen Benutzer
   */
  public void editUser(String name, String channel) {}

  /**
   * verarbeitet erweiterte Nachrichten, d.h. auch Nachrichten die f�r die normalen
   * Clients irrelevant sind, z.B. �nderungsmeldungen �ber Benutzer- und
   * Channeldaten.
   * Erweitert also Client.processMsg(String msg)
   */
  public void processMsg(String msg) {}

  /**
   * ein Benutzerdatensatz als String
   */
  private String userSet;

  /**
   * ein Channeldatensatz als String
   */
  private String channelSet;

  /**
   * Liste aller Benutzernamen
   */
  private Vector allUserList;

  /**
   * Liste aller Channelnamen
   */
  private Vector allChannelList;
}
