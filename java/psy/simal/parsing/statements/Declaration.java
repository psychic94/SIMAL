package psy.simal.parsing.statements;

import java.util.ArrayList;

import psy.simal.Face;
import psy.simal.Value;
import psy.simal.error.ParseException;
import psy.simal.parsing.CodePart;
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
	
	public static CodePart parse(Parser parser) throws ParseException{
		parser.expect(TokenType.WORD, false);
		String ident = parser.getTokens().removeFirst().getValue();
		//don't need to check this token- it was checked before this method was called
		parser.getTokens().removeFirst();
		if(parser.accept("[", false)){
			ArrayList<Value> values = parser.parseArray();
			return new Declaration(parser.getLineNum(), ident, values);
		}else if(parser.accept("a", true)){
			if(parser.accept("Face", true)){
				return Face.parse(parser);
			}
			return null;
		}else{
			Value value = parser.parseExpression();
			return new Declaration(parser.getLineNum(), ident, value);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void debug(int indent) {
		// TODO Auto-generated method stub

	}

}
