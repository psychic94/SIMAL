package psy.simal;

public interface Value{
	public double evalAsNumber();
	
	public String evalAsString();
	
	public boolean evalAsBoolean();
	
	public Type getPresumedType();
	
	public enum Type{
		NUMBER, STRING, BOOLEAN, NULL
	}
}
