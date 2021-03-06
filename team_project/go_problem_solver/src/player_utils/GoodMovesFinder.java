package player_utils;

import java.util.ArrayList;
import java.util.Collections;

import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;
import custom_java_utils.CheckFailException;

/*
 * Class for implementing heuristics
 */
public class GoodMovesFinder {
	/*
	 * Good moves in order. Actually all possible moves will be returned, 
	 * but best moves first. 
	 */
	private ArrayList<CellValuePair> goodMoves;
	private GoPlayingBoard currentBoard;
	
	/*
	 * Points for putting opponent group to atari
	 */
	private static final int ATARI_POINTS = 400; 
	private static final int KILLING_HIMSELF_PENALTY = 1000;
	private static final int GROUP_SAVER_REWARD = 1000;
	private static final int EYE_POINTS = 300;
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
			isEye();
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
			newBoard.setCellAt(pair.cell.getVerticalCoordinate(), pair.cell.getHorizontalCoordinate(), pair.cell);
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
			newBoard.setCellAt(pair.cell.getVerticalCoordinate(), pair.cell.getHorizontalCoordinate(), pair.cell);
			LegalMovesChecker checker = new LegalMovesChecker(newBoard);
			if(!checker.captureOponent(pair.cell).isEmpty()) {
				newBoard = checker.getNewBoard();
				BoardHistory.getSingleton().remove(newBoard);
			}
			newBoard.oppositeToPlayNext();
			for(GoCell neighbour : newBoard.getNeighboursOf(pair.cell))
				if (neighbour != null 
						&& GoCell.areOposite(neighbour, pair.cell) 
						&& checker.getLiberties(neighbour) == 1) {
					pair.value += ATARI_POINTS;
				}
		}
	}
	
	/*
	 * Checks if move lowered any of player's group liberties. If yes then subtracting from the move
	 * KILLING_HIMSELF_PENALTY amount of points
	 */
	private void isKillingHimself() throws CheckFailException {
		for(CellValuePair pair : goodMoves){
			GoPlayingBoard newBoard = currentBoard.clone();
			newBoard.setCellAt(pair.cell.getVerticalCoordinate(), pair.cell.getHorizontalCoordinate(), pair.cell);
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
					newBoard.setCellAt(pair.cell.getVerticalCoordinate(), pair.cell.getHorizontalCoordinate(), pair.cell);
					checker = new LegalMovesChecker(newBoard);
					if(!checker.captureOponent(pair.cell).isEmpty()) {
						newBoard = checker.getNewBoard();
						BoardHistory.getSingleton().remove(newBoard);
					}
					newBoard.oppositeToPlayNext();
					if(checker.getLiberties(pair.cell) > 1) {
						pair.value += GROUP_SAVER_REWARD;
					}
					break;
					
				}
			}
			
		}
	}
	
	/**
	 * Let's say an eye is if at least 50% of surrounding intersections
	 * have same stone colour or only two or less same colour stones are 
	 * missing to fully surround group of liberties.
	 *  
	 * @param cell The cell of group which we are inspecting as an eye
	 * @param board The board in which we are inspecting for an eye
	 * @return true if given group of liberties looks like an eye
	 */
	private boolean doesLookLikeEye(GoCell cell, GoPlayingBoard board){
		int ownStonesAround = 0;
		ArrayList<GoCell> closeCellsGroup = board.getCloseCellsOfGroup(cell);
		for(GoCell closeCell : closeCellsGroup){
			if(closeCell.getContent() == board.toPlayNext())
				ownStonesAround++;
		}
		if((double)ownStonesAround / (double)closeCellsGroup.size() >= 0.5)
			return true;
		if(0 < closeCellsGroup.size() - ownStonesAround 
				&& closeCellsGroup.size() - ownStonesAround <= 2)
			return true;
		return false;
	}
	
	/**
	 * Maybe I should use reflection? 
	 */
	public boolean testDoesLookLikeEye(GoCell cell, GoPlayingBoard board){
		return doesLookLikeEye(cell, board);
	}
	
	private void isEye() throws CheckFailException{
		GoPlayingBoard newBoard = currentBoard.clone();
		LegalMovesChecker checker = new LegalMovesChecker(currentBoard);
		for(CellValuePair pair : goodMoves) {
			newBoard.setCellAt(pair.cell.getVerticalCoordinate(), pair.cell.getHorizontalCoordinate(), pair.cell);
			checker = new LegalMovesChecker(newBoard);
			if(!checker.captureOponent(pair.cell).isEmpty()) {
				newBoard = checker.getNewBoard();
				BoardHistory.getSingleton().remove(newBoard);
			}
			newBoard.oppositeToPlayNext();
			for(GoCell cell : newBoard.getCloseCellsOfGroup(pair.cell)){
				if(cell.getContent() == Stone.NONE && doesLookLikeEye(cell, newBoard)) {
					pair.value += EYE_POINTS;
				}
			}
		}
	}
	
	public GoCell[] getGoodMoves(){
		Collections.sort(goodMoves, Collections.reverseOrder());
		GoCell[] result = new GoCell[goodMoves.size()];
		int i = 0;
		for(CellValuePair pair : goodMoves) {
			result[i++] = pair.cell;
		}
		return result;
	}	
}
