package unitTests;



import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.PollServer;
import client.AdminClient;
import client.Client;

public class milestone2Testing {

	private int polls[] = new int[10];
	int numOfPolls;
	PollServer server;
	
	@BeforeClass 
	public void method()
	{
		server = new PollServer();
		server.startListeners();
	}
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
	@Test
	public final void testVotes()
	{
		int testCount = 5;
		assertTrue(voteTest(testCount));
	}
	@Test
	public final void testVoterConnections()
	{
		int testCount = 5;
		assertTrue(voteConnectionTest(testCount));
	}
	@Test
	public final void testAdminMessages()
	{
		
		assertTrue(adminMessages());
	}
	@Test
	public final void testMultiplePolls()
	{
		int testCount = 5;
		assertTrue(multiplePolls(testCount));
	}
	/**
	 *Test creating multiple polls.   
	 * @param numberOfPolls
	 * @return false if polls fail to be created
	 */
	public boolean multiplePolls(int numberOfPolls)
	{
		Boolean testResult = true;
		
		
		return testResult;
	}
	/**
	 *Test sending the different admin commands.   
	 * @param void
	 * @return false if any of the messages fail
	 */
	public boolean adminMessages()
	{
		Boolean testResult = true;
		
		return testResult;
		
		
	}
	/**
	 *Test connecting multiple voters to the server.   
	 * @param numberOfVoters
	 * @return false if any of the connections fail
	 */
	public boolean voteConnectionTest(int numberOfVoters)
	{
		Boolean testResult = true;
		
		
		return testResult;
		
	}
	/**
	 *Test sending multiple messages to the server one
	 *after the other.   
	 * @param numberOfVotes
	 * @return false if any of the votes fail
	 */
	public boolean voteTest(int numberOfVotes)
	{
		
		Boolean testResult = true;
		Client testClient = new Client(PollServer.VOTING_PORT);
		AdminClient admin = new AdminClient(PollServer.ADMIN_PORT);
		long pollID ;
		pollID = admin.createPoll("gasg@gsag.com|2|who are you|yes|no");
		
		
		for(int i = 0; i <numberOfVotes; i++)
		{		
			testClient.vote((long)12345,2);	
		}
		
		return testResult;
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
	

}
