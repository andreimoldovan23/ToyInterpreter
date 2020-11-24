package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.InvalidWhileTypeException;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IExeStack;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.types.Bool;
import ToyInterpreter.model.values.Value;

public class WhileStmt implements Stmt{

    private final Exp exp;
    private final Stmt stmt;

    public WhileStmt(Exp e, Stmt s){
        exp = e;
        stmt = s;
    }

    public String toStringPrefix(String prefix) {
        return prefix + "while " + exp.toString() + " then\n" + stmt.toStringPrefix("\t" + prefix);
    }

    public String toString(){
        return "while " + exp.toString() + " then\n" + stmt.toStringPrefix("\t");
    }

    public PrgState exec(PrgState state) throws MyException {
        IExeStack<Stmt> stack = state.getStack();

        Value val = exp.eval(state.getTable(), state.getHeap());
        if(!val.getType().equals(new Bool()))
            throw new InvalidWhileTypeException();

        if((boolean)val.getValue()){
            stack.push(this);
            stack.push(stmt);
        }

        return null;
    }

}
