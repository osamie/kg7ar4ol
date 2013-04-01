package unitTests;

import java.util.Random;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

import server.PollServer;



import client.AdminClient;
import client.Client;
import model.*;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.DateTime;

public class Tester {

	
	
	protected Shell shlTestingEnvironment;
	private Text text_IPAddress;
	private Text text_PollIDVote;
	private Text text_NumOfVotes;
	private Text text_pollIDInfo;
	private Label lblOption1;
	private Label lblOption2;
	private Label lblOption3;
	private Label lblOption4;
	private Label lblTotVotes;
	public int ADMINCAPACITY = 100;
	public int CLIENTCAPACITY = 10000;
	public int POLLCAPACITY = 100;
	public AdminClient admins[] = new AdminClient[ADMINCAPACITY];
	public Client voters[] = new Client[CLIENTCAPACITY];
	public voterClient concurrVoters[] = new voterClient[CLIENTCAPACITY];
	public long polls[][] = new long[ADMINCAPACITY][POLLCAPACITY];
	public int numOfAdmins = 0;
	public int numOfVoters = 0;
	static PollsManager manager = PollsManager.getInstance();
	static LocalPollsManager localManager = LocalPollsManager.getInstance();
	static PrintWriter out;

	
	public static Object lock = new Object();
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			out = new PrintWriter(new FileWriter("testing.txt", true));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		try {
			Tester window = new Tester();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.close();
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlTestingEnvironment.open();
		shlTestingEnvironment.layout();
		while (!shlTestingEnvironment.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		for(int i = 0; i<numOfAdmins;i++)
		{
			admins[i].disconnect();
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlTestingEnvironment = new Shell();
		shlTestingEnvironment.setImage(SWTResourceManager.getImage(Tester.class, "/unitTests/RO-Mx2-64_meter.png"));
		shlTestingEnvironment.setSize(525, 400);
		shlTestingEnvironment.setText("Testing Environment");
		
		text_IPAddress = new Text(shlTestingEnvironment, SWT.BORDER);
		text_IPAddress.setText("127.0.0.1");
		text_IPAddress.setBounds(159, 31, 120, 24);
		
		Label lblIpAddress = new Label(shlTestingEnvironment, SWT.NONE);
		lblIpAddress.setAlignment(SWT.CENTER);
		lblIpAddress.setBounds(159, 10, 120, 15);
		lblIpAddress.setText("IP Address Of Server");
		
		Group grpVoting = new Group(shlTestingEnvironment, SWT.NONE);
		grpVoting.setText("Voting");
		grpVoting.setBounds(10, 230, 245, 122);
		
		text_PollIDVote = new Text(grpVoting, SWT.BORDER);
		text_PollIDVote.setBounds(10, 43, 76, 21);
		
		final Label lblResponse = new Label(shlTestingEnvironment, SWT.NONE);
		lblResponse.setBounds(285, 34, 214, 15);
		
				
		text_NumOfVotes = new Text(grpVoting, SWT.BORDER);
		text_NumOfVotes.setBounds(101, 43, 76, 21);
		
		final Button btnRepeatedVotes = new Button(grpVoting, SWT.CHECK);
		btnRepeatedVotes.setEnabled(false);
		btnRepeatedVotes.setBounds(102, 70, 102, 16);
		btnRepeatedVotes.setText("Repeated Votes");
		
		Label lblPollId = new Label(grpVoting, SWT.NONE);
		lblPollId.setBounds(10, 22, 55, 15);
		lblPollId.setText("Poll ID");
		
		Label lblNumberOfVotes = new Label(grpVoting, SWT.NONE);
		lblNumberOfVotes.setBounds(101, 22, 103, 15);
		lblNumberOfVotes.setText("Number of Votes");
		
		final Button btnConcurrentVoting = new Button(grpVoting, SWT.CHECK);
		btnConcurrentVoting.setBounds(101, 96, 120, 16);
		btnConcurrentVoting.setText("Concurrent Voting");

		Group grpData = new Group(shlTestingEnvironment, SWT.NONE);
		grpData.setBounds(10, 120, 237, 104);
		grpData.setText("Data");
		
		lblOption1 = new Label(grpData, SWT.NONE);
		lblOption1.setBounds(10, 22, 120, 15);
		lblOption1.setText("Option 1:");
		
		lblOption3 = new Label(grpData, SWT.NONE);
		lblOption3.setBounds(10, 64, 120, 15);
		lblOption3.setText("Option 3:");
		
		lblOption4 = new Label(grpData, SWT.NONE);
		lblOption4.setBounds(10, 86, 120, 15);
		lblOption4.setText("Option 4:");
		
		lblOption2 = new Label(grpData, SWT.NONE);
		lblOption2.setBounds(10, 43, 120, 15);
		lblOption2.setText("Option 2:");
		
		Label lblTotalVotes = new Label(grpData, SWT.NONE);
		lblTotalVotes.setBounds(136, 22, 64, 15);
		lblTotalVotes.setText("Total Votes");
		
		lblTotVotes = new Label(grpData, SWT.NONE);
		lblTotVotes.setBounds(136, 43, 55, 15);
		
		Label lblDropRate = new Label(grpData, SWT.NONE);
		lblDropRate.setBounds(136, 64, 55, 15);
		lblDropRate.setText("Drop Rate");
		
		final Label lblDrop = new Label(grpData, SWT.NONE);
		lblDrop.setBounds(136, 86, 55, 15);
		
		final List list_AdminPolls = new List(shlTestingEnvironment, SWT.BORDER | SWT.V_SCROLL);
		list_AdminPolls.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				text_PollIDVote.setText(list_AdminPolls.getItem(list_AdminPolls.getFocusIndex()));
				text_pollIDInfo.setText(list_AdminPolls.getItem(list_AdminPolls.getFocusIndex()));
				
			}
		});
		list_AdminPolls.setBounds(343, 143, 156, 209);
		
		Label lblAdmins = new Label(shlTestingEnvironment, SWT.NONE);
		lblAdmins.setBounds(261, 123, 55, 15);
		lblAdmins.setText("Admins");
		
		Label lblPolls = new Label(shlTestingEnvironment, SWT.NONE);
		lblPolls.setBounds(343, 122, 55, 15);
		lblPolls.setText("Polls");
		
		Button btnGetVoteData = new Button(shlTestingEnvironment, SWT.NONE);
		btnGetVoteData.addSelectionListener(new SelectionAdapter() {
			/*
			 * Get Data Button 
			 * Gets the data from the poll indicated in the textbox.
			 * Displays the number of votes for each option and the total votes on that poll
			 */
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				getData();
			}
		});
		btnGetVoteData.setBounds(10, 84, 89, 25);
		btnGetVoteData.setText("Get Poll Data");
		
		text_pollIDInfo = new Text(shlTestingEnvironment, SWT.BORDER);
		text_pollIDInfo.setBounds(105, 86, 115, 21);
		final List list_Admins = new List(shlTestingEnvironment, SWT.BORDER | SWT.V_SCROLL);
		list_Admins.addSelectionListener(new SelectionAdapter() {
			/*
			 * Show Polls. Displays the polls for that admin once an admin has been 
			 * selected in the admins list.
			 */
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				list_AdminPolls.removeAll();
				if(list_Admins.getSelectionIndex() != -1)
				{
					list_AdminPolls.removeAll();
					for(int i = 0; i<POLLCAPACITY;i++)
					{
						if(polls[list_Admins.getSelectionIndex()][i] == 0)
						{
							i = POLLCAPACITY;
						}
						else
						{
							list_AdminPolls.add(String.valueOf(polls[list_Admins.getSelectionIndex()][i]));
						}
					}	
				}
			}
		});
		list_Admins.setBounds(261, 143, 76, 209);
		
		Button btnCreateAdmin = new Button(shlTestingEnvironment, SWT.NONE);
		btnCreateAdmin.addSelectionListener(new SelectionAdapter() {
			
			/*
			 *Creating Admin Button.
			 *Creates an admin and connects it to the server.
			 */
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				boolean valid;
				long startTime, time;
				valid = validateIP(1);	//Validates IP Address
				
				//Creates Admin.
				if(valid == true)
				{
					startTime = System.nanoTime();
					admins[numOfAdmins] = new AdminClient(server.PollServer.ADMIN_PORT, text_IPAddress.getText());
					time = System.nanoTime() - startTime;
					numOfAdmins++;
					list_Admins.add("Admin " + numOfAdmins);
					lblResponse.setText("Time to create an admin: " + time+ "ns");
				}
				
			}
		});
		btnCreateAdmin.setBounds(343, 84, 156, 25);
		btnCreateAdmin.setText("Create Admin");
		
		
		Button btnCreatePoll = new Button(shlTestingEnvironment, SWT.NONE);
		btnCreatePoll.addSelectionListener(new SelectionAdapter() {
			/*
			 * Create a Poll Button
			 * Creates a poll for the delected admin.
			 */
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				long pollID;
				long startTime, time;
				//Validate admin selection
				if(list_Admins.getSelectionIndex() != -1)
				{
					startTime = System.nanoTime();
					pollID = admins[list_Admins.getSelectionIndex()].createPoll("Test@test.com|Poll1|4|1|2|3|4");//Create Poll
					time = System.nanoTime() - startTime;
					lblResponse.setText("Time to create an poll: " + time + "ns");
					//Add poll to the list of polls for that admin.
					for(int i = 0; i < POLLCAPACITY;i++)
					{
						if(polls[list_Admins.getSelectionIndex()][i] == 0)
						{
							polls[list_Admins.getSelectionIndex()][i] = pollID;
							i = POLLCAPACITY;
						}
					}
					list_AdminPolls.removeAll();	//Remove current display of polls.
					
					//Re-populate admin polls list display
					for(int i = 0; i<POLLCAPACITY;i++)
					{
						if(polls[list_Admins.getSelectionIndex()][i] == 0)
						{
							i = POLLCAPACITY;
						}
						else
						{
							list_AdminPolls.add(String.valueOf(polls[list_Admins.getSelectionIndex()][i]));
						}
					}
					
				}
			}
		});
		btnCreatePoll.setBounds(424, 53, 75, 25);
		btnCreatePoll.setText("Create Poll");
		Button btnVote = new Button(grpVoting, SWT.NONE);
		btnVote.addSelectionListener(new SelectionAdapter() {
			/*
			 * Vote Button 
			 */
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				boolean valid;
				long pollID;
				int votes[] = new int[4];
				int previousVotes, totalVotes;
				int numVotes;
				Random generator = new Random();
				
				pollID = Long.parseLong(text_PollIDVote.getText());
				valid = validateIP(0);	//Validate IP address
				
				if(valid == true)
				{	
					votes[0] = localManager.getVoteCount(Long.parseLong(text_pollIDInfo.getText()), 1);
					votes[1] = localManager.getVoteCount(Long.parseLong(text_pollIDInfo.getText()), 2);
					votes[2] = localManager.getVoteCount(Long.parseLong(text_pollIDInfo.getText()), 3);
					votes[3] = localManager.getVoteCount(Long.parseLong(text_pollIDInfo.getText()), 4);
					previousVotes = votes[0] + votes[1] + votes[2] + votes[3];
					numVotes = Integer.parseInt(text_NumOfVotes.getText());
					out.println("-------------- Voting " + text_NumOfVotes.getText() +" times on poll " +
							text_PollIDVote.getText() + " with concurrent votes = " + btnConcurrentVoting.getSelection() +
								" and repeated votes = " + btnRepeatedVotes.getSelection() + " ---------------");
					
					//Concurrent voting
					if(btnConcurrentVoting.getSelection()== true)
					{
						//Creates numberOfVotes threads which will be clients that will vote concurrently.
						for(int i = 0;i<numVotes;i++)
						{
							concurrVoters[i] = new voterClient(btnConcurrentVoting.getSelection(),btnRepeatedVotes.getSelection(),pollID,text_IPAddress.getText());
							concurrVoters[i].start();
						}
						//Wait a bit for all voters to become ready for voting.
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						synchronized(lock)
						{
							lock.notifyAll();	//Release all the voters to vote concurrently.
						}
					}
					//Non-concurrent votes
					else
					{
						out.println(System.nanoTime() + " ----- Non-concurrent votes -----");
						int voteChoice;
						for(int i = 0; i<numVotes;i++)
						{
							voteChoice = generator.nextInt(4)+1;//Generate random choice number
							
							voters[i] = new Client(PollServer.VOTING_PORT,text_IPAddress.getText());	//Create Client
							out.println(System.nanoTime() + " - voter " + i + " voting for choice " + voteChoice + 
									" on poll " + pollID);
							voters[i].vote(pollID, voteChoice);	//Vote
						}
						

						if(btnRepeatedVotes.getSelection() == true)
						{
							out.println(System.nanoTime() + " ----- Non-concurrent repeated votes -----");
							for(int i = 0; i<numVotes;i++)
							{
								voteChoice = generator.nextInt(4)+1;
								out.println(System.nanoTime() + " - voter " + i + " repeated vote for choice " + voteChoice + 
										" on poll " + pollID);
								voters[i].vote(pollID, voteChoice);
							}
						}
						for(int i = 0; i<numVotes;i++)
						{
							voters[i].close();
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					totalVotes = getData();
					lblDrop.setText( "" + (numVotes - (totalVotes-previousVotes)));
				}
			}
		});
		btnVote.setBounds(10, 70, 75, 25);
		btnVote.setText("Vote");	
		
	}
	/**
	 * Checks to see if the IP address is valid and it will ping the address to see if 
	 * you can connect to that address.
	 * parameters: int voteOrAdmin - 0 = voting, 1 = admin
	 */
	public boolean validateIP(int voteOrAdmin)
	{
		boolean valid = true;
		int counter=0;
		for( int i=0; i<text_IPAddress.getText().length(); i++ ) {
		    if(text_IPAddress.getText().charAt(i) == '.' ) {
		        counter++;
		    } 
		    else{
		    	try{
					Integer.parseInt(text_IPAddress.getText().substring(i,i+1));	
				}catch(NumberFormatException e){
					valid = false;
					text_IPAddress.setText("Invalid IP Address");
				}
		    }
		}
		if(counter!=3)
		{
			valid = false;
			text_IPAddress.setText("Invalid IP Address");
		}
		if(valid== true)
		{
			try {
				InetAddress address = InetAddress.getByName(text_IPAddress.getText());
				if(voteOrAdmin == 0)
				{
					valid = address.isReachable(PollServer.VOTING_PORT);	//Ping voting port and IP Address
				}
				else if(voteOrAdmin == 1)
				{
					valid = address.isReachable(PollServer.ADMIN_PORT);	//Ping admin port and IP address

				}
				if(valid == false)
				{
					text_IPAddress.setText("Cannot connect to server.");

				}
			} catch (UnknownHostException e) {
				text_IPAddress.setText("Incorrect Address.");
				valid = false;
				e.printStackTrace();
			} catch (IOException e) {
				text_IPAddress.setText("Incorrect Address.");
				e.printStackTrace();
				valid = false;
			}
		}
		return valid;
	}
	public int getData(){
		int votes[] = new int[4];
		int totalVotes = 0;
		boolean valid = true;
		
		// Validation of the pollID
		try{
			Long.parseLong(text_pollIDInfo.getText());
			
		}catch(NumberFormatException e)
		{
			valid = false;
		}
		if(valid==true)
		{
			valid = false;
			for(int i = 0; i<ADMINCAPACITY;i++)
			{
				for(int b= 0; b<POLLCAPACITY;b++)
				{
					if(polls[i][b] == Long.parseLong(text_pollIDInfo.getText()))
					{
						valid = true;
					}
				}
			}
		}
		//End of validation of the pollID
		
		//Gets the vote count for each option and displays the information accordingly.
		if(valid == true)
		{
			out.println(" ---------Getting information for poll: " + text_pollIDInfo.getText() + "-------------");
			votes[0] = localManager.getVoteCount(Long.parseLong(text_pollIDInfo.getText()), 1);
			lblOption1.setText("Option 1: " + votes[0]);
			
			votes[1] = localManager.getVoteCount(Long.parseLong(text_pollIDInfo.getText()), 2);
			lblOption2.setText("Option 2: " + votes[1]);
			
			votes[2] = localManager.getVoteCount(Long.parseLong(text_pollIDInfo.getText()), 3);
			lblOption3.setText("Option 3: " + votes[2]);
			
			votes[3] = localManager.getVoteCount(Long.parseLong(text_pollIDInfo.getText()), 4);
			lblOption4.setText("Option 4: " + votes[3]);
			
			totalVotes = votes[0]+votes[1]+votes[2]+votes[3];
			lblTotVotes.setText(""+totalVotes);
			out.println(System.nanoTime() + " - Option 1: "+ votes[0] + " Option 2: " + votes[1] +
					" Option 3: "+ votes[2] + " Option 4: " + votes[3] );
			out.println(System.nanoTime() + " - Total Votes: "+ totalVotes);
			out.println("------------END INFO-------------");

		}
		else
		{
			text_pollIDInfo.setText("Invalid Poll ID");
			
		}
	return totalVotes;
	}
}
/*
 * Client threads for concurrent voting testing. Voter clients will wait on a lock object.
 * The testing environment will then notify all the concurrent threads and they will then vote.
 */
class voterClient extends Thread
{
	boolean concurr, repeat;
	long PollID;
	Random generator = new Random();
	Client vote;
	
	voterClient( boolean concurrent, boolean repeated, long pollID, String IP)
	{
		PollID = pollID;
		concurr = concurrent;
		repeat = repeated;
		vote = new Client(PollServer.VOTING_PORT,IP);
	}
	public void run()
	{
		int voteChoice = generator.nextInt(4) + 1;//Randomly generate a vote choice

		try {
			synchronized(Tester.lock)
			{
				Tester.lock.wait();//Wait on lock.
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		vote.vote(PollID, voteChoice);	//vote
		//Repeated concurrent votes
		if(repeat == true)
		{
			voteChoice = generator.nextInt(4) + 1;
			vote.vote(PollID, voteChoice);
		}

	}
}
