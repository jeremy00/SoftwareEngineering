package Client;

import javax.swing.JLabel;

//set 
public class piece extends JLabel{
	JLabel pieceLabel;
	int x;
	int y;
	int yDiff =53;//difference in spots on the board
	int xDiff =50;//difference in spots on the board
	int tableX, tableY;
	
	String pathIcon;
	piece(boolean color, JLabel table){
		tableX = table.getX();
		tableY = table.getX();
			}
	
	//set the position of the checkers piece using 
	//@param boardX and boardY are the coordinates on the board
public void setPiece(int boardX, int boardY){
	this.y = tableX + (xDiff*x);//get the tables init coords
								//add the diff(between spots) * that spot on the board (0-8)
	this.x = tableY + (yDiff*y);
	
	this.setLocation(x, y);
}
	public void setX(int x, int y){
		
	}
	
	
}
