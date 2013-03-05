package model;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PollsManager is responsible for managing the collection of polls
 * in the system.
 *  
 * @author osamie
 *
 */
public class PollsManager {
	private ConcurrentHashMap<Long, Poll> polls;
	private Random random;
	
	/**
	 * 
	 *Initialization on demand class holder
	 */
	private static class InstanceHolder{
		//initialization phase on the JVM is guaranteed to be serial or non-concurrent
		public static PollsManager instance =  new PollsManager();
	}
	
	private PollsManager() {
		polls = new ConcurrentHashMap<Long, Poll>();
		random = new Random();
	}
	/**
	 * The InstanceHolder handles the initialization of the instance.
	 * Since the initialization phase on the JVM is non-concurrent, 
	 * the instance will only be created once and no further synchronization
	 * will be needed here.
	 *  
	 */
	public static PollsManager getInstance(){
		return InstanceHolder.instance;
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
		}while(polls.contains(pollID) && (pollID<1)); //rejection criterion
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
		//TODO print out or email poll results
		System.out.println("Ending Poll:" + pollID);
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
	
	public boolean hasPoll(long pollID){
		return polls.containsKey(pollID);
	}
	
	public int getPollState(long pollID){
		int state=-1;
		if(polls.containsKey(pollID)){
			state = polls.get(pollID).getState();
		}
		return state;
	}
	
	public void addVote(long pollID,int choice,int voterID){
		boolean voteAdded=false;
		
		int optionIndex = choice-1; //corresponds to an array index in Poll
		if(polls.containsKey(pollID)){
			voteAdded = polls.get(pollID).addVote(optionIndex,voterID);
		}
		if(voteAdded){
			//TODO notify observers
		}
		System.out.println("vote added?" + voteAdded);
	}
	
	/**
	 * Returns the number of votes as an array. 
	 * The index of the array represents the choice number.
	 * The value at the index position represents the number 
	 * of votes for that choice.
	 * 
	 * @param pollID
	 * @return null if the poll does not exist and array of int otherwise 
	 */
	public int[] getVotes(long pollID){
		if(polls.containsKey(pollID)){
			return polls.get(pollID).getVoteStats();
		}
		return null;
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
