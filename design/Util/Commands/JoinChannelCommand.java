package Util.Commands;

import Server.ClientServant;

/**
* wird von einem Client gesendet
* betritt einen Channel
*/

public class JoinChannelCommand implements Command {
    /**
     * setzt den Channelnamen
     */
    public JoinChannelCommand(String paramName) {
      this.name=paramName;
    }

    /** Der Channel-Name. */
    String name;

    /**ruft beim ClientServant joinChannel() auf*/

    public void execute(Object target) {
        if (target instanceof ClientServant) {
            ((ClientServant)target).joinChannel(name);
        } // XXX: else Exception auslösen?
    }
}
