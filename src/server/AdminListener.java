package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


/*
 * AdminListener is a TCP server where each worker accepts the admin messages
 */
class AdminListener {
	private ServerSocket serverSocket;
    Socket clientSocket;
	
	public AdminListener(int port) {
		serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
           e.printStackTrace(System.err);
           System.exit(1);
		}
	}
	
	//Continually listen for new client connection requests 
	public void listen(){
		while(true){
			try {
				System.out.println("listening");
				clientSocket = serverSocket.accept(); //accept connection from ADMIN client
				
				
				System.out.println("connected");
				new AdminWorker(clientSocket).start(); //assign a worker thread to server new client
			} catch (SocketException e2) { System.out.println("Done"); System.exit(0); }
			catch (IOException e) { e.printStackTrace(System.err); System.exit(1);  }
		}
	}
	public void finalize()
	{
		   try { serverSocket.close(); } catch (IOException e) {}
	}
}

/*Each AdminWorker is responsible for handling/serving each connected ADMIN's requests/messages*/
class AdminWorker extends Thread{
	PrintWriter outToClient;
	BufferedReader in;
	Socket clientSocket;
	
	
	public AdminWorker(Socket socket) {
		/*
		 * TODO check for any referencing issues 
		 * - reassignment of clientSocket variable in Listener.listen()
		 */
		clientSocket = socket;
		try {
			outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (SocketException e2) { System.out.println("Done"); System.exit(0); }
		catch (IOException e) { e.printStackTrace(System.err); System.exit(1);  }
	}
	
	@Override
	public void run() {
		//send the admin client a connection confirmation with its AdminId   
		outToClient.println("You are now connected as: " + clientSocket.getLocalAddress().toString()); 
		
		while(clientSocket.isConnected()){
			
			
		}
		
	}
	
	@Override
	protected void finalize() throws Throwable {
		System.out.println("AdminWorker dead");
		in.close();
		outToClient.close();
		clientSocket.close();
	}
	
}
