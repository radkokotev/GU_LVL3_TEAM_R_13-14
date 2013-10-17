/**
 * 
 */
package board_utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * A unit test for the class GoCell.
 */
public class GoCellTest {

	/**
	 * Test method for {@link board_utils.GoCell#isEmpty()}.
	 */
	@Test
	public void testIsEmpty() {
		GoCell cell;
		cell = new GoCell();
		assertTrue(cell.isEmpty());
		cell.setContent(Stone.BLACK);
		assertFalse(cell.isEmpty());
		cell.setContent(Stone.INNER_BORDER);
		assertTrue(cell.isEmpty());
	}


	/**
	 * Test method for {@link board_utils.GoCell#toString()}.
	 */
	@Test
	public void testToString() {
		GoCell cell;
		cell = new GoCell(Stone.NONE, 0, 0);
		assertEquals("NONE", cell.toString());
		cell = new GoCell(Stone.BLACK, 0, 0);
		assertEquals("BLACK", cell.toString());
		cell = new GoCell(Stone.WHITE, 0, 0);
		assertEquals("WHITE", cell.toString());
		cell = new GoCell(Stone.INNER_BORDER, 0, 0);
		assertEquals("INNER_BORDER", cell.toString());
	}

	/**
	 * Test method for {@link board_utils.GoCell#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		GoCell other;
		
		// Testing equality
		other = new GoCell(Stone.NONE, 0, 0);
		GoCell one;
		one = new GoCell(Stone.NONE, 0, 0);
		assertTrue(one.equals(other));
		other.setContent(Stone.BLACK);
		one.setContent(Stone.BLACK);
		assertTrue(one.equals(other));
		other.setContent(Stone.WHITE);
		one.setContent(Stone.WHITE);
		assertTrue(one.equals(other));
		other = new GoCell(Stone.INNER_BORDER, 0, 0);
		one.setContent(Stone.INNER_BORDER);
		assertTrue(one.equals(other));
		
		// Testing non-equal
		other.setContent(Stone.NONE);
		one.setContent(Stone.BLACK);
		assertFalse(one.equals(other));
		other.setContent(Stone.WHITE);
		assertFalse(one.equals(other));
		other.setContent(Stone.INNER_BORDER);
		assertFalse(one.equals(other));
	}

}
