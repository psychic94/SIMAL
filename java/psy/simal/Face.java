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
	
	public ArrayList<Byte> getBytes(){
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		for(int i=1; i<points.size()-1; i++){
			Point ab = new Point(
					(points.get(i).getX() - points.get(0).getX()),
					(points.get(i).getY() - points.get(0).getY()),
					(points.get(i).getZ() - points.get(0).getZ())
					);
			Point ac = new Point(
					(points.get(i+1).getX() - points.get(0).getX()),
					(points.get(i+1).getY() - points.get(0).getY()),
					(points.get(i+1).getZ() - points.get(0).getZ())
					);
			Point normal = new Point(
					(ab.getY()*ac.getZ() - ab.getZ()*ac.getY()),
					(ab.getZ()*ac.getX() - ab.getX()*ac.getZ()),
					(ab.getX()*ac.getY() - ab.getY()*ac.getX())
					);
			double normalLength = Math.sqrt(Math.pow(normal.getX(),2)+Math.pow(normal.getY(),2)+Math.pow(normal.getZ(),2));
			Point unitNormal = new Point(
					(float)(normal.getX()/normalLength),
					(float)(normal.getY()/normalLength),
					(float)(normal.getZ()/normalLength)
					);
			for(int j=0; j<12; j++)
				bytes.add(unitNormal.getBytes()[j]);
			for(int j=0; j<12; j++)
				bytes.add(points.get(0).getBytes()[j]);
			for(int j=0; j<12; j++)
				bytes.add(points.get(i).getBytes()[j]);
			for(int j=0; j<12; j++)
				bytes.add(points.get(i+1).getBytes()[j]);
			bytes.add((byte)0);
			bytes.add((byte)0);
		}
		return bytes;
	}
	
	public ArrayList<Point> getPoints(){
		return points;
	}
}
