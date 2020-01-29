package ie.gmit.dip;

import java.io.*;
import java.net.*;

public class ClientReadThread extends Thread{
	private BufferedReader reader;
    private Socket clientReadSocket;
    private ChatClient client;
	
    
    public ClientReadThread(Socket socket, ChatClient client) {
		super();
		this.clientReadSocket = socket;
		this.client = client; // direct socket handover rather than connect in here again. But keeping just in case it might be useful to have a handle on the client for future.
    
	    try {
	    	reader = new BufferedReader(new InputStreamReader(clientReadSocket.getInputStream()));
	    } catch (IOException e) {
            System.out.println("Error getting input stream: " + e.getMessage());
            e.printStackTrace();
	    	}
    
	    }
    
    public void run() {
        while (true) {
            try {
                String serverOut = reader.readLine();
                System.out.println(serverOut);
            	} catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                //ex.printStackTrace();
                break;
            }
        }
    }
}

