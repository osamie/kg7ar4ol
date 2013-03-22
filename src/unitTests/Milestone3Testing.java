package unitTests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import model.Poll;
import model.PollsManager;

import server.PollServer;
import client.AdminClient;
import client.Client;
import adminGUI.AdminGUI;


public class Milestone3Testing {

	int numOfPolls;
	static PollServer server = new PollServer();
	static PollsManager manager;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		server.testingStartListeners();
		manager = PollsManager.getInstance();
	}

	@Test
	public final void massVoting() {
		assertTrue(massVotes(10000));
	}
	@Test
	public final void massAdminPolls()
	{
		assertTrue(massPolls(5000));
	}
	@Test
	public final void massAdmins()
	{
		assertTrue(massAdminClients(5000));
	}
	

	/*
	 * Puts a mass amount of votes on one poll.
	 */
	public boolean massVotes(int numberOfVotes)
	{
		int numVotes = numberOfVotes;
		long pollID;
		int votes[] = new int[10];
		
		client.AdminClient admin = new client.AdminClient(server.ADMIN_PORT);
		client.Client voters[] = new client.Client[numberOfVotes];
		pollID = admin.createPoll("test@test.com|Testing Question|2|Yes|No");
		System.out.println("Created poll with two options and pollID is: " + pollID);
		for(int i = 0; i < numberOfVotes; i++)
		{
			voters[i] = new client.Client(server.VOTING_PORT);
			voters[i].vote(pollID,1);
			voters[i].close();
		}
		System.out.println(numberOfVotes + " clients voted for option 1");
		votes = manager.getVotes(pollID);
		System.out.println("Number of votes for option 1: " + votes[0]);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		admin.stopPoll(String.valueOf(pollID));
		admin.disconnect();
		
		if(votes[0] == numberOfVotes)
		{
			return true;
		}
		else
		{
			System.out.println("Fail");
		}
		return false;
		
	}
	public boolean massPolls(int numberOfPolls)
	{
		AdminClient admin = new AdminClient(server.ADMIN_PORT);
		long pollID;
		int numOfCreatedPolls = 0;
		for(int i = 0; i<numberOfPolls;i++)
		{
			pollID = admin.createPoll("test@test.com|poll" + i +"|2|yes|no");
			if(manager.getPollState(pollID)!=9)
			{
				numOfCreatedPolls++;
			}
			
		}
		if(numOfCreatedPolls == numberOfPolls)
		{
			return true;
		}
		return false;
	}
	public boolean massAdminClients(int numberOfAdmins)
	{
		
		AdminClient admins[] = new AdminClient[numberOfAdmins];
		for(int i = 0;i<numberOfAdmins;i++)
		{
			admins[i] = new AdminClient(server.ADMIN_PORT);
			
		}
		for(int i = 0; i < numberOfAdmins;i++)
		{
			if(admins[i].isConnected() == false)
			{
				return false;
			}
		}
			return true;
	}
}