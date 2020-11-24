package ToyInterpreter.model.stmts;

import ToyInterpreter.model.PrgState;

public class NOP implements Stmt{

    public PrgState exec(PrgState state){
        return null;
    }
    public String toString(){
        return "No operation\n";
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

}
