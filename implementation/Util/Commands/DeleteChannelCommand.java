package Util.Commands;

import Server.AdminClientServant;

/** Wird von einem AdminClient erzeugt,
 *  l�scht einen Channel, indem deleteChannel() beim
 *  AdminClientServant ausgef�hrt wird.
 */

public class DeleteChannelCommand implements Command {
    /** Setzt den Namen.
     */
    public DeleteChannelCommand(String paramName) {
        this.name = paramName;
    }

    /** Der Channelname. */
    String name;

    /**Ruft beim AdminClientServant deleteChannel() auf.*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).deleteChannel(name);
        }
    }
}
