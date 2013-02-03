package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/*
 * Listener is a TCP server where each worker accepts the admin messages
 */
public class PollServer {
	public static final int ADMIN_PORT = 1244;
	public static final int VOTING_PORT = 1122;
	
	public PollServer() {
	
	}
	
	public void startListeners(){
		new ListenerThread().start();
		new VoteListenerThread().start();		
	}
}

/*Listen for ADMIN client connections */
class ListenerThread extends Thread{
	@Override
	public void run() {
		new Listener(PollServer.ADMIN_PORT).listen();
	}
}

/*Listen for votes*/
class VoteListenerThread extends Thread{
	@Override
	public void run() {
		VoteListener voteListener = new VoteListener();
	}
}
