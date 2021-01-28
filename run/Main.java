package run;

import exceptions.MyException;
import javafx.util.Pair;
import model.Barrier;
import model.Semaphore;
import model.adts.*;
import model.exps.*;
import model.stmts.*;
import model.types.*;
import model.values.IntValue;
import model.values.StringValue;
import model.values.Value;
import view.GUI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings({"FieldMayBeFinal", "DuplicatedCode"})
public class Main {

    private static IProcTable<String, Pair<List<String>, Stmt>> procTable = new ProcTable<>();
    private static IOut<String> out = new Out<>();
    private static IFileTable<StringValue, MyBufferedReader> fileTable = new FileTable<>();
    private static IHeap<Integer, Value> heap = new Heap<>();
    private static ITypeEnv<String, Type> typeEnv = new TypeEnv<>();
    private static SemaphoreTable<Integer, Semaphore<Integer, List<Integer>>> semaphoreTable = new CountSemaphoreTable<>();
    private static CountDownTable<Integer, Integer> countDownTable = new CountDownLatch<>();
    private static BarrierTable<Integer, Barrier<Integer, List<Integer>>> barrierTable = new CyclicBarrierTable<>();
    private static LockTable<Integer, Integer> lockTable = new ReentrantLockTable<>();

    public static void typeChecker(Stmt s) throws MyException {
        typeEnv = s.typeCheck(typeEnv);
    }

    public static IOut<String> getOut() {
        return out;
    }

    public static IHeap<Integer, Value> getHeap() {
        return heap;
    }

    public static IFileTable<StringValue, MyBufferedReader> getFileTable() {
        return fileTable;
    }

    public static SemaphoreTable<Integer, Semaphore<Integer, List<Integer>>> getSemaphoreTable() {
        return semaphoreTable;
    }

    public static CountDownTable<Integer, Integer> getCountDownTable() {
        return countDownTable;
    }

    public static BarrierTable<Integer, Barrier<Integer, List<Integer>>> getBarrierTable() {
        return barrierTable;
    }

    public static LockTable<Integer, Integer> getLockTable() {
        return lockTable;
    }

    public static IProcTable<String, Pair<List<String>, Stmt>> getProcTable() {
        return procTable;
    }

    public static Stmt assemble(List<Stmt> statements){
        Collections.reverse(statements);
        return statements.stream().reduce(new NOP(), (a, b) -> new Comp(b, a));
    }

    public static void clean() throws IOException {
        procTable.clear();
        out.clear();
        heap.clear();

        List<MyBufferedReader> readers = fileTable.getValues();
        for(var r : readers)
            r.close();

        fileTable.clear();
        semaphoreTable.clear();
        countDownTable.clear();
        barrierTable.clear();
        lockTable.clear();
        typeEnv.clear();
    }


    private static Stmt generateFirst() {
        Stmt s1 = new VarDecl(new Int(), new VarExp("a"));
        Stmt s2 = new VarDecl(new Int(), new VarExp("b"));
        Stmt s3 = new VarDecl(new Int(), new VarExp("c"));

        Stmt s4 = new Assign(new VarExp("a"), new ConstExp(new IntValue(1)));
        Stmt s5 = new Assign(new VarExp("b"), new ConstExp(new IntValue(2)));
        Stmt s6 = new Assign(new VarExp("c"), new ConstExp(new IntValue(5)));

        Exp mainExp = new ArithmeticExp(new VarExp("a"), new ConstExp(new IntValue(10)), "*");
        Exp exp1 = new ArithmeticExp(new VarExp("b"), new VarExp("c"), "*");
        Exp exp2 = new ConstExp(new IntValue(10));
        Stmt s7 = new Comp(new Print(new VarExp("a")), new Print(new VarExp("b")));
        Stmt s8 = new Comp(new Print(new ConstExp(new IntValue(100))), new Print(new ConstExp(new IntValue(200))));
        Stmt s9 = new Print(new ConstExp(new IntValue(300)));
        Stmt s10 = new Switch(mainExp, exp1, s7, exp2, s8, s9);
        Stmt s11 = new Print(new ConstExp(new IntValue(300)));

        return assemble(new ArrayList<>(List.of(s1, s2, s3, s4, s5, s6, s10, s11)));
    }

    private static Stmt generateSecond() {
        Stmt s1 = new VarDecl(new Ref(new Int()), new VarExp("v1"));
        Stmt s2 = new VarDecl(new Int(), new VarExp("cnt"));
        Stmt s3 = new New("v1", new ConstExp(new IntValue(1)));
        Stmt s4 = new CreateSemaphore("cnt", new ReadHeapExp(new VarExp("v1")));

        Stmt s5 = new Acquire("cnt");
        Stmt s6 = new WriteHeap("v1", new ArithmeticExp(
                new ReadHeapExp(new VarExp("v1")), new ConstExp(new IntValue(10)), "*"));
        Stmt s7 = new Print(new ReadHeapExp(new VarExp("v1")));
        Stmt s8 = new Release("cnt");

        Stmt s9 = new Acquire("cnt");
        Stmt s10 = new WriteHeap("v1", new ArithmeticExp(
                new ReadHeapExp(new VarExp("v1")), new ConstExp(new IntValue(10)), "*"));
        Stmt s11 = new WriteHeap("v1", new ArithmeticExp(
                new ReadHeapExp(new VarExp("v1")), new ConstExp(new IntValue(2)), "*"));
        Stmt s12 = new Print(new ReadHeapExp(new VarExp("v1")));
        Stmt s13 = new Release("cnt");

        Stmt s14 = new Fork(assemble(new ArrayList<>(List.of(s5, s6, s7, s8))));
        Stmt s15 = new Fork(assemble(new ArrayList<>(List.of(s9, s10, s11, s12, s13))));

        Stmt s16 = new Acquire("cnt");
        Stmt s17 = new Print(new ArithmeticExp(
                new ReadHeapExp(new VarExp("v1")), new ConstExp(new IntValue(1)), "-"));
        Stmt s18 = new Release("cnt");

        return assemble(new ArrayList<>(List.of(s1, s2, s3, s4, s14, s15, s16, s17, s18)));
    }

    private static Stmt generateThird() {
        Stmt s1 = new VarDecl(new Ref(new Int()), new VarExp("v1"));
        Stmt s2 = new VarDecl(new Ref(new Int()), new VarExp("v2"));
        Stmt s3 = new VarDecl(new Ref(new Int()), new VarExp("v3"));

        Stmt s4 = new New("v1", new ConstExp(new IntValue(2)));
        Stmt s5 = new New("v2", new ConstExp(new IntValue(3)));
        Stmt s6 = new New("v3", new ConstExp(new IntValue(4)));

        Stmt s7 = new NewLatch("cnt", new ReadHeapExp(new VarExp("v2")));

        Stmt s8 = new WriteHeap("v1", new ArithmeticExp(new ReadHeapExp(new VarExp("v1")),
                new ConstExp(new IntValue(10)), "*"));
        Stmt s9 = new Print(new ReadHeapExp(new VarExp("v1")));
        Stmt s10 = new CountDown("cnt");

        Stmt s11 = new WriteHeap("v2", new ArithmeticExp(new ReadHeapExp(new VarExp("v2")),
                new ConstExp(new IntValue(10)), "*"));
        Stmt s12 = new Print(new ReadHeapExp(new VarExp("v2")));
        Stmt s13 = new CountDown("cnt");

        Stmt s14 = new WriteHeap("v3", new ArithmeticExp(new ReadHeapExp(new VarExp("v3")),
                new ConstExp(new IntValue(10)), "*"));
        Stmt s15 = new Print(new ReadHeapExp(new VarExp("v3")));
        Stmt s16 = new CountDown("cnt");

        Stmt s17 = new Fork(assemble(new ArrayList<>(List.of(s8, s9, s10))));
        Stmt s18 = new Fork(assemble(new ArrayList<>(List.of(s11, s12, s13))));
        Stmt s19 = new Fork(assemble(new ArrayList<>(List.of(s14, s15, s16))));

        Stmt s20 = new Await("cnt");
        Stmt s21 = new Print(new ConstExp(new IntValue(100)));
        Stmt s22 = new CountDown("cnt");
        Stmt s23 = new Print(new ConstExp(new IntValue(100)));

        return assemble(new ArrayList<>(List.of(s1, s2, s3, s4, s5, s6, s7, s17, s18, s19, s20, s21, s22, s23)));
    }

    private static Stmt generateFourth() {
        Stmt s1 = new VarDecl(new StringType(), new VarExp("fileName"));
        Stmt s2 = new Assign(new VarExp("fileName"), new ConstExp(new StringValue("program4.in")));
        Stmt s3 = new VarDecl(new Int(), new VarExp("number"));
        Stmt s4 = new VarDecl(new Ref(new Int()), new VarExp("simpleRef"));
        Stmt s5 = new VarDecl(new Ref(new Ref(new Int())), new VarExp("doubleRef"));

        Stmt s6 = new ReadFile(new VarExp("fileName"), "number");
        Stmt s7 = new NOP();
        Stmt s8 = new New("simpleRef", new VarExp("number"));

        Stmt s9 = new New("doubleRef", new VarExp("simpleRef"));

        Stmt s10 = new OpenFile(new ConstExp(new StringValue("new4.in")));
        Stmt s11 = new ReadFile(new ConstExp(new StringValue("new4.in")), "number");
        Stmt s12 = new Print(new VarExp("number"));

        Stmt s13 = new Fork(assemble(new ArrayList<>(List.of(s10, s11, s12))));
        Stmt s14 = new CloseFile(new VarExp("fileName"));

        Stmt s15 = new Fork(assemble(new ArrayList<>(List.of(s9, s13, s14))));
        Stmt s16 = new New("doubleRef", new VarExp("simpleRef"));
        Stmt s17 = new Print(new ReadHeapExp(new ReadHeapExp(new VarExp("doubleRef"))));

        Stmt s18 = new Fork(assemble(new ArrayList<>(List.of(s6, s7, s8, s15, s16, s17))));
        Stmt s19 = new OpenFile(new VarExp("fileName"));
        Stmt s20 = new ReadFile(new VarExp("fileName"), "number");
        Stmt s21 = new New("simpleRef", new VarExp("number"));
        Stmt s22 = new NOP();
        Stmt s23 = new New("doubleRef", new VarExp("simpleRef"));
        Stmt s24 = new Print(new ReadHeapExp(new ReadHeapExp(new VarExp("doubleRef"))));
        return assemble(new ArrayList<>(List.of(s1, s2, s3, s4, s5, s18, s19, s20, s21, s22, s23, s24)));
    }

    private static Stmt generateFifth() {
        Stmt s1 = new VarDecl(new Int(), new VarExp("v"));
        Stmt s2 = new Assign(new VarExp("v"), new ConstExp(new IntValue(0)));
        Stmt s3 = new Print(new VarExp("v"));
        Stmt s4 = new Assign(new VarExp("v"),
                new ArithmeticExp(new VarExp("v"), new ConstExp(new IntValue(1)), "-"));
        Stmt s5 = new Fork(assemble(new ArrayList<>(List.of(s3, s4))));
        Stmt s6 = new Assign(new VarExp("v"),
                new ArithmeticExp(new VarExp("v"), new ConstExp(new IntValue(1)), "+"));
        Stmt s7 = new RepeatUntil(assemble(new ArrayList<>(List.of(s5, s6))),
                new RelationalExp(new VarExp("v"), new ConstExp(new IntValue(3)), "=="));
        Stmt s8 = new Print(new ArithmeticExp(new VarExp("v"), new ConstExp(new IntValue(10)), "*"));
        return assemble(new ArrayList<>(List.of(s1, s2, s7, s8)));
    }

    private static Stmt generateSixth() {
        Stmt print = new Print(new VarExp("v"));
        Stmt assign = new Assign(new VarExp("v"),
                new ArithmeticExp(new VarExp("v"), new ReadHeapExp(new VarExp("a")), "*"));
        Stmt forkStmt = new Fork(new Comp(print, assign));

        Stmt s1 = new VarDecl(new Ref(new Int()), new VarExp("a"));
        Stmt s2 = new New("a", new ConstExp(new IntValue(20)));
        Stmt s3 = new For(new VarExp("v"), new ConstExp(new IntValue(0)), new ConstExp(new IntValue(3)),
                new ArithmeticExp(new VarExp("v"), new ConstExp(new IntValue(1)), "+"),
                forkStmt);
        Stmt s4 = new Print(new ReadHeapExp(new VarExp("a")));
        return assemble(new ArrayList<>(List.of(s1, s2, s3, s4)));
    }

    private static Stmt generateSeventh() {
        Stmt s1 = new VarDecl(new Ref(new Int()), new VarExp("v1"));
        Stmt s2 = new VarDecl(new Ref(new Int()), new VarExp("v2"));
        Stmt s3 = new VarDecl(new Ref(new Int()), new VarExp("v3"));

        Stmt s4 = new New("v1", new ConstExp(new IntValue(2)));
        Stmt s5 = new New("v2", new ConstExp(new IntValue(3)));
        Stmt s6 = new New("v3", new ConstExp(new IntValue(4)));

        Stmt s7 = new NewBarrier("cnt", new ReadHeapExp(new VarExp("v2")));

        Stmt s8 = new AwaitBarrier("cnt");
        Stmt s9 = new WriteHeap("v1", new ArithmeticExp(new ReadHeapExp(new VarExp("v1")),
                new ConstExp(new IntValue(10)), "*"));
        Stmt s10 = new Print(new ReadHeapExp(new VarExp("v1")));

        Stmt s11 = new AwaitBarrier("cnt");
        Stmt s12 = new WriteHeap("v2", new ArithmeticExp(new ReadHeapExp(new VarExp("v2")),
                new ConstExp(new IntValue(10)), "*"));
        Stmt s13 = new Print(new ReadHeapExp(new VarExp("v2")));

        Stmt s14 = new Fork(assemble(new ArrayList<>(List.of(s8, s9, s10))));
        Stmt s15 = new Fork(assemble(new ArrayList<>(List.of(s11, s12, s13))));

        Stmt s17 = new AwaitBarrier("cnt");
        Stmt s18 = new Print(new ReadHeapExp(new VarExp("v3")));

        return assemble(new ArrayList<>(List.of(s1, s2, s3, s4, s5, s6, s7, s14, s15, s17, s18)));
    }

    private static Stmt generateEight() {
        Stmt s1 = new VarDecl(new Int(), new VarExp("v"));
        Stmt s2 = new Assign(new VarExp("v"), new ConstExp(new IntValue(10)));
        Stmt s3 = new Assign(new VarExp("v"),
                new ArithmeticExp(new VarExp("v"), new ConstExp(new IntValue(1)), "-"));
        Stmt s4 = new Assign(new VarExp("v"),
                new ArithmeticExp(new VarExp("v"), new ConstExp(new IntValue(1)), "-"));
        Stmt s5 = new Print(new VarExp("v"));
        Stmt s6 = new Fork(assemble(new ArrayList<>(List.of(s3, s4, s5))));
        Stmt s7 = new Sleep(new ConstExp(new IntValue(10)));
        Stmt s8 = new Print(new ArithmeticExp(new VarExp("v"), new ConstExp(new IntValue(10)), "*"));
        return assemble(new ArrayList<>(List.of(s1, s2, s6, s7, s8)));
    }

    private static Stmt generateNinth() {
        Stmt s1 = new VarDecl(new Ref(new Int()), new VarExp("a"));
        Stmt s2 = new VarDecl(new Ref(new Int()), new VarExp("b"));
        Stmt s3 = new VarDecl(new Int(), new VarExp("v"));

        Stmt s4 = new New("a", new ConstExp(new IntValue(0)));
        Stmt s5 = new New("b", new ConstExp(new IntValue(0)));
        Stmt s6 = new WriteHeap("a", new ConstExp(new IntValue(1)));
        Stmt s7 = new WriteHeap("b", new ConstExp(new IntValue(2)));
        Stmt s8 = new ConditionalAssignment("v", new RelationalExp(new ReadHeapExp(new VarExp("a")),
                new ReadHeapExp(new VarExp("b")), "<"), new ConstExp(new IntValue(100)),
                new ConstExp(new IntValue(200)));
        Stmt s9 = new Print(new VarExp("v"));
        Stmt s10 = new ConditionalAssignment("v", new RelationalExp(
                new ArithmeticExp(new ReadHeapExp(new VarExp("b")), new ConstExp(new IntValue(2)), "-"),
                new ReadHeapExp(new VarExp("a")), ">"), new ConstExp(new IntValue(100)),
                new ConstExp(new IntValue(200)));
        Stmt s11 = new Print(new VarExp("v"));
        return assemble(new ArrayList<>(List.of(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11)));
    }

    private static Stmt generateTenth() {
        Stmt s1 = new VarDecl(new Ref(new Int()), new VarExp("v1"));
        Stmt s2 = new VarDecl(new Ref(new Int()), new VarExp("v2"));
        Stmt s3 = new VarDecl(new Int(), new VarExp("x"));
        Stmt s4 = new VarDecl(new Int(), new VarExp("q"));

        Stmt s5 = new New("v1", new ConstExp(new IntValue(20)));
        Stmt s6 = new New("v2", new ConstExp(new IntValue(30)));
        Stmt s7 = new NewLock("x");

        Stmt s8 = new Lock("x");
        Stmt s9 = new WriteHeap("v1", new ArithmeticExp(new ReadHeapExp(new VarExp("v1")),
                new ConstExp(new IntValue(1)), "-"));
        Stmt s10 = new Unlock("x");

        Stmt s11 = new Lock("x");
        Stmt s12 = new WriteHeap("v1", new ArithmeticExp(new ReadHeapExp(new VarExp("v1")),
                new ConstExp(new IntValue(10)), "*"));
        Stmt s13 = new Unlock("x");

        Stmt s14 = new Fork(assemble(new ArrayList<>(List.of(s8, s9, s10))));
        Stmt s15 = new Fork(assemble(new ArrayList<>(List.of(s14, s11, s12, s13))));

        Stmt s16 = new NewLock("q");

        Stmt s17 = new Lock("q");
        Stmt s18 = new WriteHeap("v2", new ArithmeticExp(new ReadHeapExp(new VarExp("v2")),
                new ConstExp(new IntValue(5)), "+"));
        Stmt s19 = new Unlock("q");

        Stmt s20 = new Lock("q");
        Stmt s21 = new WriteHeap("v2", new ArithmeticExp(new ReadHeapExp(new VarExp("v2")),
                new ConstExp(new IntValue(10)), "*"));
        Stmt s22 = new Unlock("q");

        Stmt s23 = new Fork(assemble(new ArrayList<>(List.of(s17, s18, s19))));
        Stmt s24 = new Fork(assemble(new ArrayList<>(List.of(s23, s20, s21, s22))));

        Stmt s25 = new NOP();
        Stmt s26 = new NOP();
        Stmt s27 = new NOP();
        Stmt s28 = new NOP();

        Stmt s29 = new Lock("x");
        Stmt s30 = new Print(new ReadHeapExp(new VarExp("v1")));
        Stmt s31 = new Unlock("x");

        Stmt s32 = new Lock("q");
        Stmt s33 = new Print(new ReadHeapExp(new VarExp("v2")));
        Stmt s34 = new Unlock("q");

        return assemble(new ArrayList<>(List.of(s1, s2, s3, s4, s5, s6, s7, s15, s16, s24, s25, s26, s27, s28,
                s29, s30, s31, s32, s33, s34)));
    }

    private static Stmt generateEleventh() {
        Stmt s1 = new VarDecl(new Int(), new VarExp("v"));
        Stmt s2 = new Assign(new VarExp("v"), new ArithmeticExp(new VarExp("a"), new VarExp("b"), "+"));
        Stmt s3 = new Print(new VarExp("v"));

        Stmt s4 = new VarDecl(new Int(), new VarExp("v"));
        Stmt s5 = new Assign(new VarExp("v"), new ArithmeticExp(new VarExp("a"), new VarExp("b"), "*"));
        Stmt s6 = new Print(new VarExp("v"));

        Stmt s7 = new Procedure("sum", new ArrayList<>(List.of("a", "b")), assemble(new ArrayList<>(List.of(s1, s2, s3))));
        Stmt s8 = new Procedure("product", new ArrayList<>(List.of("a", "b")), assemble(new ArrayList<>(List.of(s4, s5, s6))));

        Stmt s9 = new VarDecl(new Int(), new VarExp("v"));
        Stmt s10 = new VarDecl(new Int(), new VarExp("w"));
        Stmt s11 = new Assign(new VarExp("v"), new ConstExp(new IntValue(2)));
        Stmt s12 = new Assign(new VarExp("w"), new ConstExp(new IntValue(5)));
        Stmt s13 = new CallProc("sum", new ArrayList<>(List.of(
                new ArithmeticExp(new VarExp("v"), new ConstExp(new IntValue(10)), "*"), new VarExp("w"))));
        Stmt s14 = new Print(new VarExp("v"));

        Stmt s15 = new CallProc("product", new ArrayList<>(List.of(new VarExp("v"), new VarExp("w"))));
        Stmt s16 = new Fork(s15);

        Stmt s17 = new CallProc("sum", new ArrayList<>(List.of(new VarExp("v"), new VarExp("w"))));
        Stmt s18 = new Fork(s17);

        return assemble(new ArrayList<>(List.of(s7, s8, s9, s10, s11, s12, s13, s14, s16, s18)));
    }

    private static Stmt generateProgram(int x) {
        return switch (x) {
            case 1 -> generateFirst();
            case 2 -> generateSecond();
            case 3 -> generateThird();
            case 4 -> generateFourth();
            case 5 -> generateFifth();
            case 6 -> generateSixth();
            case 7 -> generateSeventh();
            case 8 -> generateEight();
            case 9 -> generateNinth();
            case 10 -> generateTenth();
            case 11 -> generateEleventh();
            default -> null;
        };
    }

    public static List<Stmt> getPrograms() {
        return IntStream.range(1, 12)
                .mapToObj(Main::generateProgram)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        GUI view = new GUI();
        view.run();
    }

}
