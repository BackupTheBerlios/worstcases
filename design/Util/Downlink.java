package Util;

import java.net.Socket;
import java.util.Vector;
import java.io.*;
import Util.Commands.*;

/**
 * Empfängt über einen Socket Nachrichten von einem Uplink und leitet sie an ihren Besitzer weiter.
 * Diese Klasse ist die empfangende Hälfte eines Kommunikationskanals. Die
 * andere Hälfte, die das Senden von Nachrichten übernimmt, ist der Uplink.
 * Diese Klasse wird z.B. vom ClientServant benutzt, um Nachrichten von seinem Client zu empfangen.
 */
public class Downlink extends Thread {
    /**Zeit zwischen zwei Listen() - Schleifendurchläufen in Millisekunden*/
    private int LISTEN_DELAY = 100;

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
    private boolean stopped =false;

    /** Wartet auf ankommende Nachrichten und leitet sie an den Besitzer weiter.
      * benutzt DownlinkOwner.downlinkError()
      */
    private void listen() {
        //  String tmpString;
        Command tmpCommand;
        while (!stop) {
            try {
                tmpCommand = (Command)objectInputStream.readObject();
                System.out.println("received " + tmpCommand);
                downlinkOwner.processMsg(tmpCommand);
                this.sleep(this.LISTEN_DELAY);

            }
            catch (Exception e){
             System.out.println("error while listening :"+e);
             this.stopped=true;
             this.downlinkOwner.downlinkError();
            }
        }
        try{
            this.objectInputStream.close();
        }
        catch(java.io.IOException e){
         System.out.println("error closing stream :"+e);
        }
        this.stopped=true;
    }

    /** Öffnet den Input - Stream, danach ist der Downlink betriebsbereit.
      * Benutzt DownlinkOwner.downlinkError()
      */
    public void startDownlink() {
            try{
            this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            System.out.println("downlink started");
            this.start();
            }
            catch (java.io.IOException e){
              System.out.println("error starting downlink:"+e);
              this.downlinkOwner.downlinkError();
            }
    }

    /** Startet den Thread. */
    public void run() {
        listen();
    }

    /** Schließt den Input - Stream */
    private void stopDownlink() {
        this.stop = true;
        while(!stopped){}

        this.setDownlinkOwner(null);
        System.out.println("downlink stopped");
    }

    public DownlinkOwner getDownlinkOwner() {
        return downlinkOwner;
    }

    /**
     * setDownlinkOwner(null) bewirkt stopDownlink()
     */
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
            else{
             this.stopDownlink();
            }
        }
    }
}
