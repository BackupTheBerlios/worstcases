package Server;

import java.util.Vector;
import java.util.Enumeration;

/** Eine Instanz dieser Klasse stellt wichtige Methoden zur Channel-Administration
  * zur Verf�gung.
  * So k�nnen unter anderem Channel hinzugef�gt, bearbeitet und
  * gel�scht werden.
  */
class ChannelAdministration {
	/** Gibt eine Aufz�hlung der existierenden Channel zur�ck */
    public Enumeration getChannelEnum() {
        return this.channelList.elements();
    }

	/** 
	 * Setzt ChannelList auf die in channelEnum �bergebenen Werte.  
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
	 * Liefert eine Aufz�hlung der existierenden Channel.
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
     * Gibt den Channel mit dem angegebenen Namen zur�ck,
     * falls er existiert. Ansonsten wird null zur�ckgegeben. 
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
     * @param paramChannel das Channelobjekt, das gel�scht werden soll
     */
    public synchronized void removeFromChannelList(Channel paramChannel) {
        if (this.channelList.removeElement(paramChannel)) {
            paramChannel.removeYou();
        }
    }

    /** 
     * F�gt einen Channel hinzu, sofern er noch nicht existiert 
     * @param paramChannel das Channelobjekt, das hinzugef�gt werden soll
     */
    public  synchronized void addToChannelList(Channel paramChannel) {
        if (!this.channelList.contains(paramChannel)) {
            this.channelList.addElement(paramChannel);
        }
    }

    /**
     * Editiert den Channel mit dem angegebenen Namen mit einem neuen Datensatz aus einem neuen Channelobjekt
     * benutzt Channel.setName(),setAllowedForGuest() und setAllowedUserList()
     * @param name Der Name des Channels, der ver�ndert werden soll
     * @param newChannel neues Channelobjekt, das die neuen Daten des Channels enth�lt
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
	 * liefert eine Aufz�hlung der Channel, die f�r G�ste freigegeben sind
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
