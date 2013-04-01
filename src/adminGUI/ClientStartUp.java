/*
 * Admin GUI
 * GUI for the admin client.
 * GUI creates an instance of AdminClient and the GUI is attached to the AdminClient.
 * Extracts information from the GUI and sends requests to the server.
 * 
 */
package adminGUI;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Slider;

import server.PollServer;
import voterGUI.VoterGUI;
import client.AdminClient;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;


public class ClientStartUp extends ApplicationWindow {
	
	private String[][] pollInfo;
	private int pollInfoLength;
//	private static AdminClient admin;
	private int maxOptions = 10;
	private Text text_IPAddress;
	/**
	 * Create the application window.
	 */
	public ClientStartUp() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		//addMenuBar();
		//addStatusLine();
		pollInfoLength = 25;
		pollInfo = new String[pollInfoLength][3];		
	}

	
	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(final Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		Composite container = new Composite(parent, SWT.NONE);
		container.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		String items[] = new String[maxOptions];
		for(int i = 0; i < maxOptions; i++)
		{
			items[i] = String.valueOf(i+1);
		}
		
		
		final Button voterClient_btn = new Button(container, SWT.NONE);
		voterClient_btn.addSelectionListener(new SelectionAdapter() {
			/*
			 * Launch voter client button
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean valid;
				valid = validateIP(0);
				if(valid==true)
				{
				voterClient_btn.setEnabled(false);
				Display.getDefault().asyncExec(new Runnable() {
					public void run(){
						new VoterGUI(text_IPAddress.getText());
					}
				});
				voterClient_btn.setEnabled(true);
				}
			}
		});
		voterClient_btn.setBounds(56, 104, 139, 25);
		voterClient_btn.setText("VOTER");
		
		final Button adminClient_btn = new Button(container, SWT.NONE);
		adminClient_btn.addSelectionListener(new SelectionAdapter() {
			/*
			 * Launch Admin Client Button
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean valid;
				valid = validateIP(1);
				if(valid == true)
				{
					adminClient_btn.setEnabled(false);
					Display.getDefault().asyncExec(new Runnable() {
						public void run(){
							new AdminGUI(text_IPAddress.getText());
						}
				
				});
				
				adminClient_btn.setEnabled(true);
				}
			}
		});
		adminClient_btn.setBounds(56, 52, 139, 28);
		adminClient_btn.setText("ADMINISTRATOR");
		
		Label lblPollClients = new Label(container, SWT.NONE);
		lblPollClients.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblPollClients.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblPollClients.setFont(SWTResourceManager.getFont("Segoe Print", 13, SWT.BOLD));
		lblPollClients.setBounds(79, 10, 93, 36);
		lblPollClients.setText("LAUNCH:");
		
		text_IPAddress = new Text(container, SWT.BORDER);
		text_IPAddress.setText("127.0.0.1");
		text_IPAddress.setBounds(56, 162, 139, 21);
		
		Label lblIpAddress = new Label(container, SWT.NONE);
		lblIpAddress.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblIpAddress.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblIpAddress.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblIpAddress.setAlignment(SWT.CENTER);
		lblIpAddress.setBounds(56, 135, 139, 21);
		lblIpAddress.setText("IP Address");

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
	 * Checks to see if the IP address is valid and it will ping the address to see if 
	 * you can connect to that address.
	 * @param int voteOrAdmin - 0 = voting, 1 = admin
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
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		
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
//		admin = new AdminClient(port);
		try {
			ClientStartUp window = new ClientStartUp();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		admin.disconnect();	//Disconnect once GUI is closed.
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setImage(SWTResourceManager.getImage(ClientStartUp.class, "/model/polls-icon.png"));
		super.configureShell(newShell);
		newShell.setText("POLL CLIENTS");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(270, 263);
	}
}
