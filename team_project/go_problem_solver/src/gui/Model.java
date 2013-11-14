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
	
	public Model(String fileName) throws FileNotFoundException, CheckFailException {
		this.currentBoard = new GoPlayingBoard(fileName);
		this.history = BoardHistory.getSingleton();
		this.checker = new LegalMovesChecker(currentBoard);
		legalMoves = checker.getLegalityArray();
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
	}
	
	public boolean isMoveLegal(int x, int y){
		if(legalMoves != null){
			System.out.println(legalMoves[x][y]);
			return legalMoves[x][y];
		} else 
			return true;
	}
	
	public void removeOpponent(int x, int y){
		GoCell[] neighbours = currentBoard.getNeighboursOf(currentBoard.getCellAt(x, y));
		
		for(GoCell neighbour : neighbours){
			System.out.println("Removing: " + neighbour.x() + " " + neighbour.y());
			System.out.println("Oponent moved: " + checker.captureOponent(neighbour) + " Neighbour: " + neighbour);
		}
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
