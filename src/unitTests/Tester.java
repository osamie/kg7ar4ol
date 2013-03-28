package unitTests;

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



import client.AdminClient;
import client.Client;
import server.*;
import model.*;

public class Tester {

	
	
	protected Shell shlTestingEnvironment;
	private Text text_IPAddress;
	private Text text_PollIDVote;
	private Text text_NumOfVotes;
	private Text text_pollIDInfo;
	public int ADMINCAPACITY = 100;
	public int CLIENTCAPACITY = 1000;
	public int POLLCAPACITY = 10;
	public AdminClient admins[] = new AdminClient[ADMINCAPACITY];
	public Client voters[] = new Client[CLIENTCAPACITY];
	public long polls[][] = new long[ADMINCAPACITY][POLLCAPACITY];
	public int numOfAdmins = 0;
	public int numOfVoters = 0;
	
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
		
		Button btnVote = new Button(grpVoting, SWT.NONE);
		btnVote.setBounds(10, 70, 75, 25);
		btnVote.setText("Vote");
		
		text_NumOfVotes = new Text(grpVoting, SWT.BORDER);
		text_NumOfVotes.setBounds(101, 43, 76, 21);
		
		Button btnRepeatedVotes = new Button(grpVoting, SWT.CHECK);
		btnRepeatedVotes.setBounds(102, 70, 102, 16);
		btnRepeatedVotes.setText("Repeated Votes");
		
		Label lblPollId = new Label(grpVoting, SWT.NONE);
		lblPollId.setBounds(10, 22, 55, 15);
		lblPollId.setText("Poll ID");
		
		Label lblNumberOfVotes = new Label(grpVoting, SWT.NONE);
		lblNumberOfVotes.setBounds(101, 22, 103, 15);
		lblNumberOfVotes.setText("Number of Votes");
		
		Button btnConcurrentVoting = new Button(grpVoting, SWT.CHECK);
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
		
		Label lblOption1 = new Label(grpData, SWT.NONE);
		lblOption1.setBounds(10, 22, 55, 15);
		lblOption1.setText("Option 1:");
		
		Label lblOption3 = new Label(grpData, SWT.NONE);
		lblOption3.setBounds(10, 64, 55, 15);
		lblOption3.setText("Option 3:\r\n");
		
		Label lblOption4 = new Label(grpData, SWT.NONE);
		lblOption4.setBounds(10, 86, 55, 15);
		lblOption4.setText("Option 4:");
		
		Label lblOption2 = new Label(grpData, SWT.NONE);
		lblOption2.setBounds(10, 43, 55, 15);
		lblOption2.setText("Option 2:");
		
		Label lblTotalVotes = new Label(grpData, SWT.NONE);
		lblTotalVotes.setBounds(136, 22, 64, 15);
		lblTotalVotes.setText("Total Votes");
		
		Label lblTotVotes = new Label(grpData, SWT.NONE);
		lblTotVotes.setBounds(136, 43, 55, 15);
		lblTotVotes.setText("New Label");
		
		Button btnGetVoteData = new Button(shlTestingEnvironment, SWT.NONE);
		btnGetVoteData.setBounds(10, 84, 89, 25);
		btnGetVoteData.setText("Get Poll Data");
		
		text_pollIDInfo = new Text(shlTestingEnvironment, SWT.BORDER);
		text_pollIDInfo.setBounds(105, 86, 115, 21);
		final List list_Admins = new List(shlTestingEnvironment, SWT.BORDER | SWT.V_SCROLL);
		list_Admins.addSelectionListener(new SelectionAdapter() {
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
				
				int counter = 0;
				for( int i=0; i<text_IPAddress.getText().length(); i++ ) {
				    if( text_IPAddress.getText().charAt(i) == '.' ) {
				        counter++;
				    } 
				}
				if(counter==3)
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
					
					
				}
			}
		});
		btnCreatePoll.setBounds(424, 53, 75, 25);
		btnCreatePoll.setText("Create Poll");

	}
}
