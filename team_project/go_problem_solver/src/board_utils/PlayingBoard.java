package board_utils;

/**
 * An abstract class to specify expected generic behaviour of a playing board
 *
 * @param <E>
 */
public abstract class PlayingBoard<E extends Cell<?>> {
	/**
	 * @return the width of the playing board
	 */
	public abstract int getWidth();
	
	/**
	 * @return the height of the playing board
	 */
	public abstract int getHeight();
	
	/**
	 * @return the number of pieces currently on the board
	 */
	public abstract int getCountPiecesOnBoard();
	
	/**
	 * A method to access the content of a given cell
	 * @param x horizontal coordinates of the cell
	 * @param y vertical coordinates of the cell
	 * @return the content of the specified cell
	 */
	public abstract E getCellAt(int x, int y);
	
	/**
	 * A method to update the content of the given cell
	 * @param x horizontal coordinates of the cell
	 * @param y vertical coordinates of the cell
	 * @param content the content of the specified cell
	 */
	public abstract void setCellAt(int x, int y, E content);
	
	// TODO add a method to update the board (e.g. complete changes or just a simple move) 
}
