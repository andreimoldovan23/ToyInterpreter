package repository;

import exceptions.NoProgramsAvailableException;
import exceptions.NotInIntervalException;
import java.util.ArrayList;
import java.util.List;

public class Repo<T> implements IRepo<T>{

    private List<T> elements = new ArrayList<>();
    private int current = 0;

    public void add(T el){
        elements.add(el);
    }

    public void remove(int index) throws NoProgramsAvailableException, NotInIntervalException {
        try{
            elements.remove(index);
        }
        catch (IndexOutOfBoundsException e){
            if(elements.isEmpty())
                throw new NoProgramsAvailableException();
            throw new NotInIntervalException();
        }
    }

    public T getCurrent() throws NoProgramsAvailableException {
        try {
            return elements.get(current);
        }
        catch (IndexOutOfBoundsException e){
            throw new NoProgramsAvailableException();
        }
    }

    public void changeCurrent(int index) throws NoProgramsAvailableException, NotInIntervalException {
        if(elements.isEmpty())
            throw new NoProgramsAvailableException();
        if(index >= elements.size() || index < 0)
            throw new NotInIntervalException();
        current = index;
    }

    public List<T> getAll(){
        return elements;
    }

    public int size(){
        return elements.size();
    }
}
