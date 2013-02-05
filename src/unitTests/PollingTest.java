package unitTests;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.PollServer;

import client.AdminClient;
import client.Client;

public class PollingTest {
	private int polls[] = new int[10];
	int numOfPolls;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testAdminConnections() {
		int testCount = 1;
		assertTrue(connectionTest(testCount));
	}
	
	/**
	 *Test admin connections to server    
	 * @param numberOfAdmins
	 * @return false if any of the connections fail
	 */
	public boolean connectionTest(int numberOfAdmins){
		Boolean testResult = true;
		PollServer server = new PollServer();
		AdminClient []admins = new AdminClient[numberOfAdmins];
		server.startListeners(); //start listening for connections

		//Create Admins and test their connections  
		for(int i = 0; i<admins.length;i++){
			if (!(new AdminClient(PollServer.ADMIN_PORT).isConnected())){
				testResult = false;
				break;
			}
		}
		return testResult;
	}
	
	
	public void test1()
	{
        
		//Test Section
		/*System.out.println("Creating five polls");
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
		}*/
	}
	
	public void test2(int port)
	{
		/*
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
		*/
	}
	public void test3()
	{
		/*System.out.println("Sending all different messages");
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
		*/
	}

}
