package Util.Commands;

import Server.ClientServant;

public class LoginCommand implements Command {
  
  /**
   * Konstruktor. Erzeugt einen Login-Befehl mit den zur Anmeldung eines
   * Users am Server benötigten Daten. Dieser Befehl kann nur von einem
   * ClientServant verarbeitet werden.
   *
   * @param name der Benutzername.
   * @param password das Kennwort.
   */
  public LoginCommand(String name, String password) {
  }

  /** Der Benutzername. */
  String name;
  /** Das Kennwort. */
  String password;
   
  public void execute(Object target) {
    if (target instanceof ClientServant) {
      ((ClientServant) target).loginUser(name, password);
    } // XXX: else Exception auslösen?
  }
}
