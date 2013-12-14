package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import player_utils.BoardHistory;
import board_utils.GoPlayingBoard;
import board_utils.Stone;
import custom_java_utils.CheckFailException;

public class GuiBoardPlay extends GuiBoard implements ActionListener,
													  MouseListener,
													  ItemListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6992005544699270577L;
	private Model model;
	private boolean drawLegalMoves;
	private JButton undoMoveItem, start, stop;
	private JComboBox<String> player1Type;
	private JComboBox<String> player1Colour;
	private JComboBox<String> player2Type;
	private JComboBox<String> player2Colour;
	
	public GuiBoardPlay(JFrame frame) throws FileNotFoundException, CheckFailException {
		super(frame);
		model = new Model(this);
		
		JCheckBoxMenuItem showValidMoves = new JCheckBoxMenuItem("Show valid/invalid moves");
		menuBar.add(showValidMoves);
		showValidMoves.addItemListener(this);
		addMouseListener(this);
		importFileItem.addActionListener(this);
	    exportFileItem.addActionListener(this);
	    
	    undoMoveItem = new JButton("Undo");
		menuBar.add(undoMoveItem);
		undoMoveItem.addActionListener(this);
		
		JPanel playersPanel = new JPanel(new GridLayout(2,4,10,5));
		playersPanel.add(new JLabel("Player 1: "));
		player1Type = new JComboBox<String>(new String[]{Model.COMPUTERSTRING, Model.HUMANSTRING});
		playersPanel.add(player1Type);
		player1Type.addActionListener(this);
		player1Colour = new JComboBox<String>(new String[]{Model.BlACKSTRING, Model.WHITESTRING});
		playersPanel.add(player1Colour);
		player1Colour.addActionListener(this);
		start = new JButton("Start");
		playersPanel.add(start);
		start.addActionListener(this);
		
		playersPanel.add(new JLabel("Player 2: "));
		player2Type = new JComboBox<String>(new String[]{Model.COMPUTERSTRING, Model.HUMANSTRING});
		playersPanel.add(player2Type);
		player2Type.addActionListener(this);
		player2Colour = new JComboBox<String>(new String[]{Model.WHITESTRING, Model.BlACKSTRING});
		playersPanel.add(player2Colour);
		player2Colour.addActionListener(this);
		stop = new JButton("Stop");
		playersPanel.add(stop);
		stop.addActionListener(this);
		
		frame.setTitle("Go game solver [Play mode]");
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.getContentPane().add(playersPanel, BorderLayout.SOUTH);
		frame.setBackground(new Color(242,186,107));
		frame.pack();
		frame.setVisible(true);
        frame.setSize(500, 500);
	}
	
	public GuiBoardPlay(JFrame frame, GoPlayingBoard gpb) throws FileNotFoundException, CheckFailException{
		this(frame);
		model = new Model(this, gpb);
	}
	
	private void drawStone(int x, int y){
		if(model.isMoveLegal(x, y)) {
			model.addStone(x, y);
		}
	}
	
	public void setPlayersColours(Stone firstPlayerColour){
		if(firstPlayerColour == Stone.WHITE) {
			player1Colour.setSelectedItem(Model.WHITESTRING);
			player2Colour.setSelectedItem(Model.BlACKSTRING);
		} else if(firstPlayerColour == Stone.BLACK){
			player1Colour.setSelectedItem(Model.BlACKSTRING);
			player2Colour.setSelectedItem(Model.WHITESTRING);
		}
	}
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;

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
					g2.setPaint(new Color(170, 170, 170, 200));
					g2.fillRect(intersections[i][j].getTopLeftX(), intersections[i][j].getTopLeftY(), sqWidth, sqWidth);
				}			
			}
		
		//Draw legal moves
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
		
		if(model.getTarget() != null){
			Intersection target = intersections[model.getTarget().x()][model.getTarget().y()];
			g2.setPaint(Color.RED);
			g2.fillOval(target.center.x - sqWidth/4, target.center.y - sqWidth/4, sqWidth/2, sqWidth/2);
		}
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
		            model = new Model(this, file);
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
					model.toFile(fc.getSelectedFile());
				} catch (FileNotFoundException e1) {
					System.out.println("File not found");
				}
			}
		} else if(e.getSource().equals(modeMenuItem)){
			frame.getContentPane().removeAll();
			frame.getContentPane().add(new GuiBoardBuild(frame, model.getCurrentBoard()), BorderLayout.CENTER);
			BoardHistory.wipeHistory();
		} else if(e.getSource().equals(undoMoveItem)) {
			model.undoMove();
			repaint();
		} else if(e.getSource().equals(player1Type)) {
			model.setFirstPlayerType(((JComboBox) e.getSource()).getSelectedItem());
		} else if(e.getSource().equals(player1Colour)) {
			model.setFirstPlayerColour(((JComboBox) e.getSource()).getSelectedItem());
		} else if(e.getSource().equals(player2Type)) {
			model.setSecondPlayerType(((JComboBox) e.getSource()).getSelectedItem());
		} else if(e.getSource().equals(player2Colour)) {
			model.setSecondPlayerColour(((JComboBox) e.getSource()).getSelectedItem());
		} else if(e.getSource().equals(start)) {
			model.start();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for(int i = 0; i < BOARDSIZE; i++)        
			for(int j = 0; j < BOARDSIZE; j++) {
				if(intersections[i][j].contains(e.getPoint())) {
					drawStone(i,j);
					repaint();
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
	
	 public void itemStateChanged(ItemEvent e) {
         if(e.getStateChange() == ItemEvent.SELECTED)
                 drawLegalMoves = true;
         if(e.getStateChange() == ItemEvent.DESELECTED)
                 drawLegalMoves = false;
         repaint();
	 }
}
