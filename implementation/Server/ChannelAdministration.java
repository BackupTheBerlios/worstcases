package Server;

import java.util.Vector;
import java.util.Enumeration;
import Util.Helper;
import Util.Debug.Debug;


/**
 * Stellt Methoden zur Administration der Channel zur Verf�gung.
 * So k�nnen unter anderem Channel hinzugef�gt, bearbeitet und gel�scht werden.
 */
class ChannelAdministration {

  /** Gibt eine Aufz�hlung der existierenden Channel zur�ck. */
  public Enumeration getChannelEnum() {
    return Helper.vectorCopy(this.channelList).elements();
  }

  /** Setzt ChannelList auf die in channelEnum �bergebenen Werte.  Benutzt addToChannelList() und removeFromChannelList(). */
  public synchronized void setChannelList(Enumeration channelEnum) {

    Enumeration enum = channelEnum;

    if (enum == null) {
      enum = (new Vector()).elements();
    }

    Vector tmpList = new Vector();
    Channel tmpChannel;

    while (enum.hasMoreElements()) {
      tmpChannel = (Channel) channelEnum.nextElement();

      tmpList.addElement(tmpChannel);
      this.addToChannelList(tmpChannel);
    }

    enum = this.getChannelEnum();

    while (enum.hasMoreElements()) {
      tmpChannel = (Channel) enum.nextElement();

      if (!tmpList.contains(tmpChannel)) {
        this.removeFromChannelList(tmpChannel);
      }
    }
  }

  /** Liefert eine Aufz�hlung der existierenden Channel. Benutzt getChannelEnum(). */
  public Vector getChannelNames() {

    Vector tmpVector = new Vector();
    Enumeration enum = this.getChannelEnum();
    Channel tmpChannel;

    while (enum.hasMoreElements()) {
      tmpChannel = (Channel) enum.nextElement();

      tmpVector.addElement(tmpChannel.getName());
    }

    return tmpVector;
  }

  /**
   * Gibt den Channel mit dem angegebenen Namen zur�ck, falls er existiert. Ansonsten wird null zur�ckgegeben.
   * Benutzt getChannelEnum().
   * @param name der Name des Channels, dessen Objekt erwartet wird
   */
  public Channel getFromChannelListByName(String name) {

    if (name != null) {
      Enumeration enum = this.getChannelEnum();
      Channel tmpChannel;

      while (enum.hasMoreElements()) {
        tmpChannel = (Channel) (enum.nextElement());

        if (tmpChannel.getName().compareTo(name) == 0) {
          return tmpChannel;
        }
      }
    }

    return null;
  }

  /**
   * Entfernt einen Channel. Benachrichtigt den betroffenen Channel mittels Channel.removeYou().
   * @param paramChannel das Channelobjekt, das gel�scht werden soll
   */
  public void removeFromChannelList(Channel paramChannel) {

    if (paramChannel != null) {
      if (this.channelList.removeElement(paramChannel)) {
        paramChannel.removeYou();
      }

      Debug.println(Debug.LOW, this + ": removed: " + paramChannel);
    }
  }

  /**
   * F�gt einen Channel hinzu, sofern er noch nicht existiert.
   * @param paramChannel das Channelobjekt, das hinzugef�gt werden soll
   */
  public void addToChannelList(Channel paramChannel) {

    if (paramChannel != null) {
      if (this.getFromChannelListByName(paramChannel.getName()) == null) {
        this.channelList.addElement(paramChannel);
        Debug.println(Debug.LOW, this + ": added: " + paramChannel);
      }
    }
  }

  /**
   * Editiert den Channel mit dem angegebenen Namen mit einem neuen Datensatz aus einem neuen Channelobjekt.
   * Benutzt Channel.setName(),setAllowedForGuest() und setAllowedUserList().
   * @param name Der Name des Channels, der ver�ndert werden soll
   * @param newChannel neues Channelobjekt, das die neuen Daten des Channels enth�lt
   */
  public synchronized void editChannel(String oldName, Channel newChannel) {

    if ((oldName != null) && (newChannel != null)) {
      Channel tmpChannel = this.getFromChannelListByName(oldName);

      Debug.println(Debug.MEDIUM, this + ": changing: " + tmpChannel);

      if (tmpChannel != null) {
        tmpChannel.setName(newChannel.getName());
        tmpChannel.setAllowedForGuest(newChannel.isAllowedForGuest());
        tmpChannel.setAllowedUserList(newChannel.getAllowedUserEnum());
      }

      newChannel.removeYou();
      Debug.println(Debug.MEDIUM, this + ": changed: " + tmpChannel);
    }
  }

  /**
   * Liefert eine Aufz�hlung der Channel, die f�r G�ste freigegeben sind.
   * Benutzt getChannelEnum() und channel.isAllowedForGuests().
   */
  public Enumeration getFreeForGuestEnum() {

    Vector tmpList = new Vector();
    Enumeration enum = this.getChannelEnum();
    Channel tmpChannel;

    while (enum.hasMoreElements()) {
      tmpChannel = (Channel) enum.nextElement();

      if (tmpChannel.isAllowedForGuest()) {
        tmpList.addElement(tmpChannel);
      }
    }

    return tmpList.elements();
  }

  /**
   * Liste der verf�gbaren Channel.
   * @link aggregation
   *     @associates <{Channel}>
   * @clientCardinality 1
   * @supplierCardinality 0..
   */
  private Vector channelList = new Vector();
  public static final String FOYERNAME = "Foyer";

  /**
   * "Datenbank", in der die Channel-Daten gespeichert sind.
   * @supplierCardinality 1
   * @clientCardinality 1
   */
  private DataBaseIO dataBaseIO;

  /** Setzt dataBaseIO, benutzt DataBaseIO.setChannelAdministration. */
  public void setDataBaseIO(DataBaseIO paramDataBaseIO) {

    if (this.dataBaseIO != paramDataBaseIO) {
      if (this.dataBaseIO != null) {
        DataBaseIO oldValue = this.dataBaseIO;

        this.dataBaseIO = null;

        oldValue.setChannelAdministration(null);
      }

      this.dataBaseIO = paramDataBaseIO;

      if (paramDataBaseIO != null) {
        paramDataBaseIO.setChannelAdministration(this);
      }
    }
  }
}
