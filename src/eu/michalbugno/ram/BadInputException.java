package eu.michalbugno.ram;

public class BadInputException extends Exception {
    public BadInputException(String message){
        super("Incorrect input!" + message);
    }
}
