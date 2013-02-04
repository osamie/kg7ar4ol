package server;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * VoteListener is a UDP server where each worker accepts the 
 * voting messages. (One thread per poll listening to votes).
 *
 */
class VoteListener {
	private DatagramSocket serverSocket = null;
	
	public VoteListener(int port) {
		try {
			serverSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			System.out.println("Cound not create datagram socket on local port:" + port);
			System.exit(1);
		}
	}
	
	/**
	 * Close the open sockets 
	 */
	@Override
	protected void finalize() throws Throwable {
		 serverSocket.close();
	}

}
