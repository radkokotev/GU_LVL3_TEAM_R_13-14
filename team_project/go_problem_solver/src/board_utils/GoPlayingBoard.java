package board_utils;

public class GoPlayingBoard implements PlayingBoard<Stone> {
	private static final int WIDTH = 19;
	private static final int HEIGHT = 19;
	private GoCell[][] board;
	private Stone toPlayNext;
	private int piecesOnBoard;
	
	public GoPlayingBoard() {
		this.board = new GoCell[WIDTH][HEIGHT];
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				this.board[i][j] = new GoCell(i, j);
			}
		}
		this.toPlayNext = Stone.BLACK;
		this.piecesOnBoard = 0;
	}
	
	/**
	 * @return the color of the stone to be placed next.
	 */
	public Stone toPlayNext() {
		return this.toPlayNext;
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	@Override
	public int getPiecesOnBoard() {
		return this.piecesOnBoard;
	}

	@Override
	public boolean move(int horizontalCoord, int verticalCoord, Stone piece) {
		// TODO implement this
		return false;
	}
}
