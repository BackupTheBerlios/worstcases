/* Generated by Together */

package Util.Commands;

import Client.Client;


/**
* Wird von einem ClientServant gesendet, falls ein Login fehlschl�gt.
*/
public class LoginErrorCommand implements Command {

    /**Ruft beim Client loginError() auf.*/
    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).loginError();
        } // XXX: else Exception ausl�sen?
    }
}