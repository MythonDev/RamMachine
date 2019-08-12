package eu.michalbugno.ram;

public class UnknownCommandException extends Exception{

    public UnknownCommandException(String unknownName){
        super("Unknown command: " + unknownName + "!");
    }
}
