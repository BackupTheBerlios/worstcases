package Util.Commands;

import Server.ClientServant;


/**
* wird von einem Client gesendet,
* sendet eine Nachricht in den besuchten Channel
*/

public class SendMsgToChannelCommand implements Command {
    public SendMsgToChannelCommand(String paramMsg) {
      this.msg=paramMsg;
    }

    /**die Nachricht*/
    String msg;

    /** ruft beim ClientServant sendMsgToChannel() auf*/
    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).sendMsgToChannel(msg);
        } // XXX: else Exception auslösen?
    }
}
