package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.FileAlreadyOpen;
import ToyInterpreter.exceptions.InexistingFile;
import ToyInterpreter.exceptions.InvalidFilenameException;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IFileTable;
import ToyInterpreter.model.adts.IHeap;
import ToyInterpreter.model.adts.ISymTable;
import ToyInterpreter.model.adts.MyBufferedReader;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.types.StringType;
import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.model.values.Value;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenFileStmt implements Stmt{

    private final Exp file;

    public OpenFileStmt(Exp e){
        file = e;
    }

    public String toString(){
        return "openFile (" + file.toString() + ")\n";
    }

    public String toStringPrefix(String prefix) {
        return prefix + toString();
    }

    public PrgState exec(PrgState state) throws MyException {
        ISymTable<String, Value> table = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();
        IFileTable<StringValue, MyBufferedReader> fileTable = state.getFileTable();
        Value val = file.eval(table, heap);

        if(!val.getType().equals(new StringType()))
            throw new InvalidFilenameException();
        if(fileTable.isDefined((StringValue) val))
            throw new FileAlreadyOpen();
        try{
            MyBufferedReader reader = new MyBufferedReader(new FileReader((String)val.getValue()));
            fileTable.add((StringValue) val, reader);
        } catch (FileNotFoundException e) {
            throw new InexistingFile();
        }
        return null;
    }

}
