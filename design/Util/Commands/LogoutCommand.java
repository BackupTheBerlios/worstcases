package Util.Commands;

import Server.ClientServant;

/**
* fordert den ClientServant auf, einen Logout auszuf�hren
*/
public class LogoutCommand implements Command {
    /**f�hrt beim ClientServant setUser(null) aus*/
    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).setUser(null);
        }
    }
}
