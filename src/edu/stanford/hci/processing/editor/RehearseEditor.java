package edu.stanford.hci.processing.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import bsh.ConsoleInterface;
import bsh.EvalError;
import bsh.Interpreter;
import bsh.This;
import edu.stanford.hci.processing.RehearsePApplet;
import edu.stanford.hci.processing.StaticModeException;
import processing.app.Base;
import processing.app.Editor;
import processing.app.EditorToolbar;
import processing.app.syntax.JEditTextArea;
import processing.app.syntax.TextAreaPainter;
import processing.app.syntax.TextAreaPainter.Highlight;
import processing.core.PApplet;

public class RehearseEditor extends Editor implements ConsoleInterface {

	private JFrame canvasFrame;
	private RehearsePApplet applet;
	private PrintStream outputStream;

	private HashMap<Integer, Color> lineHighlights
	= new HashMap<Integer, Color>();

	private Interpreter interpreter;

	private ArrayList<Image> snapshots = new ArrayList<Image>();
	private boolean wasLastRunInteractive = false;

	public RehearseEditor(Base ibase, String path, int[] location) {
		super(ibase, path, location);
		getTextArea().getPainter().addCustomHighlight(new RehearseHighlight());

		// TODO: I think this will need to be updated when we open a new document.
		getTextArea().getDocument().addDocumentListener(new RehearseDocumentListener());
	}

	@Override
	public EditorToolbar newEditorToolbar(Editor editor, JMenu menu) {
		return new RehearseEditorToolbar(editor, menu);
	}
	
	@Override
	public void handleRun(boolean present) {
		wasLastRunInteractive = false;
		super.handleRun(present);
	}
	
	@Override
	public void handleStop() {
		if (wasLastRunInteractive) {
			applet.stop();
			canvasFrame.dispose();
		} else {
			super.handleStop();
		}
	}
	
	public void handleInteractiveRun() {
		wasLastRunInteractive = true;
		// clear previous context
		if (canvasFrame != null)
			canvasFrame.dispose();
		if (applet != null)
			applet.stop();

		canvasFrame = new JFrame();
		canvasFrame.setLayout(new BorderLayout());
		canvasFrame.setSize(100, 100);

		applet = new RehearsePApplet();
		//applet.setupFrameResizeListener();
		applet.frame = canvasFrame;
		applet.sketchPath = getSketch().getFolder().getAbsolutePath();
		canvasFrame.add(applet, BorderLayout.CENTER);
		canvasFrame.setVisible(true);
		canvasFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		canvasFrame.addWindowListener(new WindowAdapter() {		
			public void windowClosing(WindowEvent e) {
				applet.stop();
				if (snapshots.size() > 0) {
					RehearseImageViewer viewer = new RehearseImageViewer(snapshots);
					viewer.setVisible(true);
				}
			}
		});
		//canvasFrame.setDefaultCloseOperation();
		//ProcessingMethods methods = new ProcessingMethods(canvas);

		interpreter = new Interpreter(this, applet);

		/* No longer sure this is needed, I think just setting the package solves all problems
		// Add current classpath to the interpreter's classpath
		String classpath = System.getProperty("java.class.path");
		String[] paths = classpath.split(":");
		for (String p : paths) {
			URL processingClassPath = null;
			try {
				processingClassPath = new URL("file://" + p);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (processingClassPath != null) {
				try {
					interpreter.getClassManager().addClassPath(processingClassPath);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		*/

		// now, add our script to the processing.core package
		try {
			interpreter.eval("package processing.core;");
		} catch (EvalError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		String source = super.getText();
//		ExecutionTask task = new ExecutionTask(interpreter, source, output);
//		Thread thread = new Thread(task);
//		thread.start();

		console.clear();
		lineHighlights.clear();
		snapshots.clear();
		getTextArea().repaint();
		try {
			Object obj;
			try {
				obj = interpreter.eval(source, true);
			} catch (StaticModeException e) {
				// Code was written in static mode, let's try again.
				System.out.println("Code written in static mode, wrapping and restarting.");
				// this is kind of gross...
				obj = interpreter.eval("setup() {" + source + "}");
			}
			// This actually starts the program.
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
		
		for (Map.Entry<Integer, Color> entry : lineHighlights.entrySet()) {
//			Color c = entry.getValue();
//			int green = c.getGreen() + (256 - c.getGreen()) / 100;
//			entry.setValue(new Color(c.getRed(), c.getBlue(), green));
			entry.setValue(Color.YELLOW);
		}
		
		lineHighlights.put(line, Color.GREEN);
		getTextArea().repaint();

		// snapshotPoints is zero-indexed, interpreter is one-indexed.
		RehearseLineModel m = 
			(RehearseLineModel)getTextArea().getTokenMarker().getLineModelAt(line - 1);
		if (m != null && m.isPrintPoint) {
			snapshots.add(applet.get().getImage());
		}
	}

	public class TextAreaOutputStream extends OutputStream {
		public void write( int b ) throws IOException {
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
