package Util.Commands;

import Server.ClientServant;
import Client.Client;

public class SendMsgFromChannelCommand implements Command {
    /**
     * Konstruktor. Erzeugt einen Befehl, mit dem eine Nachricht an einen
     * Channel gesendet werden kann. Dieser Befehl kann nur von einem ClientServant verarbeitet werden.
     * @param msg die Nachricht.
     */
    public SendMsgFromChannelCommand(String fromName,String paramMsg) {
        this.name=fromName;
        this.msg = paramMsg;
    }

    /** Der Benutzername. */
    String msg;
    String name;

    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).sendMsgFromChannel(name,msg);
        } // XXX: else Exception ausl�sen?
    }
}
