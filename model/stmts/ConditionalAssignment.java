package model.stmts;

import exceptions.*;
import model.PrgState;
import model.adts.ITypeEnv;
import model.exps.Exp;
import model.exps.VarExp;
import model.types.Bool;
import model.types.Type;

public class ConditionalAssignment implements Stmt{

    private final String varName;
    private final Exp exp1;
    private final Exp exp2;
    private final Exp exp3;

    public ConditionalAssignment(String s, Exp e1, Exp e2, Exp e3) {
        varName = s;
        exp1 = e1;
        exp2 = e2;
        exp3 = e3;
    }

    public String toString() {
        return varName + " = " + exp1.toString() + " ? " + exp2.toString() + " : " + exp3.toString() + " \n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    public PrgState exec(PrgState state) {
        Stmt newStmt = new If(exp1, new Assign(new VarExp(varName), exp2),
                new Assign(new VarExp(varName), exp3));
        state.getStack().push(newStmt);
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!(exp1.typeCheck(typeEnv) instanceof Bool))
            throw new InvalidConditionalCheck();
        if(!(typeEnv.isDefined(varName)))
            throw new IsNotDefined();

        Type t1 = typeEnv.lookup(varName);
        Type t2 = exp2.typeCheck(typeEnv);
        Type t3 = exp3.typeCheck(typeEnv);

        if(!t1.equals(t2) || !t2.equals(t3))
            throw new InvalidConditionalAssignment();

        return typeEnv;
    }
}
