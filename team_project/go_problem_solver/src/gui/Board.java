package gui;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;

import javax.swing.*;

import board_utils.GoPlayingBoard;

public class Board extends JPanel implements MouseListener,
								  			 ItemListener {
	
	private boolean colour;
	private boolean drawLegalMoves;
	private Model model;
	
	
	public class DrawStone {
		
		public boolean draw;
		public int x;
		public int y;
		public Color color;
		
		public DrawStone(){
			draw = false;		
		}

	}
	
	final static int MARGIN = 50;
	final static int BOARDSIZE = 19;
	private Intersection[][] intersections = new Intersection[BOARDSIZE][BOARDSIZE];
	private int sqWidth;
	private DrawStone drawStone = new DrawStone();
	
	
	public Board(){;
		addMouseListener(this);
		model = new Model();
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if(!drawStone.draw) {
			
			//If not drawing a stone.
			Dimension d = getSize();
			int min = Math.min(d.height, d.width);
			sqWidth = (min - MARGIN) / BOARDSIZE;
			//Initialising points for horizontal lines
			Point from = new Point(MARGIN, 0);
			Point to = new Point(sqWidth * (BOARDSIZE - 1) + MARGIN, 0);
			GeneralPath squares = new GeneralPath();
			
			//Horizontal lines
			for(int i = MARGIN; i < sqWidth * BOARDSIZE + MARGIN; i += sqWidth) {
				from.y = i;
				to.y = i;
				squares.moveTo(from.x, from.y);
				squares.lineTo(to.x, to.y);
				
				//This is drawing coordinate letters at the top and bottom of the board. 
				//Works with some glitches so commented for now.
				
				//String index = String.valueOf((i - MARGIN)/sqWidth + 1);
				//g2.drawString(index, 5, from.y + sqWidth/4);
				//g2.drawString(index, to.x + sqWidth, to.y + sqWidth/4);
			}
			
			from.setLocation(0, MARGIN);
			to.setLocation(0, sqWidth * (BOARDSIZE - 1) + MARGIN);
			//char index = 'A';
			//Vertical lines
			for(int i = MARGIN; i < sqWidth * BOARDSIZE + MARGIN; i += sqWidth) {
				from.x = i;
				to.x = i;
				squares.moveTo(from.x, from.y);
				squares.lineTo(to.x, to.y);	
				
				//This is drawing coordinate numbers at the left and right of the board. 
				//Works with some glitches so commented for now.
				
				//g2.drawString(String.valueOf(index), from.x - sqWidth/4, from.y - sqWidth);
				//g2.drawString(String.valueOf(index), to.x - sqWidth/4, to.y + sqWidth * 5/4);
				//index++;
				//if(index == 'I') index++;
			}
			
			for (int i = 0; i < BOARDSIZE; i++)
				for(int j = 0; j < BOARDSIZE; j++) {
					intersections[i][j] = new Intersection(
							new Point(i * sqWidth + MARGIN, j * sqWidth + MARGIN), sqWidth);
				}
			
			g2.draw(squares);
		} else {
			//if drawing stone
			g2.setPaint(drawStone.color);
			g2.fillOval(drawStone.x, drawStone.y, sqWidth, sqWidth);
			drawStone.draw = false;
		}
		
		if (drawLegalMoves){
			System.out.println("Draw Legal Move.");
			for (int x = 0; x < 19; x++)
				for (int y = 0; y < 19; y++) {
					if(model.isMoveLegal(x, y, Color.BLACK)) {
						g2.setPaint(Color.GREEN);
						
						g2.fillOval(intersections[x][y].getTopLeftX(), intersections[x][y].getTopLeftY(), sqWidth, sqWidth);
					
					} else {
						g2.setPaint(Color.RED);
						g2.fillOval(intersections[x][y].getTopLeftX(), intersections[x][y].getTopLeftY(), sqWidth, sqWidth);
					}
					
				}
					
		}	
	}
	
	public void drawStone(int xIndex, int yIndex, Color c) {
		if(model.isMoveLegal(xIndex, yIndex, c)) {
			model.addStone(xIndex, yIndex, c);
			drawStone.color = c;
			drawStone.x = intersections[xIndex][yIndex].getTopLeftX();
			drawStone.y = intersections[xIndex][yIndex].getTopLeftY();
			drawStone.draw = true;
			drawLegalMoves = false;
			repaint();
		}
		else
			System.out.println("Bad Move!!!!you suck");
	}
	
	public void drawLegalMoves() {
		System.out.println("Draw Legal Move. function1");
		drawLegalMoves = true;
		repaint();
	}
	
	public void undrawLegalMoves(){
		drawLegalMoves = false;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for(int i = 0; i < BOARDSIZE; i++)	
			for(int j = 0; j < BOARDSIZE; j++) {
				if(intersections[i][j].contains(e.getPoint())) {
					if (this.colour)
					{
						drawStone(i, j, Color.WHITE); //testing
						this.colour = false;
						return;
					}
					else 
					{
						drawStone(i, j, Color.BLACK); //testing
						this.colour = true;
						return;
					}
				}
			}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0){}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}



	@Override
	public void itemStateChanged(ItemEvent e) {
		System.out.println("Draw Legal Move. Listener");
		if(e.getStateChange() == ItemEvent.SELECTED)
			drawLegalMoves();
		else
			undrawLegalMoves();
		
	}
	
	
}
