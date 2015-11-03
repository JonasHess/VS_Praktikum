package de.hda.VSPraktikum;


public class Main {
	
    public static void main(String[] args) throws Exception{
    	int port = 8888; 
    	if (args.length > 0) {
    		port = Integer.valueOf(args[0]);
    	}
    	Monitor m = new Monitor("AZ");
    	SocketServer server = new SocketServer(port, m);
    	server.start();
    	while (true) {
    		m.printStatus();
    		Thread.sleep(500);
    	}
    }
    
}
