package Util;

import java.net.Socket;
import java.io.*;
import Util.Commands.*;

/**
 * Versendet Nachrichten über einen Socket an einen Downlink. Diese Klasse ist die sendende Hälfte eines Kommunikationskanals.
 * Die andere Hälfte, die das Empfangen von Nachrichten übernimmt, ist der Downlink.
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

    /** Über diesen Socket werden die Nachrichten versendet. */
    private Socket socket;

    /**Output-Stream für Objekte.*/
    private ObjectOutputStream objectOutputStream;

    /** Öffnet den Output-Stream. */
    public void startUplink() throws java.io.IOException {
        this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        System.out.println("uplink started");
    }

    /** Schließt den Output-Stream. */
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
     * Sendet ein Commandobjekt über den Socket. Es wird am anderen Ende des Kommunikationskanals von einem Downlink empfangen.
     * Benutzt objectOutputStream.writeObject()
     * @param msg Das zu versendende Commandobjekt.
     * @see Downlink
     */
    public synchronized void sendMsg(Command msg) throws java.io.IOException {
        objectOutputStream.writeObject(msg);
        objectOutputStream.flush();
        System.out.println("sending " + msg);
    }
}
