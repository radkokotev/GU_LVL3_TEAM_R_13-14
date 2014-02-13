package player_utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import custom_java_utils.CheckFailException;

public class MonteCarloGoSolver {
	private GoPlayingBoard board;
	private GoCell cellToCapture;
	private static final long infinity = Integer.MAX_VALUE;
	private int gamesPerMove = 100;
	private ConcurrentLinkedQueue<CellValuePair> monteCarloValues;
	
	public MonteCarloGoSolver(GoPlayingBoard board, GoCell cell) {
		this.board = board.clone();
		this.cellToCapture = cell.clone();
	}
	
	public MonteCarloGoSolver(GoPlayingBoard board, GoCell cell, int gamesPerMove) {
		this.board = board.clone();
		this.cellToCapture = cell.clone();
		this.gamesPerMove = gamesPerMove;
	}
	
	private class CellValuePair implements Comparable<CellValuePair>{
		public GoCell cell;
		public double value;
		
		@Override
		public int compareTo(CellValuePair other) {
			if (this.value < other.value) return -1;
			if (this.value > other.value) return 1;
			return 0;
		}
	}
	
	public boolean isGoalAchieved(GoPlayingBoard board) {
		if (board.getCellAt(cellToCapture.getVerticalCoordinate(), 
				cellToCapture.getHorizontalCoordinate()).isEmpty()) {
			// the target stone was captured
			return true;
		}
		// The algorithm itself checks if there are any further legal moves. There is
		// no need to duplicate this here
		return false;
	}
	
	/**
	 * Calculates what the best next move would be
	 * @return the cell that is the best move according to the algorithm
	 * @throws CheckFailException
	 * @throws InterruptedException 
	 */
	public GoCell decision() throws CheckFailException, InterruptedException {
		LegalMovesChecker checker = new LegalMovesChecker(board);
		monteCarloValues = 
				new ConcurrentLinkedQueue<MonteCarloGoSolver.CellValuePair>();
		
		LinkedList<Thread> threads = new LinkedList<Thread>();
		for (int i = 0; i < board.getWidth(); i++) {
			for (int j = 0; j < board.getHeight(); j++) {
				GoCell cell = new GoCell(board.toPlayNext(), i, j);
				if (checker.isMoveLegal(cell)) {
					CellValuePair cellValuePair = new CellValuePair();
					cellValuePair.cell = cell;
					GoPlayingBoard newBoard = checker.getNewBoard();
					newBoard.oppositeToPlayNext();
//					cellValuePair.value = monteCarloEvaluation(newBoard);
//					monteCarloValues.add(cellValuePair);
					Thread thread = new Thread(new MonteCarloInstanceRunner(newBoard, cell));
					threads.add(thread);
					thread.start();
					BoardHistory.getSingleton().remove(newBoard);
				}
				checker.reset();
			}
		}
		System.out.println("Running with " + Thread.activeCount() + " threads.");
		for (Thread t : threads) {
			t.join();
		}
		GoCell bestMove = null;
		double bestValue = (-infinity);
		for (CellValuePair pair : monteCarloValues) {
			if (pair.value > bestValue) {
				bestValue = pair.value;
				bestMove = pair.cell;
			}
			System.out.println(pair.cell.getVerticalCoordinate() + "," + 
					pair.cell.getHorizontalCoordinate() + " -> " + pair.value);
		}
		return bestMove;
	}
	
	/**
	 * A method to play random games from a certain point, returning the ratio
	 * of won games.
	 * @param board the starting point of the algorithm
	 * @return the win ratio for the given starting position
	 * @throws CheckFailException
	 */
	private double monteCarloEvaluation(GoPlayingBoard board) throws CheckFailException {
		int winCount = 0;
		int gamesToPlay = gamesPerMove;
		// Play that many random games
		for (;gamesToPlay > 0; gamesToPlay--) {
			// Keep a record of all the board positions that were played, so we can later
			// remove them from the history for the next iteration
			ArrayList<GoPlayingBoard> boardsPlayed = new ArrayList<GoPlayingBoard>();
			GoPlayingBoard newBoard = board;
			while (!isGoalAchieved(newBoard)) {
				LegalMovesChecker checker = new LegalMovesChecker(newBoard);
				// Find all legal moves for the current board
				ArrayList<GoCell> legalMoves = new ArrayList<GoCell>();
				for (int i = 0; i < newBoard.getWidth(); i++) {
					for (int j = 0; j < newBoard.getHeight(); j++) {
						GoCell currCell = new GoCell(newBoard.toPlayNext(), i, j);
						if (checker.isMoveLegal(currCell)) {
							legalMoves.add(currCell);
						}
						checker.reset();
					}
				}
				// No more legal moves => this is a terminal state
				if (legalMoves.isEmpty()) {
					break;
				}
				// Pick a random move to be played next
				Random randomGenerator = new Random();
				checker.isMoveLegal(legalMoves.get(randomGenerator.nextInt(legalMoves.size())));
				newBoard = checker.getNewBoard();
				newBoard.oppositeToPlayNext();
				boardsPlayed.add(newBoard);
			}
			if (newBoard.getCellAt(cellToCapture.getVerticalCoordinate(), 
					cellToCapture.getHorizontalCoordinate()).isEmpty()) {
				winCount++;
			}
			// Remove all played boards from history
			for (GoPlayingBoard playedBoard : boardsPlayed) {
				BoardHistory.getSingleton().remove(playedBoard);
			}
		}
		return ((double)winCount) * 1.0 / gamesPerMove;
	}
	
	private class MonteCarloInstanceRunner implements Runnable {
		private GoPlayingBoard board;
		private CellValuePair cellValuePair;
		public MonteCarloInstanceRunner(GoPlayingBoard board, GoCell move) {
			this.board = board;
			this.cellValuePair = new CellValuePair();
			this.cellValuePair.cell = move;
		}

		@Override
		public void run() {
			try {
				cellValuePair.value = monteCarloEvaluation(board);
				synchronized(monteCarloValues) {
					monteCarloValues.add(cellValuePair);
				}
			} catch (CheckFailException e) {
				e.printStackTrace();
				System.exit(1);
			}
			BoardHistory.wipeHistory();
		}
		
	}
}
