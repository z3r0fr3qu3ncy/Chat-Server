package ie.gmit.dip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
	private String nickname;
	private Socket theHandlerSocket;
	ChatServer chatServer;
	private BufferedReader breader;
	private PrintWriter pwriter;
	private BufferedReader bro;
	//boolean accepted = false;

	public ClientHandler(ChatServer cs, Socket connectionHandle) throws IOException {
		this.chatServer = cs;
		this.theHandlerSocket = connectionHandle;

	}

	synchronized public void write(String writable) throws IOException {
		this.pwriter.println(writable);
		this.pwriter.flush();
	}

	// synchronized public PrintWriter getPw() {
	// return this.pw;
	// }

	synchronized public String read() throws IOException {
		return this.bro.readLine();
	}

	public void clientClose() {
		this.pwriter.close();
		try {
			this.breader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.theHandlerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("Client handler initializing...");
		try {
			pwriter = new PrintWriter(theHandlerSocket.getOutputStream(), true);
			breader = new BufferedReader(new InputStreamReader(theHandlerSocket.getInputStream()));
			while (true) {
                pwriter.println("Please enter a unique chat name: ");
                nickname = breader.readLine();
                if (nickname == null) {
                    return;
                }
                synchronized (ChatServer.handles) {
                    if (!nickname.isBlank() && !ChatServer.handles.contains(nickname)) { //Here we enforce the uniqueness of handle/username before allow user to fully join.
                        ChatServer.handles.add(nickname);
                        break;
                    }
                }
			}
			pwriter.println("Chat handle accepted as unique!! Joining Chatroom!");
			chatServer.serverWrite("User " + nickname + " has joined chat");
			chatServer.sendMsg("\nUser " + nickname + " has joined chat", this);
			String input = "";
			do {
				input = breader.readLine();
				if(input.contains("@")) {   //Pre-processing for direct message fucntionality.
	        		String[] args = input.split("@");
	        		String target = args[1].trim();
	        		String message = args[0].trim();
	        		String format = ("[" + nickname + "]: " + message);
	        		chatServer.clientMsgTarget(target, format, this);
	        	} else {
				String output = ("[" + nickname + "]: " + input);
				chatServer.serverWrite(output); // try remove this \n due to \n in line above
				chatServer.sendMsg(output, this);
	        	}
			} while (!input.contains("\\q"));

			chatServer.serverWrite("Chatter " + nickname + " is leaving..");
			this.clientClose();
			chatServer.removeClient(this.nickname, this);
			chatServer.sendMsg("Chatter " + nickname + " has left the room. ", this);

		} catch (IOException e) {
			System.out.println("Error with client " + this.getNickname());
			//e.printStackTrace();
		}
	}

	public void sendClientMessage(String message) {
		pwriter.println(message);
	}

	public String getNickname() {
		return this.nickname;
	}
	public void setNick(String nick) {
		this.nickname = nick;
	}
}