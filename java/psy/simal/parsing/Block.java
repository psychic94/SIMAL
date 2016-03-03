package psy.simal.parsing;

import java.util.ArrayList;

public abstract class Block extends CodePart{
	public enum BlockType {
		root, model, animation,
		cond_if, cond_else_if, cond_else,
		loop_for, loop_while, loop_until
	}
	
	protected ArrayList<CodePart> code;
	
	public Block(){
		code = new ArrayList<CodePart>();
	}
	
	public abstract void run();
	
	public void addCode(CodePart part){
		code.add(part);
	}
}
