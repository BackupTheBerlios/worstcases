package Util.Commands;

import Server.ClientServant;

/**
* Wird von einem Client gesendet, damit ein Benutzer sich im System anmelden
* kann. Dazu wird die Methode loginUser der Klasse ClientServant verwendet.
*/

public class LoginCommand implements Command {

    /**
     * Der Konstruktor erzeugt einen Login-Befehl mit den zur Anmeldung eines
     * Users am Server benötigten Daten. Dieser Befehl kann nur von einem ClientServant 
     * verarbeitet werden.
     * @param name der Benutzername des Benutzers, der sich anmelden möchte.
     * @param password das Kennwort zu dem Benutzernamen
     */

    public LoginCommand(String paramName, String paramPassword) {
        this.name = paramName;
        this.password = paramPassword;
    }

    /** Der Benutzername des sich anmeldenden Benutzers. */

    String name;

    /** Das zum Benutzernamen gehörende Kennwort. */

    String password;

    /**Ruft beim ClientServant loginUser() auf.*/
    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).loginUser(name, password);
        } 
    }
}
