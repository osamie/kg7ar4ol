package unitTests;

import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import model.Poll;
import model.PollsManager;

import server.PollServer;
import client.AdminClient;
import client.Client;
import adminGUI.AdminGUI;


public class Milestone3Testing {
	
	static PrintWriter out;
	int numOfPolls;
	int go = 0;
	static PollServer server = new PollServer();
	static PollsManager manager;
	/*@After 
	public void afterClass() throws Exception
	{
		
	}*/
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		server.testingStartListeners();
		manager = PollsManager.getInstance();
		out = new PrintWriter(new FileWriter("outputfile.txt", true)); 
	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		out.close();
	}
	@Test
	public final void massVoting() {
		assertTrue(massVotes(1000));
	}
	@Test
	public final void massAdminPolls()
	{
		assertTrue(massPolls(1000));
	}
	@Test
	public final void massAdmins()
	{
		assertTrue(massAdminClients(1000));
	}
	@Test
	public final void concurrentVotes()
	{
		assertTrue(concurVotes(50));
	}
	

	/*
	 * Puts a mass amount of votes on one poll.
	 */
	public boolean massVotes(int numberOfVotes)
	{
		out.println(System.nanoTime() + " -------------Mass Votes Test--------- Voting " + numberOfVotes + " times on one poll.");
		boolean returnVal;
		int numVotes = numberOfVotes;
		long pollID;
		int votes[] = new int[10];
		
		client.AdminClient admin = new client.AdminClient(server.ADMIN_PORT);
		client.Client voters[] = new client.Client[numberOfVotes];
		
		pollID = admin.createPoll("test@test.com|Testing Question|2|Yes|No");
		
		System.out.println("Created poll with two options and pollID is: " + pollID);
		out.println(System.nanoTime() + " - Starting creating voters and vote");
		
		for(int i = 0; i < numberOfVotes; i++)
		{
			voters[i] = new client.Client(server.VOTING_PORT);
			voters[i].vote(pollID,1);
			voters[i].close();
		}
		out.println(System.nanoTime() + " - All clients have finished voting, Verifying");
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
			out.println(System.nanoTime() + " - Successly voted " + numberOfVotes + " times.");
			returnVal =true;
		}
		else
		{
			out.println(System.nanoTime() + " - Fail, all votes did not get through. " + votes[0] + " number of votes got through");
			System.out.println("Fail");
			returnVal = false;
		}
		

		return returnVal;
		
	}
	/*
	 * 
	 */
	public boolean massPolls(int numberOfPolls)
	{
		out.println(System.nanoTime() + " -------------Mass Polls Test---------Creating " + numberOfPolls + " for one admin.");
		AdminClient admin = new AdminClient(server.ADMIN_PORT);
		long pollID;
		int numOfCreatedPolls = 0;
		out.println(System.nanoTime() + " - Starting creating polls");
		for(int i = 0; i<numberOfPolls;i++)
		{
			pollID = admin.createPoll("test@test.com|poll" + i +"|2|yes|no");
			if(manager.getPollState(pollID)!=9)
			{
				numOfCreatedPolls++;
			}
			
		}
		out.println(System.nanoTime() + " - Finished creating polls, verifying");
		if(numOfCreatedPolls == numberOfPolls)
		{
			out.println(System.nanoTime() + " - Successly created " + numberOfPolls + " polls for the admin client.");
			return true;
		}
		out.println(System.nanoTime() + " - Fail, not all polls were created and stayed active for the admin. " + numOfCreatedPolls + " polls were created.");
		return false;
	}
	
	/*
	 * 
	 */
	public boolean massAdminClients(int numberOfAdmins)
	{
		boolean returnVal = true;
		AdminClient admins[] = new AdminClient[numberOfAdmins];
		
		out.println(System.nanoTime() + " -------------Mass Votes Test---------Creating " + numberOfAdmins + " Admin clients on the server.");
		out.println(System.nanoTime() + " - Starting creating Admins");
		
		for(int i = 0;i<numberOfAdmins;i++)
		{
			admins[i] = new AdminClient(server.ADMIN_PORT);
			
		}
		out.println(System.nanoTime() + " - Finished creating Admins, Verifying");
		for(int i = 0; i < numberOfAdmins;i++)
		{
			if(admins[i].isConnected() == false)
			{
				out.println(System.nanoTime() + " - Fail, not all admins were created and stay connected to the server.");
				returnVal = false;
			}
		}
		if(returnVal != false)
		{
			out.println(System.nanoTime() + " - Successly created " + numberOfAdmins + " Admin clients.");
			returnVal = true;
	
		}
		for(int i = 0; i<numberOfAdmins;i++)
		{
			admins[i].disconnect();
		}
		return returnVal;
	}
	
	public boolean concurVotes(int numberOfVotes)
	{
		boolean returnVal = true;
		long pollID;
		int votes[] = new int[10];
		voter voting[] = new voter[numberOfVotes];
		AdminClient admin = new AdminClient(server.ADMIN_PORT);
		
		pollID = admin.createPoll("test@test.com|poll|2|yes|no");
		
		out.println("Setting up voters");
		for(int i = 0; i < numberOfVotes;i++)
		{
			voting[i] = new voter(this,pollID);
			voting[i].start();
		}
		out.println("Voters setup");
		go = 1;
		out.println("voters started voting");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("voters voted");
		votes = manager.getVotes(pollID);
		if(votes[0] == numberOfVotes)
		{
			out.println("Success");
			returnVal = true;
		}
		else
		{
			out.println("Fail " + votes[0]);
			returnVal = false;
		}
		
		
		return returnVal;
		
		
	}
}
class voter extends Thread
{
	Milestone3Testing global;
	Client voter;
	long pollID;
	voter(Milestone3Testing global, long pollID)
	{
		this.global = global;
		voter = new Client(global.server.VOTING_PORT);
		this.pollID = pollID;
	}
	
	public void run()
	{
		while(global.go == 0)
		{}
		voter.vote(pollID, 1);
		
	}
}