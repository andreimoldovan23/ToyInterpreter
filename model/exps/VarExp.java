package ToyInterpreter.model.exps;

import ToyInterpreter.exceptions.IsNotDefinedException;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.values.Value;

public class VarExp implements Exp{

    private final String varName;

    public VarExp(String s){
        varName = s;
    }

    public Value eval(ISymTable<String, Value> table) throws IsNotDefinedException {
        if(table.isDefined(varName))
            return table.lookup(varName);
        throw new IsNotDefinedException();
    }

    public String toString(){
        return varName;
    }

    public boolean equals(Object other){
        return other instanceof VarExp;
    }

}
