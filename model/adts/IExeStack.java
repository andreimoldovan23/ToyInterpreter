package ToyInterpreter.model.adts;

import ToyInterpreter.exceptions.StackEmptyException;

public interface IExeStack<T> {
    void push(T element);
    T pop() throws StackEmptyException;
    String toString();
    boolean empty();
}
