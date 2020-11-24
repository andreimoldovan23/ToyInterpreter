package ToyInterpreter.model.exps;

import ToyInterpreter.exceptions.InvalidAddress;
import ToyInterpreter.exceptions.InvalidReadHeapType;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.types.Ref;
import ToyInterpreter.model.values.Value;

public class ReadHeapExp implements Exp{

    private final Exp exp;

    public ReadHeapExp(Exp e){
        exp = e;
    }

    public boolean equals(Object other){
        return other instanceof ReadHeapExp;
    }

    public String toString(){
        return "readHeap (" + exp.toString() + ")";
    }

    public Value eval(ISymTable<String, Value> table, IHeap<Integer, Value> heap) throws MyException {
        Value val = exp.eval(table, heap);
        if(!val.equals(Ref.defaultValue(new Int())))
            throw new InvalidReadHeapType();

        int addr = (int) val.getValue();
        if(!heap.isDefined(addr))
            throw new InvalidAddress();

        return heap.lookup(addr);
    }

}
