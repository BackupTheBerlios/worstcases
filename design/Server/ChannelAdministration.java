package Server;

import java.util.Vector;
import java.util.Enumeration;

/** Verwaltet die Channelobjekte. */
class ChannelAdministration {
    /** gibt den Channel mit dem angegebenen Namen zurück */
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

    public Vector getChannelNames(){
      Vector tmpVector=new Vector();
      Enumeration enum=this.getChannelEnum();
      Channel tmpChannel;
      while(enum.hasMoreElements()){
        tmpChannel=(Channel)enum.nextElement();
        tmpVector.addElement(tmpChannel.getName());
      }
      return tmpVector;


    }


    /** Entfernt einen Channel. */
    public void removeFromChannelList(Channel paramChannel) {
        if (this.channelList.removeElement(paramChannel)) {
            paramChannel.removeYou();
            try {
                this.dataBaseIO.saveToDisk();
            }
            catch (java.io.IOException e) {
                System.out.println(e);
            }
        }
    }

    public void setChannelList(Enumeration channelEnum) {
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

    /** Fügt einen Channel hinzu */
    public void addToChannelList(Channel paramChannel) {
        if (!this.channelList.contains(paramChannel)) {
            this.channelList.addElement(paramChannel);
            try {
                this.dataBaseIO.saveToDisk();
            }
            catch (java.io.IOException e) {
                System.out.println(e);
            }
        }
    }

    public Enumeration getChannelEnum() {
        return this.channelList.elements();
    }

    /**
     * Editiert den Channel mit dem angegebenen Namen mit einem neuen Datensatz aus einem String
     * @param name Der Name des Channels, der verändert werden soll
     * @param newChannelData String, der die neuen Daten des Channels enthält
     */
    public void editChannel(String oldName, Channel newChannel) {
        Channel tmpChannel = this.getFromChannelListByName(oldName);
        if (tmpChannel != null) {
            tmpChannel.setName(newChannel.getName());
            tmpChannel.setAllowedForGuest(newChannel.isAllowedForGuest());
            tmpChannel.setAllowedUserList(newChannel.getAllowedUserEnum());
            try {
                this.dataBaseIO.saveToDisk();
            }
            catch (java.io.IOException e) {
                System.out.println(e);
            }
        }
    }

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
    private Vector channelList;

    /**
     * @supplierCardinality 1
     * @clientCardinality 1
     */
    private DataBaseIO dataBaseIO;

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
