package psy.simal.gui;

import java.awt.TextArea;
import java.util.ArrayDeque;

import javax.swing.SwingUtilities;

import psy.simal.parsing.Token;
import psy.simal.parsing.Tokenizer;

public class TokenizerDebugPane extends TextArea{
	
	public TokenizerDebugPane(){
		super();
		setEditable(false);
		SwingUtilities.invokeLater(new OutputUpdater());
	}
	
	private class OutputUpdater implements Runnable{
		private int lastLine;
		
		public void run(){
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
			ArrayDeque<Token> tokens = Tokenizer.tokenizeLine(line, lineNum);
			setText(tokens.size() + " tokens:");
			for(Token token : tokens){
				append("\n" + token);
			}

			SwingUtilities.invokeLater(this);
		}
	}
}
