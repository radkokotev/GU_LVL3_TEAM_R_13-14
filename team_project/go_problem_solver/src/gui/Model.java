package gui;

import java.io.File;
import java.io.FileNotFoundException;

import player_utils.BoardHistory;
import player_utils.LegalMovesChecker;
import player_utils.MinimaxGoSolver;
import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Player;
import board_utils.Stone;
import custom_java_utils.CheckFailException;

public class Model {
	
	public static final String HUMANSTRING = "Human";
	public static final String COMPUTERSTRING = "Computer";
	public static final String BlACKSTRING = "Black";
	public static final String WHITESTRING = "White";
	
	private GoPlayingBoard currentBoard;
	private LegalMovesChecker checker;
	private boolean[][] legalMoves;
	private BoardHistory history;
	private MinimaxGoSolver minimax;
	private GuiBoardPlay gui;
	
	public Model(GuiBoardPlay g) throws FileNotFoundException, CheckFailException {
		this(g, null, null);
	}
	
	public Model(GuiBoardPlay g, File filename) throws FileNotFoundException, CheckFailException {
		this(g, null, filename);
	}
	
	public Model(GuiBoardPlay g, GoPlayingBoard board) throws FileNotFoundException, CheckFailException{
		this(g, board, null);
	}
	
	public Model(GuiBoardPlay g, GoPlayingBoard board, File filename) throws FileNotFoundException, CheckFailException {
		gui = g;
		if(filename == null)
			currentBoard = new GoPlayingBoard();
		else
			currentBoard = new GoPlayingBoard(filename);
		history = BoardHistory.getSingleton();
		history.add(currentBoard);
		checker = new LegalMovesChecker(currentBoard);
		BoardHistory.wipeHistory();
		if(board != null)
			currentBoard = board.clone();
		legalMoves = checker.getLegalityArray();
	}
	
	public void addStone(int x, int y) {
		currentBoard.setCellAt(x, y, new GoCell(currentBoard.toPlayNext(), x, y));
		currentBoard.oppositeToPlayNext();
		checker = new LegalMovesChecker(currentBoard);
		legalMoves = checker.getLegalityArray();
		removeOpponent(x, y);
		if(minimax != null && currentBoard.isNextPlayerComputer() && !minimax.isPositionTerminal(currentBoard)){
			computerMove();
		}
	}
	
	public void computerMove(){
		minimax = new MinimaxGoSolver(currentBoard, currentBoard.getTarget());
		GoCell decision = null;
		try {
			decision = minimax.minimaxDecision();
			if(decision != null)
				System.out.println(decision.x() + " " + decision.y() + " " + decision);
			else
				System.out.println("null");
		} catch(CheckFailException e){
			System.out.println("Game is finished.");
			e.printStackTrace();
		}
		gui.repaint();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(decision != null) {
			addStone(decision.x(), decision.y());
		}
	}
	
	public boolean isMoveLegal(int x, int y) {
		if(legalMoves != null){
			return legalMoves[x][y];
		} else 
			return true;
	}
	
	public void removeOpponent(int x, int y)  {
		boolean isAnyKilled = false;
		isAnyKilled = checker.captureOponent(currentBoard.getCellAt(x, y));
		try {
			if(isAnyKilled) {
				currentBoard = checker.getNewBoard();
				checker = new LegalMovesChecker(currentBoard);
				legalMoves = checker.getLegalityArray();
			}
			else 
				//this method will be called on each move, so history will be updated each time when
				//there will be no stones killed.
				history.add(currentBoard);
		} catch(Exception e){
			System.out.println("new board = old board");
		}
		
	}
	
	public int getTotalNumberOfStones(){
		return currentBoard.getCountPiecesOnBoard();
	}
	
	public int getBlackNumberOfStones(){
		return currentBoard.getNumberofBlackStones();
	}
	
	public int getWhiteNumberOfStones(){
		return currentBoard.getNumberOfWhiteStones();
	}
	
	public void recountBlackStones() {
		currentBoard.countAndSetBlackStones();
	}
	
	/**
	 * Returns the current board Stone layout
	 * @return A double dimension array of Stones
	 */
	public Stone[][] getCurrentBoardLayout() {
		GoCell[][] currentLayout = getCurrentBoard().getBoard();
		Stone[][] newLayout = new Stone[getCurrentBoard().getWidth()][getCurrentBoard().getHeight()];
		for (int i = 0; i < getCurrentBoard().getHeight(); i++)
			for (int j = 0; j < getCurrentBoard().getWidth(); j++)
				newLayout[i][j] = currentLayout[i][j].getContent();
		return newLayout;
	}
	
	/**
	 * Returns a clone of the current board
	 * @return a clone of current board
	 */
	public GoPlayingBoard getCurrentBoard() {
		return currentBoard.clone();
	}
	
	public GoCell getTarget() {
		return currentBoard.getTarget();
	}
	/**
	 * Creates a new file and populates it with current board.
	 * @param file full path of the file where to save it
	 * @throws FileNotFoundException 
	 */
	public void toFile(File file) throws FileNotFoundException{
		currentBoard.toFile(file);
	}
	
	public void setFirstPlayerType(Object b){
		String t = (String) b;
		if(t.equals(HUMANSTRING))
			currentBoard.setFirstPlayerType(Player.Type.HUMAN);
		else if(t.equals(COMPUTERSTRING))
			currentBoard.setFirstPlayerType(Player.Type.COMPUTER);
	}
	
	public void setFirstPlayerColour(Object b){
		String t = (String) b;
		if(t.equals(BlACKSTRING))
			currentBoard.setFirstPlayerColour(Stone.BLACK);
		else if(t.equals(WHITESTRING))
			currentBoard.setFirstPlayerColour(Stone.WHITE);
	}
	
	public void setSecondPlayerType(Object b){
		String t = (String) b;
		if(t.equals(HUMANSTRING))
			currentBoard.setSecondPlayerType(Player.Type.HUMAN);
		else if(t.equals(COMPUTERSTRING))
			currentBoard.setSecondPlayerType(Player.Type.COMPUTER);
	}
	
	public void setSecondPlayerColour(Object b){
		String t = (String) b;
		if(t.equals(BlACKSTRING))
			currentBoard.setSecondPlayerColour(Stone.BLACK);
		else if(t.equals(WHITESTRING))
			currentBoard.setSecondPlayerColour(Stone.WHITE);
	}
	
	public void undoMove() {
		history.undoMove();
		GoPlayingBoard last = history.getLastMove();
		if (last != null) {
			currentBoard = last;
			checker = new LegalMovesChecker(currentBoard);
			legalMoves = checker.getLegalityArray();
		}
	}
	
	public void redoMove() {
		history.redoMove();
		currentBoard = history.getUndoMove();
	}

}
