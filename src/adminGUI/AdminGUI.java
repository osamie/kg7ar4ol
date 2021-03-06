/*
 * Admin GUI
 * GUI for the admin client.
 * GUI creates an instance of AdminClient and the GUI is attached to the AdminClient.
 * Extracts information from the GUI and sends requests to the server.
 * 
 */
package adminGUI;

import model.LocalPollsManager;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import server.PollServer;
import client.AdminClient;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;


public class AdminGUI extends ApplicationWindow {
	
	private String[][] pollInfo;
	private int pollInfoLength;
	private Text text_email;
	private Text text_question;
	private static AdminClient admin;
	private int maxOptions = 10;
	/**
	 * Create the application window.
	 */
	public AdminGUI(String IPAddress) {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		pollInfoLength = 25;
		pollInfo = new String[pollInfoLength][3];
		
		//Creates the instance of an AdminClient with the port sent in either from command line argument or the default port.
		admin = new AdminClient(PollServer.ADMIN_PORT, IPAddress);
		try {
			setBlockOnOpen(true);
			open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		admin.disconnect();	//Disconnect once GUI is closed.	
	}

	private String getQuestion(String pollID)
	{
		for(int i = 0; i <pollInfoLength; i++ )
		{
			if(pollInfo[i][0] != null)
			{
				if(pollInfo[i][0].equals(pollID) == true)
				{
					return pollInfo[i][1];
				}
			}
		}
		return null;
	}
	private String getOptions(String pollID)
	{
		for(int i = 0; i <pollInfoLength; i++ )
		{
			if(pollInfo[i][0]!= null)
			{
				if(pollInfo[i][0].equals(pollID) == true)
				{
					return pollInfo[i][2];
				}
			}
		}
		return null;
	}
	private void addPollInfo(String pollID, String question, String options)
	{
		for(int i = 0; i <pollInfoLength; i++)
		{
			if(pollInfo[i][0] == null)
			{
				pollInfo[i][0] = pollID;
				pollInfo[i][1] = question;
				pollInfo[i][2] = options;
				i = pollInfo.length;
			}
		}
		
		
	}
	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(final Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		final List list_Polls = new List(container, SWT.BORDER | SWT.V_SCROLL);
		list_Polls.setItems(new String[] {});
		list_Polls.setBounds(424, 37, 194, 153);
		
		final List list_Paused = new List(container, SWT.BORDER | SWT.V_SCROLL);
		list_Paused.setBounds(49, 245, 209, 153);
		
		final List list_Active = new List(container, SWT.BORDER | SWT.V_SCROLL);
		list_Active.setBounds(409, 245, 209, 153);
		String items[] = new String[maxOptions];
		for(int i = 0; i < maxOptions; i++)
		{
			items[i] = String.valueOf(i+1);
		}
		
		Label lblCreatedPolls = new Label(container, SWT.NONE);
		lblCreatedPolls.setBounds(471, 10, 94, 15);
		lblCreatedPolls.setText("Created Polls");
		
		
		Button btnPausePoll = new Button(container, SWT.NONE);
		btnPausePoll.addSelectionListener(new SelectionAdapter() {
			/*
			 * Pause Poll
			 * Pauses the poll by sending the request to the server.
			 * Moves the pollId from the active polls list to the paused polls list.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(list_Active.getSelectionIndex()!=-1 && list_Active.getItemCount()!=0)
				{
					int focusedIndex = list_Active.getFocusIndex();	//Gets the pollID from the list.
					String temp = list_Active.getItem(focusedIndex);
					temp = temp.substring(0, temp.indexOf(" "));
					//System.out.println(temp); //Debug Print.
					admin.pausePoll(temp);	//Sends request to the server.
					//Adds the poll to the paused list and removes it from the active list.
					list_Paused.add(list_Active.getItem(focusedIndex));
					list_Active.remove(focusedIndex);
					
					//Logic for moving the focused item in the list to the next appropriate list item.
					if(focusedIndex == 0)
					{
						if(list_Active.getItemCount() == 0)
						{
							list_Active.setSelection(-1);
						}
						else
						{
							list_Active.setSelection(focusedIndex);
						}
					}
					else if(focusedIndex == list_Active.getItemCount())
					{
						list_Active.setSelection(focusedIndex-1);
					}
					else
					{
						list_Active.setSelection(focusedIndex);
					}			
				}
			}
		});
		btnPausePoll.setBounds(279, 335, 116, 25);
		btnPausePoll.setText("<< Pause Poll");
		
		Button btnResumePoll = new Button(container, SWT.NONE);
		btnResumePoll.addSelectionListener(new SelectionAdapter() {
			/*
			 * Resume Poll Button
			 *  Resumes the poll by sending the request to the server.
			 * Moves the pollId from the paused polls list to the active polls list.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(list_Paused.getSelectionIndex()!=-1 && list_Paused.getItemCount()!=0)
				{
					int focusedIndex = list_Paused.getFocusIndex();
					String temp = list_Paused.getItem(focusedIndex);
					temp = temp.substring(0, temp.indexOf(" "));
					//System.out.println(temp); 				//Debug Print
					admin.resumePoll(temp);						//Sends request to the server.
					//Moves the poll id from the paused list to the active list.
					list_Active.add(list_Paused.getItem(focusedIndex));
					list_Paused.remove(focusedIndex);
					
					//Logic for moving the focused item in the list to the next appropriate list item.
					if(focusedIndex == 0)
					{
						if(list_Paused.getItemCount() == 0)
						{
							list_Paused.setSelection(-1);
						}
						else
						{
							list_Paused.setSelection(focusedIndex);
						}
					}
					else if(focusedIndex == list_Paused.getItemCount())
					{
						list_Paused.setSelection(focusedIndex-1);
					}
					else
					{
						list_Paused.setSelection(focusedIndex);
					}
					
				}
			}
		});
		btnResumePoll.setBounds(264, 289, 139, 28);
		btnResumePoll.setText("Resume Poll >>");
		
		Button btnClearPoll = new Button(container, SWT.NONE);
		btnClearPoll.addSelectionListener(new SelectionAdapter() {
			/*
			 * Clear Poll Button
			 * Clears the polls information by sending the request to the server.
			 * 
			 *	TODO Possible
			 *		clear the active poll id information being displayed if observer doesnt, but the observer pattern should.
			 * 
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(list_Polls.getFocusIndex()!=-1)
				{
					String temp = list_Polls.getItem(list_Polls.getFocusIndex());
					temp = temp.substring(0, temp.indexOf(" "));
					System.out.println(temp);
					admin.clearPoll(temp);
				}
			}
		});
		btnClearPoll.setBounds(312, 137, 88, 25);
		btnClearPoll.setText("Clear Poll");
		
		Button btnStopPoll = new Button(container, SWT.NONE);
		btnStopPoll.addSelectionListener(new SelectionAdapter() {
			/*
			 * Stop Button
			 * Stops the poll by sending the request to the server.
			 * Deletes the list item from the created polls list and either the active or paused list depending on the state.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(list_Polls.getSelectionIndex()!=-1 && list_Polls.getItemCount()!=0)
				{
					int index = list_Polls.getSelectionIndex();
					String temp = list_Polls.getItem(index);
					String pollId = temp.substring(0,temp.indexOf(" "));	//get poll id from list.
					
					admin.stopPoll(pollId);	//Send request to the server.
					list_Polls.remove(index); //remove item from list of polls list.
					
					//Logic for moving the focused item in the list to the next appropriate list item.
					if(list_Active.indexOf(temp) != -1)	
					{
						list_Active.remove(temp);
					}
					else if(list_Paused.indexOf(temp) != -1)
					{
						list_Paused.remove(temp);
					}
					
					if(index == 0)
					{
						if(list_Polls.getItemCount() == 0)
						{
							list_Polls.setSelection(-1);
						}
						else
						{
							list_Polls.setSelection(index);
						}
					}
					else if(index == list_Polls.getItemCount())
					{
						list_Polls.setSelection(index-1);
					}
					else
					{
						list_Polls.setSelection(index);
					}
					
				}
			}
		});
		btnStopPoll.setBounds(312, 104, 88, 25);
		btnStopPoll.setText("Stop Poll");
		
		
		
		Button btnDisplayPoll = new Button(container, SWT.NONE);
		btnDisplayPoll.addSelectionListener(new SelectionAdapter() {
			/*
			 * Display Poll Button
			 * Creates a pollGUI which displays the information about the poll.
			 * 
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(list_Polls.getSelectionIndex()!=-1 && list_Polls.getItemCount()!=0)
				{
					int index = list_Polls.getSelectionIndex();
					String temp = list_Polls.getItem(index);	//Gets poll ID
					String pollID = temp.substring(0,temp.indexOf(" "));
										
					PollGUI poll = new PollGUI(Long.parseLong(temp.substring(temp.lastIndexOf(" ") + 1)),Long.parseLong(pollID),getQuestion(pollID),getOptions(pollID),LocalPollsManager.getInstance()); //Creates the Poll GUI
					poll.setBlockOnOpen(true);
					poll.open();
					
				}
				
			}
		});
		btnDisplayPoll.setBounds(302, 73, 116, 25);
		btnDisplayPoll.setText("Display Poll");
		
		Label lblPausedPolls = new Label(container, SWT.NONE);
		lblPausedPolls.setBounds(109, 224, 94, 15);
		lblPausedPolls.setText("Paused Polls");
		
		Label lblActivePolls = new Label(container, SWT.NONE);
		lblActivePolls.setBounds(471, 224, 100, 15);
		lblActivePolls.setText("Active Polls");
		
		Group grpCreatePoll = new Group(container, SWT.NONE);
		grpCreatePoll.setBounds(10, 10, 293, 188);
		
		text_email = new Text(grpCreatePoll, SWT.BORDER);
		text_email.setBounds(10, 34, 181, 21);
		text_email.setText("sample@domain.com");
		
		final Label lblEmailAddress = new Label(grpCreatePoll, SWT.NONE);
		lblEmailAddress.setBounds(197, 37, 94, 15);
		lblEmailAddress.setText("Email Address");
		
		text_question = new Text(grpCreatePoll, SWT.BORDER);
		text_question.setBounds(10, 67, 181, 21);
		
		
		
		final Label lblQuestion = new Label(grpCreatePoll, SWT.NONE);
		lblQuestion.setBounds(197, 73, 55, 15);
		lblQuestion.setText("Question");
		
		final Combo combo_options = new Combo(grpCreatePoll, SWT.NONE);
		combo_options.setBounds(10, 96, 55, 22);
		combo_options.setToolTipText("Select the number of options the voters can choose. Must be an integer and within the max limit.");
		combo_options.setItems(new String[] {"2", "3", "4", "5", "6", "7", "8", "9", "10", "11"});
		
		final Label lblNumberOfOptions = new Label(grpCreatePoll, SWT.NONE);
		lblNumberOfOptions.setBounds(71, 104, 120, 15);
		lblNumberOfOptions.setText("Number of Options");
		
		
		
		Button btnCreatePoll = new Button(grpCreatePoll, SWT.NONE);
		btnCreatePoll.setBounds(93, 139, 109, 25);
		btnCreatePoll.addSelectionListener(new SelectionAdapter() {
			/*
			 * 	 Create Poll button.
			 * Creates a poll by sending a request to the server. 
			 * Information is extracted from the GUI and the information is then constructed into a 
			 * string and is sent to the sever. 
			 * The information is verified before being sent.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				String numOfOptions = combo_options.getText();
				String email = text_email.getText();
				String optionsString = "";
				Boolean valid = true;	//Ensures the information in valid.
				long pollID;			//Poll ID which is returned from the server.

				lblQuestion.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				lblEmailAddress.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				lblNumberOfOptions.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				
				try	//Ensures the number of options is an integer.
				{
					Integer.parseInt(numOfOptions);
				}
				catch(NumberFormatException e1)
				{
					lblNumberOfOptions.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					valid = false;
				}
				if(valid == true)	//Ensures the number of Options is within the limit of options and is not 0.
				{
					if(numOfOptions== "" || Integer.parseInt(numOfOptions) > maxOptions || Integer.parseInt(numOfOptions)<1)
					{
						
						lblNumberOfOptions.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
						valid = false;
					}
				}
				if(text_question.getText().equals("") == true)	//Ensures a question is entered.
				{
					valid = false;
					lblQuestion.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if(email == "" || email.contains("@") == false || email.contains(".") == false)	//Ensures a valid email is entered and in the correct format: contains a @ and a .
				{
					lblEmailAddress.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					valid = false;
				}
				/*
				 * Creates an optionsGUI that will get the options the voters can vote for.
				 */
				if(valid == true)	
				{
					try{
						OptionsGUI options = new OptionsGUI(numOfOptions);
						parent.setVisible(false);
						options.setBlockOnOpen(true);
						options.open();
						while(options.done == 0)	//Need some sort of blocking, wait/notify
						{	}
						
						optionsString = options.optionsValue;
						parent.setVisible(true);
						System.out.println(optionsString);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				/*
				 * Constructs the string that will be sent to the server.
				 * Receives the pollID from the server.
				 * Adds the poll id and the number of options to the active and created polls lists.
				 */
				if(valid == true)
				{	
					String message = "";	
					//Constructs the string to be sent to the server. 
					//Format: email|questions|number of options|Options1|Options2|Option3|etc...
					message = email + "|" + text_question.getText() +"|"+ numOfOptions + "|" + optionsString;
						
					pollID = admin.createPoll(message);//blocking, Returns the poll ID.
					
					addPollInfo(String.valueOf(pollID),text_question.getText(),optionsString);
					lblQuestion.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					lblEmailAddress.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					lblNumberOfOptions.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					
					list_Polls.add(String.valueOf(pollID) + " - " + numOfOptions);
					list_Active.add(String.valueOf(pollID)+ " - " + numOfOptions);
				}
			}
			
		});
		btnCreatePoll.setText("Create Poll");

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}
	
	public static void init(String args[]){
		int port = PollServer.ADMIN_PORT;		//Default port
		if (args.length > 0) {
		    try {
		        port = Integer.parseInt(args[0]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + " must be an integer");
		        System.exit(1);
		    }
		}
		//Creates the instance of an AdminClient with the port sent in either from command line argument or the default port.
		admin = new AdminClient(port, "127.0.0.1");
		try {
			AdminGUI window = new AdminGUI("127.0.0.1");
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		admin.disconnect();	//Disconnect once GUI is closed.
	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
//		init(args);
		new AdminGUI("127.0.0.1");
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setImage(SWTResourceManager.getImage(AdminGUI.class, "/model/polls-icon.png"));
		super.configureShell(newShell);
		newShell.setText("POLL ADMIN");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(654, 497);
	}
}
