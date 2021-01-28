# Toy interpreter for a toy java-like language, implemented in Java with an MVC architecture

# Interpreter classes:
&nbsp;&nbsp;&nbsp; -types\
&nbsp;&nbsp;&nbsp; -values\
&nbsp;&nbsp;&nbsp; -expressions\
&nbsp;&nbsp;&nbsp; -statements\
&nbsp;&nbsp;&nbsp; -concurrency control mechanisms\
&nbsp;&nbsp;&nbsp; -procedure support\
&nbsp;&nbsp;&nbsp; -program state\
&nbsp;&nbsp;&nbsp; -adts\
&nbsp;&nbsp;&nbsp; -repository\
&nbsp;&nbsp;&nbsp; -controller\
&nbsp;&nbsp;&nbsp; -text menu\
&nbsp;&nbsp;&nbsp; -commands\
&nbsp;&nbsp;&nbsp; -main class
  
# Types: Int, Bool, StringType, Ref
-represent the primitives the interpreter will use; all of them implement the Type interface\
-supported operations: checking if two Types are the same, printing a Type, returning the default Value for a Type\
-extra operations for Ref: getting the inner type which the Ref references, the equality between two Refs consists of checking the equality also for the inner types

# Values: IntValue, BoolValue, StringValue, RefValue plus True and False as constants
-represent the possible values the interpreter will work with; all of them implement the Value interface\
-supported operations: checking if two Values are of the same Type, printing a Value, returning the Type of a Value, returning the actual value stored in a Value object\
-extra operation for StringValue: hashCode, returns the ascii sum of the contained string, used to check uniqueness of filenames in IFileTable\
-extra operation for RefValue: getValue returns the address of the reference

# ADTs: IExeStack(T), ISymTable(T, U), IOut(T), IFileTable(T, U), IHeap(T, U), ITypeEnv(T, U), BarrierTable(T, U), CountDownTable(T, U), LockTable(T, U), SemaphoreTable(T, U), IProcTable(T, U)
-represent the execution stack, symbol table, output stream and file descriptor table of a program\
-besides those there are also tables for barriers, semaphores, count down latches and locks for managing these concurrency control mechanisms\
-the proc table is the table storing the declared procedures(functions)\
-ITypeEnv is the typing environment used for typechecking the program before running it\
-they are generic interfaces\
-support for basic operations like adding/removing/updating elements, checking if the container is empty, clearing the container, printing the stored elements, getting the content or setting the content

# Expressions: ConstExp, VarExp, ArithmeticExp, LogicExp, RelationalExp, ReadHeapExp, NotExp
-all of them implement the Exp interface\
-ConstExp(Value) - a constant(be it int, boolean or string)\
-VarExp(String) - the name of a variable\
-ArithmeticExp(Exp left, Exp right, String operator) - an arithmetic expression; can be chained, can use constants or variables; support for +, -, /, *\
-LogicExp(Exp left, Exp right, String operator) - a logic expression; can be chained, can use constants or variables; support for &, |\
-RelationalExp(Exp left, Exp right, String operator) - a relational expression; can be chained, can use constants or variables; support for <, >, ==, <=, >=\
-ReadHeapExp(Exp varName) - reads the value found at the memory address of varName\
-NotExp(Exp e) - negates the return value(boolean) of another expression\
-supported operations: checking if two expressions are of the same type, printing an expression, evaluating an expression based on the symbol table of the program, typechecking an expression based on the typing environment

# Statements:
-all of them implement the Stmt interface\
-Acquire(String semaphore) - used to acquire a semaphore spot, if free\
-Assign(Exp e1, Exp e2) - normal assignment\
-Await(String countDownLatch) - used to wait for the CountDownLatch to reach 0\
-AwaitBarrier(String barrier) - used to wait until all threads reach the barrier\
-CallProc(String procName, List<Exp> expressions) - calls a procedure with the given expressions as arguments\
-CloseFile(Exp file) - closes the stream associated with the file\
-Comp(Stmt s1, Stmt s2) - composes 2 statements into 1\
-ConditionalAssignment(String s, Exp e1, Exp e2, Exp e3) - also known as ternary operator\
-CountDown(String countDownLatch) - decrements the latch\
-CreateSemaphore(String s, Exp e) - creates a semaphore with given size\
-For(Exp e1, Exp e2, Exp e3, Exp e4, Stmt s) - basic for loop\
-Fork(Stmt s) - creates a new thread of execution\
-If(Exp e, Stmt s1, Stmt s2) - basic if-else\
-Lock(String s) - locks the lock/mutex\
-New(String v, Exp e) - allocates memory on the heap for a variable\
-NewBarrier(String v, Exp e) - creates barrier with given size\
-NewLatch(String v, Exp e) - creates countDownLatch with given size\
-NewLock(String s) - creates new lock/mutex\
-NOP - no operation\
-OpenFile(Exp file) - opens a stream to the file\
-Print(Exp e) - prints the value of the expression\
-Procedure(String name, List<String> args, Stmt body) - creates a procedure with name, arguments and body\
-ReadFile(Exp file, String var) - reads from file into var\
-Release(String) - releases spot in semaphore\
-RepeatUntil(Stmt s, Exp e) - basic do-while, gets converted into while\
-Return - used to return to the original symbol table after a procedure call\
-Sleep(Exp e) - basic sleep\
-Switch(Exp e1, Exp e2, Stmt s1, Exp e3, Stmt s2, Stmt s3) - basic switch, gets converted into an if\
-Unlock(String) - unlocks the lock/mutes\
-VarDecl(Type t, Exp e) - variable declaration\
-While(Exp e, Stmt s) - basic while\
-WriteHeap(String v, Exp e) - modifies the value of a variable stored on the heap\
-supported operations: printing the statement, executing the statement, updating the ADTs associated with the program, typechecking the statement, might change the typing environment

# Program state: PrgState
-represents a program with an execution stack, a stack of symbol tables(for main program and procedures), an output stream, a file descriptor table, a heap, semaphore table, barrier table, lock table, count down latch table and procedures table\
-supported operations: getters for the ADTs, printing the program, closing possible existing input streams and cleaning the memory associated with the adts and their contents

# Repository: IRepo(T)
-generic interface\
-represent a list of the multiple threads of a program\
-supported operations: getters for the initial state of the main thread of the program, writing the program states of all threads to a log file, changing the log file

# Controller: Controller
-connects the view with the data\
-logs the current program state into a file at each step\
-performs garbage collecting after each step

# GUI: the view

# Main: starts the app, has some hardcoded examples
