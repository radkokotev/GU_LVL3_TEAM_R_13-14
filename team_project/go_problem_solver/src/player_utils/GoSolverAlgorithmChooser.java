package player_utils;

import gui.Model;
import board_utils.GoCell;
import board_utils.GoPlayingBoard;

public class GoSolverAlgorithmChooser {
	
	private GoPlayingBoard board;
	private GoCell cell;
	private String name;
	
	public GoSolverAlgorithmChooser(GoPlayingBoard board, GoCell cell, String name) {
		this.board = board;
		this.cell = cell;
		this.name = name;
	}

	public GoSolverAlgorithm getAlgorithm() {
		GoSolverAlgorithm algorithm = null;
		switch (name) {
		case Model.MINIMAXSTRING: algorithm = new MinimaxGoSolver(board, cell);
			break;
		case Model.ALPHABETASTRING: algorithm = new AlphaBetaGoSolver(board, cell);
			break;
		case Model.MONTECARLOSTRING: algorithm = new MinimaxGoSolver(board, cell);
			break;
		}
		return algorithm;
	}
	
	
	
}
