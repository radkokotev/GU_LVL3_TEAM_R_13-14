package board_utils;

/**
 * An interface to specify expected generic behaviour of a playing board
 *
 * @param <E>
 */
public interface PlayingBoard<E> {
	/**
	 * @return the width of the playing board
	 */
	public int getWidth();
	
	/**
	 * @return the height of the playing board
	 */
	public int getHeight();
	
	/**
	 * @return the number of pieces currently on the board
	 */
	public int getPiecesOnBoard();
	
	/**
	 * Performs a move by placing the given piece on the playing board.
	 * @param horizontalCoord x coordinates of the cell
	 * @param verticalCoord y coordinates of the cell
	 * @param piece the piece to be placed at the specified cell position
	 * @return true if move is possible, false - otherwise.
	 */
	boolean move(int horizontalCoord, int verticalCoord, E piece);
}
