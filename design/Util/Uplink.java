package Util;

import java.net.Socket;

/**
 * Versendet Nachrichten �ber einen Socket an einen Downlink.
 * Diese Klasse ist die sendende H�lfte eines Kommunikationskanals. Die andere
 * H�lfte, die das Empfangen von Nachrichten �bernimmt, ist der Downlink.
 * Diese Klasse wird z.B. vom Client benutzt, um Nachrichten an seinen
 * ClientServant zu senden.
 *
 * @see Util.Downlink
 * @see Client.Client
 * @see Server.ClientServant
 */
public class Uplink {

  /**
   * Konstruktor.
   * @param socket der zu benutzende Socket.
   */
  public Uplink(Socket socket) {}

  /**
   * �ber diesen Socket werden die Nachrichten versendet.
   */
  private Socket socket;

  /**
   * �ffnet den Output-Stream.
   */
  public void startUplink() {}

  /**
   * Schlie�t den Output-Stream.
   */
  public void stopUplink() {}

  /**
   * Sendet eine Nachricht �ber den Socket. Sie wird am anderen Ende des
   * Kommunikationskanals von einem Downlink empfangen.
   *
   * @param msg Die zu versendende Nachricht.
   * @see Dowlink
   */
  // FIXME: In Client.Uplink hie� diese Methode sendMsg(String msg) ->
  // alle Client-Klassen durchgehen und zu send(String msg) korrigieren! 
  public void send(String msg) {}
}