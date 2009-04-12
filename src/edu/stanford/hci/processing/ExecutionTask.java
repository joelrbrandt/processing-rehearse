package edu.stanford.hci.processing;

import javax.swing.JTextArea;

import bsh.EvalError;
import bsh.Interpreter;

public class ExecutionTask implements Runnable {

	private Interpreter interpreter;
	private String source;
	private JTextArea output;
	
	public ExecutionTask(Interpreter interpreter, String source, 
			JTextArea output) {
		this.interpreter = interpreter;
		this.source = source;
		this.output = output;
	}
	
	public void run() {
		System.out.println("Run calleddddd");
		output.setText("");
		try {	
			Object obj = interpreter.eval(source);
			if (obj != null)
				output.append(obj.toString());
			
		} catch (EvalError e) {
			output.append(e.toString());
			e.printStackTrace();
		}
		System.out.println("Run 2");
		output.append("\n + Line numbers executed:");
		for (Integer i : interpreter.getLineNumberSet()) {
			output.append(" " + i);
		}
		System.out.println("Run 3");
	}

}
