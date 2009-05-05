package edu.stanford.hci.processing;

import bsh.EvalError;
import bsh.Interpreter;
import processing.core.PApplet;

public class RehearsePApplet extends PApplet {
	
	Interpreter i;
	
	public void setInterpreter(Interpreter i) {
		this.i = i;
	}
	
	@Override
	public void draw() {
		try {
			i.eval("draw()");
		} catch (EvalError e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void setup() {
		try {
			i.eval("setup()");
		} catch (EvalError e) {
			throw new RuntimeException(e);
		}
	}
}
