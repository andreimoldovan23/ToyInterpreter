package model.stmts;

import exceptions.*;
import model.PrgState;
import model.Semaphore;
import model.adts.ISymTable;
import model.adts.ITypeEnv;
import model.adts.SemaphoreTable;
import model.exps.Exp;
import model.types.Int;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;
import java.util.ArrayList;
import java.util.List;

public class CreateSemaphore implements Stmt{

    private final String varName;
    private final Exp expression;

    public CreateSemaphore(String v, Exp e) {
        varName = v;
        expression = e;
    }

    public String toString() {
        return "createSemaphore (" + varName + ", " + expression.toString() + ") \n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> symTable = state.getTable();
        SemaphoreTable<Integer, Semaphore<Integer, List<Integer>>> semTable = state.getSemaphoreTable();
        Value val;
        Value expVal;

        if(!symTable.isDefined(varName))
            throw new ThreadException(new IsNotDefined(), state.getId());
        val = symTable.lookup(varName);
        if(!(val.getType() instanceof Int))
            throw new ThreadException(new InvalidSemaphoreType(), state.getId());

        try {
            expVal = expression.eval(symTable, state.getHeap());
            if (!(expVal.getType() instanceof Int))
                throw new InvalidSemaphoreSize();
        }
        catch (MyException me) {
            throw new ThreadException(me, state.getId());
        }

        int id = semTable.add(new Semaphore<>((Integer)expVal.getValue(), new ArrayList<>()));
        symTable.update(varName, new IntValue(id));

        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        Type t1 = typeEnv.lookup(varName);
        if(!(t1 instanceof Int))
            throw new InvalidSemaphoreType();

        Type t2 = expression.typeCheck(typeEnv);
        if(!(t2 instanceof Int))
            throw new InvalidSemaphoreSize();

        return typeEnv;
    }

}
