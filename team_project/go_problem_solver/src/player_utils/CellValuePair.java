package player_utils;

import board_utils.GoCell;

public class CellValuePair implements Comparable<CellValuePair>{
	public GoCell cell;
	public double value;
	public int attackerKoViolations;
	public int defenderKoViolations;
	
	public CellValuePair() {
		this.value = 0;
		this.attackerKoViolations = 0;
		this.defenderKoViolations = 0;
	}
	
	@Override
	public int compareTo(CellValuePair other) {
		int valuesCmp = Double.compare(this.value, other.value);
		if (valuesCmp != 0) {
			return valuesCmp;
		} 
		return Integer.compare(this.attackerKoViolations - this.defenderKoViolations, 
				other.attackerKoViolations - other.defenderKoViolations);
	}
}