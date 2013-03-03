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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class answersGUI extends ApplicationWindow {
	private Text[] answers;
	public String answersValue;
	private int numOptions;
	public int done = 0;
	/**
	 * Create the application window.
	 */
	public answersGUI(String numOfAnswers) {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		numOptions = Integer.parseInt(numOfAnswers);
	}
	public String getVals()
	{
		String value ="";
		while(done ==0)
		{}
		for(int i = 0;i<numOptions;i++)
		{
			if(i ==0 && i!= numOptions-1)
			{
				value = answers[i].getText() + ",";
			}
			else if(i == numOptions-1)
			{
				value = value + answers[i].getText();
			}
			else
			{
				value = value + answers[i].getText() + ",";
			}
		}
		System.out.println(value);
		return value;
	}
	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			/*
			 * Done Button.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				int valid = 0;
				answersValue ="";
				for(int i = 0;i<numOptions;i++)
				{
					String temp = answers[i].getText();
					if(answers[i].getText().equals("") == true)
					{
						valid = 1;
						answers[i].setText("Enter a Value");
					}
					if(i ==0 && i!= numOptions-1)
					{
						answersValue = answers[i].getText() + "|";
					}
					else if(i == numOptions-1)
					{
						answersValue = answersValue + answers[i].getText();
					}
					else
					{
						answersValue = answersValue + answers[i].getText() + "|";
					}
				}
				if(valid == 0)
				{
					done = 1;
					close();
				}
				
			}
		});
		Label lblAnswers[] = new Label[numOptions];
		
		
		btnNewButton.setBounds(77, numOptions*35 + 30, 68, 23);
		btnNewButton.setText("Done");
		
		answers = new Text[numOptions];
		for(int i = 0; i <numOptions; i++)
		{
			lblAnswers[i] = new Label(container, SWT.NONE);
			lblAnswers[i].setBounds(10, 35*i + 35, 61, 20);
			lblAnswers[i].setText("Answer " + (i+1) + ":");
			
			answers[i] = new Text(container, SWT.BORDER);
			answers[i].setBounds(75, 35*i + 35, 155, 20);
		}
				
		
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setBounds(77, 10, 89, 13);
		lblNewLabel_1.setText("Input Answers");

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			answersGUI window = new answersGUI("12");
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
		newShell.setText("Input Answers");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(260, numOptions*35 + 110);
	}
}
