package model;

import java.util.concurrent.ConcurrentHashMap;


public class LocalPollsManager extends Observable {
	private ConcurrentHashMap<Long, LocalPoll> localPolls;
	
	/**
	 * 
	 *Initialization on demand class holder
	 */
	private static class InstanceHolder{
		//initialization phase on the JVM is guaranteed to be serial or non-concurrent
		public static LocalPollsManager instance =  new LocalPollsManager();
	}
	
	private LocalPollsManager() {
		super();
		localPolls = new ConcurrentHashMap<Long, LocalPoll>();	
	}
	/**
	 * The InstanceHolder handles the initialization of the instance.
	 * Since the initialization phase on the JVM is non-concurrent, 
	 * the instance will only be created once and no further synchronization
	 * will be needed here.
	 *  
	 */
	public static LocalPollsManager getInstance(){
		return InstanceHolder.instance;
	}
	
	
	/**
	 * Create a new local Poll and add it to the collection of polls
	 * @return pollID
	 */
	public long createNewPoll(long pollID,String pollQuestion,int[] optionsList,int pollState){
		LocalPoll newPoll = new LocalPoll(pollID, pollQuestion, optionsList,pollState);
		localPolls.put(pollID, newPoll);
		return pollID;
	}
	
	/**
	 * Updates the votes results of the local poll identified with the given pollID 
	 * 
	 * @param pollID
	 * @param count
	 */
	public void updatePoll(long pollID,int[]count,int state) {
		LocalPoll poll = localPolls.get(pollID);
		poll.update(count,state);
		
		updateObservers(pollID,count,state); //notify the GUI and/or other observers
	}
	
	public int getVoteCount(long pollID,int choice){
		return localPolls.get(pollID).getVoteCount(choice-1);
	}
	
	public boolean hasPoll(long pollID){
		return localPolls.containsKey(pollID);
	}
	
	public int getPollState(long pollID){
		int state=-1;
		if(localPolls.containsKey(pollID)){
			state = localPolls.get(pollID).getState();
		}
		return state;
	}
}

class LocalPoll {
	private int[] votesCount;
	private long pollID;
	String pollQuestion;
	int pollState;
	
	public LocalPoll(long pollID,String pollQuestion,int [] count,int state) {
		this.pollID = pollID;
		this.pollQuestion = pollQuestion;
		votesCount = new int[count.length];
		pollState = state;
		System.arraycopy(count, 0, votesCount, 0, count.length);
	}
	
	public int getVoteCount(int optionsIndex){
		if(optionsIndex >= votesCount.length) return 0;
		return votesCount[optionsIndex];
	}
	
	public int getState(){
		return pollState;
	}
	
	public synchronized void update(int [] count,int pollState) {
		this.pollState = pollState;
		System.arraycopy(count, 0, this.votesCount, 0, count.length);
	}
	
}
