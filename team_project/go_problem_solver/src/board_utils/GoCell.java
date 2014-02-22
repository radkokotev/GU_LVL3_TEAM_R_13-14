package board_utils;

/**
 * A  Go specific representation of a cell 
 */
public class GoCell extends Cell<Stone> implements Comparable<GoCell> {
	private Stone stone;
	private int verticalCoordinate;
	private int horizontalCoordinate;
	
	/**
	 * Constructor a cell with no stone in it.
	 */
	public GoCell() {
		this.stone = Stone.NONE;
		this.verticalCoordinate = -1;
		this.horizontalCoordinate = -1;
	}
	
	/**
	 * Constructor for a cell containing the given stone
	 * @param the stone to be placed in that cell
	 */
	public GoCell(Stone stone, int verticalCoordinate, int horizontalCoordinate) {
		this.stone = stone;
		this.verticalCoordinate = verticalCoordinate;
		this.horizontalCoordinate = horizontalCoordinate;
	}
	
	/**
	 * @return Vertical coordinates of the cell
	 */
	public int getVerticalCoordinate() {
		return this.verticalCoordinate;
	}

	/**
	 * @return Horizontal coordinates of the cell 
	 */
	public int getHorizontalCoordinate() {
		return this.horizontalCoordinate;
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
		return stone.name() + " " + verticalCoordinate + " " + horizontalCoordinate;
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
		return this.stone == other.stone && 
				this.verticalCoordinate == other.verticalCoordinate && 
				this.horizontalCoordinate == other.horizontalCoordinate;
	}
	
	@Override
	public GoCell clone(){
		return new GoCell(this.stone, this.verticalCoordinate, this.horizontalCoordinate);
	}

	@Override
	public int compareTo(GoCell other) {
		int result;
		result = this.verticalCoordinate - other.verticalCoordinate;
		if (result != 0) {
			return result;
		}
		result = this.horizontalCoordinate - other.horizontalCoordinate;
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
