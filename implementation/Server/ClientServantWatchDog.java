package Server;

import java.util.Enumeration;


/** Ein Thread, der dafür zuständig ist,
  * ClientServants aus dem System zu entfernen,
  * die seit einer bestimmten Zeitspanne
  * keine Nachrichten mehr von ihrem Client
  * empfangen haben.
  */

class ClientServantWatchDog extends Thread {

    /**Zeit in Millisekunden, die angibt,
      *wie lange eine ClientServant inaktiv sein darf.
      */
    private int timeToLive = 600000;


    /**
     * Zeitspanne in Millisekunden, die vergeht, bis
     * der ClientServantWatchDog erneut alle ClientServants überprüft.
     */
    private int updateDelay = 6000;


    /**
     * Flag, welches angibt, ob der Thread beendet werden soll.
     */
    public boolean stop = false;

    private Server server;


    /**
     * Setzt das Server-Attribut.
     */
    public ClientServantWatchDog(Server paramServer) {
        this.server = paramServer;
    }

    /**
     * Die Runmethode enthält im Wesentlichen eine Schleife,
     * die solange ausgeführt wird bis stop auf true gesetzt wird,
     * und mittels servant.getClientServantEnum() die entsprechenden
     * Clientservants überprüft.
     * Für die Überprüfung wird ClientServant.getAliveStamp() und java.lang.System.currentTimeMillis() benutzt.
     * ClientServants werden ggf. durch ClientServant.stopClientServant() entfernt.
     * Zur Kontrolle der Schleifendurchläufe wird timeToLive und updateDelay benutzt
     */

    public void run() {
        Enumeration enum;
        ClientServant tmpClientServant;
        while (!stop) {
            enum = this.server.getClientServantEnum();
            while (enum.hasMoreElements()) {
                tmpClientServant = (ClientServant)enum.nextElement();
                if ((java.lang.System.currentTimeMillis() - tmpClientServant.getAliveStamp()) > timeToLive) {


                    tmpClientServant.stopClientServant();
                    System.out.println(tmpClientServant+": stopped by watchdog");  
                }
            }
            try{
            sleep(updateDelay);
            }
            catch (java.lang.InterruptedException e){
               System.out.println("watchdog interrupted: "+e);
            }
        }
    }
}
