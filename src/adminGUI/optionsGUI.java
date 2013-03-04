/*
 * OptionsGUI
 * GUI that prompts the user for the options that the voters can vote for.
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class optionsGUI extends ApplicationWindow {
	private Text[] options;
	public String optionsValue;
	private int numOptions;
	public int done = 0;
	
	/**
	 * Create the application window.
	 */
	public optionsGUI(String numOfOptions) {
		super(null);
		setShellStyle(SWT.TITLE);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		numOptions = Integer.parseInt(numOfOptions);
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
				value = options[i].getText() + ",";
			}
			else if(i == numOptions-1)
			{
				value = value + options[i].getText();
			}
			else
			{
				value = value + options[i].getText() + ",";
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
		final Label lblOptions[] = new Label[numOptions];
		Composite container = new Composite(parent, SWT.NONE);
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			/*
			 * Done Button.
			 * Collects the information from the combo boxes and will set the global variable to the constructed 
			 * string so that the admin Client can grab it.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				int valid = 0;
				optionsValue ="";
				//Constructs the options to be Option1|Option2|Option3|etc...
				for(int i = 0;i<numOptions;i++)
				{
					String temp = options[i].getText();
					//Checks if any of the options are blank.
					if(options[i].getText().equals("") == true)
					{
						valid = 1;
						lblOptions[i].setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
						
					}
					else
					{
						lblOptions[i].setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					}
					if(i ==0 && i!= numOptions-1)
					{
						optionsValue = options[i].getText() + "|";
					}
					else if(i == numOptions-1)
					{
						optionsValue = optionsValue + options[i].getText();
					}
					else
					{
						optionsValue = optionsValue + options[i].getText() + "|";
					}
				}
				if(valid == 0)
				{
					done = 1;
					close();
				}
				
			}
		});
		//Creates the objects on the GUI
		//Posiitons everything according to how many options there will be.
		
		
		
		btnNewButton.setBounds(77, numOptions*35 + 30, 68, 23);
		btnNewButton.setText("Done");
		
		options = new Text[numOptions];
		for(int i = 0; i <numOptions; i++)
		{
			lblOptions[i] = new Label(container, SWT.NONE);
			lblOptions[i].setBounds(10, 35*i + 35, 61, 20);	
			lblOptions[i].setText("Option " + (i+1) + ":");
			
			options[i] = new Text(container, SWT.BORDER);
			options[i].setBounds(75, 35*i + 35, 155, 20);
		}
				
		
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setBounds(77, 10, 89, 16);
		lblNewLabel_1.setText("Input Options");

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
			optionsGUI window = new optionsGUI("5");
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
		newShell.setText("Input Options");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(260, numOptions*35 + 130);	//Size of the window depends on how many options there are.
	}
}
