package psy.simal.compiling;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import psy.simal.Dictionary;
import psy.simal.Face;
import psy.simal.error.ParseException;
import psy.simal.parsing.CodePart;
import psy.simal.parsing.Parser;

public class StlCompiler extends SimalCompiler{
	public static void compile(String[] lines, String filename){
		for(String line : lines){
			try{
				CodePart code = Parser.parseLine(line);
				code.run();
			}catch(ParseException e){
				
			}
		}
		ArrayList<Byte> bytes = getBytes();
		
		byte[] out = new byte[bytes.size()];
		for(int i=0; i<bytes.size(); i++){
			out[i] = bytes.get(i);
		}
		
		File file = new File(filename);
		try{
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(out, 0, out.length);
			fos.close();
		}catch(IOException ioe){
			
		}
	}
	
	
	
	public static ArrayList<Byte> getBytes(){
		Calendar now = Calendar.getInstance();
		//Generate header text
		String header = "solid Generated by SiGMAL beta 1 on ";
		header += now.get(Calendar.DATE) + "/";
		header += (now.get(Calendar.MONTH) + 1) + "/";
		header += now.get(Calendar.YEAR) + " at ";
		header += now.get(Calendar.HOUR) + ":";
		header += now.get(Calendar.MINUTE) + ":";
		header += now.get(Calendar.SECOND) + " ";
		header += now.get(Calendar.AM_PM)==0? "AM" : "PM";
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		byte[] strBytes = header.getBytes();
		//Pad header to proper length
		for(int i=0; i<80; i++)
			bytes.add(i<strBytes.length ? strBytes[i] : 0x20);
		//Add face count
		ArrayList<Face> faces = Dictionary.allFaces();
		ArrayList<Byte> bytes2 = new ArrayList<Byte>();
		for(Face face : faces){
			bytes2.addAll(face.getBytes());
		}
		byte[] faceCount = SimalCompiler.intToBytes(bytes2.size()/50);
		for(int i=0; i<4; i++){
			bytes.add(faceCount[i]);
		}
		bytes.addAll(bytes2);
		return bytes;
	}
}
