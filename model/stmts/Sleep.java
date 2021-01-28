package model.stmts;

import exceptions.InvalidSleepArgument;
import exceptions.MyException;
import exceptions.ThreadException;
import model.PrgState;
import model.adts.ITypeEnv;
import model.exps.ConstExp;
import model.exps.Exp;
import model.types.Int;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;

public class Sleep implements Stmt{

    private final Exp expression;

    public Sleep(Exp e) {
        expression = e;
    }

    public String toString() {
        return "sleep " + expression.toString() + " \n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    public PrgState exec(PrgState state) throws ThreadException {
        Value val;
        try{
            val = expression.eval(state.getTable(), state.getHeap());
            if(!(val.getType() instanceof Int))
                throw new InvalidSleepArgument();
        }
        catch (MyException me) {
            throw new ThreadException(me, state.getId());
        }

        int number = (Integer)val.getValue();
        if(number > 0)
            state.getStack().push(new Sleep(new ConstExp(new IntValue(number - 1))));
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!(expression.typeCheck(typeEnv) instanceof Int))
            throw new InvalidSleepArgument();
        return typeEnv;
    }

}
