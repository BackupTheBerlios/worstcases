package Server;

import java.util.Vector;
import java.util.Enumeration;
import Util.Helper;
import Util.Debug.Debug;


/**
 * Stellt Methoden zur Administration der Channel zur Verfügung.
 * So können unter anderem Channel hinzugefügt, bearbeitet und gelöscht werden.
 */
class ChannelAdministration {

  /** Gibt eine Aufzählung der existierenden Channel zurück. */
  public Enumeration getChannelEnum() {
    return Helper.vectorCopy(this.channelList).elements();
    //Kopiert die ChannelList und gibt ihre Elemente zurück.
  }

  /**
   * Setzt ChannelList auf die in channelEnum übergebenen Werte.  
   * Benutzt addToChannelList() und removeFromChannelList(). 
   */
  public synchronized void setChannelList(Enumeration channelEnum) {

    Enumeration enum = channelEnum;

    if (enum == null) { //Wenn channelEnum leer übergeben wurde,
      enum = (new Vector()).elements(); //wird eine neue Liste angelegt.
    }

    Vector tmpList = new Vector();
    Channel tmpChannel;

    while (enum.hasMoreElements()) {
      //Die Liste wird bis zum Ende durchgearbeitet.
      tmpChannel = (Channel) channelEnum.nextElement();
	  //Die einzelnen Channelobjekte werden geladen,   
      tmpList.addElement(tmpChannel);	//und zur tmpList hinzugefügt,
      this.addToChannelList(tmpChannel);//und zur ChannelList hinzugefügt!
    }

    enum = this.getChannelEnum();//Die Liste der existierenden Channel

    while (enum.hasMoreElements()) {//wird nun durchgearbeitet.	
      tmpChannel = (Channel) enum.nextElement();

      if (!tmpList.contains(tmpChannel)) {
      	//Channel, die nicht mehr existieren
        this.removeFromChannelList(tmpChannel);
        //werden aus der ChannelList entfernt.
      }
    }
  }

  /** 
   * Liefert eine Aufzählung der Name der existierenden Channel.
   * Benutzt getChannelEnum(). 
   */
  public Vector getChannelNames() {

    Vector tmpVector = new Vector();
    Enumeration enum = this.getChannelEnum();//Die ChannelList
    Channel tmpChannel;

    while (enum.hasMoreElements()) {//Die Liste wird durchgearbeitet.
      tmpChannel = (Channel) enum.nextElement();
	  //Der jeweilige Channel an tmpChannel übergeben,
      tmpVector.addElement(tmpChannel.getName());
       //und dessen Name in tmpVector abgelegt.
    }

    return tmpVector;//Die Liste der Channel-Namen wird zurückgegeben.
  }

  /**
   * Gibt den Channel mit dem angegebenen Namen zurück, falls er existiert.
   * Ansonsten wird null zurückgegeben.
   * Benutzt getChannelEnum().
   * @param name der Name des Channels, dessen Objekt erwartet wird
   */
  public Channel getFromChannelListByName(String name) {

    if (name != null) {//Wenn überhaupt ein Name angegeben wurde.
      Enumeration enum = this.getChannelEnum();//Die ChannelList!
      Channel tmpChannel;

      while (enum.hasMoreElements()) {//Die Liste wird durchgearbeitet.
        tmpChannel = (Channel) (enum.nextElement());
		//Der jeweilige wird Channel in tmpChannel abgelegt.
        if (tmpChannel.getName().compareTo(name) == 0) {
          return tmpChannel;
          //Wenn der Channel mit dem angegebenen Namen gefunden wird,
          //wird dieser zurückgegeben.
        }
      }
    }

    return null;//Keine Channel mit angegebenen Namen gefunden:
        		//Rückgaben = "null"
  }

  /**
   * Entfernt einen Channel. Benachrichtigt den betroffenen Channel 
   * mittels Channel.removeYou().
   * @param paramChannel das Channelobjekt, das gelöscht werden soll
   */
  public void removeFromChannelList(Channel paramChannel) {

    if (paramChannel != null) {//Wenn ein Channel übergeben wurde..
      if (this.channelList.removeElement(paramChannel)) {
        paramChannel.removeYou();
      }
	  //wird dieser aus der ChannelList entfernt und
      //mittels removeYou gelöscht.
      Debug.println(Debug.LOW, "ChannelAdministration: removed: " + paramChannel);
    }
  }

  /**
   * Fügt einen Channel hinzu, sofern er noch nicht existiert.
   * @param paramChannel das Channelobjekt, das hinzugefügt werden soll
   */
  public void addToChannelList(Channel paramChannel) {

    if (paramChannel != null) {//Wenn ein Channel übergeben wurde...
      if (this.getFromChannelListByName(paramChannel.getName()) == null) {
      	//Wenn dieser Channel noch nicht existiert...
        this.channelList.addElement(paramChannel);
        //wird er zur channelList hinzugefügt.
        Debug.println(Debug.LOW, "ChannelAdministration: added: " + paramChannel);
      }
    }
  }

  /**
   * Editiert den Channel mit dem angegebenen Namen mit einem neuen Datensatz
   * aus einem neuen Channelobjekt.
   * Benutzt Channel.setName(),setAllowedForGuest() und setAllowedUserList().
   * @param name Der Name des Channels, der verändert werden soll
   * @param newChannel neues Channelobjekt, 
   * das die neuen Daten des Channels enthält
   */
  public synchronized void editChannel(String oldName, Channel newChannel) {

    if ((oldName != null) && (newChannel != null)) {
    	//Wenn oldName und newChannel nicht leer übergeben wurden...
      Channel tmpChannel = this.getFromChannelListByName(oldName);
		//...wird der zu verändernde Channel geladen.
      Debug.println(Debug.MEDIUM, "ChannelAdministration: changing: " + tmpChannel);

      if (tmpChannel != null) {//Wenn der Channel gefunden wurde...
        tmpChannel.setName(newChannel.getName());
        //...wird sein Name aktualisiert.
        tmpChannel.setAllowedForGuest(newChannel.isAllowedForGuest());
        //...wird sein AllowedForGuest-Status aktualisiert.
        tmpChannel.setAllowedUserList(newChannel.getAllowedUserEnum());
        //...wird seine AllowedUserList aktualisiert.
      }
      newChannel.removeYou();
      //Das nun unnötige newChannel-Objekt wird gelöscht.
      Debug.println(Debug.MEDIUM, "ChannelAdministration: changed: " + tmpChannel);
    }
  }

  /**
   * Liefert eine Aufzählung der Channel, die für Gäste freigegeben sind.
   * Benutzt getChannelEnum() und channel.isAllowedForGuests().
   */
  public Enumeration getFreeForGuestEnum() {

    Vector tmpList = new Vector();
    Enumeration enum = this.getChannelEnum();//Die ChannelList.
    Channel tmpChannel;

    while (enum.hasMoreElements()) {//Die Liste wird durchgearbeitet.
      tmpChannel = (Channel) enum.nextElement();
		//Der jeweilige Channel wird an tmpChannel übergeben.
      if (tmpChannel.isAllowedForGuest()) {
        tmpList.addElement(tmpChannel);
        //Er wird zur tmpList hinzugefügt, falls er für Gäste
        //zugänglich ist.
      }
    }

    return tmpList.elements();//tmpList wird zurückgegeben.
  }

  /**
   * Liste der verfügbaren Channel.
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
