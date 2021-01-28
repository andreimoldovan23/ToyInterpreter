package model.stmts;

import exceptions.*;
import model.PrgState;
import model.adts.CountDownTable;
import model.adts.IOut;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.types.Int;
import model.types.Type;
import model.values.Value;


public class CountDown implements Stmt {

    private final String varName;

    public CountDown(String s) {
        varName = s;
    }

    public String toString() {
        return "countDown(" + varName + ") \n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    @SuppressWarnings("DuplicatedCode")
    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> symTable = state.getTable();
        CountDownTable<Integer, Integer> countDownTable = state.getCountDownTable();
        IOut<String> out = state.getOut();

        if(!symTable.isDefined(varName))
            throw new ThreadException(new IsNotDefined(), state.getId());
        Value val = symTable.lookup(varName);
        if(!(val.getType() instanceof Int))
            throw new ThreadException(new InvalidLatchType(), state.getId());

        Integer countVal = countDownTable.lookup((Integer) val.getValue());
        if(countVal == null)
            throw new ThreadException(new NonExistingLatch(), state.getId());

        if (countVal > 0) {
            countDownTable.update((Integer) val.getValue(), countVal - 1);
            out.add(String.valueOf(state.getId()));
        }

        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!(typeEnv.lookup(varName) instanceof Int))
            throw new InvalidLatchType();
        return typeEnv;
    }
}
