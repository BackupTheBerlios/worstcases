
package Util.Commands;

import Client.Client;


/**
 * Wird von einem ClientServant gesendet, falls ein Login fehlschlägt,
 * wenn z.Bspl. zu viele Benutzer im System sind, oder wenn die
 * Benutzerdaten falsch sind. Es wird dazu die Methode loginError
 * verwendet.
 */
public class LoginErrorCommand implements Command {

  /** Ruft beim Client loginError() auf. */
  public void execute(Object target) {

    if (target instanceof Client) {
      ((Client) target).loginError();
    }  // XXX: else Exception auslösen?
  }
}
