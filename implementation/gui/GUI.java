package gui;
import java.util.Vector;
public interface GUI{

    public void setCurrentUserData(String userName, boolean isAdmin,Vector channelNames);
    public void setCurrentChannelData(String channelName, Vector userNames);
    public void sendMsgFromChannel(String fromName, String msg);
    public void sendMsgFromUser(String fromName, String msg);
    public  void loginError();

    public void setUserList(Vector list) ;
        public void setChannelList(Vector list);
}
