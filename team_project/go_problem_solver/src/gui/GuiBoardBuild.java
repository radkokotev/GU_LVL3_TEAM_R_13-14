package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import custom_java_utils.CheckFailException;
import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;

public class GuiBoardBuild extends GuiBoard implements MouseListener, 
													   ActionListener {
	private Stone current;
	private GoPlayingBoard gpb;
	private JComboBox<String> combo;
	private boolean isNextStoneTarget;
	private Point targetStone = null;
	
	//Constants for combo box
	private static final String WHITE = "White";
	private static final String BLACK = "Black";
	private static final String INNER_BORDER = "Inner border";
	private static final String NONE = "Empty";
	private static final String TARGET_STONE ="Target stone";
	
	private static final File DEFAULT_BOARD = new File(System.getProperty("user.home") + "/git/GU_LVL3_TEAM_R_13-14/team_project/go_problem_solver/src/gui/defaultBoardforBuildMode.go");
	
	public GuiBoardBuild(JFrame frame) {
		super(frame);
		current = Stone.BLACK;
		try {
			gpb = new GoPlayingBoard(DEFAULT_BOARD);
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + DEFAULT_BOARD);
		} catch (CheckFailException e) {
			System.out.println("File is in bad format: " + DEFAULT_BOARD);
		}
		addMouseListener(this);
		importFileItem.addActionListener(this);
	    exportFileItem.addActionListener(this);
	    
	    combo = new JComboBox<String>();
	    combo.addItem(BLACK);
	    combo.addItem(WHITE);
	    combo.addItem(INNER_BORDER);
	    combo.addItem(NONE);
	    combo.addItem(TARGET_STONE);
	    combo.addActionListener(this);
	    menuBar.add(combo);
	    
	    frame.setTitle("Go game solver [Build mode]");
	    frame.getContentPane().add(this);
	    frame.setBackground(new Color(242,186,107));
		frame.pack();
		frame.setVisible(true);
        frame.setSize(500, 500);
	}
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		//draw all stones
		GoCell[][] cells = gpb.getBoard();
		for(int i = 0; i < 19; i++)
			for(int j = 0; j < 19; j++){
				if(cells[i][j].getContent() == Stone.BLACK) {
					g2.setPaint(Color.BLACK);
					g2.fillOval(intersections[i][j].getTopLeftX(), intersections[i][j].getTopLeftY(), sqWidth, sqWidth);
				} else if(cells[i][j].getContent() == Stone.WHITE){
					g2.setPaint(Color.WHITE);
					g2.fillOval(intersections[i][j].getTopLeftX(), intersections[i][j].getTopLeftY(), sqWidth, sqWidth);
				} else if(cells[i][j].getContent() == Stone.INNER_BORDER){
					g2.setPaint(new Color(170, 170, 170, 200));
					g2.fillRect(intersections[i][j].getTopLeftX(), intersections[i][j].getTopLeftY(), sqWidth, sqWidth);
				}		
			}
		
		if(targetStone != null){
			Intersection target = intersections[targetStone.x][targetStone.y];
			g2.setPaint(Color.RED);
			g2.fillOval(target.center.x - sqWidth/4, target.center.y - sqWidth/4, sqWidth/2, sqWidth/2);
		}
	}
	
	@Override
    public void mouseClicked(MouseEvent e) {
	    for(int i = 0; i < BOARDSIZE; i++)        
            for(int j = 0; j < BOARDSIZE; j++) {
                if(intersections[i][j].contains(e.getPoint())) {
                	if(!isNextStoneTarget)
	                	gpb.setCellAt(i, j, new GoCell(current, i, j));
                	else {
                		targetStone = new Point(i, j);
                	}
	                repaint();
	                return;
                }
            }
    }

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		JFileChooser fc = new JFileChooser(DEFAULT_DIRECTORY);
		fc.setMultiSelectionEnabled(false);
		if(e.getSource().equals(importFileItem)) {
			int returnVal = fc.showOpenDialog(this);
			try {
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            gpb = new GoPlayingBoard(file);
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
				try {
					gpb.toFile(fc.getSelectedFile(), targetStone);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else if(e.getSource().equals(combo)){
			JComboBox source = (JComboBox) e.getSource();
			if(WHITE.equals(source.getSelectedItem())){
				current = Stone.WHITE;
				isNextStoneTarget = false;
			} else if (BLACK.equals(source.getSelectedItem())){
				current = Stone.BLACK;
				isNextStoneTarget = false;
			} else if (INNER_BORDER.equals(source.getSelectedItem())){
				current = Stone.INNER_BORDER;
				isNextStoneTarget = false;
			} else if (NONE.equals(source.getSelectedItem())){
				current = Stone.NONE;
				isNextStoneTarget = false;
			} else if (TARGET_STONE.equals(source.getSelectedItem())){
				isNextStoneTarget = true;
			}
		} else if(e.getSource().equals(modeMenuItem)){
			frame.getContentPane().removeAll();;
			frame.getContentPane().add(new GuiBoardPlay(frame), BorderLayout.CENTER);
		}
	}
}
