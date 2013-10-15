/**
testing
 * 
 */
package board_utils;

/**
 * An abstract class to guarantee generic cell functionality. 
 * @param <E> is an enum representation of what a cell may contain.
 */
public abstract class Cell<E extends Enum<?>> {
	/**
	 * 
	 * @return true if the cell is not occupied and false otherwise.
	 */
	public abstract boolean isEmpty();
	
	/**
	 * 
	 * @return the piece that occupies the cell.
	 */
	public abstract E getContent();
}
