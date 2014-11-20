package Client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;

import java.awt.GridLayout;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
//import javax.swing.text.html.AccessibleHTML.TableElementInfo.TableAccessibleContext;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.ListSelectionModel;

public class checkersTable extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTable table;

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
		setBounds(100, 100, 640, 439);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 354, 485, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(508, 353, 89, 23);
		contentPane.add(btnNewButton);
		
		
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 266, 587, 78);
		contentPane.add(textArea);
		
		
		
		
		JList list = new JList();
		list.setBounds(512, 65, 126, 147);
		contentPane.add(list);
		
		table = new JTable(){
			public Component prepareRenderer(TableCellRenderer r, int rw, int col)
			{
				Component c = super.prepareRenderer(r, rw,  col);
				c.setBackground(Color.WHITE);
				if (col==0){
					c.setBackground(Color.GREEN);
				}
				return c;
			}
		};
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		
		
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
			},
			new String[] {
				"New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column"
			}
		));
		
		
		
		table.setBounds(107, 84, 388, 128);
		contentPane.add(table);
		
		JButton btnLeave = new JButton("Leave");
		btnLeave.setBounds(0, 159, 89, 23);
		contentPane.add(btnLeave);
		
		JButton button = new JButton("Surrender");		
		button.setBounds(0, 125, 89, 23);
		contentPane.add(button);
		
		JLabel lblTable = new JLabel("table #");
		lblTable.setBounds(113, 11, 46, 14);
		contentPane.add(lblTable);
		
		JLabel lblRedsTurn = new JLabel("Red's Turn");
		lblRedsTurn.setBounds(113, 46, 46, 14);
		contentPane.add(lblRedsTurn);
	}
}
