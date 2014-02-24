package player_utils;

import java.util.ArrayList;

import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import custom_java_utils.CheckFailException;

public class AlphaBetaGoSolver implements GoSolverAlgorithm{
	private GoPlayingBoard board;
	private GoCell cellToCapture;
	private static final long infinity = Integer.MAX_VALUE;
	
	public AlphaBetaGoSolver(GoPlayingBoard board, GoCell cell) {
		this.board = board.clone();
		this.cellToCapture = cell.clone();
	}
	
	private class CellValuePair implements Comparable<CellValuePair>{
		public GoCell cell;
		public long minimaxValue;
		
		@Override
		public int compareTo(CellValuePair other) {
			if (this.minimaxValue < other.minimaxValue) return -1;
			if (this.minimaxValue > other.minimaxValue) return 1;
			return 0;
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
				new ArrayList<AlphaBetaGoSolver.CellValuePair>();
		
		for (int i = 0; i < board.getWidth(); i++) {
			for (int j = 0; j < board.getHeight(); j++) {
				GoCell cell = new GoCell(board.toPlayNext(), i, j);
				if (checker.isMoveLegal(cell)) {
					CellValuePair cellValuePair = new CellValuePair();
					cellValuePair.cell = cell;
					GoPlayingBoard newBoard = checker.getNewBoard();
					newBoard.oppositeToPlayNext();
					cellValuePair.minimaxValue = minimize(newBoard, -infinity, infinity);
					decisionMinimaxValues.add(cellValuePair);
					BoardHistory.getSingleton().remove(newBoard);
				}
				checker.reset();
			}
		}
		GoCell bestMove = null;
		long bestValue = (-infinity);
		for (CellValuePair pair : decisionMinimaxValues) {
			if (pair.minimaxValue > bestValue) {
				bestValue = pair.minimaxValue;
				bestMove = pair.cell;
			}
		}
		return bestMove;
	}
	
	private long maximize(GoPlayingBoard board, long alpha, long beta) throws CheckFailException {
		if (isPositionTerminal(board)) {
			if (board.getCellAt(cellToCapture.getVerticalCoordinate(), 
					cellToCapture.getHorizontalCoordinate()).isEmpty()) {
				return infinity;
			}
			return (-infinity);
		}
		LegalMovesChecker checker = new LegalMovesChecker(board);
		boolean foundMax = false;
		for (int i = 0; i < board.getWidth()&& !foundMax; i++) {
			for (int j = 0; j < board.getHeight() && !foundMax; j++) {
				if (checker.isMoveLegal(new GoCell(board.toPlayNext(), i, j))) {
					GoPlayingBoard newBoard = checker.getNewBoard();
					newBoard.oppositeToPlayNext();
					alpha = Math.max(alpha, minimize(newBoard, alpha, beta));
					if (alpha >= beta) {
						foundMax = true;
					}
					BoardHistory.getSingleton().remove(newBoard);
				}
				checker.reset();
			}
		}
		return alpha;
	}
	
	private long minimize(GoPlayingBoard board, long alpha, long beta) throws CheckFailException {
		if (isPositionTerminal(board)) {
			if (board.getCellAt(cellToCapture.getVerticalCoordinate(), 
					cellToCapture.getHorizontalCoordinate()).isEmpty()) {
				return infinity;
			}
			return (-infinity);
		}
		LegalMovesChecker checker = new LegalMovesChecker(board);
		boolean foundMin = false;
		for (int i = 0; i < board.getWidth() && !foundMin; i++) {
			for (int j = 0; j < board.getHeight() && !foundMin; j++) {
				if (checker.isMoveLegal(new GoCell(board.toPlayNext(), i, j))) {
					GoPlayingBoard newBoard = checker.getNewBoard();
					newBoard.oppositeToPlayNext();
					beta = Math.min(beta, maximize(newBoard, alpha, beta));
					if (alpha >= beta) {
						foundMin = true;
					}
					BoardHistory.getSingleton().remove(newBoard);
				}
				checker.reset();
			}
		}
		return beta;
	}
	
}
