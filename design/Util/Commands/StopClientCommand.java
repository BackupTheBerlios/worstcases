/* Generated by Together */

package Util.Commands;

import Client.Client;

public class StopClientCommand implements Command {

    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).stopClient();
        } // XXX: else Exception ausl�sen?
    }
}
