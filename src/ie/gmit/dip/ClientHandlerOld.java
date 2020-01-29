package ie.gmit.dip;

import java.net.*;
import java.io.*;

public class ClientHandlerOld implements Runnable{
	private Socket clientSocket;
	protected String nick;
	private PrintWriter printSysIn;
	private PrintWriter printStreamOut;
	Thread t1, t2;
	String inward = "";
	String outward = "";
	
	public ClientHandlerOld(Socket clientIn, String nickIn) {
        this.clientSocket = clientIn;
        this.nick = nickIn;
    }
	
	public void run() {
        try { 
        	if(Thread.currentThread() == t2) {
			
			//DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
	        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        printSysIn = new PrintWriter(clientSocket.getOutputStream(), true);
	        inward = br.readLine();
	        while(br.readLine().contains("\\q")) {
	        	printSysIn.println(inward);	
	        	}
	        printSysIn.println("Shutting down..");
	        printSysIn.close();
	        clientSocket.close();
        	} else {
        		//DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
        		BufferedReader bro = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        		OutputStream os = clientSocket.getOutputStream();
        		printStreamOut = new PrintWriter(os);
        		while(!bro.readLine().contains("\\q")) {
        		printStreamOut.println(bro); 		
        		}
        		printStreamOut.println("Shutting down...");
        		printStreamOut.close();
        		clientSocket.close();
        	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String line = "";
        //String check = "\\q";
        while(!line.contains("\\q")) {
        	
        }
	}
}