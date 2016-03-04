package psy.simal.error;

public class ParseException extends Exception{
	public ParseException(){
		super("Error parsing.");
	}
	
	public ParseException(String message){
		super(message);
	}
}
