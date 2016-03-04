package psy.simal.error;

public class MissingValueException extends ParseException{
	public MissingValueException(){
		super("Value expected.");
	}
	
	public MissingValueException(int line, int col){
		super("Value expected at line " + line + " column " + col + ".");
	}
}
