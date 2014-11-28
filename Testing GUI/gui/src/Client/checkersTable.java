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
import java.awt.FlowLayout;
import javax.swing.ImageIcon;

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
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(14, 75, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(105, 73, 89, 23);
		contentPane.add(btnNewButton);
		
		
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(199, 74, 4, 22);
		contentPane.add(textArea);

	    JList list = new JList();
	    list.setBounds(208, 85, 0, 0);
		contentPane.add(list);
		
		     JLabel    red = new javax.swing.JLabel();
			red.setBounds(249, 130, 42, 38);
			 
			 
			     red.setIcon(new ImageIcon("C:\\Users\\Meri\\Desktop\\red.png"));
			     contentPane.add(red);
			
		JButton btnLeave = new JButton("Leave");
		btnLeave.setBounds(366, 73, 61, 23);
		contentPane.add(btnLeave);
		
		JButton button = new JButton("Surrender");
		button.setBounds(432, 73, 81, 23);
		contentPane.add(button);
		
		JLabel lblTable = new JLabel("table #");
		lblTable.setBounds(518, 78, 35, 14);
		contentPane.add(lblTable);
		
		JLabel lblRedsTurn = new JLabel("Red's Turn");
		lblRedsTurn.setBounds(558, 78, 51, 14);
		contentPane.add(lblRedsTurn);
		
		
		
		 JLabel    jLabel1 = new javax.swing.JLabel();
		 jLabel1.setBounds(249, 130, 408, 404);
		 
		 
		     jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\Meri\\Desktop\\ds32352.gif"));
		     contentPane.add(jLabel1);
		     
		     JLabel label = new JLabel();
		     label.setBounds(310, 493, 42, 38);
		     contentPane.add(label);
	}
//private JLabel returnRed(){
//	
//}
}
