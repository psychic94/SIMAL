package psy.simal.parsing.statements;

import java.util.ArrayList;

import psy.simal.Face;
import psy.simal.Value;
import psy.simal.error.ParseException;
import psy.simal.parsing.CodePart;
import psy.simal.parsing.Parser;
import psy.simal.parsing.Token.TokenType;

public class DeclarationParser extends StatementParser{
		public CodePart parse(Parser parser) throws ParseException{
			parser.expect(TokenType.WORD, false);
			String ident = parser.getTokens().removeFirst().getValue();
			//don't need to check this token- it was checked before this method was called
			parser.getTokens().removeFirst();
			if(parser.accept("[", false)){
				ArrayList<Value> values = parser.parseArray();
				return new Declaration(parser.getLineNum(), ident, values);
			}else if(parser.accept("a", true)){
				if(parser.accept("Face", true)){
					return Face.parse(ident, parser);
				}
				return null;
			}else{
				Value value = parser.parseExpression();
				return new Declaration(parser.getLineNum(), ident, value);
			}
		}
	}