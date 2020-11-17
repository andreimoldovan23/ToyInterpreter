package ToyInterpreter.model.adts;

import java.io.BufferedReader;
import java.io.Reader;

public class MyBufferedReader extends BufferedReader {

    private static int id = 0;
    private final int localId;

    public MyBufferedReader(Reader in) {
        super(in);
        id++;
        localId = id;
    }

    @Override
    public String toString(){
        return "BufferedReader, id: " + localId;
    }

}
