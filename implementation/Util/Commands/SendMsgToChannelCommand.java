package Util.Commands;

import Server.ClientServant;


/**
* Wird von einem Client gesendet,
* sendet eine Nachricht in den besuchten Channel.
*/

public class SendMsgToChannelCommand implements Command {
    public SendMsgToChannelCommand(String paramMsg) {
      this.msg=paramMsg;
    }

    /**Die Nachricht.*/
    String msg;

    /** Ruft beim ClientServant sendMsgToChannel() auf.*/
    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).sendMsgToChannel(msg);
        } // XXX: else Exception auslösen?
    }
}
