package bsh;

public class BSHColorMethodInvocation extends SimpleNode {
	public BSHColorMethodInvocation(int id) {
		super(id);
	}

	BSHArguments getArgsNode() {
		return (BSHArguments) jjtGetChild(0);
	}
	
	public Object evalNode (CallStack callstack, Interpreter interpreter) throws EvalError {
		return new Integer(12); // TODO: need to do the right thing here
	}
}
