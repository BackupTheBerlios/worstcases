
package Util.Commands;

import Client.Client;

/** Allgemeine Fehlermeldung an den Client
  * Ruft beim Client displayError() auf
	*/
public class ErrorCommand implements Command {
    public ErrorCommand(String paramMsg) {
        this.msg = paramMsg;
    }

    String msg = new String();

    public void execute(Object target) {
        if (target instanceof Client) {
            ((Client)target).displayError(msg);
        }
    }
}
