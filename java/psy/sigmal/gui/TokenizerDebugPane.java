package psy.sigmal.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import java.util.ArrayDeque;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import psy.sigmal.parsing.Token;
import psy.sigmal.parsing.Tokenizer;

public class TokenizerDebugPane extends TextArea{
	
	public TokenizerDebugPane(){
		super();
		setEditable(false);
		SwingUtilities.invokeLater(new OutputUpdater());
	}
	
	@Deprecated
	public void init(){
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		//setLayout(layout);
		
		//spinner = new JSpinner();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		//add(spinner, c);
		
		//output = new TextArea();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		//add(output, c);
		
		updateSpinner(1);
	}
	
	@Deprecated
	public void updateSpinner(int selected){
		int max = StudioApplet.countInputLines();
		SpinnerNumberModel model;
		if(max == 0){
			model = new SpinnerNumberModel(1, 1, 1, 1);
			//spinner.setEnabled(false);
		}else{
			selected = Math.min(selected, max);
			model = new SpinnerNumberModel(selected, 1, max, 1);
			//spinner.setEnabled(true);
		}
		//spinner.setModel(model);
		//updateOutput();
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
