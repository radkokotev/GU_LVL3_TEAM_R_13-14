package gui;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;

import custom_java_utils.CheckFailException;
import player_utils.*;
import board_utils.*;

public class Model {
	
	private GoPlayingBoard currentBoard;
	private LegalMovesChecker checker;
	private boolean[][] legalMoves;
	private BoardHistory history;
	
	public Model() {
		currentBoard = new GoPlayingBoard();
		history = BoardHistory.getSingleton();
		checker = new LegalMovesChecker(currentBoard);
	}
	
	public Model(File fileName) throws FileNotFoundException, CheckFailException {
		this.currentBoard = new GoPlayingBoard(fileName);
		history = BoardHistory.getSingleton();
		this.checker = new LegalMovesChecker(currentBoard);
		legalMoves = checker.getLegalityArray();
	}
	
	public void addStone(int x, int y) {
		currentBoard.setCellAt(x, y, new GoCell(currentBoard.toPlayNext(), x, y));
		currentBoard.oppositeToPlayNext();
		checker = new LegalMovesChecker(currentBoard);
		legalMoves = checker.getLegalityArray();
		history.add(currentBoard);
		
	}
	
	public boolean isMoveLegal(int x, int y) {
		if(legalMoves != null){
			return legalMoves[x][y];
		} else 
			return true;
	}
	
	public void removeOpponent(int x, int y)  {
		boolean isAnyKilled = false;
		isAnyKilled = checker.captureOponent(currentBoard.getCellAt(x, y));
		try {
			if(isAnyKilled) {
				currentBoard = checker.getNewBoard();
				checker = new LegalMovesChecker(currentBoard);
				legalMoves = checker.getLegalityArray();
			}
		} catch(Exception e){
			System.out.println("new board == old board");
		}
		
	}
	
	public int getTotalNumberOfStones(){
		return currentBoard.getCountPiecesOnBoard();
	}
	
	public int getBlackNumberOfStones(){
		return currentBoard.getNumberofBlackStones();
	}
	
	public int getWhiteNumberOfStones(){
		return currentBoard.getNumberOfWhiteStones();
	}
	
	public void recountBlackStones() {
		currentBoard.countAndSetBlackStones();
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
	/**
	 * Creates a new file and populates it with current board.
	 * @param file full path of the file where to save it
	 * @throws FileNotFoundException 
	 */
	public void toFile(File file) throws FileNotFoundException{
		currentBoard.toFile(file);
	}
	
	public void undoMove() {
		history.undoMove();
		if (history.getLastMove() != null)
			currentBoard = history.getLastMove();
	}
	
	public void redoMove() {
		history.redoMove();
		currentBoard = history.getUndoMove();
	}

}
