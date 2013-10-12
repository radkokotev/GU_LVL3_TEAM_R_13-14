package board_utils;

/**
 * A  Go specific representation of a cell 
 */
public class GoCell implements Cell<Stone> {
	private int x_coord;
	private int y_coord;
	private Stone stone;
	
	/**
	 * Constructor for the specified cell with no stone in it.
	 * @param x_coord horizontal coordinate
	 * @param y_coord vertical coordinate
	 */
	public GoCell(int x_coord, int y_coord) {
		this.x_coord = x_coord;
		this.y_coord = y_coord;
		this.stone = Stone.NONE;
	}
	
	/**
	 * Constructor for the specified cell by placing the given stone
	 * @param x_coord horizontal coordinate
	 * @param y_coord vertical coordinate
	 * @param stone the stone to be placed in that cell
	 */
	public GoCell(int x_coord, int y_coord, Stone stone) {
		this.x_coord = x_coord;
		this.y_coord = y_coord;
		this.stone = stone;
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
	public int horizontalCoord() {
		return this.x_coord;
	}

	@Override
	public int verticalCoord() {
		return this.y_coord;
	}


}
