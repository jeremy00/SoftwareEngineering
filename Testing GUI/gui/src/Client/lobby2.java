package Client;



import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Button;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListModel;

import RMIConnection.Interfaces.CheckersClient;
import RMIConnection.Interfaces.RMIServerInterface;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class lobby2 extends JFrame implements CheckersClient {

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
	private JPanel submitPanel;
	private JTextArea chatArea;
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
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					lobby2 frame = new lobby2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public lobby2() {
		setDefaultCloseOp         eration(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 523, 477);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Button submitButton = new Button("New button");
		submitButton.setBounds(435, 411, 70, 22);
		contentPane.add(submitButton);
		
		JList chatArea = new JList();
		chatArea.setForeground(Color.WHITE);
		chatArea.setBounds(10, 157, 495, 239);
		contentPane.add(chatArea);
		
		JList userListPane = new JList();
		userListPane.setBounds(379, 11, 126, 122);
		contentPane.add(userListPane);
		
		JTextArea tableList = new JTextArea();
		tableList.setBounds(10, 15, 359, 131);
		contentPane.add(tableList);
		
		chatInputField = new JTextField();
		chatInputField.setBounds(10, 411, 419, 20);
		contentPane.add(chatInputField);
		chatInputField.setColumns(10);
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
	 * Methods satisfying the checkers client interface
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
	

    /** 
     * Error messages
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
