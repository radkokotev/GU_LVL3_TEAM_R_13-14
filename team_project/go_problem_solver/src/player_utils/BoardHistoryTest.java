package player_utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;

/**
 * A unit test for BoardHistory
 */
public class BoardHistoryTest {

	/**
	 * Tests a trivial case when the two boards are empty
	 */
	@Test
	public void testEmptyBoards() {
		BoardHistory history = BoardHistory.getSingleton();
		GoPlayingBoard board1 = new GoPlayingBoard();
		history.add(board1);
		GoPlayingBoard board2 = new GoPlayingBoard();
		assertTrue(history.hasBeenPlayed(board2));
	}

	/**
	 * Comparing non colliding boards
	 */
	@Test
	public void testNonCollidingMoves() {
		BoardHistory history = BoardHistory.getSingleton();
		GoPlayingBoard board1 = new GoPlayingBoard();
		
		board1.setCellAt(0, 0, new GoCell(Stone.BLACK));
		board1.setCellAt(0, 1, new GoCell(Stone.WHITE));
		history.add(board1);
		
		GoPlayingBoard board2 = new GoPlayingBoard();
		board2.setCellAt(1, 1, new GoCell(Stone.BLACK));
		board2.setCellAt(1, 2, new GoCell(Stone.WHITE));
		assertFalse(history.hasBeenPlayed(board2));
	}
	
	/**
	 * Make some moves and then compare with a board that collides with them
	 */
	@Test
	public void testCollidingMoves() {
		BoardHistory history = BoardHistory.getSingleton();
		GoPlayingBoard board1 = new GoPlayingBoard();
		board1.setCellAt(0, 0, new GoCell(Stone.BLACK));
		history.add(board1);
		
		board1.setCellAt(0, 1, new GoCell(Stone.WHITE));
		history.add(board1);
		
		GoPlayingBoard board2 = new GoPlayingBoard();
		board2.setCellAt(0, 0, new GoCell(Stone.BLACK));
		assertTrue(history.hasBeenPlayed(board2));
		
		board2.setCellAt(0, 1, new GoCell(Stone.WHITE));
		assertTrue(history.hasBeenPlayed(board2));
	}
}
