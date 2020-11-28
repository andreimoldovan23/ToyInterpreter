package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.InvalidIfCondition;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.exceptions.ThreadException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IExeStack;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.ITypeEnv;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.types.Bool;
import ToyInterpreter.model.types.Type;
import ToyInterpreter.model.values.Value;

public class IfStmt implements Stmt{

    private final Exp e;
    private final Stmt s1;
    private final Stmt s2;

    public IfStmt(Exp e, Stmt s1, Stmt s2){
        this.e = e;
        this.s1 = s1;
        this.s2 = s2;
    }

    public PrgState exec(PrgState state) throws ThreadException {
        IExeStack<Stmt> stack = state.getStack();
        ISymTable<String, Value> table = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();
        Value v;

        try{
            v = e.eval(table, heap);
        }
        catch (MyException e){
            throw new ThreadException(e, state.getId());
        }

        if (v.getType().equals(new Bool())) {
            if ((Boolean) v.getValue()) {
                stack.push(s1);
            } else
                stack.push(s2);
        }
        else
            throw new ThreadException(new InvalidIfCondition(), state.getId());
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        Type t = e.typeCheck(typeEnv);
        if(!t.equals(new Bool()))
            throw new InvalidIfCondition();
        s1.typeCheck(typeEnv.copy());
        s2.typeCheck(typeEnv.copy());
        return typeEnv;
    }

    public String toString(){
        return "if " + e.toString() + " then \n" +
                s1.toStringPrefix("\t") + "else \n" + s2.toStringPrefix("\t");
    }

    public String toStringPrefix(String prefix){
        return prefix + "if " + e.toString() + " then \n" +
                s1.toStringPrefix(prefix + "\t") + prefix + "else \n" + s2.toStringPrefix(prefix + "\t");
    }

}
