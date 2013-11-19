package gui;

import java.awt.*;

import javax.swing.*;

public class Gui {
	
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Go Game Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JMenuBar menuBar = new JMenuBar();
        JCheckBoxMenuItem showValidMoves = new JCheckBoxMenuItem("Show valid/invalid moves");
        JMenuItem importFile = new JMenuItem("Import File");
        menuBar.add(showValidMoves);
        menuBar.add(importFile);
        frame.setJMenuBar(menuBar);
        
        frame.setLayout(new BorderLayout());
        Board board = new Board(frame);
        showValidMoves.addItemListener(board);
        importFile.addActionListener(board);
        frame.getContentPane().add(board, BorderLayout.CENTER);
        frame.setBackground(new Color(242,186,107));
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setSize(500, 500);
    }
}
