package model.stmts;

import exceptions.InvalidBarrier;
import exceptions.InvalidBarrierType;
import exceptions.MyException;
import exceptions.ThreadException;
import model.Barrier;
import model.PrgState;
import model.adts.BarrierTable;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.exps.Exp;
import model.types.Int;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;

import java.util.ArrayList;
import java.util.List;

public class NewBarrier implements Stmt {

    private final String varName;
    private final Exp expression;

    public NewBarrier(String v, Exp e) {
        varName = v;
        expression = e;
    }

    public String toString() {
        return "newBarrier(" + varName + ", " + expression.toString() + ") \n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> table = state.getTable();
        BarrierTable<Integer, Barrier<Integer, List<Integer>>> barrierTable = state.getBarrierTable();
        Value val;

        try {
            val = expression.eval(table, state.getHeap());
            if(!(val.getType() instanceof Int))
                throw new InvalidBarrier();
        }
        catch (MyException me) {
            throw new ThreadException(me, state.getId());
        }

        if(!table.isDefined(varName)){
            table.add(varName, Int.defaultValue());
        }
        else if(!(table.lookup(varName) instanceof Int))
            throw new ThreadException(new InvalidBarrierType(), state.getId());

        int idx = barrierTable.add(new Barrier<>((Integer)val.getValue(), new ArrayList<>()));
        table.add(varName, new IntValue(idx));
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!(expression.typeCheck(typeEnv) instanceof Int))
            throw new InvalidBarrier();
        if(typeEnv.isDefined(varName) && !(typeEnv.lookup(varName) instanceof Int))
            throw new InvalidBarrierType();
        if(!typeEnv.isDefined(varName))
            typeEnv.add(varName, new Int());
        return typeEnv;
    }

}
