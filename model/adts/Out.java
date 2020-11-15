package model.adts;

import java.util.ArrayList;
import java.util.List;

public class Out<T> implements IOut<T> {

    private List<T> text;

    public Out(){
        text = new ArrayList<>();
    }

    public void add(T el){
        text.add(el);
    }

    public String toString(){
        StringBuilder strBuilder = new StringBuilder();
        for(T el : text)
            strBuilder.append(el.toString()).append("\n");
        return strBuilder.toString();
    }

    public void clear() {
        text.clear();
    }

}