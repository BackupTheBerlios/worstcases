package Util.Commands;

import Server.ClientServant;

public class LoginGuestCommand implements Command {
  
  /**
   * Konstruktor. Erzeugt einen Login-Befehl mit den zur Anmeldung eines
   * Users am Server benötigten Daten. Dieser Befehl kann nur von einem
   * ClientServant verarbeitet werden.
   *
   * @param name der Benutzername.
   * @param password das Kennwort.
   */
  public LoginGuestCommand(String paramName) {
   this.name=paramName;
  }

  /** Der Benutzername. */
  String name;
   
  public void execute(Object target) {
    if (target instanceof ClientServant) {
      ((ClientServant) target).loginAsGuest(name);
    } // XXX: else Exception auslösen?
  }
}
