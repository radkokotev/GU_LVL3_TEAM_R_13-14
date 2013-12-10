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
		board.setCellAt(0, 0, new GoCell(Stone.WHITE, 0, 0));
		board.setCellAt(1, 0, new GoCell(Stone.BLACK, 1, 0));
		for(int i = 0; i < 19; i++)
			for(int j = 0; j < 19; j++) 
				if(i > 1 || j > 1)
					board.setCellAt(i, j, new GoCell(Stone.INNER_BORDER, i, j));
		board.setToPlayNext(Stone.BLACK);
		GoodMovesFinder finder = new GoodMovesFinder(board);
		GoCell[] correct = new GoCell[2];
		correct[0] = new GoCell(Stone.BLACK, 0, 1);
		correct[1] = new GoCell(Stone.BLACK, 1, 1);
		GoCell[] found = finder.getGoodMoves();
		assertArrayEquals(correct, found);
	}
}