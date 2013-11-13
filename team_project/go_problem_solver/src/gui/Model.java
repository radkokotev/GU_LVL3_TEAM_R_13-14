package gui;

import java.awt.Color;

import player_utils.*;
import board_utils.*;

public class Model {
	
	private GoPlayingBoard currentBoard;
	private BoardHistory history;
	private LegalMovesChecker checker;
	
	public Model(){
		currentBoard = new GoPlayingBoard();
		history = BoardHistory.getSingleton();
		checker = new LegalMovesChecker(currentBoard);
	}
	
	public void addStone(int x, int y, Color c) {
		if(c.equals(Color.BLACK))
			currentBoard.setCellAt(x, y, new GoCell(Stone.BLACK, x, y));
		else if (c.equals(Color.WHITE))
			currentBoard.setCellAt(x, y, new GoCell(Stone.WHITE, x, y));
		else System.out.println("Something wrong with colours.");
		history.add(currentBoard);
	}
	
	public boolean isMoveLegal(int x, int y, Color c){
		if(c.equals(Color.BLACK))
			return checker.isMoveLegal(new GoCell(Stone.BLACK, x, y));
		else if (c.equals(Color.WHITE))
			return checker.isMoveLegal(new GoCell(Stone.WHITE, x, y));
		return false;
	}
	
	public GoPlayingBoard getCurrentBoard() {
		return currentBoard.clone();
	}

}
