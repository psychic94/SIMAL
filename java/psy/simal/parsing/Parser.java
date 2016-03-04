package psy.simal.parsing;

import java.util.ArrayDeque;

import psy.simal.Dictionary;
import psy.simal.error.EndOfLineException;
import psy.simal.error.MissingValueException;
import psy.simal.error.ParseException;
import psy.simal.parsing.Token.TokenType;

//In context free grammar notations:
//() means pick exactly one
//[] means put contents zero or one time(s)
//{} means put contents and number of times
public class Parser{
	private ArrayDeque<Token> tokens;
	//Used to indicate line number in errors
	private int lineNum;
	
	public Parser(ArrayDeque<Token> tokens){
		this(tokens, 1);
	}
	
	public Parser(ArrayDeque<Token> tokens, int lineNum){
		this.tokens = tokens;
		this.lineNum = lineNum;
	}
	
	public static CodePart parseLine(ArrayDeque<Token> tokens){
		return (new Parser(tokens).parseLine());
	}
	
	public CodePart parseLine(){
		return null;
	}
	
	//Context free grammar:
	//expression = product {("+"|"-") product}
	public Expression parseExpression() throws ParseException{
		Expression left = parseProduct();
		while(accept(TokenType.ADD_SUB, false)){
			String operator = tokens.removeFirst().getValue();
			Expression right = parseProduct();
			if(left == null && operator.equals("-")){
				//Negative number
				left = new Expression(new TerminalValue("0"), operator, right);
			}else{
				left = new Expression(left, operator, right);
			}
		}
		return left;
	}
	
	//Context free grammar:
	//product = power {("*"|"/"|"%") power}
	private Expression parseProduct() throws ParseException{
		Expression left = parsePower();
		while(accept(TokenType.MUL_DIV_MOD, false)){
			String operator = tokens.removeFirst().getValue();
			Expression right = parsePower();
			left = new Expression(left, operator, right);
		}
		return left;
	}
	
	//Context free grammar:
	//power = term ["^" term]
	private Expression parsePower() throws ParseException{
		Expression left = parseTerm();
		if(accept("^", true)){
			Expression right = parseTerm();
			return new Expression(left, "^", right);
		}
		return left;
	}
	
	//Context free grammar:
	//term = (ident | number | "(" expression ")")
	private Expression parseTerm() throws ParseException{
		if(accept(TokenType.WORD, false)){
			String word = tokens.peekFirst().getValue();
			if(Dictionary.isIdentifier(word)){
				tokens.removeFirst();
				return Dictionary.getValue(word);
			}
			//No variable found, is it a modifier?
			//TODO: try to exit expression parsing 
			else{
				Token token = tokens.removeFirst();
				throw new MissingValueException(lineNum, token.getIndex());
			}
		}else if(accept(TokenType.NUMBER, false)){
			String number = tokens.removeFirst().getValue();
			return new TerminalValue(number);
		}else if(accept("(", true)){
			Expression expr = parseExpression();
			expect(")", true);
			return expr;
		}else if(tokens.peekFirst() == null){
			throw new EndOfLineException(lineNum);
		}
			
		return null;
	}
	
	public boolean accept(String target, boolean shouldAdvance){
		Token token = tokens.peekFirst();
		if(token == null){
			return false;
		}
		String actual = token.getValue();
		if(actual.equals(target)){
			if(shouldAdvance)
				tokens.removeFirst();
			return true;
		}else
			return false;
	}
	
	public boolean accept(TokenType targetType, boolean shouldAdvance){
		Token actual = tokens.peekFirst();
		if(actual == null){
			return false;
		}
		
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
