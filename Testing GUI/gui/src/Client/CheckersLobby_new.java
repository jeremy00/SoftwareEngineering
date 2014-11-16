package Client;



import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Button;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import RMIConnection.Interfaces.CheckersClient;
import RMIConnection.Interfaces.RMIServerInterface;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class CheckersLobby_new extends JFrame implements CheckersClient {

	public enum State {notConnected, connected, inLobby, onTable, inGame};
	private ArrayList<String> lobbyUserList; //string lists of users for output
	private static RMIServerInterface serverConnection;
	private static State curState;
	private String conText = "To connect, enter <ip address> <username>"; //
	private JList userListPane;
	private JScrollPane userPane;
	private JTabbedPane jTabbedPane;
	private JButton submitButton;
	private JTextField chatInputField;

	private JTextArea chatArea;
	private JTextArea tableList;
	private JScrollPane chatPane;
	private String myName = "";
	private String selectedUser = "";
	
	private boolean isCheckers;
	private byte[][] curBoardState;
	private boolean debug = false;	// set true for debug mode, which prints more messages.

	private JPanel contentPane;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {    
				CheckersLobby_new tester = new CheckersLobby_new();
				tester.setLocationRelativeTo(null);
				tester.setVisible(true);
				System.setProperty("java.security.policy","file:./src/Client/client.policy");
				System.setProperty("java.rmi.server.codebase", "file:./bin/");
				
				//now establish a presence in the RMI registry and try to get the checkers server connector.
				try{   
		    		//generate a random registry id for this player
			    	String name = "CheckersClient"+(int)(Math.random()*10000);
			    		//export the player to the registry. Stub is a reference to the object in the reg.
			        CheckersClient stub =
			            (CheckersClient) UnicastRemoteObject.exportObject((CheckersClient)tester, 0);
			        //get the registry
			        Registry registry = LocateRegistry.getRegistry();
			        //bind the object in registry to the unique registry id we generated
			        registry.rebind(name, stub);
			        System.out.println("TestClient bound to registry!");
			        //connect to the RMI server connection on this pc (localhost) and give it the id of this client.
			    	tester.getServerConnection("localhost", name);
			    	
			    	//add a hook to disconnect for when the user force quits / alt+f4 / cmd+q's
			    	Runtime.getRuntime().addShutdownHook(new Thread()
			    	{
			    	    @Override
			    	    public void run()
			    	    {
							try {
								serverConnection.disconnect(false);
							} catch (RemoteException e) {
								/** 
								 * it's dead lol
								**/
							}
			    	    }
			    	});
			    	
			    }catch(RemoteException e){
			    	System.out.println("Error binding client to registry.");
			    	System.out.println(e.getMessage());
			    }			
			}
		});
	}
	private void getServerConnection(String host, String clientID) {
		if (System.getSecurityManager() == null) {
			 System.setSecurityManager(new SecurityManager());
	    }
	    try {
	        String name = "CheckersServerInterface";
	        Registry registry = LocateRegistry.getRegistry(host);
	        serverConnection = (RMIServerInterface) registry.lookup(name);
	        if(serverConnection != null){
	        	System.out.println("Server connection found in registry!");
	        	serverConnection.registerPlayer(clientID, host);
	        }
	        else{
	        	System.out.println("Could not register with the server");	        	
	        	System.exit(0);
	        }
	    } catch (Exception e) {
	        System.err.println("TestClient Exception:");
	        e.printStackTrace();
	    }
	}	

	/**
	 * Create the frame.
	 */
	public CheckersLobby_new() {
		super();
		lobbyUserList = new ArrayList<String>();
		curState = State.notConnected;
		initGUI();
	}
	
	private void initGUI() {
		try {
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 523, 477);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);
			contentPane.setSize(500, 500);
			
			submitButton = new JButton("New button");
			submitButton.setBounds(435, 411, 70, 22);
			contentPane.add(submitButton);
			
			chatArea = new JTextArea();
			chatArea.setForeground(Color.WHITE);
			chatArea.setBounds(10, 157, 495, 239);
			contentPane.add(chatArea);
			
			userListPane = new JList();
			userListPane.setBounds(379, 11, 126, 122);
			contentPane.add(userListPane);
			
			 tableList = new JTextArea();
			tableList.setBounds(10, 15, 359, 131);
			contentPane.add(tableList);
			
			chatInputField = new JTextField("fe80::e945:8111:8e95:9e9d");
			chatInputField.setBounds(10, 411, 419, 20);
			contentPane.add(chatInputField);
			chatInputField.setColumns(10);
			setActionListeners();
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					try{
						if(curState != State.notConnected){
							serverConnection.disconnect(true);
						}
					}catch(RemoteException e){ 
						//suppress
					}
					System.exit(1);
				}
			});
			{
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*Set the submit button up to listen for a click and launch inputSubmit when it happens.*/
	public void setActionListeners(){
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				inputSubmit();
			}
		});
	}
		
	// Event for submitButton and ENTER key
	private void inputSubmit(){
		try{
			System.out.println("Submit buttons was pressed.");
			//chatInputField.setText("button pressed");
			if(curState.equals(State.notConnected)){
				String input[] = chatInputField.getText().split("\\s");
				//0 - ip address 1 - username			
				if(input.length < 2) { 
					output("Connection failed. Did you remember to add a username?");
				}
				else if(!serverConnection.connectToServer(input[0], input[1])) {					
					output("Connection failed. Check console output of RMI process for information.");
				}
				else {
					myName = input[1];
					System.out.print("welcome! ");
					chatArea.setText(">> Welcome, " + myName + "!\n");
				}
			}
			else //if(curState.equals(State.inLobby)){		
				{String input = chatInputField.getText();
				// Private Message
				if (input.startsWith("@")) {
					String pmInput[] = input.split("\\s", 2);
					String recp = pmInput[0].substring(1);
					String msg = pmInput[1];
					serverConnection.sendMsg(recp, msg);
					if (!recp.equals(myName))
						output("[PM to " + recp + "] " + myName + ": " + msg);
				}
				// Public Message
				else {
					serverConnection.sendMsg_All(input);
				}
			}
		}catch(RemoteException e){
			output("A remote exception occured: ");
			output(e.getMessage());
		}finally{
			chatInputField.setText("");
		}
	}

	// Event for adding private message format to text area
	private void userListSelect() {
		String input = chatInputField.getText();
		if (input.startsWith("@")) {
			String pmInput[] = input.split("\\s", 2);
			pmInput[0] = "@" + selectedUser;
			chatInputField.setText(pmInput[0] + " " + pmInput[1]);
		}
		else
			chatInputField.setText("@" + selectedUser + " " + input);
	}
	

	
	
	
	/**
	* GUI Helpers
	*/
	public JScrollPane getChatPane() {
		if(chatPane == null) {
			chatPane = new JScrollPane(getChatArea());
			chatPane.setPreferredSize(new java.awt.Dimension(406, 267));

		}
		return chatPane;
	}
	public JTextArea getChatArea() {
		
		return chatArea;
	}
	private JTextField getChatInputField() {
		
			chatInputField = new JTextField("130.108.28.165");
			chatInputField.setScrollOffset(1);
			chatInputField.setPreferredSize(new java.awt.Dimension(389, 29));
			chatInputField.addKeyListener(new KeyAdapter(){
				// Listener for ENTER key
				public void keyPressed(KeyEvent evt) {
					if (evt.getKeyCode() == 10)
						inputSubmit();
				}
			});
		
		return chatInputField;
	}
	private JButton getSubmitButton() {
//		if (submitButton == null) {
//			submitButton = new JButton();
//			submitButton.setText("Enter");
//			submitButton.setBackground(new java.awt.Color(255,64,64));
//			submitButton.setBackground(new java.awt.Color(235,233,237));
//			submitButton.setPreferredSize(new java.awt.Dimension(61, 31));
//			
//		}
//		submitButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent evt) {
//				inputSubmit();
//			}
//		});
		return submitButton;
	}	
	private JTabbedPane getJTabbedPane() {
//		if(jTabbedPane == null) {
//			jTabbedPane = new JTabbedPane();
//			jTabbedPane.setPreferredSize(new java.awt.Dimension(163, 250));
//			jTabbedPane.addTab("Users", null, getUserPane(), null);
//			//jTabbedPane.addTab("Tables", null, getTablePane(), null);
//		}
		return jTabbedPane;
	}
	private JScrollPane getUserPane() {
		if(userPane == null) {
			userPane = new JScrollPane(getUserListPane());
		}
		return userPane;
	}
	private JList getUserListPane() {
		if (userListPane == null) {
			String[] userList = new String[lobbyUserList.size()];
			lobbyUserList.toArray(userList);
			userListPane = new JList();
			userListPane.setModel(new DefaultComboBoxModel(userList));
			userListPane.setBackground(new java.awt.Color(255,255,255));
			userListPane.setFont(new java.awt.Font("Tahoma",0,12));
			userListPane.setModel(new DefaultComboBoxModel(userList));
	
			// Creates pop-up menu and menu item
			final JPopupMenu popup = new JPopupMenu();
			JMenuItem menuItem = new JMenuItem("Send a PM");
			menuItem.addMouseListener(new MouseAdapter() {
				// Adds PM tag to the input
				public void mouseReleased(MouseEvent e) {
					userListSelect();
				}
			});
			popup.add(menuItem);
			// Opens popup menu
			userListPane.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					maybeShowPopup(e);
				}
				public void mouseReleased(MouseEvent e) {
					int index = userListPane.locationToIndex(e.getPoint());
					selectedUser = lobbyUserList.get(index);
					maybeShowPopup(e);
				}
				private void maybeShowPopup(MouseEvent e) {
					if (e.isPopupTrigger())
						popup.show(e.getComponent(), e.getX(), e.getY());
				}
			});
		}
		return userListPane;
	}
	
	// Updates the actual user list pane
	private void updateUserList(){
		String[] userList = new String[lobbyUserList.size()];
		lobbyUserList.toArray(userList);
		ListModel lstUsersModel = new DefaultComboBoxModel(userList);
		userListPane.setModel(lstUsersModel);	
	}
	
	// Helper method for outputing to the chat pane
		private void output(String s){		
	        chatArea.append(s + "\r\n");
	        chatArea.setCaretPosition(chatArea.getDocument().getLength());
		}
		
		// Forwards debug messages to output() if debugging is turned on
		private void debugOutput(String s){
			if (debug)
				output(s);
		}

	/**
	 * ***************************************
	 * ***************************************
	 * Methods satisfying the checkers client interface
	 ****************************************
	 ****************************************
	 */
	public void connectionOK() {
		debugOutput("Server says connection OK!");
		curState = State.connected;		
	}
	public void nowJoinedLobby(String user) {
		if(!user.equals(this.myName)){
			debugOutput(">> "+user+" has joined the lobby.");
		}
		lobbyUserList.add(user);
		updateUserList();
	}
	public void newMsg(String user, String msg, boolean pm) {
		if(pm) {
			output("[PM] " + user + ": " + msg);
		}
		else output(user + ": " + msg);
	}
	//alert that a user has left the lobby
	public void nowLeftLobby(String user) {
		lobbyUserList.remove(user);
		updateUserList();
	}
	//updated listing of users in lobby
	public void usersInLobby(String[] users) {		
		lobbyUserList.clear();
		for(String s:users)
			lobbyUserList.add(s);
		updateUserList();
	}
	//alert that you have joined the lobby
	public void youInLobby() {
		curState = State.inLobby;
		output(">> Welcome to the game lobby.");
	}
	//alert that you have left the lobby
	public void youLeftLobby() {
		curState = State.connected;
		output(">> You have left the game lobby.");		
	}
	//initial listing of tables
	public void tableList(int[] tids) {

	}
	//an alert saying that a table state has changed. 
	//this is received whenever anyone joins or leaves a table,
	//or if table state is queried by calling getTblStatus()
	public void onTable(int tid, String blackSeat, String redSeat) {
		
	}
	//same preconditions as onTable()
	//called immediately after onTable()
    public void tableGame(int tid) throws RemoteException {
    	
	}
	public void newTable(int t) {
		
	}
	//alert that you have joined the table with id tid.
	public void joinedTable(int tid) {
		curState = State.onTable;
		debugOutput(">> You have joined table " + Integer.toString(tid));
	}	
	//alert that you have left your table.
	public void alertLeftTable() {
		curState = State.connected;
		debugOutput(">> You have left the table");
	}
	//alert that at the table you are sitting at, a game is starting.
	public void gameStart() {
		curState = State.inGame;
		if (isCheckers) {
			curBoardState = new byte[8][8];
			for(int y=0;y<8;y++) {
				for(int x=0;x<8;x++) {
					// if both row & column are even (or ordd)
					if( (x%2==0 && y%2==0) || (x%2!=0 && y%2!=0))
						curBoardState[y][x] = 0;
					else if(y<3) // top three rows
						curBoardState[y][x] = 1;
					else if(y>4) // bottom three rows
						curBoardState[y][x] = 2;
				}
			}	
		}
		else {
			curBoardState = new byte[19][19];
			for(int y=0;y<19;y++)
				for(int x=0;x<19;x++)
					curBoardState[y][x] = 0;	
		}
	}
	//alert that your color is Black, for the game.
	public void colorBlack() {
		
	}
	//alert that your color is Red, for the game.
	public void colorRed() {
		
	}
	//notice that your opponent has moved from position (fr,fc) to (tr,tc)
	public void oppMove(int fr, int fc, int tr, int tc) {
		debugOutput(">> oppMove("+fr+","+fc+","+tr+","+tc+")");
	}
	//server has updated the board state
	public void curBoardState(int t, byte[][] boardState) {
		
	}
	//notice that for the game you are playing, you win!
	public void youWin() {
		
	}
	//notice that for the game you are playing, you lost.
	public void youLose() {
		debugOutput(">> youLose()");
	}
	//its your turn.
	public void yourTurn() {
		debugOutput(">> yourTurn()");
	}
	//you are now observing table tid.
	public void nowObserving(int tid) {
		debugOutput(">> nowObserving("+tid+")");
	}
	//you stopped observing table tid.
	public void stoppedObserving(int tid) {
		debugOutput(">> stoppedObserving("+tid+")");
	}
	

    /***************************************
     ****************************************
     * Error messages
     ****************************************
     ****************************************
     */
	public void networkException(String msg) {
		output("A network exception has occured. Connection lost.");
		output(conText);
		curState = State.notConnected;
	}
	public void nameInUseError() {
		chatArea.setText("");
		output("The name requested is in use. Please choose another.");
		output(conText);
		curState = State.notConnected;
	    chatInputField.setText("137.99.11.115 ");
	}
	public void nameIllegal() throws RemoteException {
		output("The name requested is illegal. Length must be > 0 and have no whitespace.");
		output(conText);
		curState = State.notConnected;
	    chatInputField.setText("137.99.11.115 ");	
	}
	//the requested move is illegal.
	public void illegalMove() {
		output(">> That move is illegal!");
	}
	//the table your trying to join is full.
	public void tableFull() {
		output(">> The table you are trying to join is full. Please choose another one.");
	}
	//the table queried does not exist.
	public void tblNotExists() {
		debugOutput(">> tblNotExists()");
	}
	
	//called if you say you are ready on a table with no current game.
	public void gameNotCreatedYet() {
		output(">> Please wait for an opponent before starting the game.");
	}
	//called if it is not your turn but you make a move.
	public void notYourTurn() {
		output(">> It is not your turn!");
	}
	//called if you send a stop observing command but you are not observing a table.
	public void notObserving() {
		debugOutput(">> notObserving()");
	}
	//called if you send a game command but your opponent is not ready
	public void oppNotReady() {
		output(">> Please wait for your opponent to start the game.");
	}
	//you cannot perform the requested operation because you are in the lobby.
	public void errorInLobby() {
		output(">> You cannot perform that action from within the lobby.");
	}
	//called if the client sends an ill-formated TCP message
	public void badMessage() {
		debugOutput(">> badMessage()");
	}
	//called when your opponent leaves the table
	public void oppLeftTable() {
		debugOutput(">> oppLeftTable()");
	}
	//you cannot perform the requested op because you are not in the lobby.
	public void notInLobby() {
		output(">> You cannot perform that action from outside of the lobby.");
	}
	
}
