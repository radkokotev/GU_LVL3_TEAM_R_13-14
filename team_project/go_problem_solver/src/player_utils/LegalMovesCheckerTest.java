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
		assertEquals(4, checker.getLiberties(board.getCellAt(4, 6)));
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
		GoPlayingBoard board = new GoPlayingBoard("src/player_utils/test_data/liberties_board");
		LegalMovesChecker checker = new LegalMovesChecker(board);
		assertTrue(checker.isMoveLegal(new GoCell(Stone.BLACK, 0, 1)));
		checker = new LegalMovesChecker(board);
		assertTrue(checker.isMoveLegal(new GoCell(Stone.WHITE, 4, 4)));
		checker = new LegalMovesChecker(board);
		assertTrue(checker.isMoveLegal(new GoCell(Stone.WHITE, 12, 12)));
		checker = new LegalMovesChecker(board);
		assertFalse(checker.isMoveLegal(new GoCell(Stone.WHITE, 12, 18)));
		checker = new LegalMovesChecker(board);
		assertTrue(checker.isMoveLegal(new GoCell(Stone.BLACK, 14, 16)));
		checker = new LegalMovesChecker(board);
		assertFalse(checker.isMoveLegal(new GoCell(Stone.BLACK, 14, 18)));
		checker = new LegalMovesChecker(board);
		assertFalse(checker.isMoveLegal(new GoCell(Stone.BLACK, 0, 18)));
		checker = new LegalMovesChecker(board);
		assertTrue(checker.isMoveLegal(new GoCell(Stone.WHITE, 0, 18)));
		checker = new LegalMovesChecker(board);
		assertFalse(checker.isMoveLegal(new GoCell(Stone.BLACK, 0, 0)));
		checker = new LegalMovesChecker(board);
		assertFalse(checker.isMoveLegal(new GoCell(Stone.WHITE, 21, 5)));
		checker = new LegalMovesChecker(board);
		assertFalse(checker.isMoveLegal(new GoCell(Stone.BLACK, 5, 25)));
		checker = new LegalMovesChecker(board);
		assertFalse(checker.isMoveLegal(new GoCell(Stone.WHITE, -4, 30)));
		checker = new LegalMovesChecker(board);
	}

	@Test
	public void testIsMoveLegalWithKo() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard("src/player_utils/test_data/liberties_board");
		LegalMovesChecker checker = new LegalMovesChecker(board);
		BoardHistory history = BoardHistory.getSingleton();
		history.add(board); 
		assertTrue(checker.isMoveLegal(new GoCell(Stone.BLACK, 14, 1)));
		assertFalse(checker.isMoveLegal(new GoCell(Stone.WHITE, 14, 0)));
		assertTrue(checker.isMoveLegal(new GoCell(Stone.WHITE, 14, 18)));
		assertTrue(checker.isMoveLegal(new GoCell(Stone.BLACK, 3, 16)));
		assertTrue(checker.isMoveLegal(new GoCell(Stone.WHITE, 3, 15)));
	}
	
	@Test
	public void testIsMoveLegalPrint() throws FileNotFoundException, CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard("src/player_utils/test_data/liberties_board");
		LegalMovesChecker checker;
		
		// this 
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
				"NYNYYYYYYYYYYYYNYNN\n" +
				"NNYYYYYYYYYYYYYNNNN\n" +
				"YYYYYYYYYYYYYYYYYYY\n" +
				"NYYYYYYYYYYYYYNNNNN\n" +
				"NNYYYYYYYYYYYNNNNNN\n";
		String result = "";
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				checker= new LegalMovesChecker(board);
				if (!checker.isMoveLegal(new GoCell(Stone.BLACK, i, j))) {
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
