package Server;

import java.util.Vector;
import java.util.Enumeration;

/** Eine Instanz dieser Klasse stellt wichtige Methoden zur Channel-Administration
  * zur Verfügung.
  * So können unter anderem Channel hinzugefügt, bearbeitet und
  * gelöscht werden.
  */
class ChannelAdministration {
	/** Gibt eine Aufzählung der existierenden Channel zurück */
    public Enumeration getChannelEnum() {
        return this.channelList.elements();
    }

	/** 
	 * Setzt ChannelList auf die in channelEnum übergebenen Werte.  
 	 * Benutzt addToChannelList() und removeFromChannelList().
 	 */ 
    public  synchronized void setChannelList(Enumeration channelEnum) {
        Vector tmpList = new Vector();
        Channel tmpChannel;
        while (channelEnum.hasMoreElements()) {
            tmpChannel = (Channel)channelEnum.nextElement();
            tmpList.addElement(tmpChannel);
            this.addToChannelList(tmpChannel);
        }
        Enumeration enum = this.getChannelEnum();
        while (enum.hasMoreElements()) {
            tmpChannel = (Channel)enum.nextElement();
            if (!tmpList.contains(tmpChannel)) {
                this.removeFromChannelList(tmpChannel);
            }
        }
    }

	/** 
	 * Liefert eine Aufzählung der existierenden Channel.
	 * Benutzt getChannelEnum().
	 */
    public Vector getChannelNames() {
 		Vector tmpVector = new Vector();
   		Enumeration enum = this.getChannelEnum();
   		Channel tmpChannel;
    	while (enum.hasMoreElements()) {
       		tmpChannel = (Channel)enum.nextElement();
        	tmpVector.addElement(tmpChannel.getName());
      	}
      	return tmpVector;
    }

    /** 
     * Gibt den Channel mit dem angegebenen Namen zurück,
     * falls er existiert. Ansonsten wird null zurückgegeben. 
     * benutzt getChannelEnum()
     * @param name der Name des Channels, dessen Objekt erwartet wird
     */    
    public Channel getFromChannelListByName(String name) {
        Enumeration enum = this.getChannelEnum();
        Channel tmpChannel;
        while (enum.hasMoreElements()) {
            tmpChannel = (Channel)(enum.nextElement());
            if (tmpChannel.getName().compareTo(name) == 0) {
                return tmpChannel;
            }
        }
        return null;
    }

    /** 
     * Entfernt einen Channel.
     * Benachrichtigt den Betroffenen Channel mittels Channel.removeYou()
     * @param paramChannel das Channelobjekt, das gelöscht werden soll
     */
    public synchronized void removeFromChannelList(Channel paramChannel) {
        if (this.channelList.removeElement(paramChannel)) {
            paramChannel.removeYou();
        }
    }

    /** 
     * Fügt einen Channel hinzu, sofern er noch nicht existiert 
     * @param paramChannel das Channelobjekt, das hinzugefügt werden soll
     */
    public  synchronized void addToChannelList(Channel paramChannel) {
        if (!this.channelList.contains(paramChannel)) {
            this.channelList.addElement(paramChannel);
        }
    }

    /**
     * Editiert den Channel mit dem angegebenen Namen mit einem neuen Datensatz aus einem neuen Channelobjekt
     * benutzt Channel.setName(),setAllowedForGuest() und setAllowedUserList()
     * @param name Der Name des Channels, der verändert werden soll
     * @param newChannel neues Channelobjekt, das die neuen Daten des Channels enthält
     */

    public  synchronized void editChannel(String oldName, Channel newChannel) {
        Channel tmpChannel = this.getFromChannelListByName(oldName);
        if (tmpChannel != null) {
            tmpChannel.setName(newChannel.getName());
            tmpChannel.setAllowedForGuest(newChannel.isAllowedForGuest());
            tmpChannel.setAllowedUserList(newChannel.getAllowedUserEnum());
        }
    }


	/**
	 * liefert eine Aufzählung der Channel, die für Gäste freigegeben sind
	 * benutzt getChannelEnum() und channel.isAllowedForGuests()
	 */
    public Enumeration getFreeForGuestEnum() {
        Vector tmpList = new Vector();
        Enumeration enum = this.getChannelEnum();
        Channel tmpChannel;
        while (enum.hasMoreElements()) {
            tmpChannel = (Channel)enum.nextElement();
            if (tmpChannel.isAllowedForGuest()) {
                tmpList.addElement(tmpChannel);
            }
        }
        return tmpList.elements();
    }


    /**
     * @link aggregation
     *     @associates <{Channel}>
     * @clientCardinality 1
     * @supplierCardinality 0..
     */
    private Vector channelList=new Vector();


    /**
     * @supplierCardinality 1
     * @clientCardinality 1
     */
    private DataBaseIO dataBaseIO;


    /**setzt dataBaseIO, benutzt DataBaseIO.setChannelAdministration*/
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
