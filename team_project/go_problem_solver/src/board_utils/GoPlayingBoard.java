package board_utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import custom_java_utils.CheckFailException;
import custom_java_utils.CheckUtils;

public class GoPlayingBoard extends PlayingBoard<GoCell> {
	private GoCell[][] board;
	private Player toPlayNext;
	private Player firstPlayer;
	private Player secondPlayer;
	private int blackStones;
	private int whiteStones;
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
		
		blackStones = 0;
		whiteStones = 0;
		firstPlayer = new Player();
		secondPlayer = new Player(Stone.WHITE);
		toPlayNext = firstPlayer;
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
			toPlayNext.colour = Stone.WHITE;
		else if(fileArgs[0].equals(FIRST_IS_BLACK))
			toPlayNext.colour = Stone.BLACK;
		
		for (int i = 0; fileScanner.hasNext(); i++) {
			// TODO what happens if there are less than 19 lines in the file?
			CheckUtils.checkLess(i, HEIGHT);
			String line = fileScanner.nextLine();
			CheckUtils.checkEqual(line.length(), WIDTH);
			for (int j = 0; j < WIDTH; j++) {
				Stone stone;
				switch (line.charAt(j)) {
					case (BLACK) : stone = Stone.BLACK; blackStones++; break;
					case (WHITE) : stone = Stone.WHITE; whiteStones++; break;
					case (NONE) : stone = Stone.NONE; break;
					default : stone = Stone.INNER_BORDER;
				}
				this.board[i][j].setContent(stone);
			}
		}
		if(!firstline.equals(""))
			target = getCellAt(Integer.valueOf(fileArgs[2]), Integer.valueOf(fileArgs[3])).clone();
		
		fileScanner.close();
	}
	
	/**
	 * @return the colour of the stone to be placed next.
	 */
	public Stone toPlayNext() {
		return toPlayNext.colour;
	}
	
	/**
	 * A setter for the value of who is to play next.
	 */
	public void setToPlayNext(Stone stone) {
		toPlayNext.colour = stone;
	}
	
	/**
	 * A setter for the value of toPlayNext. The new value would be of the opposite colour.
	 */
	public void oppositeToPlayNext() {
		toPlayNext = toPlayNext == firstPlayer ? secondPlayer : firstPlayer;
	}
	
	public Player getFirstPlayer(){
		return firstPlayer;
	}
	
	public Player getNextPlayer(){
		return toPlayNext;
	}
	
	public boolean isNextPlayerComputer(){
		return toPlayNext.type == Player.Type.COMPUTER;
	}
	
	/**
	 * Gets the neighbouring cells to the given one
	 * @param cell the central cell
	 * @return array of 4 cells, that may contain null values if some 
	 * cell is out of bounds
	 */
	public GoCell[] getNeighboursOf(GoCell cell) {
		GoCell[] neighbours = new GoCell[4];
		neighbours[0] = this.getCellAt(cell.getVerticalCoordinate() - 1, cell.getHorizontalCoordinate());
		neighbours[1] = this.getCellAt(cell.getVerticalCoordinate() + 1, cell.getHorizontalCoordinate());
		neighbours[2] = this.getCellAt(cell.getVerticalCoordinate(), cell.getHorizontalCoordinate() - 1);
		neighbours[3] = this.getCellAt(cell.getVerticalCoordinate(), cell.getHorizontalCoordinate() + 1);
		return neighbours;
	}
	
	/**
	 * @param cell the central cell
	 * @return array of all eight cells which are around given cell, that may 
	 * contain null values if some are outside of bounds
	 */
	public GoCell[] getCloseCellsOf(GoCell cell) {
		GoCell[] allAround = new GoCell[8];
		GoCell[] neighbours = getNeighboursOf(cell);
		for(int i = 0; i < neighbours.length; i++)
			allAround[i] = neighbours[i];
		allAround[4] = this.getCellAt(cell.getVerticalCoordinate() - 1, cell.getHorizontalCoordinate() - 1);
		allAround[5] = this.getCellAt(cell.getVerticalCoordinate() + 1, cell.getHorizontalCoordinate() + 1);
		allAround[6] = this.getCellAt(cell.getVerticalCoordinate() + 1, cell.getHorizontalCoordinate() - 1);
		allAround[7] = this.getCellAt(cell.getVerticalCoordinate() - 1, cell.getHorizontalCoordinate() + 1);
		return allAround;
	}
	
	/**
	 * @param cell Cell from the group you want to find.
	 * @param set of visited cells
	 * @return array of group cells which are adjacent to given cell and same as 
	 * colour as given cell
	 */
	public ArrayList<GoCell> findGroupOf(GoCell cell, Set<GoCell> visited){
		ArrayList<GoCell> group = new ArrayList<GoCell>();
		group.add(cell);
		visited.add(cell);
		for(GoCell neighbour : getNeighboursOf(cell)){
			if(neighbour != null && neighbour.getContent() == cell.getContent()
					&& !visited.contains(neighbour)) 
				group.addAll(findGroupOf(neighbour, visited));
		}
		return group;
	}
	
	public ArrayList<GoCell> getCloseCellsOfGroup(GoCell cell){
		ArrayList<GoCell> group = findGroupOf(cell, new TreeSet<GoCell>());
		ArrayList<GoCell> closeCells = new ArrayList<GoCell>();
		for(GoCell groupMember : group){
			for(GoCell closeCell : getCloseCellsOf(groupMember))
				if(closeCell != null && !group.contains(closeCell)
						&& !closeCells.contains(closeCell))
					closeCells.add(closeCell);
		}
		return closeCells;
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
		return blackStones + whiteStones;
	}
	
	public int getNumberofBlackStones() {
		return blackStones;
	}
	
	public int getNumberOfWhiteStones() {
		return whiteStones;
	}
	
	public int getNumberOfOpponentStones() {
		return toPlayNext.colour == Stone.BLACK ? blackStones : whiteStones;
	}
	
	public int getNumberOfOwnStones() {
		return toPlayNext.colour == Stone.BLACK ? whiteStones : blackStones;
	}
	
	public void countAndSetBlackStones() {
		int count = 0;
		for (int y = 0; y < 19; y++)
			for (GoCell x: board[y])
				if (x.getContent().equals(Stone.BLACK))
					count++;
		blackStones = count;
	}
	
	public Stone getFirstPlayerColour() {
		return firstPlayer.colour;
	}
	
	public String getFirstPlayerAlgorithmName() {
		return firstPlayer.algorithmName;
	}
	
	public String getSecondPlayerAlgorithmName() {
		return secondPlayer.algorithmName;
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
		
		if (content.getContent().equals(Stone.BLACK))
			blackStones++;
		else if(content.getContent().equals(Stone.WHITE))
			whiteStones++;

		if(board[x][y].getContent() == Stone.BLACK)
			blackStones--;
		else if(board[x][y].getContent() == Stone.WHITE)
			whiteStones--;
		
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
	
	public void setFirstPlayerType(Player.Type t){
		firstPlayer.type = t;
	}
	
	public void setFirstPlayerColour(Stone t){
		firstPlayer.colour = t;
	}
	
	public void setFirstPlayerAlgorithmName(String name){
		firstPlayer.algorithmName = name;
	}
	
	public void setSecondPlayerType(Player.Type t){
		secondPlayer.type = t;
	}
	
	public void setSecondPlayerColour(Stone t){
		secondPlayer.colour = t;
	}
	
	public void setSecondPlayerAlgorithmName(String name){
		secondPlayer.algorithmName = name;
	}
	
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				if (this.board[i][j].getContent() == Stone.BLACK) {
					result += BLACK;
				} else if (this.board[i][j].getContent() == Stone.WHITE) {
					result += WHITE;
				} else if (this.board[i][j].getContent() == Stone.NONE) {
					result += NONE;
				} else {
					result += '?';
				}
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
		other.toPlayNext = this.toPlayNext;
		other.firstPlayer = this.firstPlayer;
		other.secondPlayer = this.secondPlayer;
		other.blackStones = this.blackStones;
		other.whiteStones = this.whiteStones;
		other.target = this.target;
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
		writer.printf("BLACK COMPUTER KILL %d %d\r\n", target.getVerticalCoordinate(), target.getHorizontalCoordinate());
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
