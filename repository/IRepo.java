package ToyInterpreter.repository;

import java.io.IOException;
import java.util.List;

public interface IRepo<T> {
    T getMainProgram();
    void setPrgList(List<T> l);
    void logCurrentPrg(T element);
    void setLogFile(String filePath) throws IOException;
    List<T> getAll();
    void closeWriter();
}
