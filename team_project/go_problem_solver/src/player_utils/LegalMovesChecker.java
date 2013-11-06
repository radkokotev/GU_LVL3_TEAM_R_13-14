/**
 * 
 */
package player_utils;

import java.util.Set;
import java.util.TreeSet;

import custom_java_utils.CheckFailException;
import custom_java_utils.CheckUtils;

import board_utils.Cell;
import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;

/**
 * A class holding the functionality to determine if a given move is 
 * legal or not.
 */
public class LegalMovesChecker implements LegalityChecker{
	private BoardHistory history;
	private GoPlayingBoard newBoard;
	private GoPlayingBoard originalBoard;
	
	/**
	 * Creates an instance of the class and makes an internal deep copy of
	 * the provided board
	 * @param board the board to test for legal moves
	 */
	public LegalMovesChecker(GoPlayingBoard board) {
		this.newBoard = board.clone();
		this.originalBoard = board.clone();
		this.history = BoardHistory.getSingleton();
	}
	
	@Override
	public boolean isMoveLegal(Cell<?> c) {
		GoCell cell = (GoCell) c;
		if (newBoard.getCellAt(cell.x(), cell.y()) == null) {
			return false;
		}
		if (!newBoard.getCellAt(cell.x(), cell.y()).isEmpty()) {
			return false;
		}
		newBoard.setCellAt(cell.x(), cell.y(), cell);
		if (!captureOponent(cell)) {
			if (getLiberties(cell) == 0) {
				this.reset();
				return false;
			}
		}
		if (this.history.hasBeenPlayed(newBoard)) {
			this.reset();
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
			// Go through all non-empty neighbouring cells from the same color (to traverse 
			// the whole group) and if the cell is empty add it to the liberties set.
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
	
	@Override
	public GoPlayingBoard getNewBoard() throws CheckFailException {
		CheckUtils.checkNotEqual(this.originalBoard, this.newBoard);
		return this.newBoard.clone();
	}
	
	@Override
	public void reset() {
		this.newBoard = this.originalBoard.clone();
	}
	
	@Override
	public boolean[][] getLegalityArray() {
		boolean[][] legalityArray = 
				new boolean[this.originalBoard.getHeight()][this.originalBoard.getWidth()];
		Stone stone = this.originalBoard.toPlayNext();
		for (int i = 0; i < this.originalBoard.getHeight(); i++) {
			for (int j = 0; j < this.originalBoard.getWidth(); j++) {
				legalityArray[i][j] = this.isMoveLegal(new GoCell(stone, i, j));
				this.reset();
			}
		}
		return legalityArray;
	}
}
