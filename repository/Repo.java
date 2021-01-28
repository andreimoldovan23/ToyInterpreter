package repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
public class Repo<T> implements IRepo<T>{

    private List<T> elements;
    private PrintWriter printWriter;
    private String logPath;
    private String defaultPath = "log.txt";

    public Repo(T initial, String path) throws IOException {
        elements = new ArrayList<>();
        if(initial != null)
            elements.add(initial);
        try {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(path, true)), true);
            logPath = path;
        }
        catch (IOException e){
            logPath = defaultPath;
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(defaultPath, true)), true);
        }
    }

    public T getProgram(int x) {
        return elements.get(x);
    }

    public void logCurrentPrg(T element) {
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        printWriter.println(date);
        printWriter.println(element);
    }

    public void setLogFile(String path) throws IOException {
        logPath = path;
        closeWriter();
        try {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)), true);
        }
        catch (IOException e) {
            logPath = defaultPath;
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)), true);
        }
    }

    public List<T> getAll(){
        return elements;
    }

    public void setPrgList(List<T> l){
        elements = l;
    }

    public void closeWriter(){
        if(printWriter != null)
            printWriter.close();
    }

    public int getSize() {
        return elements.size();
    }

}
