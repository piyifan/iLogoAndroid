package com.iLogo.driver;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;

import com.iLogo.translate.Translator;
import com.iLogo.tree.Tree;
import com.iLogo.typecheck.TypeCheck;



import com.iLogo.error.BaseError;

import com.iLogo.frontend.Lexer;
import com.iLogo.frontend.Parser;
import com.iLogo.iCode.Functy;


public class Driver {
	
	private Lexer lexer;

	private Parser parser;
	
	private static Driver driver;
	
	private List<BaseError> errors;
	
	Translator translator; 
	
	private static String InputFile;
	
	private boolean compileError;
	
	private Activity window;

	private boolean init() {
			if (InputFile == null || InputFile.length() == 0) {
				new AlertDialog.Builder(window).setTitle("Compilation Error")
				.setMessage("File not found!")
				.setPositiveButton("Confirm", null).show();  
				return false;
			}
	    	try {
				lexer = new Lexer(new BufferedInputStream(new FileInputStream(InputFile)));
			} catch (FileNotFoundException e) {
				new AlertDialog.Builder(window).setTitle("Compilation Error")
				.setMessage("File not found!")
				.setPositiveButton("Confirm", null).show();  
				return false;
			}
	    	parser = new Parser();
	    	lexer.setParser(parser);
	    	parser.setLexer(lexer);
	    	errors = new ArrayList<BaseError>();
	    	compileError = false;
	    	return true;
	}
	
	public void issueError(BaseError error) {
		errors.add(error);
	}

	private void checkPoint() {
		if (errors.size() == 0) return;
		Collections.sort(errors, new Comparator<BaseError>() {

				@Override
				public int compare(BaseError o1, BaseError o2) {
					return o1.getLocation().compareTo(o2.getLocation());
				}

		});
		new AlertDialog.Builder(window).setTitle("Compilation Error")
		.setMessage(errors.get(0).toString())
		.setPositiveButton("Confirm", null).show();  
		compileError = true;
	}
	
	public static Driver getDriver() {
		return driver;
	}

	private boolean compile() {
		
		Tree.TopLevel tree = parser.parseFile();
		checkPoint();
		if (compileError) return false;
		//IndentPrintWriter pw = new IndentPrintWriter(System.out, 4);
		//tree.printTo(pw);
		
		TypeCheck.checkType(tree);
		checkPoint();
		if (compileError) return false;
		
		translator = new Translator();
		translator.visitTopLevel(tree);
		translator.printTo();
		return true;
		
	}
	
	public ArrayList<Functy> run(String inputFile, Activity window)
	{
		this.window = window;
		InputFile = inputFile;
		if (!driver.init()) return null;
		if (driver.compile())
			return translator.returnFunctys();
		else
			return null;
	}

	public static void setDriver() {
		driver = new Driver();
	}

}
