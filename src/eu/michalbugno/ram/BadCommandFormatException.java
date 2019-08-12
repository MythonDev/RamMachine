package eu.michalbugno.ram;

public class BadCommandFormatException extends Exception {
    public BadCommandFormatException(String message){
        super("Bad Command Format! "+message);
    }
}
