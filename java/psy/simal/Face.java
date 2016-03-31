package psy.simal;

import java.util.ArrayList;

import psy.simal.parsing.Parser;
import psy.simal.parsing.statements.Instantiation;

public class Face extends SimalObject implements Property{
	private Point[] points;
	
	public Face(Point[] points){
		this.points = points;
	}
	
	public static Instantiation<Face> parse(Parser parser){
		ArrayList<Property> props = new ArrayList<Property>();
		if(parser.accept("with", true)){
			while(true){
				String name = parser.getTokens().removeFirst().getValue();
				if(name.equals("bounds")){
					parseBounds(parser);
				}
			}
		}
		return null;
	}
	
	private static Point[] parseBounds(Parser parser){
		return null;
	}
}
