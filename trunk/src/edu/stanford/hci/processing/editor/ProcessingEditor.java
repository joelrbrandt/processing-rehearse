package edu.stanford.hci.processing.editor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import edu.stanford.hci.processing.ProcessingMethods;

import bsh.ConsoleInterface;
import bsh.EvalError;
import bsh.Interpreter;

public class ProcessingEditor extends JFrame implements ActionListener, ConsoleInterface {
	JButton runButton;
	JTextArea textArea;
	JTextArea output;

	JFrame canvasFrame;
	Canvas canvas;
	
	PrintStream outputStream;
	
	Interpreter interpreter;

	public ProcessingEditor() {
		super();
		textArea = new JTextArea();
		textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		setSize(400, 650);
		getContentPane().add(textArea, BorderLayout.CENTER);

		runButton = new JButton("Run");
		getContentPane().add(runButton, BorderLayout.NORTH);

		output = new JTextArea();
		getContentPane().add(output, BorderLayout.SOUTH);
		output.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		output.setEditable(false);

		outputStream = new PrintStream(new TextAreaOutputStream());
		runButton.addActionListener(this);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ProcessingEditor editor = new ProcessingEditor();
		editor.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(runButton)) {
			// clear previous context
			if (canvasFrame != null)
				canvasFrame.dispose();
			
			canvasFrame = new JFrame();
			canvasFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			canvasFrame.setBounds(700, 0, 100, 100);
			
			canvas = new Canvas();
			canvasFrame.getContentPane().add(canvas);
			canvas.setSize(500,500);
			canvasFrame.pack();
			
			canvasFrame.setVisible(true);
			ProcessingMethods methods = new ProcessingMethods(canvas, canvasFrame);
			
			interpreter = new Interpreter(this, methods, canvas);
			String source = textArea.getText();
			try {	
				Object obj = interpreter.eval(source);
				if (obj != null)
					output.setText(obj.toString());
				
			} catch (EvalError e) {
				output.setText(e.toString());
			}
		}
	}

	@Override
	public void error(Object o) {
		getOut().append(o.toString() + "\n");
	}

	@Override
	public PrintStream getErr() {
		return outputStream;
	}

	@Override
	public Reader getIn() {
		return new StringReader("");
	}

	@Override
	public PrintStream getOut() {
		return outputStream;
	}

	@Override
	public void print(Object o) {
		getOut().append(o.toString());
	}

	@Override
	public void println(Object o) {
		getOut().append(o.toString() + "\n");	
	}

	public class TextAreaOutputStream extends OutputStream {
		public void write( int b ) throws IOException {
			System.out.println("TAOS gets call");
			output.append( String.valueOf( ( char )b ) );
		}
	}
}
