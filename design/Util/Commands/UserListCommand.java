package Util.Commands;

import Client.AdminClient;
import java.util.Vector;

public class UserListCommand implements Command {
    /**
     * Konstruktor. Erzeugt einen Login-Befehl mit den zur Anmeldung eines
     * Users am Server benötigten Daten. Dieser Befehl kann nur von einem ClientServant verarbeitet werden.
     * @param name der Benutzername.
     * @param password das Kennwort.
     */
    public UserListCommand(Vector list) {
        this.userNames = list;
    }

    Vector userNames = new Vector();

    public void execute(Object target) {
        if (target instanceof AdminClient) {
            ((AdminClient)target).setUserList(userNames);
        } // XXX: else Exception auslösen?
    }
}
