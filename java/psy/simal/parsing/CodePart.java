package psy.simal.parsing;

import java.util.ArrayList;

public abstract class CodePart {
	public abstract void run();
	public abstract ArrayList<String> debug(int indent);
}
