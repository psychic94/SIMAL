package psy.simal.parsing;

import java.util.ArrayDeque;

import psy.simal.parsing.Token.TokenType;

public class Parser{
	private ArrayDeque<Token> tokens;
	
	public Parser(ArrayDeque<Token> tokens){
		this.tokens = tokens;
	}
	
	public static CodePart parseLine(ArrayDeque<Token> tokens){
		return (new Parser(tokens).parseLine());
	}
	
	public CodePart parseLine(){
		return null;
	}
	
	//public Expression parseExpression(){
		
	//}
	
	public boolean accept(String target, boolean shouldAdvance){
		String actual = tokens.peekFirst().getValue();
		if(actual.equals(target)){
			if(shouldAdvance)
				tokens.removeFirst();
			return true;
		}else
			return false;
	}
	
	public boolean accept(TokenType targetType, boolean shouldAdvance){
		Token actual = tokens.peekFirst();
		if(actual.isType(targetType)){
			if(shouldAdvance)
				tokens.removeFirst();
			return true;
		}else
			return false;
	}
	
	public boolean expect(String target, boolean shouldAdvance){
		boolean success = accept(target, shouldAdvance);
		if(!success){
			//TODO: error handling
		}
		return success;
	}
	
	public boolean expect(TokenType targetType, boolean shouldAdvance){
		boolean success = accept(targetType, shouldAdvance);
		if(!success){
			//TODO: error handling
		}
		return success;
	}
}
