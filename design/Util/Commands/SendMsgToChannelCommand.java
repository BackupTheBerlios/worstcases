package Util.Commands;

import Server.ClientServant;

public class SendMsgToChannelCommand implements Command {
    /**
     * Konstruktor. Erzeugt einen Befehl, mit dem eine Nachricht an einen
     * Channel gesendet werden kann. Dieser Befehl kann nur von einem ClientServant verarbeitet werden.
     * @param msg die Nachricht.
     */
    public SendMsgToChannelCommand(String paramMsg) {
      this.msg=paramMsg;
    }

    /** Der Benutzername. */
    String msg;

    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).sendMsgToChannel(msg);
        } // XXX: else Exception auslösen?
    }
}
