package ToyInterpreter.repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Repo<T> implements IRepo<T>{

    private List<T> elements;
    private PrintWriter printWriter;
    private String logPath;

    public Repo(T initial, String path) {
        elements = new ArrayList<>();
        elements.add(initial);
        logPath = path;
        try {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)), true);
        }
        catch (IOException e){
            printWriter = null;
        }
    }

    public T getCurrent() {
        return elements.get(0);
    }

    public void logCurrentPrg() {
        T currentElement = elements.get(0);
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        printWriter.println(date);
        printWriter.println(currentElement);
    }

    public void setLogFile(String path) throws IOException {
        logPath = path;
        closeWriter();
        printWriter = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)), true);
    }

    public List<T> getAll(){
        return elements;
    }

    public void setPrgList(List<T> l){
        elements.clear();
        elements = l;
    }

    public void closeWriter(){
        if(printWriter != null)
            printWriter.close();
    }

}
