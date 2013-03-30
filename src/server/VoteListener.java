package server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
	private PrintWriter out;
	
	public VoteListener(int port,PollsManager manager) {
		try {
			out = new PrintWriter(new FileWriter("serverVoteListener.txt", true));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		pollsManager = PollsManager.getInstance(); 
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
				new VotesHandler(receivePacket,pollsManager).start(); //handle the vote
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
	private PollsManager pollsManager;
	DatagramPacket receivedPacket;
	public VotesHandler(DatagramPacket packet,PollsManager manager){
		receivedPacket = packet;
		pollsManager = PollsManager.getInstance();
	}
	
	/**
	 * Parse and then update the necessary poll  
	 */
	private void processRequest(String req){
		//TODO parse req properly 
		String []request = req.split(" ");
		System.out.println("here but" + request[0] + "and" + request[1]);
		if(request.length<2) return;
		
		Long pollID = Long.parseLong(request[0]);
		int choice = Integer.parseInt(request[1].trim());
		System.out.println("Voting option:" + pollID + " for pollID:" + choice);
		pollsManager.addVote(pollID, choice, 0);
//		System.out.println("choice count before:"+ countVotes(voteStats));		
	}
	
	
	/**
	 * Count total number of votes
	 * @param voteStats
	 * @return
	 */
	private int countVotes(int[] voteStats){
		int totalVoteCount=0;
		for(int i=0;i<voteStats.length;i++){
			totalVoteCount+=voteStats[i];
		}
		return totalVoteCount;
	}
	
	@Override
	public void run() {
//		byte[] request;
		//TODO update the poll specified in the packet
		//String request = new String (receivedPacket.getData());

		processRequest(new String(receivedPacket.getData())); //TODO
	}
	
}