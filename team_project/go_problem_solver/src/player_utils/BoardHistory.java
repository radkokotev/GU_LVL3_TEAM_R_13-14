package player_utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Stack;

import board_utils.GoPlayingBoard;

/**
 * A singleton to keep the history of all boards that have been played
 */
public class BoardHistory {
	private static BoardHistory instance;
	private static HashMap<Integer, LinkedList<GoPlayingBoard>> boards;
	private Stack<GoPlayingBoard> allMoves;
	private Stack<GoPlayingBoard> undoMoves;

	/**
	 * Default constructor to create an instance of the history
	 */
	private BoardHistory() {
		boards = new HashMap<Integer, LinkedList<GoPlayingBoard>>();
		this.allMoves = new Stack<GoPlayingBoard>();
		this.undoMoves = new Stack<GoPlayingBoard>();
	}

	/**
	 * A private constructor to efficiently construct an identical instance by taking a 
	 * deep copy of the given history
	 * @param boards existing history to clone into the new instance 
	 */
	public static BoardHistory getSingleton() {
		if (instance == null) {
			instance = new BoardHistory();
		}
		return instance;
	}
	/**
	 * Adding the given board to the board history by making a deep copy of it.
	 * @param board the board to be added
	 */
	public void add(GoPlayingBoard board) {
		if (!boards.containsKey(board.getCountPiecesOnBoard())) {
			LinkedList<GoPlayingBoard> list = new LinkedList<GoPlayingBoard>();
			list.add(board.clone());
			boards.put(board.getCountPiecesOnBoard(), list);
			allMoves.add(board);
			System.out.println(board);
			System.out.println(allMoves.size());
		} else {
			LinkedList<GoPlayingBoard> list = boards.get(board
					.getCountPiecesOnBoard());
			list.add(board.clone());
		}
		if (!undoMoves.isEmpty()) {
			GoPlayingBoard toRemove = new GoPlayingBoard();
			while (!undoMoves.isEmpty()) {
				toRemove = undoMoves.pop();
				boards.get(toRemove.getCountPiecesOnBoard()).remove(toRemove);
			}
		}
	}

	/**
	 * Adding the given board to the board history by making a deep copy of it.
	 * @param board the board to be added
	 */
	public void remove(GoPlayingBoard board) {
		if (!boards.containsKey(board.getCountPiecesOnBoard())) {
			return;
		} else {
			LinkedList<GoPlayingBoard> list = boards.get(board
					.getCountPiecesOnBoard());
			list.remove(board);
		}
	}

	/**
	 * Method to undo the board by 1 position
	 */
	public void undoMove() {
		GoPlayingBoard temp = new GoPlayingBoard();
		if (!allMoves.isEmpty()) {
			temp = allMoves.pop();
			undoMoves.push(temp);
			System.out.println(undoMoves.size());
		}
	}
	
	/**
	 * Method to redo the last move
	 */
	public void redoMove() {
		if (!undoMoves.isEmpty()) {
			allMoves.push(undoMoves.pop());		}
	}
	
	/**
	 * Method to get the last move 
	 */
	public GoPlayingBoard getLastMove() {
		if (!allMoves.isEmpty())
			return allMoves.get(0);
		return null;
	}
	
	/**
	 * Method to go forward a move
	 */
	public GoPlayingBoard getUndoMove() {
		return undoMoves.get(0);
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
	
	public static void wipeHistory() {
		instance = null;
	}
}
