package psy.simal.parsing.statements;

import psy.simal.error.ParseException;
import psy.simal.parsing.CodePart;
import psy.simal.parsing.Parser;

public abstract class StatementParser{
	public abstract CodePart parse(Parser parser) throws ParseException;
}
