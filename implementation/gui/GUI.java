package gui;

import java.util.Vector;

public interface GUI {
    public void setCurrentUserData(String userName, boolean isAdmin, Vector channelNames,String currentChannelName);

    public void setCurrentChannelData(String channelName, Vector userNames);

    public void sendMsgFromChannel(String fromName, String msg);

    public void sendMsgFromUser(String fromName, String msg);

    public void loginError();

    public void setUserList(Vector list);

    public void setChannelList(Vector list);

    public void setUserData(String userName, String password, boolean isAdmin, Vector channelNames, Vector passiveChannelNames);

    public void setChannelData(String channelName, boolean isAllowedForGuest, Vector userNames, Vector passiveUserNames);
}
