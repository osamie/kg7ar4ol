package voterGUI;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import client.Client;
import server.PollServer;


public class voterGUI extends ApplicationWindow {
	private Text text_PollID;
	private Text text_Choice;
	private Label lblError;
	private static client.Client voter;
	/**
	 * Create the application window.
	 */
	public voterGUI() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		Button btnVote = new Button(container, SWT.NONE);
		btnVote.addSelectionListener(new SelectionAdapter() {
			
			/*
			 * Voting Button
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				Boolean valid = true;
				String pollIdInput, choiceInput;
				Long pollId = null; 
				int choice=0;
				pollIdInput = text_PollID.getText();
				choiceInput = text_Choice.getText();
				
				try { 
					pollId = Long.parseLong(pollIdInput);
				} catch(NumberFormatException e1) { 
			       valid = false;
			       text_PollID.setText("");
			       lblError.setText("Invalid Poll ID");
			    }
				try { 
					choice = Integer.parseInt(choiceInput);
				} catch(NumberFormatException e1) { 
			       valid = false;
			       text_Choice.setText("");
			       lblError.setText("Invalid Choice Value");
			    }
				
				if(valid == true)
				{
					voter.vote(pollId, choice);
					lblError.setText("Vote Sent");
				}
				
			}
		});
		
		
		btnVote.setBounds(83, 72, 107, 37);
		btnVote.setText("Vote");
		
		text_PollID = new Text(container, SWT.BORDER);
		text_PollID.setBounds(73, 45, 76, 21);
		
		text_Choice = new Text(container, SWT.BORDER);
		text_Choice.setBounds(172, 45, 32, 21);
		
		Label lblPollId = new Label(container, SWT.NONE);
		lblPollId.setBounds(73, 24, 55, 15);
		lblPollId.setText("Poll ID");
		
		Label lblChoice = new Label(container, SWT.NONE);
		lblChoice.setBounds(172, 24, 55, 15);
		lblChoice.setText("Choice");
		
		lblError = new Label(container, SWT.NONE);
		lblError.setBounds(90, 3, 171, 15);

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
		int portNum = PollServer.VOTING_PORT;	
		if (args.length > 0) {
		    try {
		        portNum = Integer.parseInt(args[0]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + " must be an integer");
		        System.exit(1);
		    }
		}
		voter = new client.Client(portNum);
		try {
			voterGUI window = new voterGUI();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Voter");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(304, 228);
	}
}
