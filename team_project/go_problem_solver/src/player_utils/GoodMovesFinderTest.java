package player_utils;

import static org.junit.Assert.*;

import org.junit.Test;

import custom_java_utils.CheckFailException;
import board_utils.GoCell;
import board_utils.GoPlayingBoard;
import board_utils.Stone;

public class GoodMovesFinderTest {

	@Test
	public void testGetGoodMoves() throws CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard();
		for(int i = 0; i < 19; i++)
			for(int j = 0; j < 19; j++) 
				board.setCellAt(i, j, new GoCell(Stone.INNER_BORDER, i, j));
		board.setCellAt(0, 0, new GoCell(Stone.WHITE, 0, 0));
		board.setCellAt(0, 1, new GoCell(Stone.WHITE, 0, 1));
		board.setCellAt(1, 0, new GoCell(Stone.BLACK, 1, 0));
		board.setCellAt(1, 1, new GoCell(Stone.NONE, 1, 1));
		board.setCellAt(0, 2, new GoCell(Stone.NONE, 0, 2));
		board.setCellAt(1, 2, new GoCell(Stone.NONE, 1, 2));
		board.setToPlayNext(Stone.BLACK);
		board.setFirstPlayerColour(Stone.BLACK);
		board.setSecondPlayerColour(Stone.WHITE);
		GoodMovesFinder finder = new GoodMovesFinder(board);
		GoCell[] correct = new GoCell[3];
		correct[0] = new GoCell(Stone.BLACK, 1, 1);
		correct[1] = new GoCell(Stone.BLACK, 0, 2);
		correct[2] = new GoCell(Stone.BLACK, 1, 2);
		GoCell[] found = finder.getGoodMoves();
		//assertArrayEquals(correct, found);
		//TODO Fix this test
	}
	
	@Test
	public void testDoesLookLikeEye() throws CheckFailException {
		GoPlayingBoard board = new GoPlayingBoard();
		for(int i = 0; i < 19; i++)
			for(int j = 0; j < 19; j++) 
				board.setCellAt(i, j, new GoCell(Stone.INNER_BORDER, i, j));
		board.setCellAt(1, 0, new GoCell(Stone.WHITE, 1, 0));
		board.setCellAt(0, 1, new GoCell(Stone.WHITE, 0, 1));
		board.setCellAt(2, 1, new GoCell(Stone.WHITE, 2, 1));
		board.setCellAt(1, 2, new GoCell(Stone.WHITE, 1, 2));
		board.setCellAt(1, 1, new GoCell(Stone.NONE, 1, 1));
		board.setToPlayNext(Stone.WHITE);
		GoodMovesFinder finder = new GoodMovesFinder(board);
		assertTrue(finder.testDoesLookLikeEye(new GoCell(Stone.NONE, 1, 1), board));
	}
	
}