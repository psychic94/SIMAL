package psy.simal.parsing.statements;

import java.util.ArrayList;

import psy.simal.Dictionary;
import psy.simal.Face;
import psy.simal.Point;
import psy.simal.SimalObject;

public class Instantiation<V extends SimalObject> extends Statement{
	public final String identifier;
	//public final HashMap<String, Property> props;
	private final ArrayList<Point> points; 
	
	public Instantiation(int index, String identifier, ArrayList<Point> points){//HashMap<String, Property> props){
		super(index);
		this.identifier = identifier;
		this.points = points;
		//this.props = props;
	}

	@Override
	public void run() {
		// TODO un-hard-code object type
		Dictionary.registerObject(identifier, new Face(points));
	}

	@Override
	public ArrayList<String> debug(int indent) {
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("type: face");
		lines.add("    identifier: " + identifier);
		for(int i=0; i<points.size(); i++){
			lines.add("    point " + i + ": " + points.get(i));
		}
		return lines;
	}
}
