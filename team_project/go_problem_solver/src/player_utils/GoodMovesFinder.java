package player_utils;

import java.util.ArrayList;
import java.util.Collections;

import custom_java_utils.CheckFailException;
import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;

/*
 * Class for implementing heuristics
 */
public class GoodMovesFinder {
	
	private class CellValuePair implements Comparable<CellValuePair>{
		public GoCell cell;
		public long value = 0;
		
		/* 
		 * if this < other then return >0
		 * if this > other then return <0
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
	private LegalMovesChecker checker;
	
	public GoodMovesFinder(GoPlayingBoard board) throws CheckFailException{
		currentBoard = board.clone();
		goodMoves = new ArrayList<CellValuePair>();
		checker = new LegalMovesChecker(board);
		
		//And here all heuristic methods can be called
		countPieces();
	}
	
	private void countPieces() throws CheckFailException {
		for(int i = 0; i < currentBoard.getWidth(); i++)
			for(int j = 0; j < currentBoard.getHeight(); j++) {
				GoCell cell = new GoCell(currentBoard.toPlayNext(), i, j);
				if(checker.isMoveLegal(cell)) {
					GoPlayingBoard newBoard = checker.getNewBoard();
					newBoard.oppositeToPlayNext();
					CellValuePair pair = new CellValuePair();
					pair.value = pair.value + newBoard.getNumberOfOwnStones();
					pair.value = pair.value - newBoard.getNumberOfOpponentStones();
					pair.cell = cell;
					goodMoves.add(pair);
					checker.reset();
				}
			}	
	}
	
	public GoCell[] getGoodMoves(){
		Collections.sort(goodMoves);
		GoCell[] result = new GoCell[goodMoves.size()];
		int i = 0;
		for(CellValuePair pair : goodMoves)
			result[i++] = pair.cell;
		return result;
	}	
}
