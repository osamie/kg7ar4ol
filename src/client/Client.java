package client;

import java.io.*;
import java.net.*;

public class Client {
	DatagramPacket sendPacket, receivePacket;
	   DatagramSocket sendReceiveSocket;
	
	   
	 Client(int port)
	 {
		 try {
	         // Bind a datagram socket to any available port on the local host machine. 
	    	 sendReceiveSocket = new DatagramSocket();
	      } catch (SocketException se) {   // Can't create the socket.
	         se.printStackTrace();
	         System.exit(1);
	      }
	 }
	 public void vote(String s,int port)
	 {
		// Send a DatagramPacket to port 5000 on the same host.
	      try {
	          // Java stores characters as 16-bit Unicode values, but 
	          // DatagramPackets store their messages as byte arrays.
	          byte msg[] = s.getBytes();
	    	 sendPacket = new DatagramPacket(msg, msg.length,InetAddress.getLocalHost(), port);
	         
	         sendReceiveSocket.send(sendPacket);
	         System.out.println("Client: Vote sent.");         
	      }
	      catch (UnknownHostException e1)  { e1.printStackTrace(); System.exit(1); }
	      catch (IOException e2) { e2.printStackTrace(); System.exit(1);  }
 
	 }
	   /**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String vote = "";
		
		int portNum = 5000;	
		if (args.length > 0) {
		    try {
		        portNum = Integer.parseInt(args[0]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + " must be an integer");
		        System.exit(1);
		    }
		}
		Client clients = new Client(portNum);
		
		
		//Test options
		vote = "!-> 1234 2";
		clients.vote(vote,portNum);
	}

}
