package Util.Commands;

import Client.Client;

/**
* Wird von einem ClientServant an einen Client geschickt,
* falls eine neue Nachricht in dem besuchten Channel
* gesendet wurde, also ein Benutzer etwas für alle anderen
* Benutzer in dem Channel geschrieben hat. Dazu wird die 
* Methode sendMsgFromChannel der Klasse Client be-
* nutzt.
*/



public class SendMsgFromChannelCommand implements Command {
    /**

   /** Setzt die Nachricht (paramMsg)  und den Absender (paramFromName).
     */
    public SendMsgFromChannelCommand(String paramFromName,String paramMsg) {
        this.msg = paramMsg;
        this.fromName=paramFromName;
    }

    /** Die Nachricht, die in den Channel gesendet werden soll.- */
    String msg;

    /** Der Name des Benutzers, der diese Nachricht gesendet hat.*/
    String fromName;

    /** Führt beim Client sendMsgFromChannel() aus*/
    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).sendMsgFromChannel(fromName,msg);
        }
    }
}
