package Client;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

//set 
public class piece extends JLabel{
	JLabel pieceLabel;
	int x;
	int y;
	int yDiff =53;//difference in spots on the board
	int xDiff =50;//difference in spots on the board
	int tableX, tableY;
	JLabel table;
	
	String pathIcon;
	piece(boolean red, JLabel table){
		this.table = table;
		tableX = table.getX();//retrieve the table coords
		tableY = table.getX();
		x = 0;
		y = 0;
		//set the image icon of the JLabel
		if (red)this.setIcon(new ImageIcon("C:\\Users\\Meri\\Desktop\\red.png"));
		System.out.println("piece x: " + x  +" piece y: " + y)	;
		this.setBounds(x, y, 50, 50);//set the position 
//		  table.setHorizontalAlignment(SwingConstants.);
	}
	
	//set the position of the checkers piece using 
	//@param boardX and boardY are the coordinates on the board
public void setPiece(int boardX, int boardY){
	this.y = tableX + (xDiff*x);//get the tables init coords
								//add the diff(between spots) * that spot on the board (0-8)
	this.x = tableY + (yDiff*y);
	
	this.setLocation(x, y);//set the location of the JLabel on the table
	System.out.format("x: %d y: %d", x,y);
}
	
}
