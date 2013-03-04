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
	 * Create a new Poll and add it to the collection of polls
	 * @return pollID
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
		if(polls.containsKey(pollID)){
			polls.get(pollID).pausePoll();
		}
	}
	
	/**
	 * 
	 * @param pollID
	 */
	public void stopPoll(long pollID)
	{
//		System.out.println("poll exists before:" + polls.containsKey(pollID));
		polls.remove(pollID);
//		System.out.println("poll exists AFTER:" + polls.containsKey(pollID));	
	}
	
	/**
	 * 
	 * @param pollID
	 */
	public void clearPoll(long pollID)
	{
		if(polls.containsKey(pollID)){
			polls.get(pollID).clearPoll();
		}
	}
	
	public void addVote(long pollID,int optionIndex,int voterID){
		boolean voteAdded=false;
		if(polls.containsKey(pollID)){
			voteAdded = polls.get(pollID).addVote(optionIndex,voterID);
		}
		if(voteAdded){
			//TODO notify observers
		}
	}
	
	public int[] getVoteCount(long pollID){
		int[] votes = null;
		if(polls.containsKey(pollID)){
			votes = polls.get(pollID).getVoteCount();
			System.out.println("votes count:" + votes.length);
		}
		return votes;
	}
	
	/**
	 * Sends a 'resumePoll' request to server (i.e poll should be reactivated if not active)
	 * @param pollID
	 */
	public void resumePoll(long pollID)
	{	
		if(polls.containsKey(pollID)){
			polls.get(pollID).resumePoll();
		}
	}
}
