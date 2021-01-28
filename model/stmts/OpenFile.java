package model.stmts;

import exceptions.*;
import model.PrgState;
import model.adts.*;
import model.exps.Exp;
import model.types.StringType;
import model.types.Type;
import model.values.StringValue;
import model.values.Value;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenFile implements Stmt{

    private final Exp file;

    public OpenFile(Exp e){
        file = e;
    }

    public String toString(){
        return "openFile (" + file.toString() + ") \n";
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
            throw new ThreadException(new InvalidFilename(), state.getId());
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
            throw new InvalidFilename();
        return typeEnv;
    }

}
