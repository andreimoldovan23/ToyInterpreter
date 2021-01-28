package model.stmts;

import exceptions.MyException;
import model.PrgState;
import model.adts.ITypeEnv;
import model.exps.Exp;
import model.exps.NotExp;
import model.types.Type;

public class RepeatUntil implements Stmt {

    private final Stmt stmt;
    private final Exp exp;

    public RepeatUntil(Stmt s, Exp e) {
        stmt = s;
        exp = e;
    }

    public String toString() {
        return "repeat\n" + stmt.toStringPrefix("\t") + "until " + exp.toString() + "\n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + "repeat\n" + stmt.toStringPrefix(prefix + "\t") + prefix + "until " + exp.toString() + "\n";
    }

    public PrgState exec(PrgState state) {
        Stmt newWhile = new While(new NotExp(exp), stmt);
        state.getStack().push(newWhile);
        state.getStack().push(stmt);
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        Stmt newWhile = new While(new NotExp(exp), stmt);
        return newWhile.typeCheck(typeEnv.copy());
    }

}
