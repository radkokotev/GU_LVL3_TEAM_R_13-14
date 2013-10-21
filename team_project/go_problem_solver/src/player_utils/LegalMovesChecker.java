/**
 * 
 */
package player_utils;

import java.util.Set;
import java.util.TreeSet;

import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;

/**
 * A class holding the functionality to determine if a given move is 
 * legal or not.
 */
public class LegalMovesChecker {
	private BoardHistory history;
	private GoPlayingBoard newBoard;
	
	/**
	 * Creates an instance of the class and makes an internal deep copy of
	 * the provided board
	 * @param board the board to test for legal moves
	 */
	public LegalMovesChecker(GoPlayingBoard board) {
		this.newBoard = board.clone();
		this.history = BoardHistory.getSingleton();
	}
	
	/**
	 * Determines whether the given cell is a legal move on the current board
	 * @param cell the move to be made
	 * @return null if the move is illegal, the new board otherwise.
	 */
	public boolean isMoveLegal(GoCell cell) {
		if (newBoard.getCellAt(cell.x(), cell.y()) == null) {
			return false;
		}
		if (!newBoard.getCellAt(cell.x(), cell.y()).isEmpty()) {
			return false;
		}
		newBoard.setCellAt(cell.x(), cell.y(), cell);
		if (!captureOponent(cell)) {
			if (getLiberties(cell) == 0) {
				return false;
			}
		}
		if (this.history.hasBeenPlayed(newBoard)) {
			return false;
		}
		return true;
	}

	/**
	 * Removes and all opponent stones (if any) that have no liberties left 
	 * after this move.
	 * @param cell the new move that is being made
	 * @return true if there were opponent stones that were killed, 
	 * false - otherwise  
	 */
	public boolean captureOponent(GoCell cell) {
		boolean captured = false;
		for (GoCell neighbour : this.newBoard.getNeighboursOf(cell)) {
			if (neighbour != null && GoCell.areOposite(cell, neighbour)) {
				if (getLiberties(neighbour) == 0) {
					removeOponentsStone(neighbour, neighbour.getContent());
					captured = true;
				}
			}
		}
		return captured;
	}

	/**
	 * Determines the number of liberties that the group of the given cell has 
	 * @param cell a stone whose group is tested for liberties
	 * @return the number of liberties the group has.
	 */
	public int getLiberties(GoCell cell) {
		Set<GoCell> liberties = new TreeSet<GoCell>();
		Set<GoCell> visited = new TreeSet<GoCell>();
		visited.add(cell);
		getLibertiesRecursively(cell, liberties, visited);
		return liberties.size();
	}
	
	/**
	 * Recursively counts the number of empty cells that are adjacent to the group
	 * @param cell a member of a group
	 * @param liberties a set of already found empty cells
	 * @param visited a set of all visited cells
	 * @return the number of empty cells adjacent to the group.
	 */
	private int getLibertiesRecursively(GoCell cell, Set<GoCell> liberties, Set<GoCell> visited) {
		for (GoCell neighbour : newBoard.getNeighboursOf(cell)) {
			if (neighbour != null && !visited.contains(neighbour) && 
					neighbour.getContent() == cell.getContent()) {
				visited.add(neighbour.clone());
				getLibertiesRecursively(neighbour, liberties, visited);
			} else if (neighbour != null && neighbour.isEmpty()) {
				liberties.add(neighbour.clone());
			}
		}
		return liberties.size();
	}

	/**
	 * Recursively convert all cells of the group to empty cells.
	 * @param cell a cell that is a member of that group
	 * @param stone the type of stone that is to be removed
	 */
	private void removeOponentsStone(GoCell cell, Stone stone) {
		newBoard.setCellAt(cell.x(), cell.y(), new GoCell(Stone.NONE, cell.x(), cell.y()));
		for (GoCell neighbour : newBoard.getNeighboursOf(cell)) {
			if (neighbour != null && !neighbour.isEmpty() && 
					neighbour.getContent() == stone) {
				removeOponentsStone(neighbour, stone);
			}
		}
	}
}
