
package Util.Commands;

import java.util.Vector;
import Server.AdminClientServant;


/**
 * Wird von einem AdminClient erzeugt, um Benutzerdaten eines Benutzers zu
 * verändern, indem die Methode editUser der Klasse AdminClientServant
 * ausgeführt wird.
 */
public class EditUserCommand implements Command {

  /**
   * Setzt die neuen Attribute des Benutzers. paramOldName ist der alte Name des Benutzers,
   *   der zur Identifizierung benötigt wird. Es kann der Benutzername, das Passwort, sowie
   *   die Channels, auf die der entsprechende Benutzer Zugriff hat, verändert werden. Außer-
   *   können dem Benutzer Administrationsrechte gegeben, bzw. entzogen werden.
   */
  public EditUserCommand(String paramOldName, String paramName,
                         String paramPassword, boolean paramIsAdmin,
                         Vector paramAllowedChannelNames) {

    this.oldName = paramOldName;
    this.name = paramName;
    this.password = paramPassword;
    this.isAdmin = paramIsAdmin;
    this.allowedChannelNames = paramAllowedChannelNames;
  }

  /** Der alte Name des Users. */
  private String oldName;

  /** Name des Users. */
  private String name;

  /** Passwort des Users. */
  private String password;

  /** Admin-Status des Users. */
  private boolean isAdmin = false;

  /** Liste der Namen der Channels, die der Benutzer betreten darf. */
  private Vector allowedChannelNames;

  /**
   * Führt adminClientServant.editUser() mit den Attributen des Commands aus, also
   *   die Methode editUser der Klasse adminClientServant.
   */
  public void execute(Object target) {

    if (target instanceof AdminClientServant) {
      ((AdminClientServant) target).editUser(oldName, name, password,
                                             isAdmin, allowedChannelNames);
    }
  }
}
