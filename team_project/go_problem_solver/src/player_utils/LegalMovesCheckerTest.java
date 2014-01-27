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

public class LegalMovesCheckerTest {
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
	public void testGetLiberties() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory + "/src/player_utils/test_data/liberties_board");
		LegalMovesChecker checker = new LegalMovesChecker(board);
		assertEquals(2, checker.getLiberties(board.getCellAt(0, 0)));
		assertEquals(8, checker.getLiberties(board.getCellAt(2, 2)));
		assertEquals(1, checker.getLiberties(board.getCellAt(5, 5)));
		assertEquals(4, checker.getLiberties(board.getCellAt(4, 6)));
		assertEquals(0, checker.getLiberties(board.getCellAt(9, 9)));
		assertEquals(6, checker.getLiberties(board.getCellAt(17, 17)));
		assertEquals(0, checker.getLiberties(board.getCellAt(18, 0)));
		assertEquals(2, checker.getLiberties(board.getCellAt(18, 18)));
	}
	
	@Test
	public void testCaptureOponent() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory + "/src/player_utils/test_data/liberties_board");
		LegalMovesChecker checker = new LegalMovesChecker(board);
		assertFalse(checker.captureOponent(board.getCellAt(0, 0)));
		assertFalse(checker.captureOponent(board.getCellAt(6, 6)));
		assertTrue(checker.captureOponent(board.getCellAt(6, 18)));
		assertFalse(checker.captureOponent(board.getCellAt(7, 17)));
		assertFalse(checker.captureOponent(board.getCellAt(8, 18)));
		assertTrue(checker.captureOponent(board.getCellAt(8, 8)));
		assertFalse(checker.captureOponent(board.getCellAt(8, 8)));
		assertFalse(checker.captureOponent(board.getCellAt(10, 8)));
		assertTrue(checker.captureOponent(board.getCellAt(17, 0)));
		assertFalse(checker.captureOponent(board.getCellAt(18, 1)));
		assertFalse(checker.captureOponent(board.getCellAt(18, 18)));
		assertFalse(checker.captureOponent(board.getCellAt(18, 14)));
	}

	@Test
	public void testIsMoveLegalNoKo() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory + "/src/player_utils/test_data/liberties_board");
		LegalMovesChecker checker = new LegalMovesChecker(board);
		assertTrue(checker.isMoveLegal(new GoCell(Stone.BLACK, 0, 1)));
		checker.reset();
		assertTrue(checker.isMoveLegal(new GoCell(Stone.WHITE, 4, 4)));
		checker.reset();
		assertTrue(checker.isMoveLegal(new GoCell(Stone.WHITE, 12, 12)));
		checker.reset();
		assertFalse(checker.isMoveLegal(new GoCell(Stone.WHITE, 12, 18)));
		checker.reset();
		assertTrue(checker.isMoveLegal(new GoCell(Stone.BLACK, 14, 16)));
		GoPlayingBoard newBoard = checker.getNewBoard();
		assertNotSame(board, newBoard);
		assertNotSame(board.getCellAt(14, 16), newBoard.getCellAt(14, 16));
		assertEquals(Stone.BLACK, newBoard.getCellAt(14, 16).getContent());
		checker.reset();
		assertTrue(checker.isMoveLegal(new GoCell(Stone.BLACK, 14, 18)));
		checker.reset();
		assertFalse(checker.isMoveLegal(new GoCell(Stone.BLACK, 0, 18)));
		checker.reset();
		assertTrue(checker.isMoveLegal(new GoCell(Stone.WHITE, 0, 18)));
		checker.reset();
		assertFalse(checker.isMoveLegal(new GoCell(Stone.BLACK, 0, 0)));
		checker.reset();
		assertFalse(checker.isMoveLegal(new GoCell(Stone.WHITE, 21, 5)));
		checker.reset();
		assertFalse(checker.isMoveLegal(new GoCell(Stone.BLACK, 5, 25)));
		checker.reset();
		assertFalse(checker.isMoveLegal(new GoCell(Stone.WHITE, -4, 30)));
		checker.reset();
	}

	@Test
	public void testIsMoveLegalWithKo() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory + "/src/player_utils/test_data/liberties_board");
		BoardHistory.getSingleton().add(board); 
		LegalMovesChecker checker = new LegalMovesChecker(board);
		assertTrue(checker.isMoveLegal(new GoCell(Stone.BLACK, 14, 1)));
		assertFalse(checker.isMoveLegal(new GoCell(Stone.WHITE, 14, 0)));
		assertTrue(checker.isMoveLegal(new GoCell(Stone.WHITE, 14, 18)));
		assertTrue(checker.isMoveLegal(new GoCell(Stone.BLACK, 3, 16)));
		assertTrue(checker.isMoveLegal(new GoCell(Stone.WHITE, 3, 15)));
	}
	
	@Test
	public void testIsMoveLegalPrint() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory + "/src/player_utils/test_data/liberties_board");
		board.setToPlayNext(Stone.BLACK);
		LegalMovesChecker checker = new LegalMovesChecker(board);
		
		String expected_result = 
				"NYYYYYYYYYYYYYYYYNN\n" + 
				"YYYYYYYYYYYYYYYYYYN\n" +
				"YYNNNYYYYYYYYYNNNYY\n" +
				"YYYYYYYYYYYYYNNNYNY\n" +
				"YYYYYNNYYYYYYYNNNYY\n" +
				"YYYNNNNNYYYYYYYYYYY\n" +
				"YYYYNNNYYYYYYYYYYYN\n" +
				"YYYYYYYYYYYYYYYYYNN\n" +
				"YYYYYYYYNNYYYYYYYYN\n" +
				"YYYYYYYNNNNYYYYYYYY\n" +
				"YYYYYYYYNNYYYYYYYYY\n" +
				"YYYYYYYYYYYYYYYYYYY\n" +
				"YYYYYYYYYYYYYYYNNNN\n" +
				"NNYYYYYYYYYYYYYNYYN\n" +
				"NYNYYYYYYYYYYYYNYNY\n" +
				"NNYYYYYYYYYYYYYNNNN\n" +
				"YYYYYYYYYYYYYYYYYYY\n" +
				"NYYYYYYYYYYYYYNNNNN\n" +
				"NNYYYYYYYYYYYNNNNNN\n";
		String result = "";
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (checker.isMoveLegal(new GoCell(Stone.BLACK, i, j))) {
					result += "Y";
				} else {
					result += "N";
				}
				checker.reset();
			}
			result += "\n";
		}
		assertEquals(expected_result, result);
		
		result = "";
		boolean[][] legalityArray = checker.getLegalityArray();
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (legalityArray[i][j]) {
					result += "Y";
				} else {
					result += "N";
				}
			}
			result += "\n";
		}
		assertEquals(expected_result, result);		
	}
	
	@Test
	public void testBug() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard(workspaceDirectory + "/src/player_utils/test_data/unsettled_three_white");
		board.setToPlayNext(Stone.BLACK);
		LegalMovesChecker checker = new LegalMovesChecker(board);
		assertTrue(checker.isMoveLegal(new GoCell(Stone.BLACK, 13, 0)));
		assertTrue(checker.isMoveLegal(new GoCell(Stone.WHITE, 14, 0)));
	}
}
