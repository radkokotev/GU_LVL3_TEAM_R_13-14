package board_utils;

public class Player {
	public enum Type { HUMAN, COMPUTER };
	public Stone colour;
	public Type type;
	public String algorithmName;
		
	public Player(Stone stone, Type t){
		colour = stone;
		type = t;
	}
	
	public Player(){
		this(Stone.BLACK, Type.HUMAN);
	}
	
	public Player(Stone stone){
		this(stone, Type.HUMAN);
	}
	
	public Player(Type t){
		this(Stone.BLACK, t);
	}
	
	public void setAlgorithmName(String name){
		algorithmName = name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!o.getClass().equals(this.getClass())) {
			return false;
		}
		Player other = (Player) o;
		return this.colour.equals(other.colour);
		
	}
}
