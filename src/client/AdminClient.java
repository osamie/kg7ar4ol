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
	private int polls[] = new int[10];
	private int numOfPolls;
	

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
		String [] array = str.split("::",1);
		int method;
		String []args;
		
		if(array.length < 2) return false;
		try{
			method = Integer.parseInt(array[0]);
			args = array[1].split(","); //get the arguments for the function
		}catch(Exception e){
			System.out.println("does not recognise the method call");
			return false;
		}
		
		switch (method) {
		case 0:{ 
			System.out.println("herre");
			//connect(String numOfOptions, String emailAddress)
			if(args.length < 2){
				System.out.println("invalid connect arguments...USAGE:'0::5,osamie2002@gmail.com'");
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
			return true;
		}
		case 4:{
			//clearPoll(String pollID)
			return true;
		}
		case 5:{
			//resumePoll(String pollID)
			return true;
		}

		default:
			return false;
		}
		
	}
	
	public void testConnect(){
		connect("5","mock@mockdomain.ca");
	}
	
	public void test1()
	{
        
		//Test Section
		System.out.println("Creating five polls");
		polls[numOfPolls] = connect("5","mock@mockdomain.ca");
		numOfPolls++;
		polls[numOfPolls] = connect("4","mock@mockdomain.ca");
		numOfPolls++;
		polls[numOfPolls] = connect("3","mock@mockdomain.ca");
		numOfPolls++;
		polls[numOfPolls] = connect("2","mock@mockdomain.ca");
		numOfPolls++;
		polls[numOfPolls] = connect("1","mock@mockdomain.ca");
		numOfPolls++;
		System.out.println("Printing created polls ID");
		
		for(int i = 0; i < numOfPolls; i++)
		{
			System.out.println("Poll " + i + " has an ID of " + polls[i]);
		}
	}
	
	public void test2(int port)
	{
		System.out.println("Creating five polls with 5 different admins");
		AdminClient myPoll2 = new AdminClient(port);
		AdminClient myPoll3 = new AdminClient(port);
		AdminClient myPoll4 = new AdminClient(port);
		AdminClient myPoll5 = new AdminClient(port);
		
		polls[numOfPolls] = connect("1", "email1@e.ca");
		myPoll2.polls[myPoll2.numOfPolls] = myPoll2.connect("2", "email2@e.ca");
		myPoll3.polls[myPoll3.numOfPolls] = myPoll3.connect("3", "email3@e.ca");
		myPoll4.polls[myPoll4.numOfPolls] = myPoll4.connect("4", "email4@e.ca");
		myPoll5.polls[myPoll5.numOfPolls] = myPoll5.connect("5", "email5@e.ca");
		
		System.out.println("Printing created polls ID");
		
		System.out.println("Admin 1's poll has an ID of " + polls[numOfPolls]);
		System.out.println("Admin 2's poll has an ID of " + myPoll2.polls[myPoll2.numOfPolls]);
		System.out.println("Admin 3's poll has an ID of " + myPoll3.polls[myPoll3.numOfPolls]);
		System.out.println("Admin 4's poll has an ID of " + myPoll4.polls[myPoll4.numOfPolls]);
		System.out.println("Admin 5's poll has an ID of " + myPoll5.polls[myPoll5.numOfPolls]);
		
	}
	public void test3()
	{
		System.out.println("Sending all different messages");
		startPoll(String.valueOf(polls[0]));
		System.out.println("Sent Start.");
		pausePoll(String.valueOf(polls[0]));
		System.out.println("Sent Pause.");
		resumePoll(String.valueOf(polls[0]));
		System.out.println("Sent Resume.");
		clearPoll(String.valueOf(polls[0]));
		System.out.println("Sent Clear.");
		stopPoll(String.valueOf(polls[0]));
		System.out.println("Sent Stop.");
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
		
		myPoll.testConnect();
		
		while(myPoll.streamSocket.isConnected()){
			
			System.out.print("\nSend Request: ");
			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			
			
			try {
				String str = in.readLine();
//				System.out.print(str);
				myPoll.processUserInput(str);
			} catch (IOException e) {
				
			}
		}
//		myPoll.test1();
//		myPoll.test2(port);
		
		
		
		/*while(connected == 1)
		{
			try {
				decision = myPoll.bufferRead.readLine();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(decision.equals("Connect") == true)
			{
				String emailAddress = "";
				String numOptions;
				System.out.print("Enter how many options: ");
				try {
					numOptions = bufferRead.readLine();	
					System.out.print("Enter your email address: ");
					emailAddress = bufferRead.readLine();
					
				} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
					
				myPoll.polls[myPoll.numOfPolls] = myPoll.connect(numOptions, emailAddress);
				myPoll.numOfPolls++;
			}
			else if(decision.equals("Start")== true)
			{
				System.out.print("What PollID would you like to start: ");
				myPoll.startPoll(bufferRead.readLine());
			}
			else if(decision.equals("Pause") == true)
			{
				System.out.print("What PollID would you like to pause: ");
				myPoll.pausePoll(bufferRead.readLine());
			}
			else if(decision.equals("stop")== true)
			{
				System.out.print("What PollID would you like to stop: ");
				myPoll.stopPoll(bufferRead.readLine());
			}
			else if(decision.equals("resume")== true)
			{
				System.out.print("What PollID would you like to resume: ");
				myPoll.resumePoll(bufferRead.readLine());
			}
			else if(decision.equals("clear")== true)
			{
				System.out.print("What PollID would you like to clear: ");
				myPoll.clearPoll(bufferRead.readLine());
			}
			
		}
*/
	}
}
