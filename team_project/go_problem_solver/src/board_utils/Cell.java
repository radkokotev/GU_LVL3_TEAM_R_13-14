/**
 * 
 */
package board_utils;

/**
 * An interface to guarantee generic cell functionality. 
 * @param <E> is a representation of what a cell may contain.
 */
public interface Cell<E> {
	/**
	 * 
	 * @return true if the cell is not occupied and false otherwise.
	 */
	public boolean isEmpty();
	
	/**
	 * 
	 * @return the horizontal coordinate of the cell on the playing board
	 */
	public int horizontalCoord();
	
	/**
	 * 
	 * @return the vertical coordinate of the cell on the playing board
	 */
	public int verticalCoord();
	
	/**
	 * 
	 * @return the piece that occupies the cell.
	 */
	public E getContent();
}
