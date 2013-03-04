package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import model.PollsManager;

/**
 * VoteListener is a UDP server where each worker accepts the 
 * voting messages. (One thread per poll listening to votes).
 *
 */
class VoteListener extends Thread {
	private DatagramSocket receiveSocket = null;
	private DatagramPacket receivePacket; 
	private PollsManager pollsManager;
	
	public VoteListener(int port,PollsManager manager) {
		pollsManager = manager; 
		try {
			receiveSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			System.out.println("Cound not create datagram socket on local port:" + port);
			System.exit(1);
		}
	}
	
	/**
	 * Listen for requests(votes) from voterClients. Create a worker thread to handle each vote 
	 */
	public void listen(){
		byte[]data;
		System.out.println("Listening for Voters...");
		while (receiveSocket.isBound()){
			data = new byte[100];
			receivePacket = new DatagramPacket(data, data.length);
			try {
				receiveSocket.receive(receivePacket);
				new VotesHandler(receivePacket).start(); //handle the vote
			} catch (IOException e) {
				System.out.println("IOexception receiving packet" + e.getMessage());
				continue;
			}
		}
		
	}
	
	@Override
	public void run() {
		listen();
	}
	
	/**
	 * Close the open sockets 
	 */
	@Override
	protected void finalize() throws Throwable {
		 receiveSocket.close();
	}

}

/**
 * Worker thread: handles the voter's input
 */
class VotesHandler extends Thread{
	
	DatagramPacket receivedPacket;
	public VotesHandler(DatagramPacket packet){
		receivedPacket = packet;
		
	}
	
	/**
	 * Parse and then update the necessary poll  
	 */
	private void processRequest(byte[] req){//String req){
		Long id = Long.valueOf(req[0]);
		Long choice = Long.valueOf(req[1]);
		System.out.println(id + " " + choice);
		
		
		//request = req.replaceFirst("!->", ""); //remove the vote request identifier if any
		//String [] args = request.split(",");
		//if (args.length<2){
		//	System.out.println("Invalid vote request received:" + request);
		//	return;
		//}
		/*
		 * TODO update the corresponding poll. The corresponding poll
		 *    checks for duplicate or repeated voter
		 */
		//System.out.println("voted option:" + args[1] + " for pollID:" + args[0]);
	}
	
	@Override
	public void run() {
		byte[] request;
		//TODO update the poll specified in the packet
		//String request = new String (receivedPacket.getData());
		
		request = receivedPacket.getData();
		processRequest(request); //TODO
	}
	
}