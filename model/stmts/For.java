package model.stmts;

import exceptions.InvalidForType;
import exceptions.MyException;
import model.PrgState;
import model.adts.IExeStack;
import model.adts.ITypeEnv;
import model.exps.Exp;
import model.exps.RelationalExp;
import model.types.Int;
import model.types.Type;

public class For implements Stmt{

    private final Exp varExp;
    private final Exp declaration;
    private final Exp limit;
    private final Exp modifier;
    private final Stmt stmt;

    public For(Exp e1, Exp e2, Exp e3, Exp e4, Stmt s) {
        varExp = e1;
        declaration = e2;
        limit = e3;
        modifier = e4;
        stmt = s;
    }

    public String toString() {
        return "for (" + varExp.toString() + " = " + declaration.toString() + "; " + varExp.toString() + " < " +
                limit.toString() + "; " + varExp.toString() + " = " + modifier.toString() + ")\n" +
                stmt.toStringPrefix("\t");
    }

    public String toStringPrefix(String prefix) {
        return prefix + "for (" + varExp.toString() + " = " + declaration.toString() + "; " + varExp.toString() + " < " +
                limit.toString() + "; " + varExp.toString() + " = " + modifier.toString() + ")\n" +
                stmt.toStringPrefix(prefix + "\t");
    }

    public PrgState exec(PrgState state) {
        IExeStack<Stmt> stack = state.getStack();
        Stmt s1 = new VarDecl(new Int(), varExp);
        Stmt s2 = new Assign(varExp, declaration);
        Stmt s3 = new While(new RelationalExp(varExp, limit, "<"),
                new Comp(stmt, new Assign(varExp, modifier)));
        stack.push(s3);
        stack.push(s2);
        stack.push(s1);
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(typeEnv.isDefined(varExp.toString()))
            throw new InvalidForType();
        typeEnv.add(varExp.toString(), new Int());

        if(!(declaration.typeCheck(typeEnv) instanceof Int))
            throw new InvalidForType();
        if(!(limit.typeCheck(typeEnv) instanceof Int))
            throw new InvalidForType();
        if(!(modifier.typeCheck(typeEnv) instanceof Int))
            throw new InvalidForType();
        return typeEnv;
    }

}
