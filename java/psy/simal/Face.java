package psy.simal;

import java.util.ArrayList;

import psy.simal.parsing.Parser;
import psy.simal.parsing.statements.Instantiation;

public class Face extends SimalObject implements Property{
	private ArrayList<Point> points;
	
	public Face(ArrayList<Point> points){
		this.points = points;
	}
	
	public static Instantiation<Face> parse(String ident, Parser parser){
		ArrayList<Property> props = new ArrayList<Property>();
		//if(parser.accept("with", true)){
		//	  while(true){
		//		String name = parser.getTokens().removeFirst().getValue();
		//		if(name.equals("bounds")){
		//			parseBounds(parser);
		//		}
		//	}
		//}
		parser.expect("with", true);
		parser.expect("bounds", true);
		ArrayList<Point> newPoints = parseBounds(parser);
		return new Instantiation<Face>(0, ident, newPoints);
	}
	
	private static ArrayList<Point> parseBounds(Parser parser){
		parser.expect("[", true);
		ArrayList<Point> points = new ArrayList<Point>();
		//if(parser.accept("]", true))
			//TODO: invalid face
		while(true){
			String token = parser.getTokens().removeFirst().getValue().trim();
			if(Dictionary.isArray(token)){
				//TODO: check array length
				float x = (float)Dictionary.getArrayPart(token, 0).evalAsNumber();
				float y = (float)Dictionary.getArrayPart(token, 1).evalAsNumber();
				float z = (float)Dictionary.getArrayPart(token, 2).evalAsNumber();
				Point point = new Point(x, y, z);
				points.add(point);
			}
			if(!parser.accept(",", true)){
				break;
			}
		}
		parser.expect("]", true);
		//TODO: enforce 3-point minimum
		return points;
	}
	
	public ArrayList<Point> getPoints(){
		return points;
	}
}
