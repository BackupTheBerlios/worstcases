package Util.Commands;

import Server.ClientServant;
import Client.Client;

/**
* Gesendet von ClientServant, falls ein Benutzer eine Privatnachricht
* verschicken m�chte.
*/


public class SendMsgFromUserCommand implements Command {
    /**
     * Setzt die Attribute.
     */
    public SendMsgFromUserCommand(String fromName,String paramMsg) {
        this.name=fromName;
        this.msg = paramMsg;
    }

    /** Die Nachricht.*/
    String msg;
    /** Der Benutzername des Absenders. */
    String name;

    /**F�hrt beim Client sendMsgFromUser() auf.*/
    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).sendMsgFromUser(name,msg);
        } // XXX: else Exception ausl�sen?
    }
}
