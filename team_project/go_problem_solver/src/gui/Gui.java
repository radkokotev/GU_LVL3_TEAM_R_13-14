package gui;

import java.awt.*;
import java.io.FileNotFoundException;

import javax.swing.*;

import custom_java_utils.CheckFailException;

public class Gui {
	
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CheckFailException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
    
    private static void createAndShowGUI() throws FileNotFoundException, CheckFailException {
        //Create and set up the window.
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setLayout(new BorderLayout());
        GuiBoard board = new GuiBoardPlay(frame);
        frame.getContentPane().add(board, BorderLayout.CENTER);
    }
}
