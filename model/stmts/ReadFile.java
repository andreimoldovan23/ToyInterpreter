package model.stmts;

import exceptions.*;
import model.PrgState;
import model.adts.*;
import model.exps.Exp;
import model.types.Int;
import model.types.StringType;
import model.types.Type;
import model.values.IntValue;
import model.values.StringValue;
import model.values.Value;

import java.io.IOException;

public class ReadFile implements Stmt {

    private final Exp file;
    private final String varName;

    public ReadFile(Exp e, String s){
        file = e;
        varName = s;
    }

    public String toString(){
        return "readFile (" + file.toString() + ", " + varName + ") \n";
    }

    public String toStringPrefix(String prefix){
        return prefix + toString();
    }

    @SuppressWarnings("DuplicatedCode")
    public PrgState exec(PrgState state) throws ThreadException {
        ISymTable<String, Value> symTable = state.getTable();
        IHeap<Integer, Value> heap = state.getHeap();
        Value val;

        try{
            val = file.eval(symTable, heap);
        }
        catch (MyException e){
            throw new ThreadException(e, state.getId());
        }

        if(!val.getType().equals(new StringType()))
            throw new ThreadException(new InvalidFilename(), state.getId());
        if(!symTable.isDefined(varName))
            throw new ThreadException(new IsNotDefined(), state.getId());
        Value intVal = symTable.lookup(varName);
        if(!intVal.getType().equals(new Int()))
            throw new ThreadException(new InvalidFileReadType(), state.getId());

        IFileTable<StringValue, MyBufferedReader> fileTable = state.getFileTable();
        if(!fileTable.isDefined((StringValue) val))
            throw new ThreadException(new FileNotOpen(), state.getId());
        try{
            MyBufferedReader reader = fileTable.lookup((StringValue) val);
            if(reader == null)
                throw new ThreadException(new FileNotOpen(), state.getId());

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
            throw new ThreadException(new InexistingFile(), state.getId());
        }

        return null;
    }

    public ITypeEnv<String, Type> typeCheck(ITypeEnv<String, Type> typeEnv) throws MyException {
        if(!typeEnv.isDefined(varName))
            throw new IsNotDefined();
        Type varType = typeEnv.lookup(varName);
        Type expected = file.typeCheck(typeEnv);

        if(!varType.equals(new Int()))
            throw new InvalidFileReadType();
        if(!expected.equals(new StringType()))
            throw new InvalidFilename();

        return typeEnv;
    }

}
