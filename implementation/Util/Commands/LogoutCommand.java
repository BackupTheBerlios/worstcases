package Util.Commands;

import Server.ClientServant;

/**
* Dieses Command fordert den ClientServant auf, einen Logout auszuführen.
* Dazu wird die Methode setUser benutzt. 
*/

public class LogoutCommand implements Command {
    /** Führt beim ClientServant setUser(null) aus. Der Parameter "null"
        * sagt aus, das der Benutzer komplett beim System abgemeldet
        * werden soll.
        */
    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).setUser(null);
        }
    }
}
