package Server;

import java.util.Vector;
import java.util.Enumeration;

/** Ein Benutzerdatensatz */
public class User {
    /** @param userSet Benutzerdaten als String */
    public User(String paramName, String paramPassword, boolean paramGuest, boolean paramAdmin,
        UserAdministration paramUserAdministration) {
            this.name = paramName;
            this.password = paramPassword;
            this.isAdmin = paramAdmin;
            this.isGuest = paramGuest;
            this.setUserAdministration(paramUserAdministration);
    }

    private UserAdministration userAdministration;

    public void setUserAdministration(UserAdministration paramUserAdministration) {
        if (this.userAdministration != paramUserAdministration) {
            if (this.userAdministration != null) {
                UserAdministration old = this.userAdministration;
                this.userAdministration = null;
                old.removeFromUserList(this);
            }
            this.userAdministration = paramUserAdministration;
            if (paramUserAdministration != null) {
                paramUserAdministration.addToUserList(this);
            }
        }
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void removeYou() {
        this.setLoggedIn(false);
        this.setClientServant(null);
        this.setAllowedChannelList(null);
        if (this.userAdministration != null) {
            this.userAdministration.removeFromUserList(this);
        }
    }

    public void setLoggedIn(boolean paramLoggedIn) {
        if (this.isLoggedIn() != paramLoggedIn) {
            boolean old = this.loggedIn;
            this.loggedIn = paramLoggedIn;
            if (paramLoggedIn) {
                this.userAdministration.incNumCurrentUsers();
            }
            else {
                this.setCurrentChannel(null);
                this.setClientServant(null);
                this.userAdministration.decNumCurrentUsers();
                if (this.isGuest()) {
                    this.userAdministration.removeFromUserList(this);
                }
            }
        }
    }

    public Channel getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(Channel paramChannel) {
        if (this.currentChannel != paramChannel) {
            if (this.currentChannel != null) {
                Channel old = this.currentChannel;
                this.currentChannel = null;
                old.removeFromCurrentUserList(this);
            }
            this.currentChannel = paramChannel;
            if (paramChannel != null) {
                paramChannel.addToCurrentUserList(this);
            }
        }
    }

    /** gibt einen Channel aus der Liste der erlaubten Channels mit dem angegebenen Namen zurück */
    public Channel getFromAllowedChannelByName(String channelName) {
        Enumeration enum = this.getAllowedChannelEnum();
        Channel tmpChannel;
        while (enum.hasMoreElements()) {
            tmpChannel = (Channel)enum.nextElement();
            if (tmpChannel.getName().compareTo(channelName) == 0) {
                return tmpChannel;
            }
        }
        return null;
    }

    public ClientServant getClientServant() {
        return clientServant;
    }

    public void setClientServant(ClientServant paramClientServant) {
        if (this.clientServant != paramClientServant) {
            if (this.clientServant != null) {
                ClientServant old = this.clientServant;
                this.clientServant = null;
                old.setUser(null);
            }
            this.clientServant = paramClientServant;
            if (paramClientServant != null) {
                paramClientServant.setUser(this);
            }
            else{
             this.setLoggedIn(false);
            }
        }
    }

    public String toString() {
        return ("Name:" + this.getName() + " isAdmin:" + this.isAdmin() + " isGuest:" + this.isGuest() + " currentChannel:" +
            this.getCurrentChannel() + " allowedChannelEnum:" + this.getAllowedChannelEnum());
    }

    public boolean isGuest() {
        return isGuest;
    }

    public Enumeration getAllowedChannelEnum() {
        return allowedChannelList.elements();
    }

    public Vector getAllowedChannelNames(){
      Vector tmpVector=new Vector();
      Enumeration enum=this.getAllowedChannelEnum();
      while(enum.hasMoreElements()){
       tmpVector.addElement(((Channel)enum.nextElement()).getName());
      }
      return tmpVector;
    }


    public void setAllowedChannelList(Enumeration channelEnum) {
        Vector tmpChannelList = new Vector();
        Channel tmpChannel;
        while (channelEnum.hasMoreElements()) {
            tmpChannel = (Channel)channelEnum.nextElement();
            tmpChannelList.addElement(tmpChannel);
            this.addToAllowedChannelList(tmpChannel);
        }
        Enumeration enum = tmpChannelList.elements();
        while (enum.hasMoreElements()) {
            tmpChannel = (Channel)enum.nextElement();
            if (!tmpChannelList.contains(tmpChannel)) {
                this.removeFromAllowedChannelList(tmpChannel);
            }
        }
    }

    public void addToAllowedChannelList(Channel paramChannel) {
        if (!this.allowedChannelList.contains(paramChannel)) {
            this.allowedChannelList.addElement(paramChannel);
            if(!this.isGuest()){
            paramChannel.addToAllowedUserList(this);
            }
        }
    }

    public void removeFromAllowedChannelList(Channel paramChannel) {
        if (this.allowedChannelList.removeElement(paramChannel)) {
            if(!this.isGuest()){
            paramChannel.removeFromAllowedUserList(this);
            }
            if(paramChannel==this.currentChannel){
             this.setCurrentChannel(null);
            }
        }
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    /** Gibt den Channel an, in dem sich der Benutzer zur Zeit befindet.
     * @supplierCardinality 0..1
     * @clientCardinality 0.. */
    private Channel currentChannel; /** Gibt die Channels an, die der Benutzer betreten darf.
     * @associates <{Channel}>
     * @clientCardinality 0..
     * @supplierCardinality 0..* */
    private Vector allowedChannelList = new Vector(); private String name; private String password;
         /** Gibt an, ob der Benutzer momentan das System benutzt. */
        private boolean loggedIn = false; /** Ist der User Administrator? */
        private boolean isAdmin = false; /** @clientCardinality 1
         * @supplierCardinality 0..1 */
        private ClientServant clientServant; private boolean isGuest = false;
        private Vector allowedChannelStringList = new Vector();
}
