package test;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import player_utils.AlphaBetaGoSolver;
import player_utils.BoardHistory;
import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;
import custom_java_utils.CheckFailException;
import custom_java_utils.ProjectPathUtils;

public class AlphaBeta_Test1 {

	private static String workspaceDirectory;
	
	@Before public void initialize() {
		workspaceDirectory = ProjectPathUtils.getWorkspaceDir();
	}
	
	@Before
	public void setUp() throws Exception {
		//Reset board history
		BoardHistory.wipeHistory();
	}

	@Test
	public void testAlphaBetaProblem1() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory + 
				"/src/test/test_data/problem1_unsettled3_1");
		board.setToPlayNext(Stone.WHITE);
		board.setFirstPlayerColour(Stone.WHITE);
		board.setSecondPlayerColour(Stone.BLACK);
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(14, 1));
		assertEquals(Stone.BLACK, board.getCellAt(14, 1).getContent());
		assertEquals(new GoCell(Stone.WHITE, 14, 0), solver.decision());
	}
	
	@Test
	public void testAlphaBetaProblem3() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem3_unsettled3_3");
		board.setToPlayNext(Stone.WHITE);
		board.setFirstPlayerColour(Stone.WHITE);
		board.setSecondPlayerColour(Stone.BLACK);
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(17, 4));
		assertEquals(Stone.BLACK, board.getCellAt(17, 4).getContent());
		assertEquals(new GoCell(Stone.WHITE, 16, 4), solver.decision());
	}

	@Test
	public void testAlphaBetaProblem_2_2() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem_2_2");
		board.setToPlayNext(Stone.WHITE);
		board.setFirstPlayerColour(Stone.WHITE);
		board.setSecondPlayerColour(Stone.BLACK);
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(17, 17));
		assertEquals(Stone.BLACK, board.getCellAt(17, 17).getContent());
		assertEquals(new GoCell(Stone.WHITE, 18, 14), solver.decision());
	}
	
	@Test
	public void testAlphaBetaProblem_4_2() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem_4_2");
		board.setToPlayNext(Stone.BLACK);
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(6, 0));
		assertEquals(Stone.WHITE, board.getCellAt(6, 0).getContent());
		assertEquals(new GoCell(Stone.BLACK, 8, 0), solver.decision());
	}
	
	@Test
	public void testAlphaBetaProblem_1_6() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem_1_6");
		board.setToPlayNext(Stone.WHITE);
		board.setFirstPlayerColour(Stone.WHITE);
		board.setSecondPlayerColour(Stone.BLACK);
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(17, 2));
		assertEquals(Stone.BLACK, board.getCellAt(17, 2).getContent());
		assertEquals(new GoCell(Stone.WHITE, 18, 0), solver.decision());
	}
	
	@Test
	public void testAlphaBetaProblem_3_2() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem_3_2");
		board.setToPlayNext(Stone.WHITE);
		board.setFirstPlayerColour(Stone.WHITE);
		board.setSecondPlayerColour(Stone.BLACK);
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(7, 2));
		assertEquals(Stone.BLACK, board.getCellAt(7, 2).getContent());
		assertEquals(new GoCell(Stone.WHITE, 7, 0), solver.decision());
	}
	
	@Test
	public void testAlphaBetaProblem_other_problem() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/other_problem");
		board.setToPlayNext(Stone.BLACK);
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(4, 1));
		assertEquals(Stone.WHITE, board.getCellAt(4, 1).getContent());
		assertEquals(new GoCell(Stone.BLACK, 1, 0), solver.decision());
	}
	
	@Test
	public void testAlphaBetaProblem_5_2() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem_5_2");
		board.setToPlayNext(Stone.BLACK);
		AlphaBetaGoSolver solver = new AlphaBetaGoSolver(board, board.getCellAt(15, 1));
		assertEquals(Stone.WHITE, board.getCellAt(15, 1).getContent());
		assertEquals(new GoCell(Stone.BLACK, 14, 0), solver.decision());
	}
}
