package Client;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ListSelectionModel;

//TOP LEFT IS 0,0
public class checkersTable extends JFrame {
	
	//Scroll Panes
	private final JScrollPane scrollPane = new JScrollPane();
	private final JScrollPane scrollPane_1 = new JScrollPane();
	private final JScrollPane scrollPane_2 = new JScrollPane();
	
	private ArrayList<String> lobbyUserList; // string lists of users for output
	private String tableName = "";
	private JPanel contentPane;
	private JTextField ChatInputField;
	private JButton btnStart, btnLeave, btnSubmit;
	private JLabel lblUserList, lblTableNum, lblConsole, table;
	private piece[][] piecesArray;
	private JTextArea ChatArea;
	private piece selectedPiece = new piece(0, table);
	private int redPlayer;
	private int blackPlayer;
	public JList userList ; 
	public boolean pieceIsSelected = false;
	public int moveToX;
	public int moveToY;

	checkersTable(int tid){
		lobbyUserList = new ArrayList<String>();
		tableName = Integer.toString(tid);
		setConsole("Table #: "+ tableName);
		piecesArray = new piece[8][8];
		setGUI();
		setTable();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					checkersTable frame = new checkersTable();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	public int getBlackPlayer() {
		return blackPlayer;
	}
	public void setBlackPlayer(int blackPlayer) {
		this.blackPlayer = blackPlayer;
	}
	public int getRedPlayer() {
		return redPlayer;
	}
	public void setRedPlayer(int redPlayer) {
		this.redPlayer = redPlayer;
	}
	/**
	 * Launch the applicat ion.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					checkersTable frame = new checkersTable();
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
	public checkersTable() {
		piecesArray = new piece[8][8];
		setGUI();
		setTable();

	}

	public void setGUI() {
		// Setup the JPANEL
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 730);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Set all of the options, then add it to the panel
		// Table
		table = new javax.swing.JLabel();
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent mouseClick) {
				try {
					System.out.println("mouse click x: " + mouseClick.getX()
							+ "  mouse click y: " + mouseClick.getY());
					System.out.println(findPiece(mouseClick.getX(),
							mouseClick.getY()).color);
					// find the piece that the user clicked
					piece newPiece = findPiece(mouseClick.getX(),
							mouseClick.getY());

					// /If the piece is a valid piece
					if (newPiece.color != 0 || selectedPiece == null) {
						// Set the current selected piece back to normal
						selectedPiece.setUnSelected();
						selectedPiece = newPiece; // make new piece the selected
						selectedPiece.setSelected();// set the new piece as
													// selected
						pieceIsSelected = true;
					}// end if
					
					//otherwise the person is trying to select a piece.
					else if (selectedPiece != null && newPiece.color == 0)
					{
						if(pieceIsSelected){
							int x = posX / 50;
							int y = posY / 50;
						}
					}

					
				} catch (Exception e) {
					System.out.print("Error at mouse click event"
							+ e.toString());

				}
			}// end mouse click method
		});

		table.setHorizontalAlignment(SwingConstants.TRAILING);
		table.setBounds(187, 83, 408, 404);
		table.setIcon(new ImageIcon(checkersTable.class
				.getResource("/Client/table.gif")));

		// Chat Input
		ChatInputField = new JTextField("");
		ChatInputField.setColumns(10);
		ChatInputField.setBounds(10, 652, 585, 20);

		// Submit Button
		btnSubmit = new JButton("Submit");
		btnSubmit.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnSubmit.setBounds(619, 651, 155, 35);

		// Chat Area
		ChatArea = new JTextArea("");
		ChatArea.setForeground(Color.BLACK);
		ChatArea.setBounds(10, 498, 764, 143);

		// Start Button
		btnStart = new JButton("Start");
		btnStart.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnStart.setBounds(10, 142, 124, 68);
		// Leave Button
		btnLeave = new JButton("Leave");
		btnLeave.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnLeave.setBounds(10, 232, 124, 68);
		// User List
		lblUserList = new JLabel("User List");
		lblUserList.setFont(new Font("Times New Roman", Font.BOLD, 19));
		lblUserList.setBounds(624, 71, 150, 53);
		// Table Number
		lblTableNum = new JLabel("Table #:");
		lblTableNum.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblTableNum.setBounds(230, 11, 322, 14);
		// Console to let user know who's turn and what the status of the game
		// is.
		lblConsole = new JLabel("Start Game");
		lblConsole.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblConsole.setBounds(230, 45, 322, 14);
 
		userList = new JList();
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setBounds(604, 130, 170, 220);
		scrollPane_2.setViewportView(userList);
		
		// Now add everything to the table
		contentPane.add(table);
		contentPane.add(ChatInputField);
		contentPane.add(btnSubmit);
		contentPane.add(ChatArea);
		contentPane.add(btnStart);
		contentPane.add(btnLeave);
		contentPane.add(lblUserList);
		contentPane.add(lblTableNum);
		contentPane.add(lblConsole);
		contentPane.add(userList);
		
		//set table name
	lblTableNum.setText("Table Num: #"+ tableName);
	
	
	}

	
	//Set up the user List, called with WHO_ON_TBL 219, and onTable() in checkersLobby
	public void setUserList(String black, String red){
		lobbyUserList.add(black + " (black)");
		lobbyUserList.add(red+ " (red)");
		
		String[] userList = new String[lobbyUserList.size()];
		lobbyUserList.toArray(userList);
		ListModel lstUsersModel = new DefaultComboBoxModel(userList);
		this.userList.setModel(lstUsersModel);
	
		
	}
	
	//Set the console
	public void setConsole(String text){
		lblConsole.setText(text);
	}
	//setup the chat
	public void setChat(){
		
		
	}
	
	
/*checks the new byte table for changes and applies a movement
	*/
	public void updateTableFromServer(byte[][] table){
		
		int moveFromX=-1, moveFromY=-1, moveToX=-1, moveToY=-1;
		//search for two changes.. one will be "empty" and one will be "clear"
		//keep those changes and apply the move
		for(int i=0; i < 8; i++){//go through the OLD (piecesArray) and compare to NEW (byte[] table)
			for(int j=0; j < 8; j++){
				if(piecesArray[i][j].color != table[i][j] )//there is a match, therefore a movement
						//if the old spot has a spot and the new spot is empty, a piece moved
						//MAKING IT THE PIECE TO MOVE FROM!
					if (piecesArray[i][j].color != 0 && table[i][j] == 0)
					{
						moveFromX = i; moveFromY= j;
					}
				//If the old spot is empty, and is no longer empty, a piece will be in the new spot
				//MAKING IT THE MOVE TO SPOT
				if (piecesArray[i][j].color == 0 && table[i][j] != 0)
				{
					moveToX = i; moveToY= j;
				}
					
			}
		}
		//Done checking the arrays for movement
		//if there is a successful find of a movement, then apply movement
		if(moveFromX != -1 && moveFromY != -1 && moveToX != -1 && moveToY != -1)
		{
			movePiece(moveFromX, moveFromY, moveToX, moveToY);

		}
		
	}
	// Set the table up for the first run.
	// Runs from the top left to the bottom right.
	public void setTable() {
		int red = 1;// if the piece is red, then it's true

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				// Set first three rows, stop, then set last three rows
				if (i < 3)
					red = 1;
				else if (i > 4)
					red = 2;
				else
					red = 0;

				// If it's a checkers position that needs a piece.
				if (i % 2 == 0 && j % 2 == 0) {

					setTablePiece(i, j, red);
				} else if (i % 2 == 1 && j % 2 == 1) {

					setTablePiece(i, j, red);
				} else
					setTablePiece(i, j, 0);
			}// end row
		}
	}

	// called by setTable()
	// Creates a new piece by taking in the row, col, and if the piece is
	// red/black.
	public void setTablePiece(int i, int j, int red) {
		piece p = new piece(red, table);
		p.setPiece(i, j);

		table.add(p);// add the the GUI
		piecesArray[i][j] = p;
	}

	// Called when moving from spot to spot. FROM TOP LEFT is 0,0
	public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {

		// set the piece when it's been moved..
		piecesArray[toRow - 1][toCol - 1].setPiece(toRow, toCol);
	}

	public piece findPiece(int posX, int posY) {

		

		// each step is 50px apart.
		// so divide them by 50 and throw the response to the array to be
		// changed.
		int x = posX / 50;
		int y = posY / 50;

		return piecesArray[y][x];
		// System.out.println("Position x: " + x +" Position y: "+ y +
		// "color: "+ piecesArray[y][x].color + "spot: " +piecesArray[x][y].x +
		// " " +piecesArray[x][y].x);

	}
}

