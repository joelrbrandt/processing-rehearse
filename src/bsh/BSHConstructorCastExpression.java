package bsh;

import java.lang.reflect.Type;

public class BSHConstructorCastExpression extends SimpleNode {
	public Type castToType = null;

	public BSHConstructorCastExpression(int id) {
		super(id);
	}

	BSHArguments getArgsNode() {
		return (BSHArguments)jjtGetChild(0);
	}
	
	public Object evalNode( CallStack callstack, Interpreter interpreter ) throws EvalError {
		BSHArguments a = getArgsNode();
		System.out.println(a.toString());
		return new Integer(12); // TODO: need to do the right thing here
	}

}
