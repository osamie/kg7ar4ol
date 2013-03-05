package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.concurrent.Semaphore;

import model.LocalPollsManager;

import server.PollServer;


public class AdminClient {

	Semaphore poll_sem, block_sem;
	static Long newPollId;
	
	Socket adminSocket;private BufferedReader in;
	private PrintWriter outToServer;
	BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
	private int numOfPolls;
	private int polls[] = new int[10];
	private long adminID = 0;
	private MessageListener messageReciever;

	/**
	 * Connects the client to the server and initializes the read and write steams/buffers
	 * @param portNum
	 */
	public AdminClient(int portNum)
	{			
		numOfPolls = 0;
		try {
			// Bind a socket to any available port on the local host machine. 
			adminSocket = new Socket("127.0.0.1", portNum);
			
		} catch (UnknownHostException e1) {
			System.err.println("Don't know about host");
			System.exit(1);
		} catch (IOException e2) {
			System.err.println("Couldn't get port" + portNum);
			System.exit(1);
		}
		try {
			outToServer = new PrintWriter(adminSocket.getOutputStream(), true);
			in = new BufferedReader( new InputStreamReader( adminSocket.getInputStream()));
		} catch (IOException e2) {
			System.err.println("Couldn't get I/O connection");
			System.exit(1);
		} 
		poll_sem = new Semaphore(1);//initialize semaphore value to 1
		block_sem = new Semaphore(1);
		messageReciever = new MessageListener(this,LocalPollsManager.getInstance());
		messageReciever.start();
	}
	
	/**
	 * Sends a 'create new poll' request to the server 
	 * @param numOfOptions
	 * @param emailAddress
	 * @return 
	 */
	public long createPoll(String message)
	{
		/*
		 * TODO the name of this method should be createPoll.
		 * Connection is already being done in the constructor 
		 */
		String msgToSend = "->" + message;
		String temp = "";
		try {
			block_sem.acquire();	//Acquires before sending.
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		outToServer.println(msgToSend); //sends the request
		
		//update GUI with "waiting for pollID" status 
		
		//Blocking wait for messageListener to receive and update this.newPollID 
		try {
			poll_sem.acquire();	//waits until the receiver has changed the poll id and releases its semaphore.
			block_sem.release(); //Releases its semaphore
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			poll_sem.release();	//Releases the second semaphore.	
		}
		return newPollId; //get newPollID
	}
	
	/**
	 * Sends a 'startPoll' request to server (i.e now allow votes for this poll)
	 * @param pollID
	 */
	public void startPoll(String pollID) 
	{
		String msgToSend = "(+)";
		msgToSend = msgToSend + pollID;
		outToServer.println(msgToSend);	
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
		outToServer.println(msgToSend);
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
		outToServer.println(msgToSend);
	}
	
	/**
	 * Sends a 'clearPoll' request to the server (i.e discard all votes for this poll...poll remains active)
	 * @param pollID
	 */
	public void clearPoll(String pollID)
	{
		String msgToSend = "(-)";			
		msgToSend = msgToSend + pollID;
		outToServer.println(msgToSend);
	}
	
	/**
	 * Sends a 'resumePoll' request to server (i.e poll should be reactivated if not active)
	 * @param pollID
	 */
	public void resumePoll(String pollID)
	{
		String msgToSend = "(0)";
			
		msgToSend = msgToSend + pollID;
		outToServer.println(msgToSend);
	}
	
	/**
	 * Validates user input and sends to server
	 * Input format is "methodName::arg1,agr2...."
	 * @param str
	 * @return isValidInput
	 */
	public boolean processUserInput(String str){
		String [] array = str.split("::");
		int method;
		String []args;
		
		if(array.length < 1){
			if(str.equals("help")){
				displayHelp();
				return true;
			}
			return false;
		}
		try{
			method = Integer.parseInt(array[0]);
			args = array[1].split(","); //get the arguments for the function
		}catch(Exception e){
			System.out.println("does not recognise the method call");
			return false;
		}
		
		switch (method) {
		case 0:{ 
			//connect(String numOfOptions, String emailAddress)
			if(args.length < 1){
				return false;
			}
			createPoll(args[0]);
			return true;
		}
		case 1:{
			//startPoll(String pollID)
			startPoll(args[0]);
			return true;
		}
		case 2:{
			//pausePoll(String pollID)
			pausePoll(args[0]);
			return true;
		}
		case 3:{
			//stopPoll(String pollID)
			stopPoll(args[0]);
			return true;
		}
		case 4:{
			//clearPoll(String pollID)
			clearPoll(args[0]);
			return true;
		}
		case 5:{
			//resumePoll(String pollID)
			resumePoll(args[0]);
			return true;
		}
		default:
			return false;
		}
		
	}
	/**
	 * Displays help information on the client's interface
	 */
	public void displayHelp(){
		System.out.println("METHOD CALLS:");
		System.out.println("CreatePoll:\t '0::<numOfOptions>,<emailAdress>'");
		System.out.println("startPoll:\t '1::<pollID>'");
		System.out.println("pausePoll:\t '2::<pollID>'");
		System.out.println("stopPoll:\t '3::<pollID>'");
		System.out.println("clearPoll:\t '4::<pollID>'");
		System.out.println("resumePoll:\t '5::<pollID>'");
	}
	
	public boolean isConnected(){
		return this.adminSocket.isConnected();
	}
	public void disconnect()
	{
		try {
			adminSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int port = PollServer.ADMIN_PORT;		
		if (args.length > 0) {
		    try {
		        port = Integer.parseInt(args[0]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + " must be an integer");
		        System.exit(1);
		    }
		}
		AdminClient myPoll = new AdminClient(port);
		while(myPoll.adminSocket.isConnected()){
			
			System.out.print("\nSend Request: ");
			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			
			try {
				String str = in.readLine();
				if (!myPoll.processUserInput(str)) {
					System.out.println("invalid arguments...USAGE:'<0-5>::<pollID>'\n Enter 'help' for manual");
				}
			} catch (IOException e) {
				
			}
		}
	}
}

/**
 * Listens to all messages from the server
 */
class MessageListener extends Thread
{
	BufferedReader fromServer;
	AdminClient adminClient;
	Socket socket;
	LocalPollsManager pollsManager;
	MessageListener(AdminClient client,LocalPollsManager manager)
	{
		socket = client.adminSocket;
		adminClient = client;
		pollsManager = manager;
		try {
			//acquires semaphore on AdminClient startup 
			adminClient.poll_sem.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			fromServer = new BufferedReader( new InputStreamReader( socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void run()
	{
		String input;
		while(socket.isClosed() == false)
		{
		try {
			input = fromServer.readLine();
			System.out.println(input);	
			processServerMessage(input);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		}		
	}

	private void processServerMessage(String input) {
		if(input.contains("$") == true)
		{
			//message received from server is a pollID
			System.out.println("TRUE");
			Long id = Long.parseLong(input.substring(input.indexOf("$") + 2));
			AdminClient.newPollId = id;
			System.out.println(id);
			//allow admin client use newPollID
			adminClient.poll_sem.release();
			
			try {
				adminClient.block_sem.acquire();//Waits until the client has the poll_sem
				adminClient.poll_sem.acquire();//Waits until the admin is done with the pollID
				adminClient.block_sem.release();//releases for next run through
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		else if(input.contains("*%")){
			/*
			 * Poll update received from server
			 * Expected message format:
			 * 		*%<pollID> | optionsCount | <option1Count> | <option2Count>...
			 */
			
			input = input.replace("*%", "").trim();
			String[] params = input.split("\\|");
			
			if(params.length<3) return;
			
			long pollID = Long.parseLong(params[0].trim());
			int optionsCount = Integer.parseInt(params[1].trim());
			int[] votesCount = new int[optionsCount]; 
			int paramsIndex=2;
			
			//populate votesCount array for use in creating/updating a localPoll
			for(int i=0;i<optionsCount;i++){
				if(paramsIndex>=optionsCount) break;
				votesCount[i]=Integer.parseInt(params[paramsIndex].trim());
			}
			
			//first, check for local copy 
			if(pollsManager.hasPoll(pollID)){
				pollsManager.updatePoll(pollID, votesCount); //update
			}else{
				//create a local copy of poll if non exists
				pollsManager.createNewPoll(pollID, " ", votesCount);
			}		
		}
	}
}
