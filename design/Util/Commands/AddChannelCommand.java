package Util.Commands;

import java.util.Vector;
import Server.AdminClientServant;

/**
 * Wird von einem AdminClient erzeugt
 * Legt einen neuen Channel an
 * ruft addChannel beim AdminClientServant auf
 */
public class AddChannelCommand implements Command {

    /**setzt die Attribute*/
    public AddChannelCommand(String paramName, boolean paramAllowedForGuests, Vector paramAllowedUserNames) {
        this.name = paramName;
        this.allowedForGuests=paramAllowedForGuests;
        this.allowedUserNames = paramAllowedUserNames;
    }
    /**name des Channels*/
    private String name;
    /**Channel �ffentlich oder nicht �ffentlich*/
    private boolean allowedForGuests = false;
    /**Liste der Namen der User, die den Channel betreten d�rfen*/
    private Vector allowedUserNames;

    /**f�hrt adminClientServant.addChannel() mit den Attributen des Commands aus*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).addChannel(name, allowedForGuests, allowedUserNames);
        }
    }
}
