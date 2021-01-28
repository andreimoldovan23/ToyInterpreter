package model.stmts;

import exceptions.*;
import model.PrgState;
import model.adts.IFileTable;
import model.adts.ITypeEnv;
import model.adts.MyBufferedReader;
import model.exps.Exp;
import model.types.StringType;
import model.types.Type;
import model.values.StringValue;
import model.values.Value;
import java.io.IOException;

public class CloseFile implements Stmt {

    private final Exp file;

    public CloseFile(Exp e){
        file = e;
    }

    public String toString(){
        return "closeFile (" + file.toString() + ") \n";
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
            throw new ThreadException(new InvalidFilename(), state.getId());
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
            throw new InvalidFilename();
        return typeEnv;
    }

}
