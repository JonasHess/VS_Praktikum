package de.hda.VSPraktikum;

import de.hda.VSPraktikum.ConnectionHandlers.ConnectionHandler;
import de.hda.VSPraktikum.ConnectionHandlers.TCP_ConnectionHandler;
import de.hda.VSPraktikum.ConnectionHandlers.UDP_ConnectionHandler;

public class Main {
	
    public static void main(String[] args) throws Exception{
    	int port = 8888; 
    	if (args.length > 0) {
    		port = Integer.valueOf(args[0]);
    	}
    	Monitor m = new Monitor("AZ");
        final boolean tcp = false;
        ConnectionHandler server;
        if (tcp) {
            server = new TCP_ConnectionHandler(port, m);

        } else {
            server = new UDP_ConnectionHandler(port, m);

        }
    	server.start();
    	while (true) {
    		m.printStatus();
    		Thread.sleep(500);
    	}
    }
    
}
