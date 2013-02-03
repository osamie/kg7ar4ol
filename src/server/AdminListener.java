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
				new AdminWorker(clientSocket); //assign a worker thread to server new client
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
	}
	
	@Override
	public void run() {
		try {
			outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToClient.println("Connected"); //notify admin client that it is now connected
			
			in.close();
			outToClient.close();
		} catch (SocketException e2) { System.out.println("Done"); System.exit(0); }
		catch (IOException e) { e.printStackTrace(System.err); System.exit(1);  }
	}
	
	@Override
	protected void finalize() throws Throwable {
		clientSocket.close();
	}
	
}