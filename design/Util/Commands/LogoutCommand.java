package Util.Commands;

import Server.ClientServant;

/**
* fordert den ClientServant auf, einen Logout auszuführen
*/
public class LogoutCommand implements Command {
    /**führt beim ClientServant setUser(null) aus*/
    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).setUser(null);
        }
    }
}
