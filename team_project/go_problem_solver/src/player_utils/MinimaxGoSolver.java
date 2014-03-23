package player_utils;

import java.util.ArrayList;

import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import custom_java_utils.CheckFailException;

public class MinimaxGoSolver implements GoSolverAlgorithm {
	private GoPlayingBoard board;
	private GoCell cellToCapture;
	private static final long infinity = Integer.MAX_VALUE;
	private int playSurviveCoef;
	
	public MinimaxGoSolver(GoPlayingBoard board, GoCell cell) {
		this.board = board.clone();
		this.cellToCapture = cell.clone();
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
		return true;
	}
	
	public GoCell decision() throws CheckFailException {
		LegalMovesChecker checker = new LegalMovesChecker(board);
		ArrayList<CellValuePair> decisionMinimaxValues = 
				new ArrayList<CellValuePair>();
		
		for (int i = 0; i < board.getWidth(); i++) {
			for (int j = 0; j < board.getHeight(); j++) {
				GoCell cell = new GoCell(board.toPlayNext(), i, j);
				if (checker.isMoveLegal(cell)) {
					CellValuePair cellValuePair = new CellValuePair();
					cellValuePair.cell = cell;
					GoPlayingBoard newBoard = checker.getNewBoard();
					newBoard.oppositeToPlayNext();
					cellValuePair.value = minimize(newBoard, 0);
					decisionMinimaxValues.add(cellValuePair);
					BoardHistory.getSingleton().remove(newBoard);
				}
				checker.reset();
			}
		}
		GoCell bestMove = null;
		long bestValue = (-infinity);
		for (CellValuePair pair : decisionMinimaxValues) {
			System.out.println("pair" + pair.value);
			if (pair.value >= bestValue) {
				bestValue = (long) pair.value;
				bestMove = pair.cell;
			}
		}
		return bestMove;
	}
	
	private long maximize(GoPlayingBoard board, int depth) throws CheckFailException {
		if(depth <= 0) System.out.println(depth);
		depth++;
		if (isPositionTerminal(board)) {
			if (board.getCellAt(cellToCapture.getVerticalCoordinate(), 
					cellToCapture.getHorizontalCoordinate()).isEmpty()) {
				return infinity;
			}
			return (-infinity);
		}
		ArrayList<Long> minimaxValues = new ArrayList<Long>();
		LegalMovesChecker checker = new LegalMovesChecker(board);
		boolean foundMax = false;
		for (int i = 0; i < board.getWidth()&& !foundMax; i++) {
			for (int j = 0; j < board.getHeight() && !foundMax; j++) {
				if (checker.isMoveLegal(new GoCell(board.toPlayNext(), i, j))) {
					GoPlayingBoard newBoard = checker.getNewBoard();
					newBoard.oppositeToPlayNext();
					long currMinimaxValue = minimize(newBoard, depth);
					minimaxValues.add(currMinimaxValue);  // there is a legal move
					if (currMinimaxValue >= infinity) {
						foundMax = true;
					}
					BoardHistory.getSingleton().remove(newBoard);
				}
				checker.reset();
			}
		}
		long maxValue = (-infinity);
		for (long value : minimaxValues) {
			if (value > maxValue) {
				maxValue = value;
			}
		}
		return maxValue;
	}
	
	private long minimize(GoPlayingBoard board, int depth) throws CheckFailException {
		if(depth <= 0) System.out.println(depth);
		depth++;
		if (isPositionTerminal(board)) {
			if (board.getCellAt(cellToCapture.getVerticalCoordinate(), 
					cellToCapture.getHorizontalCoordinate()).isEmpty()) {
				return infinity;
			}
			return (-infinity);
		}
		ArrayList<Long> minimaxValues = new ArrayList<Long>();
		LegalMovesChecker checker = new LegalMovesChecker(board);
		boolean foundMin = false;
		for (int i = 0; i < board.getWidth() && !foundMin; i++) {
			for (int j = 0; j < board.getHeight() && !foundMin; j++) {
				if (checker.isMoveLegal(new GoCell(board.toPlayNext(), i, j))) {
					GoPlayingBoard newBoard = checker.getNewBoard();
					newBoard.oppositeToPlayNext();
					long currMinimaxValue = maximize(newBoard, depth);
					minimaxValues.add(currMinimaxValue);  // there is a legal move
					if (currMinimaxValue <= -infinity) {
						foundMin = true;
					}
					BoardHistory.getSingleton().remove(newBoard);
				}
				checker.reset();
			}
		}
		long maxValue = infinity;
		for (long value : minimaxValues) {
			if (value < maxValue) {
				maxValue = value;
			}
		}
		return maxValue;
	}
	
}