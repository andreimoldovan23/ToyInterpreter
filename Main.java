package ToyInterpreter;

import ToyInterpreter.controller.Controller;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.ExeStack;
import ToyInterpreter.model.adts.Out;
import ToyInterpreter.model.adts.SymTable;
import ToyInterpreter.model.exps.*;
import ToyInterpreter.model.stmts.*;
import ToyInterpreter.model.types.Bool;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.values.False;
import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.True;
import ToyInterpreter.repository.IRepo;
import ToyInterpreter.repository.Repo;
import ToyInterpreter.view.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static Exp genConstInt(int x){
        return new ConstExp(new IntValue(x));
    }

    private static Exp genVarExp(String s){
        return new VarExp(s);
    }

    private static List<Stmt> generateProgram() throws MyException{
        Exp constTrue = new ConstExp(new True());
        Exp constFalse = new ConstExp(new False());
        Exp logicOr = new LogicExp(constTrue, constFalse, "|");

        Exp a = genVarExp("a");
        Exp b = genVarExp("b");
        Exp bool1 = genVarExp("bool1");
        Exp bool2 = genVarExp("bool2");

        Stmt aDecl = new VarDecl(new Int(), a);
        Stmt bDecl = new VarDecl(new Int(), b);
        Stmt bool1Decl = new VarDecl(new Bool(), bool1);
        Stmt bool2Decl = new VarDecl(new Bool(), bool2);

        Stmt aAssign = new AssignStmt(a, genConstInt(7));
        Stmt bAssign = new AssignStmt(b, genConstInt(2));
        Stmt bool1Assign = new AssignStmt(bool1, constTrue);
        Stmt bool2Assign = new AssignStmt(bool2, constFalse);

        Exp logicAnd = new LogicExp(bool1, bool2, "&");
        Exp logicFinal = new LogicExp(logicOr, logicAnd, "|");

        Exp arithmeticAdd = new ArithmeticExp(genConstInt(7), genConstInt(8), "+");
        Exp arithmeticSub = new ArithmeticExp(genConstInt(3), genConstInt(5), "-");
        Exp arithmeticMultiply = new ArithmeticExp(genConstInt(1), genConstInt(10), "*");
        Exp arithmeticDiv = new ArithmeticExp(genConstInt(15), genConstInt(3), "/");
        Exp arithmeticAddDiv = new ArithmeticExp(arithmeticAdd, arithmeticDiv, "/");
        Exp arithmeticSubMul = new ArithmeticExp(arithmeticSub, arithmeticMultiply, "*");
        Exp arithmeticFinal = new ArithmeticExp(arithmeticAddDiv, arithmeticSubMul, "-");

        Exp arithmeticVarAdd = new ArithmeticExp(a, b, "+");
        Exp arithmeticVarSub = new ArithmeticExp(a, b, "-");
        Exp arithmeticVarFinal = new ArithmeticExp(arithmeticVarAdd, arithmeticVarSub, "/");

        Stmt printConst = new PrintStmt(arithmeticFinal);
        Stmt assignVarArithmetic = new AssignStmt(a, arithmeticFinal);
        Stmt printVar = new PrintStmt(arithmeticVarFinal);
        Stmt innerIf = new IfStmt(logicAnd, new PrintStmt(b), new PrintStmt(a));
        Stmt compIfBlock = PrgState.assemble(new ArrayList<>(List.of(printConst, assignVarArithmetic, innerIf)));
        Stmt ifStmt = new IfStmt(logicFinal, compIfBlock, printVar);

        return new ArrayList<>(List.of(aDecl, bDecl, bool1Decl, bool2Decl, aAssign, bAssign,
                bool1Assign, bool2Assign, ifStmt));
    }

    public static void main(String[] args) {
        try {
            List<Stmt> stmts = generateProgram();
            PrgState p1 = new PrgState(new ExeStack<>(), new SymTable<>(), new Out<>(), stmts);

            IRepo<PrgState> repo = new Repo<>();

            repo.add(p1);

            Controller c = new Controller(repo);

            TextMenu menu = new TextMenu();
            Command oneStep = new OneStepCommand("oneStep", "One step execution of current program", c);
            Command allStep = new AllStepsCommand("allStep", "Complete execution of current program", c);
            Command setDisplay = new SetDisplayFlagCommand("setFlag", "Set display flag for program", c);
            Command displayCurrent = new DisplayCurrentCommand("displayCurrent", "Display current program", c);
            Command displayAll = new DisplayAllCommand("displayAll", "Display all loaded programs", c);
            Command changeCurrent = new ChangeCurrentCommand("changeCurrent", "Change current program", c);
            Command removeProgram = new RemoveProgramCommand("remove", "Remove a program", c);
            Command quit = new QuitCommand("quit", "Exit current session");

            menu.addCommand(displayCurrent);
            menu.addCommand(displayAll);
            menu.addCommand(oneStep);
            menu.addCommand(allStep);
            menu.addCommand(setDisplay);
            menu.addCommand(changeCurrent);
            menu.addCommand(removeProgram);
            menu.addCommand(quit);

            menu.run();
        }
        catch (MyException e) {
            System.out.println(e);
        }
    }

}
