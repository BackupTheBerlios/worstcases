package Util.Commands;

import java.util.Vector;
import Server.AdminClientServant;


/**
 * Wird von einem AdminClient erzeugt, um einen neuen User
 * anzulegen. Dieses Command ruft die Methode addUser
 * der Klasse AdminClientServant auf.
 */
public class AddUserCommand implements Command {

  /**
   * Setzt die Attribute des Users. paramName ist der Benutzername, paramPassword
   *    ist das Passwort des Benutzers. paramIsAdmin sagt aus, ob der Benutzer Ad-
   *   ministratorrechte hat und paramAllowedChannelNames gibt an, welche Channels
   *   dieser Benutzer betreten darf.
   */
  public AddUserCommand(String paramName, String paramPassword,
                        boolean paramIsAdmin,
                        Vector paramAllowedChannelNames) {

    this.name = paramName;
    this.password = paramPassword;
    this.isAdmin = paramIsAdmin;
    this.allowedChannelNames = paramAllowedChannelNames;
  }

  /** Name des neuen Users. */
  private String name;

  /** Passwort des neuen Users. */
  private String password;

  /** Admin-Status des Users. Zuerst nein, da false. */
  private boolean isAdmin = false;

  /** Liste der Namen der Channels, die der neue Benutzer betreten darf. */
  private Vector allowedChannelNames;

  /**
   * Führt die Methode adminClientServant.addUser() mit den Attributen
   *   des Commands aus.
   */
  public void execute(Object target) {

    if (target instanceof AdminClientServant) {
      ((AdminClientServant) target).addUser(name, password, isAdmin,
                                            allowedChannelNames);
    }
  }
}
