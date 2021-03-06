/* Generated by Together */

package Server;

import java.util.Vector;
/**
 * Verwaltet die Channelobjekte.
 */
class ChannelAdministration {
    public ChannelAdministration(DataBaseIO paramDataBaseIO) {
    }

    /**
     * gibt den Channel mit dem angegebenen Namen zur�ck
     */
    public Channel getByName(String name) {
        return null;
    }

    /**
     * F�gt einen Channel hinzu
     */
    public void addToChannelList(Channel paramChannel) {
    }

    /**
     * Entfernt einen Channel.
     */
    public void removeFromChannelList(Channel paramChannel) {
     int pos=0;
     User tmpUser=(User)(paramChannel.getCurrentUserList().elementAt(pos));
     tmpUser.getClientServant().leaveChannel();
    }

    public void setChannelList(java.util.Vector channelList){
        this.channelList = channelList;
    }

    public java.util.Vector getChannelList(){
        return channelList;
    }

    /**
     * Editiert den Channel mit dem angegebenen Namen mit einem neuen Datensatz aus einem String
     * @param name Der Name des Channels, der ver�ndert werden soll
     * @param newChannelData String, der die neuen Daten des Channels enth�lt
     */
    public void editChannel(String name, String newChannelData) {

     
    }

    /**
     *@link aggregation
     *     @associates <{Channel}>
     * @clientCardinality 1
     * @supplierCardinality 0..*
     */
    private Vector channelList;

    /**
     * @supplierCardinality 1
     * @clientCardinality 1 
     */
    private DataBaseIO dataBaseIO;
}
