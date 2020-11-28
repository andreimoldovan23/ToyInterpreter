package ToyInterpreter.model.stmts;

import ToyInterpreter.exceptions.*;
import ToyInterpreter.model.PrgState;
import ToyInterpreter.model.adts.*;
import ToyInterpreter.model.exps.Exp;
import ToyInterpreter.model.types.StringType;
import ToyInterpreter.model.types.Type;
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

    @SuppressWarnings("DuplicatedCode")
    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> table = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();
        IFileTable<StringValue, MyBufferedReader> fileTable = state.getFileTable();
        Value val;
        try {
            val = file.eval(table, heap);
        }
        catch (MyException e){
            throw new ThreadException(e, state.getId());
        }

        if(!val.getType().equals(new StringType()))
            throw new ThreadException(new InvalidFilenameException(), state.getId());
        if(fileTable.isDefined((StringValue) val))
            throw new ThreadException(new FileAlreadyOpen(), state.getId());
        try{
            MyBufferedReader reader = new MyBufferedReader(new FileReader((String)val.getValue()));
            fileTable.add((StringValue) val, reader);
        } catch (FileNotFoundException e) {
            throw new ThreadException(new InexistingFile(), state.getId());
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
