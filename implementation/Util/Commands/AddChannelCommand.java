package Util.Commands;

import java.util.Vector;
import Server.AdminClientServant;

/**
 * Ein AddChannelCommand wird von einem AdminClient erzeugt und hat die 
 * Funktion, einen neuen Channel anzulegen, indem addChannel beim
 * AdminClientServant aufgerufen wird.
 */

public class AddChannelCommand implements Command {

    /** Dieser Konstruktor setzt die Attribute. paramName ist der Name des anzulegenden Channels,
        * paramAllowedForGuests sagt, ob auch Gäste in dem Raum zugelassen sein können und
        * der paramAllowedUserNames enthält die Liste der für diesen Channel berechtigten Be-
        * nutzer.
        */

    public AddChannelCommand(String paramName, boolean paramAllowedForGuests, Vector paramAllowedUserNames) {
        this.name = paramName;
        this.allowedForGuests=paramAllowedForGuests;
        this.allowedUserNames = paramAllowedUserNames;
    }
    /**Name des Channels.*/
    private String name;

    /**Channel auch für Gäste, oder nicht für Gäste. Hier zuerst nicht für Gäste, da false.*/

    private boolean allowedForGuests = false;

    /**Liste der Namen der User, die den Channel betreten dürfen*/
    private Vector allowedUserNames;


    /** Führt die Methode adminClientServant.addChannel() mit den Attributen des 
       * Commands aus.
       */

    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).addChannel(name, allowedForGuests, allowedUserNames);
        }
    }
}
