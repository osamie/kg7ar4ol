package server;


/*
 * Listener is a TCP server where each worker accepts the admin messages
 */
public class PollServer {
	public static final int ADMIN_PORT = 9005;
	public static final int VOTING_PORT = 6050;

	public PollServer() {
	
	}
	
	public void startListeners(){
		
		/*Initialize a Listener for ADMIN client connections */
		AdminListener adminListener = new AdminListener(ADMIN_PORT);
		
		/*Initialize a listener for other clients - voters */
		VoteListener voteListener = new VoteListener(PollServer.VOTING_PORT); 
		
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
	
	public static void main(String[] args) {
		
		new PollServer().startListeners();
	}
}