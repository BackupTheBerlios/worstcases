package Util;

import java.net.Socket;
import java.util.Vector;
import java.io.*;
import Util.Commands.*;
import Util.Debug.Debug;

/**
 * Empfängt über einen Socket Nachrichten von einem Uplink und leitet sie an ihren Besitzer weiter.
 * Diese Klasse ist die empfangende Hälfte eines Kommunikationskanals. Die
 * andere Hälfte, die das Senden von Nachrichten übernimmt, ist der Uplink.
 * Diese Klasse wird z.B. vom ClientServant benutzt, um Nachrichten von seinem Client zu empfangen.
 */
public class Downlink extends Thread {
    private int LISTEN_DELAY = 1000;

    /**
     * Konstruktor.
     * @param socket der zu benutzende Socket.
     * @param owner der Besitzer des Downlinks.
     */
    public Downlink(Socket socket, DownlinkOwner owner) {
        this.socket = socket;
        this.setDownlinkOwner(owner);
    }

    /** Über diesen Socket werden die Nachrichten empfangen. */
    private Socket socket;

    /** Der Besitzer des Downlinks, an den die ankommenden Nachrichten weitergeleitet werden sollen. */
    private DownlinkOwner downlinkOwner;
    private ObjectInputStream objectInputStream;
    private boolean stop = false;

    /** Wartet auf ankommende Nachrichten und leitet sie an den Besitzer weiter. */
    private void listen() {
        //  String tmpString;
        Command tmpCommand;
        while (!stop) {
            try {
                tmpCommand = (Command)objectInputStream.readObject();
                Debug.println("Downlink: received " + tmpCommand);
                downlinkOwner.processMsg(tmpCommand);
                try {
                    this.sleep(Downlink.LISTEN_DELAY);
                }
                catch (java.lang.InterruptedException e) {
                    Debug.println(Debug.HIGH, e);
                }
            } catch (java.io.IOException e) {
                Debug.println(Debug.HIGH, e);
                this.stopDownlink();
            }
            catch (java.lang.ClassNotFoundException e) {
                Debug.println(Debug.HIGH, e);
            }
        }
        try {
            this.objectInputStream.close();
        }
        catch (java.io.IOException e) {
            Debug.println(Debug.HIGH, e);
        }
        Debug.println("Downlink: stopped");
    }

    /** Öffnet den Input - Stream, danach ist der Downlink betriebsbereit. */
    public void startDownlink() throws java.io.IOException{
            this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            Debug.println("Downlink: started");
    }

    /** Startet den Thread. */
    public void run() {
        listen();
    }

    /** Schließt den Input - Stream */
    private void stopDownlink() {
        this.stop = true;
    }

    public DownlinkOwner getDownlinkOwner() {
        return downlinkOwner;
    }

    public void setDownlinkOwner(DownlinkOwner paramDownlinkOwner) {
        if (this.downlinkOwner != paramDownlinkOwner) {
            if (this.downlinkOwner != null) {
                DownlinkOwner old = this.downlinkOwner;
                this.downlinkOwner = null;
                old.setDownlink(null);
            }
            this.downlinkOwner = paramDownlinkOwner;
            if (paramDownlinkOwner != null) {
                paramDownlinkOwner.setDownlink(this);
            }
        }
    }
}
