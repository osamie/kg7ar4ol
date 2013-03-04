package model;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Poll extends Observable {
	private int adminID;
	private int pollID;
	private String emailAddress;
	private String[] options;
	private int[] votesCount;
	private int currentState;

	
	//States of the poll
	private static final int STOPPED = 9;
	private static final int RUNNING = 1;
	private static final int PAUSED = 0;
	
	
	
	public Poll(int pollID,int adminID,String emailAdress,String [] list) {
		//initialize class properties
		this.adminID = adminID;
		this.emailAddress = emailAdress; //user email address
		this.options = new String[list.length];
		this.votesCount = new int[list.length]; //initialized with zeros
		
		//copy content of list to this.options
		System.arraycopy(list, 0, options, 0, list.length); 
		
		this.currentState = PAUSED; //default state
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
	public int getPollID(){
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
	 * Stopped state: poll has ended and has stopped accepting votes. 
	 */
	public void stopPoll(){
		currentState = STOPPED;
	}
	
	public void startPoll(){
		currentState = RUNNING;
	}
	
	/**
	 * Adds a vote for a specific option
	 * @param option
	 * @param voterID
	 * @return true if the poll was sucessfully added to the poll
	 */
	public boolean addVote(int optionIndex,int voterID){
		//TODO Using voterID, check if voter has already voted or is anonymous
		if((currentState==RUNNING)&&(optionIndex < options.length)){
			votesCount[optionIndex] = votesCount[optionIndex]+1;  
			return true;
		}
		return false;
	}
	

}
