package server;

import model.PollsManager;


/*
 * Listener is a TCP server where each worker accepts the admin messages
 */
public class PollServer {
	public static final int ADMIN_PORT = 9005;
	public static final int VOTING_PORT = 6050;
	private PollsManager pollsManager;

	public PollServer() {
		pollsManager = PollsManager.getInstance();
	}
	
	public void startListeners(){
		
		//Listener for ADMIN client connections
		AdminListener adminListener = new AdminListener(ADMIN_PORT,pollsManager);
		
		//Listener for other clients - voters
		VoteListener voteListener = new VoteListener(PollServer.VOTING_PORT,pollsManager); 
		
		/*start listening for new admins and clients*/
		adminListener.start();
		voteListener.start();
		
		try {
			adminListener.join();
			voteListener.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	public void testingStartListeners(){
		//Listener for ADMIN client connections
		AdminListener adminListener = new AdminListener(ADMIN_PORT,pollsManager);
		
		//Listener for other clients - voters
		VoteListener voteListener = new VoteListener(PollServer.VOTING_PORT,pollsManager); 
		
		/*start listening for new admins and clients*/
		adminListener.start();
		voteListener.start();
		
		
	}
	public static void main(String[] args) {
		
		new PollServer().startListeners();
	}
}