package ToyInterpreter.model.stmts;

import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IExeStack;

public class CompStmt implements Stmt{

    private final Stmt s1;
    private final Stmt s2;

    public CompStmt(Stmt s1, Stmt s2){
        this.s1 = s1;
        this.s2 = s2;
    }

    public PrgState exec(PrgState state){
        IExeStack<Stmt> stack = state.getStack();
        stack.push(s2);
        stack.push(s1);
        return state;
    }

    public String toString(){
        return s1.toString() + s2.toString();
    }

    public String toStringPrefix(String prefix){
        return s1.toStringPrefix(prefix) + s2.toStringPrefix(prefix);
    }

}
