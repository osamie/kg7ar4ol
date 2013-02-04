package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import server.PollServer;


public class AdminClient {

	private Socket streamSocket;
	private BufferedReader in;
	private PrintWriter outToServer;
	BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
	private int numOfPolls;
	private int polls[] = new int[10];
	

	/**
	 * Connects the client to the server and initializes the read and write steams/buffers
	 * @param portNum
	 */
	public AdminClient(int portNum)
	{		
		numOfPolls = 0;
		try {
			// Bind a socket to any available port on the local host machine. 
			streamSocket = new Socket("127.0.0.1", portNum);
		} catch (UnknownHostException e1) {
			System.err.println("Don't know about host");
			System.exit(1);
		} catch (IOException e2) {
			System.err.println("Couldn't get port" + portNum);
			System.exit(1);
		}
		try {
			outToServer = new PrintWriter(streamSocket.getOutputStream(), true);
			in = new BufferedReader( new InputStreamReader( streamSocket.getInputStream()));
		} catch (IOException e2) {
			System.err.println("Couldn't get I/O connection");
			System.exit(1);
		} 
	
	
	}
	
	/**
	 * Sends a 'create new poll' request to the server 
	 * @param numOfOptions
	 * @param emailAddress
	 * @return 
	 */
	public int connect(String numOfOptions, String emailAddress)
	{
		/*
		 * TODO the name of this method should be createPoll.
		 * Connection is already being done in the constructor 
		 */
		String msgToSend = "->";
		int id = 0;
			
		msgToSend = msgToSend + " " + numOfOptions + " " + emailAddress;
		outToServer.println(msgToSend);

		try {
			String fromServer = in.readLine();
			System.out.println("Server: " + fromServer);
			//TODO figure out how to use this id variable and ensure its uniqueness
//			id = Integer.decode(fromServer);
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return id;
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
	 * string format is methodName::arg1,agr2....
	 * @param str
	 * @return isValidInput
	 */
	public boolean processUserInput(String str){
		String [] array = str.split("::");
		int method;
		String []args;
		
		if(array.length < 2){
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
			if(args.length < 2){
				return false;
			}
			connect(args[0], args[1]);
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
//		myPoll.testCreatePoll();
		while(myPoll.streamSocket.isConnected()){
			
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
