/* Generated by Together */

package Util.Commands;

import Client.Client;


/**
* wird von einem ClientServant gesendet, falls ein Login fehlschlägt
*/
public class LoginErrorCommand implements Command {

    /**ruft beim Client loginError() auf*/
    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).loginError();
        } // XXX: else Exception auslösen?
    }
}
