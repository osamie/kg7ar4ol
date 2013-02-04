package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
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
	
	
	/**
	 *Listen for new client connection requests and spawns a new AdminWorker thread
	 *for every new connection 
	 */
	public void listen(){
		while(true){
			try {
				System.out.println("listening...");
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

/**
 *  Each AdminWorker is responsible for handling/serving each connected 
 *  ADMIN's requests/messages
 */
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
		
		/*
		 * TODO
		 * Provide the connected AdminClient with an adminID
		 */
		
//		outToClient.println("**You are now connected as: " + clientSocket.getLocalSocketAddress().toString() + "**"); 
		
		System.out.println("**You are now connected as: " + clientSocket.getPort() + "**");
		while(clientSocket.isConnected()){
			//listen for any request from AdminClient
			try {
				String str = in.readLine();
				if (str==null){
					System.out.println("Admin client disconnected");
					break;
				}
				this.processRequest(str);
			} catch (IOException e) {
				//TODO handle problem reading input
				continue;
			}
		}
		
	}
	
	/**
	 * Parses the string to determine the admin's actual request and to get 
	 * the request arguments if any.
	 * @param request
	 */
	private void processRequest(String request){
		//TODO parse string
		System.out.println("received request: " + request);
		
		/*
		 * TODO
		 * if request is createPoll
		 *  { //generate pollID 
		 *    //using java.util.uuid or APACHE lib
		 * 		import java.util.UUID;
				String uuid = UUID.randomUUID().toString();
				System.out.println("uuid = " + uuid);
				
				//Apache lib
				org.apache.commons.lang.RandomStringUtils
			}
		 *  
		 */
	}
	
	@Override
	protected void finalize() throws Throwable {
		System.out.println("AdminWorker dead");
		in.close();
		outToClient.close();
		clientSocket.close();
	}
	
}
