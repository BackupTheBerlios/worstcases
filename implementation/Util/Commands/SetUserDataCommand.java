package Util.Commands;

import Server.ClientServant;
import Client.AdminClient;
import java.util.Vector;


/**
 * Setzt bei einem AdminClient Informationen über einen angeforderten User.
 */
public class SetUserDataCommand implements Command {

  /**
   * Setzt die Attribute. Benutzername (paramUserName), Passwort des Benutzers
   * (paramUserPassword), Administrationsrechte? (paramIsAdmin), Liste der
   * Channels, die der Benutzer betreten darf (paramChannelNames).
   */
  public SetUserDataCommand(String paramUserName, String paramUserPassword,
                            boolean paramIsAdmin, Vector paramChannelNames) {

    this.userName = paramUserName;
    this.password = paramUserPassword;
    this.isAdmin = paramIsAdmin;
    this.channelNames = paramChannelNames;
  }

  /** Der Benutzername. */
  String userName;

  /** Das Passwort. */
  String password;

  /** Admin-Status. */
  boolean isAdmin = false;

  /** Namensliste der Channels, die der Benutzer betreten darf. */
  Vector channelNames;

  /** Führt AdminClient.setUserDataRequest() aus. */
  public void execute(Object target) {

    if (target instanceof AdminClient) {
      ((AdminClient) target).setUserData(userName, password, isAdmin,
                                         channelNames);
    }
  }
}
