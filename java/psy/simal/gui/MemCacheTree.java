package psy.simal.gui;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import psy.simal.Face;
import psy.simal.Point;
import psy.simal.Primitive;
import psy.simal.SimalObject;
import psy.simal.Value;
import psy.simal.Value.Type;

public class MemCacheTree extends JTree{
	private DefaultMutableTreeNode primitives, arrays, objects;
	
	public MemCacheTree(){
		super();
		primitives = new DefaultMutableTreeNode("Primitives");
		arrays = new DefaultMutableTreeNode("Arrays");
		objects = new DefaultMutableTreeNode("Objects");
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Memory Cache");
		root.add(primitives);
		root.add(arrays);
		root.add(objects);
		this.setModel(new DefaultTreeModel(root));
	}
	
	public void updatePrimitives(HashMap<String, Primitive> prims){
		primitives.removeAllChildren();
		for(String name : prims.keySet()){
			Primitive prim = prims.get(name);
			String text = name + ": ";
			Type type = prim.getPresumedType();
			switch(type){
				case STRING:
					text += "\"" + prim.evalAsString() + "\"";
					break;
				case BOOLEAN:
					text += prim.evalAsString();
					break;
				case NULL:
					text += prim.evalAsString();
					break;
				case NUMBER:
					text += prim.evalAsNumber();
					break;
			}
			DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(text);
			primitives.add(leaf);
		}
		((DefaultTreeModel)getModel()).reload();
	}
	
	public void updateArrays(HashMap<String, ArrayList<Value>> arrayHash){
		arrays.removeAllChildren();
		for(String name : arrayHash.keySet()){
			ArrayList<Value> array = arrayHash.get(name);
			DefaultMutableTreeNode branch = new DefaultMutableTreeNode(name);
			for(int i=0; i<array.size(); i++){
				String text = i + ": \"" + array.get(i).evalAsString() + "\"";
				branch.add(new DefaultMutableTreeNode(text));
			}
			arrays.add(branch);
		}
		((DefaultTreeModel)getModel()).reload();
	}
	
	public void updateObjects(HashMap<String, SimalObject> objs){
		objects.removeAllChildren();
		for(String name : objs.keySet()){
			DefaultMutableTreeNode branch = new DefaultMutableTreeNode(name);
			branch.add(new DefaultMutableTreeNode("type: face"));
			DefaultMutableTreeNode pointBranch = new DefaultMutableTreeNode("points:");
			Face face = (Face)objs.get(name);
			ArrayList<Point> points = face.getPoints();
			for(Point point : points){
				pointBranch.add(new DefaultMutableTreeNode(point));
				branch.add(pointBranch);
			}
			objects.add(branch);
		}
		((DefaultTreeModel)getModel()).reload();
	}
}
