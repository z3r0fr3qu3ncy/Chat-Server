package ie.gmit.dip;

import java.net.*;

//import ie.gmit.dip.ChatServer.ClientHandler;

import java.io.*;

public class ChatClient {
	Socket clientSideSocket;
	// ServerSocket theClientServSoc;
	// InetAddress defaultAddress = InetAddress.getByName("127.0.0.1");
	private String iSocAddr = "localhost";
	private static int Port = 63519;
	//BufferedReader reader;
	//PrintWriter writer;
	//BufferedReader sysIn;
	//PrintWriter sysOut;
	String chatter;
	//boolean clientAccepted = false;

	public ChatClient(int port) {
		super();
		this.Port = port;
	}

	public ChatClient() {
		super();
	}

	public ChatClient(String socAddr, int port) {
		super();
		this.iSocAddr = socAddr;
		Port = port;
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			ChatClient client = new ChatClient();
			client.commence();
		} else if (args.length == 1) {
			ChatClient client = new ChatClient(Integer.parseInt(args[0]));
			client.commence();
		} else if (args.length == 2) {
			ChatClient client = new ChatClient(args[0], Integer.parseInt(args[1]));
			client.commence();
		} else {
			System.out.println("Please enter ChatClient for default mode or ChatClient <Port> or ChatClient <INetAddr> <Port>: ");
			System.exit(0);
		}
	}

	public void commence() {
		try {
			clientSideSocket = new Socket(iSocAddr, Port);
			System.out.println("Client connected");

			new ClientReadThread(clientSideSocket, this).start();
			new ClientWriteThread(clientSideSocket, this).start();

		} catch (UnknownHostException e) {
			System.err.println("Cannot find the host");
		} catch (SocketException ea) {
			System.err.println("Could not connect to the socket");
		} catch (IOException eb) {
			System.err.println(eb);
		}
	}

	/*
	 * public String getInetAddr() { return this.inetSocAddr; }
	 */
	/*
	 * void setUserName(String userName) { synchronized(ChatServer.handles) {
	 * if(!ChatServer.handles.contains(userName)) { this.chatter = userName;
	 * ChatServer.handles.add(userName); this.clientAccepted = true;
	 * //ChatServer.addClient(this); } } }
	 */
	String getUserName() {
		return this.chatter;
	}
}
