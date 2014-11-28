package Client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class checkersTable extends JFrame {

	private JPanel contentPane;
	private JLabel table;

	/**
	 * Launch the application.
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		table = new javax.swing.JLabel();

		table.setHorizontalAlignment(SwingConstants.TRAILING);
		table.setBounds(20, 11, 408, 404);

		table.setIcon(new ImageIcon(checkersTable.class
				.getResource("/Client/table.gif")));
		contentPane.add(table);
		setTable();

		System.out.println("add piece");

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
				if (i % 2 == 0 && j % 2 == 0)
					setTablePiece(i, j, red);

				else if (i % 2 == 1 && j % 2 == 1)
					setTablePiece(i, j, red);
			}// end row
		}
	}

	//called by setTable()
	//Creates a new piece by taking in the row, col, and if the piece is red/black.
	public void setTablePiece(int i, int j, boolean red) {
		piece p = new piece(red, table);
		p.setPiece(i, j);
		table.add(p);//add the the GUI
	}

}
