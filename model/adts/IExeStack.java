package model.adts;

import exceptions.StackEmpty;
import java.util.List;

public interface IExeStack<T> {
    void push(T element);
    T pop() throws StackEmpty;
    String toString();
    boolean empty();
    List<T> getAll();
}
