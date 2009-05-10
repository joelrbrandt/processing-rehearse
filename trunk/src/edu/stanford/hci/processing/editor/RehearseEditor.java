package edu.stanford.hci.processing.editor;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.JFrame;

import bsh.ConsoleInterface;
import bsh.EvalError;
import bsh.Interpreter;
import edu.stanford.hci.processing.RehearsePApplet;
import processing.app.Base;
import processing.app.Editor;

public class RehearseEditor extends Editor implements ConsoleInterface {

	private JFrame canvasFrame;
	private RehearsePApplet applet;
	private PrintStream outputStream;
	private Interpreter interpreter;
	
	public RehearseEditor(Base ibase, String path, int[] location) {
		super(ibase, path, location);
		this.setText("void setup() {} \n"
					 + "void draw() {}");
	}
	
	@Override
	public void handleRun(boolean present) {
		// clear previous context
		if (canvasFrame != null)
			canvasFrame.dispose();
		
		canvasFrame = new JFrame();
		canvasFrame.setLayout(new BorderLayout());
		canvasFrame.setSize(500, 500);
		
		applet = new RehearsePApplet();
		canvasFrame.add(applet, BorderLayout.CENTER);
		canvasFrame.setVisible(true);
		canvasFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		canvasFrame.addWindowListener(new WindowAdapter() {		
			public void windowClosing(WindowEvent e) {
				applet.stop();
			}
		});
		//canvasFrame.setDefaultCloseOperation();
		//ProcessingMethods methods = new ProcessingMethods(canvas);
		
		interpreter = new Interpreter(this, applet);
		
		String source = super.getText();
//		ExecutionTask task = new ExecutionTask(interpreter, source, output);
//		Thread thread = new Thread(task);
//		thread.start();
		
		console.clear();
		try {
			// TODO: right now assumes that source has setup() and draw()
			// so this line just registers setup(), draw() and other
			// user-defined functions.
			Object obj = interpreter.eval(source);
			applet.init();
			
			if (obj != null)
				console.message(obj.toString(), false, false);
		} catch (EvalError e) {
			console.message(e.toString(), true, false);
			e.printStackTrace();
		}
	}
	
	public void error(Object o) {
		getOut().append(o.toString() + "\n");
	}

	public PrintStream getErr() {
		return outputStream;
	}

	public Reader getIn() {
		return new StringReader("");
	}

	public PrintStream getOut() {
		return outputStream;
	}

	public void print(Object o) {
		getOut().append(o.toString());
	}

	public void println(Object o) {
		getOut().append(o.toString() + "\n");	
	}

	public class TextAreaOutputStream extends OutputStream {
		public void write( int b ) throws IOException {
			System.out.println("TAOS gets call");
			console.message( String.valueOf( ( char )b ), false, false);
		}
	}
	

}
