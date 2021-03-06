package Util.Commands;

import Server.ClientServant;


/**
* Wird von einem Client gesendet,
* sendet eine Nachricht an einen Benutzer.
*/

public class SendMsgToUserCommand implements Command {
    public SendMsgToUserCommand(String paramName,String paramMsg) {
      this.msg=paramMsg;
      this.name=paramName;
    }

    /**Die Nachricht.*/
    String msg;
    
    /**Der Empf�nger.*/
    String name;

    /** Ruft beim ClientServant sendMsgToUser() auf.*/
    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).sendMsgToUser(name,msg);
        } // XXX: else Exception ausl�sen?
    }
}
