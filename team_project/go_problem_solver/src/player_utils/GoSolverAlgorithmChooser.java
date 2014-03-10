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
		System.out.println("NAME RECEIVED : " + name);
	}

	public GoSolverAlgorithm getAlgorithm() {
		GoSolverAlgorithm algorithm = null;
		System.out.println("NAME IS : " + name);
		if (name.equals(Model.MINIMAXSTRING)) {
			algorithm = new MinimaxGoSolver(board, cell);
		} else if (name.equals(Model.ALPHABETASTRING)) {
			algorithm = new AlphaBetaGoSolver(board, cell);
		} else if (name.equals(Model.MONTECARLOSTRING)) {
			algorithm = new MinimaxGoSolver(board, cell);
		}
		return algorithm;
	}
	
	
	
}
