package Util.Commands;

import Server.AdminClientServant;


/**
 * Wird von einem AdminClient erzeugt, uim einen Channel
 *    zu löschen, indem die Methode  deleteChannel() der
 *   Klasse AdminClientServant ausgeführt wird.
 */
public class DeleteChannelCommand implements Command {

  /** Setzt den Namen des Channels, der gelöscht werden soll. */
  public DeleteChannelCommand(String paramName) {
    this.name = paramName;
  }

  /** Der Name des Channels, der gelöscht werden soll. */
  String name;

  /** Ruft beim AdminClientServant die Methode deleteChannel() auf. */
  public void execute(Object target) {

    if (target instanceof AdminClientServant) {
      ((AdminClientServant) target).deleteChannel(name);
    }
  }
}
