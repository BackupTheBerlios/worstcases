package Util.Commands;

import Server.ClientServant;

public class LoginGuestCommand implements Command {
  
  /**
   * Konstruktor. Erzeugt einen Login-Befehl mit den zur Anmeldung eines
   * Gasts am Server benötigten Daten. Dieser Befehl kann nur von einem
   * ClientServant verarbeitet werden.
   *
   * @param name der Benutzername.
   */
  public LoginGuestCommand(String name) {
   this.name = name;
  }

  /** Der Name des Gasts. */
  String name;
   
  public void execute(Object target) {
    if (target instanceof ClientServant) {
      ((ClientServant) target).loginAsGuest(name);
    } // XXX: else Exception auslösen?
  }
}
