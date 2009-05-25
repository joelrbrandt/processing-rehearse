package edu.stanford.hci;

import bsh.*;

public class BeanShellTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Interpreter i = new Interpreter();
		try {
			i.eval("class Foo { static Bar b; }");
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
