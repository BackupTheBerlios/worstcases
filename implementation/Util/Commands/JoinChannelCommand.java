package Util.Commands;

import Server.ClientServant;


/**
 * Wird von einem Client gesendet, damit der Benutzer einen
 * Channel betreten kann. Die Methode joinChannel der
 * Klasse ClientServant wird dazu benutzt.
 */
public class JoinChannelCommand implements Command {

  /**
   * Setzt den Channelnamen des Channels, der betreten werden soll.
   */
  public JoinChannelCommand(String paramName) {
    this.name = paramName;
  }

  /**
   * Der Name des Channels, der durch den Benutzer betreten
   *    wird.
   */
  String name;

  /** Ruft beim ClientServant die Methode joinChannel() auf. */
  public void execute(Object target) {

    if (target instanceof ClientServant) {
      ((ClientServant) target).joinChannel(name);
    }  // XXX: else Exception auslösen?
  }
}
