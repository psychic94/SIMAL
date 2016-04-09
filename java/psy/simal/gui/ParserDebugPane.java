package psy.simal.gui;

import java.awt.TextArea;
import java.util.ArrayDeque;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import psy.simal.error.EndOfLineException;
import psy.simal.error.ParseException;
import psy.simal.parsing.CodePart;
import psy.simal.parsing.Parser;
import psy.simal.parsing.Token;
import psy.simal.parsing.Tokenizer;

public class ParserDebugPane extends TextArea{
	
	public ParserDebugPane(){
		super();
		setEditable(false);
		SwingUtilities.invokeLater(new OutputUpdater());
	}
	
	private class OutputUpdater implements Runnable{
		private int lastLine;
		
		public void run(){
			if(StudioApplet.getDebugMode()!=1){
				return;
			}
			int lineNum = StudioApplet.getCaretLine();
			if(lineNum == lastLine){
				SwingUtilities.invokeLater(this);
				return;
			}
			lastLine = lineNum;
			String line;
			try{
				 line = StudioApplet.getInputLine(lineNum-1);
			}catch(ArrayIndexOutOfBoundsException e){
				SwingUtilities.invokeLater(this);
				return;
			}
			try{
				ArrayDeque<Token> tokens = Tokenizer.tokenizeLine(line, lineNum);
				CodePart code = Parser.parseLine(tokens, lineNum);
				setText("");
				if(code!=null){
					ArrayList<String> lines = code.debug(0);
					for(String str : lines){
						append(str + "\n");
					}
				}
			}catch(EndOfLineException e){
				System.out.println(e.getMessage());
			}catch(ParseException e){
				System.out.println(e.getMessage());
			}

			SwingUtilities.invokeLater(this);
		}
	}
}
