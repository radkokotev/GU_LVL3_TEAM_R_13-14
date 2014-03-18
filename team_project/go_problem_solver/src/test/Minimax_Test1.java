package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import player_utils.BoardHistory;
import player_utils.MinimaxGoSolver;
import player_utils.MonteCarloGoSolver;
import custom_java_utils.CheckFailException;
import custom_java_utils.ProjectPathUtils;
import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;

public class Minimax_Test1 {

	private static String workspaceDirectory;
	
	@Before public void initialize() {
		workspaceDirectory = ProjectPathUtils.getWorkspaceDir();
	}
	
	@Before
	public void setUp() throws Exception {
		//Reset board history
		BoardHistory.wipeHistory();
	}
	
	/*
	 * Unsettled Three - the only one that is right lol
	 *
	@Test
	public void testMinimaxProblem1() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory + 
				"/src/test/test_data/problem1_unsettled3_1");
		board.setToPlayNext(Stone.WHITE);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(14, 1));
		assertEquals(Stone.BLACK, board.getCellAt(14, 1).getContent());
		assertEquals(new GoCell(Stone.WHITE, 14, 0), solver.decision());
	}
	
	//OK
	@Test 
	public void testMinimaxProblem2() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory + 
				"/src/test/test_data/problem2_unsettled3_2");
		board.setToPlayNext(Stone.WHITE);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(9, 1));
		assertEquals(Stone.BLACK, board.getCellAt(9, 1).getContent());
		assertEquals(new GoCell(Stone.WHITE, 9, 0), solver.decision());
	}
	
	//OK
	@Test
	public void testMinimaxProblem3() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem3_unsettled3_3");
		board.setToPlayNext(Stone.WHITE);
		MinimaxGoSolver solver = new MinimaxGoSolver(board, board.getCellAt(17, 4));
		assertEquals(Stone.BLACK, board.getCellAt(17, 4).getContent());
		assertEquals(new GoCell(Stone.WHITE, 16, 4), solver.minimaxDecision());
	}
	
	//ok
	@Test
	public void testMinimaxProblem5() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem5_rabbitysix_1");
		board.setToPlayNext(Stone.WHITE);
		MinimaxGoSolver solver = new MinimaxGoSolver(board, board.getCellAt(6, 3));
		assertEquals(Stone.BLACK, board.getCellAt(6, 3).getContent());
		assertEquals(new GoCell(Stone.WHITE, 6, 0), solver.minimaxDecision());
	}
	
	//OK
	@Test
	public void testMinimaxProblem6() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem6_shortageofliberties_1");
		board.setToPlayNext(Stone.WHITE);
		MinimaxGoSolver solver = new MinimaxGoSolver(board, board.getCellAt(5, 16));
		assertEquals(Stone.BLACK, board.getCellAt(5, 16).getContent());
		assertEquals(new GoCell(Stone.WHITE, 5, 18), solver.minimaxDecision());
	}
	
	//
	@Test
	public void testMinimaxProblem7() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem7_interioreye_1");
		board.setToPlayNext(Stone.WHITE);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(15, 15), 
				10, System.currentTimeMillis() + 1000 * 90);
		assertEquals(Stone.BLACK, board.getCellAt(15, 15).getContent());
		assertEquals(new GoCell(Stone.WHITE, 18, 17), solver.decision());
	}
	
	@Test
	public void testMinimaxProblem8() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem8_falseeyes_1");
		board.setToPlayNext(Stone.BLACK);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(16, 15));
		assertEquals(Stone.WHITE, board.getCellAt(16, 15).getContent());
		assertEquals(new GoCell(Stone.BLACK, 18, 17), solver.decision());
	}
	
	@Test
	public void testMinimaxProblem9() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem9_doorgroup_1");
		board.setToPlayNext(Stone.WHITE);
		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(17, 9));
		assertEquals(Stone.BLACK, board.getCellAt(17, 9).getContent());
		assertEquals(new GoCell(Stone.WHITE, 16, 8), solver.decision());
	}
	
	@Test
	public void testMinimaxProblem10() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/problem10_incompleteshapes_1");
		board.setToPlayNext(Stone.WHITE);
		MinimaxGoSolver solver = new MinimaxGoSolver(board, board.getCellAt(14, 1));
		assertEquals(Stone.BLACK, board.getCellAt(14, 1).getContent());
		assertEquals(new GoCell(Stone.WHITE, 14, 0), solver.minimaxDecision());
	}	*/
	
	@Test
	public void mins_2_testKoFightProblem_12_5() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/chapter_12_5");
		board.setToPlayNext(Stone.WHITE);
		System.out.println("Running an algorithm handling KO with penalties of 0.5 for 2 minutes");
		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(16, 5), 
				20, System.currentTimeMillis() + 1000 * 120, true);
		assertEquals(Stone.BLACK, board.getCellAt(16, 5).getContent());
		assertEquals(new GoCell(Stone.WHITE, 17, 5), solver.decision());
	}
	
	@Test
	public void mins_5_testKoFightProblem_12_5() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/chapter_12_5");
		board.setToPlayNext(Stone.WHITE);
		System.out.println("Running an algorithm handling KO with penalties of 0.5 for 5 minutes");
		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(16, 5), 
				50, System.currentTimeMillis() + 1000 * 300, true);
		assertEquals(Stone.BLACK, board.getCellAt(16, 5).getContent());
		assertEquals(new GoCell(Stone.WHITE, 17, 5), solver.decision());
	}
	
	@Test
	public void mins_9_testKoFightProblem_12_5() throws FileNotFoundException, CheckFailException, InterruptedException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
				"/src/test/test_data/chapter_12_5");
		board.setToPlayNext(Stone.WHITE);
		System.out.println("Running an algorithm handling KO with penalties of 0.5 for 10 minutes");
		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(16, 5), 
				50, System.currentTimeMillis() + 1000 * 600, true);
		assertEquals(Stone.BLACK, board.getCellAt(16, 5).getContent());
		assertEquals(new GoCell(Stone.WHITE, 17, 5), solver.decision());
	}
	
//	@Test
//	public void mins_2_testNoKoProblem_12_5() throws FileNotFoundException, CheckFailException, InterruptedException {
//		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
//				"/src/test/test_data/chapter_12_5");
//		board.setToPlayNext(Stone.WHITE);
//		System.out.println("Running simple Monte-Carlo 2 minutes");
//		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(16, 5), 
//				20, System.currentTimeMillis() + 1000 * 120, false);
//		assertEquals(Stone.BLACK, board.getCellAt(16, 5).getContent());
//		assertEquals(new GoCell(Stone.WHITE, 17, 5), solver.decision());
//	}
//	
//	@Test
//	public void mins_5_testNoKoProblem_12_5() throws FileNotFoundException, CheckFailException, InterruptedException {
//		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
//				"/src/test/test_data/chapter_12_5");
//		board.setToPlayNext(Stone.WHITE);
//		System.out.println("Running simple Monte-Carlo 5 minutes");
//		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(16, 5), 
//				50, System.currentTimeMillis() + 1000 * 300, false);
//		assertEquals(Stone.BLACK, board.getCellAt(16, 5).getContent());
//		assertEquals(new GoCell(Stone.WHITE, 17, 5), solver.decision());
//	}
//
//	@Test
//	public void mins_9_testNoKoProblem_12_5() throws FileNotFoundException, CheckFailException, InterruptedException {
//		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory +
//				"/src/test/test_data/chapter_12_5");
//		board.setToPlayNext(Stone.WHITE);
//		System.out.println("Running simple Monte-Carlo 10 minutes");
//		MonteCarloGoSolver solver = new MonteCarloGoSolver(board, board.getCellAt(16, 5), 
//				50, System.currentTimeMillis() + 1000 * 600, false);
//		assertEquals(Stone.BLACK, board.getCellAt(16, 5).getContent());
//		assertEquals(new GoCell(Stone.WHITE, 17, 5), solver.decision());
//	}
}
