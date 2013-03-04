package model;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class PollsManager {
	private ConcurrentHashMap<Long, Poll> polls;
	private Random random;
	public PollsManager() {
		polls = new ConcurrentHashMap<Long, Poll>();
		random = new Random();
	}
	
	
	/**
	 * Create a new Poll and add to the 
	 */
	public long createNewPoll(int adminID,String emailAdress,String [] list){
		long pollID = generatePollID();//generate a new pollID
		Poll newPoll = new Poll(pollID,adminID,emailAdress,list);
		polls.put(pollID, newPoll);
		return pollID;
	}
	
	private long generatePollID(){
		long pollID = -1;
		do{
			//generate pollIDs until a unique ID is generated 
			pollID = random.nextLong();
		}while(polls.contains(pollID)); //rejection criterion
		return pollID;
	}
	
	/**
	 * Sends a 'startPoll' request to server (i.e now allow votes for this poll)
	 * @param pollID
	 */
	public void startPoll(long pollID) 
	{
		if(polls.containsKey(pollID)){
			Poll poll = polls.get(pollID);
		}
	}
	
	/**
	 * Sends a 'pausePoll' request to server. Temporarily deactivate poll 
	 * (i.e temporarily disallow votes for this poll)
	 * 
	 * @param pollID
	 */
	public void pausePoll(long pollID)
	{
		String msgToSend = "(!)";
		
		
		msgToSend = msgToSend + pollID;
		
	}
	
	/**
	 * Sends a 'stopPoll' request to server (i.e permanently deactivate the poll.
	 * Results should be collected).
	 * 
	 * @param pollID
	 */
	public void stopPoll(long pollID)
	{
		String msgToSend = "(X)";
		
		msgToSend = msgToSend + pollID;
	
	}
	
	/**
	 * Sends a 'clearPoll' request to the server (i.e discard all votes for this poll...poll remains active)
	 * @param pollID
	 */
	public void clearPoll(long pollID)
	{
		String msgToSend = "(-)";
			
		msgToSend = msgToSend + pollID;
	
	}
	
	/**
	 * Sends a 'resumePoll' request to server (i.e poll should be reactivated if not active)
	 * @param pollID
	 */
	public void resumePoll(long pollID)
	{
		String msgToSend = "(0)";
			
		msgToSend = msgToSend + pollID;
		
	}
}
