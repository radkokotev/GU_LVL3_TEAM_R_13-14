package player_utils;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import custom_java_utils.CheckFailException;
import custom_java_utils.ProjectPathUtils;

import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;

public class AlphaBetaGoSolverTest {
	private static String workspaceDirectory;
        
        @Before public void initialize() {
         workspaceDirectory = ProjectPathUtils.getWorkspaceDir();
        }
	
	@Before
	public void setUp() throws Exception {
		// Reset the history after every test case;
		BoardHistory.wipeHistory();
	}
	@Test
	public void testIsPositionTerminal() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/player_utils/test_data/small_board_for_minimax");
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(18, 18));
		assertFalse(solver.isPositionTerminal(board));
		board.setCellAt(18, 18, new GoCell(Stone.BLACK, 18, 17));
		board.setCellAt(18, 18, new GoCell(Stone.NONE, 18, 18));
		assertTrue(solver.isPositionTerminal(board));
	}

	@Test
	public void testMinimaxDecisionBlack() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/player_utils/test_data/small_board_for_minimax");
		board.setToPlayNext(Stone.BLACK);
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(18, 18));
		assertEquals(new GoCell(Stone.BLACK, 18, 17), solver.decision());
	}
	
	@Test
	public void testMinimaxDecisionWhite() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/player_utils/test_data/small_board_for_minimax_white");
		board.setToPlayNext(Stone.WHITE);
		board.setFirstPlayerColour(Stone.WHITE);
		board.setSecondPlayerColour(Stone.BLACK);
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(18, 18));
		assertEquals(new GoCell(Stone.WHITE, 18, 17), solver.decision());
	}
	
	@Test
	public void testMinimaxDecisionUnsettled3White() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory+
				"/src/player_utils/test_data/unsettled_three_white");
		board.setToPlayNext(Stone.WHITE);
		board.setFirstPlayerColour(Stone.WHITE);
		board.setSecondPlayerColour(Stone.BLACK);
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(14, 1));
		assertEquals(new GoCell(Stone.WHITE, 14, 0), solver.decision());
	}
	
	@Test
	public void testMinimaxDecisionUnsettled3WhiteToSurvive() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory+
				"/src/player_utils/test_data/unsettled_three_white");
		board.setToPlayNext(Stone.BLACK);
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(14, 1));
		assertEquals(new GoCell(Stone.BLACK, 14, 0), solver.decision());
	}
	
	@Test
	public void testMinimaxDecisionUnsettled3Black() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/player_utils/test_data/unsettled_three_black");
		board.setToPlayNext(Stone.BLACK);
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(14, 1));
		assertEquals(new GoCell(Stone.BLACK, 14, 0), solver.decision());
	}
}
