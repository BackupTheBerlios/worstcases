package Util;

import java.net.Socket;
import java.io.*;
import Util.Commands.*;

/**
 * Versendet Nachrichten �ber einen Socket an einen Downlink. Diese Klasse ist die sendende H�lfte eines Kommunikationskanals.
 * Die andere H�lfte, die das Empfangen von Nachrichten �bernimmt, ist der Downlink.
 * Diese Klasse wird z.B. vom Client benutzt, um Nachrichten an seinen ClientServant zu senden.
 * @see Util.Downlink
 * @see Client.Client
 * @see Server.ClientServant
 */
public class Uplink {
    /**
     * Konstruktor.
     * @param socket der zu benutzende Socket.
     */
    public Uplink(Socket paramSocket) {
        this.socket = paramSocket;
    }

    /** �ber diesen Socket werden die Nachrichten versendet. */
    private Socket socket;
    private ObjectOutputStream objectOutputStream;

    /** �ffnet den Output-Stream. */
    public void startUplink() throws java.io.IOException {
        this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        System.out.println("uplink started");
    }

    /** Schlie�t den Output-Stream. */
    public void stopUplink() {
        try{
        this.objectOutputStream.close();
        System.out.println("uplink stopped");
        }
        catch(java.io.IOException e){
         System.out.println(e);
        }
    }

    /**
     * Sendet eine Nachricht �ber den Socket. Sie wird am anderen Ende des Kommunikationskanals von einem Downlink empfangen.
     * @param msg Die zu versendende Nachricht.
     * @see Dowlink
     */
    public void sendMsg(Command msg) throws java.io.IOException {
        objectOutputStream.writeObject(msg); // FIXME: Dummy
        objectOutputStream.flush();
        System.out.println("sending " + msg);
    }
}
