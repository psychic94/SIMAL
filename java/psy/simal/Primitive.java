package psy.simal;

public class Primitive implements Value, Property{
	private String value;
	
	public Primitive(String value){
		this.value = value.trim();
	}
	
	@Override
	public double evalAsNumber() {
		if(value.equals("true"))
			return 1;
		if(value.equals("false"))
			return 0;
		try{
			return Double.parseDouble(value);
		}catch(NumberFormatException e){
			
		}
		return 0;
	}

	@Override
	public String evalAsString() {
		return value;
	}

	@Override
	public boolean evalAsBoolean() {
		if(value.equals("true"))
			return true;
		if(value.equals("false"))
			return false;
		try{
			double temp = Double.parseDouble(value);
			return temp > 0;
		}catch(NumberFormatException e){
			
		}
		return value.length() > 0;
	}

}
