package Server;

import java.util.Enumeration;

class ClientServantWatchDog extends Thread {
    private int timeToLive = 10000;
    private int updateDelay = 10000;
    public boolean stop = false;
    private Server server;

    public ClientServantWatchDog(Server paramServer) {
        this.server = paramServer;
    }

    public void run() {
        Enumeration enum;
        ClientServant tmpClientServant;
        while (!stop) {
            System.out.println("watching ClientServants");
            enum = this.server.getClientServantEnum();
            while (enum.hasMoreElements()) {
                tmpClientServant = (ClientServant)enum.nextElement();
                if ((java.lang.System.currentTimeMillis() - tmpClientServant.getAliveStamp()) > timeToLive) {
                    System.out.println("clientservant killed by watchdog");
                    tmpClientServant.stopClientServant();
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
