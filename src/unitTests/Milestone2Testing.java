package unitTests;



import static org.junit.Assert.assertTrue;

import model.Poll;
import model.PollsManager;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.PollServer;
import client.AdminClient;
import client.Client;

public class Milestone2Testing {

	private int polls[] = new int[10];
	int numOfPolls;
	static PollServer server;
	
	@BeforeClass 
	public static void method()
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
	public final void testInvalidVotes()
	{
		assertTrue(invalidVotesTest());
	}
	
	@Test
	public final void testSessionControl() {
		assertTrue(sessionControlTest());
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
	
	@Test
	public final void testRealTime()
	{
		assertTrue(realTimeTest());
	}
	
	
	private boolean sessionControlTest()
	{
		AdminClient admin = new AdminClient(PollServer.ADMIN_PORT);
		long pollID = admin.createPoll("TEST@TEST.com|2|Poll ID|Yes|No");
		PollsManager manager = PollsManager.getInstance();
		/*
		 * If(server.poll.getstate(pollId) != RUNNING)
		 * {
		 * return  false;
		 * }
		 */
		
		admin.pausePoll(String.valueOf(pollID));
		
		
		
		  if(manager.getPollState(pollID) != Poll.PAUSED) 
		  {
		  return false;
		  }
		 
		
//		admin.resumePoll(String.valueOf(pollID));
		
		/*
		 * if(server.poll.getstate(pollId) != RUNNING)
		 * {
		 * return false;
		 * }
		 */
		
//		admin.stopPoll(String.valueOf(pollID));
		
		/*
		 * if (server.poll.getstate(pollId) != STOPPED) 
		 * {
		 * return false;
		 * }
		 */
		
		
		return true;
	}
	
	private boolean invalidVotesTest() {
		
		AdminClient admin = new AdminClient(PollServer.ADMIN_PORT);
		Client testClient = new Client(PollServer.VOTING_PORT);
		long pollId = admin.createPoll("TEST@TEST.com|2|Poll ID|Yes|No");
		
		testClient.vote(pollId + 1,1);
		
		/*
		 * if (server.poll.getVotes(pollId,1) != 0) 
		 * {
		 * return false;
		 * }
		 */
		
		testClient.vote(pollId,3);
		
		/*
		 * if (server.poll.getVotes(pollId,1) != 0 || server.poll.getVotes(pollId,2) != 0) 
		 * {
		 * return false;
		 * }
		 */
		
		return true;
	}
	private boolean lateVotesTest() {
		
		AdminClient admin = new AdminClient(PollServer.ADMIN_PORT);
		Client testClient = new Client(PollServer.VOTING_PORT);
		long pollId = admin.createPoll("TEST@TEST.com|2|Poll ID|Yes|No");
		
		admin.pausePoll(String.valueOf(pollId));
		
		testClient.vote(pollId,1);
		
		/*
		 * if (server.poll.getVotes(pollId, 1) != 0)
		 * {
		 * return false;
		 * } 
		 */
		
		admin.stopPoll(String.valueOf(pollId));
		testClient.vote(pollId,1);
		
		/*
		 * if (server.poll.getVotes(pollId, 1) != 0)
		 * {
		 * return false;
		 * } 
		 */
		
		return true;
	}
	
	private boolean realTimeTest() {
		
		return false;
	}
	/**
	 *Test sending multiple messages to the server one
	 *after the other.   
	 * @param numberOfVotes
	 * @return false if any of the votes fail
	 */
	public boolean repeatedVotesTest(int numberOfVotes)
	{
		Client testClient = new Client(PollServer.VOTING_PORT);
		AdminClient admin = new AdminClient(PollServer.ADMIN_PORT);
		long pollID ;
		pollID = admin.createPoll("gasg@gsag.com|2|who are you|yes|no");
		
		for(int i = 0; i <numberOfVotes; i++)
		{		
			testClient.vote(pollID,1);	
		}
		/*if(server.poll.getvotes(pollID,1) == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
		*/
		return true;
	}
	
	
	

}
