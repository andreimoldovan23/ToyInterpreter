package ToyInterpreter.repository;

import java.io.IOException;
import java.util.List;

public interface IRepo<T> {
    T getCurrent();
    void setPrgList(List<T> l);
    void logCurrentPrg();
    void setLogFile(String filePath) throws IOException;
    List<T> getAll();
    void closeWriter();
}
