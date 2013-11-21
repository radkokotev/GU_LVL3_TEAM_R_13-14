package gui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.*;

import custom_java_utils.CheckFailException;
import board_utils.GoPlayingBoard;
import board_utils.Stone;

public class Board extends JPanel implements MouseListener,
								  			 ItemListener,
								  			 ActionListener{
	
	private boolean colour;
	private boolean drawLegalMoves;
	private boolean drawStones;
	private boolean eraseLegalMoves;
	private Model model;
	private JLabel totalStonesLabel;
	private JCheckBoxMenuItem showValidMoves;
	private JMenuItem importFile;
	
	
	public class DrawStone {
		
		public boolean draw;
		public int x;
		public int y;
		public Color color;
	}
	
	final static int MARGIN = 50;
	final static int BOARDSIZE = 19;
	private Intersection[][] intersections = new Intersection[BOARDSIZE][BOARDSIZE];
	private int sqWidth;
	private DrawStone drawStone = new DrawStone();
	
	
	public Board(JFrame frame){
	    JMenuBar menuBar = new JMenuBar();
	    showValidMoves = new JCheckBoxMenuItem("Show valid/invalid moves");
	    importFile = new JMenuItem("Import File");
	    menuBar.add(showValidMoves);
	    menuBar.add(importFile);
	    frame.setJMenuBar(menuBar); 
	    showValidMoves.addItemListener(this);
        importFile.addActionListener(this);
		addMouseListener(this);
		totalStonesLabel = new JLabel("Total stones: 0 Black stones: 0 White stones: 0");
		frame.getContentPane().add(totalStonesLabel, BorderLayout.NORTH);
		model = new Model();
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		//If not drawing a stone.
		Dimension d = getSize();
		//Clearing everything
		g.clearRect(0, 0, d.width, d.height);
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
				intersections[j][i] = new Intersection(
						new Point(i * sqWidth + MARGIN, j * sqWidth + MARGIN), sqWidth);
			}
		
		g2.draw(squares);
		
		//draw all stones
		Stone[][] stones = model.getCurrentBoardLayout();
		for(int i = 0; i < 19; i++)
			for(int j = 0; j < 19; j++){
				if(stones[i][j] == Stone.BLACK) {
					g2.setPaint(Color.BLACK);
					g2.fillOval(intersections[i][j].getTopLeftX(), intersections[i][j].getTopLeftY(), sqWidth, sqWidth);
				} else if(stones[i][j] == Stone.WHITE){
					g2.setPaint(Color.WHITE);
					g2.fillOval(intersections[i][j].getTopLeftX(), intersections[i][j].getTopLeftY(), sqWidth, sqWidth);
				} else if(stones[i][j] == Stone.INNER_BORDER){
					g2.setPaint(Color.LIGHT_GRAY);
					g2.fillRect(intersections[i][j].getTopLeftX(), intersections[i][j].getTopLeftY(), sqWidth, sqWidth);
				}			
			}
		
		if (drawLegalMoves){
			for (int x = 0; x < 19; x++)
				for (int y = 0; y < 19; y++) {
					if(model.isMoveLegal(x, y)) {
						//Green with transparency
						g2.setPaint(new Color(0, 255, 0, 50));
					} else {	
						//Red with less transparency
						g2.setPaint(new Color(255, 0, 0, 150));
					}
					Ellipse2D oval = new Ellipse2D.Float(intersections[x][y].getTopLeftX(), intersections[x][y].getTopLeftY(), sqWidth, sqWidth);
					g2.fill(oval);
				}	
					
		}

		/*
		if(drawStone.draw){
			//if drawing stone
			g2.setPaint(drawStone.color);
			g2.fillOval(drawStone.x, drawStone.y, sqWidth, sqWidth);
			drawStone.draw = false;
		} */				
	}
	
	public void drawStone(int xIndex, int yIndex, Color c) {
		if(model.isMoveLegal(xIndex, yIndex)) {
			model.addStone(xIndex, yIndex, c);
			/*
			drawStone.color = c;
			drawStone.x = intersections[xIndex][yIndex].getTopLeftX();
			drawStone.y = intersections[xIndex][yIndex].getTopLeftY();
			*/
			this.colour = !this.colour;
			repaint();
			model.removeOpponent(xIndex, yIndex);
			totalStonesLabel.setText("Total stones: " + model.getTotalNumberOfStones() + " Black stones: " + model.getBlackNumberOfStones() + " White stones: "
					+ model.getWhiteNumberOfStones());
		}
		else
			System.out.println("Bad Move!!!!");
	}
	
	public void drawLegalMoves() {
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
						drawStone(i, j, Color.WHITE); 
						return;
					}
					else 
					{
						drawStone(i, j, Color.BLACK);
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
		if(e.getStateChange() == ItemEvent.SELECTED)
			drawLegalMoves();
		else
			undrawLegalMoves();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);
		
		try {
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            model = new Model(file.getAbsolutePath());
	            model.recountBlackStones();
				drawStones = true;
				repaint();
	        }
		} catch (FileNotFoundException e1) {
			System.out.println("File not found.");	
		} catch (CheckFailException e1) {
			System.out.println("Check fail exception.");
		}		
	}
	
	
	
}
