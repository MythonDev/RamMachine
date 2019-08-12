package eu.michalbugno.ram;

import java.util.*;

public class Machine {
    private static class Memory{
        private Map<Integer, Integer> memory = new HashMap<>();

        Memory(){
            memory.put(0, 0);
        }

        @Override
        public String toString(){
            StringBuilder buffer = new StringBuilder("0: "+memory.get(0)+" (ACC)\n\n");
            Object[] keys = memory.keySet().toArray();
            Integer[] intKeys = new Integer[keys.length];
            for(int i = 0; i<keys.length; i++){
                intKeys[i] = (Integer) keys[i];
            }
            for(int i=1; i<intKeys.length; i++)
                buffer.append(intKeys[i]+": "+memory.get(intKeys[i])+"\n");
            return buffer.toString();
        }

        void store(int index){
            memory.put(index, peek());
        }
        void load(int index){
            int value = peek(index);
            setAccumulatorValue(value);
        }

        int peek(int index){
            Integer value =  memory.get(index);
            if(value==null)
                return 0;
            return value;
        }

        int peek(){
            return peek(0);
        }

        void setAccumulatorValue(int value){
            memory.put(0, value);
        }

    }

    // ***** End of Memory class *****

    public enum ArgumentType{ VALUE, ADDRESS, INDIRECT_ADDRESS}

    private Memory memory = new Memory();
    private Queue<Integer> inTape = new ArrayDeque<>();
    private List<Integer> result = new LinkedList<>();

    public Machine(int...input){
        for(int i: input){
            inTape.add(i);
        }
    }

    public void add(int number, ArgumentType type){
        int value = getValue(number, type);
        value += memory.peek();
        memory.setAccumulatorValue(value);
    }

    public void subtract(int number, ArgumentType type){
        int value = memory.peek();
        value -= getValue(number, type);
        memory.setAccumulatorValue(value);
    }

    public void multiply(int number, ArgumentType type){
        int value = getValue(number, type);
        value *= memory.peek();
        memory.setAccumulatorValue(value);
    }

    public void divide(int number, ArgumentType type) throws InvalidArgumentException{
        int value = memory.peek();
        int operand = getValue(number, type);
        if(operand == 0){
            throw new InvalidArgumentException("Cannot devide by zero!");
        }
        value /= operand;
        memory.setAccumulatorValue(value);
    }

    public void store(int number, ArgumentType type) throws InvalidArgumentException{
        int target = getAddress(number, type);
        memory.store(target);
    }

    public void load(int number, ArgumentType type) throws InvalidArgumentException{
        int source = getAddress(number, type);
        memory.load(source);
    }

    public void read(){
        Integer value = inTape.poll();
        if(value == null)
            value = 0;
        memory.setAccumulatorValue(value);
    }

    public void write(){
        Integer value = memory.peek();
        result.add(value);
    }



    public String getMemoryState(){
        return memory.toString();
    }

    public int peekAccumulator(){
        return memory.peek();
    }

    private int getValue(int value, ArgumentType type) {
        int result = 0;
        switch (type) {
            case ADDRESS:
                result = memory.peek(value);
                break;
            case INDIRECT_ADDRESS:
                result = memory.peek(memory.peek(value));
                break;
            case VALUE:
                result =  value;
        }
        return result;
    }

    private int getAddress(int value, ArgumentType type) throws InvalidArgumentException{
        int result = 0;
        switch(type){
            case VALUE:
                throw new InvalidArgumentException("Address required!");
            case ADDRESS:
                result = value;
                break;
            case INDIRECT_ADDRESS:
                result = memory.peek(value);
        }
        return result;
    }

    //------------------------
    public static void main(String[] args) {

    }

}
