package Util.Commands;

import Server.ClientServant;


/**
 * Dieses Command wird von einem Client gesendet, um einen Gast anzumelden.
 */
public class LoginGuestCommand implements Command {

  /**
   * Der Konstruktor erzeugt einen Login-Befehl mit den zur Anmeldung eines
   * Users am Server benötigten Daten (hier nur den Namen des Gastes.
   * Dieser Befehl kann nur von einem ClientServant durch die Methode
   * loginAsGuest  verarbeitet werden.
   *
   * @param name der Benutzername, den der Gast benutzen möchte.
   */
  public LoginGuestCommand(String paramName) {
    this.name = paramName;
  }

  /** Der Benutzername des Gastes. */
  String name;

  /** Ruft beim ClientServant loginAsGuest() auf. */
  public void execute(Object target) {

    if (target instanceof ClientServant) {
      ((ClientServant) target).loginAsGuest(name);
    }
  }
}
