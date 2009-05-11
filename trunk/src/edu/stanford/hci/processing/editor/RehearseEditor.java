package edu.stanford.hci.processing.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import bsh.ConsoleInterface;
import bsh.EvalError;
import bsh.Interpreter;
import edu.stanford.hci.processing.RehearsePApplet;
import processing.app.Base;
import processing.app.Editor;
import processing.app.syntax.JEditTextArea;
import processing.app.syntax.TextAreaPainter;
import processing.app.syntax.TextAreaPainter.Highlight;

public class RehearseEditor extends Editor implements ConsoleInterface {

	private JFrame canvasFrame;
	private RehearsePApplet applet;
	private PrintStream outputStream;
	
	private HashMap<Integer, Color> lineHighlights
		= new HashMap<Integer, Color>();
	
	private Interpreter interpreter;
	
	public RehearseEditor(Base ibase, String path, int[] location) {
		super(ibase, path, location);
		this.setText("void setup() {} \n"
					 + "void draw() {}");
		getTextArea().getPainter().addCustomHighlight(new RehearseHighlight());
		
		// TODO: I think this will need to be updated when we open a new document.
		getTextArea().getDocument().addDocumentListener(new RehearseDocumentListener());
	}
	
	@Override
	public void handleRun(boolean present) {
	
		// clear previous context
		if (canvasFrame != null)
			canvasFrame.dispose();
		if (applet != null)
			applet.stop();
		
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
		lineHighlights.clear();
		getTextArea().repaint();
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
		//getOut().append(o.toString());
		System.out.print(o.toString());
	}

	public void println(Object o) {
		//getOut().append(o.toString() + "\n");	
		System.out.println(o.toString());
	}

	public void notifyLineExecution() {
		int line = interpreter.getLastExecutedLine();
		System.out.println("Last line executed: " + line);
		lineHighlights.put(line, Color.GREEN);
		getTextArea().repaint();
	}
	
	public class TextAreaOutputStream extends OutputStream {
		public void write( int b ) throws IOException {
			System.out.println("TAOS gets call");
			console.message( String.valueOf( ( char )b ), false, false);
		}
	}
	
	private class RehearseHighlight implements TextAreaPainter.Highlight {
		JEditTextArea textarea;
		Highlight next;
		
		public String getToolTipText(MouseEvent evt) {
			if (next != null) {
				return null;
			}
			return null;
		}

		public void init(JEditTextArea textArea, Highlight next) {
			textarea = textArea;
			this.next = next;
		}

		public void paintHighlight(Graphics gfx, int line, int y) {
			// Interpreter uses one-offset, processing uses zero-offset.
			Color c = lineHighlights.get(line + 1);
			if (c != null) {
				FontMetrics fm = textarea.getPainter().getFontMetrics();
				int height = fm.getHeight();
				y += fm.getLeading() + fm.getMaxDescent();
				gfx.setColor(c);
		        gfx.fillRect(0,y,getWidth(),height);
			}
			
			if (next != null) {
				next.paintHighlight(gfx, line, y);
			}
		}
	}
	
	// TODO: it'd be better if it didn't just clear but instead
	// moved the line annotations around.
	class RehearseDocumentListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
			lineHighlights.clear();
			getTextArea().repaint();
		}

		public void insertUpdate(DocumentEvent e) {
			lineHighlights.clear();
			getTextArea().repaint();
		}

		public void removeUpdate(DocumentEvent e) {
			lineHighlights.clear();
			getTextArea().repaint();
		}
		
	}
}
