package board_utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import custom_java_utils.CheckFailException;
import custom_java_utils.CheckUtils;

public class GoPlayingBoard extends PlayingBoard<GoCell> {
	private GoCell[][] board;
	private Stone toPlayNext;
	private Player nextPlayer;
	private int countPiecesOnBoard;
	private int blackStones;
	private GoCell target = null;
	
	// Board constants
	private static final int WIDTH = 19;
	private static final int HEIGHT = 19;
	
	// Characters used for representing types of stones in a text file 
	private static final char BLACK = 'x';
	private static final char WHITE = 'o';
	private static final char NONE = '-';
	
	// Who is playing first from file?
	private static final String FIRST_IS_WHITE = "WHITE";
	private static final String FIRST_IS_BLACK = "BLACK";
	private static final String HUMAN = "HUMAN";
	private static final String COMPUTER = "COMPUTER";
	
	/**
	 * Creates an empty Go playing board
	 */
	public GoPlayingBoard() {
		board = new GoCell[HEIGHT][WIDTH];
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				board[i][j] = new GoCell(Stone.NONE, i, j);
			}
		}
		toPlayNext = Stone.BLACK;
		countPiecesOnBoard = 0;
		blackStones = 0;
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
		
		//reading first line
		String firstline = fileScanner.nextLine();
		String[] fileArgs = firstline.split(" ");
		if(fileArgs[0].equals(FIRST_IS_WHITE))
			toPlayNext = Stone.WHITE;
		else if(fileArgs[0].equals(FIRST_IS_BLACK))
			toPlayNext = Stone.BLACK;
		
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
		if(!firstline.equals("")) {
			target = getCellAt(Integer.valueOf(fileArgs[3]), Integer.valueOf(fileArgs[4])).clone();
			if(fileArgs[1].equals(COMPUTER)){
				nextPlayer = Player.COMPUTER;
			} else if(fileArgs[1].equals(HUMAN)){
				nextPlayer = Player.HUMAN;
			}
		} else 
			nextPlayer = Player.COMPUTER;
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
	 * A setter for the value of toPlayNext. The new value would be of the opposite colour.
	 */
	public void oppositeToPlayNext() {
		this.toPlayNext = this.toPlayNext == Stone.BLACK ? Stone.WHITE : Stone.BLACK;
	}
	
	public Player getNextPlayer(){
		return nextPlayer;
	}
	
	public void oppositePlayer(){
		nextPlayer = nextPlayer == Player.HUMAN ? Player.COMPUTER : Player.HUMAN;
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
	
	public int getNumberofBlackStones() {
		return blackStones;
	}
	
	public int getNumberOfWhiteStones() {
		return (countPiecesOnBoard - blackStones);
	}
	
	public void countAndSetBlackStones() {
		int count = 0;
		for (int y = 0; y < 19; y++)
			for (GoCell x: board[y])
				if (x.getContent().equals(Stone.BLACK))
					count++;
		blackStones = count;
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
			if (content.getContent().equals(Stone.BLACK))
				blackStones++;
		} else if (!this.board[x][y].isEmpty() && content.isEmpty()) {
			this.countPiecesOnBoard--;
			if(board[x][y].getContent() == Stone.BLACK)
				blackStones--;
		}
		
		this.board[x][y] = content.clone();
	}
	
	public GoCell getTarget(){
		if (target != null)
			return target.clone();
		else
			return null;
	}
	
	public void setTarget(Stone content, int x, int y){
		target = new GoCell(content, x, y);
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
		other.blackStones = this.blackStones;
		other.target = this.target;
		other.nextPlayer = this.nextPlayer;
		return other;
	}
	
	
	public void exportBoard(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		
	}
	/**
	 * Creates a new file and populates it with this board.
	 * @param file full path of the file where to save it
	 * @throws FileNotFoundException 
	 */
	public void toFile(File file) throws FileNotFoundException{
		PrintWriter writer = new PrintWriter(file.toString());
		writer.printf("BLACK COMPUTER KILL %d %d\r\n", target.x(), target.y());
		for (GoCell[] row : board) {
			for (GoCell cell : row) {
				String c = "";
				switch (cell.getContent()) {
					case BLACK : c = "x"; break;
					case WHITE : c = "o"; break;
					case NONE : c = "-"; break;
					default : c = "?";
				}
				writer.print(c);
			}
			writer.println();
		}
		writer.close();
	}
	
	public GoCell[][] getBoard() {
		return this.board;
	}
}
