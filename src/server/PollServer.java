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
		Thread admin = new ListenerThread();
		Thread t = new VoteListenerThread();
		admin.start();
		t.start();
		
		try {
			admin.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		new PollServer().startListeners();
	}
}

/*Initialize a Listener for ADMIN client connections */
class ListenerThread extends Thread{
	@Override
	public void run() {
		new AdminListener(PollServer.ADMIN_PORT).listen();
	}
}

/*Initialize a listener for other clients - voters */
class VoteListenerThread extends Thread{
	@Override
	public void run() {
		new VoteListener(PollServer.VOTING_PORT).listen();
	}
}
