import Server.*;
import Client.*;
import Util.*;
import java.io.*;

class Clienttest {
    public static void main(String[] args) {
        Client[] tmp = new Client[10000];
	for(int i=0;i<1;i++){
	 tmp[i] = new Client();
	 tmp[i].startClient();
	 tmp[i].loginAsGuest("Gast "+i%1000);		
	}    
	
    }
}
