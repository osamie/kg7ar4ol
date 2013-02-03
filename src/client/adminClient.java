package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class adminClient {

	private Socket streamSocket;
	private BufferedReader in;
	private PrintWriter out;
	BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
	private int polls[] = new int[10];
	private int numOfPolls;
	public adminClient(int portNum)
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
			out = new PrintWriter(streamSocket.getOutputStream(), true);
			in = new BufferedReader( new InputStreamReader( streamSocket.getInputStream()));
		} catch (IOException e2) {
			System.err.println("Couldn't get I/O connection");
			System.exit(1);
		} 
	
	
	}
	public int connect(String numOfOptions, String emailAddress)
	{
		String msgToSend = "->";
		int gameID = 0;
		
			
			msgToSend = msgToSend + " " + numOfOptions + " " + emailAddress;
			out.println(msgToSend);

			try {
				gameID = Integer.decode(in.readLine());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		return gameID;
	}
	public void startPoll(String pollID) 
	{
		String msgToSend = "(+)";
		
		msgToSend = msgToSend + pollID;
		out.println(msgToSend);
		
	}
	public void pausePoll(String pollID)
	{
		String msgToSend = "(!)";
		
		
		msgToSend = msgToSend + pollID;
		out.println(msgToSend);
	}
	public void stopPoll(String pollID)
	{
		String msgToSend = "(X)";
		
		msgToSend = msgToSend + pollID;
		out.println(msgToSend);
	}
	public void clearPoll(String pollID)
	{
		String msgToSend = "(-)";
			
		msgToSend = msgToSend + pollID;
		out.println(msgToSend);
	}
	public void resumePoll(String pollID)
	{
		String msgToSend = "(0)";
			
		msgToSend = msgToSend + pollID;
		out.println(msgToSend);
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
		adminClient myPoll2 = new adminClient(port);
		adminClient myPoll3 = new adminClient(port);
		adminClient myPoll4 = new adminClient(port);
		adminClient myPoll5 = new adminClient(port);
		
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
		int connected = 1;
		int port = 5000;
		String decision = "";
		
		if (args.length > 0) {
		    try {
		        port = Integer.parseInt(args[0]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + " must be an integer");
		        System.exit(1);
		    }
		}
		
		
		adminClient myPoll = new adminClient(port);
		
		myPoll.test1();
		myPoll.test2(port);
		
		
		
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
