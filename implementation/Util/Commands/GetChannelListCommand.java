package Util.Commands;

import Client.AdminClient;
import Server.AdminClientServant;

/**
* Fordert vom AdminClientServant die komplette Channelliste an, indem
* die Methode sendChannelList der Klasse AdminClientServant ausge-
* f�hrt wird. Parameter sind nicht erforderlich, da die komplette Channel-
* liste angefordert wird.
*/

public class GetChannelListCommand implements Command {

    /**F�hrt AdminClientServant.sendChannelList() aus.*/

    public void execute(Object target) {
        if (target instanceof AdminClientServant) {
            ((AdminClientServant)target).sendChannelList();
        }
    }
}
