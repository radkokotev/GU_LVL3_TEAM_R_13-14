package test;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import player_utils.BoardHistory;
import player_utils.MonteCarloGoSolver;
import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;
import custom_java_utils.CheckFailException;
import custom_java_utils.ProjectPathUtils;

public class MonteCarlo_Test1 {

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
	public void testMonteCarloProblem1() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory + 
				"/src/test/test_data/problem1_unsettled3_1");
		board.setToPlayNext(Stone.WHITE);
		board.setFirstPlayerColour(Stone.WHITE);
		board.setSecondPlayerColour(Stone.BLACK);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(14, 1));
		assertEquals(Stone.BLACK, board.getCellAt(14, 1).getContent());
		assertEquals(new GoCell(Stone.WHITE, 14, 0), solver.decision());
	}
	
	@Test
	public void testMonteCarloProblem3() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem3_unsettled3_3");
		board.setToPlayNext(Stone.WHITE);
		board.setFirstPlayerColour(Stone.WHITE);
		board.setSecondPlayerColour(Stone.BLACK);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(17, 4), 500);
		assertEquals(Stone.BLACK, board.getCellAt(17, 4).getContent());
		assertEquals(new GoCell(Stone.WHITE, 16, 4), solver.decision());
	}
	
	@Test
	public void testMonteCarloProblem_4_2() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem_4_2");
		board.setToPlayNext(Stone.BLACK);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(
				board, board.getCellAt(6, 0), 20, System.currentTimeMillis() + 60 * 1000);
		assertEquals(Stone.WHITE, board.getCellAt(6, 0).getContent());
		assertEquals(new GoCell(Stone.BLACK, 8, 0), solver.decision());
	}
	
	@Test
	public void testMonteCarloProblem_other_problem() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/other_problem");
		board.setToPlayNext(Stone.BLACK);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(4, 1), 150);
		assertEquals(Stone.WHITE, board.getCellAt(4, 1).getContent());
		assertEquals(new GoCell(Stone.BLACK, 3, 0), solver.decision());
	}
	
	@Test
	public void testMonteCarloProblem_3_2() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem_3_2");
		board.setToPlayNext(Stone.WHITE);
		board.setFirstPlayerColour(Stone.WHITE);
		board.setSecondPlayerColour(Stone.BLACK);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(7, 2));
		assertEquals(Stone.BLACK, board.getCellAt(7, 2).getContent());
		assertEquals(new GoCell(Stone.WHITE, 7, 0), solver.decision());
	}
	
	@Test
	public void testMonteCarloProblem_5_2() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem_5_2");
		board.setToPlayNext(Stone.BLACK);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(
				board, board.getCellAt(15, 1), 20, System.currentTimeMillis() + 60 * 1000);
		assertEquals(Stone.WHITE, board.getCellAt(15, 1).getContent());
		assertEquals(new GoCell(Stone.BLACK, 14, 0), solver.decision());
	}
	
	@Test
	public void testMonteCarloProblem_3_handout() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem_3_handout");
		board.setToPlayNext(Stone.BLACK);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(
				board, board.getCellAt(5, 14), 20, System.currentTimeMillis() + 60 * 1000);
		assertEquals(Stone.WHITE, board.getCellAt(5, 14).getContent());
		assertEquals(new GoCell(Stone.BLACK, 6, 14), solver.decision());
	}
	
	@Test
	public void testMonteCarloProblem_4_handout() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem_4_handout");
		board.setToPlayNext(Stone.BLACK);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(
				board, board.getCellAt(14, 6), 20, 90 * 1000 + System.currentTimeMillis());
		assertEquals(Stone.WHITE, board.getCellAt(14, 6).getContent());
		assertEquals(new GoCell(Stone.BLACK, 16, 6), solver.decision());
	}
}
