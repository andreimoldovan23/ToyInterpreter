package model.stmts;

import exceptions.MyException;
import model.PrgState;
import model.adts.IExeStack;
import model.adts.ITypeEnv;
import model.types.Type;

public class Comp implements Stmt{

    private final Stmt s1;
    private final Stmt s2;

    public Comp(Stmt s1, Stmt s2){
        this.s1 = s1;
        this.s2 = s2;
    }

    public PrgState exec(PrgState state){
        IExeStack<Stmt> stack = state.getStack();
        stack.push(s2);
        stack.push(s1);
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        return s2.typeCheck(s1.typeCheck(typeEnv));
    }

    public String toString(){
        return s1.toString() + s2.toString();
    }

    public String toStringPrefix(String prefix){
        return s1.toStringPrefix(prefix) + s2.toStringPrefix(prefix);
    }

}
