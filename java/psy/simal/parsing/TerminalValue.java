package psy.simal.parsing;

public class TerminalValue extends Expression{
	private final String value;
	
	public TerminalValue(String value){
		super(null, "", null);
		this.value = value;
	}
	
	@Override
	public double evalAsNumber(){
		try{
			return Double.parseDouble(value);
		}catch(NumberFormatException e){
			return 0;
		}
	}
	
	public String toString(){
		return value;
	}
}