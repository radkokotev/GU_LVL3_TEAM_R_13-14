package player_utils;

import custom_java_utils.CheckFailException;
import board_utils.GoCell;

public interface GoSolverAlgorithm {

	GoCell decision() throws CheckFailException, InterruptedException;
	
}
