package Util.Commands;

import Server.AdminClientServant;

/** Wird von einem AdminClient erzeugt.
 *  L�scht einen Channel, indem deleteChannel() beim
 *  AdminClientServant ausgef�hrt wird
 */

public class DeleteChannelCommand implements Command {
    /** setzt den Namen
     */
    public DeleteChannelCommand(String paramName) {
        this.name = paramName;
    }

    /** Der Channelname. */
    String name;

    /**ruft beim AdminClientServant deleteChannel() auf*/
    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).deleteChannel(name);
        }
    }
}
