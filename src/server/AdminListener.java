package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;


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
				System.out.println("Listening for Admins...");
				clientSocket = serverSocket.accept(); //accept connection from ADMIN client
							
				System.out.println("New Admin connected");
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
 *  Each spawned AdminWorker is responsible for handling/serving each connected 
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
		  Provide the connected AdminClient with an adminID
		  
		  PROCESS:
		 * Right after admins are now connected;
		 * Client: if (adminID == 0) send "adminID:0"  to Server and wait for your new adminID 
		 * Server: waits for the first client message:
		 	If message == "adminID:0" {generate and send new adminID;  //"adminID:<uniqueID>"}

		 */
//		outToClient.println("**You are now connected as: " + clientSocket.getLocalSocketAddress().toString() + "**"); 
		System.out.println("**New admin connnected at port: " + clientSocket.getPort() + "**");
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
		String option = request.substring(0,3);
		if(request.contains("->") == true)
		{
			Random rdg = new Random();
			long temp = 0;
			//While(Check if ID exists)
			//generate another id.
			temp = rdg.nextLong();
			outToClient.println("$ " + String.valueOf(temp));
		}
		else if(request.contains("->"))
		{
			
		}
		else if(request.contains("(+)"))
		{
			
		}
		else if(request.contains("(!)"))
		{
			
		}
		else if(request.contains("(X)"))
		{
			
		}
		else if(request.contains("(-)"))
		{
			
		}
		else if(request.contains("(0)"))
		{
			
		}
		
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
	/**
	 * Sends a 'startPoll' request to server (i.e now allow votes for this poll)
	 * @param pollID
	 */
	public void startPoll(String pollID) 
	{
		String msgToSend = "(+)";
		
		msgToSend = msgToSend + pollID;
		
		
	}
	
	/**
	 * Sends a 'pausePoll' request to server. Temporarily deactivate poll 
	 * (i.e temporarily disallow votes for this poll)
	 * 
	 * @param pollID
	 */
	public void pausePoll(String pollID)
	{
		String msgToSend = "(!)";
		
		
		msgToSend = msgToSend + pollID;
		
	}
	
	/**
	 * Sends a 'stopPoll' request to server (i.e permanently deactivate the poll.
	 * Results should be collected).
	 * 
	 * @param pollID
	 */
	public void stopPoll(String pollID)
	{
		String msgToSend = "(X)";
		
		msgToSend = msgToSend + pollID;
	
	}
	
	/**
	 * Sends a 'clearPoll' request to the server (i.e discard all votes for this poll...poll remains active)
	 * @param pollID
	 */
	public void clearPoll(String pollID)
	{
		String msgToSend = "(-)";
			
		msgToSend = msgToSend + pollID;
	
	}
	
	/**
	 * Sends a 'resumePoll' request to server (i.e poll should be reactivated if not active)
	 * @param pollID
	 */
	public void resumePoll(String pollID)
	{
		String msgToSend = "(0)";
			
		msgToSend = msgToSend + pollID;
		
	}
	
}
