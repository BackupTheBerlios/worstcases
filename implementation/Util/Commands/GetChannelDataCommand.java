package Util.Commands;

import Client.AdminClient;
import Server.AdminClientServant;


/**
 * Fordert vom AdminClientServant einen Channeldatensatz an, indem die
 * Methode sendChannel der Klasse AdminClientServant ausgef�hrt wird.
 */
public class GetChannelDataCommand implements Command {

  /**
   * Der Konstruktor initailisiert den Channel, von dem die Daten ben�tigt werden,
   * indem der Channelname �bergeben wird.
   */
  public GetChannelDataCommand(String paramChannelName) {
    this.channelName = paramChannelName;
  }

  /**
   * Der Channelname des Channels, dessen Daten auszugeben sind.
   */
  String channelName;

  /**
   * F�hrt AdminClientServant.sendChannel() mit den Parametern
   *   dieses Commands aus.
   */
  public void execute(Object target) {

    if (target instanceof AdminClientServant) {
      ((AdminClientServant) target).sendChannel(channelName);
    }
  }
}
