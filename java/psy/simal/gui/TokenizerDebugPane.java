package psy.simal.gui;

import java.awt.Font;
import java.awt.TextArea;
import java.util.ArrayDeque;

import javax.swing.SwingUtilities;

import psy.simal.error.EndOfLineException;
import psy.simal.parsing.Token;
import psy.simal.parsing.Tokenizer;

public class TokenizerDebugPane extends TextArea{
	
	public TokenizerDebugPane(){
		super();
		this.setFont(new Font("Serif", Font.PLAIN, 24));
		setEditable(false);
		SwingUtilities.invokeLater(new OutputUpdater());
	}
	
	private class OutputUpdater implements Runnable{
		private int lastLine;
		
		public void run(){
			if(StudioApplet.getDebugMode()!=0){
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
				setText(tokens.size() + " tokens:");
				for(Token token : tokens){
					append("\n" + token);
				}
			}catch(EndOfLineException e){
				System.out.println(e.getMessage());
			}

			SwingUtilities.invokeLater(this);
		}
	}
}
