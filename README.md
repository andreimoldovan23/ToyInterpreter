# Toy interpreter for a toy java-like language, implemented in Java with an MVC architecture

# Interpreter classes:
&nbsp;&nbsp;&nbsp; -types\
&nbsp;&nbsp;&nbsp; -values\
&nbsp;&nbsp;&nbsp; -expressions\
&nbsp;&nbsp;&nbsp; -statements\
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

# ADTs: IExeStack(T), ISymTable(T, U), IOut(T), IFileTable(T, U), IHeap(T, U), ITypeEnv(T, U)
-represent the execution stack, symbol table, output stream and file descriptor table of a program\
-ITypeEnv is the typing environment used for typechecking the program before running it\
-they are generic interfaces\
-support for basic operations like adding/removing/updating elements, checking if the container is empty, clearing the container, printing the stored elements, getting the content or setting the content

# Expressions: ConstExp, VarExp, ArithmeticExp, LogicExp, RelationalExp, ReadHeapExp
-all of them implement the Exp interface\
-ConstExp(Value) - a constant(be it int, boolean or string)\
-VarExp(String) - the name of a variable\
-ArithmeticExp(Exp left, Exp right, String operator) - an arithmetic expression; can be chained, can use constants or variables; support for +, -, /, *\
-LogicExp(Exp left, Exp right, String operator) - a logic expression; can be chained, can use constants or variables; support for &, |\
-RelationalExp(Exp left, Exp right, String operator) - a relational expression; can be chained, can use constants or variables; support for <, >, ==, <=, >=\
-ReadHeapExp(Exp varName) - reads the value found at the memory address of varName\
-supported operations: checking if two expressions are of the same type, printing an expression, evaluating an expression based on the symbol table of the program, typechecking an expression based on the typing environment

# Statements: VarDecl, PrintStmt, AssignStmt, IfStmt, NOP, CompStmt, OpenFileStmt, CloseFileStmt, ReadFileStmt, NewStmt, WriteHeapStmt, WhileStmt, ForkStmt
-all of them implement the Stmt interface\
-VarDecl(Type, VarExp) - declaration of variable; updates the symbol table\
-PrintStmt(Exp) - writes to the output stream the Exp\
-AssignStmt(VarExp, Exp) - updates the values of the variable in the symbol table\
-IfStmt(Exp, Stmt, Stmt) - a basic if-else statement, updates the execution stack\
-NOP() - no operation\
-CompStmt(Stmt left, Stmt right) - compound statement, updates the execution stack with right and left Statements\
-OpenFileStmt(Exp) - opens a file represented by Exp, updates the file descriptor table\
-CloseFileStmt(Exp) - closes a file represented by Exp, updates the file descriptor table\
-ReadFileStmt(Exp file, Exp var) - reads from the file represented by file and updates the value of var in the symbol table\
-NewStmt(String varName, Exp exp) - allocates memory on the heap for the variable varName which is a reference to the value corresponding to exp, updates the heap\
-WriteHeapStmt(String varName, Exp exp) - modifies the value found at the memory address of varName, updates the heap\
-WhileStmt(Exp condition, Stmt statement) - a basic while statement, updates the execution stack\
-ForkStmt(Stmt stmt) - creates a new thread which will run the program represented by stmt; this thread will have it's own execution stack and symbol table; the file descriptor table, heap and output stream are shared by all threads \
-supported operations: printing the statement, executing the statement, updating the ADTs associated with the program, typechecking the statement, might change the typing environment

# Program state: PrgState
-represents a program with an execution stack, a symbol table, an output stream, a file descriptor table and a heap\
-supported operations: getters for the ADTs, printing the program, closing possible existing input streams and cleaning the memory associated with the adts and their contents

# Repository: IRepo(T)
-generic interface\
-represent a list of the multiple threads of a program\
-supported operations: getters for the initial state of the main thread of the program, writing the program states of all threads to a log file, changing the log file

# Controller: Controller
-connects the view with the data\
-provides methods for running a program: allStep - executes the whole program and the threads generated by this\
-if the display flag is set prints the current program state at each step\
-logs the current program state into a file at each step\
-performs garbage collecting after each step

# Commands: represent the commands the user can input, extend the abstract class Command

# Text Menu: a menu of commands from which the user can choose which one to execute

# Main: initializes the repository, controller, text menu and command, performs static type checking before running the program
