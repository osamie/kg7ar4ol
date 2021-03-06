package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Random;

import model.Observer;
import model.PollsManager;


/**
 * AdminListener is a TCP server where each worker accepts the admin messages
 */
class AdminListener extends Thread{
	private ServerSocket serverSocket;
    Socket clientSocket;
    protected PollsManager pollsManager;
	
	public AdminListener(int port,PollsManager manager) {
		pollsManager = PollsManager.getInstance();
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
				new AdminWorker(clientSocket,pollsManager).start(); //assign a worker thread to server new client
			} catch (SocketException e2) { System.out.println("Done"); System.exit(0); }
			catch (IOException e) { e.printStackTrace(System.err); System.exit(1);  }
		}
	}
	public void finalize()
	{
		   try { serverSocket.close(); } catch (IOException e) {}
	}

	@Override
	public void run() {
		listen();
	}
}

/**
 *  Each spawned AdminWorker is responsible for handling/serving each connected 
 *  ADMIN's requests/messages
 */
class AdminWorker extends Thread implements Observer{
	PrintWriter outToClient;
	BufferedReader in;
	Socket clientSocket;
	private PollsManager pollsManager;
	
	public AdminWorker(Socket socket, PollsManager manager) {
		/*
		 * TODO check for any referencing issues 
		 * - reassignment of clientSocket variable in Listener.listen()
		 */
		clientSocket = socket;
		pollsManager = manager;
		
		//register itself to listen for changes to polls
		pollsManager.addObserver(this);
		
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
//		request = request.trim();
		
		if(request.contains("->"))
		{
			/*
			 * Create a poll.
			 * Message format: emailAddress | Question | #OfOptions | options... 
			 */
			request = request.replace("->", "");
			
			String [] params = request.split("\\|");
			if (params.length<4){		
				return; //a wrong message format
			}
			else{
				String emailAddress=params[0];
				String pollQuestion = params[1];
				System.out.println(params[2]);
				int numOfOptions = Integer.parseInt(params[2]);
				String[]optionsList = new String[numOfOptions];
				int optionsCountIndex=3;
				int paramsIndex=optionsCountIndex;
				StringBuilder pollUpdateMessage=new StringBuilder();
				
				//populate optionsList
				for(int i=0;i<optionsList.length;i++){
					if(paramsIndex>=params.length) break;
					optionsList[i] = params[paramsIndex++];
					pollUpdateMessage.append("|0");
				}
				
				long pollID = pollsManager.createNewPoll(0, emailAddress, optionsList);
				System.out.println("new pollID:" + pollID);
				System.out.println("has poll?"+pollsManager.hasPoll(pollID));
				
				//send the new pollID TODO: remove this message...fix on client as well 
				outToClient.println("$ " + String.valueOf(pollID));
				
				/*String message = "*% "+pollID+ "|" +optionsList.length + pollUpdateMessage.toString();
				
				//send poll update to client
				outToClient.println(message);
				
				System.out.println(message);*/
				
				int []votesCount = new int[optionsList.length];
//				Arrays.fill(votesCount, 0); //empty votes count upon startup
				update(pollID,votesCount,pollsManager.getPollState(pollID));
			}			
		}
		else if(request.contains("(+)"))
		{
			//startPoll
		}
		else if(request.contains("(!)"))
		{
			/*
			 * pausePoll
			 * message format: pollID
			 */
			request = request.replace("(!)", "").trim();
			long pollID = Long.parseLong(request);
			pollsManager.pausePoll(pollID);
		}
		else if(request.contains("(X)"))
		{
			/*
			 * stopPoll
			 * message format: pollID
			 */
			request = request.replace("(X)", "").trim();
			
			long pollID = Long.parseLong(request);
			pollsManager.stopPoll(pollID);
//			System.out.println("pollstate:" + pollsManager.getPollState(pollID));
		}
		else if(request.contains("(-)"))
		{
			/*
			 * clearPoll
			 * message format: pollID
			 */
			request = request.replace("(-)", "").trim();
			
			long pollID = Long.parseLong(request);
			System.out.println("pollstate before:" + pollsManager.getPollState(pollID));
			pollsManager.clearPoll(pollID);
			System.out.println("pollstate:" + pollsManager.getPollState(pollID));
		}
		else if(request.contains("(0)"))
		{
			/*
			 * resumePoll
			 * message format: pollID
			 */
			request = request.replace("(0)", "").trim();
			
			long pollID = Long.parseLong(request);
			System.out.println("\npollstate before:" + pollsManager.getPollState(pollID));
			pollsManager.resumePoll(pollID);
			System.out.println("pollstate:" + pollsManager.getPollState(pollID));
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		System.out.println("AdminWorker dead");
		in.close();
		outToClient.close();
		clientSocket.close();
	}

	@Override
	public void update(long pollID, int[] count,int pollState) {
		StringBuilder pollUpdateMessage=new StringBuilder(); 
		for(int i=0;i<count.length;i++){
			pollUpdateMessage.append("|"+count[i]);
		}
		String message = "*% "+pollID+ "|" + pollState + "|" +count.length + pollUpdateMessage.toString();
		
		//send poll update to client
		outToClient.println(message);
		
		System.out.println(message);
		
	}
}
