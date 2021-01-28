package model.stmts;

import exceptions.*;
import model.PrgState;
import model.adts.CountDownTable;
import model.adts.IExeStack;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.types.Int;
import model.types.Type;
import model.values.Value;

public class Await implements Stmt {

    private final String varName;

    public Await(String s) {
        varName = s;
    }

    public String toString() {
        return "await(" + varName + ") \n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    @SuppressWarnings("DuplicatedCode")
    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> symTable = state.getTable();
        CountDownTable<Integer, Integer> countDownTable = state.getCountDownTable();
        IExeStack<Stmt> stack = state.getStack();

        if(!symTable.isDefined(varName))
            throw new ThreadException(new IsNotDefined(), state.getId());
        Value val = symTable.lookup(varName);
        if(!(val.getType() instanceof Int))
            throw new ThreadException(new InvalidLatchType(), state.getId());

        Integer countVal = countDownTable.lookup((Integer) val.getValue());
        if(countVal == null)
            throw new ThreadException(new NonExistingLatch(), state.getId());

        if(countVal > 0)
            stack.push(this);

        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!(typeEnv.lookup(varName) instanceof Int))
            throw new InvalidLatchType();
        return typeEnv;
    }

}
