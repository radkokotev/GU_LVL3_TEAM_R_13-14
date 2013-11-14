package player_utils;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import custom_java_utils.CheckFailException;

import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;

public class MinimaxGoSolverTest {

	@Test
	public void testIsPositionTerminal() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(
				"src/player_utils/test_data/small_board_for_minimax");
		MinimaxGoSolver solver = new MinimaxGoSolver(board, board.getCellAt(18, 18));
		assertFalse(solver.isPositionTerminal(board));
		board.setCellAt(18, 18, new GoCell(Stone.BLACK, 18, 17));
		board.setCellAt(18, 18, new GoCell(Stone.NONE, 18, 18));
		assertTrue(solver.isPositionTerminal(board));
	}

	@Test
	public void testMinimaxDecisionBlack() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(
				"src/player_utils/test_data/small_board_for_minimax");
		board.setToPlayNext(Stone.BLACK);
		MinimaxGoSolver solver = new MinimaxGoSolver(board, board.getCellAt(18, 18));
		assertEquals(new GoCell(Stone.BLACK, 18, 17), solver.minimaxDecision());
	}
	
	@Test
	public void testMinimaxDecisionWhite() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(
				"src/player_utils/test_data/small_board_for_minimax_white");
		board.setToPlayNext(Stone.WHITE);
		MinimaxGoSolver solver = new MinimaxGoSolver(board, board.getCellAt(18, 18));
		assertEquals(new GoCell(Stone.WHITE, 18, 17), solver.minimaxDecision());
	}
	
	@Test
	public void testMinimaxDecisionUnsettled3White() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(
				"src/player_utils/test_data/unsettled_three_white");
		board.setToPlayNext(Stone.WHITE);
		MinimaxGoSolver solver = new MinimaxGoSolver(board, board.getCellAt(14, 1));
		assertEquals(new GoCell(Stone.WHITE, 14, 0), solver.minimaxDecision());
	}
	
	@Test
	public void testMinimaxDecisionUnsettled3Black() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(
				"src/player_utils/test_data/unsettled_three_black");
		board.setToPlayNext(Stone.BLACK);
		MinimaxGoSolver solver = new MinimaxGoSolver(board, board.getCellAt(14, 1));
		assertEquals(new GoCell(Stone.BLACK, 14, 0), solver.minimaxDecision());
	}
}
