/* Generated by Together */

package Util.Commands;

import Client.Client;

/**
* Wird von einem ClientServant gesendet, falls ein joinChannel() Aufruf
* fehlschl�gt.
*/

public class JoinChannelErrorCommand implements Command {
    /**F�hrt beim Client joinChannelError() auf.*/
    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).joinChannelError();
        } // XXX: else Exception ausl�sen?
    }
}