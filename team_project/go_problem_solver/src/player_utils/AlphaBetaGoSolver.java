package player_utils;

import java.util.ArrayList;

import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import custom_java_utils.CheckFailException;

public class AlphaBetaGoSolver implements GoSolverAlgorithm{
	private GoPlayingBoard board;
	private GoCell cellToCapture;
	private static final long infinity = Integer.MAX_VALUE;
	private long countLeafNodesDiscovered;
	private int playSurviveCoef;
	private boolean minPassed = false;

	
	public AlphaBetaGoSolver(GoPlayingBoard board, GoCell cell) {
		this.board = board.clone();
		this.cellToCapture = cell.clone();
		this.countLeafNodesDiscovered = 0;
		this.setPlaySurviveCoef();
	}
	
	private void setPlaySurviveCoef() {
		if (this.board.getFirstPlayer().colour == this.cellToCapture.getContent()) {
			playSurviveCoef = -1;
		} else {
			playSurviveCoef = 1;
		}
	}
	
	public boolean isPositionTerminal(GoPlayingBoard board) {
		if (board.getCellAt(cellToCapture.getVerticalCoordinate(), 
				cellToCapture.getHorizontalCoordinate()).isEmpty()) {
			// the target stone was captured
			this.countLeafNodesDiscovered++;
			return true;
		}
		LegalMovesChecker checker = new LegalMovesChecker(board);
		for (int i = 0; i < board.getWidth(); i++) {
			for (int j = 0; j < board.getHeight(); j++) {
				if (checker.isMoveLegal(new GoCell(board.toPlayNext(), i, j))) {
					checker.reset();
					return false;  // there is a legal move
				}
				checker.reset();
			}
		}
		this.countLeafNodesDiscovered++;
		return true;
	}
	
	public GoCell decision() throws CheckFailException {
		LegalMovesChecker checker = new LegalMovesChecker(board);
		ArrayList<CellValuePair> decisionMinimaxValues = 
				new ArrayList<CellValuePair>();
		
		GoodMovesFinder finder = new GoodMovesFinder(board.clone());
		for (GoCell cell : finder.getGoodMoves()) {
			if (checker.isMoveLegal(cell)) {
				CellValuePair cellValuePair = new CellValuePair();
				cellValuePair.cell = cell;
				GoPlayingBoard newBoard = checker.getNewBoard();
				newBoard.oppositeToPlayNext();
				cellValuePair.value = minimize(newBoard, -infinity, infinity);
				decisionMinimaxValues.add(cellValuePair);
				BoardHistory.getSingleton().remove(newBoard);
			}
			checker.reset();
		}
		GoCell bestMove = null;
		long bestValue = Long.MIN_VALUE;
		for (CellValuePair pair : decisionMinimaxValues) {
			System.out.println("pair" + pair.value + " (" + pair.cell.getVerticalCoordinate() +
					"," + pair.cell.getHorizontalCoordinate());
			if (pair.value > bestValue) {
				bestValue = (long) pair.value;
				bestMove = pair.cell;
			}
		}
		System.out.println("Count Leaf nodes = " + this.countLeafNodesDiscovered);
		return bestMove;
	}
	
	private long maximize(GoPlayingBoard board, long alpha, long beta) throws CheckFailException {
		if (isPositionTerminal(board)) {
			if (board.getCellAt(cellToCapture.getVerticalCoordinate(), 
					cellToCapture.getHorizontalCoordinate()).isEmpty()) {
				return playSurviveCoef * infinity;
			}
			return playSurviveCoef * (-infinity);
		}
		if (minPassed) {
			if (playSurviveCoef == -1 && 
					!board.getCellAt(cellToCapture.getVerticalCoordinate(), 
							cellToCapture.getHorizontalCoordinate()).isEmpty()) {
				return infinity;
			}
		}
		LegalMovesChecker checker = new LegalMovesChecker(board);
		GoodMovesFinder finder = new GoodMovesFinder(board.clone());
		for (GoCell cell : finder.getGoodMoves()) {
			if (checker.isMoveLegal(cell)) {
				GoPlayingBoard newBoard = checker.getNewBoard();
				newBoard.oppositeToPlayNext();
				alpha = Math.max(alpha, minimize(newBoard, alpha, beta));
				BoardHistory.getSingleton().remove(newBoard);
				if (alpha >= beta) {
					break;
				}
			}
			checker.reset();
		}

		return alpha;
	}
	
	private long minimize(GoPlayingBoard board, long alpha, long beta) throws CheckFailException {
		if (isPositionTerminal(board)) {
			board.oppositeToPlayNext();
			minPassed = true;
			return maximize(board, alpha, beta);
		}
		LegalMovesChecker checker = new LegalMovesChecker(board);
		GoodMovesFinder finder = new GoodMovesFinder(board.clone());
		for (GoCell cell : finder.getGoodMoves()) {
			if (checker.isMoveLegal(cell)) {
				GoPlayingBoard newBoard = checker.getNewBoard();
				newBoard.oppositeToPlayNext();
				beta = Math.min(beta, maximize(newBoard, alpha, beta));
				BoardHistory.getSingleton().remove(newBoard);
				if (alpha >= beta) {
					break;
				}
			}
			checker.reset();
		}
		return beta;
	}

	@Override
	public void setNoOfGames(int num) {
		System.out.println("Alpha-Beta set number");
	}
	
}