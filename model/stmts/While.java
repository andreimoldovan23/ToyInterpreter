package model.stmts;

import exceptions.InvalidWhileType;
import exceptions.MyException;
import exceptions.ThreadException;
import model.PrgState;
import model.adts.IExeStack;
import model.adts.ITypeEnv;
import model.exps.Exp;
import model.types.Bool;
import model.types.Type;
import model.values.Value;

public class While implements Stmt{

    private final Exp exp;
    private final Stmt stmt;

    public While(Exp e, Stmt s){
        exp = e;
        stmt = s;
    }

    public String toStringPrefix(String prefix) {
        return prefix + "while " + exp.toString() + " then\n" + stmt.toStringPrefix("\t" + prefix);
    }

    public String toString(){
        return "while " + exp.toString() + " then\n" + stmt.toStringPrefix("\t");
    }

    public PrgState exec(PrgState state) throws ThreadException {
        IExeStack<Stmt> stack = state.getStack();

        Value val;
        try{
            val = exp.eval(state.getTable(), state.getHeap());
        }
        catch (MyException e){
            throw new ThreadException(e, state.getId());
        }

        if(!val.getType().equals(new Bool()))
            throw new ThreadException(new InvalidWhileType(), state.getId());

        if((boolean)val.getValue()){
            stack.push(this);
            stack.push(stmt);
        }

        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        Type t = exp.typeCheck(typeEnv);
        if(!t.equals(new Bool()))
            throw new InvalidWhileType();

        return stmt.typeCheck(typeEnv.copy());
    }

}
