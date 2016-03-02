package psy.sigmal.parsing;

public class Token{
	public enum TokenType{
		IDENT, KEYWORD, ACTION, MODIFIER,
		OPEN_PAREN, CLOSE_PAREN,
		ADD_SUB, MUL_DIV_MOD, POWER, COND_OP, CONJUNCTION,
		SYMBOL, STRING, NUMBER,WORD
	}
	
	private TokenType type;
	private final String value;
	private final int index;
	
	public Token(TokenType type, String value, int index){
		this.type = type;
		this.value = value;
		this.index = index;
	}
	
	public boolean isType(TokenType testType){
		return type.equals(testType);
	}
	
	public TokenType getType(){
		return type;
	}
	
	public String getValue(){
		return value;
	}
	
	public int getIndex(){
		return index;
	}
	
	public String toString(){
		String str = "";
		str += index;
		while(str.length()<4)
			str += " ";
		str += type;
		while(str.length()<24)
			str += " ";
		str += value;
		return str;
	}
}
