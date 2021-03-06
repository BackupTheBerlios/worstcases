
package Util.Commands;

import Client.Client;


/**
 * Wird von einem ClientServant gesendet, falls ein joinChannel() Aufruf
 * fehlschl�gt, also falls es nicht m�glich ist, den Channel zu betreten, z.Bspl.
 * wenn der Benutzer den Channel nicht betreten darf.
 */
public class JoinChannelErrorCommand implements Command {

  /** F�hrt beim Client joinChannelError() auf. */
  public void execute(Object target) {

    if (target instanceof Client) {
      ((Client) target).joinChannelError();
    }
  }
}
