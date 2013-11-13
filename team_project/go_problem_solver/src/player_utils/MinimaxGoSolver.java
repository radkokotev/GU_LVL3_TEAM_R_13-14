package player_utils;

import java.util.ArrayList;

import custom_java_utils.CheckFailException;

import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;

public class MinimaxGoSolver {
	private GoPlayingBoard board;
	private GoCell cellToCapture;
	private static final long infinity = Integer.MAX_VALUE;
	
	public MinimaxGoSolver(GoPlayingBoard board, GoCell cell) {
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
		if (board.getCellAt(cellToCapture.x(), cellToCapture.y()).isEmpty()) {
			// the target stone was captured 
			return true;
		}
		LegalMovesChecker checker = new LegalMovesChecker(board);
		for (int i = 0; i < board.getWidth(); i++) {
			for (int j = 0; j < board.getHeight(); j++) {
				if (checker.isMoveLegal(new GoCell(board.toPlayNext(), i, j))) {
					return false;  // there is a legal move
				}
				checker.reset();
			}
		}
		return true;
	}
	
	public GoCell minimaxDecision() throws CheckFailException {
		LegalMovesChecker checker = new LegalMovesChecker(board);
		ArrayList<CellValuePair> decisionMinimaxValues = 
				new ArrayList<MinimaxGoSolver.CellValuePair>();
		if (board.toPlayNext() ==  Stone.BLACK) {
			for (int i = 0; i < board.getWidth(); i++) {
				for (int j = 0; j < board.getHeight(); j++) {
					GoCell cell = new GoCell(board.toPlayNext(), i, j);
					if (checker.isMoveLegal(cell)) {
						CellValuePair cellValuePair = new CellValuePair();
						cellValuePair.cell = cell;
						GoPlayingBoard newBoard = checker.getNewBoard();
						newBoard.setToPlayNext(board.toPlayNext() == Stone.BLACK ? 
								Stone.WHITE : Stone.BLACK);
						cellValuePair.minimaxValue = minimize(newBoard);
						decisionMinimaxValues.add(cellValuePair);
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
		} else {
			for (int i = 0; i < board.getWidth(); i++) {
				for (int j = 0; j < board.getHeight(); j++) {
					GoCell cell = new GoCell(board.toPlayNext(), i, j);
					if (checker.isMoveLegal(cell)) {
						CellValuePair cellValuePair = new CellValuePair();
						cellValuePair.cell = cell;
						GoPlayingBoard newBoard = checker.getNewBoard();
						newBoard.setToPlayNext(board.toPlayNext() == Stone.BLACK ? 
								Stone.WHITE : Stone.BLACK);
						cellValuePair.minimaxValue = maximize(newBoard);
						decisionMinimaxValues.add(cellValuePair);
					}
					checker.reset();
				}
			}
			GoCell bestMove = null;
			long bestValue = infinity;
			for (CellValuePair pair : decisionMinimaxValues) {
				if (pair.minimaxValue < bestValue) {
					bestValue = pair.minimaxValue;
					bestMove = pair.cell;
				}
			}
			return bestMove;
		}
	}
	
	private long maximize(GoPlayingBoard board) throws CheckFailException {
		if (isPositionTerminal(board)) {
			if (board.getCellAt(cellToCapture.x(), cellToCapture.y()).isEmpty()) {
				return -infinity;
			} 
			return (infinity);
		}
		ArrayList<Long> minimaxValues = new ArrayList<Long>();
		LegalMovesChecker checker = new LegalMovesChecker(board);
		for (int i = 0; i < board.getWidth(); i++) {
			for (int j = 0; j < board.getHeight(); j++) {
				if (checker.isMoveLegal(new GoCell(board.toPlayNext(), i, j))) {
					GoPlayingBoard newBoard = checker.getNewBoard();
					newBoard.setToPlayNext(board.toPlayNext() == Stone.BLACK ? 
							Stone.WHITE : Stone.BLACK);
					minimaxValues.add(minimize(newBoard));  // there is a legal move
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
	
	private long minimize(GoPlayingBoard board) throws CheckFailException {
		if (isPositionTerminal(board)) {
			if (board.getCellAt(cellToCapture.x(), cellToCapture.y()).isEmpty()) {
				return infinity; //TODO get why this is like that
			} 
			return (-infinity);
		}
		ArrayList<Long> minimaxValues = new ArrayList<Long>();
		LegalMovesChecker checker = new LegalMovesChecker(board);
		for (int i = 0; i < board.getWidth(); i++) {
			for (int j = 0; j < board.getHeight(); j++) {
				if (checker.isMoveLegal(new GoCell(board.toPlayNext(), i, j))) {
					GoPlayingBoard newBoard = checker.getNewBoard();
					newBoard.setToPlayNext(board.toPlayNext() == Stone.BLACK ? 
							Stone.WHITE : Stone.BLACK);
					minimaxValues.add(maximize(newBoard));  // there is a legal move
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
