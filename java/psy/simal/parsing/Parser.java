package psy.simal.parsing;

import java.util.ArrayDeque;
import java.util.ArrayList;

import psy.simal.Dictionary;
import psy.simal.Value;
import psy.simal.error.EndOfLineException;
import psy.simal.error.MissingValueException;
import psy.simal.error.ParseException;
import psy.simal.parsing.Token.TokenType;
import psy.simal.parsing.statements.Declaration;

//In context free grammar notations:
//() means pick exactly one
//[] means put contents zero or one time(s)
//{} means put contents any number of times
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
	
	public static CodePart parseLine(String line) throws ParseException{
		return parseLine(line, 1);
	}
	
	public static CodePart parseLine(ArrayDeque<Token> tokens) throws ParseException{
		return parseLine(tokens, 1);
	}
	
	public static CodePart parseLine(String line, int lineNum) throws ParseException{
		return parseLine(Tokenizer.tokenizeLine(line, lineNum), lineNum);
	}
	
	public static CodePart parseLine(ArrayDeque<Token> tokens, int lineNum) throws ParseException{
		Parser instance = new Parser(tokens, lineNum);
		//this line temporarily removes the first token so the second can be read
		Token first = tokens.removeFirst();
		Token second = tokens.peekFirst();
		//add the first token back to the front of the dequeue
		//included just in case the copy of tokens encapsulated in instance is also changed
		tokens.addFirst(first);
		if(second.getValue().equals("is") || second.getValue().equals("are"))
			return Declaration.parse(instance);
		else
			throw new ParseException("Unknown statement type at line " + lineNum);
	}
	
	public ArrayDeque<Token> getTokens(){
		return tokens;
	}
	
	//Context free grammar:
	//expression = product {("+"|"-") product}
	public Value parseExpression() throws ParseException{
		Value left = parseProduct();
		while(accept(TokenType.ADD_SUB, false)){
			String operator = tokens.removeFirst().getValue();
			Value right = parseProduct();
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
	private Value parseProduct() throws ParseException{
		Value left = parsePower();
		while(accept(TokenType.MUL_DIV_MOD, false)){
			String operator = tokens.removeFirst().getValue();
			Value right = parsePower();
			left = new Expression(left, operator, right);
		}
		return left;
	}
	
	//Context free grammar:
	//power = term ["^" term]
	private Value parsePower() throws ParseException{
		Value left = parseTerm();
		if(accept("^", true)){
			Value right = parseTerm();
			return new Expression(left, "^", right);
		}
		return left;
	}
	
	//Context free grammar:
	//term = (ident | number | "(" expression ")")
	private Value parseTerm() throws ParseException{
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
			Value expr = parseExpression();
			expect(")", true);
			return expr;
		}else if(tokens.peekFirst() == null){
			throw new EndOfLineException(lineNum);
		}
			
		return null;
	}
	
	//Context-free grammar
	//array = "[" [expression {"," expression}] "]"
	public ArrayList<Value> parseArray() throws ParseException{
		expect("[", true);
		ArrayList<Value> values = new ArrayList<Value>();
		if(accept("]", true))
			return values;
		while(true){
			values.add(parseExpression());
			if(!accept(",", true)){
				break;
			}
		}
		expect("]", true);
		return values;
	}
	
	/**
	 * Tests the next token against a non-required target value
	 * @param target the target token value
	 * @param shouldAdvance will remove the first token if and only if true AND a match is found
	 * @return whether a match was found
	 */
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
	
	/**
	 * Tests the next token against a non-required target type
	 * @param target the target token type
	 * @param shouldAdvance will remove the first token if and only if true AND a match is found
	 * @return whether a match was found
	 */
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
	
	/**
	 * Tests the next token against a required target value
	 * @param target the target token value
	 * @param shouldAdvance will remove the first token if and only if true AND a match is found
	 * @return whether a match was found
	 */	
	public boolean expect(String target, boolean shouldAdvance){
		boolean success = accept(target, shouldAdvance);
		if(!success){
			//TODO: error handling
		}
		return success;
	}
	
	/**
	 * Tests the next token against a required target type
	 * @param target the target token type
	 * @param shouldAdvance will remove the first token if and only if true AND a match is found
	 * @return whether a match was found
	 */
	public boolean expect(TokenType targetType, boolean shouldAdvance){
		boolean success = accept(targetType, shouldAdvance);
		if(!success){
			//TODO: error handling
		}
		return success;
	}
}
