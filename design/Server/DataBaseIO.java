package Server;

import java.util.Vector;
import java.util.Enumeration;
import java.io.*;
import java.util.StringTokenizer;


/**
 * Stellt Methoden bereit, um die Benutzer- und Channeldatenbank zu laden
 * und zu speichern. Außerdem wird dafür gesorgt, daß für den Betrieb die
 * relationalen Beziehungen zwischen User- und Channeldatenbank gesetzt werden.
 */
class DataBaseIO {

  /**
   * Lädt die Benutzer- und Channeldaten aus userDBFile und channelDBFile.
   */

  private String userToString(User paramUser){
   String tmpString=paramUser.getName()+"#"+paramUser.getPassword()+"#"+paramUser.isAdmin();
   Enumeration enum=paramUser.getAllowedChannelList().elements();
   while(enum.hasMoreElements()){
    tmpString=tmpString+"#"+((Channel)(enum.nextElement())).getName();
   }
   return tmpString;

  }


  private User stringToUser(String userSet){
    StringTokenizer tmpTokenizer=new StringTokenizer(userSet,"#",false);
    String name=tmpTokenizer.nextToken();
    String password=tmpTokenizer.nextToken();
    boolean isAdmin=(new Boolean(tmpTokenizer.nextToken())).booleanValue();
    Vector allowedChannelStringList=new Vector();

    while(tmpTokenizer.hasMoreTokens()){
      allowedChannelStringList.addElement(tmpTokenizer.nextToken());
    }
    return (new User(name,password,false,isAdmin,allowedChannelStringList));
  }

    private String channelToString(Channel paramChannel){
   String tmpString=paramChannel.getName()+"#"+paramChannel.isAllowedForGuest();
   Enumeration enum=paramChannel.getAllowedUserList().elements();
   while(enum.hasMoreElements()){
    tmpString=tmpString+"#"+((User)(enum.nextElement())).getName();
   }
   return tmpString;

  }



    private Channel stringToChannel(String channelSet){
    StringTokenizer tmpTokenizer=new StringTokenizer(channelSet,"#",false);
    String name=tmpTokenizer.nextToken();
    boolean allowedForGuests=(new Boolean(tmpTokenizer.nextToken())).booleanValue();
    Vector allowedUserStringList=new Vector();
    while(tmpTokenizer.hasMoreTokens()){
      allowedUserStringList.addElement(tmpTokenizer.nextToken());
    }
    return (new Channel(name,allowedForGuests,allowedUserStringList));
  }



  public void loadFromDisk() throws java.io.FileNotFoundException,java.io.IOException{
    String tmpString;
    Vector tmpList=new Vector();
    BufferedReader tmpBufferedReader = new BufferedReader(new FileReader(new File(this.userDBFile)));
    tmpString=tmpBufferedReader.readLine();
    while(tmpString!=null){
    tmpList.addElement(this.stringToUser(tmpString));
    tmpString=tmpBufferedReader.readLine();

    }
    this.userAdministration.setUserList(tmpList);
    tmpBufferedReader.close();


    tmpList=new Vector();
    tmpBufferedReader = new BufferedReader(new FileReader(new File(this.channelDBFile)));
    tmpString=tmpBufferedReader.readLine();
    while(tmpString!=null){
    tmpList.addElement(this.stringToChannel(tmpString));
    tmpString=tmpBufferedReader.readLine();
    }
    this.channelAdministration.setChannelList(tmpList);
    tmpBufferedReader.close();

    this.doLinks();
    System.out.println("loaded channel:"+this.channelAdministration.getChannelList());
    System.out.println("loeaded user:"+this.userAdministration.getUserList());

  }

  /**
   * Speichert die Benutzer- und Channeldaten in userDBFile und channelDBFile.
   */
  public void saveToDisk() throws java.io.IOException{
   BufferedWriter tmpBufferedWriter=new BufferedWriter(new FileWriter(new File(this.channelDBFile)));
   Enumeration enum = this.channelAdministration.getChannelList().elements();
   while(enum.hasMoreElements()){
    tmpBufferedWriter.write(this.channelToString((Channel)(enum.nextElement()))+"\r\n");
   }
    tmpBufferedWriter.close();
    System.out.println("channel data written to disk");

   tmpBufferedWriter=new BufferedWriter(new FileWriter(new File(this.userDBFile)));
   enum = this.userAdministration.getUserList().elements();
   while(enum.hasMoreElements()){
    tmpBufferedWriter.write(this.userToString((User)(enum.nextElement()))+"\r\n");
   }
    tmpBufferedWriter.close();
    System.out.println("user data written to disk");



  }

  /**
   * Stellt die relationalen Beziehungen zwische User- und Channeldatensätzen her.
   */
  private void doLinks() {
    Enumeration userEnum=this.userAdministration.getUserList().elements();
    Enumeration channelNameEnum;
    User tmpUser;
    String tmpChannelName;
    Channel tmpChannel;
    while(userEnum.hasMoreElements()){
      tmpUser = (User)userEnum.nextElement();
      channelNameEnum=tmpUser.getAllowedChannelStringList().elements();
      while(channelNameEnum.hasMoreElements()){
       tmpChannel=this.channelAdministration.getFromChannelListByName((String)channelNameEnum.nextElement());
       tmpChannel.getAllowedUserList().addElement(tmpUser);
       tmpUser.getAllowedChannelList().addElement(tmpChannel);






      }


    }
  }

  public ChannelAdministration getChannelAdministration() {
    return channelAdministration;
  }

  public void setChannelAdministration(
          ChannelAdministration channelAdministration) {
    this.channelAdministration = channelAdministration;
  }

  public UserAdministration getUserAdministration() {
    return userAdministration;
  }

  public void setUserAdministration(UserAdministration userAdministration) {
    this.userAdministration = userAdministration;
  }

  /**
   * @clientCardinality 1
   * @supplierCardinality 1
   */
  private UserAdministration userAdministration;

  /**
   * @clientCardinality 1
   * @supplierCardinality 1
   */
  private ChannelAdministration channelAdministration;

  /**
   * Dateiname der Channeldatenbank.
   */
  private final static String channelDBFile = "channel.db";

  /**
   * Dateiname der Benutzerdatenbank.
   */
  private final static String userDBFile = "user.db";
}
