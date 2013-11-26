package gui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.io.File;

import javax.swing.*;

public class GuiBoard extends JPanel implements ActionListener {
	public JMenuItem importFileItem, exportFileItem;
	public JMenuBar menuBar;
	public JMenuItem modeMenuItem;
	public JFrame frame;
	
	final static int MARGIN = 50;
	final static int BOARDSIZE = 19;
	final static File DEFAULT_DIRECTORY = new File(System.getProperty("user.dir") + "/src/player_utils/test_data/");
	public Intersection[][] intersections = new Intersection[BOARDSIZE][BOARDSIZE];
	public int sqWidth;	
	
	public GuiBoard(JFrame frame){
		this.frame = frame;
	    menuBar = new JMenuBar();
	    
	    JMenu fileMenu = new JMenu("File");
	    importFileItem = new JMenuItem("Import file");
	    exportFileItem = new JMenuItem("Export file");
	    fileMenu.add(importFileItem);
	    fileMenu.add(exportFileItem);
	    menuBar.add(fileMenu);
	    
	    modeMenuItem = new JMenuItem("Switch mode");
	    modeMenuItem.addActionListener(this);
	    menuBar.add(modeMenuItem);
	    
	    frame.setJMenuBar(menuBar); 
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

	}

	@Override
	public void actionPerformed(ActionEvent e) {}
}
