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
    /**Zeit zwischen zwei Listen()-Schleifendurchläufen in Millisekunden.*/
    private int LISTEN_DELAY = 100;

    /**
     * Konstruktor, der setDownlinkOwner() benutzt.
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

    /** InputStream für Objekte.*/
    private ObjectInputStream objectInputStream;

    /** Wird auf true gesetzt, wenn der Thread beendet werden soll.*/
    private boolean stop = false;

    /** Wartet auf ankommende Nachrichten mit objectInputStream.readObject() und leitet sie an den Besitzer weiter.
      * Benutzt DownlinkOwner.downlinkError() und processMsg().
      * Wartet pro Schleifendurchlauf gemäß LISTEN_DELAY und beendet die Schleife, falls stop=true;
      */
    private void listen() {
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
             this.downlinkOwner.downlinkError();
            }
        }
    }

    /** Öffnet den Input - Stream, danach ist der Downlink betriebsbereit.
      * Ruft this.start() auf.
      * Benutzt DownlinkOwner.downlinkError().
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

    /** Schließt den Input-Stream.
      * Benutzt setDownlinkOwner(null).
      */
    private void stopDownlink() {
        this.stop = true;
        try{
         if(this.objectInputStream!=null){
         this.objectInputStream.close();
         }
        }
        catch(java.io.IOException e){
         System.out.println(e);
        }
        this.setDownlinkOwner(null);
        System.out.println("downlink stopped");
    }


    /**Gibt den DownlinkOwner zurück.*/
    public DownlinkOwner getDownlinkOwner() {
        return downlinkOwner;
    }

    /**
     * aufruf von setDownlinkOwner(null) bewirkt stopDownlink().
     * Benachrichtigt die betroffenen Objekte mittels setDownlink().
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
