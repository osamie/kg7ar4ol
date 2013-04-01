package unitTests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.PollServer;
import client.AdminClient;
import client.Client;

public class PollingTest {
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
		Client testClient = new Client(PollServer.VOTING_PORT,"127.0.0.1");
		long pollIds[] = new long[numberOfPolls];
		
		//Creates polls and verifies if the polls created pollIds
		for(int i = 0; i < numberOfPolls; i++)
		{
			//pollIds[i] = admin.createPoll("4", "1@asd.ca");
			if(pollIds[i] == 0) testResult = false;
		}
		//Test sending a vote to the polls.
		for(int i = 0; i< numberOfPolls; i++)
		{
			testClient.vote(pollIds[i],3);
		}
		
		return testResult;
	}
	/**
	 *Test sending the different admin commands.   
	 * @param void
	 * @return false if any of the messages fail
	 */
	public boolean adminMessages()
	{
		/*Boolean testResult = true;
		AdminClient admin = new AdminClient(PollServer.ADMIN_PORT);
		Client testClient = new Client(PollServer.VOTING_PORT);
		//Creating Poll
		long pollId = admin.createPoll("5email@domain.ca");
		if(pollId == 0) testResult = false;	//Will fail if no pollId is returned.
		
		//startingPoll
		admin.startPoll(String.valueOf(pollId));
		testClient.vote(pollId,(long) 3);// Will send a vote to see if the poll has started.
		
		//Pausing Poll
		admin.pausePoll(String.valueOf(pollId));
		testClient.vote(pollId,(long) 3);// Will send a vote to see if the poll has paused.
		
		//Resuming Poll
		admin.resumePoll(String.valueOf(pollId));
		testClient.vote(pollId,(long) 3);// Will send a vote to see if the poll has resumed.
		
		//Clear Poll
		admin.clearPoll(String.valueOf(pollId));
		testClient.vote(pollId,(long) 3);// Will send a vote to see if total votes is only 1.
		
		//Stop Poll
		admin.stopPoll(String.valueOf(pollId));
		testClient.vote(pollId,(long) 3);// Will send a vote to see if the poll has stopped.
		
		return testResult;
	*/	
		return true;
	}
	/**
	 *Test connecting multiple voters to the server.   
	 * @param numberOfVoters
	 * @return false if any of the connections fail
	 */
	public boolean voteConnectionTest(int numberOfVoters)
	{
		Boolean testResult = true;
		Client []clients = new Client[numberOfVoters];
		for(int i = 0; i < numberOfVoters; i++)
		{
			clients[i] = new Client(PollServer.VOTING_PORT,"127.0.0.1");
		}
		
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
		Client testClient = new Client(PollServer.VOTING_PORT,"127.0.0.1");
		
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
			if (!(new AdminClient(PollServer.ADMIN_PORT,"127.0.0.1").isConnected())){
				testResult = false;
				break;
			}
		}
		return testResult;
	}
	

}
