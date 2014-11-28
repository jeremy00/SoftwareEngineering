package Client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.Font;


//TOP LEFT IS 0,0
public class checkersTable extends JFrame {

	private JPanel contentPane;
	private JTextField ChatInputField;
	private JList UserList;
	private JButton btnStart,btnLeave, btnSubmit;
	private JLabel lblUserList, lblTableNum,lblConsole, table;
	private piece[][] piecesArray;
	private JTextArea ChatArea;

	/**
	 * Launch the applicat	ion.
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
public void setGUI(){
	//Setup the JPANEL
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 800, 730);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(contentPane);
	contentPane.setLayout(null);

	//Set all of the options, then add it to the panel
	//Table
	table = new javax.swing.JLabel();
	table.setHorizontalAlignment(SwingConstants.TRAILING);
	table.setBounds(187, 83, 408, 404);
	table.setIcon(new ImageIcon(checkersTable.class
			.getResource("/Client/table.gif")));
	
	//Chat Input
	ChatInputField = new JTextField("");
	ChatInputField.setColumns(10);
	ChatInputField.setBounds(10, 652, 585, 20);
	
	//Submit Button
	btnSubmit = new JButton("Submit");
	btnSubmit.setFont(new Font("Times New Roman", Font.BOLD, 15));
	btnSubmit.setBounds(619, 651, 155, 35);
	
	//Chat Area
	ChatArea = new JTextArea("");
	ChatArea.setForeground(Color.BLACK);
	ChatArea.setBounds(10, 498, 764, 143);
	
	
	//User List
	UserList = new JList();
	UserList.setBounds(605, 122, 169, 319);
	
	//Start Button
	btnStart = new JButton("Start");
	btnStart.setFont(new Font("Times New Roman", Font.BOLD, 20));
	btnStart.setBounds(10, 142, 124, 68);
	//Leave Button
	btnLeave = new JButton("Leave");
	btnLeave.setFont(new Font("Times New Roman", Font.BOLD, 20));
	btnLeave.setBounds(10, 232, 124, 68);
	//User List
	lblUserList = new JLabel("User List");
	lblUserList.setFont(new Font("Times New Roman", Font.BOLD, 19));
	lblUserList.setBounds(652, 70, 150, 53);
	//Table Number
	lblTableNum = new JLabel("Table #:");
	lblTableNum.setFont(new Font("Times New Roman", Font.PLAIN, 18));
	lblTableNum.setBounds(230, 11, 322, 14);
	//Console to let user know who's turn and what the status of the game is.
	lblConsole = new JLabel("Start Game");
	lblConsole.setFont(new Font("Times New Roman", Font.PLAIN, 18));
	lblConsole.setBounds(230, 45, 322, 14);
    
	
	//Now add everything to the table
	contentPane.add(table);		
	contentPane.add(ChatInputField);
	contentPane.add(btnSubmit);
	contentPane.add(ChatArea);
	contentPane.add(UserList);
	contentPane.add(btnStart);
	contentPane.add(btnLeave);
	contentPane.add(lblUserList);
	contentPane.add(lblTableNum);
	contentPane.add(lblConsole);
	

	
}
	// Set the table up for the first run.
	//Runs from the top left to the bottom right.
	public void setTable() {
		boolean red = true;// if the piece is red, then it's true

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				
				//Set first three rows, stop, then set last three rows
				if (i < 3)
					red = true;
				else if (i > 4)
					red = false;
				else
					break;

				//If it's a checkers position that needs a piece.
				if (i % 2 == 0 && j % 2 == 0){
					
					setTablePiece(i, j, red);
				}
				else if (i % 2 == 1 && j % 2 == 1){
					
					setTablePiece(i, j, red);
			}
			}// end row
		}
	}

	//called by setTable()
	//Creates a new piece by taking in the row, col, and if the piece is red/black.
	public void setTablePiece(int i, int j, boolean red) {
		piece p = new piece(red, table);
		p.setPiece(i, j);
		table.add(p);//add the the GUI
		piecesArray[i][j] = p;
	}
	
	//Called when moving from spot to spot. FROM TOP LEFT is 0,0
	public void movePiece(int fromRow, int fromCol, int toRow, int toCol){
		
		//set the piece when it's been moved..
		piecesArray[toRow-1][toCol-1].setPiece(toRow, toCol);
	}
}
