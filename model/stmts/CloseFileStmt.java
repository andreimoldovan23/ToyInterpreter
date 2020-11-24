package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.FileNotOpen;
import ToyInterpreter.exceptions.InexistingFile;
import ToyInterpreter.exceptions.InvalidFilenameException;
import ToyInterpreter.exceptions.MyException;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IFileTable;
import ToyInterpreter.model.adts.MyBufferedReader;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.types.StringType;
import ToyInterpreter.model.values.StringValue;
import ToyInterpreter.model.values.Value;
import java.io.IOException;

public class CloseFileStmt implements Stmt {

    private final Exp file;

    public CloseFileStmt(Exp e){
        file = e;
    }

    public String toString(){
        return "closeFile (" + file.toString() + ")\n";
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

    public PrgState exec(PrgState state) throws MyException {
        Value val = file.eval(state.getTable(), state.getHeap());
        if (!val.getType().equals(new StringType()))
            throw new InvalidFilenameException();
        IFileTable<StringValue, MyBufferedReader> fileTable = state.getFileTable();
        if (!fileTable.isDefined((StringValue) val))
            throw new FileNotOpen();
        MyBufferedReader reader = fileTable.lookup((StringValue) val);
        try {
            reader.close();
        } catch (IOException e) {
            throw new InexistingFile();
        }
        fileTable.remove((StringValue) val);
        return null;
    }

}
