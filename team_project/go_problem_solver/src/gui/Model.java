package gui;

import java.io.File;
import java.io.FileNotFoundException;

import player_utils.BoardHistory;
import player_utils.GoSolverAlgorithm;
import player_utils.GoSolverAlgorithmChooser;
import player_utils.LegalMovesChecker;
import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Player;
import board_utils.Stone;
import custom_java_utils.CheckFailException;

public class Model {
	
	public static final String ALPHABETASTRING = "Alpha Beta";
	public static final String MINIMAXSTRING = "Mini Max";
	public static final String MONTECARLOSTRING = "Monte Carlo";
	public static final String HUMANSTRING = "Human";
	public static final String COMPUTERSTRING = "Computer";
	public static final String BlACKSTRING = "Black";
	public static final String WHITESTRING = "White";
	
	private GoPlayingBoard currentBoard;
	private LegalMovesChecker checker;
	private boolean[][] legalMoves;
	private BoardHistory history;
	private GoSolverAlgorithmChooser algorithmChooser;
	private GoSolverAlgorithm algorithm;
	
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
		else {
			currentBoard = new GoPlayingBoard(filename.getAbsolutePath());
			gui.setPlayersColours(currentBoard.getFirstPlayerColour());
		}
		history = BoardHistory.getSingleton();
		checker = new LegalMovesChecker(currentBoard);
		BoardHistory.wipeHistory();
		history.add(currentBoard);
		if(board != null)
			currentBoard = board.clone();
		legalMoves = checker.getLegalityArray();
	}
	
	public void start() {
		if(currentBoard.isNextPlayerComputer()) 
			computerMove();
	}
	
	public void addStone(int x, int y) {
		currentBoard.setCellAt(x, y, new GoCell(currentBoard.toPlayNext(), x, y));
		currentBoard.oppositeToPlayNext();
		checker = new LegalMovesChecker(currentBoard);
		legalMoves = checker.getLegalityArray();
		removeOpponent(x, y);
		history.add(currentBoard);
		gui.paintImmediately(0, 0, gui.getSize().width, gui.getSize().height);
		if(currentBoard.isNextPlayerComputer()){
			computerMove();
		}
	}
	
	public void computerMove() {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (currentBoard.toPlayNext().equals(currentBoard.getFirstPlayer().colour))
			algorithmChooser = new GoSolverAlgorithmChooser(currentBoard, currentBoard.getTarget(), 
					currentBoard.getFirstPlayerAlgorithmName());
		else
			algorithmChooser = new GoSolverAlgorithmChooser(currentBoard, currentBoard.getTarget(), 
					currentBoard.getSecondPlayerAlgorithmName());
		GoCell decision = null;
		try {
			if (currentBoard.getTarget() != null) {
				algorithm = algorithmChooser.getAlgorithm();
				if (algorithm != null) {
					decision = algorithm.decision();
					if(decision != null)
						System.out.println(decision.getVerticalCoordinate() + " " + 
								decision.getHorizontalCoordinate() + " " + decision);
					else
						System.out.println("Please select an algorithm");
				}
				else
					setTextField("Could not make decision");
			} 
			else setTextField("Please select a target");
		} catch(Exception e){
			System.out.println("Game is finished.");
			e.printStackTrace();
		}
		if(decision != null) {
			addStone(decision.getVerticalCoordinate(), decision.getHorizontalCoordinate());
		}
	}
	
	public boolean isMoveLegal(int x, int y) {
		if(legalMoves != null){
			return legalMoves[x][y];
		} else 
			return true;
	}
	
	public void removeOpponent(int x, int y)  {
		try {
			if(checker.captureOponent(currentBoard.getCellAt(x, y)) != null) {
				currentBoard = checker.getNewBoard();
				checker = new LegalMovesChecker(currentBoard);
				legalMoves = checker.getLegalityArray();
			}
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
	
	/**
	 * Return the opposite colour when called
	 * @return Opposite colour from input
	 */
	public Object getOppositeColour(Object c) {
		String clr = (String) c;
		if (clr.equals(Model.BlACKSTRING))
			return Model.WHITESTRING;
		else
			return Model.BlACKSTRING;
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
	
	
	// TODO Distinguish algorithm choices between players
	public void setPlayer1AlgorithmName(String name) {
		currentBoard.setFirstPlayerAlgorithmName(name);;
	}
	
	public void setPlayer2AlgorithmName(String name) {
		currentBoard.setSecondPlayerAlgorithmName(name);;
	}
	
	public void setTextField(String text) {
		gui.setTextField(text);
	}
	
	
	public void undoMove() {
		history.undoMove();
		GoPlayingBoard last = history.getLastMove();
		currentBoard = last;
		checker = new LegalMovesChecker(currentBoard);
		legalMoves = checker.getLegalityArray();
	}
	
	public void redoMove() {
		history.redoMove();
		currentBoard = history.getLastMove();
	}
	

}
