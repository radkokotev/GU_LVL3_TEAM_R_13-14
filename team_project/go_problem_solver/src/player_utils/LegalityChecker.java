/**
 * 
 */
package player_utils;

import custom_java_utils.CheckFailException;
import board_utils.Cell;
import board_utils.PlayingBoard;

/**
 * A generic overview of what the expected functionality of a legal moves checker is. 
 */
public interface LegalityChecker {
	
	/**
	 * Determines whether the given cell is a legal move on the current board
	 * @param cell the move to be made
	 * @return 0 if move is illegal due to KO rule violation, -1 if illegal for other 
	 * reasons and 1 if the given move is legal 
	 */
	public int isMoveLegal(Cell<?> cell);
	
	/**
	 * Get the board that is produced after the move is made.
	 * @return a deep copy (clone) of the new board
	 * @throws CheckFailException if the new board is equal to the original board, which
	 * indicates that no move has been played.
	 */
	public PlayingBoard<?> getNewBoard() throws CheckFailException;
	
	/**
	 * Wipes all changes that were made to the inner structure of the object instance.
	 * Reverts the instance to its initial state.
	 */
	public void reset();
	
	/**
	 * Get a boolean representation of the board, representing where is legal to play.
	 * @return two dimensional array of booleans representing true if a move is legal
	 * at that position and false otherwise.
	 * @throws CheckFailException if the new board is equal to the original board, which
	 * indicates that no move has been played.
	 */
	public boolean[][] getLegalityArray() throws CheckFailException;
}
