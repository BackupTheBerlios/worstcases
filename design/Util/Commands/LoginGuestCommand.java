package Util.Commands;

import Server.ClientServant;

/**
* Meldet einen Gast an, wird von einem Client gesendet.
*/


public class LoginGuestCommand implements Command {
    /**
     * Konstruktor. Erzeugt einen Login-Befehl mit den zur Anmeldung eines
     * Users am Server ben�tigten Daten. Dieser Befehl kann nur von einem ClientServant verarbeitet werden.
     * @param name der Benutzername.
     */
    public LoginGuestCommand(String paramName) {
        this.name = paramName;
    }

    /** Der Benutzername. */
    String name;


    /**Ruft beim ClientServant loginAsGuest() auf.*/
    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).loginAsGuest(name);
        } // XXX: else Exception ausl�sen?
    }
}
