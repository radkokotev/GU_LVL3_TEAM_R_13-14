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
		this(Stone.BLACK, Type.COMPUTER);
	}
	
	public Player(Stone stone){
		this(stone, Type.COMPUTER);
	}
	
	public Player(Type t){
		this(Stone.BLACK, t);
	}
	
	public void setAlgorithmName(String name){
		algorithmName = name;
	}
}
