package GUI;
import java.util.Vector;
public interface GUI{

    public void setCurrentUserData(String userName, Vector channelNames);
    public void setCurrentChannelData(String channelName, Vector userNames);
    public void sendMsgFromChannel(String fromName, String msg);
    public void sendMsgFromUser(String fromName, String msg);
    public  void loginError();
}
