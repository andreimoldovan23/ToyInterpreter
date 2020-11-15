# Toy interpreter for a toy java like language, implemented in Java with an MVC architecture

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
  
# Types: Int, Bool, String
-represent the primitives the interpreter will use; all of them implement a Type interface\
-supported operations: checking if two Types are the same, printing a Type, returning the default Value for a Type

# Values: IntValue, BoolValue, StringValue, plus True and False as constants
-represent the possible values the interpreter will work with; all of them implement a Value interface\
-supported operations: checking if two Values are of the same Type, printing a Value, returning the Type of a Value, returning the actual value stored in a Value object

# ADTs: IExeStack(T), ISymTable(T, U), IOut(T), IFileTable(T, U)
-represent the execution stack, symbol table, outstream and file descriptor table of a program\
-they are generic interfaces\
-support for basic operations like adding/removing/updating elements, checking if the container is empty, clearing the container or printing the stored elements

# Expressions: ConstExp, VarExp, ArithmeticExp, LogicExp
-all of them implement the Exp interface\
-ConstExp(Value) - a constant(be it int, boolean or string)\
-VarExp(String) - the name of a variable\
-ArithmeticExp(Exp left, Exp right, String operator) - an arithmetic expression; can be chained, can use constants or variables; support for +, -, /, *\
-LogicExp(Exp left, Exp right, String operator) - a logic expression; can be chained, can use constants or variables; support for &, |\
-supported operations: checking if two expressions are of the same type, printing an expression, evaluating an expression based on the symbol table of the program

# Statements: VarDecl, PrintStmt, AssignStmt, IfStmt, NOP, CompStmt, OpenFileStmt, CloseFileStmt, ReadFileStmt
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
-supported operations: printing the statement, executing the statement and updating the ADTs associated with the program

# Program state: PrgState
-represents a program with an execution stack, a symbol table, an output stream and a file descriptor table\
-supported operations: getters for the ADTs, printing the program, reseting the program to the initial state

# Repository: IRepo(T)
-generic interface\
-represent a list of programs\
-supported operations: getters for the current program/all programs/number of programs, adding/removing programs, writing the current program state to a file

# Controller: Controller
-connects the view with the data\
-provides methods for running a program: oneStep - executes the top of the execution stack, allStep - executes the whole program\
-if the display flag is set prints the current program state at each step, otherwise it print the output at the end of the execution\
-logs the current program state into a file at each step

# Commands: represent the commands the user can input, extend the abstract class Command

# Text Menu: a menu of commands from which the user can choose which one to execute

# Main: initializes the reposiory, controller, text menu and command
