package Util.Commands;

import java.util.Vector;
import Server.AdminClientServant;


/**
 * Wird von einem AdminClient erzeugt, um die Daten eines Channels
 * zu ändern, indem die Methode editChannel beim AdminClientServant
 * aufgerufen wird.
 */
public class EditChannelCommand implements Command {

  /**
   * Setzt die neuen Attribute, die der Channel bekommen soll. Dabei ist OldName der alte
   *    Name des Channels und dient somit zur Identifizierung des Channels. Es kann der
   *    Channelname verändert werden, der Channel auch für Gäste geöffnet werden und die
   *    Liste der berechtigten Benutzer aktualisiert werden.
   */
  public EditChannelCommand(String paramOldName, String paramName,
                            boolean paramAllowedForGuests,
                            Vector paramAllowedUserNames) {

    this.oldName = paramOldName;
    this.name = paramName;
    this.allowedForGuests = paramAllowedForGuests;
    this.allowedUserNames = paramAllowedUserNames;
  }

  /**
   * Der alte Name des Channels, damit auch der richtige Channel bearbeitet wird.
   */
  private String oldName;

  /** Neuer Name des Channels. */
  private String name;

  /** Channel öffentlich oder nicht öffentlich? */
  private boolean allowedForGuests = false;

  /** Aktualisierte Liste der Namen der User, die den Channel betreten dürfen. */
  private Vector allowedUserNames;

  /** Führt adminClientServant.editChannel() mit den Attributen des Commands aus. */
  public void execute(Object target) {

    if (target instanceof AdminClientServant) {
      ((AdminClientServant) target).editChannel(oldName, name,
                                                allowedForGuests,
                                                allowedUserNames);
    }
  }
}
