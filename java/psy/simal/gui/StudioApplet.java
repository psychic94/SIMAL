package psy.simal.gui;

import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import psy.simal.compiling.FbxCompiler;
import psy.simal.compiling.StlCompiler;
import psy.simal.error.ParseException;
import psy.simal.parsing.CodePart;
import psy.simal.parsing.Parser;

public class StudioApplet extends JFrame implements ActionListener, KeyListener{
	private static StudioApplet instance = null;
	JSplitPane cacheIoPane, inOutPane;
	TextArea input;
	TextArea output;
	MemCacheTree cacheTree;
	JMenuBar menuBar;
	JMenu fileMenu, debugMenu;	
	JMenuItem openFile, export, singleCommand;
	JRadioButtonMenuItem tokenizeMode, parseMode;
	JFileChooser openFC, saveFC, exportFC;
	ButtonGroup debugModeGroup;
	File activeFile;
	FileNameExtensionFilter txtFilter, simaFilter, stlFilter, fbxFilter;
	
	public static void main(String[] args){
		instance = new StudioApplet();
		instance.init();
	}
	
	public StudioApplet(){
		super("SiGMAL Studio");
	}
	
	public void init(){
		addKeyListener(this);

		//Start menu bar setup
		menuBar = new JMenuBar();
		
		//File Menu
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
		openFile = new JMenuItem("Open", KeyEvent.VK_O);
		openFile.addActionListener(this);
		fileMenu.add(openFile);
		export = new JMenuItem("Export", KeyEvent.VK_E);
		export.addActionListener(this);
		fileMenu.add(export);
		
		//Debug Menu
		debugModeGroup = new ButtonGroup();
		debugMenu = new JMenu("Debug");
		debugMenu.setMnemonic(KeyEvent.VK_D);
		menuBar.add(debugMenu);
		tokenizeMode = new JRadioButtonMenuItem("Tokenizer");
		tokenizeMode.setMnemonic(KeyEvent.VK_T);
		tokenizeMode.setSelected(true);
		tokenizeMode.addActionListener(this);
		debugModeGroup.add(tokenizeMode);
		debugMenu.add(tokenizeMode);
		parseMode = new JRadioButtonMenuItem("Parser");
		parseMode.setMnemonic(KeyEvent.VK_P);
		parseMode.addActionListener(this);
		debugModeGroup.add(parseMode);
		debugMenu.add(parseMode);
		singleCommand = new JMenuItem("Run Single Command");
		singleCommand.setMnemonic(KeyEvent.VK_R);
		singleCommand.addActionListener(this);
		debugMenu.add(singleCommand);
		//End menu bar setup
		
		//Start panel setup
		inOutPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		input = new TextArea();
		input.addKeyListener(this);
		input.setFont(new Font("Serif", Font.PLAIN, 24));
		inOutPane.setBottomComponent(input);
		
		output = new TokenizerDebugPane();
		inOutPane.setTopComponent(output);
		inOutPane.setResizeWeight(0.5);
		inOutPane.setOneTouchExpandable(true);
		
		cacheIoPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		cacheTree = new MemCacheTree();
		cacheIoPane.setLeftComponent(cacheTree);
		cacheIoPane.setRightComponent(inOutPane);
		cacheIoPane.setResizeWeight(0.2);
		//End panel setup

		//Start file chooser setup
		txtFilter = new FileNameExtensionFilter("Text File (.txt, .text)", "txt", "text");
		simaFilter = new FileNameExtensionFilter("SIMAL Code File (.sima)", "sima");
		stlFilter = new FileNameExtensionFilter("STL File (.stl)", "stl");
		fbxFilter = new FileNameExtensionFilter("Maya Object File (.fbx)", "fbx");
		
		openFC = new JFileChooser();
		openFC.addChoosableFileFilter(txtFilter);
		openFC.addChoosableFileFilter(simaFilter);

		saveFC = new JFileChooser();
		saveFC.addChoosableFileFilter(txtFilter);
		saveFC.addChoosableFileFilter(simaFilter);
		
		exportFC = new JFileChooser();
		exportFC.setAcceptAllFileFilterUsed(false);
		exportFC.addChoosableFileFilter(stlFilter);
		//exportFC.addChoosableFileFilter(fbxFilter);
		//End file chooser setup
		
		setJMenuBar(menuBar);
		setContentPane(cacheIoPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setSize(1200, 1000);
		setVisible(true);
		
		activeFile = null;
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(openFile)){
			int returnVal = openFC.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				activeFile = openFC.getSelectedFile();
				try{
					BufferedReader br = new BufferedReader(new FileReader(activeFile));
					input.setText("");
					String line;
					while((line = br.readLine()) != null){
						input.append(line + "\n");
					}
					br.close();
				}catch(FileNotFoundException fnfe){
					
				}catch(IOException ioe){
					
				}
			}
		}else if(e.getSource().equals(export)){
			int returnVal = exportFC.showSaveDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				String name = exportFC.getSelectedFile().getName();
				if(exportFC.getFileFilter().equals(stlFilter)){
					String[] lines = input.getText().split("\n");
					if(!name.endsWith(".stl")) name += ".stl";
					StlCompiler.compile(lines, name);
				}else if(exportFC.getFileFilter().equals(fbxFilter)){
					//String[] lines = input.getText().split("\n");
					//if(!name.endsWith(".fbx")) name += ".fbx";
					//FbxCompiler.compile(lines, name);
				}else
					return;
				
				
			}
		}else if(e.getSource().equals(tokenizeMode) && !(output instanceof TokenizerDebugPane)){
			output = new TokenizerDebugPane();
			inOutPane.setTopComponent(output);
			//setContentPane(inOutPane);
		}else if(e.getSource().equals(parseMode) && !(output instanceof ParserDebugPane)){
			output = new ParserDebugPane();
			inOutPane.setTopComponent(output);
			//setContentPane(inOutPane);
		}else if(e.getSource().equals(singleCommand)){
			String cmd = JOptionPane.showInputDialog("Command to run:");
			try{
				CodePart codePart = Parser.parseLine(cmd);
				codePart.run();
			}catch(ParseException exception){
				exception.printStackTrace();
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
		try{
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
		}catch(IllegalArgumentException e){
			
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
	
	public static int getDebugMode(){
		if(instance.tokenizeMode.isSelected())
			return 0;
		if(instance.parseMode.isSelected())
			return 1;
		else
			return -1;
	}
	
	public static MemCacheTree getCacheTree(){
		return instance.cacheTree;
	}
}
