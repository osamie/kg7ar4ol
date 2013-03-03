//vhgjdfgdj

package adminGUI;

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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Slider;

import server.PollServer;
import client.AdminClient;


public class adminGUI extends ApplicationWindow {
	private Text txtSampledomaincom;
	private Text text_question;
	private Text text_2;
	private static AdminClient admin;
	private int maxOptions = 30;
	/**
	 * Create the application window.
	 */
	public adminGUI() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	
	private Boolean verifyPollId()
	{
		
		return true;
	}
	private void updateInfo()
	{
		
	}
	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		final List list_Polls = new List(container, SWT.BORDER | SWT.V_SCROLL);
		list_Polls.setItems(new String[] {});
		list_Polls.setBounds(10, 31, 181, 133);
		
		final List list_Paused = new List(container, SWT.BORDER | SWT.V_SCROLL);
		list_Paused.setBounds(282, 238, 161, 138);
		
		final List list_Active = new List(container, SWT.BORDER | SWT.V_SCROLL);
		list_Active.setBounds(14, 238, 177, 138);
		
		txtSampledomaincom = new Text(container, SWT.BORDER);
		txtSampledomaincom.setText("sample@domain.com");
		txtSampledomaincom.setBounds(282, 14, 181, 21);
		
		Label lblEmailAddress = new Label(container, SWT.NONE);
		lblEmailAddress.setBounds(469, 15, 94, 15);
		lblEmailAddress.setText("Email Address");
		
		text_question = new Text(container, SWT.BORDER);
		text_question.setBounds(282, 45, 181, 21);
		
		text_2 = new Text(container, SWT.BORDER);
		text_2.setBounds(283, 105, 180, 21);
		
		Label lblQuestion = new Label(container, SWT.NONE);
		lblQuestion.setBounds(469, 51, 55, 15);
		lblQuestion.setText("Question");
		
		final Combo combo_answers = new Combo(container, SWT.NONE);
		String items[] = new String[maxOptions];
		for(int i = 0; i < maxOptions; i++)
		{
			items[i] = String.valueOf(i+1);
		}
		combo_answers.setItems(items);
		combo_answers.setBounds(282, 74, 55, 23);
		
		Label lblNumberOfAnswers = new Label(container, SWT.NONE);
		lblNumberOfAnswers.setBounds(343, 82, 120, 15);
		lblNumberOfAnswers.setText("Number of Answers");
		
		final Label label_answers = new Label(container, SWT.NONE);
		label_answers.setBounds(471, 82, 153, 15);
		
		Label lblCreatedPolls = new Label(container, SWT.NONE);
		lblCreatedPolls.setBounds(10, 10, 94, 15);
		lblCreatedPolls.setText("Created Polls");
		
		
		
		Button btnCreatePoll = new Button(container, SWT.NONE);
		btnCreatePoll.addSelectionListener(new SelectionAdapter() {
			/*
			 * 	 Create Poll button.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				String numOfOptions = combo_answers.getText();
				String email = txtSampledomaincom.getText();
				Boolean valid = true;
				long pollID;
				if(numOfOptions== "" || numOfOptions.compareTo(String.valueOf(maxOptions)) > 0)
				{
					label_answers.setText("Select the amount of answers.");
					valid = false;
				}
				if(email == "" || email.contains("@") == false)
				{
					txtSampledomaincom.setText("Invalid Email Address");
					valid = false;
				}
				if(valid == true)
				{
					pollID = admin.createPoll(numOfOptions, email);
					label_answers.setText("");
					list_Polls.add(String.valueOf(pollID) + " - " + numOfOptions);
					list_Active.add(String.valueOf(pollID)+ " - " + numOfOptions);
				}
			}
			
		});
		btnCreatePoll.setBounds(201, 10, 75, 25);
		btnCreatePoll.setText("Create Poll");
		
		
		Button btnPausePoll = new Button(container, SWT.NONE);
		btnPausePoll.addSelectionListener(new SelectionAdapter() {
			/*
			 * Pause Poll
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(list_Active.getSelectionIndex()!=-1 && list_Active.getItemCount()!=0)
				{
					int focusedIndex = list_Active.getFocusIndex();
					String temp = list_Active.getItem(focusedIndex);
					temp = temp.substring(0, temp.indexOf(" ")-1);
					System.out.println(temp);
					admin.pausePoll(temp);
					list_Paused.add(list_Active.getItem(focusedIndex));
					list_Active.remove(focusedIndex);
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
		btnPausePoll.setBounds(201, 351, 75, 25);
		btnPausePoll.setText("Pause Poll");
		
		Button btnResumePoll = new Button(container, SWT.NONE);
		btnResumePoll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(list_Paused.getSelectionIndex()!=-1 && list_Paused.getItemCount()!=0)
				{
					int focusedIndex = list_Paused.getFocusIndex();
					String temp = list_Paused.getItem(focusedIndex);
					temp = temp.substring(0, temp.indexOf(" ")-1);
					System.out.println(temp);
					admin.resumePoll(temp);
					list_Active.add(list_Paused.getItem(focusedIndex));
					list_Paused.remove(focusedIndex);
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
		btnResumePoll.setBounds(449, 351, 75, 25);
		btnResumePoll.setText("Resume Poll");
		
		Button btnClearPoll = new Button(container, SWT.NONE);
		btnClearPoll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(list_Polls.getFocusIndex()!=-1)
				{
					String temp = list_Polls.getItem(list_Polls.getFocusIndex());
					temp = temp.substring(0, temp.indexOf(" ")-1);
					System.out.println(temp);
					admin.clearPoll(temp);
				}
			}
		});
		btnClearPoll.setBounds(201, 43, 75, 25);
		btnClearPoll.setText("Clear Poll");
		
		Button btnStopPoll = new Button(container, SWT.NONE);
		btnStopPoll.addSelectionListener(new SelectionAdapter() {
			/*
			 * Stop Button
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(list_Polls.getSelectionIndex()!=-1 && list_Polls.getItemCount()!=0)
				{
					int index = list_Polls.getSelectionIndex();
					String temp = list_Polls.getItem(index);
					String pollId = temp.substring(temp.lastIndexOf(" ") + 1);
					admin.stopPoll(pollId);
					list_Polls.remove(index);
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
		btnStopPoll.setBounds(201, 72, 75, 25);
		btnStopPoll.setText("Stop Poll");
		
		
		
		Button btnDisplayPoll = new Button(container, SWT.NONE);
		btnDisplayPoll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(list_Polls.getSelectionIndex()!=-1 && list_Polls.getItemCount()!=0)
				{
					int index = list_Polls.getSelectionIndex();
					String temp = list_Polls.getItem(index);
					String pollId = temp.substring(temp.lastIndexOf(" ") + 1);
					
					pollGUI poll = new pollGUI(Long.valueOf(temp.substring(temp.lastIndexOf(" ") + 1)),Long.valueOf(pollId));
					poll.setBlockOnOpen(true);
					poll.open();
					
					
				}
				
			}
		});
		btnDisplayPoll.setBounds(10, 170, 75, 25);
		btnDisplayPoll.setText("Display Poll");
		
		Label lblPausedPolls = new Label(container, SWT.NONE);
		lblPausedPolls.setBounds(282, 217, 133, 15);
		lblPausedPolls.setText("Paused Polls");
		
		Label lblActivePolls = new Label(container, SWT.NONE);
		lblActivePolls.setBounds(14, 217, 129, 15);
		lblActivePolls.setText("Active Polls");

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

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		
		int port = PollServer.ADMIN_PORT;		
		if (args.length > 0) {
		    try {
		        port = Integer.parseInt(args[0]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + " must be an integer");
		        System.exit(1);
		    }
		}
		
		admin = new AdminClient(port);
		try {
			adminGUI window = new adminGUI();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		admin.disconnect();
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(640, 497);
	}
}
