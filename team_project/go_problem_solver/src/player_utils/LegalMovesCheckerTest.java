package player_utils;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import custom_java_utils.CheckFailException;

import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;

public class LegalMovesCheckerTest {
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetLiberties() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard("src/player_utils/test_data/liberties_board");
		LegalMovesChecker checker = new LegalMovesChecker(board);
		assertEquals(2, checker.getLiberties(board.getCellAt(0, 0)));
		assertEquals(8, checker.getLiberties(board.getCellAt(2, 2)));
		assertEquals(1, checker.getLiberties(board.getCellAt(5, 5)));
		assertEquals(0, checker.getLiberties(board.getCellAt(9, 9)));
		assertEquals(6, checker.getLiberties(board.getCellAt(17, 17)));
		assertEquals(0, checker.getLiberties(board.getCellAt(18, 0)));
		assertEquals(2, checker.getLiberties(board.getCellAt(18, 18)));
	}
	
	@Test
	public void testCaptureOponent() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard("src/player_utils/test_data/liberties_board");
		LegalMovesChecker checker = new LegalMovesChecker(board);
		assertFalse(checker.captureOponent(board.getCellAt(0, 0)));
		assertFalse(checker.captureOponent(board.getCellAt(6, 6)));
		assertTrue(checker.captureOponent(board.getCellAt(10, 9)));
		assertTrue(checker.captureOponent(board.getCellAt(17, 0)));
		assertFalse(checker.captureOponent(board.getCellAt(18, 18)));
	}

	@Test
	public void testIsMoveLegalNoKo() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard("src/player_utils/test_data/liberties_board");
		LegalMovesChecker checker = new LegalMovesChecker(board);
		assertNotNull(checker.isMoveLegal(new GoCell(Stone.BLACK, 0, 1)));
		checker = new LegalMovesChecker(board);
		assertNotNull(checker.isMoveLegal(new GoCell(Stone.WHITE, 4, 4)));
		checker = new LegalMovesChecker(board);
		assertNotNull(checker.isMoveLegal(new GoCell(Stone.WHITE, 12, 12)));
		checker = new LegalMovesChecker(board);
		assertNull(checker.isMoveLegal(new GoCell(Stone.BLACK, 0, 18)));
		checker = new LegalMovesChecker(board);
		assertNotNull(checker.isMoveLegal(new GoCell(Stone.WHITE, 0, 18)));
		checker = new LegalMovesChecker(board);
		assertNull(checker.isMoveLegal(new GoCell(Stone.BLACK, 0, 0)));
	}

	@Test
	public void testIsMoveLegalWithKo() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard("src/player_utils/test_data/liberties_board");
		LegalMovesChecker checker = new LegalMovesChecker(board);
		BoardHistory history = BoardHistory.getSingleton();
		history.add(board); 
		assertNotNull(checker.isMoveLegal(new GoCell(Stone.BLACK, 14, 1)));
		assertNull(checker.isMoveLegal(new GoCell(Stone.WHITE, 14, 0)));
	}
	
	@Test
	public void testIsMoveLegalPrint() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard("src/player_utils/test_data/liberties_board");
		LegalMovesChecker checker;
		
		// this 
		String expected_result = 
				"NYYYYYYYYYYYYYYYYNN\n" + 
				"YYYYYYYYYYYYYYYYYYN\n" +
				"YYNNNYYYYYYYYYYYYYY\n" +
				"YYYYYYYYYYYYYYYYYYY\n" +
				"YYYYYNNYYYYYYYYYYYY\n" +
				"YYYNNNNNYYYYYYYYYYY\n" +
				"YYYYNNNYYYYYYYYYYYY\n" +
				"YYYYYYYYYYYYYYYYYYY\n" +
				"YYYYYYYYNNYYYYYYYYY\n" +
				"YYYYYYYNNNNYYYYYYYY\n" +
				"YYYYYYYYNNYYYYYYYYY\n" +
				"YYYYYYYYYYYYYYYYYYY\n" +
				"YYYYYYYYYYYYYYYNNNN\n" +
				"NNYYYYYYYYYYYYYNYYN\n" +
				"NYNYYYYYYYYYYYYNYNN\n" +
				"NNYYYYYYYYYYYYYNNNN\n" +
				"YYYYYYYYYYYYYYYYYYY\n" +
				"NYYYYYYYYYYYYYNNNNN\n" +
				"NNYYYYYYYYYYYNNNNNN\n";
		String result = "";
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				checker= new LegalMovesChecker(board);
				if (checker.isMoveLegal(new GoCell(Stone.BLACK, i, j)) == null) {
					result += "N";
				} else {
					result += "Y";
				}
			}
			result += "\n";
		}
		assertEquals(expected_result, result);
	}
}
