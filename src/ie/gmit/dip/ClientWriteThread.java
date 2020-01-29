package ie.gmit.dip;

import java.io.*;
import java.net.*;

public class ClientWriteThread extends Thread{
	private PrintWriter writer;
    private Socket clientWriteSocket;
    private ChatClient client;
	
    
    public ClientWriteThread(Socket socket, ChatClient client) {
		super();
		this.clientWriteSocket = socket;
		this.client = client;
		
		try {
            OutputStream sendOutput = socket.getOutputStream();
            writer = new PrintWriter(sendOutput, true);
        } catch (IOException e) {
            System.out.println("Error getting output stream: " + e.getMessage());
            e.printStackTrace();
        }
	}
    
    public void run() {
        Console console = System.console();
        String text;
 
        do {
        	text = console.readLine();
            writer.println(text);
 
        } while (!text.contains("\\q"));
 
        try {
            clientWriteSocket.close();
            System.out.println("Client shutting down");
            System.exit(0);
        } catch (IOException e) {
 
            System.out.println("Error writing to server: " + e.getMessage());
        }
    }
}



