package Util.Commands;

import Server.ClientServant;

/**
* Fordert den ClientServant auf, durch Benutzung der Methode 
* leaveChannel den betretenen Channel zu verlassen.
*/

public class LeaveChannelCommand implements Command {

    /**Führt ClientServant.leaveChannel() aus.*/

    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).leaveChannel();
        }
    }
}
