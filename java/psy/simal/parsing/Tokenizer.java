package psy.simal.parsing;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

import psy.simal.Dictionary;
import psy.simal.parsing.Token.TokenType;

public class Tokenizer{
	private static Stack<Block> hierarchy;
	private static Statement lastStmnt;
	
	public static Block parseFile(File file){
		lastStmnt = null;
		hierarchy = new Stack<Block>();
		// TODO: iterate through file line by line and use parseLine() on each
		return null;
	}
	
	@Deprecated
	public static void handleHierarchy(String line){
		int pointer = 0;
		
		int indent = 0;	
		
		//Allow tabs and spaces in the indent.....
				while(line.charAt(pointer) == '\t' || line.charAt(pointer) == ' '){
					//... but count only the tabs
					if(line.charAt(pointer) == '\t'){
						indent++;
					}
					pointer++;
				}
				
				//If this is the first statement, don't do anything with it yet
				if(lastStmnt != null){
					

					/* If the indent is less than or equal to the nesting depth (minus the root)
					 * the last line must've be en in the topmost block
					 * If the indent is strictly less than the nesting depth (minus the root)
					 * one or more blocks must've ended with the last line
					 * pop blocks and add them to the parent until the nesting depth matches the indent
					 */
					if(indent <= hierarchy.size()-1){
						hierarchy.peek().addCode(lastStmnt);
						while(indent < hierarchy.size()-1){
							Block block = hierarchy.pop();
							hierarchy.peek().addCode(block);
						}
					}
					/* If the indent is one more than the nesting depth (minus the root)
					 * the last line must've been a block header
					 * initialize a block with the last statement as a header, then push it to the stack
					 */
					else if(indent == hierarchy.size()){
						//Block block = new Block(lastStmnt);
						//hierarchy.push(block);
					}
				}
	}

	public static ArrayDeque<Token> tokenizeLine(String line, int index){
		int pointer = 0;
		
		ArrayDeque<Token> tokens = new ArrayDeque<Token>();
		int tokenStart = pointer;
		
		while(pointer < line.length()){
			char character = line.charAt(pointer);
			if(character == '('){
				tokens.addLast(new Token(TokenType.OPEN_PAREN, "(", tokenStart));
				//System.out.println(tokens.get(tokens.size()-1));
				pointer++;
				tokenStart = pointer;
			}else if(character == ')'){
				tokens.addLast(new Token(TokenType.CLOSE_PAREN, ")", tokenStart));
				//System.out.println(tokens.get(tokens.size()-1));
				pointer++;
				tokenStart = pointer;
			}else if(character == '+'){
				tokens.addLast(new Token(TokenType.ADD_SUB, "+", tokenStart));
				//System.out.println(tokens.get(tokens.size()-1));
				pointer++;
				tokenStart = pointer;
			}else if(character == '-'){
				tokens.addLast(new Token(TokenType.ADD_SUB, "-", tokenStart));
				//System.out.println(tokens.get(tokens.size()-1));
				pointer++;
				tokenStart = pointer;
			}else if(character == '*'){
				tokens.addLast(new Token(TokenType.MUL_DIV_MOD, "*", tokenStart));
				//System.out.println(tokens.get(tokens.size()-1));
				pointer++;
				tokenStart = pointer;
			}else if(character == '/'){
				tokens.addLast(new Token(TokenType.MUL_DIV_MOD, "/", tokenStart));
				//System.out.println(tokens.get(tokens.size()-1));
				pointer++;
				tokenStart = pointer;
			}else if(character == '%'){
				tokens.addLast(new Token(TokenType.MUL_DIV_MOD, "%", tokenStart));
				//System.out.println(tokens.get(tokens.size()-1));
				pointer++;
				tokenStart = pointer;
			}else if(character == '^'){
				tokens.addLast(new Token(TokenType.POWER, "^", tokenStart));
				//System.out.println(tokens.get(tokens.size()-1));
				pointer++;
				tokenStart = pointer;
			}else if(character == '!'){
				pointer++;
				if(pointer >= line.length()){
					//Error: Unexpected end of line
				}else if(line.charAt(pointer) != '='){
					//Error: Expected '='
				}else{
					tokens.addLast(new Token(TokenType.COND_OP, "!=", tokenStart));
					//System.out.println(tokens.get(tokens.size()-1));
					pointer++;
					tokenStart = pointer;
				}
			}else if(character == '>'){
				pointer++;
				if(pointer < line.length() && line.charAt(pointer) == '='){
					tokens.addLast(new Token(TokenType.COND_OP, ">=", tokenStart));
					//System.out.println(tokens.get(tokens.size()-1));
					pointer++;
					tokenStart = pointer;
				}else{
					tokens.addLast(new Token(TokenType.COND_OP, ">", tokenStart));
					//System.out.println(tokens.get(tokens.size()-1));
					tokenStart = pointer;
				}
			}else if(character == '<'){
				pointer++;
				if(pointer < line.length() && line.charAt(pointer) == '='){
					tokens.addLast(new Token(TokenType.COND_OP, "<=", tokenStart));
					//System.out.println(tokens.get(tokens.size()-1));
					pointer++;
					tokenStart = pointer;
				}else{
					tokens.addLast(new Token(TokenType.COND_OP, "<", tokenStart));
					//System.out.println(tokens.get(tokens.size()-1));
					tokenStart = pointer;
				}
			}else if(character == '='){
				tokens.addLast(new Token(TokenType.COND_OP, "=", tokenStart));
				//System.out.println(tokens.get(tokens.size()-1));
				pointer++;
				tokenStart = pointer;
			}else if(Character.isDigit(character)){
				boolean hasDecimal = false;
				tokenStart=(pointer++);
				while(true){
					character = line.charAt(pointer);
					if(Character.isDigit(character)){
					}else if(character == '.'){
						if(hasDecimal){
							//Error: Invalid number
						}else{
							hasDecimal = true;
						}
					}else{
						tokens.addLast(new Token(TokenType.NUMBER, line.substring(tokenStart, pointer), tokenStart));
						//System.out.println(tokens.get(tokens.size()-1));
						tokenStart = pointer;
						break;
					}
					pointer++;
				}
			}else if(Character.isWhitespace(character)){
				pointer++;
				tokenStart = (pointer);
			}else if(Character.isLetter(character)){
				do{
					pointer++;
					try{
						character = line.charAt(pointer);
					}catch(StringIndexOutOfBoundsException e){
						break;
					}
				}while(Character.isLetterOrDigit(character) || character == '_');
				String str = line.substring(tokenStart, pointer);
				
				Dictionary dict = Dictionary.getInstance();
				if(dict.isKeyword(str)){
					tokens.addLast(new Token(TokenType.KEYWORD, str, tokenStart));
					//System.out.println(tokens.get(tokens.size()-1));
				}else if(dict.isAction(str)){
					tokens.addLast(new Token(TokenType.ACTION, str, tokenStart));
					//System.out.println(tokens.get(tokens.size()-1));
				}else if(dict.isIdentifier(str)){
					tokens.addLast(new Token(TokenType.IDENT, str, tokenStart));
					//System.out.println(tokens.get(tokens.size()-1));
				}else{
					tokens.addLast(new Token(TokenType.WORD, str, tokenStart));
					//System.out.println(tokens.get(tokens.size()-1));
				}
				tokenStart = (pointer);
				// TODO add new identifier in right context
				// TODO ask if str is a modifier for appropriate action
				// TODO colon
			}else if(character == ':'){
				tokens.addLast(new Token(TokenType.SYMBOL, ":", tokenStart));
				//System.out.println(tokens.get(tokens.size()-1));
				pointer++;
				tokenStart = pointer;
			}else if(character == ','){
				tokens.addLast(new Token(TokenType.SYMBOL, ",", tokenStart));
				//System.out.println(tokens.get(tokens.size()-1));
				pointer++;
				tokenStart = pointer;
			}else{
				System.out.println("Unknown symbol: " + character);
				break;
			}
		}
		
		//tokens.addLast(new Token(TokenType.LINE_END, "", line.length()));
		return tokens;
	}
	
	public static void main(String[] args){
		String line = "";
		for(String arg : args){
			line += arg + " ";
		}
		line.trim();
		ArrayDeque<Token> tokens = tokenizeLine(line, 1);

		System.out.println(tokens.size() + " tokens");
//		for(Token token : tokens){
//			System.out.println(token);
//		}
	}
}
