package psy.simal.parsing;

import psy.simal.Dictionary;

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
			if(Dictionary.isPrimitive(value)){
				return Dictionary.getValue(value).evalAsNumber();
			}
			return 0;
		}
	}
	
	public String toString(){
		return value;
	}
}