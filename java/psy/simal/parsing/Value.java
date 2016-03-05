package psy.simal.parsing;

public interface Value{
	public double evalAsNumber();
	
	public String evalAsString();
	
	public boolean evalAsBoolean();
}
