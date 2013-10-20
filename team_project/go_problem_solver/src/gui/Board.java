package gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class Board extends JPanel implements MouseListener {
	
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
		}
	}
	
	public void drawStone(int xIndex, int yIndex, Color c) {
		drawStone.color = c;
		drawStone.x = intersections[xIndex][yIndex].getTopLeftX();
		drawStone.y = intersections[xIndex][yIndex].getTopLeftY();
		drawStone.draw = true;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for(int i = 0; i < BOARDSIZE; i++)	
			for(int j = 0; j < BOARDSIZE; j++) {
				if(intersections[i][j].contains(e.getPoint())) {
					drawStone(i, j, Color.BLACK); //testing
					return;
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
}
