package Util.Commands;

import Client.AdminClient;
import java.util.Vector;


/**
 * Wird von einem AdminClientServant als Antwort auf ein GetChannelDataRequestCommand()
 * gesendet, enthält die Channeldaten des entsprechenden Channels. Ruft beim AdminClient
 * setChannelDataRequest() auf
 */
public class SetChannelDataCommand implements Command {

  /**
   * Setzt die entsprechenden Attribute. Der Name des Channels (paramChannelName, Ist der
   *   Channel für Gäste zugänglich? (paramIsAllowedForGuest), Liste der Benutzer, die den
   *   Channel betreten dürfen (paramUserNames).
   */
  public SetChannelDataCommand(String paramChannelName,
                               boolean paramIsAllowedForGuest,
                               Vector paramUserNames) {

    this.channelName = paramChannelName;
    this.isAllowedForGuest = paramIsAllowedForGuest;
    this.userNames = paramUserNames;
  }

  /** Der Channelname. */
  String channelName;

  /** Gäste zugelassen? */
  boolean isAllowedForGuest = false;

  /** Namen der Benutzer, die den Channel betreten dürfen. */
  Vector userNames;

  /** Führt setChannelData beim AdminClient aus. */
  public void execute(Object target) {

    if (target instanceof AdminClient) {
      ((AdminClient) target).setChannelData(channelName, isAllowedForGuest,
                                            userNames);
    }
  }
}
