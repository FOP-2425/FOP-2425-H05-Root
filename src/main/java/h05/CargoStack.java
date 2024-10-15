package h05;

import java.util.Stack;

public class CargoStack {
    private final Stack<Integer> stack = new Stack<>();
    private int sum = 0;

    public boolean empty(){
        return stack.empty();
    }

    public void push(int value){
        sum += value;
        stack.push(value);
    }

    public int pop(){
        int removedValue = stack.pop();
        sum -= removedValue;
        return removedValue;
    }

    public int getSum(){
        return sum;
    }
}
