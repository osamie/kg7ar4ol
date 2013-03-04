package model;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Poll extends Observable {
	private int adminID;
	private long pollID;
	private String emailAddress;
	private String[] options;
	private int[] votesCount;
	private int currentState;

	
	//States of the poll
	public static final int STOPPED = 9;
	public static final int RUNNING = 1;
	public static final int PAUSED = 0;
	
	public Poll(long pollID,int adminID,String emailAdress,String [] list) {
		//initialize class properties
		this.adminID = adminID;
		this.emailAddress = emailAdress; //user email address
		this.options = new String[list.length];
		this.votesCount = new int[list.length]; //initialized with zeros
		
		//copy content of list to this.options
		System.arraycopy(list, 0, options, 0, list.length); 
		
		this.currentState = RUNNING; //default state
	}
	
	/**
	 * Get the admin's id for this poll
	 * @return adminID
	 */
	public int getAdminID(){
		return adminID;
	}
	
	/**
	 * Get the id for this poll
	 * @return
	 */
	public long getPollID(){
		return pollID;
	}
	
	
	/**
	 * Get a string array containing the options for the poll. 
	 * @return array of options
	 */
	public String[] getOptions(){
		return options;
	}
	
	
	/**
	 * pause state: poll is paused (i.e not accepting votes) 
	 */
	public void pausePoll(){
		currentState = PAUSED;
	}
	
	public void resumePoll(){
		currentState = RUNNING;
	}
	
	public void clearPoll(){
		//reset votes count values to 0
		for(int i=0; i<votesCount.length;i++){
			votesCount[i] = 0;
		}
	}
	
	/**
	 * Adds a vote for a specific option
	 * @param option
	 * @param voterID
	 * @return true if the poll was successfully added to the poll
	 */
	public synchronized boolean addVote(int optionIndex,int voterID){
		//TODO Using voterID, check if voter has already voted or is anonymous
		if((currentState==RUNNING)&&(optionIndex < options.length)){
			votesCount[optionIndex] = votesCount[optionIndex]+1;  
			return true;
		}
		return false;
	}
	
	public int[] getVoteStats(){
		return votesCount;
	}
	
	
	

}
