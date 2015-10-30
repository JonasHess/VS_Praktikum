package testclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TestClient {
    static String line;
    static Socket socket;
    //static BufferedReader fromServer;   
    static DataOutputStream toServer;
    static UserInterface user = new UserInterface();
    
    public static void main(String[] args) throws Exception {
        socket = new Socket("localhost", 9999);
        toServer = new DataOutputStream(socket.getOutputStream()); // Datastream TO Server
        while (send()) {        
        }
        socket.close();
        toServer.close();
    }
    
    private static boolean send() throws IOException {
        boolean holdTheLine = true;          // Connection exists
        user.output("Enter message for the Server, or end the session with . : ");
        toServer.writeBytes((line = user.input()) + '\n');
        if (line.equals("bye")) {              // Does the user want to end the session?
          holdTheLine = false;
        }
        return holdTheLine;  
    }
}
