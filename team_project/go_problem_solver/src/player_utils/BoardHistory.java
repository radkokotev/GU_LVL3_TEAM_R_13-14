package player_utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import board_utils.GoPlayingBoard;

/**
 * A singleton to keep the history of all boards that have been played
 */
public class BoardHistory {
	private HashMap<Integer, LinkedList<GoPlayingBoard>> boards;
	
	/**
	 * Default constructor to create an instance of the history
	 */
	public BoardHistory() {
		this.boards = new HashMap<Integer, LinkedList<GoPlayingBoard>>();
	}
	
	/**
	 * A private constructor to efficiently construct an identical instance by taking a 
	 * deep copy of the given history
	 * @param boards existing history to clone into the new instance 
	 */
	private BoardHistory(HashMap<Integer, LinkedList<GoPlayingBoard>> newBoards) {
		HashMap<Integer, LinkedList<GoPlayingBoard>> content = 
				new HashMap<Integer, LinkedList<GoPlayingBoard>>();
		for (Entry<Integer, LinkedList<GoPlayingBoard>> entry : newBoards.entrySet()) {
			LinkedList<GoPlayingBoard> list = new LinkedList<GoPlayingBoard>();
			for (GoPlayingBoard b : entry.getValue()) {
				list.add(b.clone());
			}
			content.put(entry.getKey(), list);
		}
		this.boards = content;
	}
	
	
	/**
	 * Adding the given board to the board history by making a deep copy of it.
	 * @param board the board to be added
	 */
	public void add(GoPlayingBoard board) {
		if (hasBeenPlayed(board)) {
			return;
		}
		if (!boards.containsKey(board.getCountPiecesOnBoard())) {
			LinkedList<GoPlayingBoard> list = new LinkedList<GoPlayingBoard>(); 
			list.add(board.clone());
			boards.put(board.getCountPiecesOnBoard(), list);
		} else {
			LinkedList<GoPlayingBoard> list = 
					boards.get(board.getCountPiecesOnBoard());
			list.add(board.clone());
		}		
	}
	
	/**
	 * A method to determine if the given board is already in the history
	 * @param board a board to be checked
	 * @return true if the given board is in the history, false - otherwise
	 */
	public boolean hasBeenPlayed(GoPlayingBoard board) {
		if (!boards.containsKey(board.getCountPiecesOnBoard())) {
			return false;
		}
		for (GoPlayingBoard one : boards.get(board.getCountPiecesOnBoard())) {
			if (one.equals(board)) {
				return true;
			}
		}
		return false;
	}

	
	@Override
	public BoardHistory clone() {
		return new BoardHistory(this.boards);
	}	

}