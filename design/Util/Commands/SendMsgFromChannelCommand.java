package Util.Commands;

import Client.Client;

/**
* wird von einem ClientServant an einen Client geschickt,
* falls eine neue Nachricht in dem besuchten Channel
* gesendet wurde
*/



public class SendMsgFromChannelCommand implements Command {
    /**
     * setzt die Nachricht und den Absender
     */
    public SendMsgFromChannelCommand(String paramFromName,String paramMsg) {
        this.msg = paramMsg;
        this.fromName=paramFromName;
    }

    /** Die Nachricht */
    String msg;
    /** der Name des Benutzers, der diese Nachricht gesendet hat*/
    String fromName;

    /** führt beim Client sendMsgFromChannel() auf*/
    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).sendMsgFromChannel(fromName,msg);
        } // XXX: else Exception auslösen?
    }
}
