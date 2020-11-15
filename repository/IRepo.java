package repository;

import exceptions.MyException;
import java.util.List;

public interface IRepo<T> {
    void add(T el);
    void remove(int index) throws MyException;
    T getCurrent() throws MyException;
    void changeCurrent(int index) throws MyException;
    List<T> getAll();
    int size();
}
