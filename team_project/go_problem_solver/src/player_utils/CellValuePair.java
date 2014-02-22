package player_utils;

import board_utils.GoCell;

public class CellValuePair implements Comparable<CellValuePair>{
	public GoCell cell;
	public double value;
	
	@Override
	public int compareTo(CellValuePair other) {
		return Double.compare(this.value, other.value);
	}
}