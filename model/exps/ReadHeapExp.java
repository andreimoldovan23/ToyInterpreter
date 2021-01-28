package model.exps;

import exceptions.InvalidAddress;
import exceptions.InvalidReadHeapType;
import exceptions.MyException;
import model.adts.IHeap;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.types.Int;
import model.types.Ref;
import model.types.Type;
import model.values.Value;

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

    public Type typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        Type t = exp.typeCheck(typeEnv);
        if(t instanceof Ref)
            return ((Ref) t).getInner();
        throw new InvalidReadHeapType();
    }

}
