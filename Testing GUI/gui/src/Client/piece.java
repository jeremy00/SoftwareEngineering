package Client;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

//Called by table to add a piece to the table.
public class piece extends JLabel{

	int x;
	int y;
	String color;
	final int X_SIZE_IMAGE = 40;
	final int Y_SIZE_IMAGE = 40;
	int yDiff =53;//difference in spots on the board
	int xDiff =50;//difference in spots on the board
	//int tableX, tableY;
	JLabel table;//the parent table
	
	piece(int red, JLabel table){
try{
	this.table = table;
		x = 0;
		y = 0;
		//set the image icon of the JLabel, dependent on constructor param red
		if (red==1){
			color = "red";
			this.setIcon(new ImageIcon(piece.class.getResource("/Client/red.png")));  }  
		else if (red == 2)
		{
			color="black";
			this.setIcon(new ImageIcon(piece.class.getResource("/Client/black.png")));}
		else color = "empty";
		this.setBounds(x, y, X_SIZE_IMAGE, Y_SIZE_IMAGE);//set the position 
//		  table.setHorizontalAlignment(SwingConstants.);
	
}
catch(Exception e){
	System.out.println("Error in the piece contstructor, probably image issue " + e);
}
	}
	
	//set the position of the checkers piece using 
	//@param boardX and boardY are the coordinates on the board
public void setPiece(int boardX, int boardY){
	this.y =  (xDiff*boardX);//get the tables init coords
								//add the diff(between spots) * that spot on the board (0-8)
	this.x = (yDiff*boardY);
	
	this.setLocation(x, y);//set the location of the JLabel on the table
	//System.out.format("x: %d y: %d", x,y);
}

public void setSelected(){
	if (color=="black")
	setIcon(new ImageIcon(piece.class.getResource("/Client/black_selected.png")));
	else     if (color=="red")
    	setIcon(new ImageIcon(piece.class.getResource("/Client/red_selected.png")));
	
}

public void setUnSelected(){
	if (color=="black")
	setIcon(new ImageIcon(piece.class.getResource("/Client/black.png")));
	else if (color=="red")
    	setIcon(new ImageIcon(piece.class.getResource("/Client/red.png")));
	
}
}
