package Util.Commands;

import Server.ClientServant;


/**
* Wird von einem Client gesendet, und stellt die Nachricht, mit Hilfe der
* Methode sendMsgToChannel in den gew�nschten Channel.
*/

public class SendMsgToChannelCommand implements Command {

/** Als Parameter wird hier die zu sendende Nachricht an den Konstruktor �bergeben.
   */

    public SendMsgToChannelCommand(String paramMsg) {
      this.msg=paramMsg;
    }

    /**Die Nachricht, die in den Channel gesendet werden soll. 
        */
    String msg;

    /** Ruft beim ClientServant die Methode sendMsgToChannel() auf.
       */
    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).sendMsgToChannel(msg);
        } 
    }
}
