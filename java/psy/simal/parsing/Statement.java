package psy.simal.parsing;

public abstract class Statement extends CodePart{
	protected final int index;
	
	public Statement(int index){
		this.index = index;
	}

	public int getIndex(){
		return index;	
	}
}
