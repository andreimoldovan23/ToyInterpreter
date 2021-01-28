package model.stmts;

import exceptions.MyException;
import model.PrgState;
import model.adts.ITypeEnv;
import model.exps.Exp;
import model.exps.RelationalExp;
import model.types.Type;

public class Switch implements Stmt {

    private final Exp mainExp;
    private final Exp exp1;
    private final Stmt stmt1;
    private final Exp exp2;
    private final Stmt stmt2;
    private final Stmt stmt3;

    public Switch(Exp e1, Exp e2, Stmt s1, Exp e3, Stmt s2, Stmt s3) {
        mainExp = e1;
        exp1 = e2;
        stmt1 = s1;
        exp2 = e3;
        stmt2 = s2;
        stmt3 = s3;
    }

    public String toString() {
        return
                "switch " + mainExp.toString() + "\n\tcase " + exp1.toString() + "\n" + stmt1.toStringPrefix("\t\t") +
                        "\tcase " + exp2.toString() + "\n" + stmt2.toStringPrefix("\t\t") +
                        "\tdefault\n" + stmt3.toStringPrefix("\t\t") + "\n";
    }

    public String toStringPrefix(String prefix) {
        return
                prefix + "switch " + mainExp.toString() + "\n\tcase " + exp1.toString() + "\n" +
                        stmt1.toStringPrefix(prefix + "\t\t") + prefix +
                        "\tcase " + exp2.toString() + "\n" + stmt2.toStringPrefix(prefix + "\t\t") + prefix +
                        "\tdefault\n" + stmt3.toStringPrefix(prefix + "\t\t") + "\n";
    }

    public PrgState exec(PrgState state) {
        Exp firstIf = new RelationalExp(mainExp, exp1, "==");
        Exp secondIf = new RelationalExp(exp1, exp2, "==");
        Stmt newStmt = new If(firstIf, stmt1, new If(secondIf, stmt2, stmt3));
        state.getStack().push(newStmt);
        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        Exp firstIf = new RelationalExp(mainExp, exp1, "==");
        Exp secondIf = new RelationalExp(exp1, exp2, "==");
        Stmt newStmt = new If(firstIf, stmt1, new If(secondIf, stmt2, stmt3));
        return newStmt.typeCheck(typeEnv.copy());
    }

}
