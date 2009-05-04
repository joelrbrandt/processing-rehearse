package edu.stanford.hci;

import bsh.*;

public class BeanShellTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int in = Integer.parseInt("34");
		System.out.println("my int is: " + in);
		Interpreter i = new Interpreter();
		try {
			i.eval("color another = #ffee00; color inside = color(204,102,0); System.out.println(\"my color is: \" + inside + \" and my another color is \" + another);");
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
