package Client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import java.awt.Color;

import javax.swing.ImageIcon;
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

import java.awt.Font;
import java.awt.Panel;

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
		list.setBounds(509, 97, 126, 147);
		contentPane.add(list);
		
		JButton btnLeave = new JButton("Leave");
		btnLeave.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLeave.setBounds(0, 159, 113, 34);
		contentPane.add(btnLeave);
		
		JButton button = new JButton("Surrender");		
		button.setFont(new Font("Tahoma", Font.BOLD, 14));
		button.setBounds(0, 102, 113, 46);
		contentPane.add(button);
		
		JLabel lblTable = new JLabel("Table #: 5532");
		lblTable.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblTable.setBounds(113, 11, 262, 24);
		contentPane.add(lblTable);
		
		JLabel lblRedsTurn = new JLabel("Red's Turn");
		lblRedsTurn.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblRedsTurn.setBounds(113, 32, 262, 24);
		contentPane.add(lblRedsTurn);
		
		table = new JTable();
		table.setBounds(140, 66, 355, 178);
		
		contentPane.add(table);
		
		
	}
}
