package Server;

import java.util.Enumeration;


/** Ein Thread, der daf�r zust�ndig ist,
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
     * der ClientServantWatchDog erneut alle ClientServants �berpr�ft.
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
     * Die Runmethode enth�lt im Wesentlichen eine Schleife,
     * die solange ausgef�hrt wird bis stop auf true gesetzt wird,
     * und mittels servant.getClientServantEnum() die entsprechenden
     * Clientservants �berpr�ft.
     * F�r die �berpr�fung wird ClientServant.getAliveStamp() und java.lang.System.currentTimeMillis() benutzt.
     * ClientServants werden ggf. durch ClientServant.stopClientServant() entfernt.
     * Zur Kontrolle der Schleifendurchl�ufe wird timeToLive und updateDelay benutzt
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
