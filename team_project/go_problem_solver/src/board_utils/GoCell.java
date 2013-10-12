package board_utils;

/**
 * A  Go specific representation of a cell 
 */
public class GoCell extends Cell<Stone> {
	private Stone stone;
	
	/**
	 * Constructor a cell with no stone in it.
	 */
	public GoCell() {
		this.stone = Stone.NONE;
	}
	
	/**
	 * Constructor for a cell containing the given stone
	 * @param the stone to be placed in that cell
	 */
	public GoCell(Stone stone) {
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
		return this.stone == other.stone;
	}

}
