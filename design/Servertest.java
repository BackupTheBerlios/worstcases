import Util.*;
import Server.*;
import Client.*;

class Servertest extends Thread{
    public static void main(String[] args) throws java.io.FileNotFoundException, java.io.IOException {
     Servertest test=new Servertest();
     test.start();
    }
    
    public void run(){
       try{
                Server tmpServer = new Server();
        tmpServer.startServer();
	}
	catch (Exception e){
	
	 System.out.println(e);
	}

    
    }
    

}
