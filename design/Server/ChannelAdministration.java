package Server;

import java.util.Vector;
import java.util.Enumeration;

/** Verwaltet die Channelobjekte. */
class ChannelAdministration {
    public ChannelAdministration(DataBaseIO paramDataBaseIO) { }

    /** gibt den Channel mit dem angegebenen Namen zurück */
    public Channel getFromChannelListByName(String name) {
        Enumeration enum = this.channelList.elements();
        Channel tmpChannel;
        while (enum.hasMoreElements()) {
            tmpChannel = (Channel)(enum.nextElement());
            if (tmpChannel.getName().compareTo(name) == 0) {
                return tmpChannel;
            }
        }
        return null;
    }

    /** Fügt einen Channel hinzu */
    public void addToChannelList(Channel paramChannel) { }

    /** Entfernt einen Channel. */
    public void removeFromChannelList(Channel paramChannel) {
        int pos = 0;
        User tmpUser = (User)(paramChannel.getCurrentUserList().elementAt(pos));
        tmpUser.getClientServant().leaveChannel();
    }

    public void setChannelList(java.util.Vector channelList) {
        this.channelList = channelList;
    }

    public java.util.Vector getChannelList() {
        return channelList;
    }

    /**
     * Editiert den Channel mit dem angegebenen Namen mit einem neuen Datensatz aus einem String
     * @param name Der Name des Channels, der verändert werden soll
     * @param newChannelData String, der die neuen Daten des Channels enthält
     */
    public void editChannel(String name, String newChannelData) { }

    public Vector getFreeForGuestList() {
        Vector tmpVector = new Vector();
        Channel tmpChannel;
        Enumeration enum = this.getChannelList().elements();
        while (enum.hasMoreElements()) {
            tmpChannel = (Channel)enum.nextElement();
            if (tmpChannel.isAllowedForGuest()) {
                tmpVector.addElement(tmpChannel);
            }
        }
        return tmpVector;
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
}
