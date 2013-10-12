package board_utils;

import java.io.FileNotFoundException;

import junit.framework.Assert;

import org.junit.Test;

import custom_java_utils.CheckFailException;

public class GoPlayingBoardTest {

	@Test
	public void testGetCountPiecesOnBoard() {
		GoPlayingBoard board = new GoPlayingBoard();
		int expectedCountPlaced = 0;
		for (int i = 0; i < 10; i++) {
			board.setCellAt(0, i, new GoCell(Stone.BLACK));
			expectedCountPlaced++;
			Assert.assertEquals(expectedCountPlaced, board.getCountPiecesOnBoard());
		}
		for (int i = 0; i < 5; i++) {
			board.setCellAt(0, i, new GoCell(Stone.NONE));
			expectedCountPlaced--;
			Assert.assertEquals(expectedCountPlaced, board.getCountPiecesOnBoard());
		}
		
		// only replace empty with empty
		for (int i = 0; i < 5; i++) {
			board.setCellAt(0, i, new GoCell(Stone.NONE));
			Assert.assertEquals(expectedCountPlaced, board.getCountPiecesOnBoard());
		}
	}

	@Test
	public void testGoPlayingBoardParserLegalInput() 
			throws FileNotFoundException, CheckFailException {
		// Empty board
		GoPlayingBoard board = 
				new GoPlayingBoard("src/board_utils/test_data/empty_board");
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				Assert.assertTrue(board.getCellAt(i, j).isEmpty());
			}
		}
		
		board = 
				new GoPlayingBoard("src/board_utils/test_data/diagonal_board");
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (i == j && i % 2 == 0) {
					Assert.assertEquals(
							new GoCell(Stone.BLACK), board.getCellAt(i, j));
				} else if (i == j && i % 2 != 0) {
					Assert.assertEquals(
							new GoCell(Stone.WHITE), board.getCellAt(i, j));
				} else {
					Assert.assertTrue(board.getCellAt(i, j).isEmpty());
				}
			}
		}
	}
	
	@Test(expected=CheckFailException.class)
	public void testGoPlayingBoardParserBadInput() 
			throws FileNotFoundException, CheckFailException {
		new GoPlayingBoard("src/board_utils/test_data/bad_input");
	}

	@Test
	public void testEqualsObject() throws FileNotFoundException, CheckFailException {
		// Empty board
		GoPlayingBoard board = new GoPlayingBoard();
		
		Assert.assertEquals(board, 
				new GoPlayingBoard("src/board_utils/test_data/empty_board"));
		
		// Make board the diagonal board represented in test_data
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (i == j && i % 2 == 0) {
					board.setCellAt(i, j, new GoCell(Stone.BLACK));
				} else if (i == j && i % 2 != 0) {
					board.setCellAt(i, j, new GoCell(Stone.WHITE));
				}
			}
		}
		
		Assert.assertEquals(board, 
				new GoPlayingBoard("src/board_utils/test_data/diagonal_board"));
		
	}

}
