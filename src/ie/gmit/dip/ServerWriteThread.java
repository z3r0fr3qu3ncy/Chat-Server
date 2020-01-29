package ie.gmit.dip;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerWriteThread extends Thread {
	private PrintWriter writer;
	private Socket serverWriteSocket;
	private ClientHandler client;
	private ChatServer theChatServer;

	public ServerWriteThread(ChatServer theChatServer) {
		super();
		this.theChatServer = theChatServer;
	}

	public ServerWriteThread(Socket serverWriteSocket, ClientHandler client) {
		super();
		this.serverWriteSocket = serverWriteSocket;
		this.client = client;

		try {
			OutputStream sendOutput = serverWriteSocket.getOutputStream();

			writer = new PrintWriter(sendOutput, true);
		} catch (IOException e) {
			System.out.println("Error getting output stream for: " + client.getNickname() + e.getMessage());
			e.printStackTrace();
		}
	}

	public ServerWriteThread(Socket serverWriteSocket, ClientHandler client, ChatServer theChatServer) {
		super();
		this.serverWriteSocket = serverWriteSocket;
		this.client = client;
		this.theChatServer = theChatServer;

		try {
			OutputStream sendOutput = serverWriteSocket.getOutputStream();

			writer = new PrintWriter(sendOutput, true);
		} catch (IOException e) {
			System.out.println("Error getting output stream for: " + client.getNickname() + e.getMessage());
			e.printStackTrace();
		}
	}

	public void run() {
		Console console = System.console();
		String text = "";
		while (!text.contains("\\q")) {
			text = console.readLine("SERVER SHOUT-OUT >>: ");
			// String text = "";
			if (text.contains("@")) {
				String[] args = text.split("@");
				String target = args[1].trim();
				String message = args[0].trim();
				// String format = "<ServerAdmin> " + message;
				theChatServer.serverMsgTarget(target, message);
			} else {
				String output = ("<" + "ServerAdmin" + ">: " + text);
				theChatServer.serverMsgAll(output);
			}
		}

		try {
			System.out.println("Closing server write thread socket");
			serverWriteSocket.close();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Error writing to client socket: " + e.getMessage());
		}
	}

}
