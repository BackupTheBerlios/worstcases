/* Generated by Together */

package Util.Commands;

import Client.Client;

public class SendMsgToChannelErrorCommand implements Command {

    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).sendMsgToChannelError();
        } // XXX: else Exception ausl�sen?
    }
}
