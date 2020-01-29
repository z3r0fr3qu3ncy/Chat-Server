package ie.gmit.dip;

import java.io.*;
import java.net.*;

public class ServerReadThread extends Thread {
	private BufferedReader reader;
	private Socket serverReadSocket;
	private ClientHandler client;

	public ServerReadThread(Socket serverReadSocket, ClientHandler client) {
		super();
		this.serverReadSocket = serverReadSocket;
		this.client = client;

		try {
			reader = new BufferedReader(new InputStreamReader(serverReadSocket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Error getting input stream: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				String clientOut = reader.readLine();
				String output = ("<" + client.getNickname() + ">: " + clientOut);
				System.out.println(output);
			} catch (IOException ex) {
				System.out.println("Error reading from client: " + client.getNickname() + ex.getMessage());
				ex.printStackTrace();
				break;
			}
		}
	}

}
