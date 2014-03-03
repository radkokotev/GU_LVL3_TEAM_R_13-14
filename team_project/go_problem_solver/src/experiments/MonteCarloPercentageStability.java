package experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import player_utils.CellValuePair;
import player_utils.MonteCarloGoSolver;
import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;
import custom_java_utils.CheckFailException;
import custom_java_utils.ProjectPathUtils;

public class MonteCarloPercentageStability {
	private static String workspaceDirectory;
	private static ArrayList<CellValuePair> previousValues;
	private static PrintWriter printWriter;
	
	private static final double CONVERGENCE_RANGE = 0.03;
	
	/**
	 * Constants to work with the following file naming convention
	 * "problem_<problem #>_moves<# of initial legal moves>"
	 */
	private static int problem_number = 1;
	private static int first_input_filename_suffix = 5;
	private static int last_input_filename_suffix = 8;
	
	/**
	 * A prefix to distinguish between runs on different machines
	 */
	private static String output_file_suffix = "bo720-2-1";
	
	private static void writeToFile(ArrayList<CellValuePair> currentValues) {
		if (previousValues == null) {
			String header = "[";
			for (int i = 0; i < currentValues.size(); i++) {
				header += String.format("(%d,%d)", 
						currentValues.get(i).cell.getVerticalCoordinate(),
						currentValues.get(i).cell.getHorizontalCoordinate());
				if (i < currentValues.size() - 1) {
					header += ",";
				}
			}
			header += "]";
			printWriter.println(header);
		}
		String nextOutputLine = "[";
		for (int i = 0; i < currentValues.size(); i++) {
			nextOutputLine += String.format("%.5f", currentValues.get(i).value);
			if (i < currentValues.size() - 1) {
				nextOutputLine += ",";
			} else {
				nextOutputLine += "],";
			}
		}
		printWriter.println(nextOutputLine);
		printWriter.flush();
	}
	
	private static boolean seemsToConverge(double games, String filename) throws 
			FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/experiments/data/" + filename);
		board.setToPlayNext(Stone.BLACK);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(9, 13), (int) games);
		GoCell move = solver.decision();
		if (new GoCell(Stone.BLACK, 8, 10).equals(move)) {
			//System.out.println("Solved correctly");
		} else {
			//System.out.println("Solved incorrectly");
		}
		ArrayList<CellValuePair> currentValues = solver.getMovesPercentages();
		writeToFile(currentValues);
		if (previousValues == null) {
			previousValues = currentValues;
			return false;
		}
		boolean result = true;
		for (int i = 0; i < currentValues.size(); i++) {
			double previous = previousValues.get(i).value;
			double current = currentValues.get(i).value;
			/*System.out.println("Cell " + currentValues.get(i).cell.getVerticalCoordinate() + "," +
					currentValues.get(i).cell.getHorizontalCoordinate() + 
					" old: " + previous + " current: " + current);*/
			if (Math.abs(previous - current) > CONVERGENCE_RANGE) {
				result = false;
			}
		}
		previousValues = currentValues;
		return result;
	}
	
	private static void handleArguments(String[] args) {
		output_file_suffix = args[0];
		problem_number = Integer.parseInt(args[1]);
		first_input_filename_suffix = Integer.parseInt(args[2]);
		last_input_filename_suffix = Integer.parseInt(args[3]);
	}
	
	public static void main(String[] args) throws 
			FileNotFoundException, CheckFailException, InterruptedException {
		handleArguments(args);
		workspaceDirectory = ProjectPathUtils.getWorkspaceDir();
		for (int fileSuffix = first_input_filename_suffix;
				fileSuffix <= last_input_filename_suffix; 
				fileSuffix++) {
			String filename = String.format("problem_%d_moves_%d", problem_number, fileSuffix);
			printWriter = new PrintWriter(new File(
					"output_" + filename + "_" + output_file_suffix));
			int i;
			for (i = 0; !seemsToConverge(5*Math.pow(2, i), filename); i++) {
				System.out.println("Currenbtly at " + i);
			}
			// Make one further iteration to confirm results
			seemsToConverge(5*Math.pow(2, i + 1), filename);
			System.out.println("Reached up to " + i + "\n");
			printWriter.close();
			previousValues = null;
		}
	}

}
