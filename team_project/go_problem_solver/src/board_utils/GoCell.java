package board_utils;

/**
 * A  Go specific representation of a cell 
 */
public class GoCell extends Cell<Stone> implements Comparable<GoCell> {
	private Stone stone;
	private int x;
	private int y;
	
	/**
	 * Constructor a cell with no stone in it.
	 */
	public GoCell() {
		this.stone = Stone.NONE;
		this.x = -1;
		this.y = -1;
	}
	
	/**
	 * Constructor for a cell containing the given stone
	 * @param the stone to be placed in that cell
	 */
	public GoCell(Stone stone, int x, int y) {
		this.stone = stone;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return Vertical coordinates of the cell
	 */
	public int x() {
		return this.x;
	}

	/**
	 * @return Horizontal coordinates of the cell 
	 */
	public int y() {
		return this.y;
	}
	
	/**
	 * Determines if the cells have stones of opposite kinds
	 * @param a first cell
	 * @param b second cell
	 * @return true if one cell contains a white and the other one a black stone,
	 * false - otherwise
	 */
	public static boolean areOposite(GoCell a, GoCell b) {
		return (a.stone == Stone.WHITE && b.stone == Stone.BLACK) ||
				(a.stone == Stone.BLACK && b.stone == Stone.WHITE);
	}
	
	@Override
	public boolean isEmpty() {
		return this.stone == Stone.NONE;
	}

	@Override
	public Stone getContent() {
		return this.stone;
	}
	
	/**
	 * Changes the stone that is currently in the given cell.
	 * @param stone the value of the new stone to be placed in the cell.
	 */
	public void setContent(Stone stone) {
		this.stone = stone;
	}
	
	@Override
	public String toString() {
		return stone.name();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) { 
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		
		GoCell other = (GoCell) obj;
		return this.stone == other.stone && this.x == other.x && this.y == other.y;
	}
	
	@Override
	public GoCell clone(){
		return new GoCell(this.stone, this.x, this.y);
	}

	@Override
	public int compareTo(GoCell other) {
		int result;
		result = this.x - other.x;
		if (result != 0) {
			return result;
		}
		result = this.y - other.y;
		if (result != 0) {
			return result;
		}
		result = this.stone.ordinal() - other.stone.ordinal();
		if (result != 0) {
			return result;
		}
		return 0;
	}

}
