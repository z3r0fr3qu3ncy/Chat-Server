package ie.gmit.dip;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.*;

public class ChatServer {
	//Set<Socket> socketList = new HashSet<Socket>();
	public static Set<String> handles = new HashSet<String>(); // This set will enforce the uniqueness of usernames - need unique to join the chat - see ClientHandler.
	//private static Set<PrintWriter> writers = new HashSet<PrintWriter>();
	protected Set<ClientHandler> clients = new HashSet<ClientHandler>(); //This Set will enforce uniqueness of clients.
	private static int PORT = 63519;
	ExecutorService executor;
	int maxThreads = 20;
	Socket theConnection;
	ServerSocket theServerSocket;
	BufferedReader bro;
	PrintWriter pro;

	public ChatServer() {
		super();
	}

	public ChatServer(int pORT) {
		super();
		PORT = pORT;
		
	}

	public static void main(String[] args) throws IOException {
		if(args.length == 0) {
		ChatServer cs = new ChatServer();
		// cs.theServerSocket = new ServerSocket(PORT);
		System.out.println("Server is up. ");
		cs.serve();
		} else if(args.length == 1) {
			ChatServer cs = new ChatServer(Integer.parseInt(args[0]));
			System.out.println("Server is up. ");
			cs.serve();
		} else {
			System.out.println("Please enter ChatServer or ChatServer <Port> to start. ");
			System.exit(0);
		}
	}
	public void serve() throws IOException {
		this.executor = Executors.newFixedThreadPool(maxThreads); 
		ServerWriteThread sWrite = new ServerWriteThread(this); //Lets conserve resources. We don't need a server writer thread for each client if we simply call an iterative method.
		sWrite.start();                                         //We have already done away with the need for a read thread due to well placed method call in client handler.
																//e.g. John Healy "Battle of Agencourt stuff, 10,000 heavy cavalry ain't all that helpful".
		try {
			theServerSocket = new ServerSocket(PORT);
			System.out.println("Server is listening on " + PORT);
			while (true) {
				theConnection = theServerSocket.accept();
				ClientHandler newChatHandler = new ClientHandler(this, theConnection);
				clients.add(newChatHandler);
				this.executor.execute(newChatHandler);
				System.out.println("\nadding client....");
				}
			} catch (IOException e) {
			e.printStackTrace();
			}
	}
	
	public void serverMsgTarget(String target, String msg) {//Here we give the server ability to dm(default case) as well as a command switch so we can kick bold users.
		String cmdSwtch = msg.trim().toLowerCase();
		switch(cmdSwtch) { //Much room for expansion here if further command switch functionality required.
		case "kline":  //old bbs term, and more unusual to be seen in parlance than the word "kick". Still has to be exactly "kline" (case insensitive) and will require
			synchronized (this.clients) { // the @ flag in the message to trigger this method at all e.g. kline without @ will not fire this, so "hello mr kline" is fine.
				for(ClientHandler c : clients) { //actually "hello Mr kline@Mr kline" should be fine too...(exactly kline(case insensitive)).
					if(c.getNickname().equalsIgnoreCase(target)) {
						c.clientClose();
						this.serverWrite(target + " has been removed from chat");
						this.serverMsgAll(target + " has been removed from chat");
					}
				}
				break;
			}
		default:
			synchronized (this.clients) {
				for(ClientHandler c : clients) {
					if(c.getNickname().equalsIgnoreCase(target)) {
						String format = "<ServerAdmin> " + msg;
						try {
						c.write(format);
						} catch(IOException e) {System.out.println(e.getMessage());}
					}
				}System.out.println("Message sent to " + target);}
				break;
			}
		}
	
	public void clientMsgTarget(String target, String msg, ClientHandler sending) {
		if(target.equalsIgnoreCase("admin")) {this.serverWrite(msg);} else {
		synchronized (this.clients) {
			for(ClientHandler c : clients) {
				if(c.getNickname().equalsIgnoreCase(target)) {
					try {
					c.write(msg);
					} catch(IOException e) {System.out.println(e.getMessage());}
				} //else if(target == "admin") {this.serverWrite(msg);}
				else {this.serverWrite("This notification represents a private direct message between " + (sending.getNickname() + "-") + target);}
			}
		}
	}
}
	public void serverMsgAll(String msg) {
		synchronized (this.clients) {
		for(ClientHandler c : clients) {
			try {
				c.write(msg);
			} catch(IOException e) {System.out.println("Error writing to client : " + c);}
			}
		}
	}

	public void sendMsg(String msg, ClientHandler client) throws IOException {
			for (ClientHandler clientIter : clients) {
	            if (clientIter != client) {
	            	clientIter.sendClientMessage(msg);
	            }
			}
	}
	
	public void serverWrite(String msg) {
		System.out.println(msg);  // Try print vs println as we need a \n for admin bound messages
	}

	//public void addClient(ClientHandler client) { //Will change this to be a synchronized alternative to clients.add(Set.add) method. 
	//		this.clients.add(client);
	//	}
	
	public Set<ClientHandler> getClientHandlers() {
		return this.clients;
	}	

	public void removeClient(String handle, ClientHandler client) {
		boolean removed = handles.remove(handle);
		synchronized (this.clients) {
			if(removed) {
			clients.remove(client);
			}
		}
	}
}
