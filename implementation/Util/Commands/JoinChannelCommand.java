package Util.Commands;

import Server.ClientServant;

/**
* Wird von einem Client gesendet,
* user betritt damit einen Channel.
*/

public class JoinChannelCommand implements Command {
    /**
     * Setzt den Channelnamen.
     */
    public JoinChannelCommand(String paramName) {
      this.name=paramName;
    }

    /** Der Channel-Name. */
    String name;

    /**Ruft beim ClientServant joinChannel() auf.*/

    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).joinChannel(name);
        } // XXX: else Exception auslösen?
    }
}
