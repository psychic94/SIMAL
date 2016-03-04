package psy.simal.error;

public class EndOfLineException extends ParseException{
	public EndOfLineException(int line){
		super("Unexpected end of line (line " + line + ").");
	}
}
