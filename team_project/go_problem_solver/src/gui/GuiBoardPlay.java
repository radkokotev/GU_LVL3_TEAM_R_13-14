package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
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

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import player_utils.BoardHistory;
import board_utils.GoPlayingBoard;
import board_utils.Stone;
import custom_java_utils.CheckFailException;

public class GuiBoardPlay extends GuiBoard implements ActionListener,
													  MouseListener,
													  ItemListener {
	
	private static final long serialVersionUID = -5583632264766625487L;
	private Model model;
	private boolean drawLegalMoves;
	private JButton undoMoveItem, redoMoveItem, start, reset, set;
	private JComboBox player1Type;
	private JComboBox player1Colour;
	private JComboBox player1Algorithm;
	private JComboBox player2Type;
	private JComboBox player2Colour;
	private JComboBox player2Algorithm;
	private JLabel noOfGamesLabel;
	private JTextField textField;
	private JTextField noOfGames;
	
	private File lastImportedFilename;
	
	public GuiBoardPlay(JFrame frame) throws FileNotFoundException, CheckFailException {
		super(frame);
		lastImportedFilename = null;
		model = new Model(this);
		
		JCheckBoxMenuItem showValidMoves = new JCheckBoxMenuItem("Show valid/invalid moves");
		menuBar.add(showValidMoves);
		showValidMoves.addItemListener(this);
		addMouseListener(this);
		importFileItem.addActionListener(this);
	    exportFileItem.addActionListener(this);
	    
	    textField = new JTextField();
	    textField.setFocusable(false);
	    textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
	    textField.setBackground(new Color(242,186,107));
	    
		JPanel playersPanel = new JPanel(new GridLayout(3,4,10,5));
		JPanel randomGamePanel = new JPanel(new GridLayout(1, 3, 10, 5));
		randomGamePanel.setBackground(Color.WHITE);
		randomGamePanel.setBackground(new Color(242,186,107));
		
		noOfGamesLabel = new JLabel("No. of Random games");
		randomGamePanel.add(noOfGamesLabel);
		noOfGamesLabel.setVisible(false);
		
		noOfGames = new JTextField();
		noOfGames.setText("100");
		noOfGames.setHorizontalAlignment(JTextField.CENTER);
		randomGamePanel.add(noOfGames);
		noOfGames.setVisible(false);
		
		set = new JButton("Set");
		randomGamePanel.add(set);
		set.addActionListener(this);
		set.setVisible(false);
		
		undoMoveItem = new JButton("Undo");
		playersPanel.add(undoMoveItem);
		undoMoveItem.addActionListener(this);
		
		redoMoveItem = new JButton("Redo");
		playersPanel.add(redoMoveItem);
		redoMoveItem.addActionListener(this);
		
		start = new JButton("Start");
		playersPanel.add(start);
		start.addActionListener(this);
		
		reset = new JButton("Reset");
		playersPanel.add(reset);
		reset.addActionListener(this);
		
		playersPanel.add(new JLabel("Player 1: "));
		player1Type = new JComboBox(new String[]{Model.HUMANSTRING, Model.COMPUTERSTRING});
		playersPanel.add(player1Type);
		player1Type.addActionListener(this);
		player1Colour = new JComboBox(new String[]{Model.BlACKSTRING, Model.WHITESTRING});
		playersPanel.add(player1Colour);
		player1Colour.addActionListener(this);
		
		// TODO add alg chooser to GUI
		player1Algorithm = new JComboBox(new String[]{Model.ALPHABETASTRING, Model.MINIMAXSTRING, Model.MONTECARLOSTRING});
		playersPanel.add(player1Algorithm);
		player1Algorithm.addActionListener(this);
		player1Algorithm.setVisible(false);
		player1Algorithm.setSelectedIndex(-1);
		
		
		playersPanel.add(new JLabel("Player 2: "));
		player2Type = new JComboBox(new String[]{Model.HUMANSTRING, Model.COMPUTERSTRING});
		playersPanel.add(player2Type);
		player2Type.addActionListener(this);
		player2Colour = new JComboBox(new String[]{Model.WHITESTRING, Model.BlACKSTRING});
		playersPanel.add(player2Colour);
		player2Colour.addActionListener(this);
		
		player2Algorithm = new JComboBox(new String[]{Model.ALPHABETASTRING, Model.MINIMAXSTRING, Model.MONTECARLOSTRING});
		playersPanel.add(player2Algorithm);
		player2Algorithm.addActionListener(this);
		player2Algorithm.setVisible(false);
		player2Algorithm.setSelectedIndex(-1);
		
		frame.setTitle("Go game solver [Play mode]");
		frame.getContentPane().add(this);
		frame.getContentPane().add(randomGamePanel, BorderLayout.NORTH);
		frame.getContentPane().add(textField);
		frame.getContentPane().add(playersPanel, BorderLayout.SOUTH);
		// TODO Windows won't accept a colour change
		frame.setBackground(new Color(242,186,107));
		//frame.getContentPane().setBackground(new Color(242,186,107));
		frame.pack();
		frame.setVisible(true);
        frame.setSize(500, 600);
	}
	
	public GuiBoardPlay(JFrame frame, GoPlayingBoard gpb) throws FileNotFoundException, CheckFailException{
		this(frame);
		model = new Model(this, gpb);
	}
	
	private void drawStone(int x, int y){
		if(model.isMoveLegal(x, y)) {
			model.addStone(x, y);
			textField.setText("To play next: " + model.getCurrentBoard().toPlayNext().toString());
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
	
	public void setTextField(String text) {
		textField.setText(text);
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
			Intersection target = intersections[model.getTarget().getVerticalCoordinate()]
					[model.getTarget().getHorizontalCoordinate()];
			g2.setPaint(Color.RED);
			g2.fillOval(target.center.x - sqWidth/4, target.center.y - sqWidth/4, sqWidth/2, sqWidth/2);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		JFileChooser fc = new JFileChooser(DEFAULT_DIRECTORY);
		fc.setMultiSelectionEnabled(false);
		fc.setFileFilter(new FileNameExtensionFilter("Go problems files", "go"));
		if(e.getSource().equals(importFileItem)) {
			int returnVal = fc.showOpenDialog(this);
			try {
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            model = new Model(this, file);
		            // Reseting all field values
		            textField.setText("To play next: " + model.getCurrentBoard().toPlayNext().toString());
					player1Type.setSelectedItem(player1Type.getSelectedItem());
					player1Algorithm.setSelectedItem(player1Algorithm.getSelectedItem());
					player1Colour.setSelectedItem(player1Colour.getSelectedItem());
					player2Type.setSelectedItem(player2Type.getSelectedItem());
					player2Algorithm.setSelectedItem(player2Algorithm.getSelectedItem());
					player2Colour.setSelectedItem(player2Colour.getSelectedItem());
					if (player1Algorithm.getSelectedItem() == "Monte Carlo" || player2Algorithm.getSelectedItem() == "Monte Carlo") {
						noOfGames.setText("100");
						noOfGamesLabel.setVisible(true);
						noOfGames.setVisible(true);
						set.setVisible(true);
					}
		            lastImportedFilename = file;
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
			textField.setText("To play next: " + model.getCurrentBoard().toPlayNext().toString());
			repaint();
		} else if(e.getSource().equals(redoMoveItem)) {
			model.redoMove();
			textField.setText("To play next: " + model.getCurrentBoard().toPlayNext().toString());
			repaint();
		} else if(e.getSource().equals(player1Type)) {
			model.setFirstPlayerType(((JComboBox) e.getSource()).getSelectedItem());
			if (((JComboBox) e.getSource()).getSelectedItem().equals("Computer")) 
				player1Algorithm.setVisible(true);
			else
				player1Algorithm.setVisible(false);
		} else if (e.getSource().equals(set)) {
			System.out.println(noOfGames.getText());
			model.setNoOfRandomGames(noOfGames.getText());
		} else if(e.getSource().equals(player1Colour)) {
			model.setFirstPlayerColour(((JComboBox) e.getSource()).getSelectedItem());
			player2Colour.setSelectedItem(model.getOppositeColour(((JComboBox) e.getSource()).getSelectedItem()));
			textField.setText("To play next: " + model.getCurrentBoard().toPlayNext().toString());
		} else if(e.getSource().equals(player2Type)) {
			model.setSecondPlayerType(((JComboBox) e.getSource()).getSelectedItem());
			if (((JComboBox) e.getSource()).getSelectedItem().equals("Computer")) 
				player2Algorithm.setVisible(true);
			else
				player2Algorithm.setVisible(false);
		} else if (e.getSource().equals(player1Algorithm)) {
			model.setPlayer1AlgorithmName((String) ((JComboBox) e.getSource()).getSelectedItem());
			if (((JComboBox) e.getSource()).getSelectedItem() == Model.MONTECARLOSTRING) {
				noOfGames.setText("100");
				noOfGamesLabel.setVisible(true);
				noOfGames.setVisible(true);
				set.setVisible(true);
			} else {
				noOfGamesLabel.setVisible(false);
				noOfGames.setVisible(false);
				set.setVisible(false);
			}
		} else if (e.getSource().equals(player2Algorithm)) {
			model.setPlayer2AlgorithmName((String) ((JComboBox) e.getSource()).getSelectedItem());
			if (((JComboBox) e.getSource()).getSelectedItem() == Model.MONTECARLOSTRING) {
				noOfGames.setText("100");
				noOfGamesLabel.setVisible(true);
				noOfGames.setVisible(true);
				set.setVisible(true);
			} else {
				noOfGamesLabel.setVisible(false);
				noOfGames.setVisible(false);
				set.setVisible(false);
			}
		} else if(e.getSource().equals(player2Colour)) {
			model.setSecondPlayerColour(((JComboBox) e.getSource()).getSelectedItem());
			player1Colour.setSelectedItem(model.getOppositeColour(((JComboBox) e.getSource()).getSelectedItem()));
			textField.setText("To play next: " + model.getCurrentBoard().toPlayNext().toString());
		} else if(e.getSource().equals(start)) {
			model.start();
		} else if(e.getSource().equals(reset)) {
			try {
				model = new Model(this, lastImportedFilename);
				// Reseting all field values
				player1Type.setSelectedItem(player1Type.getSelectedItem());
				player1Algorithm.setSelectedItem(player1Algorithm.getSelectedItem());
				player1Colour.setSelectedItem(player1Colour.getSelectedItem());
				player2Type.setSelectedItem(player2Type.getSelectedItem());
				player2Algorithm.setSelectedItem(player2Algorithm.getSelectedItem());
				player2Colour.setSelectedItem(player2Colour.getSelectedItem());
				if (player1Algorithm.getSelectedItem() == "Monte Carlo" || player2Algorithm.getSelectedItem() == "Monte Carlo") {
					noOfGames.setText("100");
					noOfGamesLabel.setVisible(true);
					noOfGames.setVisible(true);
					set.setVisible(true);
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CheckFailException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			repaint();
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
