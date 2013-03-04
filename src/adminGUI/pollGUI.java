/*
 * PollGUI
 * Displays the information for the poll.
 * 
 */

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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class pollGUI extends ApplicationWindow {

	private long numOptions;
	private long pollID;
	/**
	 * Create the application window.
	 */
	public pollGUI(long options, long pollId) {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		
		numOptions = options;
		pollID = pollId;
		
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(null);
		ProgressBar votingBar[] = new ProgressBar[(int) numOptions];
		Label lblVotes[] = new Label[(int) numOptions];
		Label lblOptions[] = new Label[(int) numOptions];
		//Creates the content based on the number of options.
		//The window is sized accordingly and the positioning.
		for(int i = 0; i < numOptions; i++)
		{
			lblVotes[i] = new Label(container, SWT.NONE);
			lblVotes[i].setBounds(460, 60 + i*35, 100, 15);
			lblVotes[i].setText("0 Votes");
			
			votingBar[i] = new ProgressBar(container, SWT.NONE);
			votingBar[i].setSelection(0);
			votingBar[i].setBounds(150, 50 + i*35, 300, 25);
			
			lblOptions[i] = new Label(container, SWT.NONE);
			lblOptions[i].setBounds(10, 60 + i*35, 100, 15);
			lblOptions[i].setText("Option " + (i+1));
						
		}

		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setBounds(10, 10, 514, 15);
		lblNewLabel.setText("Poll ID: " + String.valueOf(pollID));
	
		
	
		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
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
		try {
			pollGUI window = new pollGUI(10,213);
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
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(550, (int) (numOptions*35 + 120));//Size of window is based on the number of options.
		
	}
}
