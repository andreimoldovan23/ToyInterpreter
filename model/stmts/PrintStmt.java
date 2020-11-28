package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.exceptions.ThreadException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.IOut;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.ITypeEnv;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.types.Type;
import ToyInterpreter.model.values.Value;

public class PrintStmt implements Stmt{

    private final Exp exp;

    public PrintStmt(Exp e){
        exp = e;
    }

    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> table = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();
        IOut<String> out = state.getOut();
        Value v;
        try{
            v = exp.eval(table, heap);
        }
        catch (MyException e){
            throw new ThreadException(e, state.getId());
        }

        out.add(v.toString());
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        exp.typeCheck(typeEnv);
        return typeEnv;
    }

    public String toString(){
        return "print " + exp.toString() + "\n";
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

}
