package Util.Commands;

import java.util.Vector;
import Server.AdminClientServant;

/**
 * Wird von einem AdminClient erzeugt,
 * legt einen neuen Channel an und
 * ruft addChannel beim AdminClientServant auf.
 */
public class AddChannelCommand implements Command {

    /**Setzt die Attribute.*/
    public AddChannelCommand(String paramName, boolean paramAllowedForGuests, Vector paramAllowedUserNames) {
        this.name = paramName;
        this.allowedForGuests=paramAllowedForGuests;
        this.allowedUserNames = paramAllowedUserNames;
    }
    /**Name des Channels.*/
    private String name;
    /**Channel öffentlich oder nicht öffentlichn*/
    private boolean allowedForGuests = false;
    /**Liste der Namen der User, die den Channel betreten dürfenn*/
    private Vector allowedUserNames;

    /**Führt adminClientServant.addChannel() mit den Attributen des Commands aus.*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).addChannel(name, allowedForGuests, allowedUserNames);
        }
    }
}
