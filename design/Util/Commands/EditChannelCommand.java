package Util.Commands;

import java.util.Vector;
import Server.AdminClientServant;

/**
 * Wird von einem AdminClient erzeugt,
 * ändert Channeldaten und
 * ruft editChannel beim AdminClientServant auf.
 */
public class EditChannelCommand implements Command {

    /**Setzt die Attribute.*/
    public EditChannelCommand(String paramOldName, String paramName, boolean paramAllowedForGuests, Vector paramAllowedUserNames) {
        this.oldName = paramOldName;
        this.name = paramName;
        this.allowedForGuests=paramAllowedForGuests;
        this.allowedUserNames = paramAllowedUserNames;
    }

    /**Der alte Name des Channels.*/
    private String oldName;

    /**Name des Channels.*/
    private String name;

    /**Channel öffentlich oder nicht öffentlich?*/
    private boolean allowedForGuests = false;

    /**Liste der Namen der User, die den Channel betreten dürfen.*/
    private Vector allowedUserNames;

    /**Führt adminClientServant.editChannel() mit den Attributen des Commands aus.*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).editChannel(oldName, name, allowedForGuests, allowedUserNames);
        }
    }
}
