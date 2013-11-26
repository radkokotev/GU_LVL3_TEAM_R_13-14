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

public class GuiBoard extends JPanel implements ActionListener{
	private Model model;
	private JMenuItem importFileItem, exportFileItem;
	
	final static int MARGIN = 50;
	final static int BOARDSIZE = 19;
	final static File DEFAULT_DIRECTORY = new File(System.getProperty("user.dir") + "/src/player_utils/test_data/");
	private Intersection[][] intersections = new Intersection[BOARDSIZE][BOARDSIZE];
	private int sqWidth;
	
	
	public GuiBoard(JFrame frame){
	    JMenuBar menuBar = new JMenuBar();
	    JMenu fileMenu = new JMenu("File");
	    importFileItem = new JMenuItem("Import file");
	    exportFileItem = new JMenuItem("Export file");
	    importFileItem.addActionListener(this);
	    exportFileItem.addActionListener(this);
	    fileMenu.add(importFileItem);
	    fileMenu.add(exportFileItem);
	    menuBar.add(fileMenu);
	    frame.setJMenuBar(menuBar); 
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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser(DEFAULT_DIRECTORY);
		fc.setMultiSelectionEnabled(false);
		if(e.getSource().equals(importFileItem)) {
			int returnVal = fc.showOpenDialog(this);
			try {
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            model = new Model(file.getAbsolutePath());
					repaint();
		        }
			} catch (FileNotFoundException e1) {
				System.out.println("File not found.");	
			} catch (CheckFailException e1) {
				System.out.println("Check fail exception.");
			}
		} else if(e.getSource().equals(exportFileItem)){
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showSaveDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				//System.out.println(fc.getSelectedFile());
				//TODO implement saving the file
			}
		}		
	}
}
