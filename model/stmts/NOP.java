package ToyInterpreter.model.stmts;

import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.ITypeEnv;
import ToyInterpreter.model.types.Type;

public class NOP implements Stmt{

    public PrgState exec(PrgState state){
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) {
        return typeEnv;
    }

    public String toString(){
        return "No operation\n";
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

}
