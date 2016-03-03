package psy.simal.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class StudioApplet extends JFrame implements ActionListener, KeyListener{
	private static StudioApplet instance = null;
	TextArea input;
	TextArea output;
	JMenuBar menuBar;
	JMenu fileMenu;	
	JMenuItem openFile;
	JFileChooser fc;
	File activeFile;
	
	public static void main(String[] args){
		instance = new StudioApplet();
		instance.init();
	}
	
	public StudioApplet(){
		super("SiGMAL Studio");
	}
	
	public void init(){
		addKeyListener(this);
		
		//Start panel setup
		JSplitPane inOutPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		input = new TextArea();
		input.addKeyListener(this);
		inOutPane.setBottomComponent(input);
		
		output = new TokenizerDebugPane();
		inOutPane.setTopComponent(output);
		inOutPane.setResizeWeight(0.5);
		inOutPane.setOneTouchExpandable(true);
		//End panel setup
		
		//Start menu bar setup
		menuBar = new JMenuBar();
		
		//File Menu
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
		openFile = new JMenuItem("Open", KeyEvent.VK_O);
		openFile.addActionListener(this);
		fileMenu.add(openFile);
		//End menu bar setup

		setJMenuBar(menuBar);
		setContentPane(inOutPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setSize(1200, 1000);
		setVisible(true);
		
		fc = new JFileChooser();
		activeFile = null;
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(openFile)){
			int returnVal = fc.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				activeFile = fc.getSelectedFile();
				try{
					BufferedReader br = new BufferedReader(new FileReader(activeFile));
					input.setText("");
					String line;
					while((line = br.readLine()) != null){
						input.append(line + "\n");
					}
				}catch(FileNotFoundException fnfe){
					
				}catch(IOException ioe){
					
				}
			}
		}
	}
	
	public static int countInputLines(){
		return instance.input.getText().split("\n").length + 1;
	}
	
	public static String getInputLine(int line){
		return instance.input.getText().split("\n")[line];
	}
	
	public static int getCaretLine(){
		int loc = instance.input.getCaretPosition();
		return instance.input.getText().substring(0, loc).split("\n").length;
	}

	@Override
	public void keyPressed(KeyEvent event){
		int key = event.getKeyCode();
		if(key == KeyEvent.VK_UP){
			String line = getInputLine(getCaretLine()-1);
			input.setCaretPosition(input.getCaretPosition()-line.length()-1);
		}else if(key == KeyEvent.VK_DOWN){
			String line = getInputLine(getCaretLine()-1);
			input.setCaretPosition(input.getCaretPosition()+line.length()+1);
		}else if(key == KeyEvent.VK_LEFT){
			input.setCaretPosition(input.getCaretPosition()-1);
		}else if(key == KeyEvent.VK_RIGHT){
			input.setCaretPosition(input.getCaretPosition()+1);
		} 
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
