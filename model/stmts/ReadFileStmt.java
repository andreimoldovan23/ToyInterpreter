package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IFileTable;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.MyBufferedReader;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.types.Int;
import ToyInterpreter.model.types.StringType;
import ToyInterpreter.model.values.IntValue;
import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.model.values.Value;

import java.io.IOException;

public class ReadFileStmt implements Stmt {

    private final Exp file;
    private final String varName;

    public ReadFileStmt(Exp e, String s){
        file = e;
        varName = s;
    }

    public String toString(){
        return "readFile (" + file.toString() + ", " + varName + ")\n";
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

    public PrgState exec(PrgState state) throws MyException{
        ISymTable<String, Value> symTable = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();
        Value val = file.eval(symTable, heap);

        if(!val.getType().equals(new StringType()))
            throw new InvalidFilenameException();
        if(!symTable.isDefined(varName))
            throw new IsNotDefinedException();
        Value intVal = symTable.lookup(varName);
        if(!intVal.getType().equals(new Int()))
            throw new InvalidFileReadType();

        IFileTable<StringValue, MyBufferedReader> fileTable = state.getFileTable();
        if(!fileTable.isDefined((StringValue) val))
            throw new FileNotOpen();
        try{
            MyBufferedReader reader = fileTable.lookup((StringValue) val);
            String line = reader.readLine();
            if(line == null){
                symTable.update(varName, Int.defaultValue());
            }
            else{
                int number = Integer.parseInt(line);
                symTable.update(varName, new IntValue(number));
            }
        }
        catch (IOException e){
            throw new InexistingFile();
        }
        return null;
    }

}
