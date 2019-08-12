package eu.michalbugno.ram;

import java.util.List;

public class Controller {
    private class Interpreter{
        private int argument;
        private Machine.ArgumentType type;
        private CommandsDictionary dictionary = new CommandsDictionary();

        Interpreter(){
            defineDefaultCommands();
        }

        void interpreteAndExecute(String command) throws
                InvalidArgumentException, UnknownCommandException, BadCommandFormatException{
            String[] splittedCommand = splitCommand(command);
            Runnable task = dictionary.getOperation(splittedCommand[0].toUpperCase());
            if(splittedCommand.length == 1){
                argument = 0;
                type = Machine.ArgumentType.ADDRESS;
            }
            else{
                interpreteArgument(splittedCommand[1]);
            }
            task.run();
        }

        private String[] splitCommand(String command) throws BadCommandFormatException{
            String[] splitted = command.split(" ");
            if(splitted.length > 2)
                throw new BadCommandFormatException("Too long!");
            return splitted;
        }

        private void interpreteArgument(String argument) throws InvalidArgumentException{
            char[] charactersArray = argument.toCharArray();
            try {
                if (Character.isDigit(charactersArray[0])) {
                    this.argument = Integer.parseInt(argument);
                    type = Machine.ArgumentType.ADDRESS;
                } else if (charactersArray[0] == '=') {
                    charactersArray[0] = '+';
                    this.argument = Integer.parseInt(new String(charactersArray));
                    type = Machine.ArgumentType.VALUE;
                } else if (charactersArray[0] == '^') {
                    charactersArray[0] = '+';
                    this.argument = Integer.parseInt(new String(charactersArray));
                    type = Machine.ArgumentType.INDIRECT_ADDRESS;
                } else
                    throw new InvalidArgumentException("Incorrect addressing type!");
            }
            catch(NumberFormatException n){
                throw new InvalidArgumentException("Must be a positive integer or zero!");
            }
        }

        private void defineDefaultCommands(){
            dictionary.defineCommand("ADD", ()->{
                ramMachine.add(argument, type);
            });
            dictionary.defineCommand("SUB", ()->{
                try {
                    ramMachine.subtract(argument, type);
                    checkIfCorrectResult();
                }catch(InvalidArgumentException i){
                    handleException(i);
                }
            });
            dictionary.defineCommand("MUL", ()->{
                ramMachine.multiply(argument, type);
            });
            dictionary.defineCommand("DIV", ()->{
                try{ramMachine.divide(argument, type);}
                catch(InvalidArgumentException i){
                    handleException(i);
                }
            });
            dictionary.defineCommand("READ", ()->{
                ramMachine.read();
            });
            dictionary.defineCommand("WRITE", ()->{
                ramMachine.read();
            });
            dictionary.defineCommand("LOAD", ()->{
               try{ ramMachine.load(argument, type);}
               catch(InvalidArgumentException i){
                   handleException(i);
               }
            });
            dictionary.defineCommand("STORE", ()->{
                try{ramMachine.store(argument, type);}
                catch(InvalidArgumentException i){
                    handleException(i);
                }
            });
        }
    }
    private Machine ramMachine;
    private int programCounter;
    private List<String> program;
    private Interpreter interpreter = new Interpreter();
    private UiController ui = UiController.getInstance();
    private boolean running = false;

    public Controller(){
        setActions();
    }

    public void executeProgram(){

    }

    public void executeNextStep(){
        try {
            interpreter.interpreteAndExecute(program.get(programCounter));
            programCounter++;
        }
        catch(Exception e){
            handleException(e);
        }
    }

    public void initializeExecution(){
        try {
            int[] input = getInputTapeFromString(ui.getInput());
            ramMachine = new Machine(input);
            programCounter = 0;
            program = ui.getCode();
        }
        catch(BadInputException b){
            handleException(b);
        }
    }

    private void setActions(){
        ui.setStartAction(this::initializeExecution);
        ui.setNextStepAction(this::executeNextStep);

    }

    private int[] getInputTapeFromString(String tape) throws BadInputException{
        String[] numbers = tape.split(" ");
        int[] result = new int[numbers.length];
        try {
            for (int i = 0; i < numbers.length; i++) {
                result[i] = Integer.parseInt(numbers[i]);
                if(result[i] < 0)
                    throw new NumberFormatException(); //Input values must be positive!
            }
        }
        catch(NumberFormatException n){
            throw new BadInputException("Input must contain only positive integers!");
        }
        return result;
    }

    private void handleException(Exception e){
        ui.showMessage(e.getMessage());
    }

    private void checkIfCorrectResult() throws InvalidArgumentException{
        if(ramMachine.peekAccumulator() < 0)
            throw new InvalidArgumentException("Negative result!");
    }

    //-------------------
    public static void main(String[] args) {

        }
    }
