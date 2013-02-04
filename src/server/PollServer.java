package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.sql.rowset.Joinable;

/*
 * Listener is a TCP server where each worker accepts the admin messages
 */
public class PollServer {
	public static final int ADMIN_PORT = 5000;
	public static final int VOTING_PORT = 1122;

	public PollServer() {
	
	}
	
	public void startListeners(){
		Thread admin = new ListenerThread();
		Thread t = new VoteListenerThread();
		admin.start();
		t.start();
		
		try {
			admin.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new PollServer().startListeners();
	}
}

/*Initialize a Listener for ADMIN client connections */
class ListenerThread extends Thread{
	@Override
	public void run() {
		new AdminListener(PollServer.ADMIN_PORT).listen();
	}
}

/*Initialize a listener for other clients - voters */
class VoteListenerThread extends Thread{
	@Override
	public void run() {
		new VoteListener(PollServer.VOTING_PORT);
	}
}
