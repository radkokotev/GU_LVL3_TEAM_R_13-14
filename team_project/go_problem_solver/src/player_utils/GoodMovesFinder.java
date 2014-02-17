package player_utils;

import java.util.ArrayList;
import java.util.Collections;

import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import custom_java_utils.CheckFailException;

/*
 * Class for implementing heuristics
 */
public class GoodMovesFinder {
	
	private class CellValuePair implements Comparable<CellValuePair>{
		public GoCell cell;
		public long value = 0;
		
		/* 
		 * if other < this then return <0
		 * if other > this then return >0
		 * if this == other then return 0
		 * It actually should be reverse, but I want to Collections.sort()
		 * to work that it would sort in descending order.
		 * 
		 */
		public int compareTo(CellValuePair other) {
			return (int) (other.value - this.value);
		}
	}
		
	/*
	 * Good moves in order. Actually all possible moves will be returned, 
	 * but best moves first. 
	 */
	private ArrayList<CellValuePair> goodMoves;
	private GoPlayingBoard currentBoard;
	
	/*
	 * Points for putting opponent group to atari
	 */
	private static final int ATARI_POINTS = 1; 
	private static final int KILLING_HIMSELF_PENALTY = 1000;
	private static final int GROUP_SAVER_REWARD = 1000;
	private static final boolean SWITCH_OFF = false;
	
	public GoodMovesFinder(GoPlayingBoard board) throws CheckFailException{
		currentBoard = board.clone();
		goodMoves = new ArrayList<CellValuePair>();
		getLegalMoves();
		
		//And here all heuristic methods can be called
		if(!SWITCH_OFF) {
			countPieces();
			addAtariPoints();
			isKillingHimself();
			canISaveMyGroupWithOneLiberty();
		}
	}
	/*
	 * Getting and keeping all legal moves for current board position. All following methods can then just
	 * go through list of goodMoves and be sure that checked all possible moves.
	 */
	private void getLegalMoves(){
		LegalMovesChecker checker = new LegalMovesChecker(currentBoard);
		for(int i = 0; i < currentBoard.getWidth(); i++)
			for(int j = 0; j < currentBoard.getHeight(); j++) {
				GoCell cell = new GoCell(currentBoard.toPlayNext(), i, j);
				if(checker.isMoveLegal(cell)) {
					CellValuePair pair = new CellValuePair();
					pair.cell = cell;
					goodMoves.add(pair);
				}
				checker.reset();
			}
	}
	
	/*
	 * Adding points to goodMoves depending on number of pieces on the board.
	 */
	private void countPieces() throws CheckFailException {
		for(CellValuePair pair : goodMoves) {
			GoPlayingBoard newBoard = currentBoard.clone();
			newBoard.setCellAt(pair.cell.x(), pair.cell.y(), pair.cell);
			LegalMovesChecker checker = new LegalMovesChecker(newBoard);
			if(!checker.captureOponent(pair.cell).isEmpty()) {
				newBoard = checker.getNewBoard();
				BoardHistory.getSingleton().remove(newBoard);
			}
			newBoard.oppositeToPlayNext();
			pair.value += newBoard.getNumberOfOwnStones(); 
			pair.value -= newBoard.getNumberOfOpponentStones();
		}	
	}
	/*
	 * Adds ATARI_POINTS amount to moves, which puts opponent stones group into Atari.
	 */
	private void addAtariPoints() throws CheckFailException {
		for(CellValuePair pair : goodMoves) {
			GoPlayingBoard newBoard = currentBoard.clone();
			newBoard.setCellAt(pair.cell.x(), pair.cell.y(), pair.cell);
			LegalMovesChecker checker = new LegalMovesChecker(newBoard);
			if(!checker.captureOponent(pair.cell).isEmpty()) {
				newBoard = checker.getNewBoard();
				BoardHistory.getSingleton().remove(newBoard);
			}
			newBoard.oppositeToPlayNext();
			for(GoCell neighbour : newBoard.getNeighboursOf(pair.cell))
				if (neighbour != null 
						&& GoCell.areOposite(neighbour, pair.cell) 
						&& checker.getLiberties(neighbour) == 1)
					pair.value += ATARI_POINTS;
		}
	}
	
	/*
	 * Checks if move lowered any of player's group liberties. If yes then subtracting from the move
	 * KILLING_HIMSELF_PENALTY amount of points
	 */
	private void isKillingHimself() throws CheckFailException {
		for(CellValuePair pair : goodMoves){
			GoPlayingBoard newBoard = currentBoard.clone();
			newBoard.setCellAt(pair.cell.x(), pair.cell.y(), pair.cell);
			LegalMovesChecker newChecker = new LegalMovesChecker(newBoard);
			LegalMovesChecker oldChecker = new LegalMovesChecker(currentBoard);
			if(!newChecker.captureOponent(pair.cell).isEmpty()) {
				newBoard = newChecker.getNewBoard();
				BoardHistory.getSingleton().remove(newBoard);
			}
			newBoard.oppositeToPlayNext();
			for(GoCell neighbour : currentBoard.getNeighboursOf(pair.cell)) {
				if(neighbour != null 
						&& pair.cell.getContent() == neighbour.getContent() 
						&& oldChecker.getLiberties(neighbour) > newChecker.getLiberties(pair.cell)) {
					pair.value -= KILLING_HIMSELF_PENALTY;
					break;
				}
			}
		}
	}

	private void canISaveMyGroupWithOneLiberty() throws CheckFailException{
		for(CellValuePair pair : goodMoves){
			GoPlayingBoard newBoard = currentBoard.clone();
			LegalMovesChecker checker = new LegalMovesChecker(currentBoard);
			for(GoCell cell : currentBoard.getNeighboursOf(pair.cell)){
				if(cell != null && !GoCell.areOposite(cell, pair.cell) && checker.getLiberties(cell) == 1) {
					newBoard.setCellAt(pair.cell.x(), pair.cell.y(), pair.cell);
					checker = new LegalMovesChecker(newBoard);
					if(!checker.captureOponent(pair.cell).isEmpty()) {
						newBoard = checker.getNewBoard();
						BoardHistory.getSingleton().remove(newBoard);
					}
					newBoard.oppositeToPlayNext();
					if(checker.getLiberties(pair.cell) > 1)
						pair.value += GROUP_SAVER_REWARD;
					break;
					
				}
			}
			
		}
	}
	
	public GoCell[] getGoodMoves(){
		Collections.sort(goodMoves);
		GoCell[] result = new GoCell[goodMoves.size()];
		int i = 0;
		for(CellValuePair pair : goodMoves) {
			result[i++] = pair.cell;
			System.out.print(pair.cell + " " + pair.value + " ");
		}
		System.out.println();
		return result;
	}	
}
