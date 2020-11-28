package ToyInterpreter.model.adts;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.concurrent.atomic.AtomicInteger;

public class MyBufferedReader extends BufferedReader {

    private static AtomicInteger id = new AtomicInteger(0);
    private final int localId;

    public MyBufferedReader(Reader in) {
        super(in);
        localId = id.incrementAndGet();
    }

    @Override
    public String toString(){
        return "BufferedReader, id: " + localId;
    }

}
