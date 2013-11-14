package gui;

import java.awt.Color;
import java.io.FileNotFoundException;

import custom_java_utils.CheckFailException;
import player_utils.*;
import board_utils.*;

public class Model {
	
	private GoPlayingBoard currentBoard;
	private BoardHistory history;
	private LegalMovesChecker checker;
	private boolean[][] legalMoves;
	
	public Model() {
		currentBoard = new GoPlayingBoard();
		history = BoardHistory.getSingleton();
		checker = new LegalMovesChecker(currentBoard);
	}
	
	public void addStone(int x, int y, Color c) {
		if(c.equals(Color.BLACK)) {
			currentBoard.setCellAt(x, y, new GoCell(Stone.BLACK, x, y));
			currentBoard.setToPlayNext(Stone.WHITE);
		} else if (c.equals(Color.WHITE)) {
			currentBoard.setCellAt(x, y, new GoCell(Stone.WHITE, x, y));
			currentBoard.setToPlayNext(Stone.BLACK);
		} else System.out.println("Something wrong with colours.");
		checker = new LegalMovesChecker(currentBoard);
		history.add(currentBoard);
		legalMoves = checker.getLegalityArray();
		System.out.println("TO PLAY NEXXXXT: " + currentBoard.toPlayNext());
		
		Stone[][] s = getCurrentBoardLayout();
		for(int i = 0; i < 19; i++)
		{
			for(int j = 0; j < 19; j++)
				System.out.print(s[i][j] + " ");
			System.out.println();
		}
		
	}
	
	public boolean isMoveLegal(int x, int y){
		if(legalMoves != null){
			System.out.println(legalMoves[x][y]);
			return legalMoves[x][y];
		} else 
			return true;
	}
	
	/**
	 * Returns the current board Stone layout
	 * @return A double dimension array of Stones
	 */
	public Stone[][] getCurrentBoardLayout() {
		GoCell[][] currentLayout = getCurrentBoard().getBoard();
		Stone[][] newLayout = new Stone[getCurrentBoard().getWidth()][getCurrentBoard().getHeight()];
		for (int i = 0; i < getCurrentBoard().getHeight(); i++)
			for (int j = 0; j < getCurrentBoard().getWidth(); j++)
				newLayout[i][j] = currentLayout[i][j].getContent();
		return newLayout;
	}
	
	/**
	 * Returns a clone of the current board
	 * @return a clone of current board
	 */
	public GoPlayingBoard getCurrentBoard() {
		return currentBoard.clone();
	}

}
