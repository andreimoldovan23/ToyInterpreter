package model.stmts;

import model.PrgState;
import model.adts.ITypeEnv;
import model.types.Type;

public class NOP implements Stmt{

    public PrgState exec(PrgState state){
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) {
        return typeEnv;
    }

    public String toString(){
        return "No operation \n";
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

}
