package eu.michalbugno.ram;

import java.util.HashMap;
import java.util.Map;

public class CommandsDictionary {

    private Map<String, Runnable> dictionery = new HashMap<>();

    public Runnable getOperation(String name) throws UnknownCommandException{
        Runnable operation = dictionery.get(name);
        if(operation == null)
            throw new UnknownCommandException(name);
        return operation;
    }

    public void defineCommand(String name, Runnable operation){
        dictionery.put(name, operation);
    }

    public boolean isDefined(String name){
        return dictionery.containsKey(name);
    }
}
