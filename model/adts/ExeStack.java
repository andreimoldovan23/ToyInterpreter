package ToyInterpreter.model.adts;

import ToyInterpreter.exceptions.StackEmptyException;
import java.util.Stack;

public class ExeStack<T> implements IExeStack<T> {

    private final Stack<T> stk;

    public ExeStack() {
        stk = new Stack<>();
    }

    public void push(T el){
        stk.push(el);
    }

    public T pop()throws StackEmptyException {
        if(empty())
            throw new StackEmptyException();
        return stk.pop();
    }

    public boolean empty(){
        return stk.empty();
    }

    @SuppressWarnings("unchecked")
    public String toString(){
        StringBuilder stkStr = new StringBuilder();
        Stack<T> clone = (Stack<T>) stk.clone();
        while(!clone.empty()) {
            T el = clone.pop();
            String elStr = el.toString();
            elStr = elStr.substring(0, elStr.length()-1);
            stkStr.append("[").append(elStr).append("]\n");
        }
        return stkStr.toString();
    }

}
