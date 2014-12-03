package Client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Button;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.Position;
import javax.swing.ListSelectionModel;

public class CheckersLobby_new extends JFrame implements CheckersClient {

	public enum State {
		notConnected, connected, inLobby, onTable, inGame
	};

	 // String list of tables// string lists of users for output
	private ArrayList<String> lobbyUserList; // string lists of users for output
	private DefaultListModel tableList; // String list of tables
	private static RMIServerInterface serverConnection;
	private static State curState;
	private static String yourip = "fe80::108c:c728:1808:cec5";
	private String conText = "To connect, enter <ip address> <username>"; //
	private JList userListPane;
	private JScrollPane userPane;
	private JButton submitButton;
	private JButton btnCreateGame;
	private JButton btnJoinGame;
	private JButton btnObserveGame;
	private JTextField chatInputField;

	private JTextArea chatArea;
	private JList tableListPane;
	private JScrollPane chatPane;
	private String myName = "";
	private int selectedTable;

	private boolean isCheckers;
	private byte[][] curBoardState;
	private boolean debug = false; // set true for debug mode, which prints more
									// messages.
	private int[] tids;
	private int currentTableID;
	private checkersTable currentTable;// The current table the user is in.
										// declared in newTable(int t)
	private JPanel contentPane;
	private final JScrollPane scrollPane = new JScrollPane();
	private final JScrollPane scrollPane_1 = new JScrollPane();
	private final JScrollPane scrollPane_2 = new JScrollPane();
	private JLabel lblUserList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CheckersLobby_new tester = new CheckersLobby_new();
				tester.setLocationRelativeTo(null);
				tester.setVisible(true);
				System.setProperty("java.security.policy",
						"file:./src/Client/client.policy");
				System.setProperty("java.rmi.server.codebase", "file:./bin/");

				// now establish a presence in the RMI registry and try to get
				// the checkers server connector.
				try {
					// generate a random registry id for this player
					String name = "CheckersClient"
							+ (int) (Math.random() * 10000);
					// export the player to the registry. Stub is a reference to
					// the object in the reg.
					CheckersClient stub = (CheckersClient) UnicastRemoteObject
							.exportObject((CheckersClient) tester, 0);
					// get the registry
					Registry registry = LocateRegistry.getRegistry();
					// bind the object in registry to the unique registry id we
					// generated
					registry.rebind(name, stub);
					System.out.println("TestClient bound to registry!");
					// connect to the RMI server connection on this pc
					// (localhost) and give it the id of this client.
					tester.getServerConnection("localhost", name);

					// add a hook to disconnect for when the user force quits /
					// alt+f4 / cmd+q's
					Runtime.getRuntime().addShutdownHook(new Thread() {
						@Override
						public void run() {
							try {
								serverConnection.disconnect(false);
							} catch (RemoteException e) {
								/**
								 * it's dead lol
								 **/
							}
						}
					});

				} catch (RemoteException e) {
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
			if (serverConnection != null) {
				System.out.println("Server connection found in registry!");
				serverConnection.registerPlayer(clientID, host);
			} else {
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
		//selobbyUserList = new ArrayList<String>();
		tableList = new DefaultListModel();
		curState = State.notConnected;
		initGUI();
	}

	private void initGUI() {
		try {

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 670, 477);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);
			contentPane.setSize(500, 500);

			submitButton = new JButton("Submit");
			submitButton.setBounds(574, 410, 70, 22);
			contentPane.add(submitButton);

			/* set chat input field as your ip (variable at beginning of class */
			chatInputField = new JTextField(getLocalIPV6Address());
			chatInputField.setBounds(145, 411, 419, 20);
			contentPane.add(chatInputField);
			chatInputField.setColumns(10);

			/* CREATE JOIN OBSERVE BUTTONS */
			btnCreateGame = new JButton("Create Game");
			btnCreateGame.setEnabled(false);
			btnCreateGame.setFont(new Font("Rockwell", Font.BOLD, 15));
			btnCreateGame.setBounds(10, 11, 172, 42);
			contentPane.add(btnCreateGame);

			btnJoinGame = new JButton("Join Game");
			btnJoinGame.setEnabled(false);
			btnJoinGame.setFont(new Font("Rockwell", Font.BOLD, 15));
			btnJoinGame.setBounds(10, 63, 172, 42);
			contentPane.add(btnJoinGame);

			btnObserveGame = new JButton("Observe Game");
			btnObserveGame.setEnabled(false);
			btnObserveGame.setFont(new Font("Rockwell", Font.BOLD, 15));
			btnObserveGame.setBounds(10, 116, 172, 42);
			contentPane.add(btnObserveGame);
			scrollPane.setBounds(10, 169, 634, 230);
			contentPane.add(scrollPane);

			chatArea = new JTextArea(conText);
			scrollPane.setViewportView(chatArea);
			chatArea.setForeground(Color.BLACK);
			scrollPane_1.setBounds(192, 11, 270, 147);
			contentPane.add(scrollPane_1);

			tableListPane = new JList(tableList);
			tableListPane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			scrollPane_1.setViewportView(tableListPane);

			JLabel lblTableList = new JLabel("Table List:");
			scrollPane_1.setColumnHeaderView(lblTableList);
			scrollPane_2.setBounds(472, 11, 172, 147);
			contentPane.add(scrollPane_2);

			userListPane = new JList();
			userListPane.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			scrollPane_2.setViewportView(userListPane);

			lblUserList = new JLabel("User List:");
			scrollPane_2.setColumnHeaderView(lblUserList);

			setActionListeners();

			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					try {
						if (curState != State.notConnected) {
							serverConnection.disconnect(true);
						}
					} catch (RemoteException e) {
						// suppress
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

	/*
	 * Set the submit button up to listen for a click and launch inputSubmit
	 * when it happens.
	 */
	public void setActionListeners() {
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				inputSubmit();
			}
		});
		/* Set action listeners for the Create/Join/Observe game buttons. */
		btnCreateGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				output(">> Creating table");
				try {
					serverConnection.makeTable(myName);


				} catch (RemoteException e) {

					output("Couldn't create table");
				}
			}
		});
		btnJoinGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				output(">> Joining table " + selectedTable);
				try {
					serverConnection.joinTable(myName, selectedTable);
//
//<<<<<<< HEAD
//					//get the id of the table
//					int tid = Integer.parseInt((String) tableListPane.getSelectedValue());
//					output("Joining game: " + tid);
//					
//					//send message to server to join table
//					try {
//						serverConnection.joinTable(myName, tid);
//						currentTable = new checkersTable(tid, serverConnection);
//					} catch (RemoteException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}// end catch for server call
//				} catch (Exception e) {
//					//pop up box to tell user to select a table.
//					JOptionPane msg = new JOptionPane();
//					msg.showMessageDialog(null,
//							"Please select a table in the list!" + e.getStackTrace());
//=======
				} catch (RemoteException e) {

					output("Couldn't join table " + selectedTable);

				}
			}
		});
		btnObserveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				output(">> Observing table " + selectedTable);
				try {
					serverConnection.observeTable(myName, selectedTable);

				} catch (RemoteException e) {

					output("Couldn't observe table " + selectedTable);
				}
			}
		});

		// Set key listeners for the chat field
		chatInputField.addKeyListener(new KeyAdapter() {
			// This bit of magic auto-fills users when using the @user.
			// Press tab to quickly get to the end of the username.
			@Override
			public void keyReleased(KeyEvent arg0) {
				// Check to see if char[0] = '@'
				String input = chatInputField.getText();
				if (input.matches("@\\S+")) {
					int caretPos = chatInputField.getCaretPosition();
					chatInputField.setFocusTraversalKeysEnabled(false);
					// Choose user from lobbyUserList which matches entered
					// chars
					for (String user : lobbyUserList) {
						if (user.startsWith(input.substring(1, caretPos))) {
							// auto-fill user, while leaving the cursor at the
							// same spot
							chatInputField.setText("@" + user);
							chatInputField.setCaretPosition(caretPos);
							break;
						}
					}
				} else {
					chatInputField.setFocusTraversalKeysEnabled(true);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case 9: // tab
					String input = chatInputField.getText();
					chatInputField.setText(input + " ");
					chatInputField.setCaretPosition(input.length() + 1);
					chatInputField.setFocusTraversalKeysEnabled(true);
					break;
				case 10: // enter
				case 13:
					inputSubmit();
					break;
				}
			}
		});

		tableListPane.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int selected = tableListPane.getSelectedIndex();
				String table = tableList.getElementAt(selected).toString();
				selectedTable = Integer.parseInt(table.split("\\s")[0]);
				try {
					serverConnection.getTblStatus(myName, selectedTable);
				} catch (RemoteException ex) {
					output("Error getting status of table " + selectedTable);
				}
			}
		});
	}

	// Event for submitButton and ENTER key
	/*
	 * Called by the submit button ACTION. Takes the text in the chatInputField
	 * and take one of these steps: --If not connected, attempt to connect and
	 * all circumstances are caught --If connected, then send message to
	 * chatroom OR send a private message.
	 */
	private void inputSubmit() {
		try {
			System.out.println("Submit button was pressed.");

			// IF user is NOT CONNECTED
			if (curState.equals(State.notConnected)) {
				String input[] = chatInputField.getText().split("\\s");
				// 0 - ip address 1 - username
				// FAILED CONNECTION, lacking 2 inputs
				if (input.length < 2) {
					output("Connection failed. Did you remember to add a username?");
				}
				// FAILED CONNECTION, RMI issue
				else if (!serverConnection.connectToServer(input[0], input[1])) {
					output("Connection failed. Check console output of RMI process for information.");
				}
				// SUCCESSFUL CONNECTION, tell user welcome.
				else {
					myName = input[1];
					System.out.println("welcome!");
					this.btnCreateGame.setEnabled(true);
					this.btnJoinGame.setEnabled(true);
					this.btnObserveGame.setEnabled(true);

					chatArea.setText(">> Welcome, " + myName + "!\n");
				}
			}

			// IF THEY ARE CONNECTED, do chat functions
			else // if(curState.equals(State.inLobby)){
			{
				String input = chatInputField.getText();
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
		} catch (RemoteException e) {
			output("A remote exception occured: ");
			output(e.getMessage());
		} finally {
			chatInputField.setText("");
		}
	}

	// Updates the actual user list pane
	private void updateUserList() {
		String[] userList = new String[lobbyUserList.size()];
		lobbyUserList.toArray(userList);
		ListModel lstUsersModel = new DefaultComboBoxModel();
		userListPane.setModel(lstUsersModel);
	}

	// Helper method for outputting to the chat pane
	private void output(String s) {
		chatArea.append(s + "\r\n");
		chatArea.setCaretPosition(chatArea.getDocument().getLength());
	}

	// Forwards debug messages to output() if debugging is turned on
	private void debugOutput(String s) {
		if (debug)
			output(s);
	}

	// This function *might* give you the correct link-local IPv6 address.
	private String getLocalIPV6Address() {
		try {
			InetAddress[] inet = InetAddress.getAllByName(InetAddress
					.getLocalHost().getHostName());
			for (InetAddress addr : inet) {
				if (addr instanceof Inet6Address) {
					return ((Inet6Address) addr).getHostAddress();
				}
			}
		} catch (UnknownHostException e) {
			return "fail";
		}
		return "";

	}

	/**
	 * ***************************************
	 * *************************************** Methods satisfying the checkers
	 * client interface
	 ****************************************
	 ****************************************
	 */
	public void connectionOK() {
		debugOutput("Server says connection OK!");
		curState = State.connected;
	}

	public void nowJoinedLobby(String user) {
		if (!user.equals(this.myName)) {
			debugOutput(">> " + user + " has joined the lobby.");
		}
		lobbyUserList.add(user);
		updateUserList();
	}

	public void newMsg(String user, String msg, boolean pm) {
		if (pm) {
			output("[PM] " + user + ": " + msg);
		} else
			output(user + ": " + msg);
	}

	// alert that a user has left the lobby
	public void nowLeftLobby(String user) {
		//lobbyUserList.remove(user);
		updateUserList();
	}

	// updated listing of users in lobby
	public void usersInLobby(String[] users) {
		lobbyUserList.clear();
		for (String s : users)
			lobbyUserList.add(s);
		updateUserList();
	}

	// alert that you have joined the lobby
	public void youInLobby() {
		curState = State.inLobby;
		output(">> Welcome to the game lobby.");
		updateUserList();
	}

	// alert that you have left the lobby
	public void youLeftLobby() {
		curState = State.connected;
		output(">> You have left the game lobby.");
	}

	// initial listing of tables
	public void tableList(int[] tids) {
		for (int i = 0; i < tids.length; i++) {
			boolean tableExists = false;
			for (Object table : tableList.toArray()) {
				if (table.toString().startsWith(String.valueOf(tids[i]))){
					tableExists = true;
					break;
				}
			}
			if (!tableExists) {
				tableList.addElement(String.valueOf(tids[i]));
			}
		}
	}

	// an alert saying that a table state has changed.
	// this is received whenever anyone joins or leaves a table,
	// or if table state is queried by calling getTblStatus()
	public void onTable(int tid, String blackSeat, String redSeat) {
		output("Table " + tid + ": " +
				(blackSeat.equals("-1") ? "Empty" : blackSeat) + " " +
						(redSeat.equals("-1") ? "Empty" : redSeat));
		currentTable.setUserList(blackSeat, redSeat);
		// tableGame(
	}
		

	public void newTable(int t) {
		output("Creating table for " + myName);

		try {
			serverConnection.makeTable(myName);
			currentTable = new checkersTable(t, serverConnection);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			debugOutput("Can't make new table at newTable()");
		}


		int[] tids = {t};
		this.tableList(tids);
	}

	// alert that you have joined the table with id tid.
	public void joinedTable(int tid) {

		curState = State.onTable;
		currentTable = new checkersTable(tid, serverConnection);
		debugOutput(">> You have joined table " + Integer.toString(tid));
	}

	// alert that you have left your table.
	public void alertLeftTable() {
		curState = State.connected;
		debugOutput(">> You have left the table");
	}

	// alert that at the table you are sitting at, a game is starting.
	public void gameStart() {
		curState = State.inGame;
		if (isCheckers) {
			curBoardState = new byte[8][8];
			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					// if both row & column are even (or ordd)
					if ((x % 2 == 0 && y % 2 == 0)
							|| (x % 2 != 0 && y % 2 != 0))
						curBoardState[y][x] = 0;
					else if (y < 3) // top three rows
						curBoardState[y][x] = 1;
					else if (y > 4) // bottom three rows
						curBoardState[y][x] = 2;
				}
			}
		} else {
			curBoardState = new byte[19][19];
			for (int y = 0; y < 19; y++)
				for (int x = 0; x < 19; x++)
					curBoardState[y][x] = 0;
		}
	}

	// alert that your color is Black, for the game.
	public void colorBlack() {
		currentTable.setConsole("Your color is black");
		currentTable.setBlackPlayer(1);
	}

	// alert that your color is Red, for the game.
	public void colorRed() {
		currentTable.setConsole("Your color is red");
		currentTable.setRedPlayer(1);
	}

	// notice that your opponent has moved from position (fr,fc) to (tr,tc)
	public void oppMove(int fr, int fc, int tr, int tc) {
		currentTable.movePiece(fr, fc, tr, tc);
		debugOutput(">> oppMove(" + fr + "," + fc + "," + tr + "," + tc + ")");
	}

	// server has updated the board state
	public void curBoardState(int t, byte[][] boardState) {
		currentTable.updateTableFromServer(boardState);
	}

	// notice that for the game you are playing, you win!
	public void youWin() {

	}

	// notice that for the game you are playing, you lost.
	public void youLose() {
		debugOutput(">> youLose()");
	}

	// its your turn.
	public void yourTurn() {
		debugOutput(">> yourTurn()");
	}

	// you are now observing table tid.
	public void nowObserving(int tid) {
		debugOutput(">> nowObserving(" + tid + ")");
	}

	// you stopped observing table tid.
	public void stoppedObserving(int tid) {
		debugOutput(">> stoppedObserving(" + tid + ")");
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
		chatInputField.setText(yourip);
	}

	public void nameIllegal() throws RemoteException {
		output("The name requested is illegal. Length must be > 0 and have no whitespace.");
		output(conText);
		curState = State.notConnected;
		chatInputField.setText(yourip);
	}

	// the requested move is illegal.
	public void illegalMove() {
		output(">> That move is illegal!");
	}

	// the table your trying to join is full.
	public void tableFull() {
		output(">> The table you are trying to join is full. Please choose another one.");
	}

	// the table queried does not exist.
	public void tblNotExists() {
		debugOutput(">> tblNotExists()");
	}

	// called if you say you are ready on a table with no current game.
	public void gameNotCreatedYet() {
		output(">> Please wait for an opponent before starting the game.");
	}

	// called if it is not your turn but you make a move.
	public void notYourTurn() {
		output(">> It is not your turn!");
	}

	// called if you send a stop observing command but you are not observing a
	// table.
	public void notObserving() {
		debugOutput(">> notObserving()");
	}

	// called if you send a game command but your opponent is not ready
	public void oppNotReady() {
		output(">> Please wait for your opponent to start the game.");
	}

	// you cannot perform the requested operation because you are in the lobby.
	public void errorInLobby() {
		output(">> You cannot perform that action from within the lobby.");
	}

	// called if the client sends an ill-formated TCP message
	public void badMessage() {
		debugOutput(">> badMessage()");
	}

	// called when your opponent leaves the table
	public void oppLeftTable() {
		debugOutput(">> oppLeftTable()");
	}

	// you cannot perform the requested op because you are not in the lobby.
	public void notInLobby() {
		output(">> You cannot perform that action from outside of the lobby.");
	}
}
