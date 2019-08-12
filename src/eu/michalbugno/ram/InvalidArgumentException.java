package eu.michalbugno.ram;

public class InvalidArgumentException extends Exception {
    public InvalidArgumentException(String additionalMessage){
        super("Invalid argument! "+additionalMessage);
    }
}
