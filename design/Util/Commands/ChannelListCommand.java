package Util.Commands;

import Client.AdminClient;
import java.util.Vector;

public class ChannelListCommand implements Command {
    /**
     * Konstruktor. Erzeugt einen Login-Befehl mit den zur Anmeldung eines
     * Users am Server benötigten Daten. Dieser Befehl kann nur von einem ClientServant verarbeitet werden.
     * @param name der Benutzername.
     * @param password das Kennwort.
     */
    public ChannelListCommand(Vector list) {
        this.channelNames = list;
    }

    Vector channelNames = new Vector();

    public void execute(Object target) {
        if (target instanceof AdminClient) {
            ((AdminClient)target).setChannelList(channelNames);
        } // XXX: else Exception auslösen?
    }
}
