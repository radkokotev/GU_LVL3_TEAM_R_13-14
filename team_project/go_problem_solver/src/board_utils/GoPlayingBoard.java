package board_utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import player_utils.BoardHistory;

import custom_java_utils.CheckFailException;
import custom_java_utils.CheckUtils;

public class GoPlayingBoard extends PlayingBoard<GoCell> {
	private GoCell[][] board;
	private Stone toPlayNext;
	private int countPiecesOnBoard;
	private BoardHistory history;
	
	// Board constants
	private static final int WIDTH = 19;
	private static final int HEIGHT = 19;
	
	// Characters used for representing types of stones in a text file 
	private static final char BLACK = 'x';
	private static final char WHITE = 'o';
	private static final char NONE = '-';
	
	/**
	 * Creates an empty Go playing board
	 */
	public GoPlayingBoard() {
		this.board = new GoCell[HEIGHT][WIDTH];
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				this.board[i][j] = new GoCell(Stone.NONE, i, j);
			}
		}
		this.toPlayNext = Stone.BLACK;
		this.countPiecesOnBoard = 0;
		this.history = new BoardHistory();
	}
	
	/**
	 * Creates a Go playing board from a file.
	 * @param fileName name of file to read from
	 * @throws FileNotFoundException when file does not exist
	 * @throws CheckFailException when the given file does not have the 
	 * dimensions of a Go playing board
	 */
	public GoPlayingBoard(String fileName) 
			throws FileNotFoundException, CheckFailException {
		this();
		FileInputStream inputStream = new FileInputStream(fileName);
		Scanner fileScanner = new Scanner(inputStream);
		for (int i = 0; fileScanner.hasNext(); i++) {
			// TODO what happens if there are less than 19 lines in the file?
			CheckUtils.checkLess(i, HEIGHT);
			String line = fileScanner.nextLine();
			CheckUtils.checkEqual(line.length(), WIDTH);
			for (int j = 0; j < WIDTH; j++) {
				Stone stone;
				switch (line.charAt(j)) {
					case (BLACK) : stone = Stone.BLACK; countPiecesOnBoard++; break;
					case (WHITE) : stone = Stone.WHITE; countPiecesOnBoard++; break;
					case (NONE) : stone = Stone.NONE; break;
					default : stone = Stone.INNER_BORDER;
				}
				this.board[i][j].setContent(stone);
			}
		}
		fileScanner.close();
	}
	
	/**
	 * @return the colour of the stone to be placed next.
	 */
	public Stone toPlayNext() {
		return this.toPlayNext;
	}
	
	/**
	 * A setter for the value of who is to play next.
	 */
	public void setToPlayNext(Stone stone) {
		this.toPlayNext = stone;
	}
	
	/**
	 * Gets the neighbouring cells to the given one
	 * @param cell the central cell
	 * @return array of 4 cells, that may contain null values if some 
	 * cell is out of bounds
	 */
	public GoCell[] getNeighboursOf(GoCell cell) {
		GoCell[] neighbours = new GoCell[4];
		neighbours[0] = this.getCellAt(cell.x() - 1, cell.y());
		neighbours[1] = this.getCellAt(cell.x() + 1, cell.y());
		neighbours[2] = this.getCellAt(cell.x(), cell.y() - 1);
		neighbours[3] = this.getCellAt(cell.x(), cell.y() + 1);
		return neighbours;
	}
	
	/**
	 * Getter for the board history instance.
	 * @return a shallow copy of the board history instance for this board
	 */
	public BoardHistory getHistory() {
		return this.history;
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
	public int getCountPiecesOnBoard() {
		return this.countPiecesOnBoard;
	}

	@Override
	public GoCell getCellAt(int x, int y) {
		if (x < 0 || x >= HEIGHT || y < 0 || y >= WIDTH) {
			return null;
		}
		return this.board[x][y].clone();
	}
	
	@Override
	public void setCellAt(int x, int y, GoCell content) {
		if (this.board[x][y].isEmpty() && !content.isEmpty()) {
			this.countPiecesOnBoard++;
		} else if (!this.board[x][y].isEmpty() && content.isEmpty()) {
			this.countPiecesOnBoard--;
		}
		
		this.board[x][y] = content.clone();
	}
	
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				result += this.board[i][j].toString() + "\t";
			}
			result += "\n";
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) { 
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		
		GoPlayingBoard other = (GoPlayingBoard) obj;
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				if (!this.board[i][j].equals(other.board[i][j])) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public GoPlayingBoard clone() {
		GoPlayingBoard other = new GoPlayingBoard();
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				other.board[i][j] =this.board[i][j];
			}
		}
		other.countPiecesOnBoard = this.countPiecesOnBoard;
		other.toPlayNext = this.toPlayNext;
		other.history = this.history.clone();
		return other;
	}
	
	
	public GoCell[][] getBoard() {
		return this.board;
	}
}
