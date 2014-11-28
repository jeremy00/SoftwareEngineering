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
		          
		          
		          
		           //JLabel    table = new javax.swing.JLabel();
		           
		           table.setHorizontalAlignment(SwingConstants.TRAILING);
		           table.setBounds(20, 11, 408, 404);
		           
		           
		               table.setIcon(new javax.swing.ImageIcon("C:\\Users\\Meri\\Desktop\\ds32352.gif"));
		               contentPane.add(table);
		             
		            
		             
		     System.out.println("add piece");
		     
	}
	
	public void setTable(){
		  piece p = new piece(true, table);
          
          table.add(p);
	}
	
}
