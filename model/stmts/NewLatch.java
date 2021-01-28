package model.stmts;

import exceptions.InvalidLatchType;
import exceptions.MyException;
import exceptions.ThreadException;
import model.PrgState;
import model.adts.CountDownTable;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.exps.Exp;
import model.types.Int;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;

public class NewLatch implements Stmt {

    private final String varName;
    private final Exp expression;

    public NewLatch(String v, Exp e) {
        varName = v;
        expression = e;
    }

    public String toString() {
        return "newLatch(" + varName + ", " + expression.toString() + ") \n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> symTable = state.getTable();
        CountDownTable<Integer, Integer> countDownTable = state.getCountDownTable();
        Value val;

        try {
            val = expression.eval(symTable, state.getHeap());
            if(!(val.getType() instanceof Int))
                throw new InvalidLatchType();
        }
        catch (MyException me) {
            throw new ThreadException(me, state.getId());
        }

        if(!symTable.isDefined(varName)) {
            symTable.add(varName, Int.defaultValue());
        }

        int id = countDownTable.add((Integer) val.getValue());
        symTable.update(varName, new IntValue(id));
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if (!(expression.typeCheck(typeEnv) instanceof Int))
            throw new InvalidLatchType();
        if(typeEnv.isDefined(varName) && !(typeEnv.lookup(varName) instanceof Int))
            throw new InvalidLatchType();
        if(!typeEnv.isDefined(varName))
            typeEnv.add(varName, new Int());
        return typeEnv;
    }

}
