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
	
	private ServerSocket serverSocket;
    Socket clientSocket;
	PrintWriter out;
	BufferedReader in;
	
	public PollServer(int port) {
		serverSocket = null;
		try {
			serverSocket = new ServerSocket(1234);
		} catch (IOException e) {
           e.printStackTrace(System.err);
           System.exit(1);
		}
	}
	
	public void listen(){
		
		String msg;
		
		try {
			clientSocket = serverSocket.accept(); //accept connection from admin client 
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			
			while ( (msg = in.readLine()) != null) 
			{
				//TODO create a worker thread 
				System.out.println ("Server Rxd: " + msg );
				out.println("Echo " + msg);
			}

			in.close();
			out.close();
			clientSocket.close();
			serverSocket.close();
      
		} catch (SocketException e2) { System.out.println("Done"); System.exit(0); }
		catch (IOException e) { e.printStackTrace(System.err); System.exit(1);  }
	}
	public void finalize()
	{
		   try { serverSocket.close(); } catch (IOException e) {}
	}
}

class ServerWorker extends Thread{
	
}
