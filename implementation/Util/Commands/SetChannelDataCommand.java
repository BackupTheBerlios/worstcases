package Util.Commands;

import Client.AdminClient;
import java.util.Vector;


/**
 * Wird von einem AdminClientServant als Antwort auf ein GetChannelDataRequestCommand()
 * gesendet, enth�lt die Channeldaten des entsprechenden Channels. Ruft beim AdminClient
 * setChannelDataRequest() auf
 */
public class SetChannelDataCommand implements Command {

  /**
   * Setzt die entsprechenden Attribute. Der Name des Channels (paramChannelName, Ist der
   *   Channel f�r G�ste zug�nglich? (paramIsAllowedForGuest), Liste der Benutzer, die den
   *   Channel betreten d�rfen (paramUserNames).
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

  /** G�ste zugelassen? */
  boolean isAllowedForGuest = false;

  /** Namen der Benutzer, die den Channel betreten d�rfen. */
  Vector userNames;

  /** F�hrt setChannelData beim AdminClient aus. */
  public void execute(Object target) {

    if (target instanceof AdminClient) {
      ((AdminClient) target).setChannelData(channelName, isAllowedForGuest,
                                            userNames);
    }
  }
}
