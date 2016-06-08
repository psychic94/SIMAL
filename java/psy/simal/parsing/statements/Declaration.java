package psy.simal.parsing.statements;

import java.util.ArrayList;

import psy.simal.Dictionary;
import psy.simal.Face;
import psy.simal.Value;
import psy.simal.error.ParseException;
import psy.simal.parsing.CodePart;
import psy.simal.parsing.Expression;
import psy.simal.parsing.Parser;
import psy.simal.parsing.Token.TokenType;

public class Declaration extends Statement{
	private final String identifier;
	private Value scalarValue;
	private ArrayList<Value> arrayValues;
	private boolean scalar;
	
	public Declaration(int index, String identifier, Value value){
		super(index);
		this.identifier = identifier;
		this.scalarValue = value;
		this.scalar = true;
	}
	
	public Declaration(int index, String identifier, ArrayList<Value> values){
		super(index);
		this.identifier = identifier;
		values.trimToSize();
		this.arrayValues = values;
		this.scalar = false;
	}

	@Override
	public void run() {
		if(scalar)
			if(scalarValue instanceof Expression)
				Dictionary.setPrimitive(identifier, Double.toString(scalarValue.evalAsNumber()));
			else{
				Dictionary.setPrimitive(identifier, scalarValue.evalAsString());
			}
		else
			Dictionary.registerArray(identifier, arrayValues);
	}

	@Override
	public ArrayList<String> debug(int indent){
		ArrayList<String> lines = new ArrayList<String>();
		if(scalar){
			lines.add("type: primitive declaration");
			lines.add("    identifier: " + identifier);
			lines.add("    value: " + scalarValue);
		}else{
			lines.add("type: array declaration");
			lines.add("    identifier: " + identifier);
			for(int i=0; i<arrayValues.size(); i++){
				lines.add("    value " + i + ": " + arrayValues.get(i));
			}
		}
		return lines;
	}
}
