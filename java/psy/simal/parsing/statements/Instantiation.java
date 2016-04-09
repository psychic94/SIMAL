package psy.simal.parsing.statements;

import java.util.ArrayList;
import java.util.HashMap;

import psy.simal.Property;
import psy.simal.SimalObject;

public class Instantiation<V extends SimalObject> extends Statement{
	public final String identifier;
	public final HashMap<String, Property> props;
	
	public Instantiation(int index, String identifier, HashMap<String, Property> props){
		super(index);
		this.identifier = identifier;
		this.props = props;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<String> debug(int indent) {
		// TODO Auto-generated method stub
		return null;
	}
}
