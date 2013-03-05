package unitTests;

/*
 * INFO
 * 	Message Format:	email|question|number of options|option1|option2|etc...
 * 			Returns:	long pollID
 */

import static org.junit.Assert.assertTrue;

import java.util.Scanner;

import model.Poll;
import model.PollsManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.PollServer;
import client.AdminClient;
import client.Client;
import adminGUI.AdminGUI;

public class milestone2Testing {

	private int polls[] = new int[10];
	int numOfPolls;
	static PollServer server = new PollServer();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	 
		server.testingStartListeners();
	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	
		
	}

	@Test
	public final void testSessionControl() {
		assertTrue(sessionControlTest());
	}
	@Test
	public final void testInvalidVotes()
	{
		assertTrue(invalidVotesTest());
	}
	
	
	@Test
	public final void testRepeatedVotes()
	{
		int testCount = 5;
		assertTrue(repeatedVotesTest(testCount));
	}
	@Test
	public final void testLateVotes()
	{
		assertTrue(lateVotesTest());
	}
	/*
	@Test
	public final void testRealTime()
	{
		assertTrue(realTimeTest());
	}
	*/
	
	private boolean sessionControlTest()
	{
		AdminClient admin = new AdminClient(PollServer.ADMIN_PORT);
		long pollID = admin.createPoll("TEST@TEST.com|Session Control|2|Yes|No");
		PollsManager manager = PollsManager.getInstance();
		
		  if(manager.getPollState(pollID) != Poll.RUNNING)
		  {
			  System.out.println("Poll did not create properly.");
			  return  false;
		  }
		 
		System.out.println("TEST - Pausing poll: " + pollID);
		System.out.println("TEST INFO - State of poll before: " + manager.getPollState(pollID));
		admin.pausePoll(String.valueOf(pollID));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("TEST INFO - State of poll after: " + manager.getPollState(pollID));
		
		
		  if(manager.getPollState(pollID) != Poll.PAUSED) 
		  {
			  System.out.println("Poll did not pause.");
			  return false;
		  }
		 
		System.out.println("TEST - Resuming poll: " + pollID);
		System.out.println("TEST INFO - State of poll before: " + manager.getPollState(pollID));
		admin.resumePoll(String.valueOf(pollID));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("TEST INFO - State of poll after: " + manager.getPollState(pollID));
		  

		if(manager.getPollState(pollID) != Poll.RUNNING)
		{
			System.out.println("Poll did not resume.");
			return false;
		}
		
		
		System.out.println("TEST - Stopping poll: " + pollID);
		System.out.println("TEST INFO - State of poll before: " + manager.getPollState(pollID));
		admin.stopPoll(String.valueOf(pollID));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("TEST INFO - State of poll after: " + manager.getPollState(pollID));
		  

		if(manager.getPollState(pollID) != -1)
		{
			System.out.println("Poll did not stop.");
			return false;
		}
		
		System.out.println("All poll action successful");
		
		return true;
	}
	
	private boolean invalidVotesTest() {
		
		AdminClient admin = new AdminClient(PollServer.ADMIN_PORT);
		Client testClient = new Client(PollServer.VOTING_PORT);
		long pollID = admin.createPoll("TEST@TEST.com|Invalid Votes|2|Yes|No");
		PollsManager manager = PollsManager.getInstance();
		
		if(manager.getPollState(pollID) != Poll.RUNNING)
		{
			System.out.println("Poll did not create properly.");
			return  false;
		}
		
		System.out.println("TEST - Send vote with invalid to poll ID: " + pollID);
		System.out.println("TEST INFO - Count of votes before for option 1: " + manager.getVotes(pollID)[0]);
		testClient.vote(pollID + 1,1);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("TEST INFO - State of poll after for option 1: " + manager.getVotes(pollID)[0]);
	
		if (manager.getVotes(pollID)[0] != 0) 
		{
			System.out.println("Server did not ignore invalid pollID");
			return false;
		}
		 
		System.out.println("TEST - Send vote with invalid vote option to Poll ID: " + pollID);
		System.out.println("TEST INFO - Count of votes before for option 1: " + manager.getVotes(pollID)[0]);
		System.out.println("TEST INFO - Count of votes before for option 2: " + manager.getVotes(pollID)[1]);
		testClient.vote(pollID,3);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("TEST INFO - State of poll after for option 1: " + manager.getVotes(pollID)[0]);
		System.out.println("TEST INFO - State of poll after for option 2: " + manager.getVotes(pollID)[1]);
	
		if (manager.getVotes(pollID)[0] != 0 || manager.getVotes(pollID)[1] != 0) 
		{
			System.out.println("Server did not ignore invalid vote option");
			return false;
		}
		
		System.out.println("Server ignored votes with invalid PollID and invaild Vote option");
		
		return true;
	}
	private boolean lateVotesTest() {
		
		AdminClient admin = new AdminClient(PollServer.ADMIN_PORT);
		Client testClient = new Client(PollServer.VOTING_PORT);
		long pollID = admin.createPoll("TEST@TEST.com|Poll ID|2|Yes|No");
		PollsManager manager = PollsManager.getInstance();
		
		if(manager.getPollState(pollID) != Poll.RUNNING)
		{
			System.out.println("Poll did not create properly.");
			return  false;
		}
		
		admin.pausePoll(String.valueOf(pollID));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(manager.getPollState(pollID) != Poll.PAUSED)
		{
			System.out.println("Poll did not pause properly.");
			return  false;
		}
		
		System.out.println("TEST - Send vote after pause to poll ID: " + pollID);
		System.out.println("TEST INFO - Count of votes before for option 1: " + manager.getVotes(pollID)[0]);
		testClient.vote(pollID,1);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("TEST INFO - State of poll after for option 1: " + manager.getVotes(pollID)[0]);
	
		if (manager.getVotes(pollID)[0] != 0) 
		{
			System.out.println("Server did not ignore vote after pauseing");
			return false;
		}
		
		
		admin.stopPoll(String.valueOf(pollID));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(manager.getPollState(pollID) != -1)
		{
			System.out.println("Poll did not stop properly.");
			return  false;
		}
		
		//System.out.println("TEST - Send vote after stop to poll ID: " + pollID);
		//System.out.println("TEST INFO - Count of votes before for option 1: " + manager.getVotes(pollID)[0]);
		//testClient.vote(pollID,1);
		//try {
		//	Thread.sleep(1000);
		//} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		//System.out.println("TEST INFO - State of poll after for option 1: " + manager.getVotes(pollID)[0]);
	/*
		if (manager.getVotes(pollID)[0] != 0) 
		{
			System.out.println("Server did not ignore vote after stopping");
			return false;
		}
		*/
		System.out.println("Server ignored all late votes");
		
		return true;
	}
	
	public boolean repeatedVotesTest(int numberOfVotes)
	{
		Client testClient = new Client(PollServer.VOTING_PORT);
		AdminClient admin = new AdminClient(PollServer.ADMIN_PORT);
		long pollID = admin.createPoll("gasg@gsag.com|who are you|2|yes|no");
		PollsManager manager = PollsManager.getInstance();
		
		if(manager.getPollState(pollID) != Poll.RUNNING)
		{
			System.out.println("Poll did not create properly.");
			return  false;
		}
		
		for(int i = 0; i <numberOfVotes; i++)
		{	
			System.out.println("TEST - Send vote to poll ID: " + pollID);
			testClient.vote(pollID,1);	
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(manager.getVotes(pollID)[0] == 1)
		{
			System.out.println("TEST - Only one vote was counted");
			return true;
		}
		else
		{
			System.out.println("TEST - more then one vote was counted");
			return false;
		}

	}
	
	private boolean realTimeTest() {
		
		Client testClient1 = new Client(PollServer.VOTING_PORT);
		Client testClient2= new Client(PollServer.VOTING_PORT);
		Client testClient3 = new Client(PollServer.VOTING_PORT);
		Client testClient4 = new Client(PollServer.VOTING_PORT);
		Client testClient5 = new Client(PollServer.VOTING_PORT);
		Client testClient6 = new Client(PollServer.VOTING_PORT);
		Client testClient7 = new Client(PollServer.VOTING_PORT);
		Client testClient8 = new Client(PollServer.VOTING_PORT);
		Client testClient9 = new Client(PollServer.VOTING_PORT);
		Client testClient10 = new Client(PollServer.VOTING_PORT);
		AdminGUI testAdminGUI = new AdminGUI();
		
		Scanner userInput = new Scanner(System.in);
		String input;
		
		System.out.println("Create a poll using GUI with 3 options");
		System.out.println("Enter corresponding PollID:");
		input = userInput.next();
		
		long pollID = Long.parseLong(input);
		
		
		try {
			testClient1.vote(pollID,1);
			Thread.sleep(1000);
			testClient2.vote(pollID,1);
			Thread.sleep(1000);
			testClient3.vote(pollID,1);
			Thread.sleep(1000);
			testClient4.vote(pollID,1);
			Thread.sleep(1000);
			testClient5.vote(pollID,1);
			Thread.sleep(1000);
			testClient6.vote(pollID,1);
			Thread.sleep(1000);
			testClient7.vote(pollID,1);
			Thread.sleep(1000);
			testClient8.vote(pollID,1);
			Thread.sleep(1000);
			testClient9.vote(pollID,1);
			Thread.sleep(1000);
			testClient10.vote(pollID,1);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	

}
