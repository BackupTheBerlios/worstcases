
package Util.Commands;

import Client.Client;


/**
 * Dieses Command dient dazu, einen Client zu stoppen, also die Verbindung zu unter-
 *   brechen. Dazu wird die Methode stopClient der Klasse Client verwendet.
 */
public class StopClientCommand implements Command {

  /** Ruft beim Client stopClient() auf */
  public void execute(Object target) {

    if (target instanceof Client) {
      ((Client) target).stopClient();
    }
  }
}
