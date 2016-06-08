package psy.simal.compiling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import psy.simal.error.ParseException;
import psy.simal.parsing.CodePart;
import psy.simal.parsing.Parser;

public class FbxCompiler extends SimalCompiler{
	public static void compile(String[] lines, String filename){
		for(String line : lines){
			try{
				CodePart code = Parser.parseLine(line);
				code.run();
			}catch(ParseException e){
			}
		}
		
		
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
			Calendar now = Calendar.getInstance();
			
			bw.write("; Created by SIMAL Studio beta 1\n");
			bw.write("; ------------------------------\n\n");
			
			bw.write("FBXHeaderExtension:  {\n");
			bw.write("\tFBXHeaderVersion:  1003\n");
			bw.write("\tFBXVersion:  6100\n");
			bw.write("\tCreationTimeStamp:  {\n");
			bw.write("\t\tVersion: 1000\n");
			String year = ""+now.get(Calendar.YEAR);
			bw.write("\t\tYear:  " + year + "\n");
			int num = now.get(Calendar.MONTH) + 1;
			String month = num < 10 ? "0" + num : ""+num;
			bw.write("\t\tMonth:  " + month + "\n");
			num = now.get(Calendar.DAY_OF_MONTH);
			String day = num < 10 ? "0" + num : ""+num;
			bw.write("\t\tDay:  " + day + "\n");
			num = now.get(Calendar.HOUR_OF_DAY);
			String hour = num < 10 ? "0" + num : ""+num;
			bw.write("\t\tHour:  " + hour + "\n");
			num = now.get(Calendar.MINUTE);
			String minute = num < 10 ? "0" + num : ""+num;
			bw.write("\t\tMinute:  " + minute + "\n");
			num = now.get(Calendar.SECOND);
			String second = num < 10 ? "0" + num : ""+num;
			bw.write("\t\tSecond:  " + second + "\n");
			String milli = ""+now.get(Calendar.MILLISECOND);
			bw.write("\t\tMillisecond:  " + milli + "\n");
			bw.write("\t}\n");
			
		}catch(IOException e){
			
		}
	}
}
