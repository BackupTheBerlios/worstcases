package Util.Commands;

import Server.ClientServant;

/**
* Fordert den ClientServant auf, den betretenen Channel zu verlassen
*/
public class LeaveChannelCommand implements Command {
    /**f�hrt ClientServant.leaveChannel() aus*/
    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).leaveChannel();
        }
    }
}
