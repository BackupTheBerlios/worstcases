package Util.Commands;

import Server.ClientServant;

public class JoinChannelCommand implements Command {
  
  /**
   * Konstruktor. Erzeugt einen Befehl zum Betreten eines Channels mit den 
   * dazu benötigten Daten. Dieser Befehl kann nur von einem ClientServant
   * verarbeitet werden.
   *
   * @param name der Name des zu betretenden Channels.
   */
  public JoinChannelCommand(String name) {
  }

  /** Der Channel-Name. */
  String name;
   
  public void execute(Object target) {
    if (target instanceof ClientServant) {
      ((ClientServant) target).joinChannel(name);
    } // XXX: else Exception auslösen?
  }
}
