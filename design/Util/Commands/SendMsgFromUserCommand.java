package Util.Commands;

import Server.ClientServant;
import Client.Client;

/**
* gesendet von ClientServant, falls ein Benutzer eine Privatnachricht
* verschicken m�chte
*/


public class SendMsgFromUserCommand implements Command {
    /**
     * setzt die Attribute
     */
    public SendMsgFromUserCommand(String fromName,String paramMsg) {
        this.name=fromName;
        this.msg = paramMsg;
    }

    /** die Nachricht*/
    String msg;
    /** Der Benutzername des Absenders. */
    String name;

    /**f�hrt beim Client sendMsgFromUser() auf*/
    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).sendMsgFromUser(name,msg);
        } // XXX: else Exception ausl�sen?
    }
}
