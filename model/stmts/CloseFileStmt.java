package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.IFileTable;
import ToyInterpreter.model.adts.ITypeEnv;
import ToyInterpreter.model.adts.MyBufferedReader;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.types.StringType;
import ToyInterpreter.model.types.Type;
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

    public PrgState exec(PrgState state) throws ThreadException {
        Value val;
        try{
            val = file.eval(state.getTable(), state.getHeap());
        }
        catch (MyException e){
            throw new ThreadException(e, state.getId());
        }

        if (!val.getType().equals(new StringType()))
            throw new ThreadException(new InvalidFilenameException(), state.getId());
        IFileTable<StringValue, MyBufferedReader> fileTable = state.getFileTable();
        if (!fileTable.isDefined((StringValue) val))
            throw new ThreadException(new FileNotOpen(), state.getId());

        MyBufferedReader reader = fileTable.lookup((StringValue) val);
        try {
            reader.close();
        } catch (IOException e) {
            throw new ThreadException(new InexistingFile(), state.getId());
        }

        try {
            fileTable.remove((StringValue) val);
        }
        catch (MyException e){
            throw new ThreadException(e, state.getId());
        }

        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        Type t = file.typeCheck(typeEnv);
        if(!t.equals(new StringType()))
            throw new InvalidFilenameException();
        return typeEnv;
    }

}
