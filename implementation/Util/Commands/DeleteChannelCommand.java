package Util.Commands;

import Server.AdminClientServant;


/**
 * Wird von einem AdminClient erzeugt, uim einen Channel
 *    zu l�schen, indem die Methode  deleteChannel() der
 *   Klasse AdminClientServant ausgef�hrt wird.
 */
public class DeleteChannelCommand implements Command {

  /** Setzt den Namen des Channels, der gel�scht werden soll. */
  public DeleteChannelCommand(String paramName) {
    this.name = paramName;
  }

  /** Der Name des Channels, der gel�scht werden soll. */
  String name;

  /** Ruft beim AdminClientServant die Methode deleteChannel() auf. */
  public void execute(Object target) {

    if (target instanceof AdminClientServant) {
      ((AdminClientServant) target).deleteChannel(name);
    }
  }
}
