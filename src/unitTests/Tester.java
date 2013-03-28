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

import server.PollServer;



import client.AdminClient;
import client.Client;
import model.*;

public class Tester {

	
	
	protected Shell shlTestingEnvironment;
	private Text text_IPAddress;
	private Text text_PollIDVote;
	private Text text_NumOfVotes;
	private Text text_pollIDInfo;
	public int ADMINCAPACITY = 100;
	public int CLIENTCAPACITY = 10000;
	public int POLLCAPACITY = 10;
	public AdminClient admins[] = new AdminClient[ADMINCAPACITY];
	public Client voters[] = new Client[CLIENTCAPACITY];
	public voterClient concurrVoters[] = new voterClient[CLIENTCAPACITY];
	public long polls[][] = new long[ADMINCAPACITY][POLLCAPACITY];
	public int numOfAdmins = 0;
	public int numOfVoters = 0;
	static PollsManager manager = PollsManager.getInstance();
	static LocalPollsManager localManager = LocalPollsManager.getInstance();
	
	public static Object lock = new Object();
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Tester window = new Tester();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		shlTestingEnvironment.setSize(525, 400);
		shlTestingEnvironment.setText("Testing Environment");
		
		text_IPAddress = new Text(shlTestingEnvironment, SWT.BORDER);
		text_IPAddress.setText("127.0.0.1");
		text_IPAddress.setBounds(182, 31, 120, 24);
		
		Label lblIpAddress = new Label(shlTestingEnvironment, SWT.NONE);
		lblIpAddress.setAlignment(SWT.CENTER);
		lblIpAddress.setBounds(182, 10, 120, 15);
		lblIpAddress.setText("IP Address Of Server");
		
		Group grpVoting = new Group(shlTestingEnvironment, SWT.NONE);
		grpVoting.setText("Voting");
		grpVoting.setBounds(10, 230, 245, 122);
		
		text_PollIDVote = new Text(grpVoting, SWT.BORDER);
		text_PollIDVote.setBounds(10, 43, 76, 21);
		
		
		
		text_NumOfVotes = new Text(grpVoting, SWT.BORDER);
		text_NumOfVotes.setBounds(101, 43, 76, 21);
		
		final Button btnRepeatedVotes = new Button(grpVoting, SWT.CHECK);
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
		
		Group grpData = new Group(shlTestingEnvironment, SWT.NONE);
		grpData.setText("Data");
		grpData.setBounds(10, 115, 245, 109);
		
		final Label lblOption1 = new Label(grpData, SWT.NONE);
		lblOption1.setBounds(10, 22, 120, 15);
		lblOption1.setText("Option 1:");
		
		final Label lblOption3 = new Label(grpData, SWT.NONE);
		lblOption3.setBounds(10, 64, 120, 15);
		lblOption3.setText("Option 3:\r\n");
		
		final Label lblOption4 = new Label(grpData, SWT.NONE);
		lblOption4.setBounds(10, 86, 120, 15);
		lblOption4.setText("Option 4:");
		
		final Label lblOption2 = new Label(grpData, SWT.NONE);
		lblOption2.setBounds(10, 43, 120, 15);
		lblOption2.setText("Option 2:");
		
		Label lblTotalVotes = new Label(grpData, SWT.NONE);
		lblTotalVotes.setBounds(136, 22, 64, 15);
		lblTotalVotes.setText("Total Votes");
		
		final Label lblTotVotes = new Label(grpData, SWT.NONE);
		lblTotVotes.setBounds(136, 43, 55, 15);
		lblTotVotes.setText("New Label");
		
		Button btnGetVoteData = new Button(shlTestingEnvironment, SWT.NONE);
		btnGetVoteData.addSelectionListener(new SelectionAdapter() {
			/*
			 * Get Data Button
			 */
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int votes[] = new int[4];
				int totalVotes;
				boolean valid = true;
				try{
					Long.parseLong(text_pollIDInfo.getText());
					
				}catch(NumberFormatException e)
				{
					valid = false;
				}
				if(valid=true)
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
				if(valid == true)
				{
					votes[0] = localManager.getVoteCount(Long.parseLong(text_pollIDInfo.getText()), 1);
					System.out.println(votes[0]);
					lblOption1.setText("Option 1: " + votes[0]);
					
					votes[1] = localManager.getVoteCount(Long.parseLong(text_pollIDInfo.getText()), 2);
					System.out.println(votes[0]);
					lblOption2.setText("Option 2: " + votes[1]);
					
					votes[2] = localManager.getVoteCount(Long.parseLong(text_pollIDInfo.getText()), 3);
					System.out.println(votes[0]);
					lblOption3.setText("Option 3: " + votes[2]);
					
					votes[3] = localManager.getVoteCount(Long.parseLong(text_pollIDInfo.getText()), 4);
					System.out.println(votes[0]);
					lblOption4.setText("Option 4: " + votes[3]);
					
					totalVotes = votes[0]+votes[1]+votes[2]+votes[3];
					lblTotVotes.setText(""+totalVotes);
				}
				else
				{
					text_pollIDInfo.setText("Invalid Poll ID");
					
				}
			}
		});
		btnGetVoteData.setBounds(10, 84, 89, 25);
		btnGetVoteData.setText("Get Poll Data");
		
		text_pollIDInfo = new Text(shlTestingEnvironment, SWT.BORDER);
		text_pollIDInfo.setBounds(105, 86, 115, 21);
		final List list_Admins = new List(shlTestingEnvironment, SWT.BORDER | SWT.V_SCROLL);
		list_Admins.addSelectionListener(new SelectionAdapter() {
			/*
			 * Show Polls
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
			 *Creating Admin Button
			 */
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				boolean valid = true;
				int counter = 0;
				for( int i=0; i<text_IPAddress.getText().length(); i++ ) {
					
				    if(text_IPAddress.getText().charAt(i) == '.' ) {
				        counter++;
				    } 
				    else	
				    {
				    	try{
							Integer.parseInt(text_IPAddress.getText().substring(i,i+1));
							
						}catch(NumberFormatException e){
							valid = false;
						}
				    }
				}
				
						
				if(counter==3&& valid == true)
				{
					admins[numOfAdmins] = new AdminClient(server.PollServer.ADMIN_PORT, text_IPAddress.getText());
					numOfAdmins++;
					list_Admins.add("Admin " + numOfAdmins);
					
				}
				else
				{
					text_IPAddress.setText("Invalid IP Address");
				}
			}
		});
		btnCreateAdmin.setBounds(343, 84, 156, 25);
		btnCreateAdmin.setText("Create Admin");
		
		
		Button btnCreatePoll = new Button(shlTestingEnvironment, SWT.NONE);
		btnCreatePoll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				long pollID;
				if(list_Admins.getSelectionIndex() != -1)
				{
					pollID = admins[list_Admins.getSelectionIndex()].createPoll("Test@test.com|Poll1|4|1|2|3|4");
					for(int i = 0; i < POLLCAPACITY;i++)
					{
						if(polls[list_Admins.getSelectionIndex()][i] == 0)
						{
							polls[list_Admins.getSelectionIndex()][i] = pollID;
							i = POLLCAPACITY;
						}
					}
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
		btnCreatePoll.setBounds(424, 53, 75, 25);
		btnCreatePoll.setText("Create Poll");
		Button btnVote = new Button(grpVoting, SWT.NONE);
		btnVote.addSelectionListener(new SelectionAdapter() {
			/*
			 * Vote Button 
			 */
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				boolean valid = true;
				int counter = 0;
				long pollID;
				Random generator = new Random();
				
				pollID = Long.parseLong(text_PollIDVote.getText());
				for( int i=0; i<text_IPAddress.getText().length(); i++ ) {
				    if(text_IPAddress.getText().charAt(i) == '.' ) {
				        counter++;
				    } 
				    else{
				    	try{
							Integer.parseInt(text_IPAddress.getText().substring(i,i+1));	
						}catch(NumberFormatException e){
							valid = false;
						}
				    }
				}		
				if(counter==3&& valid == true)
				{	
					if(btnConcurrentVoting.getSelection()== true)
					{
						for(int i = 0;i<Integer.parseInt(text_NumOfVotes.getText());i++)
						{
							concurrVoters[i] = new voterClient(btnConcurrentVoting.getSelection(),btnRepeatedVotes.getSelection(),pollID );
							concurrVoters[i].start();
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						synchronized(lock)
						{
							lock.notifyAll();
						}
					}
					else
					{
						int voteChoice;
						for(int i = 0; i<Integer.parseInt(text_NumOfVotes.getText());i++)
						{
							voteChoice = generator.nextInt(4)+1;
							voters[i] = new Client(PollServer.VOTING_PORT);
							voters[i].vote(pollID, voteChoice);
						}
						voteChoice = generator.nextInt(4)+1;
						
						if(btnRepeatedVotes.getSelection() == true)
						{
							for(int i = 0; i<Integer.parseInt(text_NumOfVotes.getText());i++)
							{
								voteChoice = generator.nextInt(3)+1;
								voters[i].vote(pollID, voteChoice);
							}
						}
					}
					
				}
			}
		});
		btnVote.setBounds(10, 70, 75, 25);
		btnVote.setText("Vote");
	}
}
class voterClient extends Thread
{
	boolean concurr, repeat;
	long PollID;
	Random generator = new Random();
	Client vote;
	voterClient( boolean concurrent, boolean repeated, long pollID)
	{
		PollID = pollID;
		concurr = concurrent;
		repeat = repeated;
		vote = new Client(PollServer.VOTING_PORT);
	}
	public void run()
	{
		int voteChoice = generator.nextInt(4) + 1;

		try {
			synchronized(Tester.lock)
			{
				Tester.lock.wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vote.vote(PollID, voteChoice);

		if(repeat == true)
		{
			voteChoice = generator.nextInt(4) + 1;
			vote.vote(PollID, voteChoice);
		}

	}
}
